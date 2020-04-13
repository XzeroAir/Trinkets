package xzeroair.trinkets.items.trinkets;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.toughasnails.TANCompat;
import xzeroair.trinkets.util.helpers.AttributeHelper;
import xzeroair.trinkets.util.helpers.CallHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class TrinketEnderTiara extends AccessoryBase {

	public TrinketEnderTiara(String name) {
		super(name);
	}

	private static UUID uuid = UUID.fromString("a45dbc1c-17e9-40b4-b6a3-09dea74355b7");

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		TranslationHelper.addOtherTooltips(stack, worldIn, TrinketsConfig.SERVER.ENDER_CROWN.Attributes, tooltip);
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		if (TrinketsConfig.SERVER.ENDER_CROWN.water_hurts) {
			if ((player.isInWater() || player.isWet())) {
				player.attackEntityFrom(DamageSource.GENERIC, 2);
			}
		}

		if (TrinketsConfig.SERVER.ENDER_CROWN.compat.tan.immuneToCold) {
			TANCompat.immuneToCold(player);
		}
	}

	@Override
	public boolean playerCanEquip(ItemStack stack, EntityLivingBase player) {
		if (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDragonsEye) || TrinketHelper.AccessoryCheck(player, stack.getItem())) {
			return false;
		} else {
			return super.playerCanEquip(stack, player);
		}
	}

	@Override
	public void eventPlayerLogout(ItemStack stack, EntityLivingBase player) {
		AttributeHelper.removeAttributes(player, uuid);
	}

	@Override
	public void playerEquipped(ItemStack stack, EntityLivingBase player) {
		TrinketProperties cap = Capabilities.getTrinketProperties(stack);
		if ((cap != null)) {
			if (!(cap.Slot() == -1)) {
				super.playerEquipped(stack, player);
			} else {
				player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 1.9f);
				AttributeHelper.handleAttributes(player, TrinketsConfig.SERVER.ENDER_CROWN.Attributes, uuid);
			}
		}
	}

	@Override
	public void playerUnequipped(ItemStack stack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 2f);
		AttributeHelper.removeAttributes(player, uuid);
		super.playerUnequipped(stack, player);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void playerRender(ItemStack stack, EntityLivingBase player, float partialTicks, boolean isBauble) {

		if (!TrinketsConfig.CLIENT.ENDER_CROWN.doRender) {
			return;
		}
		final ModelBase tiara = CallHelper.getModel("tiara");
		final float scale = 0.07f;

		final float yaw = player.prevRotationYawHead + ((player.rotationYawHead - player.prevRotationYawHead) * partialTicks);
		final float yawOffset = player.prevRenderYawOffset + ((player.renderYawOffset - player.prevRenderYawOffset) * partialTicks);
		final float pitch = player.prevRotationPitch + ((player.rotationPitch - player.prevRotationPitch) * partialTicks);

		GlStateManager.rotate(yawOffset, 0, -1, 0);
		GlStateManager.rotate(yaw - 270, 0, 1, 0);
		GlStateManager.rotate(pitch, 0, 0, 1);

		GlStateManager.pushMatrix();
		GlStateManager.translate(-0.05, -0.63, 0);
		if (player.isSneaking()) {
			GlStateManager.translate(0.25F * MathHelper.sin((player.rotationPitch * (float) Math.PI) / 180), 0.25F * MathHelper.cos((player.rotationPitch * (float) Math.PI) / 180), 0F);
		}
		if (player.hasItemInSlot(EntityEquipmentSlot.HEAD)) {
			GlStateManager.translate(0.04F, -0.08F, 0F);
			GlStateManager.scale(1.1F, 1.1F, 1.1F);
		}
		GlStateManager.scale(scale, scale, scale);
		tiara.render(player, player.limbSwing, player.limbSwingAmount, player.ticksExisted, player.rotationYaw, player.rotationPitch, 1F);
		GlStateManager.popMatrix();
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		if ((EnchantmentHelper.getEnchantments(book).containsKey(Enchantments.BINDING_CURSE))) {
			return true;
		}
		return false;
	}

	@Override
	public boolean ItemEnabled() {
		return TrinketsConfig.SERVER.ENDER_CROWN.enabled;
	}

	@Override
	public boolean hasDiscription(ItemStack stack) {
		return false;
	}
}
