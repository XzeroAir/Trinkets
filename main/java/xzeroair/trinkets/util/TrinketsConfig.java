package xzeroair.trinkets.util;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Main;

@Config(name = "Trinkets_and_Baubles", modid = Reference.MODID)
@Config.LangKey("Main.TrinketsConfig.title")
public class TrinketsConfig {

	//	public static Configuration config;

	@Config.Comment("")
	@Name("Client Settings")
	public static xClient CLIENT = new xClient();
	public static class xClient {

		@Config.Comment("POV Height adjustments when wearing Race Rings. Set to False to Disable. CANT CHANGE IN WORLD. Default True")
		@Name("POV Height Adjustments")
		//		@Config.RequiresWorldRestart
		public boolean C00_Height = true;
		@Config.Comment("Step Height Adjustment when wearing Race Rings. Set to False to Disable. Default True")
		@Name("Step Height")
		public boolean C01_Step_Height = true;
		@Config.Comment("Fairy's Ring Climbing Ability. Set to False to Disable. Default True")
		@Name("Climbing")
		public boolean C02_Climbing = true;

		@Config.Comment("Dragon's Eye Effect List")
		@Name("Dragon's Eye Effects")
		public Effects effects = new Effects();
		public class Effects {
			@Config.Comment("WARNING! SETTING THESE VALUES TOO HIGH WILL CAUSE YOU TO LAG. Try to Keep within a range of 4-16")
			@Name("Detection Range")
			public Detection_Range DR = new Detection_Range();
			public class Detection_Range {
				@Config.Comment("How Far Vertically(Up, Down) in Blocks the Dragon's Eye effect triggers. Default 6")
				@Name("Vertical Distance")
				@RangeInt(min = 0, max = 32)
				public int C00_VD = 6;
				@Config.Comment("How Far Horizontally(N, E, S, W) in Blocks the Dragon's Eye effect triggers. Default 12")
				@Name("Horizontal Distance")
				@RangeInt(min = 0, max = 32)
				public int C001_HD = 12;
			}
			@Config.Comment("How Often the Dragon's Eye effect triggers in Ticks. try to avoid lower values. Default 79")
			@Name("Refresh Rate")
			@RangeInt(min = 20, max = 360)
			public int C00_RR = 79;
			@Config.Comment("Dragon's Eye Effects. Set to False to Disable. Default True")
			@Name("All Dragon's Eye Effects")
			public boolean C01_Dragon_Eye = true;
			//			@Config.Comment("Dragon's Eye Night Vision Effect. Set to False to Disable. Default True")
			//			@Name("Night Vision")
			//			public boolean C02_NV = true;
			//			@Config.Comment("Dragon's Eye Coal Effects. Set to False to Disable. Default True")
			//			@Name("Coal")
			//			public boolean C03_Coal = true;
			//			@Config.Comment("Dragon's Eye Iron Effects. Set to False to Disable. Default True")
			//			@Name("Iron")
			//			public boolean C04_Iron = true;
			//			@Config.Comment("Dragon's Eye Gold Effects. Set to False to Disable. Default True")
			//			@Name("Gold")
			//			public boolean C05_Gold = true;
			//			@Config.Comment("Dragon's Eye Redstone Effects. Set to False to Disable. Default True")
			//			@Name("Redstone")
			//			public boolean C06_Redstone = true;
			//			@Config.Comment("Dragon's Eye Lapis Effects. Set to False to Disable. Default True")
			//			@Name("Lapis")
			//			public boolean C07_Lapis = true;
			//			@Config.Comment("Dragon's Eye Diamond Effects. Set to False to Disable. Default True")
			//			@Name("Diamond")
			//			public boolean C08_Diamond = true;
			//			@Config.Comment("Dragon's Eye Emerald Effects. Set to False to Disable. Default True")
			//			@Name("Emerald")
			//			public boolean C09_Emerald = true;
			//			@Config.Comment("Dragon's Eye Quarts Effects. Set to False to Disable. Default True")
			//			@Name("Quarts")
			//			public boolean C10_Quarts = true;
			//			@Config.Comment("Dragon's Eye Chests Effects. Set to False to Disable. Default True")
			//			@Name("Chests")
			//			public boolean C11_Chests = true;
		}
	}
	//
	//	@Config.RequiresMcRestart
	//	@Name("Server Settings")
	//	public static xServer SERVER = new xServer();
	//	public static class xServer {
	//
	//		@Config.Comment("Height adjustments when wearing Race Rings. Set to False to Disable. CANT CHANGE IN WORLD. Default True")
	//		@Name("Height Adjustments")
	//		//		@Config.RequiresWorldRestart
	//		public boolean C00_Height = true;
	//		@Config.Comment("Step Height Adjustment when wearing Race Rings. Set to False to Disable. Default True")
	//		@Name("Step Height")
	//		//		@Config.RequiresWorldRestart
	//		public boolean C01_Step_Height = true;
	//		@Config.Comment("Fairy's Ring Climbing Ability. Set to False to Disable. Default True")
	//		@Name("Climbing")
	//		//		@Config.RequiresWorldRestart
	//		public boolean C02_Climbing = true;
	//
	//		@Config.Comment("Dragon's Eye Effect List")
	//		@Name("Dragon's Eye Effects")
	//		public Effects effects = new Effects();
	//		public class Effects {
	//			@Config.Comment("WARNING! SETTING THESE VALUES TOO HIGH WILL CAUSE YOU TO LAG. Try to Keep within a range of 4-16")
	//			@Name("Detection Range")
	//			public Detection_Range DR = new Detection_Range();
	//			public class Detection_Range {
	//				@Config.Comment("How Far Vertically(Up, Down) in Blocks the Dragon's Eye effect triggers. Default 6")
	//				@Name("Vertical Distance")
	//				@RangeInt(min = 0, max = 32)
	//				public int C00_VD = 6;
	//				@Config.Comment("How Far Horizontally(N, E, S, W) in Blocks the Dragon's Eye effect triggers. Default 12")
	//				@Name("Horizontal Distance")
	//				@RangeInt(min = 0, max = 32)
	//				public int C001_HD = 12;
	//			}
	//			@Config.Comment("How Often the Dragon's Eye effect triggers in Ticks. try to avoid lower values. Default 79")
	//			@Name("Refresh Rate")
	//			@RangeInt(min = 20, max = 360)
	//			public int C00_RR = 79;
	//			@Config.Comment("Dragon's Eye Effects. Set to False to Disable. Default True")
	//			@Name("All Dragon's Eye Effects")
	//			public boolean C01_Dragon_Eye = true;
	//			@Config.Comment("Dragon's Eye Night Vision Effect. Set to False to Disable. Default True")
	//			@Name("Night Vision")
	//			public boolean C02_NV = true;
	//			@Config.Comment("Dragon's Eye Coal Effects. Set to False to Disable. Default True")
	//			@Name("Coal")
	//			public boolean C03_Coal = true;
	//			@Config.Comment("Dragon's Eye Iron Effects. Set to False to Disable. Default True")
	//			@Name("Iron")
	//			public boolean C04_Iron = true;
	//			@Config.Comment("Dragon's Eye Gold Effects. Set to False to Disable. Default True")
	//			@Name("Gold")
	//			public boolean C05_Gold = true;
	//			@Config.Comment("Dragon's Eye Redstone Effects. Set to False to Disable. Default True")
	//			@Name("Redstone")
	//			public boolean C06_Redstone = true;
	//			@Config.Comment("Dragon's Eye Lapis Effects. Set to False to Disable. Default True")
	//			@Name("Lapis")
	//			public boolean C07_Lapis = true;
	//			@Config.Comment("Dragon's Eye Diamond Effects. Set to False to Disable. Default True")
	//			@Name("Diamond")
	//			public boolean C08_Diamond = true;
	//			@Config.Comment("Dragon's Eye Emerald Effects. Set to False to Disable. Default True")
	//			@Name("Emerald")
	//			public boolean C09_Emerald = true;
	//			@Config.Comment("Dragon's Eye Quarts Effects. Set to False to Disable. Default True")
	//			@Name("Quarts")
	//			public boolean C10_Quarts = true;
	//			@Config.Comment("Dragon's Eye Chests Effects. Set to False to Disable. Default True")
	//			@Name("Chests")
	//			public boolean C11_Chests = true;
	//		}
	//	}

	public static void readConfig() {
		Configuration cfg = Main.config;
		try {
			cfg.load();
		} catch (Exception e1) {
			//   ModTut.logger.log(Level.ERROR, "Problem loading config file!", e1);
		} finally {
			if (cfg.hasChanged()) {
				cfg.save();
			}
		}
	}

	@Mod.EventBusSubscriber(modid = Reference.MODID)
	private static class EventHandler {
		/**
		 * Inject the new values and save to the config file when the config has been changed from the GUI.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(Reference.MODID)) {
				ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
			}
		}
	}
}