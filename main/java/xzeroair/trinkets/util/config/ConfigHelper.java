package xzeroair.trinkets.util.config;

import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.annotation.Nonnull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.OreDictionaryCompat;
import xzeroair.trinkets.util.helpers.StringUtils;

public class ConfigHelper {

	private static String nameRegex = "([a-zA-Z0-9_*]{1,})";
	private static String itemIDRegex = "(" + nameRegex + ":" + nameRegex + ")";
	private static String materialRegex = "(material" + nameRegex + ")";
	private static String metaRegex = "(([0-9]*)|([*]))";
	private static String metaRegexOptional = "(;" + metaRegex + ")?";
	private static String doubleRegex = "(([-])?([0-9]{1,})([.][0-9]{1,})?)";
	private static String doubleRegexOptional = "(;" + doubleRegex + ")?";
	private static String optionalWordRegex = "(;[a-zA-Z]*)?";

	//	public static void parseEntityConfig(String[] configList) {
	//
	//	}

	public static String cleanConfigEntry(String config) {
		final String configEntry = config
				.replaceAll("([\\[\\]\\|,;] ?)|(  )", " ").trim().replace(" ", ";");
		return configEntry;
	}

	public static AttributeEntry getAttributeEntry(String string) {
		String configEntry = cleanConfigEntry(
				string.replaceFirst("[nN][aA][mM][eE][:]", "")
						.replaceFirst("[aA][mM][oO][uU][nN][tT][:]", "")
						.replaceFirst("[oO][pP][eE][rR][aA][tT][iI][oO][nN][:]", "")
		);
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
		if (isAttribute && !attribute.isEmpty() && (amount != 0)) {
			return new AttributeEntry(attribute, amount, op, saved);
		}
		return null;
	}

	private static <T> T continueCommand(String string, Function<String, T> func) {
		return func.apply(string);
	}

	private static <T> T continueCommand(String arg1, String arg2, BiFunction<String, String, T> func) {
		return func.apply(arg1, arg2);
	}

	public static class TrinketConfigStorage {

		public static TreeMap<String, MPRecoveryItem> MagicRecoveryItems = new TreeMap<>();
		public static TreeMap<Integer, TreasureEntry> TreasureBlocks = new TreeMap<>();
		public static TreeMap<String, ConfigObject> ClimbableBlocks = new TreeMap<>();
		public static TreeMap<String, ArmorEntry> ArmorWeightValues = new TreeMap<>();

		public static void init() {
			initRecoveryItems();
			initArmorWeightValues();
			initTreasureBlocks();
		}

		private static void initTreasureBlocks() {
			if (!TreasureBlocks.isEmpty()) {
				TreasureBlocks.clear();
			}
			final String[] treasures = TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.Blocks;
			int index = 0;
			for (String entry : treasures) {
				TreasureEntry treasure = new TreasureEntry(entry);
				if (!treasure.isEmpty()) {
					TreasureBlocks.put(index, treasure);
					index++;
				}
			}
		}

		private static void initArmorWeightValues() {
			if (!ArmorWeightValues.isEmpty()) {
				ArmorWeightValues.clear();
			}
			final String[] weightValues = TrinketsConfig.SERVER.races.faelis.heavyArmor;
			for (String entry : weightValues) {
				ArmorEntry weightValue = new ArmorEntry(entry);
				if (!weightValue.isEmpty()) {
					ArmorWeightValues.put(weightValue.getObjectRegistryName(), weightValue);
				}
			}
		}

		private static void initRecoveryItems() {
			if (!MagicRecoveryItems.isEmpty()) {
				MagicRecoveryItems.clear();
			}
			final String[] recovery = TrinketsConfig.SERVER.mana.recovery;
			for (String entry : recovery) {
				final MPRecoveryItem recoveryItem = new MPRecoveryItem(entry);
				if (!recoveryItem.isEmpty()) {
					MagicRecoveryItems.put(recoveryItem.getObjectRegistryName(), recoveryItem);
				}
			}
		}

	}

	public static class TreasureEntry extends ConfigObject {

		protected int color;

		public TreasureEntry(String configEntry) {
			super(configEntry);
			color = 16766720;
			this.initValues(this.getObjectArgs());
		}

		private void initValues(String[] args) {
			String Color = StringUtils.getStringFromArray(args, 0);
			if (!Color.isEmpty()) {
				try {
					// TODO this causing a parsing Error for Entity Entries
					color = Integer.parseInt(Color.replace("*", OreDictionaryCompat.wildcard + ""));
				} catch (Exception e) {
					Trinkets.log.error("Invalid format for entry: " + this.getOriginalEntry());
					e.printStackTrace();
					color = 16766720;
				}
			}
		}

		public final int getColor() {
			return color;
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
					Trinkets.log.error("Invalid format for entry: " + this.getOriginalEntry());
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

	public static class ArmorEntry extends ConfigObject {

		protected double weight;

		public ArmorEntry(String configEntry) {
			super(configEntry);
			weight = TrinketsConfig.SERVER.races.faelis.penalty_amount;
			this.initValues(this.getObjectArgs());
		}

		@Override
		public boolean doesItemMatchEntry(ItemStack stack) {
			if ((stack == null) || stack.isEmpty() || this.isEmpty()) {
				return false;
			}
			boolean superMatched = super.doesItemMatchEntry(stack);
			if (stack.getItem() instanceof ItemArmor) {
				ItemArmor armor = (ItemArmor) stack.getItem();
				String armorType = armor.armorType.getName();
				String optionalType = StringUtils.getStringFromArray(this.getObjectArgs(), 0);
				if (!optionalType.isEmpty()) {
					if (optionalType.matches("([a-zA-Z]{1,})")) {
						String fixedType = this.fixArmorType(optionalType);
						return superMatched && armorType.equalsIgnoreCase(fixedType);
					}
				}
			}
			return superMatched;
		}

		private void initValues(String[] args) {
			String optionalType = StringUtils.getStringFromArray(this.getObjectArgs(), 0);
			if (!optionalType.isEmpty()) {
				if (optionalType.matches(doubleRegex)) {
					try {
						weight = Double.parseDouble(optionalType);
					} catch (Exception e) {
						Trinkets.log.error("Invalid format for entry: " + this.getOriginalEntry());
						e.printStackTrace();
						weight = 0;
					}
				} else {
					String optionalWeight = StringUtils.getStringFromArray(this.getObjectArgs(), 1);
					if (!optionalWeight.isEmpty() && optionalWeight.matches(doubleRegex)) {
						try {
							weight = Double.parseDouble(optionalWeight);
						} catch (Exception e) {
							Trinkets.log.error("Invalid format for entry: " + this.getOriginalEntry());
							e.printStackTrace();
							weight = 0;
						}
					}
				}
			}

		}

		protected String fixArmorType(String string) {
			return string
					.replace("boot", "feet")
					.replace("boots", "feet")
					.replace("feets", "feet")
					.replace("leggings", "legs")
					.replace("pants", "legs")
					.replace("chestplate", "chest")
					.replace("shirt", "chest")
					.replace("helm", "head")
					.replace("hat", "head");
		}

		public final double getWeight() {
			return weight;
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
				final String[] configArgs = StringUtils.getStringFromArray(checkArgs, 1).split(";", 2);
				final boolean existsInOreDict = OreDictionaryCompat.existsInOreDictionary(entry);
				if (existsInOreDict) {
					objectType = EntryType.OREDICTIONARY;
					modID = "oreDict";
					objectID = entry;
					String leftoverArgs = StringUtils.getStringFromArray(configArgs, 0);
					if (!leftoverArgs.isEmpty()) {
						objectArgs = leftoverArgs.split(";");
					}
				} else if (EntityList.isRegistered(new ResourceLocation(entry))) {
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
					String leftoverArgs = StringUtils.getStringFromArray(configArgs, 0);
					if (!leftoverArgs.isEmpty()) {
						objectArgs = leftoverArgs.split(";");
					}
				} else if (entry.matches(itemIDRegex)) {
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
						String leftoverArgs = StringUtils.getStringFromArray(configArgs, 0);
						if (!leftoverArgs.isEmpty()) {
							objectArgs = leftoverArgs.split(";");
						}
					} else {
						final String Meta = StringUtils.getStringFromArray(configArgs, 0);
						if (Meta.contentEquals("*")) {

						} else if (Meta.matches("([0-9]{1,})")) {
							try {
								final int wildcard = OreDictionaryCompat.wildcard;
								meta = Integer.parseInt(Meta);
								if (meta > wildcard) {
									meta = wildcard;
								}
								if (meta < 0) {
									meta = wildcard;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						String leftoverArgs = StringUtils.getStringFromArray(configArgs, 1);
						if (!leftoverArgs.isEmpty()) {
							objectArgs = leftoverArgs.split(";");
						}
					}
				} else if (entry.startsWith("material") && !entry.replaceFirst("material", "").isEmpty()) {
					objectType = EntryType.MATERIAL;
					modID = "ObjectMaterial";
					objectID = entry.replaceFirst("material", "");
					String leftoverArgs = StringUtils.getStringFromArray(configArgs, 0);
					if (!leftoverArgs.isEmpty()) {
						objectArgs = leftoverArgs.split(";");
					}
				} else {
					objectType = EntryType.UNKNOWN;
					modID = "unknown";
					objectID = entry;
					String leftoverArgs = StringUtils.getStringFromArray(configArgs, 0);
					if (!leftoverArgs.isEmpty()) {
						objectArgs = leftoverArgs.split(";");
					}
				}
			}
		}

		public boolean doesItemMatchEntry(@Nonnull ItemStack stack) {
			if ((stack == null) || stack.isEmpty() || this.isEmpty()) {
				return false;
			}
			final EntryType type = this.getObjectType();
			if (type.equals(EntryType.OREDICTIONARY)) {
				for (final String oreDictionary : OreDictionaryCompat.getOreNames(stack)) {
					if (oreDictionary.equalsIgnoreCase(objectID)) {
						return true;
					}
				}
				return false;
			} else if (type.equals(EntryType.MATERIAL)) {
				if (stack.getItem() instanceof ItemArmor) {
					ItemArmor armor = (ItemArmor) stack.getItem();
					ArmorMaterial material = armor.getArmorMaterial();
					return material.toString().equalsIgnoreCase(objectID);
				} else if (stack.getItem() instanceof ItemTool) {
					ItemTool tool = (ItemTool) stack.getItem();
					return tool.getToolMaterialName().equalsIgnoreCase(objectID);
				} else if (stack.getItem() instanceof ItemSword) {
					ItemSword sword = (ItemSword) stack.getItem();
					return sword.getToolMaterialName().equalsIgnoreCase(objectID);
				} else if (stack.getItem() instanceof ItemHoe) {
					ItemHoe sword = (ItemHoe) stack.getItem();
					return sword.getMaterialName().equalsIgnoreCase(objectID);
				}
				return false;
			} else {
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
					} else {
					}
				}
				return false;
			}
		}

		public boolean doesBlockMatchEntry(@Nonnull IBlockState state) {
			//			if ((world == null) || (state == null) || (pos == null) || state.getBlock().isAir(state, world, pos))
			if ((state == null) || this.isEmpty()) {
				return false;
			}
			final ItemStack blockStack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
			return this.doesItemMatchEntry(blockStack);
			//			if (modID.contentEquals("oreDict")) {
			//				for (final String oreDictionary : OreTrackingHelper.getOreNames(blockStack)) {
			//					if (oreDictionary.equalsIgnoreCase(objectID))
			//						return true;
			//				}
			//				return false;
			//			} else {
			//				String mod = state.getBlock().getRegistryName().getNamespace();
			//				String block = state.getBlock().getRegistryName().getPath();
			//				if (modID.contentEquals("*") || modID.contentEquals(mod)) {
			//					boolean metaMatches = (meta == wildcard) || (state.getBlock().getMetaFromState(state) == meta);
			//					if (objectID.contentEquals(block) && metaMatches)
			//						return true;
			//					String objectEntry = objectID.replace("*", "");
			//					final boolean startWildcard = objectID.startsWith("*");
			//					final boolean endWildcard = objectID.endsWith("*");
			//					if (startWildcard && endWildcard) {
			//						if (block.contains(objectEntry) && metaMatches)
			//							return true;
			//					} else if (endWildcard) {
			//						if (block.startsWith(objectEntry) && metaMatches)
			//							return true;
			//					} else if (startWildcard) {
			//						if (block.endsWith(objectEntry) && metaMatches)
			//							return true;
			//					} else {
			//					}
			//				}
			//			}
			//			return false;
		}

	}

	public enum EntryType {
		UNKNOWN, NORMAL, ITEM, BLOCK, ENTITY, MATERIAL, POTION, OREDICTIONARY
	}

	public static class ObjectPair<A, B> {

		private A first;
		private B second;

		public ObjectPair(A first, B second) {
			this.first = first;
			this.second = second;
		}

		public A getFirst() {
			return first;
		}

		public B getSecond() {
			return second;
		}

	}

}
