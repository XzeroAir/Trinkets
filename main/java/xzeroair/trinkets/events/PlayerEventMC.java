package xzeroair.trinkets.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.PotionHelper;
import xzeroair.trinkets.util.helpers.PotionHelper.PotionHolder;

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
				String[] config = TrinketsConfig.SERVER.Items.TEDDY_BEAR.buffs;
				int amount = TrinketsConfig.SERVER.Items.TEDDY_BEAR.randomBuff;
				if (amount > 0) {
					String potID = "";
					for (int i = 0; i < amount; i++) {
						int potRand = Reference.random.nextInt(config.length);
						potID += config[potRand] + ",";
					}
					if (!potID.isEmpty()) {
						String[] pots = potID.split(",");
						for (String p : pots) {
							if (!p.isEmpty()) {
								PotionHolder potion = PotionHelper.getPotionHolder(p);
								if (!player.world.isRemote) {
									if (!player.isPotionActive(potion.getPotion())) {
										player.addPotionEffect(potion.getPotionEffect());
									} else {
										player.getActivePotionEffect(potion.getPotion()).combine(potion.getPotionEffect());
									}
								}
							}
						}
					}
				} else {
					for (String potID : config) {
						PotionHolder potion = PotionHelper.getPotionHolder(potID);
						if (potion.getPotion() != null) {
							player.addPotionEffect(potion.getPotionEffect());
						}
					}
				}
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
