package xzeroair.trinkets.util;

import java.text.DecimalFormat;
import java.util.Random;

public class Reference {

    public static final String MODID = "xat";
    public static final String NAME = "Trinkets and Baubles";
    public static final String VERSION = "0.32.6";
    public static final String DEPENDENCIES = "required-after:forge@[14.21.1.2387,);after:baubles";
    public static final String acceptedMinecraftVersions = "";
    public static final String RESOURCE_PREFIX = MODID.toLowerCase() + ":";
    public static final String CLIENT = "xzeroair.trinkets.proxy.ClientProxy";
    public static final String COMMON = "xzeroair.trinkets.proxy.CommonProxy";
    public static final String updateJSON = "https://raw.githubusercontent.com/XzeroAir/Trinkets-1.12.2/master/update.json";
    public static final String GUIFACTORY = "xzeroair.trinkets.client.gui.TrinketsGuiFactory";
    public static final String FINGERPRINT = "@FINGERPRINT@";

    public static final String VIP_LIST = "https://raw.githubusercontent.com/XzeroAir/AuxFiles/master/VipsV2.json";

    public static final String filePath = "/trinkets/";
    public static final String configPath = filePath + "Trinkets_And_Baubles";

    public static Random random = new Random();

    public static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.###");

    /**
     * Additive Attribute Operation Modifier
     */
    public static final int ATTRIBUTE_MODIFIER_OPERATION_ADD = 0;
    /**
     * Multiplicative Attribute Operation Modifier
     */
    public static final int ATTRIBUTE_MODIFIER_OPERATION_MULTIPLY_OLD_AMOUNT = 1;
    /**
     * Multiplicative Attribute Operation Modifier
     */
    public static final int ATTRIBUTE_MODIFIER_OPERATION_MULTIPLY_NEW_AMOUNT = 2;

    /**
     * Trinkets Container GUI
     */
    public static final int GUI = 0;
    /**
     * Trinkets Mana Configuration GUI
     */
    public static final int GUI_MANA = 1;
    /**
     * Trinkets Entity Properties Configuration GUI
     */
    public static final int GUI_ENTITY = 2;
    /**
     * Trinkets Entity Attributes and Abilities GUI
     */
    public static final int GUI_ATTRIBUTES = 3;
    /**
     * Trinkets Race Selection GUI
     */
    public static final int GUI_RACE_SELECTION = 4;

}
