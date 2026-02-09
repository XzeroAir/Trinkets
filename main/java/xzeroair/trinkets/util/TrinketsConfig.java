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
        tag.setBoolean("OF_ENABLED", TrinketsConfig.SERVER.Items.DRAGON_EYE.oreFinder);

        // Climbable blocks sync.
        tag.setBoolean("CLIMBING_ENABLED", TrinketsConfig.SERVER.races.fairy.climbing);

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
                if (tag.hasKey("OF_ENABLED")) {
                    ClientConfigStore.INSTANCE.DRAGON_EYE_OF_ENABLED = tag.getBoolean("OF_ENABLED");
                }
                if (tag.hasKey("CLIMBING_ENABLED")) {
                    ClientConfigStore.INSTANCE.CLIMBING_ENABLED = tag.getBoolean("CLIMBING_ENABLED");
                }
                if (tag.hasKey("COMPAT_TAN")) {
                    ClientConfigStore.INSTANCE.MOD_COMPAT_TOUGHASNAILS = tag.getBoolean("COMPAT_TAN");
                }
                if (tag.hasKey("COMPAT_SD")) {
                    ClientConfigStore.INSTANCE.MOD_COMPAT_SIMPLEDIFFICULTY = tag.getBoolean("COMPAT_SD");
                }
                if (tag.hasKey("COMPAT_ELENAI1")) {
                    ClientConfigStore.INSTANCE.MOD_COMPAT_ELENAI_DODGE = tag.getBoolean("COMPAT_ELENAI1");
                }
                if (tag.hasKey("COMPAT_ELENAI2")) {
                    ClientConfigStore.INSTANCE.MOD_COMPAT_ELENAI_DODGE = tag.getBoolean("COMPAT_ELENAI2");
                }
                if (tag.hasKey("COMPAT_EV")) {
                    ClientConfigStore.INSTANCE.MOD_COMPAT_ENHANCED_VISUALS = tag.getBoolean("COMPAT_EV");
                }
//                    if (config.getKey().contentEquals("compatLycanites")) {
//                        TrinketsConfig.compat.lycanites = Boolean.parseBoolean(config.getValue());
//                    }
//                    if (config.getKey().contentEquals("compatDefiledLands")) {
//                        TrinketsConfig.compat.defiledlands = Boolean.parseBoolean(config.getValue());
//                    }
                if (tag.hasKey("COMPAT_BD")) {
                    ClientConfigStore.INSTANCE.MOD_COMPAT_BETTER_DIVING = tag.getBoolean("COMPAT_BD");
                }
                if (tag.hasKey("MISC_MOVEMENT")) {
                    ClientConfigStore.INSTANCE.BLOCK_MOVEMENT = tag.getBoolean("MISC_MOVEMENT");
                }
                if (tag.hasKey("MISC_REACH_FIX")) {
                    ClientConfigStore.INSTANCE.REACH_FIX = tag.getBoolean("MISC_REACH_FIX");
                }
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