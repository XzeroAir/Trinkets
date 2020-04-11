package xzeroair.trinkets.items.effects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
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

public class EffectsDwarfRing {

	private static final UUID uuid = UUID.fromString("d222c4fa-0e05-4b90-98c0-1f574d9d2558");

	// doesnt need to check for Race because Takes Priority over Races, Might need a
	// Check if wearing other trinkets

	public static UUID getUUID() {
		return uuid;
	}

	public static final List<Item> incompatible = new ArrayList<Item>() {
		{
			this.add(ModItems.trinkets.TrinketFairyRing);
			this.add(ModItems.trinkets.TrinketTitanRing);
		}
	};

	public static void DwarfTicks(EntityLivingBase player) {
		RaceProperties cap = Capabilities.getEntityRace(player);
		if (cap != null) {
			if (!TrinketHelper.AccessoryCheck(player, incompatible)) {
				AttributeHelper.handleAttributes(player, TrinketsConfig.SERVER.DWARF_RING.Attributes, uuid);
				if ((cap.getTarget() != 75)) {
					cap.setTarget(75);
				}
				if (cap.getTrans() == true) {
					if (cap.getSize() == cap.getTarget()) {
						if ((Loader.isModLoaded("artemislib")) && TrinketsConfig.compat.artemislib) {
							SizeAttribute.addModifier(player, -0.25, -0.25, 0);
						} else {
							if ((Loader.isModLoaded("artemislib")) && !TrinketsConfig.compat.artemislib) {
								SizeAttribute.removeModifier(player);
							}
							SizeHandler.setSize(player, cap);
						}
					}
				}
			}
		}
	}

	public static void DwarfFall(LivingFallEvent event, EntityLivingBase player) {
	}

	public static void DwarfJump(EntityLivingBase player) {

	}

	public static void DwarfEquip(ItemStack stack, EntityLivingBase player) {
		EffectsFairyRing.FairyUnequip(null, player);
		EffectsTitanRing.TitanUnequip(null, player);
		if (!TrinketHelper.AccessoryCheck(player, incompatible)) {
			AttributeHelper.handleAttributes(player, TrinketsConfig.SERVER.DWARF_RING.Attributes, uuid);
		}
		//		if ((Loader.isModLoaded("artemislib"))) {
		//			SizeAttribute.removeModifier(player);
		//		}
		//		if (TrinketHelper.AccessoryCheck(player, incompatible)) {
		//			DwarfUnequip(stack, player);
		//		}
	}

	public static void DwarfUnequip(ItemStack stack, EntityLivingBase player) {
		if ((Loader.isModLoaded("artemislib"))) {
			SizeAttribute.removeModifier(player);
		}
		AttributeHelper.removeAttributes(player, uuid);
	}

	public static void DwarfLogIn(EntityLivingBase player, ItemStack stack) {
		if (Loader.isModLoaded("firstaid")) {
			FirstAidCompat.resetHP(player);
		}
	}

	public static void DwarfLogout(EntityLivingBase player) {
		AttributeHelper.removeAttributes(player, uuid);
	}

	public static void DwarfRender(EntityPlayer player, float partialTicks, boolean isBauble) {
	}

}
