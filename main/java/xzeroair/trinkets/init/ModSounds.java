package xzeroair.trinkets.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import xzeroair.trinkets.util.Reference;

public class ModSounds {

	public static SoundEvent araara;
	public static SoundEvent uwu;

	private static int index = 0;

	public static void init() {
		araara = register(null, "xat.araara");
		uwu = register(null, "xat.uwu");
	}

	public static SoundEvent register(String folder, String name) {
		ResourceLocation location = new ResourceLocation(Reference.MODID + ":" + name);
		SoundEvent event = new SoundEvent(location);
		event.setRegistryName(name);
		ForgeRegistries.SOUND_EVENTS.register(event);
		index++;
		return event;
	}

}
