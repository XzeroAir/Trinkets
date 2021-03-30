package xzeroair.trinkets.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.trinkets.TrinketTeddyBear;
import xzeroair.trinkets.util.TrinketsConfig;

public class PlayerEventMC {

	@SubscribeEvent
	public void startTracking(PlayerEvent.StartTracking event) {
		if ((event.getEntityPlayer() == null) || event.getEntityPlayer().isDead || !(event.getTarget() instanceof EntityLivingBase) || !(event.getTarget().hasCapability(Capabilities.ENTITY_RACE, null))) {
			return;
		}
		final EntityPlayer player = event.getEntityPlayer();
		final boolean client = player.world.isRemote;
		final EntityLivingBase entity = (EntityLivingBase) event.getTarget();
		if (!client) {
			EntityProperties cap = Capabilities.getEntityRace(entity);
			if (cap != null) {
				cap.sendInformationToPlayer(player);
			}
		}
	}

	@SubscribeEvent
	public void playerRespawn(PlayerRespawnEvent event) {
		EntityPlayer player = event.player;
		final boolean client = player.world.isRemote;
		if (player.hasCapability(Capabilities.ENTITY_RACE, null)) {
			if (!client) {
				EntityProperties cap = Capabilities.getEntityRace(player);
				if (cap != null) {
					cap.sendInformationToPlayer(player);
				}
				ItemStack stack = null;
				if (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDragonsEye)) {
					stack = TrinketHelper.getAccessory(player, ModItems.trinkets.TrinketDragonsEye);
					if ((stack != null)) {
						TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
						if ((iCap != null) && !player.world.isRemote) {
							iCap.turnOff();
							iCap.sendInformationToPlayer(player, player);
							//							NetworkHandler.sendItemDataTo(player, stack, iCap, true, (EntityPlayerMP) player);
						}
					}
				}
				if (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketPolarized)) {
					stack = TrinketHelper.getAccessory(player, ModItems.trinkets.TrinketPolarized);
					if ((stack != null)) {
						TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
						if ((iCap != null) && !player.world.isRemote) {
							iCap.turnOff();
							iCap.sendInformationToPlayer(player, player);
							//							NetworkHandler.sendItemDataTo(player, stack, iCap, true, (EntityPlayerMP) player);
						}
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
		if (TrinketsConfig.SERVER.Items.TEDDY_BEAR.sleep_bonus) {
			EntityPlayer player = event.getEntityPlayer();
			if (((player.getHeldItemMainhand().getItem() instanceof TrinketTeddyBear) && player.getHeldItemOffhand().isEmpty()) ||
					((player.getHeldItemOffhand().getItem() instanceof TrinketTeddyBear) && player.getHeldItemMainhand().isEmpty()) ||
					(player.getHeldItemMainhand().isEmpty() && player.getHeldItemOffhand().isEmpty() && TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketTeddyBear))) {
				player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 300, 0, false, false));
				player.addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST, 3600, 2, false, false));
				player.addPotionEffect(new PotionEffect(MobEffects.LUCK, 600, 0, false, false));
			}
		}
	}

	@SubscribeEvent
	public void playerClone(PlayerEvent.Clone event) {
		final EntityPlayer oldPlayer = event.getOriginal();
		final EntityPlayer newPlayer = event.getEntityPlayer();
		if (TrinketsConfig.SERVER.Food.keep_effects) {
			EntityProperties oldRace = Capabilities.getEntityRace(oldPlayer);
			EntityProperties newRace = Capabilities.getEntityRace(newPlayer);
			newRace.copyFrom(oldRace);
		}
	}
}
