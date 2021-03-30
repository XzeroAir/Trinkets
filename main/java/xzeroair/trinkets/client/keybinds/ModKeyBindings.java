package xzeroair.trinkets.client.keybinds;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import xzeroair.trinkets.util.Reference;

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
}