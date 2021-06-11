package xzeroair.trinkets.events;

import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.TrinketsConfig;

public class LootHandler {

	public static String toolType(int i) {
		switch (i) {
		case 0:
			return "pickaxe";
		case 1:
			return "axe";
		case 2:
			return "shovel";
		default:
			return "";
		}
	}

	@SubscribeEvent
	public void playerInteract(PlayerInteractEvent.EntityInteract event) {
		EntityProperties prop = Capabilities.getEntityRace(event.getEntityLiving());
		if (prop != null) {
			prop.getRaceProperties().interactWithEntity(event);
		}
	}

	@SubscribeEvent
	public void playerInteract(PlayerInteractEvent.EntityInteractSpecific event) {

	}

	@SubscribeEvent
	public void BreakSpeed(BreakSpeed event) {
		if (event.getEntityLiving() != null) {
			EntityProperties prop = Capabilities.getEntityRace(event.getEntityLiving());
			if (prop != null) {
				prop.getRaceProperties().breakingBlock(event);
			}
			if (TrinketsConfig.SERVER.Items.SEA_STONE.Swim_Tweaks && TrinketHelper.AccessoryCheck(event.getEntityLiving(), ModItems.trinkets.TrinketSea)) {
				final float o = event.getOriginalSpeed();
				float ns = o;
				if (event.getEntityLiving().isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier(event.getEntityLiving())) {
					ns *= 5f;
					if (!event.getEntityLiving().onGround) {
						ns *= 5.0f;
					}
					event.setNewSpeed(ns);
				}
			}
		}
	}

	@SubscribeEvent
	public void breakEvent(BreakEvent event) {
		if (event.getPlayer() != null) {
			EntityProperties prop = Capabilities.getEntityRace(event.getPlayer());
			if (prop != null) {
				prop.getRaceProperties().blockBroken(event);
			}
		}
	}

	@SubscribeEvent
	public void blockDrops(HarvestDropsEvent event) {
		if (event.getHarvester() != null) {
			EntityProperties prop = Capabilities.getEntityRace(event.getHarvester());
			if (prop != null) {
				prop.getRaceProperties().blockDrops(event);
			}
		}
	}
}
