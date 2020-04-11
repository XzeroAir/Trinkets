package xzeroair.trinkets.items.effects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.Loader;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.RaceProperties;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.artemislib.SizeAttribute;
import xzeroair.trinkets.util.compat.firstaid.FirstAidCompat;
import xzeroair.trinkets.util.handlers.ClimbHandler;
import xzeroair.trinkets.util.handlers.SizeHandler;
import xzeroair.trinkets.util.helpers.AttributeHelper;
import xzeroair.trinkets.util.helpers.CallHelper;

public class EffectsFairyRing {

	private static final UUID uuid = UUID.fromString("c1a329f8-2a05-4e0d-8099-cbe41485d0f0");

	public static UUID getUUID() {
		return uuid;
	}

	public static final List<Item> incompatible = new ArrayList<Item>() {
		{
			this.add(ModItems.trinkets.TrinketDwarfRing);
			this.add(ModItems.trinkets.TrinketTitanRing);
		}
	};

	public static void FairyTicks(EntityLivingBase entity) {
		RaceProperties cap = Capabilities.getEntityRace(entity);
		if (cap != null) {
			if (!TrinketHelper.AccessoryCheck(entity, incompatible)) {
				AttributeHelper.handleAttributes(entity, TrinketsConfig.SERVER.FAIRY_RING.Attributes, uuid);
				if ((cap.getTarget() != 25)) {
					cap.setTarget(25);
				}
				if (cap.getTrans() == true) {
					if (cap.getSize() == cap.getTarget()) {
						if (entity instanceof EntityPlayer) {
							EntityPlayer player = (EntityPlayer) entity;
							if (TrinketsConfig.SERVER.FAIRY_RING.creative_flight == true) {
								if (!player.isCreative() && (player.capabilities.allowFlying != true)) {
									player.capabilities.allowFlying = true;
									if (TrinketsConfig.SERVER.FAIRY_RING.creative_flight_speed && player.world.isRemote) {
										player.capabilities.setFlySpeed((float) TrinketsConfig.SERVER.FAIRY_RING.flight_speed);
									}
								}
							}
							if (TrinketsConfig.SERVER.FAIRY_RING.step_height != false) {
								final float step = player.stepHeight;
								final float f = 0.25f;
								if ((step != f) && !(step > 0.6f)) {
									if (!player.isCreative()) {
										player.stepHeight = f;
									}
								}
							}
							if (((TrinketsConfig.SERVER.FAIRY_RING.climbing != false))) {
								if (!player.capabilities.isFlying) {
									if (!player.onGround) {
										if (player.collidedHorizontally) {
											if (ClimbHandler.Climb(player)) {
												if (!player.isSneaking()) {
													player.motionY = 0.1f;
												}
												if (player.isSneaking()) {
													player.motionY = 0f;
												}
											}
										}
									}
								}
							}
						}
						if ((Loader.isModLoaded("artemislib")) && TrinketsConfig.compat.artemislib) {
							SizeAttribute.addModifier(entity, -0.75, -0.75, 0);
						} else {
							if ((Loader.isModLoaded("artemislib")) && !TrinketsConfig.compat.artemislib) {
								SizeAttribute.removeModifier(entity);
							}
							SizeHandler.setSize(entity, cap);
						}
					}
					if (entity.isRiding()) {
						if (entity.getRidingEntity() instanceof EntityBoat) {
							final EntityBoat boat = (EntityBoat) entity.getRidingEntity();
							if (boat.getControllingPassenger() == entity) {
								entity.dismountRidingEntity();
							}
						}
					}
				}
			}
		}
	}

	public static void FairyFall(LivingFallEvent event, EntityPlayer player) {
	}

	public static void FairyJump(EntityLivingBase player) {
		if (!TrinketHelper.AccessoryCheck(player, incompatible)) {
			player.motionY = player.motionY * 0.5f;
		}
	}

	public static void FairyEquip(ItemStack stack, EntityLivingBase player) {
		EffectsDwarfRing.DwarfUnequip(null, player);
		EffectsTitanRing.TitanUnequip(null, player);
		if (!TrinketHelper.AccessoryCheck(player, incompatible)) {
			AttributeHelper.handleAttributes(player, TrinketsConfig.SERVER.FAIRY_RING.Attributes, uuid);
		}
	}

	public static void FairyUnequip(ItemStack stack, EntityLivingBase player) {
		RaceProperties cap = Capabilities.getEntityRace(player);
		if (cap != null) {
			if ((Loader.isModLoaded("artemislib"))) {
				SizeAttribute.removeModifier(player);
			}
			if ((player instanceof EntityPlayer)) {
				if ((TrinketsConfig.SERVER.FAIRY_RING.creative_flight == true)) {
					final EntityPlayer Player = (EntityPlayer) player;
					if (!Player.isCreative()) {
						Player.capabilities.isFlying = false;
						Player.capabilities.allowFlying = false;
						if (TrinketsConfig.SERVER.FAIRY_RING.creative_flight_speed && player.world.isRemote) {
							if (Player.capabilities.getFlySpeed() != 0.05F) {
								Player.capabilities.setFlySpeed(0.05f);
							}
						}
					}
				}
			}
			if ((player.stepHeight != 0.6f) && ((TrinketsConfig.SERVER.FAIRY_RING.step_height != false))) {
				player.stepHeight = 0.6f;
			}
			AttributeHelper.removeAttributes(player, uuid);
			if ((cap.getTarget() != 100)) {
				cap.setTarget(100);
			}
		}
	}

	public static void FairyLogIn(EntityLivingBase player, ItemStack stack) {
		if (Loader.isModLoaded("firstaid")) {
			FirstAidCompat.resetHP(player);
		}
	}

	public static void FairyLogout(EntityLivingBase player, ItemStack stack) {
		AttributeHelper.removeAttributes(player, uuid);
	}

	public static void renderFairy(EntityPlayer player, float partialTicks, RaceProperties cap) {
		final ModelBase wings = CallHelper.getModel("wings");
		final float yaw = player.prevRotationYawHead + ((player.rotationYawHead - player.prevRotationYawHead) * partialTicks);
		final float yawOffset = player.prevRenderYawOffset + ((player.renderYawOffset - player.prevRenderYawOffset) * partialTicks);
		final float pitch = player.prevRotationPitch + ((player.rotationPitch - player.prevRotationPitch) * partialTicks);

		if (cap != null) {
			final float size = (float) cap.getSize() / 1000;
			final float scale = MathHelper.clamp((0.1F - size), 0.0F, 0.06F);
			GlStateManager.pushMatrix();
			GlStateManager.rotate(4, 1, 0, 0);
			GlStateManager.translate(0.02F, -(scale - 0.1F), -0.05F);
			if (player.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
				GlStateManager.translate(0F, -0.1F, 0.08F);
				GlStateManager.scale(1.1F, 1.1F, 1.1F);
			}
			if (player.isSneaking()) {
				GlStateManager.translate(0F, 0.2F, 0F);
				GlStateManager.rotate(90F / (float) Math.PI, 1.0F, 0.0F, 0.0F);
			}
			if (player.isSprinting()) {
				GlStateManager.rotate(45F / (float) Math.PI, 1.0F, 0.0F, 0.0F);
			}
			GlStateManager.scale(scale, scale, scale);
			wings.render(player, player.limbSwing, player.limbSwingAmount, player.ticksExisted, player.rotationYaw, player.rotationPitch, 1);
			GlStateManager.popMatrix();
		}
	}

	public static void FairyFOV(EntityPlayer player) {
		//		if (cap != null) {
		//
		//		}
	}

}
