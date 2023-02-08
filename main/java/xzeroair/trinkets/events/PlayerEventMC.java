package xzeroair.trinkets.events;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.SleepResult;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.interfaces.ISleepAbility;
import xzeroair.trinkets.util.TrinketsConfig;

public class PlayerEventMC {

	@SubscribeEvent
	public void startTracking(PlayerEvent.StartTracking event) {
		final EntityPlayer player = event.getEntityPlayer();
		final Entity entity = event.getTarget();
		Capabilities.getEntityProperties(entity, cap -> {
			cap.sendInformationToPlayer(player);
		});
		if (TrinketsConfig.SERVER.misc.retrieveVIP) {
			Capabilities.getVipStatus(entity, cap -> {
				cap.sendStatusToPlayer(player);
			});
		}
	}

	@SubscribeEvent
	public void playerRespawn(PlayerRespawnEvent event) {
		final EntityPlayer player = event.player;
		final boolean client = player.world.isRemote;
		if (!client && player.isEntityAlive()) {
			//			Capabilities.getEntityProperties(player, cap -> {
			//				cap.sendInformationToPlayer(player);
			//			});
			Capabilities.getMagicStats(player, cap -> {
				cap.sendManaToPlayer(player);
			});
			TrinketHelper.applyToAccessories(player, stack -> {
				Capabilities.getTrinketProperties(stack, prop -> {
					prop.turnOff();
					prop.sendInformationToPlayer(player, player);
				});
			});
		}
	}

	@SubscribeEvent
	public void playerSleep(PlayerSleepInBedEvent event) {
		final EntityLivingBase entity = event.getEntityLiving();
		Capabilities.getEntityProperties(entity, prop -> {
			Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
			for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
				String key = entry.getKey();
				AbilityHolder value = entry.getValue();
				try {
					IAbilityInterface ability = value.getAbility();
					if ((ability instanceof ISleepAbility)) {
						SleepResult defaultResult = event.getResultStatus();
						SleepResult result = ((ISleepAbility) ability).onStartSleeping(entity, event.getPos(), event.getResultStatus());
						if ((result != null)) {
							boolean ok = true;
							if (defaultResult != null) {
								ok = (result.compareTo(defaultResult) != 0);
							}
							if (ok) {
								event.setResult(result);
							}
						}
					}
				} catch (Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + key);
					e.printStackTrace();
				}
			}
		});
	}

	@SubscribeEvent
	public void playerWakeUp(PlayerWakeUpEvent event) {
		final EntityLivingBase entity = event.getEntityLiving();
		Capabilities.getEntityProperties(entity, prop -> {
			Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
			for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
				String key = entry.getKey();
				AbilityHolder value = entry.getValue();
				try {
					IAbilityInterface ability = value.getAbility();
					if ((ability instanceof ISleepAbility)) {
						((ISleepAbility) ability).onWakeUp(entity, event.wakeImmediately(), event.updateWorld(), event.shouldSetSpawn());
					}
				} catch (Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + key);
					e.printStackTrace();
				}
			}
		});
	}

	@SubscribeEvent
	public void playerClone(PlayerPickupXpEvent event) {
		//		try {
		//			final EntityPlayer player = event.getEntityPlayer();
		//			final int total = player.experienceTotal;
		//			final int level = player.experienceLevel;
		//			final float exp = player.experience;
		//			final int gainedExp = event.getOrb().getXpValue();
		//			System.out.println("total:" + total + ", level:" + level + ", exp:" + exp + " +" + gainedExp);
		//		} catch (Exception e) {
		//		}
	}

	@SubscribeEvent
	public void playerClone(PlayerEvent.Clone event) {
		final EntityPlayer oldPlayer = event.getOriginal();
		final EntityPlayer newPlayer = event.getEntityPlayer();
		final boolean wasDeath = event.isWasDeath();
		final boolean keepInv = event.getOriginal().getEntityWorld().getGameRules().getBoolean("keepInventory");
		try {
			Capabilities.getVipStatus(oldPlayer, oldVIP -> {
				Capabilities.getVipStatus(newPlayer, newVIP -> {
					newVIP.copyFrom(oldVIP, wasDeath, keepInv);
				});
			});
		} catch (final Exception e) {
			e.printStackTrace();
		}
		try {
			Capabilities.getEntityProperties(oldPlayer, oldProp -> {
				Capabilities.getEntityProperties(newPlayer, newProp -> {
					newProp.copyFrom(oldProp, wasDeath, keepInv);
				});
			});
		} catch (final Exception e) {
			e.printStackTrace();
		}
		try {
			Capabilities.getMagicStats(oldPlayer, oldMagic -> {
				Capabilities.getMagicStats(newPlayer, newMagic -> {
					newMagic.copyFrom(oldMagic, wasDeath, keepInv);
				});
			});
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
