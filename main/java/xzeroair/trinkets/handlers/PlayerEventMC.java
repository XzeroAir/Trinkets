package xzeroair.trinkets.handlers;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.compatibilities.sizeCap.CapPro;
import xzeroair.trinkets.compatibilities.sizeCap.ICap;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.helpers.TrinketHelper;
import xzeroair.trinkets.util.helpers.TrinketHelper.TrinketType;

public class PlayerEventMC {

	/*
	 * Currently Only Using Player Clone Event to get old Capability NBT data after death
	 * Seems to Run only on Server Side
	 *
	 */

	@SubscribeEvent
	public void BreakSpeed(PlayerEvent.BreakSpeed event) {

	}

	@SubscribeEvent
	public void playerClone(PlayerEvent.Clone event) {
		if(event.getEntityPlayer().hasCapability(CapPro.sizeCapability, null)) {
			final ICap oldCap = event.getOriginal().getCapability(CapPro.sizeCapability, null);
			final ICap newCap = event.getEntityPlayer().getCapability(CapPro.sizeCapability, null);
			if(event.isWasDeath()) {
				if((event.getOriginal().world.getGameRules().getBoolean("keepInventory") == true)) {
					if(TrinketHelper.getBaubleType(event.getOriginal(), TrinketType.rings) == ModItems.small_ring) {
						if ((newCap != null) && (oldCap != null)) {
							newCap.setTrans(oldCap.getTrans());
							newCap.setTarget(oldCap.getTarget());
							newCap.setSize(oldCap.getSize());
							newCap.setWidth(oldCap.getWidth());
							newCap.setHeight(oldCap.getHeight());
							newCap.setDefaultWidth(oldCap.getDefaultWidth());
							newCap.setDefaultHeight(oldCap.getDefaultHeight());
							event.getEntityPlayer().eyeHeight = event.getOriginal().eyeHeight;
							//							System.out.println("Copied Data");
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void HarvestCheck(PlayerEvent.HarvestCheck event) {

	}
}
