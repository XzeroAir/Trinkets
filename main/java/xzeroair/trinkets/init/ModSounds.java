package xzeroair.trinkets.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsRegistryNames;

public class ModSounds {

    public static SoundEvent araara;
    public static SoundEvent uwu;

    private static int index = 0;

    public static void init() {
        araara = register(null, TrinketsRegistryNames.ModSounds.ARA_ARA);
        uwu = register(null, TrinketsRegistryNames.ModSounds.UWU);
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
