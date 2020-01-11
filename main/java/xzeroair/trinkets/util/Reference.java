package xzeroair.trinkets.util;

import java.util.Random;

import net.minecraft.util.math.MathHelper;

public class Reference {

	public static final String MODID = "xat";
	public static final String NAME = "Trinkets and Baubles";
	public static final String VERSION = "0.29";
	public static final String DEPENDENCIES = "required-after:forge@[14.21.1.2387,)";
	public static final String acceptedMinecraftVersions = "";
	public static final String RESOURCE_PREFIX = MODID.toLowerCase() + ":";
	public static final String CLIENT = "xzeroair.trinkets.proxy.ClientProxy";
	public static final String COMMON = "xzeroair.trinkets.proxy.CommonProxy";
	public static final String updateJSON = "https://raw.githubusercontent.com/XzeroAir/Trinkets-1.12.2/master/update.json";
	public static final String FINGERPRINT = "@FINGERPRINT@";

	public static final String filePath = "/trinkets/";
	public static final String configPath = filePath + "Trinkets_And_Baubles";

	public static Random random = new Random();

	private final static int ATTRIBUTE_MODIFIER_OPERATION_ADD = 0;
	private static final int ATTRIBUTE_MODIFIER_OPERATION_MULTIPLY_OLD_AMOUNT = 1;
	private static final int ATTRIBUTE_MODIFIER_OPERATION_MULTIPLY_NEW_AMOUNT = 2;

	public static int getOpModifier(int i) {
		switch(i) {
		case 0: return ATTRIBUTE_MODIFIER_OPERATION_ADD;
		case 1: return ATTRIBUTE_MODIFIER_OPERATION_MULTIPLY_OLD_AMOUNT;
		case 2: return ATTRIBUTE_MODIFIER_OPERATION_MULTIPLY_NEW_AMOUNT;
		}
		return MathHelper.clamp(i, 0, 2);
	}
}
