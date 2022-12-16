<<<<<<< Updated upstream
package xzeroair.trinkets.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.config.ClientConfig;
import xzeroair.trinkets.util.config.ServerConfig;
import xzeroair.trinkets.util.config.compat.CompatabilityConfigs;
import xzeroair.trinkets.util.helpers.StringUtils;

@Config(name = Reference.configPath, modid = Reference.MODID)
@Config.LangKey("xat.config.title")
public class TrinketsConfig {

	private final static String cfgPrefix = Reference.MODID + ".config";

	@Config.Name("Client Settings")
	@LangKey(cfgPrefix + ".client.settings")
	public static ClientConfig CLIENT = new ClientConfig();

	@Config.Name("Server Settings")
	@LangKey(cfgPrefix + ".server.settings")
	public static ServerConfig SERVER = new ServerConfig();

	@Config.Name("Compatability Settings")
	@LangKey(cfgPrefix + ".compatability")
	public static CompatabilityConfigs compat = new CompatabilityConfigs();

	public static Map<String, String> writeConfigMap() {
		final Map<String, String> configMap = new HashMap<>();
		configMap.put("oreEnabled", "" + TrinketsConfig.SERVER.Items.DRAGON_EYE.oreFinder);
		configMap.put("oreEnabled.closest", "" + TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.closest);
		configMap.put("oreEnabled.blocks", StringUtils.combineStringArray(TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.Blocks));
		configMap.put("oreEnabled.hd", "" + TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.DR.C001_HD);
		configMap.put("oreEnabled.vd", "" + TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.DR.C00_VD);
		configMap.put("dragonFlight", "" + TrinketsConfig.SERVER.races.dragon.creative_flight);
		configMap.put("dragonFlightCost", "" + TrinketsConfig.SERVER.races.dragon.flight_cost);
		configMap.put("dragonFlightSpeed", "" + TrinketsConfig.SERVER.races.dragon.flight_speed);
		configMap.put("dragonBreathCost", "" + TrinketsConfig.SERVER.races.dragon.breath_cost);
		configMap.put("dragonBreathDamage", "" + TrinketsConfig.SERVER.races.dragon.breath_damage);
		configMap.put("fairyFlight", "" + TrinketsConfig.SERVER.races.fairy.creative_flight);
		configMap.put("fairyFlightSpeed", "" + TrinketsConfig.SERVER.races.fairy.flight_speed);
		configMap.put("fairyFlightCost", "" + TrinketsConfig.SERVER.races.fairy.flight_cost);
		configMap.put("TrinketContainer", "" + TrinketsConfig.SERVER.GUI.guiEnabled);
		configMap.put("compatTAN", "" + TrinketsConfig.compat.toughasnails);
		configMap.put("compatSD", "" + TrinketsConfig.compat.simpledifficulty);
		configMap.put("compatArtemisLib", "" + TrinketsConfig.compat.artemislib);
		configMap.put("compatElenaiDodge1", "" + TrinketsConfig.compat.elenaiDodge);
		configMap.put("compatElenaiDodge2", "" + TrinketsConfig.compat.elenaiDodge);
		configMap.put("compatEnhancedVisuals", "" + TrinketsConfig.compat.enhancedvisuals);
		configMap.put("compatLycanites", "" + TrinketsConfig.compat.lycanites);
		configMap.put("compatDefiledLands", "" + TrinketsConfig.compat.defiledlands);
		configMap.put("miscDepthStacking", "" + TrinketsConfig.SERVER.misc.depthStacks);
		configMap.put("miscBlockMovement", "" + TrinketsConfig.SERVER.misc.movement);
		configMap.put("raceBlacklist", StringUtils.combineStringArray(TrinketsConfig.SERVER.races.selectionBlacklist));
		return configMap;
	}

	public static void readConfigMap(Map<String, String> configMap) {
		if ((configMap != null) && !configMap.isEmpty()) {
			Trinkets.log.info("Found Server Config");
			for (final Entry<String, String> config : configMap.entrySet()) {
				try {
					if (config.getKey().contentEquals("oreEnabled")) {
						TrinketsConfig.SERVER.Items.DRAGON_EYE.oreFinder = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("oreEnabled.closest")) {
						TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.closest = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("oreEnabled.blocks")) {
						TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.Blocks = StringUtils.deconstructStringArray(config.getValue());
					}
					if (config.getKey().contentEquals("oreEnabled.hd")) {
						TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.DR.C001_HD = Integer.parseInt(config.getValue());
					}
					if (config.getKey().contentEquals("oreEnabled.vd")) {
						TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.DR.C00_VD = Integer.parseInt(config.getValue());
					}
					if (config.getKey().contentEquals("dragonFlight")) {
						TrinketsConfig.SERVER.races.dragon.creative_flight = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("dragonFlightCost")) {
						TrinketsConfig.SERVER.races.dragon.flight_cost = Float.parseFloat(config.getValue());
					}
					if (config.getKey().contentEquals("dragonFlightSpeed")) {
						TrinketsConfig.SERVER.races.dragon.flight_speed = Double.parseDouble(config.getValue());
					}
					if (config.getKey().contentEquals("dragonBreathCost")) {
						TrinketsConfig.SERVER.races.dragon.breath_cost = Float.parseFloat(config.getValue());
					}
					if (config.getKey().contentEquals("dragonBreathDamage")) {
						TrinketsConfig.SERVER.races.dragon.breath_damage = Float.parseFloat(config.getValue());
					}
					if (config.getKey().contentEquals("fairyFlight")) {
						TrinketsConfig.SERVER.races.fairy.creative_flight = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("fairyFlightSpeed")) {
						TrinketsConfig.SERVER.races.fairy.flight_speed = Double.parseDouble(config.getValue());
					}
					if (config.getKey().contentEquals("fairyFlightCost")) {
						TrinketsConfig.SERVER.races.fairy.flight_cost = Float.parseFloat(config.getValue());
					}
					if (config.getKey().contentEquals("TrinketContainer")) {
						TrinketsConfig.SERVER.GUI.guiEnabled = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("compatTAN")) {
						TrinketsConfig.compat.toughasnails = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("compatSD")) {
						TrinketsConfig.compat.simpledifficulty = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("compatArtemisLib")) {
						TrinketsConfig.compat.artemislib = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("compatElenaiDodge1")) {
						TrinketsConfig.compat.elenaiDodge = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("compatElenaiDodge2")) {
						TrinketsConfig.compat.elenaiDodge = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("compatEnhancedVisuals")) {
						TrinketsConfig.compat.enhancedvisuals = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("compatLycanites")) {
						TrinketsConfig.compat.lycanites = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("compatDefiledLands")) {
						TrinketsConfig.compat.defiledlands = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("miscDepthStacking")) {
						TrinketsConfig.SERVER.misc.depthStacks = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("miscBlockMovement")) {
						TrinketsConfig.SERVER.misc.movement = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("raceBlacklist")) {
						TrinketsConfig.SERVER.races.selectionBlacklist = StringUtils.deconstructStringArray(config.getValue());
					}
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void Save() {
		ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
	}

	public static void readConfig() {
		final Configuration cfg = Trinkets.config;
		try {
			cfg.load();
		} catch (final Exception e1) {
			Trinkets.log.error("Xat had a problem loading it's configuration");
		} finally {
			if (cfg.hasChanged()) {
				cfg.save();
			}
		}
	}

=======
package xzeroair.trinkets.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.config.ClientConfig;
import xzeroair.trinkets.util.config.ServerConfig;
import xzeroair.trinkets.util.config.compat.CompatabilityConfigs;
import xzeroair.trinkets.util.helpers.StringUtils;

@Config(name = Reference.configPath, modid = Reference.MODID)
@Config.LangKey("xat.config.title")
public class TrinketsConfig {

	private final static String cfgPrefix = Reference.MODID + ".config";

	@Config.Name("Client Settings")
	@LangKey(cfgPrefix + ".client.settings")
	public static ClientConfig CLIENT = new ClientConfig();

	@Config.Name("Server Settings")
	@LangKey(cfgPrefix + ".server.settings")
	public static ServerConfig SERVER = new ServerConfig();

	@Config.Name("Compatability Settings")
	@LangKey(cfgPrefix + ".compatability")
	public static CompatabilityConfigs compat = new CompatabilityConfigs();

	public static Map<String, String> writeConfigMap() {
		final Map<String, String> configMap = new HashMap<>();
		configMap.put("oreEnabled", "" + TrinketsConfig.SERVER.Items.DRAGON_EYE.oreFinder);
		configMap.put("oreEnabled.closest", "" + TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.closest);
		configMap.put("oreEnabled.blocks", StringUtils.combineStringArray(TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.Blocks));
		configMap.put("oreEnabled.hd", "" + TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.DR.C001_HD);
		configMap.put("oreEnabled.vd", "" + TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.DR.C00_VD);
		configMap.put("dragonFlight", "" + TrinketsConfig.SERVER.races.dragon.creative_flight);
		configMap.put("dragonFlightCost", "" + TrinketsConfig.SERVER.races.dragon.flight_cost);
		configMap.put("dragonFlightSpeed", "" + TrinketsConfig.SERVER.races.dragon.flight_speed);
		configMap.put("dragonBreathCost", "" + TrinketsConfig.SERVER.races.dragon.breath_cost);
		configMap.put("dragonBreathDamage", "" + TrinketsConfig.SERVER.races.dragon.breath_damage);
		configMap.put("fairyFlight", "" + TrinketsConfig.SERVER.races.fairy.creative_flight);
		configMap.put("fairyFlightSpeed", "" + TrinketsConfig.SERVER.races.fairy.flight_speed);
		configMap.put("fairyFlightCost", "" + TrinketsConfig.SERVER.races.fairy.flight_cost);
		configMap.put("TrinketContainer", "" + TrinketsConfig.SERVER.GUI.guiEnabled);
		configMap.put("compatTAN", "" + TrinketsConfig.compat.toughasnails);
		configMap.put("compatSD", "" + TrinketsConfig.compat.simpledifficulty);
		configMap.put("compatArtemisLib", "" + TrinketsConfig.compat.artemislib);
		configMap.put("compatElenaiDodge1", "" + TrinketsConfig.compat.elenaiDodge);
		configMap.put("compatElenaiDodge2", "" + TrinketsConfig.compat.elenaiDodge);
		configMap.put("compatEnhancedVisuals", "" + TrinketsConfig.compat.enhancedvisuals);
		configMap.put("compatLycanites", "" + TrinketsConfig.compat.lycanites);
		configMap.put("compatDefiledLands", "" + TrinketsConfig.compat.defiledlands);
		configMap.put("miscDepthStacking", "" + TrinketsConfig.SERVER.misc.depthStacks);
		configMap.put("miscBlockMovement", "" + TrinketsConfig.SERVER.misc.movement);
		configMap.put("raceBlacklist", StringUtils.combineStringArray(TrinketsConfig.SERVER.races.selectionBlacklist));
		return configMap;
	}

	public static void readConfigMap(Map<String, String> configMap) {
		if ((configMap != null) && !configMap.isEmpty()) {
			Trinkets.log.info("Found Server Config");
			for (final Entry<String, String> config : configMap.entrySet()) {
				try {
					if (config.getKey().contentEquals("oreEnabled")) {
						TrinketsConfig.SERVER.Items.DRAGON_EYE.oreFinder = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("oreEnabled.closest")) {
						TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.closest = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("oreEnabled.blocks")) {
						TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.Blocks = StringUtils.deconstructStringArray(config.getValue());
					}
					if (config.getKey().contentEquals("oreEnabled.hd")) {
						TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.DR.C001_HD = Integer.parseInt(config.getValue());
					}
					if (config.getKey().contentEquals("oreEnabled.vd")) {
						TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.DR.C00_VD = Integer.parseInt(config.getValue());
					}
					if (config.getKey().contentEquals("dragonFlight")) {
						TrinketsConfig.SERVER.races.dragon.creative_flight = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("dragonFlightCost")) {
						TrinketsConfig.SERVER.races.dragon.flight_cost = Float.parseFloat(config.getValue());
					}
					if (config.getKey().contentEquals("dragonFlightSpeed")) {
						TrinketsConfig.SERVER.races.dragon.flight_speed = Double.parseDouble(config.getValue());
					}
					if (config.getKey().contentEquals("dragonBreathCost")) {
						TrinketsConfig.SERVER.races.dragon.breath_cost = Float.parseFloat(config.getValue());
					}
					if (config.getKey().contentEquals("dragonBreathDamage")) {
						TrinketsConfig.SERVER.races.dragon.breath_damage = Float.parseFloat(config.getValue());
					}
					if (config.getKey().contentEquals("fairyFlight")) {
						TrinketsConfig.SERVER.races.fairy.creative_flight = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("fairyFlightSpeed")) {
						TrinketsConfig.SERVER.races.fairy.flight_speed = Double.parseDouble(config.getValue());
					}
					if (config.getKey().contentEquals("fairyFlightCost")) {
						TrinketsConfig.SERVER.races.fairy.flight_cost = Float.parseFloat(config.getValue());
					}
					if (config.getKey().contentEquals("TrinketContainer")) {
						TrinketsConfig.SERVER.GUI.guiEnabled = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("compatTAN")) {
						TrinketsConfig.compat.toughasnails = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("compatSD")) {
						TrinketsConfig.compat.simpledifficulty = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("compatArtemisLib")) {
						TrinketsConfig.compat.artemislib = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("compatElenaiDodge1")) {
						TrinketsConfig.compat.elenaiDodge = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("compatElenaiDodge2")) {
						TrinketsConfig.compat.elenaiDodge = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("compatEnhancedVisuals")) {
						TrinketsConfig.compat.enhancedvisuals = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("compatLycanites")) {
						TrinketsConfig.compat.lycanites = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("compatDefiledLands")) {
						TrinketsConfig.compat.defiledlands = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("miscDepthStacking")) {
						TrinketsConfig.SERVER.misc.depthStacks = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("miscBlockMovement")) {
						TrinketsConfig.SERVER.misc.movement = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("raceBlacklist")) {
						TrinketsConfig.SERVER.races.selectionBlacklist = StringUtils.deconstructStringArray(config.getValue());
					}
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void Save() {
		ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
	}

	public static void readConfig() {
		final Configuration cfg = Trinkets.config;
		try {
			cfg.load();
		} catch (final Exception e1) {
			Trinkets.log.error("Xat had a problem loading it's configuration");
		} finally {
			if (cfg.hasChanged()) {
				cfg.save();
			}
		}
	}

>>>>>>> Stashed changes
}