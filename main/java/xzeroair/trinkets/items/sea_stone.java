package xzeroair.trinkets.items;

import baubles.api.BaubleType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.attributes.swim.SwimAttribute;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.BaubleBase;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.handlers.ClimbHandler;
import xzeroair.trinkets.util.helpers.TrinketHelper;

public class sea_stone extends BaubleBase {

	public sea_stone(String name) {
		super(name);
	}
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.AMULET;
	}

	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {

	}

	public static void playerTick(EntityLivingBase player) {
		if(TrinketHelper.baubleCheck((EntityPlayer) player, ModItems.sea_stone)) {
			SwimAttribute.addModifier(player, 4D, 0);
			if (player.getAir() < 30) {
				player.setAir(20);
			}
			if(TrinketsConfig.SERVER.C06_Sea_Stone_Swim_Tweaks == true) {
				final BlockPos head = player.getPosition();
				final IBlockState headBlock = player.world.getBlockState(head);
				final Block block = headBlock.getBlock();
				if(player.isInWater() && (block != Blocks.AIR)) {
					if(!player.isSneaking()) {
						player.motionY = 0f;
						if((ClimbHandler.movingForward(player, player.getHorizontalFacing()) == true)) {
							player.motionY = MathHelper.clamp(player.getLookVec().y/1, -0.1, 0.4);
						}
					}
				}
			}
		}
	}

	public static void playerLogout(EntityLivingBase player) {
		SwimAttribute.removeModifier(player);
	}

	@Override
	public void onEquipped(ItemStack par1ItemStack, EntityLivingBase player) {
	}

	@Override
	public void onUnequipped(ItemStack par1ItemStack, EntityLivingBase player) {
		SwimAttribute.removeModifier(player);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks) {
		if (type == RenderType.BODY) {
			final float scale = 0.2F;
			GlStateManager.pushMatrix();
			GlStateManager.rotate(180F, 1F, 0F, 0F);
			GlStateManager.translate(0F, -0.12F, 0.14F);
			if(player.isSneaking()) {
				GlStateManager.translate(0F, -0.24F, -0.07F);
				GlStateManager.rotate(90F / (float) Math.PI, 1.0F, 0.0F, 0.0F);
			}
			if (player.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
				GlStateManager.translate(0F, 0F, 0.06F);
			}
			GlStateManager.scale(scale, scale, scale);
			Minecraft.getMinecraft().getRenderItem().renderItem(getDefaultInstance(), ItemCameraTransforms.TransformType.NONE);
			GlStateManager.popMatrix();

		}
	}
	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}
