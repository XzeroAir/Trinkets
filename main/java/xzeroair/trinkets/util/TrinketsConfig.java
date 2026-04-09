package xzeroair.trinkets.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.config.ClientConfig;
import xzeroair.trinkets.util.config.ClientConfigStore;
import xzeroair.trinkets.util.config.ServerConfig;
import xzeroair.trinkets.util.config.compat.CompatibilityConfigs;
import xzeroair.trinkets.util.helpers.NBTHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Config(name = Reference.configPath, modid = Reference.MODID)
@Config.LangKey("xat.config.title")
public class TrinketsConfig {

    @Config.Name(ConstantsConfigLang.CONFIG_CLIENT_SETTINGS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_CLIENT_SETTINGS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_CLIENT_SETTINGS)
    public static ClientConfig CLIENT = new ClientConfig();

    @Config.Name(ConstantsConfigLang.CONFIG_SERVER_SETTINGS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_SERVER_SETTINGS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_SERVER_SETTINGS)
    public static ServerConfig SERVER = new ServerConfig();

    @Config.Name(ConstantsConfigLang.CONFIG_COMPAT_SETTINGS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_COMPAT_SETTINGS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_COMPAT_SETTINGS)
    public static CompatibilityConfigs compat = new CompatibilityConfigs();

    public static ClientConfigStore getClientStore() {
        return ClientConfigStore.INSTANCE;
    }

    @Config.Ignore
    private static int configVersion = 0;

    private static void markConfigChanged() {
        configVersion++;
    }

    public static int getConfigVersion() {
        return configVersion;
    }

    @Nonnull
    public static NBTTagCompound writeConfigMap() {
        final NBTTagCompound tag = new NBTTagCompound();
        // Needs Synced because it effects client rendering
        tag.setBoolean("TRINKET_CONTAINER", TrinketsConfig.SERVER.GUI.ENABLED);

        NBTTagCompound races = new NBTTagCompound();
        int index = 0;
        for (String race : TrinketsConfig.SERVER.RACES.BLACKLIST) {
            races.setString(index++ + "", race);
        }
        tag.setTag("RACE_BLACKLIST", races);

        // Most of these are probably not needed.
        if (Trinkets.MOD_COMPAT.ToughAsNails) {
            tag.setBoolean("COMPAT_TAN", TrinketsConfig.compat.TOUGH_AS_NAILS);
        }
        if (Trinkets.MOD_COMPAT.SimpleDifficulty) {
            tag.setBoolean("COMPAT_SD", TrinketsConfig.compat.SIMPLE_DIFFICULTY);
        }
        if (Trinkets.MOD_COMPAT.ElenaiDodge1) {
            tag.setBoolean("COMPAT_ELENAI1", TrinketsConfig.compat.ELENAI_DODGE);
        }
        if (Trinkets.MOD_COMPAT.EnhancedVisuals) {
            tag.setBoolean("COMPAT_EV", TrinketsConfig.compat.ENHANCED_VISUALS);
        }
        if (Trinkets.MOD_COMPAT.BetterDiving) {
            tag.setBoolean("COMPAT_BD", TrinketsConfig.compat.BETTER_DIVING);
        }
        tag.setBoolean("MISC_MOVEMENT", TrinketsConfig.SERVER.MISC.MOVEMENT);
        tag.setBoolean("MISC_REACH_FIX", TrinketsConfig.SERVER.MISC.REACH);
        return tag;
    }

    public static void readConfigMap(NBTTagCompound tag) {
        if ((tag != null) && !tag.isEmpty()) {
            Trinkets.LOGGER.info("Found Server Config");
            NBTHelper.hasBoolean(tag, "TRINKET_CONTAINER", (bool) -> ClientConfigStore.INSTANCE.TRINKET_CONTAINER_ENABLED = bool);
            NBTHelper.hasTag(tag, "RACE_BLACKLIST", (t) -> {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < t.getSize(); i++) {
                    if (t.hasKey(i + "")) {
                        list.add(t.getString(i + ""));
                    }
                }
                ClientConfigStore.INSTANCE.RACE_SELECTION_BLACKLIST = list.toArray(new String[0]);
            });
            NBTHelper.hasBoolean(tag, "COMPAT_TAN", (bool) -> ClientConfigStore.INSTANCE.MOD_COMPAT_TOUGHASNAILS = bool);
            NBTHelper.hasBoolean(tag, "COMPAT_SD", (bool) -> ClientConfigStore.INSTANCE.MOD_COMPAT_SIMPLEDIFFICULTY = bool);
            NBTHelper.hasBoolean(tag, "COMPAT_ELENAI1", (bool) -> ClientConfigStore.INSTANCE.MOD_COMPAT_ELENAI_DODGE = bool);
            NBTHelper.hasBoolean(tag, "COMPAT_ELENAI2", (bool) -> ClientConfigStore.INSTANCE.MOD_COMPAT_ELENAI_DODGE = bool);
            NBTHelper.hasBoolean(tag, "COMPAT_EV", (bool) -> ClientConfigStore.INSTANCE.MOD_COMPAT_ENHANCED_VISUALS = bool);
            NBTHelper.hasBoolean(tag, "COMPAT_BD", (bool) -> ClientConfigStore.INSTANCE.MOD_COMPAT_BETTER_DIVING = bool);
            NBTHelper.hasBoolean(tag, "MISC_MOVEMENT", (bool) -> ClientConfigStore.INSTANCE.BLOCK_MOVEMENT = bool);
            NBTHelper.hasBoolean(tag, "MISC_REACH_FIX", (bool) -> ClientConfigStore.INSTANCE.REACH_FIX = bool);
        }
    }

    public static void Save() {
        ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
        markConfigChanged();
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
            Trinkets.LOGGER.error("Trinkets & Baubles(xat) had a problem loading it's configuration");
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }
        return cfg;
    }

}