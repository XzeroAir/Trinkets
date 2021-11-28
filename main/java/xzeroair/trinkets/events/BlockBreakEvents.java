package xzeroair.trinkets.events;

import org.apache.commons.lang3.tuple.ImmutablePair;

import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.traits.abilities.IAbilityHandler;
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
		EntityProperties prop = Capabilities.getEntityRace(event.getEntityLiving());
		if (prop != null) {
			boolean cancel = false;
			float newSpeed = event.getNewSpeed();
			for (IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
				if (cancel) {
					break;
				}
				IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
				if ((handler != null) && (handler instanceof IMiningAbility)) {
					ImmutablePair<Boolean, Float> result = ((IMiningAbility) handler).breakingBlock(event.getEntityLiving(), event.getState(), event.getPos(), event.getOriginalSpeed(), event.getNewSpeed());//.broke(event.getPlayer(), event.getState(), event.getPos(), event.getExpToDrop());
					if (result != null) {
						if (result.getLeft() == true) {
							cancel = true;
						} else {
							if (event.getNewSpeed() != result.getRight()) {
								newSpeed = result.getRight();
							}
						}
					}
				}
			}
			//			prop.getRaceProperties().breakingBlock(event);
			if ((cancel == true) && !this.isEventCanceled(event)) {
				this.cancelEvent(event);
			} else {
				if (event.getNewSpeed() < newSpeed) {
					event.setNewSpeed(newSpeed);
				}
			}
		}
	}

	@SubscribeEvent
	public void breakEvent(BreakEvent event) {
		EntityProperties prop = Capabilities.getEntityRace(event.getPlayer());
		if (prop != null) {
			boolean cancel = false;
			int exp = event.getExpToDrop();
			for (IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
				if (cancel) {
					break;
				}
				IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
				if ((handler != null) && (handler instanceof IMiningAbility)) {
					ImmutablePair<Boolean, Integer> result = ((IMiningAbility) handler).brokeBlock(event.getPlayer(), event.getWorld(), event.getState(), event.getPos(), exp);//.broke(event.getPlayer(), event.getState(), event.getPos(), event.getExpToDrop());
					if (result != null) {
						if ((result.getLeft() != null) && (result.getLeft() == true)) {
							cancel = true;
						} else {
							if ((result.getRight() != null) && (event.getExpToDrop() != result.getRight())) {
								exp = result.getRight();
							}
						}
					}
				}
			}
			if ((cancel == true) && !this.isEventCanceled(event)) {
				this.cancelEvent(event);
			} else {
				if (event.getExpToDrop() != exp) {
					event.setExpToDrop(exp);
				}
			}
			//			prop.getRaceProperties().blockBroken(event);
		}
	}

	@SubscribeEvent
	public void blockDrops(HarvestDropsEvent event) {
		EntityProperties prop = Capabilities.getEntityRace(event.getHarvester());
		if (prop != null) {
			for (IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
				IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
				if ((handler != null) && (handler instanceof IMiningAbility)) {
					float result = ((IMiningAbility) handler).blockDrops(event.getHarvester(), event.getWorld(), event.getState(), event.getPos(), event.getDrops(), event.getDropChance(), event.isSilkTouching(), event.getFortuneLevel());
					if (result != event.getDropChance()) {
						event.setDropChance(result);
					}
				}
			}
			//			prop.getRaceProperties().blockDrops(event);
		}
	}
}
