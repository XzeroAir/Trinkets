package xzeroair.trinkets.items.effects;

import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
import xzeroair.trinkets.util.handlers.SizeHandler;

public class EffectsDwarfRing {

	private static UUID uuid = UUID.fromString("d222c4fa-0e05-4b90-98c0-1f574d9d2558");

	static ISizeCap cap = null;

	public static UUID getUUID() {
		return uuid;
	}

	public static void DwarfTicks(EntityPlayer player) {
		cap = player.getCapability(SizeCapPro.sizeCapability, null);
		if(cap != null) {
			if(!TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketFairyRing)) {
				if((cap.getTarget() != 75)) {
					cap.setTarget(75);
				}
				if(cap.getTrans() == true) {
					if(cap.getSize() == cap.getTarget()) {
						if(TrinketsConfig.SERVER.DWARF_RING.health) {
							HealthAttribute.addModifier(player, TrinketsConfig.SERVER.DWARF_RING.health_amount, uuid, 2);
						} else {
							HealthAttribute.removeModifier(player, uuid);
						}
						if(TrinketsConfig.SERVER.DWARF_RING.damage) {
							DamageAttribute.addModifier(player, TrinketsConfig.SERVER.DWARF_RING.damage_amount, uuid, 2);
						} else {
							DamageAttribute.removeModifier(player, uuid);
						}
						if(TrinketsConfig.SERVER.DWARF_RING.armor) {
							ArmorAttribute.addModifier(player, TrinketsConfig.SERVER.DWARF_RING.armor_amount, uuid, 2);
						} else {
							ArmorAttribute.removeModifier(player, uuid);
						}
						if(TrinketsConfig.SERVER.DWARF_RING.toughness) {
							ToughnessAttribute.addModifier(player, TrinketsConfig.SERVER.DWARF_RING.toughness_amount, uuid, 2);
						} else {
							ToughnessAttribute.removeModifier(player, uuid);
						}
						if(TrinketsConfig.SERVER.DWARF_RING.speed) {
							SpeedAttribute.addModifier(player, TrinketsConfig.SERVER.DWARF_RING.speed_amount, uuid, 0);
						} else {
							SpeedAttribute.removeModifier(player, uuid);
						}
						if((Loader.isModLoaded("artemislib")) && TrinketsConfig.compat.artemislib) {
							SizeAttribute.addModifier(player, -0.25, -0.25, 0);
						} else {
							if((Loader.isModLoaded("artemislib")) && !TrinketsConfig.compat.artemislib) {
								SizeAttribute.removeModifier(player);
							}
							SizeHandler.setSize(player, cap);
						}
					}
				}
			}
		}
	}

	public static void DwarfFall(LivingFallEvent event, EntityPlayer player) {
		if(cap != null) {
		}
	}

	public static void DwarfJump(EntityLivingBase player) {
		if(cap != null) {
		}
	}

	public static void DwarfEquip(ItemStack stack, EntityLivingBase player) {
		cap = player.getCapability(SizeCapPro.sizeCapability, null);
		EffectsFairyRing.FairyUnequip(null, player);
		if(TrinketsConfig.SERVER.DWARF_RING.health) {
			HealthAttribute.addModifier(player, TrinketsConfig.SERVER.DWARF_RING.health_amount, getUUID(), 2);
			if(Loader.isModLoaded("firstaid")) {
				FirstAidCompat.resetHP(player);
			}
		} else {
			HealthAttribute.removeModifier(player, getUUID());
		}
		if(TrinketsConfig.SERVER.DWARF_RING.damage) {
			DamageAttribute.addModifier(player, TrinketsConfig.SERVER.DWARF_RING.damage_amount, getUUID(), 2);
		} else {
			DamageAttribute.removeModifier(player, getUUID());
		}
		if(TrinketsConfig.SERVER.DWARF_RING.armor) {
			ArmorAttribute.addModifier(player, TrinketsConfig.SERVER.DWARF_RING.armor_amount, getUUID(), 2);
		} else {
			ArmorAttribute.removeModifier(player, getUUID());
		}
		if(TrinketsConfig.SERVER.DWARF_RING.toughness) {
			ToughnessAttribute.addModifier(player, TrinketsConfig.SERVER.DWARF_RING.toughness_amount, getUUID(), 2);
		} else {
			ToughnessAttribute.removeModifier(player, getUUID());
		}
		if(TrinketsConfig.SERVER.DWARF_RING.speed) {
			SpeedAttribute.addModifier(player, TrinketsConfig.SERVER.DWARF_RING.speed_amount, getUUID(), 2);
		} else {
			SpeedAttribute.removeModifier(player, getUUID());
		}
		if((Loader.isModLoaded("artemislib"))) {
			SizeAttribute.removeModifier(player);
		}
	}

	public static void DwarfUnequip(ItemStack stack, EntityLivingBase player) {
		if((Loader.isModLoaded("artemislib"))) {
			SizeAttribute.removeModifier(player);
		}
		HealthAttribute.removeModifier(player, getUUID());
		DamageAttribute.removeModifier(player, getUUID());
		ToughnessAttribute.removeModifier(player, getUUID());
		ArmorAttribute.removeModifier(player, getUUID());
		SpeedAttribute.removeModifier(player, getUUID());
	}

	public static void DwarfLogIn(EntityLivingBase player, ItemStack stack) {
		if(Loader.isModLoaded("firstaid")) {
			FirstAidCompat.resetHP(player);
		}
	}

	public static void DwarfLogout(EntityLivingBase player) {
		if(cap != null) {
			HealthAttribute.removeModifier(player, getUUID());
			DamageAttribute.removeModifier(player, getUUID());
			ToughnessAttribute.removeModifier(player, getUUID());
			ArmorAttribute.removeModifier(player, getUUID());
			SpeedAttribute.removeModifier(player, getUUID());
		}
	}

	public static void DwarfRender(EntityPlayer player, float partialTicks, boolean isBauble) {
		if(cap != null) {
		}
	}
}
