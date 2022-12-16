<<<<<<< Updated upstream
package xzeroair.trinkets.client.events;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.race.KeybindHandler.keyEnum;
import xzeroair.trinkets.client.keybinds.KeyHandler;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.entity.AlphaWolf;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.init.ModPotionTypes;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.keybinds.KeybindPacket;
import xzeroair.trinkets.network.keybinds.MovementKeyPacket;
import xzeroair.trinkets.network.trinketcontainer.OpenTrinketGui;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.interfaces.IKeyBindInterface;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ConfigHelper;
import xzeroair.trinkets.util.config.ConfigHelper.ArmorEntry;
import xzeroair.trinkets.util.config.ConfigHelper.MPRecoveryItem;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

@SideOnly(Side.CLIENT)
public class EventHandlerClient {

	private final String[] keys = {
			"Left",
			"Right",
			"Forward",
			"Backward",
			"Jump",
			"Sneak"
	};

	//	@SubscribeEvent
	//	public void mouseEvent(MouseEvent event) {
	//		System.out.println(event.getButton());
	//	}

	@SubscribeEvent
	public void clientTickEvent(TickEvent.ClientTickEvent event) {
		if ((event.side == Side.CLIENT) && (event.phase == Phase.START)) {
			final EntityPlayerSP player = Minecraft.getMinecraft().player;
			if ((player != null)) {
				if (TrinketsConfig.SERVER.GUI.guiEnabled && ModKeyBindings.TRINKET_GUI.isPressed()) {
					NetworkHandler.sendToServer(new OpenTrinketGui(Trinkets.GUI));
				}
			}
		}
		if (event.phase == Phase.END) {
			if (!FMLClientHandler.instance().getClient().inGameHasFocus) {
				return;
			}
			final EntityPlayerSP player = Minecraft.getMinecraft().player;
			if ((player != null) && (player.world != null)) {
				try {
					if (player.isRiding()) {
						final Entity mount = player.getRidingEntity();
						if (mount instanceof AlphaWolf) {
							final AlphaWolf wolf = (AlphaWolf) mount;
							if (player.movementInput.jump) {
								wolf.setJumping(true);
							}
						}
					}
				} catch (final Exception e) {
					e.printStackTrace();
				}
				final EntityProperties cap = Capabilities.getEntityProperties(player, prop -> {
					Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
					for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
						String key = entry.getKey();
						AbilityHolder holder = entry.getValue();
						try {
							IAbilityInterface ability = holder.getAbility();
							if ((ability instanceof IKeyBindInterface)) {
								final IKeyBindInterface keybind = (IKeyBindInterface) ability;
								final String keybind1 = keybind.getKey().replace(" ", "");
								final String keybind2 = keybind.getAuxKey().replace(" ", "");
								final boolean keyDown = !keybind1.isEmpty() ? ModKeyBindings.isKeyDownFromName(keybind1) : false;
								final boolean auxDown = !keybind2.isEmpty() ? ModKeyBindings.isKeyDownFromName(keybind2) : false;
								final KeyHandler keyHand = prop.getKeybindHandler().getKeyHandler(key + "." + keybind1);
								if (keyHand != null) {
									keyHand.handler(
											keyDown, press -> {
												if (keybind.onKeyPress(player, auxDown)) {
													NetworkHandler.sendToServer(new KeybindPacket(player, key, keyDown, auxDown, 0));
													return true;
												} else {
													return false;
												}
											},
											down -> {
												if (keybind.onKeyDown(player, auxDown)) {
													NetworkHandler.sendToServer(new KeybindPacket(player, key, keyDown, auxDown, 1));
													return true;
												} else {
													return false;
												}
											}, release -> {
												if (keybind.onKeyRelease(player, auxDown)) {
													NetworkHandler.sendToServer(new KeybindPacket(player, key, false, auxDown, 2));
													return true;
												} else {
													return false;
												}
											}
									);
								}
							}
						} catch (final Exception e) {
							Trinkets.log.error("Trinkets had an Error with Ability:" + key);
							e.printStackTrace();
						}
					}
					final GameSettings s = Minecraft.getMinecraft().gameSettings;
					keyEnum[] defaultKeys = keyEnum.values();
					for (keyEnum key : defaultKeys) {
						KeyBinding kb = null;
						switch (key) {
						case LEFT:
							kb = s.keyBindLeft;
							break;
						case RIGHT:
							kb = s.keyBindRight;
							break;
						case FORWARD:
							kb = s.keyBindForward;
							break;
						case BACK:
							kb = s.keyBindBack;
							break;
						case JUMP:
							kb = s.keyBindJump;
							break;
						case SNEAK:
							kb = s.keyBindSneak;
							break;
						default:
						}
						if (kb != null) {
							final KeyHandler handler = prop.getKeybindHandler().getKeyHandler(key.getName());
							if (handler != null) {
								handler.handler(kb.isKeyDown(), press -> {
									if (prop.getKeybindHandler().pressKey(player, key.getName(), 0)) {
										final MovementKeyPacket message = new MovementKeyPacket(player, key.getName(), 0);
										NetworkHandler.sendToServer(message);
										return true;
									} else {
										return false;
									}
								}, down -> {
									if (prop.getKeybindHandler().pressKey(player, key.getName(), 1)) {
										final MovementKeyPacket message = new MovementKeyPacket(player, key.getName(), 1);
										NetworkHandler.sendToServer(message);
										return true;
									} else {
										return false;
									}
								}, release -> {
									if (prop.getKeybindHandler().pressKey(player, key.getName(), 2)) {
										final MovementKeyPacket message = new MovementKeyPacket(player, key.getName(), 2);
										NetworkHandler.sendToServer(message);
										return true;
									} else {
										return false;
									}
								});
							}
						}
					}
					if (TrinketsConfig.SERVER.misc.movement && prop.getRaceHandler().isTransforming()) {
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
				});
			}
		}
	}

	@SubscribeEvent
	public void ItemToolTipEvent(ItemTooltipEvent event) {
		final EntityPlayer player = event.getEntityPlayer();
		if ((event.getEntityPlayer() == null)) {
			return;
		}
		if (player.getEntityWorld() == null) {
			return;
		}
		final ItemStack stack = event.getItemStack();
		final Item item = stack.getItem();
		final String regName = item.getRegistryName().toString();
		final String ModID = item.getRegistryName().getNamespace();
		final String ItemID = item.getRegistryName().getPath();
		if (TrinketsConfig.CLIENT.debug.showOreDictEntries) {
			for (final int or : OreDictionary.getOreIDs(stack)) {
				event.getToolTip().add(OreDictionary.getOreName(or));
			}
		}
		if (stack.getItem() instanceof ItemArmor) {
			final ItemArmor armor = ((ItemArmor) stack.getItem());
			if (TrinketsConfig.CLIENT.debug.debugArmorMaterials) {
				event.getToolTip().add(armor.getArmorMaterial().toString());
			}
			if (TrinketsConfig.SERVER.races.faelis.penalties) {
				boolean isFaelis = Capabilities.getEntityProperties(player, false, (prop, rtn) -> prop.getCurrentRace().equals(EntityRaces.faelis));
				if (isFaelis) {
					TreeMap<String, ArmorEntry> ArmorWeightValues = ConfigHelper.TrinketConfigStorage.ArmorWeightValues;
					for (ArmorEntry entry : ArmorWeightValues.values()) {
						if (entry.doesItemMatchEntry(stack)) {
							if (entry.getWeight() != 0) {
								String color = entry.getWeight() > 0 ? "" + TextFormatting.RED : "" + TextFormatting.BLUE;
								event.getToolTip().add("Weight: " + color + entry.getWeight());
								break;
							}
						}
					}
				}
			}
		}
		if (TrinketsConfig.SERVER.mana.mana_enabled) {
			try {
				final TreeMap<String, MPRecoveryItem> MagicRecoveryItems = ConfigHelper.TrinketConfigStorage.MagicRecoveryItems;
				float amount = 0;
				boolean multiplied = false;
				for (MPRecoveryItem entry : MagicRecoveryItems.values()) {
					if (entry.doesItemMatchEntry(stack)) {
						amount = entry.getAmount();
						multiplied = entry.isMultiplied();
						break;
					}
				}
				if (amount != 0) {
					String additive = amount > 0 ? TextFormatting.DARK_AQUA + "+" : TextFormatting.DARK_RED + "-";
					if (multiplied) {
						event.getToolTip().add(additive + amount + "% MP");
					} else {
						event.getToolTip().add(additive + amount + " MP");
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		if (stack.getItem().equals(Items.POTIONITEM)) {
			final PotionType pot = PotionUtils.getPotionFromItem(stack);
			if (ModPotionTypes.TrinketPotionTypes.containsValue(pot)) {
				final TranslationHelper helper = TranslationHelper.INSTANCE;
				final KeyEntry human = new OptionEntry("humanticks", (MathHelper.clamp((TrinketsConfig.SERVER.Potion.human.Duration), 0, Integer.MAX_VALUE) / 20F));
				final KeyEntry fairy = new OptionEntry("fairyticks", (MathHelper.clamp((TrinketsConfig.SERVER.Potion.fairy.Duration), 0, Integer.MAX_VALUE) / 20F));
				final KeyEntry dwarf = new OptionEntry("dwarfticks", (MathHelper.clamp((TrinketsConfig.SERVER.Potion.dwarf.Duration), 0, Integer.MAX_VALUE) / 20F));
				final KeyEntry titan = new OptionEntry("titanticks", (MathHelper.clamp((TrinketsConfig.SERVER.Potion.titan.Duration), 0, Integer.MAX_VALUE) / 20F));
				final KeyEntry goblin = new OptionEntry("goblinticks", (MathHelper.clamp((TrinketsConfig.SERVER.Potion.goblin.Duration), 0, Integer.MAX_VALUE) / 20F));
				final KeyEntry elf = new OptionEntry("elfticks", (MathHelper.clamp((TrinketsConfig.SERVER.Potion.elf.Duration), 0, Integer.MAX_VALUE) / 20F));
				final KeyEntry faelis = new OptionEntry("faelisticks", (MathHelper.clamp((TrinketsConfig.SERVER.Potion.faelis.Duration), 0, Integer.MAX_VALUE) / 20F));
				final KeyEntry dragon = new OptionEntry("dragonticks", (MathHelper.clamp((TrinketsConfig.SERVER.Potion.dragon.Duration), 0, Integer.MAX_VALUE) / 20F));
				for (int i = 1; i < 10; i++) {
					final String key = Reference.MODID + "." + stack.getTranslationKey() + "." + pot.getRegistryName().getPath() + ".tooltip" + i;
					final String string = helper.getLangTranslation(
							key,
							lang -> helper.formatAddVariables(lang, human, fairy, dwarf, titan, goblin, elf, faelis, dragon)
					);
					if (!helper.isStringEmpty(string)) {
						event.getToolTip().add(
								string
						);
					}
				}
			}
		}
	}
}
=======
package xzeroair.trinkets.client.events;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.race.KeybindHandler.keyEnum;
import xzeroair.trinkets.client.keybinds.KeyHandler;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.entity.AlphaWolf;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.init.ModPotionTypes;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.keybinds.KeybindPacket;
import xzeroair.trinkets.network.keybinds.MovementKeyPacket;
import xzeroair.trinkets.network.trinketcontainer.OpenTrinketGui;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.interfaces.IKeyBindInterface;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ConfigHelper;
import xzeroair.trinkets.util.config.ConfigHelper.ArmorEntry;
import xzeroair.trinkets.util.config.ConfigHelper.MPRecoveryItem;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

@SideOnly(Side.CLIENT)
public class EventHandlerClient {

	private final String[] keys = {
			"Left",
			"Right",
			"Forward",
			"Backward",
			"Jump",
			"Sneak"
	};

	//	@SubscribeEvent
	//	public void mouseEvent(MouseEvent event) {
	//		System.out.println(event.getButton());
	//	}

	@SubscribeEvent
	public void clientTickEvent(TickEvent.ClientTickEvent event) {
		if ((event.side == Side.CLIENT) && (event.phase == Phase.START)) {
			final EntityPlayerSP player = Minecraft.getMinecraft().player;
			if ((player != null)) {
				if (TrinketsConfig.SERVER.GUI.guiEnabled && ModKeyBindings.TRINKET_GUI.isPressed()) {
					NetworkHandler.sendToServer(new OpenTrinketGui(Trinkets.GUI));
				}
			}
		}
		if (event.phase == Phase.END) {
			if (!FMLClientHandler.instance().getClient().inGameHasFocus) {
				return;
			}
			final EntityPlayerSP player = Minecraft.getMinecraft().player;
			if ((player != null) && (player.world != null)) {
				try {
					if (player.isRiding()) {
						final Entity mount = player.getRidingEntity();
						if (mount instanceof AlphaWolf) {
							final AlphaWolf wolf = (AlphaWolf) mount;
							if (player.movementInput.jump) {
								wolf.setJumping(true);
							}
						}
					}
				} catch (final Exception e) {
					e.printStackTrace();
				}
				final EntityProperties cap = Capabilities.getEntityProperties(player, prop -> {
					Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
					for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
						String key = entry.getKey();
						AbilityHolder holder = entry.getValue();
						try {
							IAbilityInterface ability = holder.getAbility();
							if ((ability instanceof IKeyBindInterface)) {
								final IKeyBindInterface keybind = (IKeyBindInterface) ability;
								final String keybind1 = keybind.getKey().replace(" ", "");
								final String keybind2 = keybind.getAuxKey().replace(" ", "");
								final boolean keyDown = !keybind1.isEmpty() ? ModKeyBindings.isKeyDownFromName(keybind1) : false;
								final boolean auxDown = !keybind2.isEmpty() ? ModKeyBindings.isKeyDownFromName(keybind2) : false;
								final KeyHandler keyHand = prop.getKeybindHandler().getKeyHandler(key + "." + keybind1);
								if (keyHand != null) {
									keyHand.handler(
											keyDown, press -> {
												if (keybind.onKeyPress(player, auxDown)) {
													NetworkHandler.sendToServer(new KeybindPacket(player, key, keyDown, auxDown, 0));
													return true;
												} else {
													return false;
												}
											},
											down -> {
												if (keybind.onKeyDown(player, auxDown)) {
													NetworkHandler.sendToServer(new KeybindPacket(player, key, keyDown, auxDown, 1));
													return true;
												} else {
													return false;
												}
											}, release -> {
												if (keybind.onKeyRelease(player, auxDown)) {
													NetworkHandler.sendToServer(new KeybindPacket(player, key, false, auxDown, 2));
													return true;
												} else {
													return false;
												}
											}
									);
								}
							}
						} catch (final Exception e) {
							Trinkets.log.error("Trinkets had an Error with Ability:" + key);
							e.printStackTrace();
						}
					}
					final GameSettings s = Minecraft.getMinecraft().gameSettings;
					keyEnum[] defaultKeys = keyEnum.values();
					for (keyEnum key : defaultKeys) {
						KeyBinding kb = null;
						switch (key) {
						case LEFT:
							kb = s.keyBindLeft;
							break;
						case RIGHT:
							kb = s.keyBindRight;
							break;
						case FORWARD:
							kb = s.keyBindForward;
							break;
						case BACK:
							kb = s.keyBindBack;
							break;
						case JUMP:
							kb = s.keyBindJump;
							break;
						case SNEAK:
							kb = s.keyBindSneak;
							break;
						default:
						}
						if (kb != null) {
							final KeyHandler handler = prop.getKeybindHandler().getKeyHandler(key.getName());
							if (handler != null) {
								handler.handler(kb.isKeyDown(), press -> {
									if (prop.getKeybindHandler().pressKey(player, key.getName(), 0)) {
										final MovementKeyPacket message = new MovementKeyPacket(player, key.getName(), 0);
										NetworkHandler.sendToServer(message);
										return true;
									} else {
										return false;
									}
								}, down -> {
									if (prop.getKeybindHandler().pressKey(player, key.getName(), 1)) {
										final MovementKeyPacket message = new MovementKeyPacket(player, key.getName(), 1);
										NetworkHandler.sendToServer(message);
										return true;
									} else {
										return false;
									}
								}, release -> {
									if (prop.getKeybindHandler().pressKey(player, key.getName(), 2)) {
										final MovementKeyPacket message = new MovementKeyPacket(player, key.getName(), 2);
										NetworkHandler.sendToServer(message);
										return true;
									} else {
										return false;
									}
								});
							}
						}
					}
					if (TrinketsConfig.SERVER.misc.movement && prop.getRaceHandler().isTransforming()) {
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
				});
			}
		}
	}

	@SubscribeEvent
	public void ItemToolTipEvent(ItemTooltipEvent event) {
		final EntityPlayer player = event.getEntityPlayer();
		if ((event.getEntityPlayer() == null)) {
			return;
		}
		if (player.getEntityWorld() == null) {
			return;
		}
		final ItemStack stack = event.getItemStack();
		final Item item = stack.getItem();
		final String regName = item.getRegistryName().toString();
		final String ModID = item.getRegistryName().getNamespace();
		final String ItemID = item.getRegistryName().getPath();
		if (TrinketsConfig.CLIENT.debug.showOreDictEntries) {
			for (final int or : OreDictionary.getOreIDs(stack)) {
				event.getToolTip().add(OreDictionary.getOreName(or));
			}
		}
		if (stack.getItem() instanceof ItemArmor) {
			final ItemArmor armor = ((ItemArmor) stack.getItem());
			if (TrinketsConfig.CLIENT.debug.debugArmorMaterials) {
				event.getToolTip().add(armor.getArmorMaterial().toString());
			}
			if (TrinketsConfig.SERVER.races.faelis.penalties) {
				boolean isFaelis = Capabilities.getEntityProperties(player, false, (prop, rtn) -> prop.getCurrentRace().equals(EntityRaces.faelis));
				if (isFaelis) {
					TreeMap<String, ArmorEntry> ArmorWeightValues = ConfigHelper.TrinketConfigStorage.ArmorWeightValues;
					for (ArmorEntry entry : ArmorWeightValues.values()) {
						if (entry.doesItemMatchEntry(stack)) {
							if (entry.getWeight() != 0) {
								String color = entry.getWeight() > 0 ? "" + TextFormatting.RED : "" + TextFormatting.BLUE;
								event.getToolTip().add("Weight: " + color + entry.getWeight());
								break;
							}
						}
					}
				}
			}
		}
		if (TrinketsConfig.SERVER.mana.mana_enabled) {
			try {
				final TreeMap<String, MPRecoveryItem> MagicRecoveryItems = ConfigHelper.TrinketConfigStorage.MagicRecoveryItems;
				float amount = 0;
				boolean multiplied = false;
				for (MPRecoveryItem entry : MagicRecoveryItems.values()) {
					if (entry.doesItemMatchEntry(stack)) {
						amount = entry.getAmount();
						multiplied = entry.isMultiplied();
						break;
					}
				}
				if (amount != 0) {
					String additive = amount > 0 ? TextFormatting.DARK_AQUA + "+" : TextFormatting.DARK_RED + "-";
					if (multiplied) {
						event.getToolTip().add(additive + amount + "% MP");
					} else {
						event.getToolTip().add(additive + amount + " MP");
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		if (stack.getItem().equals(Items.POTIONITEM)) {
			final PotionType pot = PotionUtils.getPotionFromItem(stack);
			if (ModPotionTypes.TrinketPotionTypes.containsValue(pot)) {
				final TranslationHelper helper = TranslationHelper.INSTANCE;
				final KeyEntry human = new OptionEntry("humanticks", (MathHelper.clamp((TrinketsConfig.SERVER.Potion.human.Duration), 0, Integer.MAX_VALUE) / 20F));
				final KeyEntry fairy = new OptionEntry("fairyticks", (MathHelper.clamp((TrinketsConfig.SERVER.Potion.fairy.Duration), 0, Integer.MAX_VALUE) / 20F));
				final KeyEntry dwarf = new OptionEntry("dwarfticks", (MathHelper.clamp((TrinketsConfig.SERVER.Potion.dwarf.Duration), 0, Integer.MAX_VALUE) / 20F));
				final KeyEntry titan = new OptionEntry("titanticks", (MathHelper.clamp((TrinketsConfig.SERVER.Potion.titan.Duration), 0, Integer.MAX_VALUE) / 20F));
				final KeyEntry goblin = new OptionEntry("goblinticks", (MathHelper.clamp((TrinketsConfig.SERVER.Potion.goblin.Duration), 0, Integer.MAX_VALUE) / 20F));
				final KeyEntry elf = new OptionEntry("elfticks", (MathHelper.clamp((TrinketsConfig.SERVER.Potion.elf.Duration), 0, Integer.MAX_VALUE) / 20F));
				final KeyEntry faelis = new OptionEntry("faelisticks", (MathHelper.clamp((TrinketsConfig.SERVER.Potion.faelis.Duration), 0, Integer.MAX_VALUE) / 20F));
				final KeyEntry dragon = new OptionEntry("dragonticks", (MathHelper.clamp((TrinketsConfig.SERVER.Potion.dragon.Duration), 0, Integer.MAX_VALUE) / 20F));
				for (int i = 1; i < 10; i++) {
					final String key = Reference.MODID + "." + stack.getTranslationKey() + "." + pot.getRegistryName().getPath() + ".tooltip" + i;
					final String string = helper.getLangTranslation(
							key,
							lang -> helper.formatAddVariables(lang, human, fairy, dwarf, titan, goblin, elf, faelis, dragon)
					);
					if (!helper.isStringEmpty(string)) {
						event.getToolTip().add(
								string
						);
					}
				}
			}
		}
	}
}
>>>>>>> Stashed changes
