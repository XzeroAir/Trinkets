package xzeroair.trinkets.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.TrinketCap.TrinketProvider;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.effects.EffectsPolarizedStone;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.OpenTrinketGui;
import xzeroair.trinkets.network.PolarizedStoneSyncPacket;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.OreTrackingHelper;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class EventHandlerClient {

	int Dragon_Ability = 0;
	int Polarized_Ability = 0;
	int TARGET = 0;
	int AUX = 0;

	@SubscribeEvent
	public void clientTickEvent(TickEvent.ClientTickEvent event) {
		if(event.phase == Phase.END) {
			final EntityPlayerSP player = Minecraft.getMinecraft().player;
			if((player != null)) {
				if(TrinketsConfig.SERVER.GUI.guiEnabled && ModKeyBindings.TRINKET_GUI.isPressed()) {
					NetworkHandler.INSTANCE.sendToServer(new OpenTrinketGui());
				}

				if(ModKeyBindings.POLARIZED_STONE_ABILITY.isPressed() && FMLClientHandler.instance().getClient().inGameHasFocus) {
					Polarized_Ability = 1;
				} else {
					Polarized_Ability = 0;
				}
				if(ModKeyBindings.DRAGONS_EYE_ABILITY.isPressed() && FMLClientHandler.instance().getClient().inGameHasFocus) {
					Dragon_Ability = 1;
				} else {
					Dragon_Ability = 0;
				}
				if(ModKeyBindings.DRAGONS_EYE_TARGET.isPressed() && FMLClientHandler.instance().getClient().inGameHasFocus) {
					TARGET = 1;
				} else {
					TARGET = 0;
				}
				if(ModKeyBindings.AUX_KEY.isKeyDown() && FMLClientHandler.instance().getClient().inGameHasFocus) {
					AUX = 1;
				} else {
					AUX = 0;
				}
				if(TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketPolarized)) {
					final ItemStack stack = TrinketHelper.getAccessory(player, ModItems.trinkets.TrinketPolarized);
					final IAccessoryInterface iCap = stack.getCapability(TrinketProvider.itemCapability, null);
					if(FMLClientHandler.instance().getClient().inGameHasFocus) {
						if((Polarized_Ability == 1) && (AUX == 0)) {
							iCap.setAbility(!iCap.ability());
							EffectsPolarizedStone.handleStatus(stack, iCap);
							if(iCap.ability()) {
								player.sendMessage(new TextComponentString("Collection Mode on (On)"));
							} else {
								player.sendMessage(new TextComponentString("Collection Mode off (off)"));
							}
							NetworkHandler.INSTANCE.sendToServer(new PolarizedStoneSyncPacket(player, stack, iCap, true, stack.getItemDamage()));
						}
						if((Polarized_Ability == 1) && (AUX == 1)) {
							iCap.setAltAbility(!iCap.altAbility());
							EffectsPolarizedStone.handleStatus(stack, iCap);
							if(iCap.altAbility()) {
								player.sendMessage(new TextComponentString("Repell Mode on (On)"));
							} else {
								player.sendMessage(new TextComponentString("Repell Mode off (off)"));
							}
							NetworkHandler.INSTANCE.sendToServer(new PolarizedStoneSyncPacket(player, stack, iCap, true, stack.getItemDamage()));
						}
					}
				}
				if(TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDragonsEye)) {
					final ItemStack stack = TrinketHelper.getAccessory(player, ModItems.trinkets.TrinketDragonsEye);
					final IAccessoryInterface iCap = stack.getCapability(TrinketProvider.itemCapability, null);
					if(iCap == null) {
						return;
					}
					if((Dragon_Ability == 1) && (AUX == 0)) {
						iCap.setAbility(!iCap.ability());
						NetworkHandler.sendItemDataServer(player, stack, iCap, true);
					}
					if((Dragon_Ability == 1) && (AUX == 1)) {
						iCap.setAltAbility(!iCap.altAbility());
						NetworkHandler.sendItemDataServer(player, stack, iCap, true);
					}
					if(TrinketsConfig.SERVER.DRAGON_EYE.oreFinder != false) {
						final int size = TrinketsConfig.SERVER.DRAGON_EYE.BLOCKS.Blocks.length;
						final int off = size - size -1;
						final int max = size - 1;

						if((TARGET == 1) && (AUX == 0)) {
							if(iCap.oreTarget() < size){
								iCap.setOreTarget(iCap.oreTarget()+1);
							}
							if(iCap.oreTarget() == size){
								iCap.setOreTarget(off);
							}
						}
						if((TARGET == 1) && (AUX == 1)) {
							if(iCap.oreTarget() > (off-1)){
								iCap.setOreTarget(iCap.oreTarget()-1);
							}
							if(iCap.oreTarget() == (off-1)){
								iCap.setOreTarget(max);
							}
						}
						if(iCap.oreTarget() > size) {
							iCap.setOreTarget(off);
						}
						if(TARGET == 1) {
							if((iCap.oreTarget() != off)) {
								String Type = TrinketsConfig.SERVER.DRAGON_EYE.BLOCKS.Blocks[iCap.oreTarget()];
								String getName = "Air";
								if(Type.contains(":") || Type.contains("[") || Type.contains("]")) {
									Type = Type.toLowerCase();
									getName = OreTrackingHelper.translateOreName(Type);
								} else {
									Type = Type.replace("ore", "");
									final String first = Type.substring(0, 1).toUpperCase();
									final String second = Type.substring(1).toLowerCase();
									getName = first + second;
								}
								if((iCap.oreTarget() != off)) {
									final String lul = getName.equalsIgnoreCase("Air")?" To Breath?" + TextFormatting.RED + " " + Type + " Doesn't Exist":" for my Treasury";
									player.sendMessage(new TextComponentString(TextFormatting.GOLD + "I need more " + TextFormatting.WHITE + getName + TextFormatting.GOLD + lul));
									NetworkHandler.sendItemDataServer(player, stack, iCap, true);
								} else {
									player.sendMessage(new TextComponentString("I think my Treasury is sufficiently full (off)"));
									NetworkHandler.sendItemDataServer(player, stack, iCap, true);
								}
								// Here
							} else { // Is On
								player.sendMessage(new TextComponentString("I think my Treasury is sufficiently full (off)"));
								NetworkHandler.sendItemDataServer(player, stack, iCap, true);
							}
						}
					}
				}
			}

		}
	}
}
