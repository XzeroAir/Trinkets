package xzeroair.trinkets.client.events;

import java.util.Map;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.client.keybinds.IKeyBindInterface;
import xzeroair.trinkets.client.keybinds.KeyHandler;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.entity.AlphaWolf;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.init.ModPotionTypes;
import xzeroair.trinkets.items.effects.EffectsPolarizedStone;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.PolarizedStoneSyncPacket;
import xzeroair.trinkets.network.keybinds.KeybindPacket;
import xzeroair.trinkets.network.keybinds.MovementKeyPacket;
import xzeroair.trinkets.network.trinketcontainer.OpenTrinketGui;
import xzeroair.trinkets.traits.abilities.IAbilityHandler;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ConfigHelper;
import xzeroair.trinkets.util.config.ConfigHelper.ItemObjectHolder;
import xzeroair.trinkets.util.helpers.TranslationHelper;

@SideOnly(Side.CLIENT)
public class EventHandlerClient {

	KeyHandler key = new KeyHandler();
	//	KeyHandler Aux = new KeyHandler(ModKeyBindings.AUX_KEY);
	//
	//	KeyHandler PS_Toggle = new KeyHandler(ModKeyBindings.POLARIZED_STONE_ABILITY);
	KeyHandler PS_Toggle = new KeyHandler();
	//	KeyHandler TGui = new KeyHandler(ModKeyBindings.TRINKET_GUI);
	//	KeyHandler AO_Atk = new KeyHandler(ModKeyBindings.ARCING_ORB_ABILITY);
	//	KeyHandler ability = new KeyHandler(ModKeyBindings.RACE_ABILITY);

	private boolean check, check2;
	private final boolean check3 = false;
	private int tick, tick2, tick3;
	private final int tick4 = 0;

	private final String[] keys = {
			"Left",
			"Right",
			"Forward",
			"Backward",
			"Jump",
			"Sneak"
	};

	@SubscribeEvent
	public void mouseEvent(MouseEvent event) {
		//		System.out.println(event.getButton());
	}

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
				final EntityProperties cap = Capabilities.getEntityRace(player);
				if (cap != null) {
					for (final IAbilityInterface ability : cap.getAbilityHandler().getAbilitiesList()) {
						try {
							final IAbilityHandler handler = cap.getAbilityHandler().getAbilityInstance(ability);
							if ((handler != null)) {
								if ((handler instanceof IKeyBindInterface)) {
									final IKeyBindInterface keybind = (IKeyBindInterface) handler;
									final String keybind1 = keybind.getKey();
									final String keybind2 = keybind.getAuxKey();
									final int key1 = (keybind1 == null) || keybind1.isEmpty() ? 0 : Keyboard.getKeyIndex(keybind1);
									final int key2 = (keybind2 == null) || keybind2.isEmpty() ? 0 : Keyboard.getKeyIndex(keybind2);
									final boolean keyDown = (keybind1 == null) || keybind1.isEmpty() ? false : Keyboard.isKeyDown(key1);
									final boolean auxDown = (keybind2 == null) || keybind2.isEmpty() ? false : Keyboard.isKeyDown(key2);
									final KeyHandler keyHand = cap.getKeybindHandler().getKeyHandler(ability.getName() + "." + keybind1);
									//									if (handler instanceof AbilityBlockFinder) {
									//										System.out.println(keybind1 + " | " + key1 + " | " + keyDown + " | " + (ability.getName() + "." + keybind1) + " | ");
									//									}
									if (keyHand != null) {
										keyHand.handler(
												keyDown, press -> {
													if (keybind.onKeyPress(player, auxDown)) {
														NetworkHandler.sendToServer(new KeybindPacket(player, ability, keyDown, auxDown, 0));
														return true;
													} else {
														return false;
													}
												},
												down -> {
													if (keybind.onKeyDown(player, auxDown)) {
														NetworkHandler.sendToServer(new KeybindPacket(player, ability, keyDown, auxDown, 1));
														return true;
													} else {
														return false;
													}
												}, release -> {
													if (keybind.onKeyRelease(player, auxDown)) {
														NetworkHandler.sendToServer(new KeybindPacket(player, ability, false, auxDown, 2));
														return true;
													} else {
														return false;
													}
												}
										);
									}
								}
							}
						} catch (final Exception e) {
							Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
							e.printStackTrace();
						}
					}
					final GameSettings s = Minecraft.getMinecraft().gameSettings;
					for (final String name : keys) {
						KeyBinding kb = null;
						if (name.contentEquals("Left")) {
							kb = s.keyBindLeft;
						} else if (name.contentEquals("Right")) {
							kb = s.keyBindRight;
						} else if (name.contentEquals("Forward")) {
							kb = s.keyBindForward;
						} else if (name.contentEquals("Backward")) {
							kb = s.keyBindBack;
						} else if (name.contentEquals("Jump")) {
							kb = s.keyBindJump;
						} else if (name.contentEquals("Sneak")) {
							kb = s.keyBindSneak;
						} else {
						}
						if (kb == null) {
							return;
						}
						final KeyHandler handler = cap.getKeybindHandler().getKeyHandler(name);
						if (handler != null) {
							handler.handler(kb.isKeyDown(), press -> {
								if (cap.getKeybindHandler().pressKey(player, name, 0)) {
									final MovementKeyPacket message = new MovementKeyPacket(player, name, 0);
									NetworkHandler.sendToServer(message);
									return true;
								} else {
									return false;
								}
							}, down -> {
								if (cap.getKeybindHandler().pressKey(player, name, 1)) {
									final MovementKeyPacket message = new MovementKeyPacket(player, name, 1);
									NetworkHandler.sendToServer(message);
									return true;
								} else {
									return false;
								}
							}, release -> {
								if (cap.getKeybindHandler().pressKey(player, name, 2)) {
									final MovementKeyPacket message = new MovementKeyPacket(player, name, 2);
									NetworkHandler.sendToServer(message);
									return true;
								} else {
									return false;
								}
							});
						}
					}
					if (TrinketsConfig.SERVER.misc.movement && cap.isTransforming()) {
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
				// TODO Need to fix this and Turn it into an ability
				if (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketPolarized)) {
					final ItemStack stack = TrinketHelper.getAccessory(player, ModItems.trinkets.TrinketPolarized);
					final TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
					PS_Toggle.Pressed(iCap, ModKeyBindings.POLARIZED_STONE_ABILITY.isKeyDown(), press -> {
						if (!ModKeyBindings.AUX_KEY.isKeyDown()) {
							iCap.toggleMainAbility(!iCap.mainAbility());
							EffectsPolarizedStone.handleStatus(stack, iCap);
							final TextComponentString magnet = new TextComponentString(TranslationHelper.formatLangKeys(stack, new TextComponentTranslation(stack.getTranslationKey() + ".magnetmode")));
							player.sendStatusMessage(magnet, true);
							NetworkHandler.sendToServer(new PolarizedStoneSyncPacket(player, stack, iCap, iCap.getSlot(), iCap.getHandler(), stack.getItemDamage()));
						} else {
							iCap.toggleAltAbility(!iCap.altAbility());
							EffectsPolarizedStone.handleStatus(stack, iCap);
							final TextComponentString repel = new TextComponentString(TranslationHelper.formatLangKeys(stack, new TextComponentTranslation(stack.getTranslationKey() + ".repelmode")));
							player.sendStatusMessage(repel, true);
							NetworkHandler.sendToServer(new PolarizedStoneSyncPacket(player, stack, iCap, iCap.getSlot(), iCap.getHandler(), stack.getItemDamage()));
						}
						return true;
					});
				}
			}
		}
	}

	@SubscribeEvent
	public void ItemToolTipEvent(ItemTooltipEvent event) {
		if ((event.getItemStack() != null) && (event.getEntityPlayer() != null)) {
			final ItemStack stack = event.getItemStack();
			//			for (int or : OreDictionary.getOreIDs(stack)) {
			//				event.getToolTip().add(OreDictionary.getOreName(or));
			//			}
			if (stack.getItem() instanceof ItemArmor) {
				final ItemArmor armor = ((ItemArmor) stack.getItem());
				if (TrinketsConfig.CLIENT.debug.debugArmorMaterials) {
					event.getToolTip().add(armor.getArmorMaterial().toString());
				}
				if (TrinketsConfig.SERVER.races.faelis.penalties) {
					final EntityProperties prop = Capabilities.getEntityRace(event.getEntityPlayer());
					if (prop != null) {
						if (prop.getCurrentRace().equals(EntityRaces.faelis)) {
							final double weight = ConfigHelper.parseItemArmor(stack, TrinketsConfig.SERVER.races.faelis.heavyArmor);
							if (weight > 0) {
								event.getToolTip().add(I18n.format("xat.tooltip.weight") + " " + weight);
							}
						}
					}
				}
			}
			if (TrinketsConfig.SERVER.mana.mana_enabled) {
				final Map<String, ItemObjectHolder> configObjects = ConfigHelper.getMagicRecoveryItems();
				if ((configObjects != null) && !configObjects.isEmpty()) {
					final boolean flag1 = configObjects.containsKey(stack.getItem().getRegistryName().toString() + ":" + stack.getItemDamage());
					final boolean flag2 = configObjects.containsKey(stack.getItem().getRegistryName().toString() + ":" + OreDictionary.WILDCARD_VALUE);
					if (flag1 || flag2) {
						final String key = flag2 ? stack.getItem().getRegistryName().toString() + ":" + OreDictionary.WILDCARD_VALUE : stack.getItem().getRegistryName().toString() + ":" + stack.getItemDamage();
						final ItemObjectHolder item = configObjects.get(key);
						if (stack.getItem().getRegistryName().toString().equalsIgnoreCase(item.getObject())) {
							if ((item.getMeta() == OreDictionary.WILDCARD_VALUE) || (stack.getItemDamage() == item.getMeta())) {
								if (item.getParams().length > 0) {
									final String recoveryAmount = item.getParams()[0];
									if (!recoveryAmount.isEmpty()) {
										if (recoveryAmount.endsWith("%")) {
											final float amount = MathHelper.clamp(Float.parseFloat(recoveryAmount.replace("%", "")), 0, 100);
											event.getToolTip().add(TextFormatting.DARK_AQUA + "+" + amount + "% MP");
										} else {
											final float amount = Float.parseFloat(recoveryAmount);
											event.getToolTip().add(TextFormatting.DARK_AQUA + "+" + amount + " MP");
										}
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
