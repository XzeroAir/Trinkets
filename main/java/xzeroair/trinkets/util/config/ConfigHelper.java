package xzeroair.trinkets.util.config;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.OreDictionaryCompat;
import xzeroair.trinkets.util.helpers.ColorHelper;
import xzeroair.trinkets.util.helpers.StringUtils;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class ConfigHelper {

    private static String nameRegex = "([a-zA-Z0-9_*]{1,})";
    private static String itemIDRegex = "(" + nameRegex + ":" + nameRegex + ")";
    private static String materialRegex = "(material" + nameRegex + ")";
    private static String metaRegex = "(([0-9]*)|([*]))";
    private static String metaRegexOptional = "(;" + metaRegex + ")?";
    private static String doubleRegex = "(([-])?([0-9]{1,})([.][0-9]{1,})?)";
    private static String doubleRegexOptional = "(;" + doubleRegex + ")?";
    private static String optionalWordRegex = "(;[a-zA-Z]*)?";

    public static String cleanConfigEntry(final String config) {
        if (!config.isEmpty()) {
            final String configEntry = config.replaceAll("([\\[\\]\\|,;] ?)|(  )", " ").trim().replace(" ", ";");
            return configEntry;
        }
        return config;
    }

    private static final HashMap<String, ConfigHelper.AttributeEntry> attributeCacheMap = new HashMap<>();
    private static final AttributeEntry NULL_ENTRY = new AttributeEntry("null", 0, 0, false);

    public static HashMap<String, ConfigHelper.AttributeEntry> getAttributeCacheMap() {
        return attributeCacheMap;
    }

    public static void refreshAttributeCacheMap() {
        attributeCacheMap.clear();
    }

    public static ConfigHelper.AttributeEntry getAttributeEntry(String string) {
        ConfigHelper.AttributeEntry temp = ConfigHelper.getAttributeCacheMap().computeIfAbsent(string, ConfigHelper::getAttributeEntryOld);
        return temp == NULL_ENTRY ? null : temp;
    }

    public static AttributeEntry getAttributeEntryOld(String string) {
        String configEntry = cleanConfigEntry(string.replaceFirst("[nN][aA][mM][eE][:]", "").replaceFirst("[aA][mM][oO][uU][nN][tT][:]", "").replaceFirst("[oO][pP][eE][rR][aA][tT][iI][oO][nN][:]", ""));
        String[] vars = configEntry.split(";");
        String arg1 = StringUtils.getStringFromArray(vars, 0); // Attribute Name
        String arg2 = StringUtils.getStringFromArray(vars, 1); // Amount
        String arg3 = StringUtils.getStringFromArray(vars, 2); // Operation
        String arg4 = StringUtils.getStringFromArray(vars, 3); // Saved

        String attributeRegex = "([a-zA-Z0-9_.]*)";
        String amountRegex = doubleRegex;
        String opRegex = "([0-2])";
        String boolRegex = "(true)|(false)";
        String attribute = "";
        double amount = 0;
        int op = 0;
        boolean saved = false;
        boolean isAttribute = false;
        if (!arg1.isEmpty()) {
            if (arg1.matches(attributeRegex)) {
                attribute = arg1;
                if (!arg2.isEmpty() && arg2.matches(amountRegex)) {
                    try {
                        amount = Double.parseDouble(arg2.replace("+", ""));
                        isAttribute = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!arg3.isEmpty() && arg3.matches(opRegex)) {
                        try {
                            op = Integer.parseInt(arg3);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (!arg4.isEmpty() && arg4.toLowerCase().matches(boolRegex)) {
                        if (arg4.equalsIgnoreCase("true")) {
                            saved = true;
                        }
                    }
                }
            }
        }
        return isAttribute && !attribute.isEmpty() && (amount != 0.0) ? new ConfigHelper.AttributeEntry(attribute, amount, op, saved) : NULL_ENTRY;
    }

    private static <T> T continueCommand(String string, Function<String, T> func) {
        return func.apply(string);
    }

    private static <T> T continueCommand(String arg1, String arg2, BiFunction<String, String, T> func) {
        return func.apply(arg1, arg2);
    }

    public static class TrinketConfigStorage {

        public static TreeMap<String, MPRecoveryItem> MagicRecoveryItems = new TreeMap();
        public static LinkedHashMap<String, ConfigEquipmentObject> ArmorWeightValues = new LinkedHashMap();
        public static LinkedHashMap<String, ConfigEquipmentObject> BareHandedItems = new LinkedHashMap();
        public static LinkedHashMap<String, ConfigEquipmentObject> BowWeights = new LinkedHashMap();

        public static void init() {
            initRecoveryItems();
            initBareHandedEquipment();
            initEquipmentWeight();
            initBowWeights();
        }

        public static ConfigEquipmentObject getEquipmentEntry(String... strings) {
            return getEquipmentEntry((Predicate<ConfigEquipmentObject>) null, strings);
        }

        public static ConfigEquipmentObject getEquipmentEntry(Predicate<ConfigEquipmentObject> predicate, String... strings) {
            return getListEntry(ArmorWeightValues, predicate, strings);
        }

        public static ConfigEquipmentObject getEquipmentEntry(BiPredicate<String, ConfigEquipmentObject> predicate, String... strings) {
            return getListEntry(ArmorWeightValues, predicate, strings);
        }

        public static <v, s> v getListEntry(Map<s, v> map, BiPredicate<s, v> predicate, s... keys) {
            for (s s : keys) {
                v e = map.get(s);
                if (e != null) {
                    if ((predicate != null)) {
                        if (predicate.test(s, e)) {
                            return e;
                        }
                    } else {
                        return e;
                    }
                }
            }
            if ((predicate != null) && (keys.length <= 0)) {
                for (Entry<s, v> e : map.entrySet()) {
                    if (predicate.test(e.getKey(), e.getValue())) {
                        return e.getValue();
                    }
                }
            }
            return null;
        }

        public static <v, s> v getListEntry(Map<s, v> map, Predicate<v> predicate, s... keys) {
            for (s s : keys) {
                v e = map.get(s);
                if (e != null) {
                    if ((predicate != null)) {
                        if (predicate.test(e)) {
                            return e;
                        }
                    } else {
                        return e;
                    }
                }
            }
            if ((predicate != null) && (keys.length <= 0)) {
                for (Entry<s, v> e : map.entrySet()) {
                    if (predicate.test(e.getValue())) {
                        return e.getValue();
                    }
                }
            }
            return null;
        }

        public static void initBareHandedEquipment() {
            if (!BareHandedItems.isEmpty()) {
                BareHandedItems.clear();
            }
            final String[] list = TrinketsConfig.SERVER.RACES.FAELIS.BARE_HANDS;
            for (String entry : list) {
                ConfigEquipmentObject config = new ConfigEquipmentObject(entry);
                if (!config.isEmpty()) {
                    String equipSlot = config.getEquipmentSlot();
                    String toolType = config.getEquipmentType();
                    BareHandedItems.put(config.getObjectRegistryName() + (equipSlot.isEmpty() ? "" : ":" + equipSlot) + (toolType.isEmpty() ? "" : ":" + toolType), config);
                }
            }
        }

        public static void initEquipmentWeight() {
            if (!ArmorWeightValues.isEmpty()) {
                ArmorWeightValues.clear();
            }
            final String[] list = TrinketsConfig.SERVER.RACES.FAELIS.HEAVY_ARMOR;
            for (String entry : list) {
                ConfigEquipmentObject config = new ConfigEquipmentObject(entry);
                if (!config.isEmpty()) {
                    String equipSlot = config.getEquipmentSlot();
                    String toolType = config.getEquipmentType();
                    ArmorWeightValues.put(config.getObjectRegistryName() + (equipSlot.isEmpty() ? "" : ":" + equipSlot) + (toolType.isEmpty() ? "" : ":" + toolType), config);
                }
            }
        }

        public static void initBowWeights() {
            if (!BowWeights.isEmpty()) {
                BowWeights.clear();
            }
            final String[] weightValues = TrinketsConfig.SERVER.RACES.ELF.ABILITIES.SKILLED_ARCHER.BOWS;
            for (String entry : weightValues) {
                ConfigEquipmentObject bowWeightObject = new ConfigEquipmentObject(entry);
                if (!bowWeightObject.isEmpty()) {
                    boolean blacklistedBow = false;
                    final String[] blacklist = TrinketsConfig.SERVER.RACES.ELF.ABILITIES.SKILLED_ARCHER.BOW_BLACKLIST;
                    for (final String s : blacklist) {
                        ConfigHelper.ConfigObject object = new ConfigHelper.ConfigObject(s);
                        if (object.doesConfigObjectMatch(bowWeightObject)) {
                            blacklistedBow = true;
                            break;
                        }
                    }
                    if (!blacklistedBow) {
                        String equipSlot = bowWeightObject.getEquipmentSlot();
                        String toolType = bowWeightObject.getEquipmentType();
                        BowWeights.put(bowWeightObject.getObjectRegistryName() + (equipSlot.isEmpty() ? "" : ":" + equipSlot) + (toolType.isEmpty() ? "" : ":" + toolType), bowWeightObject);
                    }
                }
            }
        }

        public static void initRecoveryItems() {
            if (!MagicRecoveryItems.isEmpty()) {
                MagicRecoveryItems.clear();
            }
            final String[] recovery = TrinketsConfig.SERVER.MAGIC.recovery;
            for (String entry : recovery) {
                final MPRecoveryItem recoveryItem = new MPRecoveryItem(entry);
                if (!recoveryItem.isEmpty()) {
                    MagicRecoveryItems.put(recoveryItem.getObjectRegistryName(), recoveryItem);
                }
            }
        }

    }


    /// CONFIG OBJECTS

    public static class ConfigTreasureObject extends ConfigObject {

        protected int color;
        public static ConfigTreasureObject EMPTY = new ConfigTreasureObject("");

        public ConfigTreasureObject(String configEntry) {
            super(configEntry);
            color = 16766720;
            this.initValues(this.getObjectArgs());
        }

        private void initValues(String[] args) {
            String Color = StringUtils.getStringFromArray(args, 0);
            if (!Color.isEmpty()) {
                try {
                    color = ColorHelper.getColorFromString(Color.replace("*", OreDictionaryCompat.wildcard + ""));
                } catch (Exception e) {
                    Trinkets.LOGGER.error("Invalid format for entry: " + this.getOriginalEntry());
                    e.printStackTrace();
                    color = 16766720;
                }
            }
        }

        public final int getColor() {
            return color;
        }

        public String parseTargetName() {
            return parseTargetName(this).trim();
        }

        public String parseTargetName(final ConfigTreasureObject treasure) {
            if (treasure == null || treasure.isEmpty()) {
                return "";
            }
            try {
                String target = treasure.getObjectRegistryName();
                if (treasure.getObjectType() == EntryType.OREDICTIONARY) {
                    return target.replaceFirst("oreDict:", "").replaceAll("(([ ]?[oO][rR][eE])|([\\[\\]])|([tT][iI][lL][eE][\\.]))", "");
                } else if (treasure.getObjectType() == EntryType.ENTITY) {
                    try {
                        ResourceLocation t = new ResourceLocation(target);
                        if (EntityList.isRegistered(t)) {
                            return new TextComponentTranslation("entity." + EntityList.getTranslationName(t) + ".name").getFormattedText();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (treasure.getObjectType() == EntryType.MATERIAL) {
                    return "";
                } else {
                    final String namespace = treasure.getModID();
                    final String pathID = treasure.getObjectID();
                    final boolean isWildSpace = namespace.contentEquals("*");
                    final boolean isWildPath = pathID.contentEquals("*");
                    final int meta = treasure.getMeta();
                    if (isWildSpace) {
                        if (isWildPath) {
                            return "";
                        } else {
                            return pathID;
                        }
                    } else {
                        if (isWildPath) {
                            return namespace + " " + new TextComponentTranslation("stat.blocksButton").getFormattedText();
                        } else {
                            final Item itemTarget = Item.getByNameOrId(target);
                            ItemStack parseName = new ItemStack(itemTarget, 1);
                            if (itemTarget == null) {
                                if (Block.getBlockFromName(target) != null) {
                                    target = Block.getBlockFromName(target).getRegistryName().toString();
                                }
                            }
                            target = parseName.getTextComponent().getUnformattedText();
                            if ((itemTarget != null) && itemTarget.getHasSubtypes() && (meta != OreDictionaryCompat.wildcard)) {
                                final NonNullList<ItemStack> parseMeta = NonNullList.create();
                                itemTarget.getSubItems(CreativeTabs.SEARCH, parseMeta);
                                for (final ItemStack t : parseMeta) {
                                    if (t.getMetadata() == meta) {
                                        parseName = new ItemStack(itemTarget, 1, meta);
                                        target = parseName.getTextComponent().getUnformattedText();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (target.equalsIgnoreCase("[air]")) {
                        return "";
                    }
                    return target.replaceAll("(" + "([ ]?[oO][rR][eE])" + "|" + "([\\[\\]])" + "|" + "([tT][iI][lL][eE][\\.])" + ")", "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

    }

    public static class MPRecoveryItem extends ConfigObject {

        protected float amount;
        protected boolean multiplied;

        public MPRecoveryItem(String configEntry) {
            super(configEntry);
            this.initValues(this.getObjectArgs());
        }

        private void initValues(String[] args) {
            String Amount = StringUtils.getStringFromArray(args, 0);
            if (!Amount.isEmpty()) {
                if (Amount.endsWith("%")) {
                    multiplied = true;
                }
                Amount = Amount.replace("%", "");
                try {
                    amount = Float.parseFloat(Amount);
                } catch (Exception e) {
                    Trinkets.LOGGER.error("Invalid format for entry: " + this.getOriginalEntry());
                    e.printStackTrace();
                    amount = 0;
                }
            }
        }

        public final float getAmount() {
            return amount;
        }

        public final boolean isMultiplied() {
            return multiplied;
        }

    }

    public static class ConfigEquipmentObject extends ConfigObject {

        public static final Set<String> validHandTypes = new HashSet<>(Arrays.asList("mainhand", "offhand"));
        public static final Set<String> validArmorTypes = new HashSet<>(Arrays.asList("feet", "legs", "chest", "head"));
        public static final Set<String> validEquipmentTypes = new HashSet<>(Arrays.asList("feet", "legs", "chest", "head", "mainhand", "offhand"));
        public static final Set<String> validArmsTypes = new HashSet<>(Arrays.asList("sword", "shield", "axe", "pickaxe", "shovel", "hoe"));
        public static final Set<String> validToolTypes = new HashSet<>(Arrays.asList("axe", "pickaxe", "shovel", "hoe", "shears"));

        protected double equipmentWeight;
        protected String slotType;
        protected String equipmentType;

        public ConfigEquipmentObject(String configEntry) {
            super(configEntry);
        }

        @Override
        protected void generateConfigObject(String configEntry) {
            slotType = "";
            equipmentType = "";
            configEntry.replaceAll("([\\[\\]\\|,;] ?)", ";").trim();
            final String[] checkArgs = configEntry.split(";", 2);
            final String entry = StringUtils.getStringFromArray(checkArgs, 0);
            if (!entry.isEmpty()) {
                final String configArgs = StringUtils.getStringFromArray(checkArgs, 1);
                if (entry.matches(itemIDRegex)) {
                    this.configureNormalEntry(entry, configArgs);
                } else {
                    this.configureMaterialEntry(entry.toLowerCase(), configArgs);
                }
            }
        }

        @Override
        public boolean doesItemMatchEntry(ItemStack stack) {
            boolean matches = super.doesItemMatchEntry(stack);
            if (matches) {
                final Item item = stack.getItem();
                String t = this.getEquipmentType();
                String s = this.getEquipmentSlot();
                final String itemType = getItemType(stack).toLowerCase();
                boolean typeMatch = t.isEmpty() ? true : t.equals("tool") ? (item instanceof ItemTool) || !isValidTools(itemType).isEmpty() : t.equals("armor") ? (item instanceof ItemArmor) || !isValidArmor(itemType).isEmpty() : t.equals(itemType);
                boolean slotMatch = s.isEmpty() ? true : isValidArmor(itemType).isEmpty();
                final String regName = item.getRegistryName().toString();
                boolean regMatch = this.getObjectRegistryName().contentEquals(regName);
                if (regMatch && typeMatch && slotMatch) {
                    return true;
                }
                return typeMatch && slotMatch;
            }
            return matches;
        }

        @Override
        protected boolean doesMaterialMatch(ItemStack stack) {
            return super.doesMaterialMatch(stack);
        }

        @Override
        protected void configureNormalEntry(String entry, String configArgs) {
            super.configureNormalEntry(entry, configArgs);
            int index = 0;
            if (!this.addEquipmentWeight(StringUtils.getStringFromArray(this.getObjectArgs(), index))) {
                if (this.addEquipmentSlot(StringUtils.getStringFromArray(this.getObjectArgs(), index))) {
                    index += 1;
                }
                if (this.addArmType(StringUtils.getStringFromArray(this.getObjectArgs(), index))) {
                    index += 1;
                }
                this.addEquipmentWeight(StringUtils.getStringFromArray(this.getObjectArgs(), index));
            }
        }

        @Override
        protected void configureMaterialEntry(String entry, String configArgs) {
            super.configureMaterialEntry(entry, configArgs);
            int index = 0;
            if (!this.addEquipmentWeight(StringUtils.getStringFromArray(this.getObjectArgs(), index))) {
                if (this.addEquipmentSlot(StringUtils.getStringFromArray(this.getObjectArgs(), index))) {
                    index += 1;
                }
                if (this.addArmType(StringUtils.getStringFromArray(this.getObjectArgs(), index))) {
                    index += 1;
                }
                this.addEquipmentWeight(StringUtils.getStringFromArray(this.getObjectArgs(), index));
            }
        }

        private boolean addArmType(String entry) {
            String toolType = isValidArms(entry);
            if (!toolType.isEmpty() || entry.equals("tool")) {
                equipmentType = entry;
                return true;
            } else {
                equipmentType = "";
                return false;
            }
            //			if (!entry.isEmpty() && entry.matches("([a-zA-Z]{1,})")) {
            //				slotType = this.fixArmorType(entry);
            //				return true;
            //			}
        }

        private boolean addEquipmentSlot(String entry) {
            String slotType = isValidEquipmentSlot(this.fixArmorType(entry));
            if (!slotType.isEmpty() || entry.equals("armor") || entry.equals("hand")) {
                this.slotType = entry;
                return true;
            } else {
                this.slotType = "";
                return false;
            }
            //			if (!entry.isEmpty() && entry.matches("([a-zA-Z]{1,})")) {
            //				slotType = this.fixArmorType(entry);
            //				return true;
            //			}
        }

        private boolean addEquipmentWeight(String entry) {
            if (!entry.isEmpty() && entry.matches(doubleRegex)) {
                try {
                    equipmentWeight = Double.parseDouble(entry);
                    return true;
                } catch (Exception e) {
                    Trinkets.LOGGER.error("Invalid format for entry: " + this.getOriginalEntry());
                    e.printStackTrace();
                }
            }
            equipmentWeight = 0;
            return false;
        }

        protected String fixArmorType(String string) {
            return string.replace("boot", "feet").replace("boots", "feet").replace("feets", "feet").replace("leggings", "legs").replace("pants", "legs").replace("chestplate", "chest").replace("shirt", "chest").replace("helm", "head").replace("hat", "head");
        }

        public static String getItemMaterial(@Nonnull ItemStack stack) {
            if (stack.isEmpty()) {
                return "";
            }
            Item item = stack.getItem();
            if (item instanceof ItemArmor) {
                final ItemArmor armor = ((ItemArmor) item);
                return armor.getArmorMaterial().toString();
            } else if (item instanceof ItemSword) {
                ItemSword sword = (ItemSword) item;
                return sword.getToolMaterialName();
            } else if (item instanceof ItemTool) {
                ItemTool tool = (ItemTool) item;
                return tool.getToolMaterialName();
            } else if (item instanceof ItemHoe) {
                ItemHoe hoe = (ItemHoe) item;
                return hoe.getMaterialName();
            }
            return "";
        }

        public static String getItemType(@Nonnull ItemStack stack) {
            if (stack.isEmpty()) {
                return "";
            }
            Item item = stack.getItem();
            if (item instanceof ItemArmor) {
                return ((ItemArmor) item).armorType.getName();
            }
            if (item instanceof ItemSword) {
                return "sword";
            }
            if (item instanceof ItemBow) {
                return "bow";
            }
            if (item instanceof ItemShield) {
                return "shield";
            }
            if (item instanceof ItemAxe) {
                return "axe";
            }
            if (item instanceof ItemPickaxe) {
                return "pickaxe";
            }
            if (item instanceof ItemSpade) {
                return "shovel";
            }
            if (item instanceof ItemHoe) {
                return "hoe";
            }
            if (item instanceof ItemShears) {
                return "shears";
            }
            if (item instanceof ItemTool) {
                return "tool";
            }
            return "";
        }

        public static String isValidEquipmentSlot(String input) {
            if (!input.isEmpty() && validEquipmentTypes.contains(input)) {
                return input;
            }
            return "";
        }

        public static String isValidArms(String input) {
            if (!input.isEmpty() && validArmsTypes.contains(input)) {
                return input;
            }
            return "";
        }

        public static String isValidTools(String input) {
            if (!input.isEmpty() && validToolTypes.contains(input)) {
                return input;
            }
            return "";
        }

        public static String isValidArmor(String input) {
            if (!input.isEmpty() && validArmorTypes.contains(input)) {
                return input;
            }
            return "";
        }

        public static String isValidHand(String input) {
            if (!input.isEmpty() && validHandTypes.contains(input)) {
                return input;
            }
            return "";
        }

        public String getEquipmentSlot() {
            return slotType;
        }

        public String getEquipmentType() {
            return equipmentType;
        }

        public double getEquipmentWeight() {
            return equipmentWeight;
        }
    }

    public static class PotionEntry extends ConfigObject {

        protected Potion potion;
        protected PotionEffect effect;
        protected int duration;
        protected int amplifier;

        public PotionEntry(String ConfigEntry) {
            super(ConfigEntry);
        }

        @Override
        public boolean doesItemMatchEntry(ItemStack stack) {
            if ((stack == null) || stack.isEmpty() || this.isEmpty()) {
                return false;
            }
            return super.doesItemMatchEntry(stack);
        }
    }

    public static class AttributeEntry {

        private String attribute;
        private double amount;
        private int operation;
        private boolean isSaved;

        public AttributeEntry(String attribute, double amount, int operation, boolean isSaved) {
            this.attribute = attribute;
            this.amount = amount;
            this.operation = operation;
            this.isSaved = isSaved;
        }

        public String getAttribute() {
            return attribute;
        }

        public double getAmount() {
            return amount;
        }

        public int getOperation() {
            return operation;
        }

        public boolean isSaved() {
            return isSaved;
        }

        @Override
        public String toString() {
            return "AttributeEntry [attribute=" + attribute + ", amount=" + amount + ", operation=" + operation + ", isSaved=" + isSaved + "]";
        }

    }

    public static class ConfigObject {

        protected String originalEntry;
        protected String modID;
        protected String objectID;
        protected int meta;
        protected String[] objectArgs;
        protected EntryType objectType;
        public static ConfigObject EMPTY_CONFIG = new ConfigObject("");

        public ConfigObject(String modID, String objectID, int meta) {
            this(modID, objectID, meta, new String[0]);
        }

        public ConfigObject(String config) {
            this("minecraft", "air", OreDictionaryCompat.wildcard);
            originalEntry = config;
            this.generateConfigObject(config);
        }

        public ConfigObject(String modID, String objectID, int meta, String[] args) {
            originalEntry = modID + ":" + objectID + ";" + meta;
            this.modID = modID;
            this.objectID = objectID;
            this.meta = meta;
            objectType = EntryType.UNKNOWN;
            objectArgs = args;
        }

        public final String getModID() {
            return modID;
        }

        public final String getObjectID() {
            return objectID;
        }

        public final String getObjectRegistryName() {
            return this.getModID() + ":" + this.getObjectID();
        }

        public final int getMeta() {
            return meta;
        }

        public final EntryType getObjectType() {
            return objectType;
        }

        public final String[] getObjectArgs() {
            return objectArgs;
        }

        public final String getOriginalEntry() {
            return originalEntry;
        }

        public final boolean isEmpty() {
            String reg = this.getObjectRegistryName();
            boolean flag1 = reg.contentEquals("minecraft:air");
            boolean flag2 = reg.contentEquals("*:air");
            return flag1 || flag2;
        }

        protected void generateConfigObject(String configEntry) {
            configEntry = cleanConfigEntry(configEntry);
            final String[] checkArgs = configEntry.split(";", 2);
            final String entry = StringUtils.getStringFromArray(checkArgs, 0);
            if (!entry.isEmpty()) {
                final String configArgs = StringUtils.getStringFromArray(checkArgs, 1);//.split(";", 2);
                final boolean existsInOreDict = OreDictionaryCompat.existsInOreDictionary(entry);
                if (existsInOreDict) {
                    this.configureOreDictEntry(entry, configArgs);
                } else if (EntityList.isRegistered(new ResourceLocation(entry))) {
                    this.configureEntityEntry(entry, configArgs);
                } else if (entry.matches(itemIDRegex)) {
                    this.configureNormalEntry(entry, configArgs);
                } else if (entry.startsWith("!mat:") && !entry.replaceFirst("!mat:", "").isEmpty()) {
                    this.configureMaterialEntry(entry.replaceFirst("!mat:", "").toLowerCase(), configArgs);
                } else {
                    this.configureUnknownEntry(entry, configArgs);
                }
            }
        }

        protected void configureOreDictEntry(String entry, String configArgs) {
            objectType = EntryType.OREDICTIONARY;
            modID = "oreDict";
            objectID = entry;
            if (!configArgs.isEmpty()) {
                objectArgs = configArgs.split(";");
            }
        }

        protected void configureEntityEntry(String entry, String configArgs) {
            objectType = EntryType.ENTITY;
            if (entry.contains(":")) {
                final String[] entryArgs = entry.split(":");
                final String mod = StringUtils.getStringFromArray(entryArgs, 0);
                final String entity = StringUtils.getStringFromArray(entryArgs, 1);
                if (!mod.isEmpty()) {
                    modID = mod;
                }
                if (!entity.isEmpty()) {
                    objectID = entity;
                }
            } else {
                objectID = entry;
            }
            if (!configArgs.isEmpty()) {
                objectArgs = configArgs.split(";");
            }
        }

        protected void configureMaterialEntry(String entry, String configArgs) {
            objectType = EntryType.MATERIAL;
            modID = "ObjectMaterial";
            objectID = entry;
            if (!configArgs.isEmpty()) {
                objectArgs = configArgs.split(";");
            }
        }

        protected void configureNormalEntry(String entry, String configArgs) {
            objectType = EntryType.NORMAL;
            final String[] itemArgs = entry.split(":");
            final String mod = StringUtils.getStringFromArray(itemArgs, 0);
            final String item = StringUtils.getStringFromArray(itemArgs, 1);
            if (!mod.isEmpty()) {
                modID = mod;
            }
            if (!item.isEmpty()) {
                objectID = item;
            }
            if (mod.equalsIgnoreCase("minecraft") && item.equalsIgnoreCase("potion")) {
                objectType = EntryType.POTION;
                if (!configArgs.isEmpty()) {
                    objectArgs = configArgs.split(";");
                }
            } else {
                final String[] args = configArgs.split(";", 2);
                final String Meta = StringUtils.getStringFromArray(args, 0);
                if (!Meta.isEmpty()) {
                    int index = 1;
                    if (Meta.contentEquals("*")) {
                    } else if (Meta.matches("([0-9]{1,})")) {
                        try {
                            meta = Math.min(Math.max(Integer.parseInt(Meta), 0), OreDictionaryCompat.wildcard);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        index = 0;
                    }
                    String leftoverArgs = StringUtils.getStringFromArray(args, index);
                    if (!leftoverArgs.isEmpty()) {
                        objectArgs = leftoverArgs.split(";");
                    }
                }
            }
        }

        protected void configureUnknownEntry(String entry, String configArgs) {
            objectType = EntryType.UNKNOWN;
            modID = "unknown";
            objectID = entry;
            if (!configArgs.isEmpty()) {
                objectArgs = configArgs.split(";");
            }
        }

        public boolean doesItemMatchEntry(@Nonnull ItemStack stack) {
            if ((stack == null) || stack.isEmpty() || this.isEmpty()) {
                return false;
            }
            final EntryType type = this.getObjectType();
            switch (type) {
                case OREDICTIONARY:
                    return this.doesOreDictMatch(stack);
                case MATERIAL:
                    return this.doesMaterialMatch(stack);
                case POTION:
                    return this.doesPotionMatch(stack);
                default:
                    return this.doesItemMatch(stack);
            }
        }

        protected boolean doesPotionMatch(@Nonnull ItemStack stack) {
            if (this.doesItemMatch(stack)) {
                return true;
            }
            return false;
        }

        protected boolean doesItemMatch(@Nonnull ItemStack stack) {
            String mod = stack.getItem().getRegistryName().getNamespace();
            String item = stack.getItem().getRegistryName().getPath();
            if (modID.contentEquals("*") || modID.contentEquals(mod)) {
                boolean metaMatches = (meta == OreDictionaryCompat.wildcard) || (stack.getMetadata() == meta);
                if (objectID.contentEquals(item) && metaMatches) {
                    return true;
                }
                String objectEntry = objectID.replace("*", "");
                final boolean startWildcard = objectID.startsWith("*");
                final boolean endWildcard = objectID.endsWith("*");
                if (startWildcard && endWildcard) {
                    if (item.contains(objectEntry) && metaMatches) {
                        return true;
                    }
                } else if (endWildcard) {
                    if (item.startsWith(objectEntry) && metaMatches) {
                        return true;
                    }
                } else if (startWildcard) {
                    if (item.endsWith(objectEntry) && metaMatches) {
                        return true;
                    }
                }
            }
            return false;
        }

        protected boolean doesConfigObjectMatch(@Nonnull ConfigObject object) {
            String mod = object.getModID();
            String item = object.getObjectID();
            if (modID.contentEquals("*") || modID.contentEquals(mod)) {
                boolean metaMatches = (meta == OreDictionaryCompat.wildcard) || (object.getMeta() == meta);
                if (objectID.contentEquals(item) && metaMatches) {
                    return true;
                }
                String objectEntry = objectID.replace("*", "");
                final boolean startWildcard = objectID.startsWith("*");
                final boolean endWildcard = objectID.endsWith("*");
                if (startWildcard && endWildcard) {
                    if (item.contains(objectEntry) && metaMatches) {
                        return true;
                    }
                } else if (endWildcard) {
                    if (item.startsWith(objectEntry) && metaMatches) {
                        return true;
                    }
                } else if (startWildcard) {
                    if (item.endsWith(objectEntry) && metaMatches) {
                        return true;
                    }
                }
            }
            return false;
        }

        protected boolean doesOreDictMatch(@Nonnull ItemStack stack) {
            for (final String oreDictionary : OreDictionaryCompat.getOreNames(stack)) {
                if (oreDictionary.equalsIgnoreCase(objectID)) {
                    return true;
                }
            }
            return false;
        }

        protected boolean doesMaterialMatch(@Nonnull ItemStack stack) {
            String[] checkForType = objectID.split(":");
            String mat = StringUtils.getStringFromArray(checkForType, 0);
            String ItemMaterial = ConfigEquipmentObject.getItemMaterial(stack).toLowerCase();
            return !ItemMaterial.isEmpty() && mat.equalsIgnoreCase(ItemMaterial);
        }

        public boolean doesBlockMatchEntry(@Nonnull IBlockState state) {
            if (this.isEmpty()) {
                return false;
            }
            final ItemStack blockStack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
            return this.doesItemMatchEntry(blockStack);
        }
    }

    public enum EntryType {
        UNKNOWN, NORMAL, ITEM, BLOCK, ENTITY, MATERIAL, POTION, OREDICTIONARY
    }

}
