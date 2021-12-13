package xzeroair.trinkets.util.config;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.CallHelper;

public class ConfigHelper {

	private static int wildcard = OreDictionary.WILDCARD_VALUE;

	//	public static void parseEntityConfig(String[] configList) {
	//
	//	}

	public static double parseItemArmor(ItemStack stack, String[] configList) {
		if (stack.getItem() instanceof ItemArmor) {
			final ItemArmor armor = (ItemArmor) stack.getItem();
			final String regName = armor.getRegistryName().toString().toLowerCase();
			final ArmorMaterial mat = armor.getArmorMaterial();
			boolean foundEntry = false;
			double itemWeight = TrinketsConfig.SERVER.races.faelis.penalty_amount;
			for (final String string : configList) {
				final String[] entry = string.replace("[", ";").replace("]", "").split(";");
				final int values = entry.length;
				if (values > 0) {
					final String itemOrMaterial = CallHelper.getStringFromArray(entry, 0);

					final String regex = "[^*.\\d]";
					final String metaOrWeight = CallHelper.getStringFromArray(entry, 1);
					final String weight = CallHelper.getStringFromArray(entry, 2);
					//					final String regex = "([^.\\d";
					//					System.out.println(metaString.replaceAll("\\d+(.\\d+)*", ""));
					if (itemOrMaterial.contains(":")) {
						if (regName.contentEquals(itemOrMaterial.toLowerCase())) {
							//							System.out.println(metaString.replaceAll("[^.\\d]", ""));
							if (!metaOrWeight.contains(".")) { // Looks like there's no Meta
								final String metaString = metaOrWeight.replaceAll("[^*\\d]", "");
								int meta = wildcard;
								if (metaString.contains("*")) {
								} else {
									meta = Integer.parseInt(metaString);
								}
								if ((meta == wildcard) || (stack.getItemDamage() == meta)) {
									foundEntry = true;
									if (values > 2) {
										final String weightString = weight.replaceAll("[^.\\d]", "");
										if (!weightString.isEmpty()) {
											itemWeight = Double.parseDouble(weightString);
										}
									}
									break;
								}
							} else {
								foundEntry = true;
								if (values > 1) {
									final String weightString = metaOrWeight.replaceAll("[^.\\d]", "");
									if (!weightString.isEmpty()) {
										itemWeight = Double.parseDouble(weightString);
									}
								}
								break;
							}
						}
						//					} else if (!metaString.replaceAll("[\\D]", "").isEmpty()) {
					} else {
						if (mat.toString().toLowerCase().contentEquals(itemOrMaterial.toLowerCase().replaceFirst("material", ""))) {
							final String weightOrPart = metaOrWeight.replaceAll("[^.\\d]", "");
							if (!weightOrPart.isEmpty()) {
								foundEntry = true;
								itemWeight = Double.parseDouble(weightOrPart);
								break;
							} else {
								final String partString = metaOrWeight.replaceAll("[^\\D]", "");
								if (!partString.isEmpty()) {
									if (armor.armorType.getName().equalsIgnoreCase(
											partString
													.replace("boot", "feet")
													.replace("boots", "feet")
													.replace("feets", "feet")
													.replace("leggings", "legs")
													.replace("pants", "legs")
													.replace("chestplate", "chest")
													.replace("shirt", "chest")
													.replace("helm", "head")
													.replace("hat", "head")
									)) {
										foundEntry = true;
										if (values > 2) {
											final String weightString = weight.replaceAll("[^.\\d]", "");
											if (!weightString.isEmpty()) {
												itemWeight = Double.parseDouble(weightString);
											}
										}
										break;
									}
								} else {
								}
							}
						}
					}
				}

			}
			if (foundEntry) {
				return itemWeight;
				//				System.out.println("Config Has Item " + regName + " | " + itemWeight);
			} else {
				return 0;
			}
		}
		return 0;
	}

	private static Map<String, ItemObjectHolder> magicRecoveryItems = new HashMap<>();
	private static Map<String, ItemObjectHolder> dragonEyeBlocks = new HashMap<>();

	public static Map<String, ItemObjectHolder> parseConfigList(String[] configList) {
		final Map<String, ItemObjectHolder> map = new HashMap<>();
		for (final String item : configList) {
			final ItemObjectHolder object = parseConfigItem(item);
			if ((object != null) && !object.getObject().isEmpty()) {
				if (!map.containsKey(object.getObject() + ":" + object.getMeta())) {
					map.put(object.getObject() + ":" + object.getMeta(), object);
				}
			} else {
			}
			//			} else {
			//				NonNullList<ItemStack> items = OreDictionary.getOres(itemString, false);
			//				if (!items.isEmpty()) {
			//					for (ItemStack stack : items) {
			//						if (!map.containsKey(stack.getItem().getRegistryName() + ":" + stack.getMetadata())) {
			//							String params = "";
			//							if (itemConfig.length > 1) {
			//								for (int i = 1; i < itemConfig.length; i++) {
			//									String splitter = i == (itemConfig.length - 1) ? "" : ";";
			//									params += itemConfig[i] + splitter;
			//								}
			//							}
			//							ItemObjectHolder object = new ItemObjectHolder().setStack(stack).setMeta(stack.getMetadata()).setParams(params);
			//							if ((object != null) && !object.getStack().isEmpty()) {
			//								map.put(stack.getItem().getRegistryName() + ":" + stack.getMetadata(), object);
			//							}
			//						}
			//					}
			//				}
		}
		return map;
	}

	public static boolean checkBlockInConfig(IBlockState state, String configBlock) {
		if ((state == null)) {
			return false;
		}
		final ResourceLocation regName = state.getBlock().getRegistryName();
		final String modID = regName.getNamespace();
		final String itemID = regName.getPath();
		final ItemObjectHolder configObject = parseConfigItem(configBlock);
		if (configObject == null) {
			return false;
		}
		final boolean modWildcard = configObject.getModID().equalsIgnoreCase("*");
		final boolean itemWildcard = configObject.getItemID().equalsIgnoreCase("*");
		if (modWildcard && itemWildcard) {
			return false;
		}
		final boolean metaMatches = (configObject.getMeta() == wildcard) || (state.getBlock().getMetaFromState(state) == configObject.getMeta());
		boolean isObjectCorrect = false;
		if (modWildcard || modID.equalsIgnoreCase(configObject.getModID())) {
			if (itemWildcard) {
				isObjectCorrect = true;
			} else if (!itemWildcard && configObject.getItemID().contains("*")) {
				final String regex = configObject.getItemID().replace("*", "");
				final boolean startWildcard = configObject.getItemID().startsWith("*");
				final boolean endWildcard = configObject.getItemID().endsWith("*");
				if (startWildcard && endWildcard) {
					if (itemID.contains(regex) && metaMatches) {
						isObjectCorrect = true;
					} else {
						isObjectCorrect = false;
					}
				} else if (endWildcard) {
					if (itemID.startsWith(regex) && metaMatches) {
						isObjectCorrect = true;
					} else {
						isObjectCorrect = false;
					}
				} else if (startWildcard) {
					if (itemID.endsWith(regex) && metaMatches) {
						isObjectCorrect = true;
					} else {
						isObjectCorrect = false;
					}
				} else {
					isObjectCorrect = false;
				}
			} else if (itemID.equalsIgnoreCase(configObject.getItemID())) {
				if (metaMatches) {
					isObjectCorrect = true;
				} else {
					isObjectCorrect = false;
				}
			} else {
				isObjectCorrect = false;
			}
		}
		return isObjectCorrect;
	}

	public static boolean checkItemInConfig(ItemStack stack, String configItemStack) {
		if ((stack == null) || stack.isEmpty()) {
			return false;
		}
		final ResourceLocation regName = stack.getItem().getRegistryName();
		final String modID = regName.getNamespace();
		final String itemID = regName.getPath();
		final ItemObjectHolder configObject = parseConfigItem(configItemStack);
		if (configObject == null) {
			return false;
		}
		final boolean modWildcard = configObject.getModID().equalsIgnoreCase("*");
		final boolean itemWildcard = configObject.getItemID().equalsIgnoreCase("*");
		if (modWildcard && itemWildcard) {
			return false;
		}
		final boolean metaMatches = (configObject.getMeta() == wildcard) || (stack.getItemDamage() == configObject.getMeta());
		boolean isObjectCorrect = false;
		if (modWildcard || modID.equalsIgnoreCase(configObject.getModID())) {
			if (itemWildcard) {
				isObjectCorrect = true;
			} else if (!itemWildcard && configObject.getItemID().contains("*")) {
				final String regex = configObject.getItemID().replace("*", "");
				final boolean startWildcard = configObject.getItemID().startsWith("*");
				final boolean endWildcard = configObject.getItemID().endsWith("*");
				if (startWildcard && endWildcard) {
					if (itemID.contains(regex) && metaMatches) {
						isObjectCorrect = true;
					} else {
						isObjectCorrect = false;
					}
				} else if (endWildcard) {
					if (itemID.startsWith(regex) && metaMatches) {
						isObjectCorrect = true;
					} else {
						isObjectCorrect = false;
					}
				} else if (startWildcard) {
					if (itemID.endsWith(regex) && metaMatches) {
						isObjectCorrect = true;
					} else {
						isObjectCorrect = false;
					}
				} else {
					isObjectCorrect = false;
				}
			} else if (itemID.equalsIgnoreCase(configObject.getItemID())) {
				if (metaMatches) {
					isObjectCorrect = true;
				} else {
					isObjectCorrect = false;
				}
			} else {
				isObjectCorrect = false;
			}
		}
		return isObjectCorrect;
	}

	public static ItemObjectHolder parseConfigItem(String configItem) {
		try {
			final String[] itemConfig = configItem.replace("|", ";").trim().split(";");
			String params = "";
			int meta = wildcard;
			final String itemString = CallHelper.getStringFromArray(itemConfig, 0);
			final String metaString = CallHelper.getStringFromArray(itemConfig, 1);
			if (!itemString.isEmpty()) {
				if (!metaString.isEmpty()) {
					if (metaString.contentEquals("*") || metaString.contentEquals(wildcard + "")) {
						meta = wildcard;
					} else {
						meta = Integer.parseInt(metaString);
					}
				}
				if (itemConfig.length > 2) {
					for (int i = 2; i < itemConfig.length; i++) {
						final String splitter = i == (itemConfig.length - 1) ? "" : ";";
						params += itemConfig[i] + splitter;
					}
				}
				return new ItemObjectHolder(itemString, meta, params.split(";"));
			} else {
				return null;
			}
		} catch (final Exception e) {
			Trinkets.log.error("Failed to get ConfigObject: " + configItem);
			return null;
		}
	}

	public static void initConfigLists() {
		magicRecoveryItems = parseConfigList(TrinketsConfig.SERVER.mana.recovery);
		//		dragonEyeBlocks = parseConfigList(TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.Blocks);
	}

	public static Map<String, ItemObjectHolder> getMagicRecoveryItems() {
		if ((magicRecoveryItems == null)) {
			magicRecoveryItems = parseConfigList(TrinketsConfig.SERVER.mana.recovery);
		}
		return magicRecoveryItems;
	}

	public static Map<String, ItemObjectHolder> getDragonsEyeBlocks() {
		if ((dragonEyeBlocks == null)) {
			dragonEyeBlocks = parseConfigList(TrinketsConfig.SERVER.mana.recovery);
		}
		return dragonEyeBlocks;
	}

	public static class ItemObjectHolder {
		private int meta;
		private String[] params;
		private String object;
		private String modID;
		private String itemID;

		public ItemObjectHolder() {
			meta = OreDictionary.WILDCARD_VALUE;
			params = new String[0];
		}

		public ItemObjectHolder(String object, int meta, String... params) {
			this.object = object;
			this.parseItem(object);
			this.meta = meta;
			this.params = params;
		}

		public String getObject() {
			return object;
		}

		public String getModID() {
			return modID;
		}

		public String getItemID() {
			return itemID;
		}

		public int getMeta() {
			return meta;
		}

		public String[] getParams() {
			return params;
		}

		public ItemObjectHolder setObject(String object) {
			this.object = object;
			return this;
		}

		public ItemObjectHolder setMeta(int meta) {
			this.meta = meta;
			return this;
		}

		public ItemObjectHolder setParams(String... params) {
			this.params = params;
			return this;
		}

		private void parseItem(String item) {
			try {
				if (item.isEmpty()) {
					modID = "minecraft";
					itemID = "air";
					return;
				} else {
					if (item.contains(":")) {
						final String[] vals = item.split(":");
						if (!vals[0].isEmpty()) {
							modID = vals[0];
						} else {
							modID = "minecraft";
						}
						if (!vals[1].isEmpty()) {
							itemID = vals[1];
						} else {
							itemID = "air";
						}
					} else {
						modID = "minecraft";
						itemID = item;
					}
				}
			} catch (final Exception e) {
			}
		}
	}

	public static AttributeHolder parseAttributeConfig(String attribute) {
		try {
			final String[] itemConfig = attribute.replace("|", ";").trim().split(";");
			final String attributeName = CallHelper.getStringFromArray(itemConfig, 0);
			final String attributeValue = CallHelper.getStringFromArray(itemConfig, 1);
			final String attributeOperation = CallHelper.getStringFromArray(itemConfig, 2);
			final String attributeSaved = CallHelper.getStringFromArray(itemConfig, 3);

			final String name = "";
			double amount = 0;
			int operation = 0;
			boolean isSaved = true;
			if (!attributeName.isEmpty()) {
				if (!attributeValue.isEmpty()) {
					final String value = attributeValue.replaceAll("[^.\\d]", "");
					if (!value.isEmpty()) {
						amount = Double.parseDouble(value);
					}
				}
				if (!attributeOperation.isEmpty()) {
					final String value = attributeOperation.replaceAll("[^\\d]", "");
					if (!value.isEmpty()) {
						operation = Integer.parseInt(value);
					} else {
						if (attributeOperation.equalsIgnoreCase("multiply")) {
							operation = 2;
						} else if (attributeOperation.equalsIgnoreCase("multiply_base")) {
							operation = 1;
						} else {

						}
					}
				}
				if (!attributeSaved.isEmpty()) {
					if (attributeSaved.equalsIgnoreCase("false")) {
						isSaved = false;
					}
				}

				if (operation < 0) {
					operation = 0;
				}
				if (operation > 2) {
					operation = 2;
				}
				return new AttributeHolder(name, amount, operation, isSaved);
			} else {
				return null;
			}
		} catch (final Exception e) {
			Trinkets.log.error("Failed to get ConfigObject: " + attribute);
			return null;
		}
	}

	public static class AttributeHolder {
		private String name;
		private double amount;
		private int operation;
		private boolean isSaved;

		public AttributeHolder(String name, double amount, int operation, boolean isSaved) {
			this.name = name;
			this.amount = amount;
			this.operation = operation;
			this.isSaved = isSaved;
		}
	}

}
