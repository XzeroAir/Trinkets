package xzeroair.trinkets.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.compatibilities.ItemCap.ItemCap;
import xzeroair.trinkets.compatibilities.sizeCap.CapPro;
import xzeroair.trinkets.compatibilities.sizeCap.ICap;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.network.ItemCapDataMessage;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.OreTrackingHelper;
import xzeroair.trinkets.util.helpers.TrinketHelper;

public class EventHandlerClient {

	boolean toggleNV = true;
	int typeCheck = 0;
	int TARGET = 0;
	int AUX = 0;

	@SubscribeEvent
	public void clientTickEvent(TickEvent.ClientTickEvent event) {
		if(event.phase == event.phase.END) {
			EntityPlayerSP player = Minecraft.getMinecraft().player;
			if(player != null) {
				ICap nbt = player.getCapability(CapPro.sizeCapability, null);
				if(TrinketHelper.baubleCheck(player, ModItems.dragons_eye)) {
					if(TrinketHelper.hasCap(TrinketHelper.getBaubleStack(player, ModItems.dragons_eye))) {
						ItemCap itemNBT = TrinketHelper.getBaubleCap(TrinketHelper.getBaubleStack(player, ModItems.dragons_eye));
						if(ModKeyBindings.TRINKET_TOGGLE_EFFECT.isPressed()) {
							itemNBT.nightVisionOn(!itemNBT.nightVision());
							NetworkHandler.INSTANCE.sendToServer(new ItemCapDataMessage(itemNBT.oreType(), itemNBT.nightVision(), player.getEntityId()));
						}
						if(ModKeyBindings.TRINKET_TARGET.isPressed()) {
							TARGET = 1;
						} else {
							TARGET = 0;
						}
						if(ModKeyBindings.AUX_KEY.isKeyDown()) {
							AUX = 1;
						} else {
							AUX = 0;
						}
						if(TrinketsConfig.CLIENT.effects.C01_Dragon_Eye != false) {
							int size = OreTrackingHelper.oreTypesLoaded().size();
							int off = size - size -1;
							int max = size - 1;

							if((TARGET == 1) && (AUX == 0)) {
								if(itemNBT.oreType() < size){
									itemNBT.setOreType(itemNBT.oreType()+1);
								}
								if(itemNBT.oreType() == size){
									itemNBT.setOreType(off);
								}
							}
							if((TARGET == 1) && (AUX == 1)) {
								if(itemNBT.oreType() > (off-1)){
									itemNBT.setOreType(itemNBT.oreType()-1);
								}
								if(itemNBT.oreType() == (off-1)){
									itemNBT.setOreType(max);
								}
							}
							if(TARGET == 1) {
								if((itemNBT.oreType() != off)) {
									if((TrinketsConfig.SERVER.C04_DE_Chests == false)) {
										if(OreTrackingHelper.oreTypesLoaded().get(itemNBT.oreType()).toString().contains("Chest")) {
											if(!(AUX == 1)) {
												if((itemNBT.oreType()+1) >= size) {
													itemNBT.setOreType(off);
												} else {
													itemNBT.setOreType(itemNBT.oreType()+1);
												}
											} else {
												if((itemNBT.oreType()-1) <= (off-1)) {
													itemNBT.setOreType(max);
												} else {
													itemNBT.setOreType(itemNBT.oreType()-1);
												}
											}
										}
									}
									if((itemNBT.oreType() != off)) {
										String Type = OreTrackingHelper.oreTypesLoaded().get(itemNBT.oreType()).toString();
										player.sendMessage(new TextComponentString("I need more " + Type.replace("Ore", "").replace("ore", "").replace("Chest", "Treasure") + " for my treasury"));
										NetworkHandler.INSTANCE.sendToServer(new ItemCapDataMessage(itemNBT.oreType(), itemNBT.nightVision(), player.getEntityId()));
									} else {
										player.sendMessage(new TextComponentString("I think my Treasury is sufficiently full (off)"));
										NetworkHandler.INSTANCE.sendToServer(new ItemCapDataMessage(itemNBT.oreType(), itemNBT.nightVision(), player.getEntityId()));
									}
								} else {
									player.sendMessage(new TextComponentString("I think my Treasury is sufficiently full (off)"));
									NetworkHandler.INSTANCE.sendToServer(new ItemCapDataMessage(itemNBT.oreType(), itemNBT.nightVision(), player.getEntityId()));
								}
							}
						}
					}
				}
			}

		}
	}
}
