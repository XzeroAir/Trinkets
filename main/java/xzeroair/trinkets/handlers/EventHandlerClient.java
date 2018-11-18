package xzeroair.trinkets.handlers;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.compatibilities.ItemCap.ItemCap;
import xzeroair.trinkets.compatibilities.ItemCap.ItemProvider;
import xzeroair.trinkets.compatibilities.sizeCap.CapPro;
import xzeroair.trinkets.compatibilities.sizeCap.ICap;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.network.ItemCapDataMessage;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.TrinketHelper;
import xzeroair.trinkets.util.helpers.TrinketHelper.TrinketType;

public class EventHandlerClient {

	@SubscribeEvent
	public void KeyEvent(InputEvent.KeyInputEvent event) {
		//		Keyboard.getEventKey();
		//		Keyboard.getEventKeyState();
	}

	boolean toggleNV = true;
	boolean toggleLoot = true;
	int typeCheck = 0;
	int TARGET = 0;
	int AUX = 0;

	@SubscribeEvent
	public void clientTickEvent(TickEvent.ClientTickEvent event) {
		if(event.phase == event.phase.END) {
			typeCheck = MathHelper.clamp(typeCheck, -1, 10);
			EntityPlayerSP player = Minecraft.getMinecraft().player;
			if(player != null) {
				ICap nbt = player.getCapability(CapPro.sizeCapability, null);
				ItemStack baubleCheck = TrinketHelper.getBaubleTypeStack(player, TrinketType.head);
				ItemCap itemNBT = baubleCheck.getCapability(ItemProvider.itemCapability, null);
				if(baubleCheck.getItem() == ModItems.dragons_eye) {
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
						if((TARGET == 1) && (AUX == 0)) {
							if(typeCheck < 10) {
								typeCheck++;
							}
							if(typeCheck == 10) {
								typeCheck = 0;
							}
							if(typeCheck != 0) {
								player.sendMessage(new TextComponentString("I need more " + Block.getBlockById(TrinketHelper.getOre(typeCheck)).getLocalizedName().replace("Ore", "").replace("Chest", "Treasure ") + "for my treasury"));
								itemNBT.setOreType(TrinketHelper.getOre(typeCheck));
								NetworkHandler.INSTANCE.sendToServer(new ItemCapDataMessage(itemNBT.oreType(), itemNBT.nightVision(), player.getEntityId()));
							} else {

								player.sendMessage(new TextComponentString("I think my Treasury is sufficiently full (off)"));
								itemNBT.setOreType(0);
							}
						}
						if((TARGET == 1) && (AUX == 1)) {
							if(typeCheck > -1) {
								typeCheck--;
							}
							if(typeCheck == -1) {
								typeCheck = 9;
							}
							if(typeCheck != 0) {
								player.sendMessage(new TextComponentString("I need more " + Block.getBlockById(TrinketHelper.getOre(typeCheck)).getLocalizedName().replace("Ore", "").replace("Chest", "Treasure ") + "for my treasury"));
								itemNBT.setOreType(TrinketHelper.getOre(typeCheck));
								NetworkHandler.INSTANCE.sendToServer(new ItemCapDataMessage(itemNBT.oreType(), itemNBT.nightVision(), player.getEntityId()));
							} else {
								player.sendMessage(new TextComponentString("I think my Treasury is sufficiently full (off)"));
								itemNBT.setOreType(0);
							}
						}
					}
				}
			}

		}
	}
}
