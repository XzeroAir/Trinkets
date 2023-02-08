package xzeroair.trinkets.traits;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.NonNullList;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation.ItemHandlerType;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.interfaces.IContainerAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IEquippedAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IHeldAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableInventoryAbility;

public class AbilityHandler {

	protected Map<String, AbilityHolder> active;
	protected boolean hasChanged = false;
	protected EntityLivingBase entity;

	public AbilityHandler(EntityLivingBase entity) {
		active = new TreeMap<>();
		this.entity = entity;
	}

	public Map<String, AbilityHolder> getActiveAbilities() {
		return active;
	}

	public void registerAbilities(String source, List<? extends IAbilityInterface> abilities) {
		for (IAbilityInterface ability : abilities) {
			this.registerAbility(source, new SlotInformation(ItemHandlerType.OTHER), ability);
		}
	}

	public void registerAbilities(String source, SlotInformation info, List<? extends IAbilityInterface> abilities) {
		for (IAbilityInterface ability : abilities) {
			this.registerAbility(source, info, ability);
		}
	}

	public void registerRaceAbility(String source, IAbilityInterface ability) {
		this.replaceAbility(source, new SlotInformation(ItemHandlerType.RACE), ability);
	}

	public void replaceAbility(String source, SlotInformation info, IAbilityInterface ability) {
		String key = ability.getRegistryName().toString();
		if (info == null) {
			info = new SlotInformation(ItemHandlerType.OTHER);
		}
		AbilityHolder value = active.get(key);
		if (value == null) {
			ability.onAbilityAdded(entity);
			AbilityHolder holder = new AbilityHolder(source, info, ability);
			active.put(key, holder);
			hasChanged = true;
		} else {
			boolean sameSource = value.getSourceID().contentEquals(source);
			boolean sameHandler = value.getInfo().getHandlerType() == info.getHandlerType();
			boolean sameSlot = value.getInfo().getSlot() == info.getSlot();
			if (!sameSource || !sameHandler || !sameSlot) {
				value.getAbility().onAbilityRemoved(entity);
				ability.onAbilityAdded(entity);
				AbilityHolder holder = new AbilityHolder(source, info, ability);
				active.put(key, holder);
				hasChanged = true;
			}
		}
	}

	@Nullable
	public IAbilityInterface registerAbility(String source, IAbilityInterface ability) {
		return this.registerAbility(source, new SlotInformation(ItemHandlerType.OTHER), ability);
	}

	@Nullable
	public IAbilityInterface registerAbility(String source, SlotInformation info, IAbilityInterface ability) {
		String key = ability.getRegistryName().toString();
		if (info == null) {
			info = new SlotInformation(ItemHandlerType.OTHER);
		}
		if (!active.containsKey(key)) {
			ability.onAbilityAdded(entity);
			AbilityHolder holder = new AbilityHolder(source, info, ability);
			active.put(key, holder);
			hasChanged = true;
			return null;
		}
		return ability;
	}

	@Nullable
	public IAbilityInterface removeAbility(String ability) {
		if (active.containsKey(ability)) {
			AbilityHolder oldHolder = active.remove(ability);
			IAbilityInterface oldAbility = oldHolder.getAbility();
			oldAbility.onAbilityRemoved(entity);
			hasChanged = true;
			return oldAbility;
		}
		return null;
	}

	@Nullable
	public AbilityHolder getAbilityHolder(String ability) {
		if (active.containsKey(ability)) {
			return active.get(ability);
		}
		return null;
	}

	@Nullable
	public IAbilityInterface getAbility(String ability) {
		AbilityHolder holder = this.getAbilityHolder(ability);
		if (holder != null) {
			return holder.getAbility();
		}
		return null;
	}

	public void updateAbilityHandler() {
		active.values().removeIf(cache -> cache.getAbility().shouldRemove());
		for (Entry<String, AbilityHolder> entry : active.entrySet()) {
			String key = entry.getKey();
			AbilityHolder cache = entry.getValue();
			String source = cache.getSourceID();
			SlotInformation sourceInfo = cache.getInfo();
			IAbilityInterface ability = cache.getAbility();
			boolean remove = this.shouldRemove(sourceInfo, source, entity);
			if (hasChanged) {
				NBTTagCompound data = Capabilities.getEntityProperties(entity, new NBTTagCompound(), (prop, rtn) -> prop.getTag());
				if (data.hasKey("Abilities")) {
					NBTTagCompound tag = data.getCompoundTag("Abilities");
					this.loadAbilityFromNBT(ability, tag);
				}
			}
			this.processAbility(ability, entity);
			if (remove || ability.shouldRemove()) {
				ability.scheduleRemoval();
				ability.onAbilityRemoved(entity);
				NBTTagCompound entityTag = Capabilities.getEntityProperties(entity, new NBTTagCompound(), (prop, rtn) -> prop.getTag());
				if (!entityTag.hasKey("Abilities")) {
					entityTag.setTag("Abilities", new NBTTagCompound());
				}
				NBTTagCompound abilitiesTag = entityTag.getCompoundTag("Abilities");
				this.saveAbilityToNBT(ability, abilitiesTag);
			}
		}
		if (hasChanged) {
			hasChanged = false;
		}
	}

	private void processAbility(IAbilityInterface ability, EntityLivingBase entity) {
		try {
			final AbilityHolder holder = this.getAbilityHolder(ability.getRegistryName().toString());
			final SlotInformation info = holder != null ? holder.getInfo() : null;
			if (ability instanceof ITickableAbility) {
				((ITickableAbility) ability).tickAbility(entity);
			}
			if (ability instanceof IEquippedAbility) {
				final IEquippedAbility a = ((IEquippedAbility) ability);
				a.head(entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD), entity);
				a.chest(entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST), entity);
				a.legs(entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS), entity);
				a.feet(entity.getItemStackFromSlot(EntityEquipmentSlot.FEET), entity);
			}
			if (ability instanceof IHeldAbility) {
				final IHeldAbility a = ((IHeldAbility) ability);
				a.heldMainHand(entity.getHeldItemMainhand(), entity);
				a.heldOffhand(entity.getHeldItemOffhand(), entity);
			}
			if (entity instanceof EntityPlayer) {
				final EntityPlayer player = (EntityPlayer) entity;
				if (ability instanceof IContainerAbility) {
					final IContainerAbility a = ((IContainerAbility) ability);
					a.inventoryContainer(player.inventoryContainer);
					a.openContainer(player.openContainer);
					a.playerInventory(player.inventory);
				}
				if (ability instanceof ITickableInventoryAbility) {
					final ITickableInventoryAbility a = ((ITickableInventoryAbility) ability);
					final int inHand = player.inventory.currentItem;
					if (info != null) {
						final ItemStack stack = info.getStackFromHandler(player);
						if (!stack.isEmpty()) {
							final int slot = info.getSlot();
							final boolean selected = info.getHandlerType().equals(ItemHandlerType.MAINHAND) && (inHand == slot);
							a.onUpdate(stack, player.world, player, slot, selected);
						} else {
							final NonNullList<ItemStack> inventory = player.inventory.mainInventory;
							for (int index = 0; index < inventory.size(); index++) {
								final ItemStack IStack = inventory.get(index);
								if (!IStack.isEmpty()) {
									a.onUpdate(IStack, player.world, player, index, inHand == index);
								}
							}
						}
					}
				}
			}
		} catch (final Exception e) {
			Trinkets.log.error("Error with ability:" + ability.getRegistryName().toString());
			e.printStackTrace();
		}
	}

	private boolean shouldRemove(SlotInformation info, String source, EntityLivingBase entity) {
		switch (info.getHandlerType()) {
		case NONE:
			return true;
		case OTHER:
			return false;
		case RACE:
			EntityRace race = Capabilities.getEntityProperties(entity, EntityRaces.none, (prop, rtn) -> prop.getCurrentRace());
			if (!race.isNone() && race.getRegistryName().toString().contentEquals(source)) {
				return false;
			}
			return true;
		//		case TRINKETS:
		//			final ITrinketContainerHandler TrinketHandler = getTrinketHandler(entity);
		//			return TrinketHandler != null ? getTrinketHandler(entity).getStackInSlot(this.getSlot()) : ItemStack.EMPTY;
		//		case BAUBLES:
		//			if (Trinkets.Baubles) {
		//				IItemHandler BaublesHandler = BaublesHelper.getBaublesHandler(entity);
		//				if (BaublesHandler != null)
		//					return BaublesHandler.getStackInSlot(this.getSlot());
		//			}
		//			return ItemStack.EMPTY;
		//		case HEAD:
		//			return entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
		//		case CHEST:
		//			return entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		//		case LEGS:
		//			return entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
		//		case FEET:
		//			return entity.getItemStackFromSlot(EntityEquipmentSlot.FEET);
		//		case OFFHAND:
		//			return entity.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
		//		case MAINHAND:
		//			return entity.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
		//		case HOTBAR:
		//			return (entity instanceof EntityPlayer) && InventoryPlayer.isHotbar(this.getSlot()) ? ((EntityPlayer) entity).inventory.getStackInSlot(this.getSlot()) : ItemStack.EMPTY;
		//		case INVENTORY:
		//			return (entity instanceof EntityPlayer) ? ((EntityPlayer) entity).inventory.getStackInSlot(this.getSlot()) : ItemStack.EMPTY;
		case POTION:
			Potion potion = Potion.getPotionFromResourceLocation(source);
			return potion == null ? true : !entity.isPotionActive(potion);
		default:
			ItemStack s = info.getStackFromHandler(entity);
			boolean remove = s.isEmpty() || !s.getItem().getRegistryName().toString().contentEquals(source);
			return remove;
		}
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

	public void copyFrom(AbilityHandler source, boolean wasDeath, boolean keepInv) {

		if (wasDeath) {

		} else {
			active = source.active;
		}
	}

	public NBTTagCompound saveAbilityToNBT(IAbilityInterface ability, NBTTagCompound compound) {
		String key = ability.getRegistryName().toString();
		if (!compound.hasKey(key)) {
			compound.setTag(key, new NBTTagCompound());
		}
		ability.saveStorage(compound.getCompoundTag(key));
		return compound;
	}

	public void loadAbilityFromNBT(IAbilityInterface ability, NBTTagCompound compound) {
		String key = ability.getRegistryName().toString();
		if (compound.hasKey(key)) {
			ability.loadStorage(compound.getCompoundTag(key));
		}
	}

	public NBTTagCompound saveAbilitiesToNBT(NBTTagCompound compound) {
		if (!compound.hasKey("Abilities")) {
			compound.setTag("Abilities", new NBTTagCompound());
		}
		NBTTagCompound abilityNBT = compound.getCompoundTag("Abilities");
		for (Entry<String, AbilityHolder> entry : active.entrySet()) {
			String key = entry.getKey();
			AbilityHolder value = entry.getValue();
			try {
				this.saveAbilityToNBT(value.getAbility(), abilityNBT);
			} catch (final Exception e) {
				Trinkets.log.error("Error when saving ability:" + key);
				e.printStackTrace();
			}
		}
		return compound;
	}

	public void loadAbilitiesFromNBT(NBTTagCompound compound) {
		if (compound.hasKey("Abilities")) {
			NBTTagCompound abilityNBT = compound.getCompoundTag("Abilities");
			for (Entry<String, AbilityHolder> entry : active.entrySet()) {
				String key = entry.getKey();
				AbilityHolder value = entry.getValue();
				try {
					this.loadAbilityFromNBT(value.getAbility(), abilityNBT);
				} catch (final Exception e) {
					Trinkets.log.error("Error when saving ability:" + key);
					e.printStackTrace();
				}
			}
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Call Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

	public class AbilityHolder {
		protected String source;
		protected SlotInformation info;
		protected IAbilityInterface ability;

		public AbilityHolder(String source, SlotInformation info, IAbilityInterface ability) {
			this.source = source;
			this.info = info;
			this.ability = ability.cacheAbilityHolder(this);
		}

		public final String getSourceID() {
			return source;
		}

		public final SlotInformation getInfo() {
			return info;
		}

		public final IAbilityInterface getAbility() {
			return ability;
		}
	}
}
