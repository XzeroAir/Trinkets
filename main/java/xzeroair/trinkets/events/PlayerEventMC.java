package xzeroair.trinkets.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.attributes.RaceAttribute.RaceAttribute;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.capabilities.manaCap.ManaStats;
import xzeroair.trinkets.capabilities.race.RaceProperties;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.foods.Dwarf_Stout;
import xzeroair.trinkets.items.foods.Fairy_Food;
import xzeroair.trinkets.items.foods.Titan_Spirit;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.SizeDataPacket;
import xzeroair.trinkets.util.TrinketsConfig;

public class PlayerEventMC {

	@SubscribeEvent
	public void startTracking(PlayerEvent.StartTracking event) {
		if (event.getEntityPlayer() != null) {
			final EntityPlayer player = event.getEntityPlayer();
			final boolean client = player.world.isRemote;
			if ((event.getTarget() != null) && (event.getTarget() instanceof EntityLivingBase) && event.getTarget().hasCapability(Capabilities.ENTITY_RACE, null)) {
				final EntityLivingBase entity = (EntityLivingBase) event.getTarget();
				RaceProperties cap = Capabilities.getEntityRace(entity);
				if (!client) {
					NetworkHandler.INSTANCE.sendTo(new SizeDataPacket(entity, cap), (EntityPlayerMP) player);
					//					NetworkHandler.INSTANCE.sendTo(new SizeDataPacket(player, cap), (EntityPlayerMP) entity);
					//					NetworkHandler.sendPlayerDataTo(entity, cap, (EntityPlayerMP) player);
				}
			}
		}
	}

	@SubscribeEvent
	public void playerClone(PlayerEvent.Clone event) {

		final EntityPlayer oldPlayer = event.getOriginal();
		final EntityPlayer newPlayer = event.getEntityPlayer();

		if (TrinketsConfig.SERVER.Food.keep_effects) {
			if (oldPlayer.getAttributeMap().getAttributeInstance(RaceAttribute.ENTITY_RACE) != null) {
				final IAttributeInstance raceOld = oldPlayer.getAttributeMap().getAttributeInstance(RaceAttribute.ENTITY_RACE);
				final AttributeModifier fairyFoodOld = raceOld.getModifier(Fairy_Food.getUUID());
				final AttributeModifier dwarfFoodOld = raceOld.getModifier(Dwarf_Stout.getUUID());
				final AttributeModifier titanFoodOld = raceOld.getModifier(Titan_Spirit.getUUID());
				if (newPlayer.getAttributeMap().getAttributeInstance(RaceAttribute.ENTITY_RACE) != null) {
					//					AttributeHelper.removeRaceAttribute(newPlayer);
					final IAttributeInstance raceNew = newPlayer.getAttributeMap().getAttributeInstance(RaceAttribute.ENTITY_RACE);
					final AttributeModifier fairyFoodNew = raceNew.getModifier(Fairy_Food.getUUID());
					final AttributeModifier dwarfFoodNew = raceNew.getModifier(Dwarf_Stout.getUUID());
					final AttributeModifier titanFoodNew = raceNew.getModifier(Titan_Spirit.getUUID());
					if ((fairyFoodOld != null) && (fairyFoodNew == null)) {
						RaceAttribute.addModifier(newPlayer, 1, Fairy_Food.getUUID(), 0);
					}
					if ((dwarfFoodOld != null) && (dwarfFoodNew == null)) {
						RaceAttribute.addModifier(newPlayer, 1, Dwarf_Stout.getUUID(), 0);
					}
					if ((titanFoodOld != null) && (titanFoodNew == null)) {
						RaceAttribute.addModifier(newPlayer, 1, Titan_Spirit.getUUID(), 0);
					}
				}
				RaceProperties oldRace = Capabilities.getEntityRace(oldPlayer);
				RaceProperties newRace = Capabilities.getEntityRace(newPlayer);
				newRace.copyFrom(oldRace);
			}
		}
		if (event.isWasDeath()) {
			if (event.getOriginal().hasCapability(Capabilities.PLAYER_MANA, null)) {
				final ManaStats oldMana = Capabilities.getPlayerMana(oldPlayer);
				final ManaStats newMana = Capabilities.getPlayerMana(newPlayer);
				newMana.copyFrom(oldMana);
			}
		}

		if (event.isWasDeath() && (event.getOriginal().world.getGameRules().getBoolean("keepInventory") == true)) {
			RaceProperties oldRace = Capabilities.getEntityRace(oldPlayer);
			RaceProperties newRace = Capabilities.getEntityRace(newPlayer);
			ItemStack stack = null;

			if (TrinketHelper.AccessoryCheck(event.getEntityPlayer(), ModItems.trinkets.TrinketDragonsEye)) {
				stack = TrinketHelper.getAccessory(event.getEntityPlayer(), ModItems.trinkets.TrinketDragonsEye);
			}
			if (TrinketHelper.AccessoryCheck(event.getEntityPlayer(), ModItems.trinkets.TrinketPolarized)) {
				stack = TrinketHelper.getAccessory(event.getEntityPlayer(), ModItems.trinkets.TrinketPolarized);
			}
			if ((stack != null)) {
				TrinketProperties cap = Capabilities.getTrinketProperties(stack);
				if ((cap != null) && !event.getEntityPlayer().world.isRemote) {
					cap.toggleMainAbility(false);
					NetworkHandler.sendItemDataTo(event.getEntityPlayer(), stack, cap, true, (EntityPlayerMP) event.getEntityPlayer());
				}
			}

			if (TrinketHelper.AccessoryCheck(oldPlayer, TrinketHelper.SizeTrinkets)) {
				newRace.copyFrom(oldRace);
				if (TrinketHelper.AccessoryCheck(oldPlayer, ModItems.trinkets.TrinketFairyRing)) {
					if (TrinketsConfig.SERVER.FAIRY_RING.creative_flight == true) {
						if (TrinketsConfig.SERVER.FAIRY_RING.creative_flight_speed && oldPlayer.world.isRemote) {
							oldPlayer.capabilities.setFlySpeed((float) TrinketsConfig.SERVER.FAIRY_RING.flight_speed);
						}
					}
				}
			}
		}
	}
}
