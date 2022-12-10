package xzeroair.trinkets.events;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.interfaces.IMiningAbility;

public class BlockBreakEvents extends EventBaseHandler {

	/**
	 * Only Fires with an Empty Hand Apparently actually, it fires only if using an
	 * empty hand, or the tool level is less then 0, Which Basically makes this very
	 * situational and worthless
	 */
	//	@SubscribeEvent
	//	public void canHarvest(HarvestCheck event) {
	//		System.out.println(event.canHarvest());
	//		final ItemStack heldItemStack = event.getEntityPlayer().getActiveItemStack();
	//		final Item heldItem = heldItemStack.getItem();
	//		final int blockLevel = event.getTargetBlock().getBlock().getHarvestLevel(event.getTargetBlock());
	//		final int toolLevel = heldItem.getHarvestLevel(heldItemStack, "pickaxe", null, event.getTargetBlock());
	//		if (!heldItem.getToolClasses(heldItemStack).isEmpty() && heldItem.getToolClasses(heldItemStack).contains("pickaxe")) {
	//			if ((toolLevel == (blockLevel - 1))) {// && serverConfig.skilled_miner) {
	//
	//			}
	//		}
	//		((IMiningAbility) null).canHarvest(event.getEntityLiving(), event.getTargetBlock(), event.canHarvest());
	//	}

	@SubscribeEvent
	public void BreakSpeed(BreakSpeed event) {
		Capabilities.getEntityProperties(event.getEntityLiving(), prop -> {
			prop.getRaceHandler().breakingBlock(event);
			if (!event.isCanceled()) {
				final float newOriginal = event.getNewSpeed();
				float newSpeed = event.getNewSpeed();
				Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
				for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
					if ((newSpeed <= 0F) || (newSpeed >= 100F)) {
						break;
					}
					String key = entry.getKey();
					AbilityHolder holder = entry.getValue();
					try {
						IAbilityInterface ability = holder.getAbility();
						if ((ability instanceof IMiningAbility)) {
							newSpeed = ((IMiningAbility) ability).breakingBlock(event.getEntityLiving(), event.getState(), event.getPos(), newOriginal, newSpeed);
						}
					} catch (Exception e) {
						Trinkets.log.error("Trinkets had an Error with Ability:" + key);
						e.printStackTrace();
					}
				}
				if (newSpeed != newOriginal) {
					if (newSpeed <= 0) {
						this.cancelEvent(event);
					} else {
						event.setNewSpeed(newSpeed);
					}
				}
			}
		});
	}

	@SubscribeEvent
	public void breakEvent(BreakEvent event) {
		final EntityPlayer player = event.getPlayer();
		Capabilities.getEntityProperties(player, prop -> {
			prop.getRaceHandler().blockBroken(event);
			if (!event.isCanceled()) {
				final int defaultExp = event.getExpToDrop();
				int exp = event.getExpToDrop();
				Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
				for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
					if (exp < 0) {
						break;
					}
					String key = entry.getKey();
					AbilityHolder holder = entry.getValue();
					try {
						IAbilityInterface ability = holder.getAbility();
						if ((ability instanceof IMiningAbility)) {
							final int result = ((IMiningAbility) ability).brokeBlock(player, event.getWorld(), event.getState(), event.getPos(), exp);
							exp = result;
						}
					} catch (Exception e) {
						Trinkets.log.error("Trinkets had an Error with Ability:" + key);
						e.printStackTrace();
					}
					if (exp < 0) {
						break;
					}
				}
				if (exp != defaultExp) {
					if (exp < 0) {
						this.cancelEvent(event);
					} else {
						event.setExpToDrop(exp);
					}
				}
			}
		});
	}

	@SubscribeEvent
	public void blockDrops(HarvestDropsEvent event) {
		Capabilities.getEntityProperties(event.getHarvester(), prop -> {
			prop.getRaceHandler().blockDrops(event);
			if (!event.isCanceled()) {
				final float defaultChance = event.getDropChance();
				float dropChance = event.getDropChance();
				Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
				for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
					if (dropChance <= 0) {
						break;
					}
					String key = entry.getKey();
					AbilityHolder holder = entry.getValue();
					try {
						IAbilityInterface ability = holder.getAbility();
						if ((ability instanceof IMiningAbility)) {
							dropChance = ((IMiningAbility) ability).blockDrops(event.getHarvester(), event.getWorld(), event.getState(), event.getPos(), event.getDrops(), dropChance, event.isSilkTouching(), event.getFortuneLevel());
						}
					} catch (Exception e) {
						Trinkets.log.error("Trinkets had an Error with Ability:" + key);
						e.printStackTrace();
					}
				}
				if (dropChance != defaultChance) {
					if (dropChance <= 0) {
						this.cancelEvent(event);
					} else {
						event.setDropChance(dropChance);
					}
				}
			}
		});
	}
}
