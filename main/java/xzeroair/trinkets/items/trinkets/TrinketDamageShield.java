package xzeroair.trinkets.items.trinkets;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.VIPHandler;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.items.effects.EffectsDamageShield;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.AttributeHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class TrinketDamageShield extends AccessoryBase {

	public TrinketDamageShield(String name) {
		super(name);
	}

	protected final UUID uuid = UUID.fromString("c0885371-20dd-4c56-86eb-78f24d9fe777");
	protected ResourceLocation background = null;
	protected boolean hasResist = false;

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, world, tooltip, flagIn);
		TranslationHelper.addOtherTooltips(stack, world, TrinketsConfig.SERVER.DAMAGE_SHIELD.Attributes, tooltip);
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		AttributeHelper.handleAttributes(player, TrinketsConfig.SERVER.DAMAGE_SHIELD.Attributes, uuid);
		if (!player.isPotionActive(MobEffects.RESISTANCE)) {
			player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 200, 0, false, false));
			hasResist = false;
		}
		if (player.isPotionActive(MobEffects.RESISTANCE)) {
			final int dur = player.getActivePotionEffect(MobEffects.RESISTANCE).getDuration();
			final int amp = player.getActivePotionEffect(MobEffects.RESISTANCE).getAmplifier();
			if ((dur < 200) && !(amp > 0) && (hasResist == false)) {
				player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 200, 0, false, false));
				hasResist = false;
			}
			if (((dur > 200) && (amp < 2)) && (hasResist == false)) {
				player.getActivePotionEffect(MobEffects.RESISTANCE).combine(new PotionEffect(MobEffects.RESISTANCE, dur + 1, amp + 1, false, false));
				hasResist = true;
			}
		}
	}

	@Override
	public void eventPlayerHurt(LivingHurtEvent event, ItemStack stack, EntityLivingBase player) {
		EffectsDamageShield.eventPlayerHurt(event, stack, player);
	}

	@Override
	public void eventLivingDamageAttacked(LivingDamageEvent event, ItemStack stack, EntityLivingBase player) {
		EffectsDamageShield.eventLivingDamageAttacked(event, stack, player);
	}

	@Override
	public boolean playerCanEquip(ItemStack stack, EntityLivingBase player) {
		if (TrinketHelper.AccessoryCheck(player, stack.getItem())) {
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
				AttributeHelper.handleAttributes(player, TrinketsConfig.SERVER.DAMAGE_SHIELD.Attributes, uuid);
			}
		}
	}

	@Override
	public void playerUnequipped(ItemStack stack, EntityLivingBase player) {
		if (!TrinketHelper.AccessoryCheck(player, stack.getItem())) {
			player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 2f);
			if (player.isPotionActive(MobEffects.RESISTANCE)) {
				player.removeActivePotionEffect(MobEffects.RESISTANCE);
				player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 30, 0, false, false));
				hasResist = false;
			}
		}
		AttributeHelper.removeAttributes(player, uuid);
		super.playerUnequipped(stack, player);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void playerRender(ItemStack stack, EntityLivingBase player, float partialTicks, boolean isBauble) {
		if (!TrinketsConfig.CLIENT.DAMAGE_SHIELD.doRender) {
			return;
		}
		final float scale = 0.2F;
		GlStateManager.pushMatrix();
		GlStateManager.rotate(180F, 1F, 0F, 0F);
		GlStateManager.translate(0.15F, -0.22F, 0.14F);
		if (player.isSneaking()) {
			GlStateManager.translate(0F, -0.24F, -0.07F);
			GlStateManager.rotate(90F / (float) Math.PI, 1.0F, 0.0F, 0.0F);
		}
		if (player.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
			GlStateManager.translate(0F, 0F, 0.06F);
		}
		GlStateManager.scale(scale, scale, scale);
		Minecraft.getMinecraft().getRenderItem().renderItem(this.getDefaultInstance(), ItemCameraTransforms.TransformType.NONE);
		GlStateManager.popMatrix();

		if (VIPHandler.CheckPlayerVIPStatus(player.getUniqueID(), VIPHandler.getBro())) {
			background = new ResourceLocation("xat:textures/awesomesauce/damage_shield_bro.png");
		} else if (VIPHandler.CheckPlayerVIPStatus(player.getUniqueID(), VIPHandler.getPanda())) {
			background = new ResourceLocation("xat:textures/awesomesauce/damage_shield_panda.png");
		} else if (VIPHandler.CheckPlayerVIPStatus(player.getUniqueID(), VIPHandler.getVIP())) {
			background = new ResourceLocation("xat:textures/awesomesauce/damage_shield_vip.png");
		} else {
			background = null;
		}
		if (background != null) {
			GlStateManager.pushMatrix();
			if (player.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
				GlStateManager.translate(0F, 0F, -0.07F);
			}
			GlStateManager.translate(0, 0, -0.95F);
			Minecraft.getMinecraft().renderEngine.bindTexture(background);
			final float size = 0.165f;
			Draw(0.065, 0.14, 0.8, 0, 0, size, size, 6.0f, 1, 1, 1, 1);
			GlStateManager.popMatrix();
		}
	}

	public static void Draw(double x, double y, double z, int texX, int texY, float width, float height, float scale, float r, float g, float b, float a) {
		GlStateManager.pushMatrix();
		// GlStateManager.color(r, g, b, a);
		final int l1 = 240;
		final int l2 = 0;
		final Tessellator tes = Tessellator.getInstance();
		final BufferBuilder buffer = tes.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		// Bottom Left
		buffer.pos(x + 0, y + height, z).tex((texX + 0) * scale, (texY + height) * scale).color(r, g, b, a).endVertex();
		// buffer.pos(x + 0, y + height, 0).color(r, g, b, a).lightmap(l1,
		// l2).endVertex();
		// bottom right
		buffer.pos(x + width, y + height, z).tex((texX + width) * scale, (texY + height) * scale).color(r, g, b, a).endVertex();
		// buffer.pos(x + width, y + height, 0).color(r, g, b, a).lightmap(l1,
		// l2).endVertex();
		// top right
		buffer.pos(x + width, y + 0, z).tex((texX + width) * scale, (texY + 0) * scale).color(r, g, b, a).endVertex();
		// buffer.pos(x + width, y + 0, 0).color(r, g, b, a).lightmap(l1,
		// l2).endVertex();
		// top left
		buffer.pos(x + 0, y + 0, z).tex((texX + 0) * scale, (texY + 0) * scale).color(r, g, b, a).endVertex();
		// buffer.pos(x + 0, y + 0, 0).color(r, g, b, a).lightmap(l1, l2).endVertex();
		tes.draw();
		GlStateManager.popMatrix();
	}

	@Override
	public boolean ItemEnabled() {
		return TrinketsConfig.SERVER.DAMAGE_SHIELD.enabled;
	}

	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}