package xzeroair.trinkets.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
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
import xzeroair.trinkets.util.config.ConfigHelper.ConfigEquipmentObject;
import xzeroair.trinkets.util.config.ConfigHelper.MPRecoveryItem;

import java.util.Map;
import java.util.Map.Entry;

@SideOnly(Side.CLIENT)
public class EventHandlerClient {

    //	@SubscribeEvent
    //	public void mouseEvent(MouseEvent event) {
    //		System.out.println(event.getButton());
    //	}

    @SubscribeEvent
    public void clientTickEvent(TickEvent.ClientTickEvent event) {
        if ((event.side == Side.CLIENT) && (event.phase == Phase.START)) {
            final EntityPlayerSP player = Minecraft.getMinecraft().player;
            if ((player != null)) {
                if (TrinketsConfig.getClientStore().TRINKET_CONTAINER_ENABLED && ModKeyBindings.TRINKET_GUI.isPressed()) {
                    NetworkHandler.sendToServer(new OpenTrinketGui(Reference.GUI));
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
                                final boolean keyDown = !keybind1.isEmpty() && ModKeyBindings.isKeyDownFromName(keybind1);
                                final boolean auxDown = !keybind2.isEmpty() && ModKeyBindings.isKeyDownFromName(keybind2);
                                final KeyHandler keyHand = prop.getKeybindHandler().getKeyHandler(key + "." + keybind1);
                                if (keyHand != null) {
                                    keyHand.handler(keyDown, press -> {
                                        if (keybind.onKeyPress(player, auxDown)) {
                                            NetworkHandler.sendToServer(new KeybindPacket(player, key, keyDown, auxDown, 0));
                                            return true;
                                        } else {
                                            return false;
                                        }
                                    }, down -> {
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
                                    });
                                }
                            }
                        } catch (final Exception e) {
                            Trinkets.LOGGER.error("Trinkets had an Error with Ability:{}", key);
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
                    if (TrinketsConfig.getClientStore().BLOCK_MOVEMENT && prop.getRaceHandler().isTransforming()) {
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
        if ((player == null) || (player.getEntityWorld() == null)) {
            return;
        }
        final GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        if ((screen == null) || screen.doesGuiPauseGame()) {
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
        if (TrinketsConfig.CLIENT.debug.debugArmorMaterials || TrinketsConfig.SERVER.RACES.FAELIS.HEAVY_ARMOR_PENALTY) {
            final String itemType = ConfigEquipmentObject.getItemType(stack);
            if (!itemType.isEmpty()) {
                final String ItemMaterial = ConfigEquipmentObject.getItemMaterial(stack).toLowerCase();
                if (TrinketsConfig.CLIENT.debug.debugArmorMaterials && !ItemMaterial.isEmpty()) {
                    event.getToolTip().add(ItemMaterial);
                }
                if (TrinketsConfig.SERVER.RACES.FAELIS.HEAVY_ARMOR_PENALTY) {
                    boolean isFaelis = Capabilities.getEntityProperties(player, false, (prop, rtn) -> prop.getCurrentRace().compareRace(EntityRaces.faelis));
                    if (isFaelis) {
                        ConfigEquipmentObject entry = null;
                        if (item instanceof ItemArmor) {
                            final ItemArmor armor = ((ItemArmor) item);
                            final String armorType = armor.armorType.getName();
                            entry = ConfigHelper.TrinketConfigStorage.getEquipmentEntry(regName + ":" + armorType, regName, "ObjectMaterial:" + ItemMaterial + ":" + armorType, "ObjectMaterial:" + ItemMaterial);
                            if (entry != null) {
                                String color = entry.getEquipmentWeight() > 0 ? "" + TextFormatting.RED : "" + TextFormatting.BLUE;
                                //								event.getToolTip().add("Weight: " + color + entry.getEquipmentWeight());
                                event.getToolTip().add(new TextComponentTranslation("xat.tooltip.weight").getFormattedText() + " " + color + entry.getEquipmentWeight());
                            }
                            //						} else if (item instanceof ItemShield) {
                        } else {
                            String[] mS = new String[]{regName + ":" + "mainhand", regName, "ObjectMaterial:" + ItemMaterial + ":" + "mainhand" + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + "mainhand" + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + "hand" + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + "hand" + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + "mainhand", "ObjectMaterial:" + ItemMaterial + ":" + "hand", "ObjectMaterial:" + ItemMaterial,};
                            final ConfigEquipmentObject main = ConfigHelper.TrinketConfigStorage.getEquipmentEntry((k, v) -> v.doesItemMatchEntry(stack), mS);
                            String[] oS = new String[]{regName + ":" + "offhand", regName, "ObjectMaterial:" + ItemMaterial + ":" + "offhand" + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + "offhand" + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + "hand" + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + "hand" + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + itemType, "ObjectMaterial:" + ItemMaterial + ":" + "tool", "ObjectMaterial:" + ItemMaterial + ":" + "offhand", "ObjectMaterial:" + ItemMaterial + ":" + "hand", "ObjectMaterial:" + ItemMaterial};
                            final ConfigEquipmentObject off = ConfigHelper.TrinketConfigStorage.getEquipmentEntry((k, v) -> v.doesItemMatchEntry(stack), oS);
                            double mW = main == null ? 0 : main.getEquipmentWeight();
                            String color1 = mW > 0 ? "" + TextFormatting.RED : "" + TextFormatting.BLUE;
                            double oW = off == null ? 0 : off.getEquipmentWeight();
                            String color2 = oW > 0 ? "" + TextFormatting.RED : "" + TextFormatting.BLUE;

                            if ((mW != 0) || (oW != 0)) {
                                if (mW != oW) {
                                    if (mW != 0) {
                                        event.getToolTip().add("Weight: " + color1 + mW + " - Mainhand");
                                        if (oW != 0) {
                                            event.getToolTip().add("Weight: " + color2 + oW + " - Offhand");
                                        }
                                    } else {
                                        event.getToolTip().add("Weight: " + color2 + oW + " - Offhand");
                                    }
                                } else {
                                    event.getToolTip().add("Weight: " + color1 + mW);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (TrinketsConfig.SERVER.MAGIC.mana_enabled) {
            try {
                final Map<String, MPRecoveryItem> MagicRecoveryItems = ConfigHelper.TrinketConfigStorage.MagicRecoveryItems;
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
//        if (stack.getItem().equals(Items.POTIONITEM)) {
//            final PotionType pot = PotionUtils.getPotionFromItem(stack);
//            if (ModPotionTypes.TrinketPotionTypes.containsValue(pot)) {
//                final TranslationHelper helper = TranslationHelper.INSTANCE;
//                for (int i = 1; i < 10; i++) {
//                    String string = helper.getLangTranslation("xat.item.potion." + pot.getRegistryName().getPath() + ".tooltip" + i);
//                    if (!helper.isStringEmpty(string)) {
//                        event.getToolTip().add(string);
//                    }
//                }
//            }
//        }
    }
}
