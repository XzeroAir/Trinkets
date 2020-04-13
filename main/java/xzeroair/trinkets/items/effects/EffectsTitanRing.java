package xzeroair.trinkets.items.effects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.Loader;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.RaceProperties;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.artemislib.SizeAttribute;
import xzeroair.trinkets.util.compat.firstaid.FirstAidCompat;
import xzeroair.trinkets.util.handlers.SizeHandler;
import xzeroair.trinkets.util.helpers.AttributeHelper;

public class EffectsTitanRing {

	private static final UUID uuid = UUID.fromString("c4f10831-7177-4538-8c3e-fcfcf3647207");

	public static UUID getUUID() {
		return uuid;
	}

	public static final List<Item> incompatible = new ArrayList<Item>() {
		{
			this.add(ModItems.trinkets.TrinketDwarfRing);
			this.add(ModItems.trinkets.TrinketFairyRing);
		}
	};

	public static void TitansTicks(EntityLivingBase player) {
		RaceProperties cap = Capabilities.getEntityRace(player);
		if (cap != null) {
			if (!TrinketHelper.AccessoryCheck(player, incompatible)) {
				AttributeHelper.handleAttributes(player, TrinketsConfig.SERVER.TITAN_RING.Attributes, uuid);
				if ((cap.getTarget() != 300)) {
					cap.setTarget(300);
				}
				if (cap.getTrans() == true) {
					if (cap.getSize() == cap.getTarget()) {
						if ((Loader.isModLoaded("artemislib")) && TrinketsConfig.compat.artemislib) {
							SizeAttribute.addModifier(player, 2, 2, 2);
						} else {
							if ((Loader.isModLoaded("artemislib")) && !TrinketsConfig.compat.artemislib) {
								SizeAttribute.removeModifier(player);
							}
							SizeHandler.setSize(player, cap);
						}
					}
					if (player.isInWater()) {
						player.motionY -= 0.1f;
					}
					if (TrinketsConfig.SERVER.TITAN_RING.step_height != false) {
						final float step = player.stepHeight;
						final float f = 1.2f;
						if ((step != f) && !(step > 0.6f)) {
							player.stepHeight = f;
						}
					}
					if (player.isRiding()) {
						if (player.getRidingEntity() instanceof EntityBoat) {
							final EntityBoat boat = (EntityBoat) player.getRidingEntity();
							if (boat.getControllingPassenger() == player) {
								player.dismountRidingEntity();
							}
						}
					}
				}
			}
		}
	}

	public static void TitanFall(LivingFallEvent event, EntityLivingBase player) {
		if (!TrinketHelper.AccessoryCheck(player, incompatible)) {
			final float base = event.getDistance();
			// System.out.println(base);
			final float distance = (3F * 2F);
			// System.out.println(base - distance);
			event.setDistance(base - distance);
		}
	}

	public static void TitanJump(EntityLivingBase player) {
		if (!TrinketHelper.AccessoryCheck(player, incompatible)) {
			player.motionY *= 1.5f;
		}
	}

	public static void TitanEquip(ItemStack stack, EntityLivingBase player) {
		EffectsFairyRing.FairyUnequip(null, player);
		EffectsDwarfRing.DwarfUnequip(null, player);
		if (!TrinketHelper.AccessoryCheck(player, incompatible)) {
			AttributeHelper.handleAttributes(player, TrinketsConfig.SERVER.TITAN_RING.Attributes, uuid);
		}
		//		if ((Loader.isModLoaded("artemislib"))) {
		//			SizeAttribute.removeModifier(player);
		//		}
		//		if (TrinketHelper.AccessoryCheck(player, incompatible)) {
		//			TitanUnequip(stack, player);
		//		}
	}

	public static void TitanUnequip(ItemStack stack, EntityLivingBase player) {
		RaceProperties cap = Capabilities.getEntityRace(player);
		if (cap != null) {
			if ((player.stepHeight != 0.6f) && ((TrinketsConfig.SERVER.TITAN_RING.step_height != false))) {
				player.stepHeight = 0.6f;
			}
			if ((Loader.isModLoaded("artemislib"))) {
				SizeAttribute.removeModifier(player);
			}
			AttributeHelper.removeAttributes(player, uuid);
			if ((cap.getTarget() != 100)) {
				cap.setTarget(100);
			}
		}
	}

	public static void TitanLogIn(EntityLivingBase player, ItemStack stack) {
		if (Loader.isModLoaded("firstaid")) {
			FirstAidCompat.resetHP(player);
		}
	}

	public static void TitanLogout(EntityLivingBase player, ItemStack stack) {
		AttributeHelper.removeAttributes(player, uuid);
	}

	public static void renderTitan(EntityPlayer player, float partialTicks, RaceProperties cap) {

	}

	public static void TitanFOV(EntityPlayer player) {
	}

}