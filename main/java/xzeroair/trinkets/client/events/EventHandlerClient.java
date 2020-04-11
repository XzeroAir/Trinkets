package xzeroair.trinkets.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.init.ModPotionTypes;
import xzeroair.trinkets.items.effects.EffectsPolarizedStone;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.OpenTrinketGui;
import xzeroair.trinkets.network.PolarizedStoneSyncPacket;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.OreTrackingHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class EventHandlerClient {

	int Dragon_Ability = 0;
	int Polarized_Ability = 0;
	int TARGET = 0;
	int AUX = 0;

	@SubscribeEvent
	public void clientTickEvent(TickEvent.ClientTickEvent event) {
		if ((event.side == Side.CLIENT) && (event.phase == Phase.START)) {
			final EntityPlayerSP player = Minecraft.getMinecraft().player;
			if ((player != null)) {
				if (TrinketsConfig.SERVER.GUI.guiEnabled && ModKeyBindings.TRINKET_GUI.isPressed()) {
					NetworkHandler.INSTANCE.sendToServer(new OpenTrinketGui());
				}
			}
		}
		if (event.phase == Phase.END) {
			final EntityPlayerSP player = Minecraft.getMinecraft().player;
			if ((player != null)) {

				if (ModKeyBindings.POLARIZED_STONE_ABILITY.isPressed() && FMLClientHandler.instance().getClient().inGameHasFocus) {
					this.Polarized_Ability = 1;
				} else {
					this.Polarized_Ability = 0;
				}
				if (ModKeyBindings.DRAGONS_EYE_ABILITY.isPressed() && FMLClientHandler.instance().getClient().inGameHasFocus) {
					this.Dragon_Ability = 1;
				} else {
					this.Dragon_Ability = 0;
				}
				if (ModKeyBindings.DRAGONS_EYE_TARGET.isPressed() && FMLClientHandler.instance().getClient().inGameHasFocus) {
					this.TARGET = 1;
				} else {
					this.TARGET = 0;
				}
				if (ModKeyBindings.AUX_KEY.isKeyDown() && FMLClientHandler.instance().getClient().inGameHasFocus) {
					this.AUX = 1;
				} else {
					this.AUX = 0;
				}
				if (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketPolarized)) {
					final ItemStack stack = TrinketHelper.getAccessory(player, ModItems.trinkets.TrinketPolarized);
					TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
					if (FMLClientHandler.instance().getClient().inGameHasFocus) {
						TextComponentString magnet = new TextComponentString(TranslationHelper.formatLangKeys(stack, new TextComponentTranslation(stack.getTranslationKey() + ".magnetmode")));
						TextComponentString repel = new TextComponentString(TranslationHelper.formatLangKeys(stack, new TextComponentTranslation(stack.getTranslationKey() + ".repelmode")));
						if ((this.Polarized_Ability == 1) && (this.AUX == 0)) {
							iCap.toggleMainAbility(!iCap.mainAbility());
							EffectsPolarizedStone.handleStatus(stack, iCap);
							player.sendMessage(magnet);
							NetworkHandler.INSTANCE.sendToServer(new PolarizedStoneSyncPacket(player, stack, iCap, true, stack.getItemDamage()));
						}
						if ((this.Polarized_Ability == 1) && (this.AUX == 1)) {
							iCap.toggleAltAbility(!iCap.altAbility());
							EffectsPolarizedStone.handleStatus(stack, iCap);
							player.sendMessage(repel);
							NetworkHandler.INSTANCE.sendToServer(new PolarizedStoneSyncPacket(player, stack, iCap, true, stack.getItemDamage()));
						}
					}
				}
				if (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDragonsEye)) {
					final ItemStack stack = TrinketHelper.getAccessory(player, ModItems.trinkets.TrinketDragonsEye);
					TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
					if (iCap == null) {
						return;
					}
					if ((this.Dragon_Ability == 1) && (this.AUX == 0)) {
						iCap.toggleMainAbility(!iCap.mainAbility());
						NetworkHandler.sendItemDataServer(player, stack, iCap, true);
					}
					if ((this.Dragon_Ability == 1) && (this.AUX == 1)) {
						iCap.toggleAltAbility(!iCap.altAbility());
					}
					if (TrinketsConfig.SERVER.DRAGON_EYE.oreFinder != false) {
						final int size = TrinketsConfig.SERVER.DRAGON_EYE.BLOCKS.Blocks.length;
						final int off = size - size - 1;
						final int max = size - 1;

						if ((this.TARGET == 1) && (this.AUX == 0)) {
							if (iCap.Target() < size) {
								iCap.setTarget(iCap.Target() + 1);
							}
							if (iCap.Target() == size) {
								iCap.setTarget(off);
							}
						}
						if ((this.TARGET == 1) && (this.AUX == 1)) {
							if (iCap.Target() > (off - 1)) {
								iCap.setTarget(iCap.Target() - 1);
							}
							if (iCap.Target() == (off - 1)) {
								iCap.setTarget(max);
							}
						}
						if (iCap.Target() > size) {
							iCap.setTarget(off);
						}
						if (this.TARGET == 1) {
							TextComponentTranslation UnkownTarget = new TextComponentTranslation(stack.getTranslationKey() + ".treasurefinder.notfound");
							TextComponentTranslation FinderOn = new TextComponentTranslation(stack.getTranslationKey() + ".treasurefinder.on");
							TextComponentTranslation FinderOff = new TextComponentTranslation(stack.getTranslationKey() + ".treasurefinder.off");
							if ((iCap.Target() != off)) {
								String Type = TrinketsConfig.SERVER.DRAGON_EYE.BLOCKS.Blocks[iCap.Target()];
								String getName = "Air";
								if (Type.contains(":") || Type.contains("[") || Type.contains("]")) {
									Type = Type.toLowerCase();
									getName = OreTrackingHelper.translateOreName(Type);
								} else {
									Type = Type.replace("ore", "");
									final String first = Type.substring(0, 1).toUpperCase();
									final String second = Type.substring(1).toLowerCase();
									getName = first + second;
								}
								if ((iCap.Target() != off)) {
									String NotFound = TranslationHelper.formatLangKeys(stack, UnkownTarget);
									String FoundTarget = TranslationHelper.formatLangKeys(stack, FinderOn);
									String Message = getName.equalsIgnoreCase("Air") ? NotFound : FoundTarget;
									player.sendMessage(new TextComponentString(Message));
									NetworkHandler.sendItemDataServer(player, stack, iCap, true);
								} else {
									String offMode = TranslationHelper.formatLangKeys(stack, FinderOff);
									player.sendMessage(new TextComponentString(offMode));
									NetworkHandler.sendItemDataServer(player, stack, iCap, true);
								}
								// Here
							} else { // Is On
								String offMode = TranslationHelper.formatLangKeys(stack, FinderOff);
								player.sendMessage(new TextComponentString(offMode));
								NetworkHandler.sendItemDataServer(player, stack, iCap, true);
							}
						}
					}
				}
			}

		}
	}

	@SubscribeEvent
	public void ItemToolTipEvent(ItemTooltipEvent event) {
		if ((event.getItemStack() != null) && (event.getEntityPlayer() != null)) {
			ItemStack stack = event.getItemStack();
			if (stack.getItem().equals(Items.POTIONITEM)) {
				final PotionType pot = PotionUtils.getPotionFromItem(stack);
				if (pot.equals(ModPotionTypes.Enhanced) ||
						pot.equals(ModPotionTypes.Restorative) ||
						pot.equals(ModPotionTypes.DwarfType) ||
						pot.equals(ModPotionTypes.FairyType) ||
						pot.equals(ModPotionTypes.TitanType)) {
					TranslationHelper.addPotionTooltips(stack, event.getEntityPlayer().world, event.getToolTip());
				}
			}
		}
	}
}
