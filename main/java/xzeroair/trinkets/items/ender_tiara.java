package xzeroair.trinkets.items;

import baubles.api.BaubleType;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.items.base.BaubleBase;
import xzeroair.trinkets.util.helpers.CallHelper;

public class ender_tiara extends BaubleBase {

	public ender_tiara(String name) {
		super(name);
	}
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.HEAD;
	}
	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
	}
	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		if((EnchantmentHelper.getEnchantments(book).containsKey(Enchantments.BINDING_CURSE))){
			return true;
		}
		return false;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks) {
		if(type == RenderType.HEAD) {
			final ModelBase tiara = CallHelper.getModel("tiara");
			final float scale = 0.07f;
			GlStateManager.pushMatrix();
			GlStateManager.translate(-0.05, -0.63, 0);
			if (player.isSneaking()) {
				GlStateManager.translate(0.25F * MathHelper.sin((player.rotationPitch * (float) Math.PI) / 180), 0.25F * MathHelper.cos((player.rotationPitch * (float) Math.PI) / 180), 0F);
			}
			if(player.hasItemInSlot(EntityEquipmentSlot.HEAD)) {
				GlStateManager.translate(0.04F, -0.06F, 0F);
				GlStateManager.scale(1.1F, 1.1F, 1.1F);
			}
			GlStateManager.scale(scale, scale, scale);
			tiara.render(player, player.limbSwing, player.limbSwingAmount, player.ticksExisted, player.rotationYaw, player.rotationPitch, 1F);
			GlStateManager.popMatrix();
		}
	}
	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}
