package xzeroair.trinkets.items.effects;

import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.Loader;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.attributes.armor.ArmorAttribute;
import xzeroair.trinkets.attributes.attackdamage.DamageAttribute;
import xzeroair.trinkets.attributes.health.HealthAttribute;
import xzeroair.trinkets.attributes.speed.SpeedAttribute;
import xzeroair.trinkets.attributes.toughness.ToughnessAttribute;
import xzeroair.trinkets.capabilities.sizeCap.ISizeCap;
import xzeroair.trinkets.capabilities.sizeCap.SizeCapPro;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.artemislib.SizeAttribute;
import xzeroair.trinkets.util.compat.firstaid.FirstAidCompat;
import xzeroair.trinkets.util.handlers.ClimbHandler;
import xzeroair.trinkets.util.handlers.SizeHandler;
import xzeroair.trinkets.util.helpers.CallHelper;

public class EffectsFairyRing {

	private static UUID uuid = UUID.fromString("c1a329f8-2a05-4e0d-8099-cbe41485d0f0");

	static ISizeCap cap = null;

	public static UUID getUUID() {
		return uuid;
	}

	public static void FairyTicks(EntityPlayer player) {
		cap = player.getCapability(SizeCapPro.sizeCapability, null);
		if(cap != null) {
			if(!TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDwarfRing)) {
				if((cap.getTarget() != 25)) {
					cap.setTarget(25);
				}
				if(cap.getTrans() == true) {
					if(cap.getSize() == cap.getTarget()) {
						if(TrinketsConfig.SERVER.FAIRY_RING.health) {
							HealthAttribute.addModifier(player, TrinketsConfig.SERVER.FAIRY_RING.health_amount, uuid, 2);
						} else {
							HealthAttribute.removeModifier(player, uuid);
						}
						if(TrinketsConfig.SERVER.FAIRY_RING.damage) {
							DamageAttribute.addModifier(player, TrinketsConfig.SERVER.FAIRY_RING.damage_amount, uuid, 2);
						} else {
							DamageAttribute.removeModifier(player, uuid);
						}
						if(TrinketsConfig.SERVER.FAIRY_RING.armor) {
							ArmorAttribute.addModifier(player, TrinketsConfig.SERVER.FAIRY_RING.armor_amount, uuid, 2);
						} else {
							ArmorAttribute.removeModifier(player, uuid);
						}
						if(TrinketsConfig.SERVER.FAIRY_RING.toughness) {
							ToughnessAttribute.addModifier(player, TrinketsConfig.SERVER.FAIRY_RING.toughness_amount, uuid, 2);
						} else {
							ToughnessAttribute.removeModifier(player, uuid);
						}
						if(TrinketsConfig.SERVER.FAIRY_RING.speed) {
							SpeedAttribute.addModifier(player, TrinketsConfig.SERVER.FAIRY_RING.speed_amount, uuid, 0);
						} else {
							SpeedAttribute.removeModifier(player, uuid);
						}
						if(TrinketsConfig.SERVER.FAIRY_RING.creative_flight == true) {
							if(!player.isCreative() && (player.capabilities.allowFlying != true)) {
								player.capabilities.allowFlying = true;
								if(TrinketsConfig.SERVER.FAIRY_RING.creative_flight_speed && player.world.isRemote) {
									player.capabilities.setFlySpeed((float) TrinketsConfig.SERVER.FAIRY_RING.flight_speed);
								}
							}
						}
						if(TrinketsConfig.SERVER.FAIRY_RING.step_height != false) {
							final float step = player.stepHeight;
							final float f = 0.25f;
							if((step != f) && !(step > 0.6f)) {
								if(!player.isCreative()) {
									player.stepHeight = f;
								}
							}
						}
						if(((TrinketsConfig.SERVER.FAIRY_RING.climbing != false))) {
							if(!player.capabilities.isFlying) {
								final EnumFacing facing = player.getHorizontalFacing();
								final BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);
								final IBlockState state = player.world.getBlockState(pos.add(0, 0, 0).offset(facing));
								final Block block = state.getBlock();
								final boolean canPass = block.isPassable(player.world, pos.offset(facing));
								if(!player.onGround){
									if(player.collidedHorizontally) {
										if(ClimbHandler.Climb(player)) {
											if(!player.isSneaking()){
												player.motionY = 0.1f;
											}
											if(player.isSneaking()){
												player.motionY = 0f;
											}
										}
									}
								}
							}
						}
						if(!TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDwarfRing)) {
							if((Loader.isModLoaded("artemislib")) && TrinketsConfig.compat.artemislib) {
								SizeAttribute.addModifier(player, -0.75, -0.75, 0);
							} else {
								if((Loader.isModLoaded("artemislib")) && !TrinketsConfig.compat.artemislib) {
									SizeAttribute.removeModifier(player);
								}
								SizeHandler.setSize(player, cap);
							}
						}

					}
					if(player.isRiding()) {
						if(player.getRidingEntity() instanceof EntityBoat) {
							final EntityBoat boat = (EntityBoat) player.getRidingEntity();
							if(boat.getControllingPassenger() == player) {
								player.dismountRidingEntity();
							}
						}
					}
				}
			}
		}
	}

	public static void FairyFall(LivingFallEvent event, EntityPlayer player) {
		if(cap != null) {
		}
	}

	public static void FairyJump(EntityLivingBase player) {
		if(cap != null) {
			player.motionY = player.motionY*0.5f;
		}
	}

	public static void FairyEquip(ItemStack stack, EntityLivingBase player) {
		cap = player.getCapability(SizeCapPro.sizeCapability, null);
		EffectsDwarfRing.DwarfUnequip(null, player);
		if(TrinketsConfig.SERVER.FAIRY_RING.health) {
			HealthAttribute.addModifier(player, TrinketsConfig.SERVER.FAIRY_RING.health_amount, uuid, 2);
			if(Loader.isModLoaded("firstaid")) {
				FirstAidCompat.resetHP(player);
			}
		} else {
			HealthAttribute.removeModifier(player, uuid);
		}
		if(TrinketsConfig.SERVER.FAIRY_RING.damage) {
			DamageAttribute.addModifier(player, TrinketsConfig.SERVER.FAIRY_RING.damage_amount, uuid, 2);
		} else {
			DamageAttribute.removeModifier(player, uuid);
		}
		if(TrinketsConfig.SERVER.FAIRY_RING.armor) {
			ArmorAttribute.addModifier(player, TrinketsConfig.SERVER.FAIRY_RING.armor_amount, uuid, 2);
		} else {
			ArmorAttribute.removeModifier(player, uuid);
		}
		if(TrinketsConfig.SERVER.FAIRY_RING.toughness) {
			ToughnessAttribute.addModifier(player, TrinketsConfig.SERVER.FAIRY_RING.toughness_amount, uuid, 2);
		} else {
			ToughnessAttribute.removeModifier(player, uuid);
		}
		if(TrinketsConfig.SERVER.FAIRY_RING.speed) {
			SpeedAttribute.addModifier(player, TrinketsConfig.SERVER.FAIRY_RING.speed_amount, uuid, 2);
		} else {
			SpeedAttribute.removeModifier(player, uuid);
		}
		if((Loader.isModLoaded("artemislib"))) {
			SizeAttribute.removeModifier(player);
		}
	}

	public static void FairyUnequip(ItemStack stack, EntityLivingBase player) {
		if((TrinketsConfig.SERVER.FAIRY_RING.creative_flight == true) && (player instanceof EntityPlayer)) {
			final EntityPlayer Player = (EntityPlayer) player;
			if(!Player.isCreative()) {
				Player.capabilities.isFlying = false;
				Player.capabilities.allowFlying = false;
				if(TrinketsConfig.SERVER.FAIRY_RING.creative_flight_speed) {
					if(Player.capabilities.getFlySpeed() != 0.05F) {
						Player.capabilities.setFlySpeed(0.05f);
					}
				}
			}
		}
		if((player.stepHeight != 0.6f) && ((TrinketsConfig.SERVER.FAIRY_RING.step_height != false))){
			player.stepHeight = 0.6f;
		}
		if((Loader.isModLoaded("artemislib"))) {
			SizeAttribute.removeModifier(player);
		}
		HealthAttribute.removeModifier(player, uuid);
		DamageAttribute.removeModifier(player, uuid);
		ToughnessAttribute.removeModifier(player, uuid);
		ArmorAttribute.removeModifier(player, uuid);
		SpeedAttribute.removeModifier(player, uuid);
	}

	public static void FairyLogIn(EntityLivingBase player, ItemStack stack) {
		if(Loader.isModLoaded("firstaid")) {
			FirstAidCompat.resetHP(player);
		}
	}

	public static void FairyLogout(EntityLivingBase player, ItemStack stack) {
		if(cap != null) {
			HealthAttribute.removeModifier(player, uuid);
			DamageAttribute.removeModifier(player, uuid);
			ToughnessAttribute.removeModifier(player, uuid);
			ArmorAttribute.removeModifier(player, uuid);
			SpeedAttribute.removeModifier(player, uuid);
		}
	}

	public static void renderFairy(EntityPlayer player, float partialTicks, ISizeCap cap) {
		final ModelBase wings = CallHelper.getModel("wings");
		final float yaw = player.prevRotationYawHead + ((player.rotationYawHead - player.prevRotationYawHead) * partialTicks);
		final float yawOffset = player.prevRenderYawOffset + ((player.renderYawOffset - player.prevRenderYawOffset) * partialTicks);
		final float pitch = player.prevRotationPitch + ((player.rotationPitch - player.prevRotationPitch) * partialTicks);

		if(cap != null) {
			final float size = (float)cap.getSize()/1000;
			final float scale = MathHelper.clamp((0.1F-size), 0.0F, 0.06F);
			GlStateManager.pushMatrix();
			GlStateManager.rotate(4, 1, 0, 0);
			GlStateManager.translate(0.02F, -(scale-0.1F), -0.05F);
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

	public static void FairyFOV(EntityPlayer player) {
		if(cap != null) {

		}
	}
}
