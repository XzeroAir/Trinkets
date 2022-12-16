package xzeroair.trinkets.client.keybinds;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.util.Reference;

@SideOnly(Side.CLIENT)
public class ModKeyBindings {

	public static KeyBinding TRINKET_GUI = new KeyBinding("key." + Reference.MODID + ".trinket_gui.desc", Keyboard.KEY_P, "key." + Reference.MODID + ".category");
	public static KeyBinding DRAGONS_EYE_TARGET = new KeyBinding("key." + Reference.MODID + ".trinket_target.desc", KeyConflictContext.IN_GAME, Keyboard.KEY_P, "key." + Reference.MODID + ".category");
	public static KeyBinding DRAGONS_EYE_ABILITY = new KeyBinding("key." + Reference.MODID + ".trinket_toggle_effect.desc", KeyConflictContext.IN_GAME, Keyboard.KEY_BACKSLASH, "key." + Reference.MODID + ".category");
	public static KeyBinding POLARIZED_STONE_ABILITY = new KeyBinding("key." + Reference.MODID + ".magnet_toggle_effect.desc", KeyConflictContext.IN_GAME, Keyboard.KEY_BACKSLASH, "key." + Reference.MODID + ".category");
	public static KeyBinding AUX_KEY = new KeyBinding("key." + Reference.MODID + ".aux_key.desc", KeyConflictContext.IN_GAME, Keyboard.KEY_LCONTROL, "key." + Reference.MODID + ".category");

	public static KeyBinding ARCING_ORB_ABILITY = new KeyBinding("key." + Reference.MODID + ".trinket_arcing_attack.desc", KeyConflictContext.IN_GAME, Keyboard.KEY_R, "key." + Reference.MODID + ".category");

	public static KeyBinding RACE_ABILITY = new KeyBinding("key." + Reference.MODID + ".race_ability.desc", KeyConflictContext.IN_GAME, Keyboard.KEY_R, "key." + Reference.MODID + ".category");

	public static void init() {
		ClientRegistry.registerKeyBinding(TRINKET_GUI);
		ClientRegistry.registerKeyBinding(DRAGONS_EYE_TARGET);
		ClientRegistry.registerKeyBinding(DRAGONS_EYE_ABILITY);
		ClientRegistry.registerKeyBinding(POLARIZED_STONE_ABILITY);
		ClientRegistry.registerKeyBinding(ARCING_ORB_ABILITY);
		ClientRegistry.registerKeyBinding(AUX_KEY);
		ClientRegistry.registerKeyBinding(RACE_ABILITY);
	}

	public static boolean isKeyDownFromName(String key) {
		if ((key == null) || key.isEmpty()) {
			return false;
		}
		boolean isKeysDown = false;
		try {
			final String[] MultiCheck = key.split("\\+");
			for (String s : MultiCheck) {
				s = s.trim();
				if (s.equalsIgnoreCase("shift")) {
					final int lShift = Keyboard.getKeyIndex("LSHIFT");
					final int rShift = Keyboard.getKeyIndex("RSHIFT");
					if (Keyboard.isKeyDown(lShift) || Keyboard.isKeyDown(rShift)) {
						isKeysDown = true;
					} else {
						isKeysDown = false;
						return false;
					}
				} else if (s.equalsIgnoreCase("ctrl")) {
					final int lCtrl = Keyboard.getKeyIndex("LCONTROL");
					final int rCtrl = Keyboard.getKeyIndex("RCONTROL");
					if (Keyboard.isKeyDown(lCtrl) || Keyboard.isKeyDown(rCtrl)) {
						isKeysDown = true;
					} else {
						isKeysDown = false;
						return false;
					}
				} else if (s.equalsIgnoreCase("alt")) {
					final int lCtrl = Keyboard.getKeyIndex("LMENU");
					final int rCtrl = Keyboard.getKeyIndex("RMENU");
					if (Keyboard.isKeyDown(lCtrl) || Keyboard.isKeyDown(rCtrl)) {
						isKeysDown = true;
					} else {
						isKeysDown = false;
						return false;
					}
				} else {
					final int keyCode = Keyboard.getKeyIndex(s);
					if (keyCode == 0) {
						return false;
					}
					if (Keyboard.isKeyDown(keyCode)) {
						isKeysDown = true;
					} else {
						isKeysDown = false;
						return false;
					}
				}
			}
		} catch (final Exception e) {
		}
		return isKeysDown;
	}
}