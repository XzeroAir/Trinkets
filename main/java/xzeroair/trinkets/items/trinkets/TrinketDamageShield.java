package xzeroair.trinkets.items.trinkets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.capabilities.Vip.VipStatus;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.items.effects.EffectsDamageShield;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsConfig.xClient.TrinketItems.Shield;
import xzeroair.trinkets.util.config.trinkets.ConfigDamageShield;
import xzeroair.trinkets.util.handlers.TickHandler;

public class TrinketDamageShield extends AccessoryBase {

	public static final ConfigDamageShield serverConfig = TrinketsConfig.SERVER.Items.DAMAGE_SHIELD;
	public static final Shield clientConfig = TrinketsConfig.CLIENT.items.DAMAGE_SHIELD;

	public TrinketDamageShield(String name) {
		super(name);
		this.setUUID("c0885371-20dd-4c56-86eb-78f24d9fe777");
		this.setItemAttributes(serverConfig.Attributes);
		ModItems.trinkets.ITEMS.add(this);
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		super.eventPlayerTick(stack, player);
		int duration = 30;
		TrinketProperties cap = Capabilities.getTrinketProperties(stack);
		if (cap != null) {
			if (!player.isPotionActive(MobEffects.RESISTANCE)) {
				player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, duration, serverConfig.resistance_level, false, false));
				cap.removeCounter("CheckResist");
			} else {
				final int dur = player.getActivePotionEffect(MobEffects.RESISTANCE).getDuration();
				final int amp = player.getActivePotionEffect(MobEffects.RESISTANCE).getAmplifier();
				TickHandler counter = cap.getCounter("CheckResist");
				boolean trigger = counter.Tick();
				if (trigger) {
					cap.removeCounter("CheckResist");
				}
				if ((dur > duration) && serverConfig.resistance_stacks) {
					//Assume it was given by something else
					if ((amp <= serverConfig.resistance_level)) {
						player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, duration, amp + 1, false, false));
					} else {
						if (trigger && ((amp + 1) < 3)) {
							player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, dur, amp + 1, false, false));
						}
					}
				} else {
					if ((amp <= serverConfig.resistance_level)) {
						player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, duration, serverConfig.resistance_level, false, false));
					}
				}
			}
		}
	}

	@Override
	public void eventPotionApplicable(PotionApplicableEvent event, ItemStack stack, EntityLivingBase player) {
		TrinketProperties cap = Capabilities.getTrinketProperties(stack);
		if ((cap != null) && event.getPotionEffect().getPotion().equals(MobEffects.RESISTANCE)) {
			TickHandler counter = cap.getCounter("CheckResist");
			counter.setLength(event.getPotionEffect().getDuration() - 1).setCountdown(true);
		}
	}

	@Override
	public void eventPlayerHurt(LivingHurtEvent event, ItemStack stack, EntityLivingBase player) {
		EffectsDamageShield.eventPlayerHurt(event, stack, player);
	}

	@Override
	public void playerEquipped(ItemStack stack, EntityLivingBase player) {
		this.addPlayerStatus(stack, player);
		super.playerEquipped(stack, player);
	}

	@Override
	public void playerUnequipped(ItemStack stack, EntityLivingBase player) {
		super.playerUnequipped(stack, player);
		if (!TrinketHelper.AccessoryCheck(player, stack.getItem())) {
			this.removePlayerStatus(stack, player);
			if (player.isPotionActive(MobEffects.RESISTANCE)) {
				player.removeActivePotionEffect(MobEffects.RESISTANCE);
				player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 10, serverConfig.resistance_level, false, false));
			}
		}
	}

	private boolean stackHasStatus(ItemStack stack) {
		return this.getTagCompoundSafe(stack).hasKey("status");
	}

	private void addPlayerStatus(ItemStack stack, EntityLivingBase player) {
		if (this.stackHasStatus(stack)) {
			this.getTagCompoundSafe(stack).removeTag("status");
		}
		VipStatus status = Capabilities.getVipStatus(player);
		if ((status != null) && (status.getStatus() > 0)) {
			this.getTagCompoundSafe(stack).setInteger("status", status.getStatus());
		}
	}

	private void removePlayerStatus(ItemStack stack, EntityLivingBase player) {
		if (this.stackHasStatus(stack)) {
			this.getTagCompoundSafe(stack).removeTag("status");
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		ModelResourceLocation normal = new ModelResourceLocation(this.getRegistryName().toString(), "inventory");
		ModelResourceLocation bro = new ModelResourceLocation(this.getRegistryName().toString() + "_bro", "inventory");
		ModelResourceLocation panda = new ModelResourceLocation(this.getRegistryName().toString() + "_panda", "inventory");
		ModelResourceLocation vip = new ModelResourceLocation(this.getRegistryName().toString() + "_vip", "inventory");
		ModelBakery.registerItemVariants(this, normal, bro, panda, vip);
		ModelLoader.setCustomMeshDefinition(this, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				if (TrinketDamageShield.this.stackHasStatus(stack)) {
					if (TrinketDamageShield.this.getTagCompoundSafe(stack).getInteger("status") == 1) {
						return vip;
					} else if (TrinketDamageShield.this.getTagCompoundSafe(stack).getInteger("status") == 2) {
						return bro;
					} else if (TrinketDamageShield.this.getTagCompoundSafe(stack).getInteger("status") == 3) {
						return panda;
					} else {
						return vip;
					}
				} else {
					return normal;
				}
			}
		});
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void playerRender(ItemStack stack, EntityLivingBase player, RenderPlayer renderer, float partialTicks, float scale, boolean isBauble) {
		if (!clientConfig.doRender) {
			return;
		}
		float offsetX = 0.17F;
		float offsetY = 0.22F;
		float offsetZ = 0.16F;
		GlStateManager.pushMatrix();
		if (player.isSneaking()) {
			GlStateManager.translate(0F, 0.2F, 0F);
		}
		renderer.getMainModel().bipedBody.postRender(scale);
		GlStateManager.rotate(180F, 1F, 0F, 0F);
		GlStateManager.translate(offsetX, -offsetY, offsetZ);
		if (player.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
			GlStateManager.translate(offsetX - 0.14F, 0, -(offsetZ - 0.2F));
		}
		float bS = 3f;
		GlStateManager.scale(scale * bS, scale * bS, scale * bS);
		Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
		GlStateManager.popMatrix();
	}

	@Override
	public boolean ItemEnabled() {
		return serverConfig.enabled;
	}
}