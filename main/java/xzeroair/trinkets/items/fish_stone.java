package xzeroair.trinkets.items;

import baubles.api.BaubleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.items.base.BaubleBase;

public class fish_stone extends BaubleBase {

	public fish_stone(String name) {
		super(name);
	}
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.AMULET;
	}

	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		if (itemstack.getItemDamage() == 0) {
			if (player.getAir() < 30) {
				player.setAir(20);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks) {
		if (type == RenderType.BODY) {
			final float scale = 0.2F;
			GlStateManager.pushMatrix();
			GlStateManager.rotate(180F, 1F, 0F, 0F);
			GlStateManager.translate(0F, -0.12F, 0.13F);
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
}
