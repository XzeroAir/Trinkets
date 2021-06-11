package xzeroair.trinkets.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.client.keybinds.KeyHandler;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.init.ModPotionTypes;
import xzeroair.trinkets.items.effects.EffectsPolarizedStone;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.PolarizedStoneSyncPacket;
import xzeroair.trinkets.network.arcingorb.ArcingOrbAttackPacket;
import xzeroair.trinkets.network.trinketcontainer.OpenTrinketGui;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class EventHandlerClient {

	//	int Dragon_Ability = 0;
	//	int Polarized_Ability = 0;
	//	int TARGET = 0;
	//	int AUX = 0;

	KeyHandler Aux = new KeyHandler(ModKeyBindings.AUX_KEY);

	KeyHandler PS_Toggle = new KeyHandler(ModKeyBindings.POLARIZED_STONE_ABILITY);
	KeyHandler TGui = new KeyHandler(ModKeyBindings.TRINKET_GUI);
	KeyHandler AO_Atk = new KeyHandler(ModKeyBindings.ARCING_ORB_ABILITY);

	private boolean check, check2, check3 = false;
	private int tick, tick2, tick3, tick4 = 0;

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
			if (!FMLClientHandler.instance().getClient().inGameHasFocus) {
				return;
			}
			final EntityPlayerSP player = Minecraft.getMinecraft().player;
			if ((player != null) && (player.world != null)) {

				EntityProperties cap = Capabilities.getEntityRace(player);
				float maxMP = cap.getMagic().getMaxMana();
				float MP = cap.getMagic().getMana();
				if (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketPolarized)) {
					final ItemStack stack = TrinketHelper.getAccessory(player, ModItems.trinkets.TrinketPolarized);
					TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
					PS_Toggle.Pressed(iCap, press -> {
						if (!Aux.isDown()) {
							iCap.toggleMainAbility(!iCap.mainAbility());
							EffectsPolarizedStone.handleStatus(stack, iCap);
							TextComponentString magnet = new TextComponentString(TranslationHelper.formatLangKeys(stack, new TextComponentTranslation(stack.getTranslationKey() + ".magnetmode")));
							player.sendStatusMessage(magnet, true);
							NetworkHandler.INSTANCE.sendToServer(new PolarizedStoneSyncPacket(player, stack, iCap, iCap.getSlot(), iCap.getHandler(), stack.getItemDamage()));
						} else {
							iCap.toggleAltAbility(!iCap.altAbility());
							EffectsPolarizedStone.handleStatus(stack, iCap);
							TextComponentString repel = new TextComponentString(TranslationHelper.formatLangKeys(stack, new TextComponentTranslation(stack.getTranslationKey() + ".repelmode")));
							player.sendStatusMessage(repel, true);
							NetworkHandler.INSTANCE.sendToServer(new PolarizedStoneSyncPacket(player, stack, iCap, iCap.getSlot(), iCap.getHandler(), stack.getItemDamage()));
						}
						return true;
					});
				}
				if ((cap != null)) {
					if (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketArcingOrb)) {
						float iCost = TrinketsConfig.SERVER.Items.ARCING_ORB.attackCost;
						AO_Atk.handler(
								player,
								press -> {
									if ((iCost >= 25F) && (MP < 25F)) {
										return false;
									} else {
										return true;
									}
								},
								down -> {
									//TODO Redo Cost for this
									float percentage = MathHelper.clamp(
											(((AO_Atk.heldDuration()) / iCost)),
											0F,
											((MP / iCost) * 1.5F)
									);
									if (percentage < ((MP / iCost) * 1.5F)) {
										if (percentage <= (MP / iCost)) {
											player.getEntityWorld().playSound(player, player.getPosition(), SoundEvents.BLOCK_NOTE_BELL, SoundCategory.PLAYERS, 0.1F, 1f - percentage);
											ScreenOverlayEvents.instance.SyncCost(iCost * MathHelper.clamp(percentage, 0F, 1F));
										} else {
											ScreenOverlayEvents.instance.SyncCost(iCost);
											player.getEntityWorld().playSound(player, player.getPosition(), SoundEvents.BLOCK_NOTE_XYLOPHONE, SoundCategory.PLAYERS, 0.1F, 1f - percentage);
										}
										return true;
									} else {
										player.world.playSound(player.posX, player.posY, player.posZ, SoundEvents.ENTITY_LIGHTNING_IMPACT, SoundCategory.WEATHER, 0.3F, 1F, false);
										return false;
									}
								},
								release -> {
									if (!player.isSneaking()) {
										NetworkHandler.INSTANCE.sendToServer(new ArcingOrbAttackPacket(player, AO_Atk.heldDuration()));
									}
									return true;
								}
						);
					}
					cap.getRaceProperties().onClientTick();
					if (TrinketsConfig.SERVER.misc.movement && cap.isTransforming()) {
						//						player.setVelocity(0, player.motionY, 0);
						if ((player.posX != player.prevPosX) || (player.posZ != player.prevPosZ)) {
							player.setPositionAndUpdate(player.prevPosX, player.posY, player.prevPosZ);
						}
						if (player == Minecraft.getMinecraft().player) {
							KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), false);
							KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindBack.getKeyCode(), false);
							KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindLeft.getKeyCode(), false);
							KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindRight.getKeyCode(), false);
							//							if (((EntityPlayer) player).capabilities.isFlying) {
							KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode(), false);
							KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), false);
							//							}
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
			//			for (int or : OreDictionary.getOreIDs(stack)) {
			//				event.getToolTip().add(OreDictionary.getOreName(or));
			//			}
			String[] recoveryItems = TrinketsConfig.SERVER.mana.recovery;
			for (String item : recoveryItems) {
				String[] itemConfig = item.split(":");
				if (itemConfig.length > 0) {
					if (stack.getItem().getRegistryName().toString().equalsIgnoreCase(itemConfig[0] + ":" + itemConfig[1])) {
						if (itemConfig.length > 1) {
							if (stack.getItemDamage() == Integer.parseInt(itemConfig[2])) {
								if (itemConfig.length > 2) {
									String recoveryAmount = itemConfig[3];
									if (recoveryAmount.endsWith("%")) {
										float amount = MathHelper.clamp(Float.parseFloat(recoveryAmount.replace("%", "")), 0, 100);
										event.getToolTip().add(TextFormatting.DARK_AQUA + "+" + amount + "% MP");
									} else {
										float amount = Float.parseFloat(recoveryAmount);
										event.getToolTip().add(TextFormatting.DARK_AQUA + "+" + amount + " MP");
									}
								}
							}
						}
					}
				}
			}
			if (stack.getItem().equals(Items.POTIONITEM)) {
				final PotionType pot = PotionUtils.getPotionFromItem(stack);
				if (ModPotionTypes.TrinketPotionTypes.containsValue(pot)) {
					TranslationHelper.addPotionTooltips(stack, event.getEntityPlayer().world, event.getToolTip());
				}
			}
		}
	}
}
