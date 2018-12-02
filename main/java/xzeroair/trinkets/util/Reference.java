package xzeroair.trinkets.util;

import java.util.Random;

public class Reference {

	public static final String MODID = "xat";
	public static final String NAME = "Trinkets and Baubles";
	public static final String VERSION = "0.20";
	public static final String DEPENDENCIES = "required-after:forge@[14.21.1.2387,);after:baubles";
	public static final String acceptedMinecraftVersions = "";
	public static final String RESOURCE_PREFIX = MODID.toLowerCase() + ":";
	public static final String CLIENT = "xzeroair.trinkets.proxy.ClientProxy";
	public static final String COMMON = "xzeroair.trinkets.proxy.CommonProxy";
	public static final String updateJSON = "https://raw.githubusercontent.com/XzeroAir/Trinkets-1.12.2/master/update.json";

	public static Random random = new Random();

}
