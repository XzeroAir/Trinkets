package xzeroair.trinkets.util;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.Trinkets;

@Config(name = "Trinkets_and_Baubles", modid = Reference.MODID)
@Config.LangKey("Main.TrinketsConfig.title")
public class TrinketsConfig {

	@Config.Comment("")
	@Name("Client Settings")
	public static xClient CLIENT = new xClient();
	public static class xClient {

		@Config.Comment("POV Height adjustments when wearing Race Rings. Set to False to Disable. Default True")
		@Name("POV Height Adjustments")
		public boolean C00_Height = true;

		@Config.Comment("Rendering of Trinkets. Set to False to Disable. Default True")
		@Name("Render Trinkets")
		public boolean C01_Render = true;

		@Config.Comment("Dragon's Eye Effect List")
		@Name("Dragon's Eye Effects")
		public Effects effects = new Effects();
		public class Effects {

			@Config.Comment("WARNING! SETTING THESE VALUES TOO HIGH WILL CAUSE YOU TO LAG. Try to Keep within a range of 4-16")
			@Name("Detection Range")
			public Detection_Range DR = new Detection_Range();
			public class Detection_Range {

				@Config.Comment("How Far Vertically(Up, Down) in Blocks the Dragon's Eye effect triggers. Default 6, MIN 0, MAX 32")
				@Name("Vertical Distance")
				@RangeInt(min = 0, max = 32)
				public int C00_VD = 6;

				@Config.Comment("How Far Horizontally(N, E, S, W) in Blocks the Dragon's Eye effect triggers. Default 12, MIN 0, MAX 32")
				@Name("Horizontal Distance")
				@RangeInt(min = 0, max = 32)
				public int C001_HD = 12;
			}

			@Config.Comment("How Often the Dragon's Eye effect triggers in Ticks. try to avoid lower values. Default 79, 20 MIN, 360 MAX")
			@Name("Refresh Rate")
			@RangeInt(min = 20, max = 360)
			public int C00_RR = 79;

			@Config.Comment("Dragon's Eye Ore Detection. Set to False to Disable. Default True")
			@Name("Dragon's Eye Ore Detection")
			public boolean C01_Dragon_Eye = true;
		}
	}

	@Name("Server Settings")
	public static xServer SERVER = new xServer();
	public static class xServer {

		@Config.Comment("Creative Flight when wearing the Fairies Ring. Set to False to Disable. Default True")
		@Name("Creative Flight")
		public boolean C00_Creative_Flight = true;

		@Config.Comment("Step Height Adjustment when wearing Race Rings. Set to False to Disable. Default True")
		@Name("Step Height")
		public boolean C01_Step_Height = true;

		@Config.Comment("Fairy's Ring Climbing Ability. Set to False to Disable. Default True")
		@Name("Climbing")
		public boolean C02_Climbing = true;

		@Config.Comment("Dwarves Ring Special Fortune Like Effect. Set to False to Disable. Default True")
		@Name("Dwarves Ring Fortune Effect")
		public boolean C03_Fortune = true;

		@Config.RequiresWorldRestart
		@Config.Comment("Dragon's Eye Ability to Find Chests. Set to False to Disable. Default True :Requires Relogin to world")
		@Name("Find Chests Ability")
		public boolean C04_DE_Chests = true;

		@Config.Comment("Polarized Stone's Push and pull speed. Default 0.1, 0.1 MIN, 1.0 MAX")
		@Name("Polarized Stone Push and Pull Speed")
		@RangeDouble(min = 0.1, max = 1)
		public double C05_Polarized_Stone_Speed = 0.1;

		@Config.Comment("Sea Stone's Ability to float in water. Set to False to Disable. Default True")
		@Name("Sea Stone's Swimming Tweaks")
		public boolean C06_Sea_Stone_Swim_Tweaks = true;
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

	@Mod.EventBusSubscriber(modid = Reference.MODID)
	private static class EventHandler {
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(Reference.MODID)) {
				ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
			}
		}
	}
}