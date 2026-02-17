package xzeroair.trinkets.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.config.ClientConfig;
import xzeroair.trinkets.util.config.ClientConfigStore;
import xzeroair.trinkets.util.config.ServerConfig;
import xzeroair.trinkets.util.config.compat.CompatabilityConfigs;
import xzeroair.trinkets.util.helpers.NBTHelper;

@Config(name = Reference.configPath, modid = Reference.MODID)
@LangKey("xat.config.title")
public class TrinketsConfig {

    private final static String cfgPrefix = Reference.MODID + ".config";

    @Config.Name("Client Settings")
    @LangKey(cfgPrefix + ".client.settings")
    public static ClientConfig CLIENT = new ClientConfig();

    @Config.Name("Server Settings")
    @LangKey(cfgPrefix + ".server.settings")
    public static ServerConfig SERVER = new ServerConfig();

    @Config.Name("Compatibility Settings")
    @LangKey(cfgPrefix + ".compatibility")
    public static CompatabilityConfigs compat = new CompatabilityConfigs();

    public static ClientConfigStore getClientStore() {
        return ClientConfigStore.INSTANCE;
    }

    public static NBTTagCompound writeConfigMap() {
        final NBTTagCompound tag = new NBTTagCompound();
        // Needs Synced because it effects client rendering
        tag.setBoolean("TRINKET_CONTAINER", TrinketsConfig.SERVER.GUI.guiEnabled);
        tag.setBoolean("OF_ENABLED", TrinketsConfig.SERVER.Items.DRAGON_EYE.oreFinder);

        // Climbable blocks sync.
        tag.setBoolean("FAIRY_CLIMBING_ENABLED", TrinketsConfig.SERVER.races.fairy.climbing);
        tag.setBoolean("GOBLIN_CLIMBING_ENABLED", TrinketsConfig.SERVER.races.fairy.climbing);
        tag.setBoolean("FAELIS_CLIMBING_ENABLED", TrinketsConfig.SERVER.races.fairy.climbing);

        // Most of these are probably not needed.
        if (Trinkets.MOD_COMPAT.ToughAsNails) {
            tag.setBoolean("COMPAT_TAN", TrinketsConfig.compat.toughasnails);
        }
        if (Trinkets.MOD_COMPAT.SimpleDifficulty) {
            tag.setBoolean("COMPAT_SD", TrinketsConfig.compat.simpledifficulty);
        }
        if (Trinkets.MOD_COMPAT.ElenaiDodge1) {
            tag.setBoolean("COMPAT_ELENAI1", TrinketsConfig.compat.elenaiDodge);
        }
//        if (Trinkets.MOD_COMPAT.ElenaiDodge2) {
//            tag.setBoolean("COMPAT_ELENAI2", TrinketsConfig.compat.elenaiDodge);
//        }
        if (Trinkets.MOD_COMPAT.EnhancedVisuals) {
            tag.setBoolean("COMPAT_EV", TrinketsConfig.compat.enhancedvisuals);
        }
//        configMap.put("compatLycanites", "" + TrinketsConfig.compat.lycanites);
//        configMap.put("compatDefiledLands", "" + TrinketsConfig.compat.defiledlands);
        if (Trinkets.MOD_COMPAT.BetterDiving) {
            tag.setBoolean("COMPAT_BD", TrinketsConfig.compat.betterdiving);
        }
        tag.setBoolean("MISC_MOVEMENT", TrinketsConfig.SERVER.misc.movement);
        tag.setBoolean("MISC_REACH_FIX", TrinketsConfig.SERVER.misc.reach);
        return tag;
    }

    public static void readConfigMap(NBTTagCompound tag) {
        if ((tag != null) && !tag.isEmpty()) {
            Trinkets.log.info("Found Server Config");
            try {
                NBTHelper.hasBoolean(tag, "TRINKET_CONTAINER", (bool) -> {
                    ClientConfigStore.INSTANCE.TRINKET_CONTAINER_ENABLED = bool;
                });
                NBTHelper.hasBoolean(tag, "OF_ENABLED", (bool) -> {
                    ClientConfigStore.INSTANCE.DRAGON_EYE_OF_ENABLED = bool;
                });
                NBTHelper.hasBoolean(tag, "FAIRY_CLIMBING_ENABLED", (bool) -> {
                    ClientConfigStore.INSTANCE.CLIMBING_ENABLED = bool;
                });
                NBTHelper.hasBoolean(tag, "GOBLIN_CLIMBING_ENABLED", (bool) -> {
                    ClientConfigStore.INSTANCE.CLIMBING_ENABLED = bool;
                });
                NBTHelper.hasBoolean(tag, "FAELIS_CLIMBING_ENABLED", (bool) -> {
                    ClientConfigStore.INSTANCE.CLIMBING_ENABLED = bool;
                });
                NBTHelper.hasBoolean(tag, "COMPAT_TAN", (bool) -> {
                    ClientConfigStore.INSTANCE.MOD_COMPAT_TOUGHASNAILS = bool;
                });
                NBTHelper.hasBoolean(tag, "COMPAT_SD", (bool) -> {
                    ClientConfigStore.INSTANCE.MOD_COMPAT_SIMPLEDIFFICULTY = bool;
                });
                NBTHelper.hasBoolean(tag, "COMPAT_ELENAI1", (bool) -> {
                    ClientConfigStore.INSTANCE.MOD_COMPAT_ELENAI_DODGE = bool;
                });
                NBTHelper.hasBoolean(tag, "COMPAT_ELENAI2", (bool) -> {
                    ClientConfigStore.INSTANCE.MOD_COMPAT_ELENAI_DODGE = bool;
                });
                NBTHelper.hasBoolean(tag, "COMPAT_EV", (bool) -> {
                    ClientConfigStore.INSTANCE.MOD_COMPAT_ENHANCED_VISUALS = bool;
                });
                NBTHelper.hasBoolean(tag, "COMPAT_BD", (bool) -> {
                    ClientConfigStore.INSTANCE.MOD_COMPAT_BETTER_DIVING = bool;
                });
                NBTHelper.hasBoolean(tag, "MISC_MOVEMENT", (bool) -> {
                    ClientConfigStore.INSTANCE.BLOCK_MOVEMENT = bool;
                });
                NBTHelper.hasBoolean(tag, "MISC_REACH_FIX", (bool) -> {
                    ClientConfigStore.INSTANCE.REACH_FIX = bool;
                });
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void Save() {
        ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
    }

    public static void Load() {
        ConfigManager.load(Reference.MODID, Type.INSTANCE);
    }

    public static Configuration readConfig() {
        final Configuration cfg = Trinkets.config;
        return readConfig(cfg);
    }

    public static Configuration readConfig(Configuration cfg) {
        try {
            cfg.load();
        } catch (final Exception e1) {
            Trinkets.log.error("Xat had a problem loading it's configuration");
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }
        return cfg;
    }

}