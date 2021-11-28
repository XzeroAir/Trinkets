package xzeroair.trinkets.api;

import java.util.List;
import java.util.Map;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Loader;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.ITrinketContainerHandler;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.TrinketContainerProvider;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.traits.AbilityHandler;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.TrinketsConfig;

public class TrinketHelper {

	public static ITrinketContainerHandler getTrinketHandler(EntityPlayer player) {
		final ITrinketContainerHandler handler = player.getCapability(TrinketContainerProvider.containerCap, null);
		handler.setPlayer(player);
		return handler;
	}

	public static TrinketProperties getTrinketItemHandler(ItemStack stack) {
		final TrinketProperties handler = Capabilities.getTrinketProperties(stack);
		return handler;
	}

	public static boolean AccessoryCheck(EntityLivingBase player, Item item) {
		if (TrinketCheck(player, item)) {
			return true;
		} else if (baubleCheck(player, item)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean AccessoryCheck(EntityLivingBase player, List<Item> items) {
		boolean found = false;
		for (final Item item : items) {
			if (TrinketCheck(player, item)) {
				found = true;
			} else if (baubleCheck(player, item)) {
				found = true;
			}
		}
		return found;
	}

	private static boolean TrinketCheck(EntityLivingBase player, Item item) {
		if ((player instanceof EntityPlayer) && (item != null)) {
			final ITrinketContainerHandler Trinket = getTrinketHandler((EntityPlayer) player);
			if (Trinket != null) {
				for (int i = 0; i < Trinket.getSlots(); i++) {
					if (!Trinket.getStackInSlot(i).isEmpty() && (Trinket.getStackInSlot(i).getItem().getTranslationKey().contentEquals(item.getTranslationKey()))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private static boolean baubleCheck(EntityLivingBase player, Item item) {
		if (Loader.isModLoaded("baubles") && (player instanceof EntityPlayer) && (item != null)) {
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityPlayer) player);
			if (baubles != null) {
				for (int i = 0; i < baubles.getSlots(); i++) {
					if (!baubles.getStackInSlot(i).isEmpty() && (baubles.getStackInSlot(i).getItem().getTranslationKey().contentEquals(item.getTranslationKey()))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static ItemStack getAccessory(EntityPlayer player, Item item) {
		boolean skip = false;
		final ITrinketContainerHandler Trinket = getTrinketHandler(player);
		if (Trinket != null) {
			for (int i = 0; i < Trinket.getSlots(); i++) {
				if (!Trinket.getStackInSlot(i).isEmpty() && (Trinket.getStackInSlot(i).getItem().getTranslationKey().contentEquals(item.getTranslationKey()))) {
					skip = true;
					return Trinket.getStackInSlot(i);
				}
			}
		}
		if (Loader.isModLoaded("baubles") && (skip != true)) {
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			if (baubles != null) {
				for (int i = 0; i < baubles.getSlots(); i++) {
					if (!baubles.getStackInSlot(i).isEmpty() && (baubles.getStackInSlot(i).getItem().getTranslationKey().contentEquals(item.getTranslationKey()))) {
						return baubles.getStackInSlot(i);
					}
				}
			}
		}
		return ItemStack.EMPTY;
	}

	public static ItemStack getBaubleStack(EntityPlayer player, Item item) {
		if (Loader.isModLoaded("baubles")) {
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			if (baubles != null) {
				for (int i = 0; i < baubles.getSlots(); i++) {
					if (!baubles.getStackInSlot(i).isEmpty() && (baubles.getStackInSlot(i).getItem() == item)) {
						return baubles.getStackInSlot(i);
					}
				}
			}
		}
		return ItemStack.EMPTY;
	}

	public static ItemStack getItemStackFromSlot(EntityLivingBase player, int slot, int handler) {
		//TODO this is causing a crash from the Trinkets Container because the slot is somehow -1
		if ((player instanceof EntityPlayer) && (slot >= 0)) {
			if (handler == 1) {
				final ITrinketContainerHandler Trinket = getTrinketHandler((EntityPlayer) player);
				if (!Trinket.getStackInSlot(slot).isEmpty()) {
					return Trinket.getStackInSlot(slot);
				}
			} else if (Loader.isModLoaded("baubles") && (handler == 2)) {
				final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityPlayer) player);
				if (!baubles.getStackInSlot(slot).isEmpty()) {
					return baubles.getStackInSlot(slot);
				}
			}
		}
		return ItemStack.EMPTY;
	}

	public static NBTTagCompound getTagCompoundSafe(ItemStack stack) {
		NBTTagCompound tagCompound = stack.getTagCompound();
		if (tagCompound == null) {
			tagCompound = new NBTTagCompound();
			stack.setTagCompound(tagCompound);
		}
		return tagCompound;
	}

	public static Map<Integer, ItemStack> getEquippedBaubles(Map<Integer, ItemStack> map, EntityPlayer player) {
		if (Loader.isModLoaded("baubles") && (player != null)) {
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			if (baubles != null) {
				for (int i = 0; i < baubles.getSlots(); i++) {
					final ItemStack stack = baubles.getStackInSlot(i);
					final TrinketProperties prop = Capabilities.getTrinketProperties(stack);
					if (map.get(i) == null) {
						if (prop != null) {
							prop.itemEquipped(player, i, 2);
						}
						map.put(i, stack);
					} else {
						final ItemStack oldStack = map.get(i);
						if (!oldStack.isItemEqualIgnoreDurability(stack)) {
							final TrinketProperties oldProp = Capabilities.getTrinketProperties(oldStack);
							if (oldProp != null) {
								oldProp.itemUnequipped(player);
							}
							if (prop != null) {
								prop.itemEquipped(player, i, 2);
							}
							map.replace(i, stack);
						}
					}
				}
			}
		}
		return map;
	}

	public static Map<Integer, ItemStack> getEquippedTrinkets(Map<Integer, ItemStack> map, EntityPlayer player) {
		if (TrinketsConfig.SERVER.GUI.guiEnabled && (player != null)) {
			final ITrinketContainerHandler trinkets = getTrinketHandler(player);
			if (trinkets != null) {
				for (int i = 0; i < trinkets.getSlots(); i++) {
					final ItemStack stack = trinkets.getStackInSlot(i);
					final TrinketProperties prop = Capabilities.getTrinketProperties(stack);
					if (map.get(i) == null) {
						if (prop != null) {
							prop.itemEquipped(player, i, 2);
						}
						map.put(i, stack);
					} else {
						final ItemStack oldStack = map.get(i);
						if (!oldStack.isItemEqualIgnoreDurability(stack)) {
							final TrinketProperties oldProp = Capabilities.getTrinketProperties(oldStack);
							if (oldProp != null) {
								oldProp.itemUnequipped(player);
							}
							if (prop != null) {
								prop.itemEquipped(player, i, 2);
							}
							map.replace(i, stack);
						}
					}
				}
			}
		}
		return map;
	}

	public static Map<String, ItemStack> getEquippedList(Map<String, ItemStack> map, EntityPlayer player) {
		if (player == null) {
			return map;
		}
		String record = "";
		if (Loader.isModLoaded("baubles")) {
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			if (baubles != null) {
				for (int i = 0; i < baubles.getSlots(); i++) {
					record = "baubles:" + i;
					final ItemStack stack = baubles.getStackInSlot(i);
					if (map.get(record) == null) {
						map.put(record, stack);
					} else {
						final ItemStack oldStack = map.get(record);
						if (!oldStack.isItemEqualIgnoreDurability(stack)) {
							map.replace(record, stack);
						}
					}
				}
			}
		}
		final ITrinketContainerHandler trinkets = getTrinketHandler(player);
		if (trinkets != null) {
			for (int i = 0; i < trinkets.getSlots(); i++) {
				record = "trinkets:" + i;
				final ItemStack stack = trinkets.getStackInSlot(i);
				map.putIfAbsent(record, stack);
				if (!map.get(record).isItemEqualIgnoreDurability(stack)) {
					map.replace(record, stack);
				}
			}
		}
		return map;
	}

	public static boolean entityHasAbility(IAbilityInterface ability, EntityLivingBase entity) {
		if (entity.hasCapability(Capabilities.ENTITY_RACE, null)) {
			final EntityProperties properties = Capabilities.getEntityRace(entity);
			if (properties != null) {
				final AbilityHandler abilityHandler = properties.getAbilityHandler();
				if (abilityHandler != null) {
					return abilityHandler.hasAbility(ability);
				}
			}
		}
		return false;
	}

	//	final AxisAlignedBB bBox = entity.getEntityBoundingBox();
	//	List<String> cfg = Arrays.asList(serverConfig.repelledEntities);
	//	final Predicate<Entity> Targets = Predicates.and(
	//			EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE,
	//			ent -> (ent != null) && !(ent instanceof EntityPlayer) &&
	//					cfg.contains(EntityRegistry.getEntry(ent.getClass()).getRegistryName().toString())
	//	);
	//	final List<Entity> entityList = entity.world.getEntitiesWithinAABB(Entity.class, bBox.grow(1.1), Targets);
	//	for (final Entity repelledEntity : entityList) {

}
