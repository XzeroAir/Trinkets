package xzeroair.trinkets.util;

import java.util.Random;

public class Reference {
	
	public static final String MODID = "xat";
	public static final String NAME = "Trinkets and Baubles";
	public static final String VERSION = "0.9";
	public static final String DEPENDENCIES = "required-after:forge@[14.23.0.2509,);after:Baubles;after:ProjectE";
	public static final String RESOURCE_PREFIX = MODID.toLowerCase() + ":";
	public static final String CLIENT = "xzeroair.trinkets.proxy.ClientProxy";
	public static final String COMMON = "xzeroair.trinkets.proxy.CommonProxy";
	
	 public static Random random = new Random();
}
