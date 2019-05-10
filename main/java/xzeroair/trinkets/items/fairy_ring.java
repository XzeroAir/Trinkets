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
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.sizeCap.ISizeCap;
import xzeroair.trinkets.capabilities.sizeCap.SizeCapPro;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.BaubleBase;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.artemislib.SizeAttribute;
import xzeroair.trinkets.util.helpers.CallHelper;
import xzeroair.trinkets.util.helpers.TrinketHelper;

public class fairy_ring extends BaubleBase {

	public fairy_ring(String name) {
		super(name);
	}
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.RING;
	}
	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		final EntityPlayer p = (EntityPlayer) player;
		if(TrinketsConfig.SERVER.C00_Creative_Flight == true) {
			if(p.getCapability(SizeCapPro.sizeCapability, null) != null) {
				final ISizeCap nbt = p.getCapability(SizeCapPro.sizeCapability, null);
				if(nbt.getTrans() == true) {
					if(nbt.getSize() == nbt.getTarget()) {
						if(!p.isCreative() && (p.capabilities.allowFlying != true)) {
							p.capabilities.allowFlying = true;
						}
						if((Loader.isModLoaded("artemislib"))) {
							SizeAttribute.addModifier(player, -0.75, -0.75, 0);
						}
					}
				}
			}
		}
	}
	public static void playerJump(EntityPlayer player) {
		if((TrinketHelper.baubleCheck(player, ModItems.fairy_ring)) && ((TrinketsConfig.SERVER.C01_Step_Height != false))){
			player.motionY = player.motionY*0.5f;
		}
	}
	public static void onPlayerLogout(EntityPlayer player) {
		if(TrinketHelper.baubleCheck(player, ModItems.fairy_ring)) {
		}
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		if((EnchantmentHelper.getEnchantments(book).containsKey(Enchantments.BINDING_CURSE))){
			return true;
		}
		return false;
	}
	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {

		if(player.getCapability(SizeCapPro.sizeCapability, null) != null) {
			final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);
			cap.setTrans(true);
			cap.setTarget(25);
		}
		//		super.onEquipped(itemstack, player);
	}
	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {

		if(player.getCapability(SizeCapPro.sizeCapability, null) != null) {
			final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);
			if(!((EntityPlayer) player).isCreative() && (TrinketsConfig.SERVER.C00_Creative_Flight == true)) {
				((EntityPlayer) player).capabilities.isFlying = false;
				((EntityPlayer) player).capabilities.allowFlying = false;
			}
			cap.setTrans(false);
			cap.setTarget(100);
		}
		if((Loader.isModLoaded("artemislib"))) {
			SizeAttribute.removeModifier(player);
		}
		//		super.onUnequipped(itemstack, player);
	}
	@Override
	public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
		if(itemstack.getItemDamage()==0) {
			if(TrinketHelper.baubleCheck((EntityPlayer)player, ModItems.dwarf_ring)) {
				return false;
			}
		}
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks) {
		if(type == RenderType.BODY) {
			final ModelBase wings = CallHelper.getModel("wings");
			if(player.getCapability(SizeCapPro.sizeCapability, null) != null) {
				final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);
				final float size = (float)cap.getSize()/1000;
				final float scale = MathHelper.clamp((0.1F-size), 0.0F, 0.06F);
				GlStateManager.pushMatrix();
				GlStateManager.translate(0.02F, -(scale-0.1F), 0.0F);
				if(player.hasItemInSlot(EntityEquipmentSlot.CHEST))
				{
					GlStateManager.translate(0F, -0.1F, 0.08F);
					GlStateManager.scale(1.1F, 1.1F, 1.1F);
				}
				if(player.isSneaking()) {
					GlStateManager.translate(0F, 0.2F, 0F);
					GlStateManager.rotate(90F / (float) Math.PI, 1.0F, 0.0F, 0.0F);
				}
				if(player.isSprinting())
				{
					GlStateManager.rotate(45F / (float) Math.PI, 1.0F, 0.0F, 0.0F);
				}
				GlStateManager.scale(scale, scale, scale);
				wings.render(player, player.limbSwing, player.limbSwingAmount, player.ticksExisted, player.rotationYaw, player.rotationPitch, 1);
				GlStateManager.popMatrix();
			}
		}
	}
	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}
