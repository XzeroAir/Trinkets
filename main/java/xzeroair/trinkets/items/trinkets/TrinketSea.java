package xzeroair.trinkets.items.trinkets;

import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.attributes.swim.SwimAttribute;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.betterdiving.BetterDivingCompat;
import xzeroair.trinkets.util.compat.toughasnails.TANCompat;
import xzeroair.trinkets.util.handlers.ClimbHandler;

public class TrinketSea extends AccessoryBase {

	public TrinketSea(String name) {
		super(name);
	}

	private final UUID uuid = UUID.fromString("6029aecd-318e-4b45-8c36-2ddd7f481e36");

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		SwimAttribute.addModifier(player, TrinketsConfig.SERVER.SEA_STONE.speed, uuid, 0);
		if(TrinketsConfig.SERVER.SEA_STONE.underwater_breathing) {
			if(!TrinketsConfig.SERVER.SEA_STONE.always_full) {
				if (player.getAir() < 20) {
					player.setAir(20);
					BetterDivingCompat.setOxygen(player, 100);
				}
			} else {
				player.setAir(300);
				BetterDivingCompat.setOxygen(player, 300);
			}
		}
		if(TrinketsConfig.SERVER.SEA_STONE.prevent_thirst) {
			TANCompat.clearThirst(player);
		}
		if(TrinketsConfig.SERVER.SEA_STONE.C06_Sea_Stone_Swim_Tweaks == true) {
			final BlockPos head = player.getPosition();
			final IBlockState headBlock = player.world.getBlockState(head);
			final Block block = headBlock.getBlock();
			if((player.isInWater() || player.isInLava()) && (block != Blocks.AIR)) {

				double motion = 0.1;
				final double bouyance = 0.25;
				if(player.isInLava()) {
					motion = 0.09;
				}
				if(!player.isSneaking()) {
					player.motionY = 0f;
					if((ClimbHandler.movingForward(player, player.getHorizontalFacing()) == true)) {
						if(((player.motionX > motion) || (player.motionX < -motion)) || ((player.motionZ > motion) || (player.motionZ < -motion))) {
							player.motionY += MathHelper.clamp(player.getLookVec().y/1, -bouyance, bouyance);
						}
					}
				} else {
					if((ClimbHandler.movingForward(player, player.getHorizontalFacing()) == false)) {
						if(!(player.motionY > 0)) {
							if(player.isInLava()) {
								player.motionY *= 1.75;
							} else {
								player.motionY *= 1.25;
							}
						} else {

						}

					}
				}
			}
		}
	}

	@Override
	public void eventPlayerLogout(ItemStack stack, EntityLivingBase player) {
		SwimAttribute.removeModifier(player, uuid);
	}

	@Override
	public boolean playerCanEquip(ItemStack stack, EntityLivingBase player) {
		if(TrinketHelper.AccessoryCheck(player, stack.getItem())) {
			return false;
		} else {
			return super.playerCanEquip(stack, player);
		}
	}
	@Override
	public void playerEquipped(ItemStack stack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 1.9f);
		super.playerEquipped(stack, player);
	}
	@Override
	public void playerUnequipped(ItemStack stack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 2f);
		SwimAttribute.removeModifier(player, uuid);
		super.playerUnequipped(stack, player);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void playerRender(ItemStack stack, EntityLivingBase player, float partialTicks, boolean isBauble) {
		if(!TrinketsConfig.CLIENT.SEA_STONE.doRender) {
			return;
		}

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

	@Override
	public boolean ItemEnabled() {
		return TrinketsConfig.SERVER.SEA_STONE.enabled;
	}

	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}
