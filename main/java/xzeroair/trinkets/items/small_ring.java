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
import xzeroair.trinkets.Main;
import xzeroair.trinkets.compatibilities.sizeCap.CapPro;
import xzeroair.trinkets.compatibilities.sizeCap.ICap;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.BaubleBase;
import xzeroair.trinkets.util.helpers.TrinketHelper;

public class small_ring extends BaubleBase {

	public small_ring(String name) {
		super(name);
	}
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.RING;
	}
	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		final EntityPlayer p = (EntityPlayer) player;
		if(p.hasCapability(CapPro.sizeCapability, null)) {
			final ICap nbt = p.getCapability(CapPro.sizeCapability, null);
			if(nbt.getTrans() == true) {
				if(nbt.getSize() == nbt.getTarget()) {
					if(!p.isCreative() && (p.capabilities.allowFlying != true)) {
						p.capabilities.allowFlying = true;
					}
				}
			}
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
		final EntityPlayer p = (EntityPlayer) player;
		if(p.hasCapability(CapPro.sizeCapability, null)) {
			final ICap cap = p.getCapability(CapPro.sizeCapability, null);
			cap.setTrans(true);
			cap.setTarget(25);
		}
		super.onEquipped(itemstack, player);
	}
	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
		final EntityPlayer p = (EntityPlayer) player;
		if(p.hasCapability(CapPro.sizeCapability, null)) {
			final ICap cap = p.getCapability(CapPro.sizeCapability, null);
			if(!p.isCreative()) {
				p.capabilities.isFlying = false;
				p.capabilities.allowFlying = false;
			}
			cap.setTrans(false);
			cap.setTarget(100);
		}
		super.onUnequipped(itemstack, player);
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
			final ModelBase wings = Main.proxy.getModel("wings");
			if(player.hasCapability(CapPro.sizeCapability, null)) {
				final ICap cap = player.getCapability(CapPro.sizeCapability, null);
				final float size = (float)cap.getSize()/1000;
				final float scale = MathHelper.clamp((0.1F-size), 0.0F, 0.05F);
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
}
