package xzeroair.trinkets.client.keybinds;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import xzeroair.trinkets.util.Reference;

public class ModKeyBindings {

	public static KeyBinding TRINKET_TARGET = new KeyBinding("key." + Reference.MODID + ".trinket_target.desc", KeyConflictContext.IN_GAME, Keyboard.KEY_P, "key." + Reference.MODID + ".category");
	public static KeyBinding TRINKET_TOGGLE_EFFECT = new KeyBinding("key." + Reference.MODID + ".trinket_toggle_effect.desc", KeyConflictContext.IN_GAME, Keyboard.KEY_BACKSLASH, "key." + Reference.MODID + ".category");
	public static KeyBinding AUX_KEY = new KeyBinding("key." + Reference.MODID + ".aux_key.desc", KeyConflictContext.IN_GAME, Keyboard.KEY_LCONTROL, "key." + Reference.MODID + ".category");

	public static void init() {
		ClientRegistry.registerKeyBinding(TRINKET_TARGET);
		ClientRegistry.registerKeyBinding(TRINKET_TOGGLE_EFFECT);
		ClientRegistry.registerKeyBinding(AUX_KEY);

		//		MinecraftForge.EVENT_BUS.register(new ModKeyBindings());
	}
}