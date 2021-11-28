package xzeroair.trinkets.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.capabilities.Vip.VipStatus;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.traits.abilities.IAbilityHandler;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.interfaces.ISleepAbility;

public class PlayerEventMC {

	@SubscribeEvent
	public void startTracking(PlayerEvent.StartTracking event) {
		if ((event.getEntityPlayer() == null) || event.getEntityPlayer().isDead) {
			return;
		}
		final EntityPlayer player = event.getEntityPlayer();
		final boolean client = player.world.isRemote;
		final Entity entity = event.getTarget();
		if (!client) {
			final EntityProperties cap = Capabilities.getEntityRace(entity);
			if (cap != null) {
				cap.sendInformationToPlayer(player);
			}
			final VipStatus vip = Capabilities.getVipStatus(entity);
			if ((vip != null)) {
				vip.sendStatusToPlayer(player);
			}
		}
	}

	@SubscribeEvent
	public void playerRespawn(PlayerRespawnEvent event) {
		final EntityPlayer player = event.player;
		final boolean client = player.world.isRemote;
		if (!client && player.isEntityAlive()) {
			final EntityProperties cap = Capabilities.getEntityRace(player);
			if (cap != null) {
				cap.sendInformationToPlayer(player);
			}
			final MagicStats magic = Capabilities.getMagicStats(player);
			if (magic != null) {
				magic.sendManaToPlayer(player);
			}
			ItemStack stack = null;
			if (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDragonsEye)) {
				stack = TrinketHelper.getAccessory(player, ModItems.trinkets.TrinketDragonsEye);
				if ((stack != null)) {
					final TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
					if ((iCap != null) && !player.world.isRemote) {
						iCap.turnOff();
						iCap.sendInformationToPlayer(player, player);
					}
				}
			}
			if (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketPolarized)) {
				stack = TrinketHelper.getAccessory(player, ModItems.trinkets.TrinketPolarized);
				if ((stack != null)) {
					final TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
					if ((iCap != null) && !player.world.isRemote) {
						iCap.turnOff();
						iCap.sendInformationToPlayer(player, player);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void playerSleep(PlayerSleepInBedEvent event) {
	}

	@SubscribeEvent
	public void playerWakeUp(PlayerWakeUpEvent event) {
		final EntityLivingBase entity = event.getEntityLiving();
		final EntityProperties prop = Capabilities.getEntityRace(entity);
		if (prop != null) {
			for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
				try {
					final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
					if ((handler != null) && (handler instanceof ISleepAbility)) {
						((ISleepAbility) handler).onWakeUp(entity, event.wakeImmediately(), event.updateWorld(), event.shouldSetSpawn());
					}
				} catch (final Exception e) {
					Trinkets.log.error("Error with ability:" + ability.getName());
					e.printStackTrace();
				}
			}
		}
	}

	@SubscribeEvent
	public void playerClone(PlayerEvent.Clone event) {
		final EntityPlayer oldPlayer = event.getOriginal();
		final EntityPlayer newPlayer = event.getEntityPlayer();
		final boolean wasDeath = event.isWasDeath();
		final boolean keepInv = event.getOriginal().getEntityWorld().getGameRules().getBoolean("keepInventory");
		try {
			final EntityProperties oldRace = Capabilities.getEntityRace(oldPlayer);
			final EntityProperties newRace = Capabilities.getEntityRace(newPlayer);
			if ((oldRace != null) && (newRace != null)) {
				newRace.copyFrom(oldRace, wasDeath, keepInv);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		try {
			final MagicStats oldMagic = Capabilities.getMagicStats(oldPlayer);
			final MagicStats newMagic = Capabilities.getMagicStats(newPlayer);
			if ((oldMagic != null) && (newMagic != null)) {
				newMagic.copyFrom(oldMagic, wasDeath, keepInv);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
