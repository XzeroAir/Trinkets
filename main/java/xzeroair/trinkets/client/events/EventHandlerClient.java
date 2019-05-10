package xzeroair.trinkets.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import xzeroair.trinkets.capabilities.ItemCap.IItemCap;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.network.ItemCapDataMessage;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.OreTrackingHelper;
import xzeroair.trinkets.util.helpers.TrinketHelper;

public class EventHandlerClient {

	boolean toggleNV = true;
	boolean toggleLoot = true;
	int typeCheck = 0;
	int TARGET = 0;
	int AUX = 0;

	@SubscribeEvent
	public void clientTickEvent(TickEvent.ClientTickEvent event) {
		if(event.phase == Phase.END) {
			final EntityPlayerSP player = Minecraft.getMinecraft().player;
			if(player != null) {
				if(TrinketHelper.baubleCheck(player, ModItems.dragons_eye)) {
					if(TrinketHelper.hasCap(TrinketHelper.getBaubleStack(player, ModItems.dragons_eye))) {
						final IItemCap itemNBT = TrinketHelper.getBaubleCap(TrinketHelper.getBaubleStack(player, ModItems.dragons_eye));
						if(ModKeyBindings.TRINKET_TOGGLE_EFFECT.isPressed()) {
							itemNBT.setEffect(!itemNBT.effect());
							NetworkHandler.INSTANCE.sendToServer(new ItemCapDataMessage(itemNBT.oreType(), itemNBT.effect(), player.getEntityId()));
						}
						if(ModKeyBindings.TRINKET_TARGET.isPressed() && FMLClientHandler.instance().getClient().inGameHasFocus) {
							this.TARGET = 1;
						} else {
							this.TARGET = 0;
						}
						if(ModKeyBindings.AUX_KEY.isKeyDown() && FMLClientHandler.instance().getClient().inGameHasFocus) {
							this.AUX = 1;
						} else {
							this.AUX = 0;
						}
						if(TrinketsConfig.CLIENT.effects.C01_Dragon_Eye != false) {
							final int size = OreTrackingHelper.oreTypesLoaded().size();
							final int off = size - size -1;
							final int max = size - 1;

							if((this.TARGET == 1) && (this.AUX == 0)) {
								if(itemNBT.oreType() < size){
									itemNBT.setOreType(itemNBT.oreType()+1);
								}
								if(itemNBT.oreType() == size){
									itemNBT.setOreType(off);
								}
							}
							if((this.TARGET == 1) && (this.AUX == 1)) {
								if(itemNBT.oreType() > (off-1)){
									itemNBT.setOreType(itemNBT.oreType()-1);
								}
								if(itemNBT.oreType() == (off-1)){
									itemNBT.setOreType(max);
								}
							}
							if(this.TARGET == 1) {
								if((itemNBT.oreType() != off)) {
									if((TrinketsConfig.SERVER.C04_DE_Chests == false)) {
										if(OreTrackingHelper.oreTypesLoaded().get(itemNBT.oreType()).toString().contains("Chest")) {
											if(!(this.AUX == 1)) {
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
										final String Type = OreTrackingHelper.oreTypesLoaded().get(itemNBT.oreType()).toString();
										player.sendMessage(new TextComponentString("I need more " + Type.replace("Ore", "").replace("ore", "").replace("Chest", "Treasure") + " for my treasury"));
										NetworkHandler.INSTANCE.sendToServer(new ItemCapDataMessage(itemNBT.oreType(), itemNBT.effect(), player.getEntityId()));
									} else {
										player.sendMessage(new TextComponentString("I think my Treasury is sufficiently full (off)"));
										NetworkHandler.INSTANCE.sendToServer(new ItemCapDataMessage(itemNBT.oreType(), itemNBT.effect(), player.getEntityId()));
									}
								} else {
									player.sendMessage(new TextComponentString("I think my Treasury is sufficiently full (off)"));
									NetworkHandler.INSTANCE.sendToServer(new ItemCapDataMessage(itemNBT.oreType(), itemNBT.effect(), player.getEntityId()));
								}
							}
						}
					}
				}
			}

		}
	}
}
