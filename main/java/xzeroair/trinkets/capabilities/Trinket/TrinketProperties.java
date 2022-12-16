<<<<<<< Updated upstream
package xzeroair.trinkets.capabilities.Trinket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation.ItemHandlerType;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.CapabilityBase;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.SyncItemDataPacket;
import xzeroair.trinkets.traits.abilities.base.ItemAbilityProvider;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.interfaces.IHeldAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableInventoryAbility;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.handlers.Counter;

public class TrinketProperties extends CapabilityBase<TrinketProperties, ItemStack> {

	protected int variant;
	protected int exp;
	protected float mana;
	protected boolean mainAbility;
	protected boolean altAbility;
	protected SlotInformation slotInfo;
	protected boolean sync;
	protected String crafter;
	protected String crafterUUID;

	public TrinketProperties(ItemStack stack) {
		super(stack);
		variant = 0;
		exp = 0;
		mana = 0;
		mainAbility = false;
		altAbility = false;
		crafter = "";
		crafterUUID = "";
		slotInfo = new SlotInformation(stack, ItemHandlerType.NONE.getName(), -1);
	}

	@Override
	public NBTTagCompound getTag() {
		if (object.getTagCompound() == null) {
			final NBTTagCompound itemTag = new NBTTagCompound();
			this.saveToNBT(itemTag);
			object.setTagCompound(itemTag);
		}
		return object.getTagCompound();
	}

	public SlotInformation getSlotInfo() {
		return slotInfo;
	}

	public void itemRightClicked() {

	}

	public void itemLeftClicked() {

	}

	public void itemUsed() {

	}

	public void onCrafted(ItemStack stack, World world, EntityPlayer player) {
		if (player == null) {
			return;
		}
		try {
			if (player.getUniqueID() != null) {
				this.setCrafterUUID(player.getUniqueID().toString());
			}
			this.setCrafter(player.getDisplayNameString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getCrafter() {
		return crafter;
	}

	public String getCrafterUUID() {
		return crafterUUID;
	}

	public void setCrafter(String crafter) {
		if (!crafter.isEmpty() && !this.crafter.contentEquals(crafter)) {
			this.crafter = crafter;
			this.saveToNBT(this.getTag());
		}
	}

	public void setCrafterUUID(String crafterUUID) {
		if (!crafterUUID.isEmpty() && !this.crafterUUID.contentEquals(crafterUUID)) {
			this.crafterUUID = crafterUUID;
			this.saveToNBT(this.getTag());
		}
	}

	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (entity instanceof EntityLivingBase) {
			//TODO Recheck this, It's showing slot index 9 as Inventory instead of hotbar
			final boolean onHotBar = !isSelected && (entity instanceof EntityPlayer) && InventoryPlayer.isHotbar(itemSlot);
			final boolean moved = slotInfo.getSlot() != itemSlot;
			final boolean notSelected = slotInfo.getHandlerType().equals(ItemHandlerType.MAINHAND) && !isSelected;
			final boolean selected = !slotInfo.getHandlerType().equals(ItemHandlerType.MAINHAND) && isSelected;
			if (moved || notSelected || selected) {
				ItemHandlerType type = ItemHandlerType.INVENTORY;
				if (onHotBar && !isSelected) {
					type = ItemHandlerType.HOTBAR;
				}
				if ((itemSlot <= 3)) {
					SlotInformation hands = TrinketHelper.getSlotInfoForItemFromHeldEquipment(
							(EntityLivingBase) entity,
							s -> this.compareStacks(s, stack, false)
					);
					if (hands != null) {
						type = hands.getHandlerType();
					} else {
						SlotInformation armor = TrinketHelper.getSlotInfoForItemFromEquipment(
								(EntityLivingBase) entity,
								s -> this.compareStacks(s, stack, false)
						);
						if (armor != null) {
							type = armor.getHandlerType();
						}
					}
				}
				slotInfo.setHandler(type);
				slotInfo.setSlot(itemSlot);
			}
			if (stack.getItem() instanceof ItemAbilityProvider) {
				final ItemHandlerType cacheType = slotInfo.getHandlerType();
				List<IAbilityInterface> abilities = new ArrayList<>();
				ItemAbilityProvider provider = (ItemAbilityProvider) stack.getItem();
				provider.initAbilities(stack, (EntityLivingBase) entity, abilities);
				if (!abilities.isEmpty()) {
					List<IAbilityInterface> abilitiesToAdd = new ArrayList<>();
					for (IAbilityInterface attachedAbility : abilities) {
						if (((cacheType == ItemHandlerType.INVENTORY) || (cacheType == ItemHandlerType.HOTBAR)) && (attachedAbility instanceof ITickableInventoryAbility)) {
							abilitiesToAdd.add(attachedAbility);
						}
						if (((cacheType == ItemHandlerType.MAINHAND) || (cacheType == ItemHandlerType.OFFHAND)) && (attachedAbility instanceof IHeldAbility)) {
							abilitiesToAdd.add(attachedAbility);
						}
					}
					if (!abilitiesToAdd.isEmpty()) {
						Capabilities.getEntityProperties(entity, prop -> {
							prop.getAbilityHandler().registerAbilities(
									stack.getItem().getRegistryName().toString(),
									new SlotInformation(stack, cacheType, itemSlot),
									abilitiesToAdd
							);
						});
					}
				}
			}
			if (!tickHandler.getCounters().isEmpty()) {
				for (Entry<String, Counter> counter : tickHandler.getCounters().entrySet()) {
					counter.getValue().Tick();
				}
			}
		}
	}

	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {

	}

	public void onEntityTick(ItemStack stack, EntityLivingBase entity) {
		ItemStack logicCheck = slotInfo.getStackFromHandler(entity);
		if (!this.compareStacks(stack, logicCheck, true)) {
			SlotInformation info = TrinketHelper.getSlotInfoForItemFromAccessory(entity, s -> this.compareStacks(s, stack, false));
			if (info == null) {
				info = TrinketHelper.getSlotInfoForItemFromEquipment(entity, s -> this.compareStacks(s, stack, false));
				if (info == null) {
					info = TrinketHelper.getSlotInfoForItemFromHeldEquipment(entity, s -> this.compareStacks(s, stack, false));
				}
			}
			if (info != null) {
				final ItemHandlerType handler = info.getHandlerType();
				final int slot = info.getSlot();
				slotInfo.setHandler(handler);
				slotInfo.setSlot(slot);
			}
		}
		if (stack.getItem() instanceof ItemAbilityProvider) {
			List<IAbilityInterface> abilities = new ArrayList<>();
			ItemAbilityProvider provider = (ItemAbilityProvider) stack.getItem();
			provider.initAbilities(stack, entity, abilities);
			if (!abilities.isEmpty()) {
				Capabilities.getEntityProperties(entity, prop -> {
					prop.getAbilityHandler().registerAbilities(
							stack.getItem().getRegistryName().toString(),
							new SlotInformation(stack, slotInfo.getHandlerType(), slotInfo.getSlot()),
							abilities
					);
				});
				abilities.clear();
			}
		}
		//		}
		if (sync) {
			this.sendInformationToTracking(entity);
			sync = false;
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~Keybind handler~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private boolean compareStacks(ItemStack first, ItemStack second, boolean ignoreDurability) {
		if ((first == null) || (second == null)) {
			return false;
		}
		if ((first.isEmpty() && !second.isEmpty()) || (!first.isEmpty() && second.isEmpty())) {
			return false;
		}
		Item firstItem = first.getItem();
		Item secondItem = second.getItem();
		String firstItemID = firstItem.getRegistryName().toString();
		String secondItemID = secondItem.getRegistryName().toString();
		if (!firstItemID.contentEquals(secondItemID)) {
			return false;
		} else {
			if (first.isItemEqual(second)) {
				return true;
			} else if (first.isItemEqualIgnoreDurability(second) && ignoreDurability) {
				return true;
			} else {
				return false;
			}
		}
	}

	public void itemEquipped(ItemStack stack, EntityLivingBase entity) {
		final boolean isClient = entity.world.isRemote;
		if (!isClient) {
			final SlotInformation info = TrinketHelper.getSlotInfoForItemFromAccessory(entity, s -> s.isItemEqual(stack));
			if (info != null) {
				//				stack.setStackDisplayName("Equipped");
				final int slot = info.getSlot();
				final int handler = info.getHandlerType().getId();
				slotInfo.setHandler(info.getHandlerType());
				slotInfo.setSlot(slot);
				//			//		final VipStatus status = Capabilities.getVipStatus(e);
				//			//		if (status != null) {
				//			//			playerStatus = status.getStatus();
				final NBTTagCompound tag = new NBTTagCompound();
				this.saveToNBT(tag);
				if (entity instanceof EntityPlayer) {
					NetworkHandler.sendTo(
							new SyncItemDataPacket(
									entity,
									object,
									tag,
									slot,
									handler,
									handler == ItemHandlerType.BAUBLES.getId(),
									false
							), (EntityPlayerMP) entity
					);
				}
			}
		}
	}

	public void itemUnequipped(ItemStack stack, EntityLivingBase entity) {
		final boolean isClient = entity.world.isRemote;
		if (!isClient) {
			final int slot = slotInfo.getSlot();
			final int handler = slotInfo.getHandlerType().getId();
			//		System.out.println("We're unequipping");
			//		ItemStack sanityCheck = slotInfo.getStackFromHandler(entity);
			//		//		//		final SlotInformation info = TrinketHelper.getSlotInfoForItemFromAccessory(entity, s -> s.isItemEqual(stack));
			//		if (!sanityCheck.isEmpty() && sanityCheck.isItemEqual(stack)) {
			//			stack.setStackDisplayName("Unequipped");
			slotInfo.setHandler(ItemHandlerType.NONE);
			slotInfo.setSlot(-1);
			this.turnOff();
			tickHandler.clearCounters();
			final NBTTagCompound tag = new NBTTagCompound();
			this.saveToNBT(tag);
			if (entity instanceof EntityPlayer) {
				NetworkHandler.sendTo(
						new SyncItemDataPacket(
								entity,
								object,
								tag,
								slot,
								handler,
								handler == ItemHandlerType.BAUBLES.getId(),
								false
						), (EntityPlayerMP) entity
				);
			}
			//		}
		} else {

		}
	}

	public void turnOff() {
		this.toggleMainAbility(false);
		this.toggleAltAbility(false);
	}

	public boolean isEquipped() {
		return (slotInfo.getHandlerType().equals(ItemHandlerType.TRINKETS) || slotInfo.getHandlerType().equals(ItemHandlerType.BAUBLES)) && (slotInfo.getSlot() > -1);//ItemStack.areItemsEqual(object, this.getStackFromSlot(e, slot, handler));//TrinketHelper.AccessoryCheck(entity, stack.getItem());
	}

	//Handle Network stuff

	public void sendInformationToPlayer(EntityLivingBase e, EntityLivingBase receiver) {
		if (!e.getEntityWorld().isRemote && (receiver instanceof EntityPlayer)) {
			final NBTTagCompound tag = new NBTTagCompound();
			this.saveToNBT(tag);
			NetworkHandler.sendTo(new SyncItemDataPacket(e, object, tag, slotInfo.getSlot(), slotInfo.getHandlerType().getId()), (EntityPlayerMP) receiver);
		}
	}

	public void sendInformationToServer(EntityLivingBase e) {
		if (e.getEntityWorld().isRemote) {
			final NBTTagCompound tag = new NBTTagCompound();
			this.saveToNBT(tag);
			NetworkHandler.sendToServer(
					new SyncItemDataPacket(e, object, tag, slotInfo.getSlot(), slotInfo.getHandlerType().getId())
			);
		}
	}

	public void sendInformationToTracking(EntityLivingBase e) {
		final World world = e.getEntityWorld();
		if (!world.isRemote && (world instanceof WorldServer)) {
			final WorldServer w = (WorldServer) world;
			final NBTTagCompound tag = new NBTTagCompound();
			this.saveToNBT(tag);
			final int slot = slotInfo.getSlot();
			final int handler = slotInfo.getHandlerType().getId();
			NetworkHandler.sendToClients(
					w, e.getPosition(),
					new SyncItemDataPacket(
							e,
							object,
							tag,
							slot,
							handler,
							handler == ItemHandlerType.BAUBLES.getId(),
							this.isEquipped()
					)
			);
		}
	}

	public void scheduleResync() {
		sync = true;
	}

	//Handle Network stuff end.

	public boolean mainAbility() {
		return mainAbility;
	}

	public void toggleMainAbility(boolean bool) {
		if (mainAbility != bool) {
			mainAbility = bool;
			this.saveToNBT(this.getTag());
			//		this.getTag().setBoolean("main.ability", bool);
		}
	}

	public boolean altAbility() {
		return altAbility;
	}

	public void toggleAltAbility(boolean bool) {
		if (altAbility != bool) {
			altAbility = bool;
			this.saveToNBT(this.getTag());
		}
		//		this.getTag().setBoolean("alt.ability", bool);
	}

	public int StoredExp() {
		return exp;
	}

	public void setStoredExp(int integer) {
		if (exp != integer) {
			exp = integer;
			this.saveToNBT(this.getTag());
		}
	}

	public int getSlot() {
		return slotInfo.getSlot();
	}

	public float StoredMana() {
		return mana;
	}

	public void setStoredMana(float mana) {
		if (this.mana != mana) {
			this.mana = mana;
			this.saveToNBT(this.getTag());
		}
	}

	public final int getVariant() {
		return variant;
	}

	public final void setVariant(int variant) {
		if (this.variant != variant) {
			this.variant = variant;
			this.saveToNBT(this.getTag());
		}
	}

	/*
	 * int target = -1; int slot = -1; int count = 0; int exp = 0; boolean
	 * mainAbility = false; boolean altAbility = false;
	 */

	@Override
	public NBTTagCompound saveToNBT(NBTTagCompound compound) {
		final String capTag = Reference.MODID + ":Trinket";
		final NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("variant", variant);
		tag.setInteger("slot", slotInfo.getSlot());
		tag.setString("handler", slotInfo.getHandler());
		tag.setInteger("exp", exp);
		tag.setFloat("mana", mana);
		tag.setBoolean("main.ability", mainAbility);
		tag.setBoolean("alt.ability", altAbility);
		tag.setString("crafter.name", crafter);
		tag.setString("crafter.uuid", crafterUUID);
		try {
			tickHandler.saveCountersToNBT(tag);
		} catch (Exception e) {
			e.printStackTrace();
		}
		compound.setTag(capTag, tag);
		return compound;
	}

	@Override
	public void loadFromNBT(NBTTagCompound compound) {
		final String capTag = Reference.MODID + ":Trinket";
		if (compound.hasKey(capTag)) {
			final NBTTagCompound tag = compound.getCompoundTag(capTag);
			if (tag.hasKey("variant")) {
				variant = tag.getInteger("variant");
			}
			if (tag.hasKey("slot")) {
				slotInfo.setSlot(tag.getInteger("slot"));
			}
			if (tag.hasKey("handler")) {
				slotInfo.setHandler(tag.getString("handler"));
			}
			if (tag.hasKey("exp")) {
				exp = tag.getInteger("exp");
			}
			if (tag.hasKey("mana")) {
				mana = tag.getFloat("mana");
			}
			if (tag.hasKey("main.ability")) {
				mainAbility = tag.getBoolean("main.ability");
			}
			if (tag.hasKey("alt.ability")) {
				altAbility = tag.getBoolean("alt.ability");
			}
			if (tag.hasKey("crafter.name")) {
				crafter = (tag.getString("crafter.name"));
			}
			if (tag.hasKey("crafter.uuid")) {
				crafterUUID = (tag.getString("crafter.uuid"));
			}
			try {
				tickHandler.loadCountersFromNBT(tag);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
=======
package xzeroair.trinkets.capabilities.Trinket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation.ItemHandlerType;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.CapabilityBase;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.SyncItemDataPacket;
import xzeroair.trinkets.traits.abilities.base.ItemAbilityProvider;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.interfaces.IHeldAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableInventoryAbility;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.handlers.Counter;

public class TrinketProperties extends CapabilityBase<TrinketProperties, ItemStack> {

	protected int variant;
	protected int exp;
	protected float mana;
	protected boolean mainAbility;
	protected boolean altAbility;
	protected SlotInformation slotInfo;
	protected boolean sync;
	protected String crafter;
	protected String crafterUUID;

	public TrinketProperties(ItemStack stack) {
		super(stack);
		variant = 0;
		exp = 0;
		mana = 0;
		mainAbility = false;
		altAbility = false;
		crafter = "";
		crafterUUID = "";
		slotInfo = new SlotInformation(stack, ItemHandlerType.NONE.getName(), -1);
	}

	@Override
	public NBTTagCompound getTag() {
		if (object.getTagCompound() == null) {
			final NBTTagCompound itemTag = new NBTTagCompound();
			this.saveToNBT(itemTag);
			object.setTagCompound(itemTag);
		}
		return object.getTagCompound();
	}

	public SlotInformation getSlotInfo() {
		return slotInfo;
	}

	public void itemRightClicked() {

	}

	public void itemLeftClicked() {

	}

	public void itemUsed() {

	}

	public void onCrafted(ItemStack stack, World world, EntityPlayer player) {
		if (player == null) {
			return;
		}
		try {
			if (player.getUniqueID() != null) {
				this.setCrafterUUID(player.getUniqueID().toString());
			}
			this.setCrafter(player.getDisplayNameString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getCrafter() {
		return crafter;
	}

	public String getCrafterUUID() {
		return crafterUUID;
	}

	public void setCrafter(String crafter) {
		if (!crafter.isEmpty() && !this.crafter.contentEquals(crafter)) {
			this.crafter = crafter;
			this.saveToNBT(this.getTag());
		}
	}

	public void setCrafterUUID(String crafterUUID) {
		if (!crafterUUID.isEmpty() && !this.crafterUUID.contentEquals(crafterUUID)) {
			this.crafterUUID = crafterUUID;
			this.saveToNBT(this.getTag());
		}
	}

	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (entity instanceof EntityLivingBase) {
			//TODO Recheck this, It's showing slot index 9 as Inventory instead of hotbar
			final boolean onHotBar = !isSelected && (entity instanceof EntityPlayer) && InventoryPlayer.isHotbar(itemSlot);
			final boolean moved = slotInfo.getSlot() != itemSlot;
			final boolean notSelected = slotInfo.getHandlerType().equals(ItemHandlerType.MAINHAND) && !isSelected;
			final boolean selected = !slotInfo.getHandlerType().equals(ItemHandlerType.MAINHAND) && isSelected;
			if (moved || notSelected || selected) {
				ItemHandlerType type = ItemHandlerType.INVENTORY;
				if (onHotBar && !isSelected) {
					type = ItemHandlerType.HOTBAR;
				}
				if ((itemSlot <= 3)) {
					SlotInformation hands = TrinketHelper.getSlotInfoForItemFromHeldEquipment(
							(EntityLivingBase) entity,
							s -> this.compareStacks(s, stack, false)
					);
					if (hands != null) {
						type = hands.getHandlerType();
					} else {
						SlotInformation armor = TrinketHelper.getSlotInfoForItemFromEquipment(
								(EntityLivingBase) entity,
								s -> this.compareStacks(s, stack, false)
						);
						if (armor != null) {
							type = armor.getHandlerType();
						}
					}
				}
				slotInfo.setHandler(type);
				slotInfo.setSlot(itemSlot);
			}
			if (stack.getItem() instanceof ItemAbilityProvider) {
				final ItemHandlerType cacheType = slotInfo.getHandlerType();
				List<IAbilityInterface> abilities = new ArrayList<>();
				ItemAbilityProvider provider = (ItemAbilityProvider) stack.getItem();
				provider.initAbilities(stack, (EntityLivingBase) entity, abilities);
				if (!abilities.isEmpty()) {
					List<IAbilityInterface> abilitiesToAdd = new ArrayList<>();
					for (IAbilityInterface attachedAbility : abilities) {
						if (((cacheType == ItemHandlerType.INVENTORY) || (cacheType == ItemHandlerType.HOTBAR)) && (attachedAbility instanceof ITickableInventoryAbility)) {
							abilitiesToAdd.add(attachedAbility);
						}
						if (((cacheType == ItemHandlerType.MAINHAND) || (cacheType == ItemHandlerType.OFFHAND)) && (attachedAbility instanceof IHeldAbility)) {
							abilitiesToAdd.add(attachedAbility);
						}
					}
					if (!abilitiesToAdd.isEmpty()) {
						Capabilities.getEntityProperties(entity, prop -> {
							prop.getAbilityHandler().registerAbilities(
									stack.getItem().getRegistryName().toString(),
									new SlotInformation(stack, cacheType, itemSlot),
									abilitiesToAdd
							);
						});
					}
				}
			}
			if (!tickHandler.getCounters().isEmpty()) {
				for (Entry<String, Counter> counter : tickHandler.getCounters().entrySet()) {
					counter.getValue().Tick();
				}
			}
		}
	}

	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {

	}

	public void onEntityTick(ItemStack stack, EntityLivingBase entity) {
		ItemStack logicCheck = slotInfo.getStackFromHandler(entity);
		if (!this.compareStacks(stack, logicCheck, true)) {
			SlotInformation info = TrinketHelper.getSlotInfoForItemFromAccessory(entity, s -> this.compareStacks(s, stack, false));
			if (info == null) {
				info = TrinketHelper.getSlotInfoForItemFromEquipment(entity, s -> this.compareStacks(s, stack, false));
				if (info == null) {
					info = TrinketHelper.getSlotInfoForItemFromHeldEquipment(entity, s -> this.compareStacks(s, stack, false));
				}
			}
			if (info != null) {
				final ItemHandlerType handler = info.getHandlerType();
				final int slot = info.getSlot();
				slotInfo.setHandler(handler);
				slotInfo.setSlot(slot);
			}
		}
		if (stack.getItem() instanceof ItemAbilityProvider) {
			List<IAbilityInterface> abilities = new ArrayList<>();
			ItemAbilityProvider provider = (ItemAbilityProvider) stack.getItem();
			provider.initAbilities(stack, entity, abilities);
			if (!abilities.isEmpty()) {
				Capabilities.getEntityProperties(entity, prop -> {
					prop.getAbilityHandler().registerAbilities(
							stack.getItem().getRegistryName().toString(),
							new SlotInformation(stack, slotInfo.getHandlerType(), slotInfo.getSlot()),
							abilities
					);
				});
				abilities.clear();
			}
		}
		//		}
		if (sync) {
			this.sendInformationToTracking(entity);
			sync = false;
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~Keybind handler~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private boolean compareStacks(ItemStack first, ItemStack second, boolean ignoreDurability) {
		if ((first == null) || (second == null)) {
			return false;
		}
		if ((first.isEmpty() && !second.isEmpty()) || (!first.isEmpty() && second.isEmpty())) {
			return false;
		}
		Item firstItem = first.getItem();
		Item secondItem = second.getItem();
		String firstItemID = firstItem.getRegistryName().toString();
		String secondItemID = secondItem.getRegistryName().toString();
		if (!firstItemID.contentEquals(secondItemID)) {
			return false;
		} else {
			if (first.isItemEqual(second)) {
				return true;
			} else if (first.isItemEqualIgnoreDurability(second) && ignoreDurability) {
				return true;
			} else {
				return false;
			}
		}
	}

	public void itemEquipped(ItemStack stack, EntityLivingBase entity) {
		final boolean isClient = entity.world.isRemote;
		if (!isClient) {
			final SlotInformation info = TrinketHelper.getSlotInfoForItemFromAccessory(entity, s -> s.isItemEqual(stack));
			if (info != null) {
				//				stack.setStackDisplayName("Equipped");
				final int slot = info.getSlot();
				final int handler = info.getHandlerType().getId();
				slotInfo.setHandler(info.getHandlerType());
				slotInfo.setSlot(slot);
				//			//		final VipStatus status = Capabilities.getVipStatus(e);
				//			//		if (status != null) {
				//			//			playerStatus = status.getStatus();
				final NBTTagCompound tag = new NBTTagCompound();
				this.saveToNBT(tag);
				if (entity instanceof EntityPlayer) {
					NetworkHandler.sendTo(
							new SyncItemDataPacket(
									entity,
									object,
									tag,
									slot,
									handler,
									handler == ItemHandlerType.BAUBLES.getId(),
									false
							), (EntityPlayerMP) entity
					);
				}
			}
		}
	}

	public void itemUnequipped(ItemStack stack, EntityLivingBase entity) {
		final boolean isClient = entity.world.isRemote;
		if (!isClient) {
			final int slot = slotInfo.getSlot();
			final int handler = slotInfo.getHandlerType().getId();
			//		System.out.println("We're unequipping");
			//		ItemStack sanityCheck = slotInfo.getStackFromHandler(entity);
			//		//		//		final SlotInformation info = TrinketHelper.getSlotInfoForItemFromAccessory(entity, s -> s.isItemEqual(stack));
			//		if (!sanityCheck.isEmpty() && sanityCheck.isItemEqual(stack)) {
			//			stack.setStackDisplayName("Unequipped");
			slotInfo.setHandler(ItemHandlerType.NONE);
			slotInfo.setSlot(-1);
			this.turnOff();
			tickHandler.clearCounters();
			final NBTTagCompound tag = new NBTTagCompound();
			this.saveToNBT(tag);
			if (entity instanceof EntityPlayer) {
				NetworkHandler.sendTo(
						new SyncItemDataPacket(
								entity,
								object,
								tag,
								slot,
								handler,
								handler == ItemHandlerType.BAUBLES.getId(),
								false
						), (EntityPlayerMP) entity
				);
			}
			//		}
		} else {

		}
	}

	public void turnOff() {
		this.toggleMainAbility(false);
		this.toggleAltAbility(false);
	}

	public boolean isEquipped() {
		return (slotInfo.getHandlerType().equals(ItemHandlerType.TRINKETS) || slotInfo.getHandlerType().equals(ItemHandlerType.BAUBLES)) && (slotInfo.getSlot() > -1);//ItemStack.areItemsEqual(object, this.getStackFromSlot(e, slot, handler));//TrinketHelper.AccessoryCheck(entity, stack.getItem());
	}

	//Handle Network stuff

	public void sendInformationToPlayer(EntityLivingBase e, EntityLivingBase receiver) {
		if (!e.getEntityWorld().isRemote && (receiver instanceof EntityPlayer)) {
			final NBTTagCompound tag = new NBTTagCompound();
			this.saveToNBT(tag);
			NetworkHandler.sendTo(new SyncItemDataPacket(e, object, tag, slotInfo.getSlot(), slotInfo.getHandlerType().getId()), (EntityPlayerMP) receiver);
		}
	}

	public void sendInformationToServer(EntityLivingBase e) {
		if (e.getEntityWorld().isRemote) {
			final NBTTagCompound tag = new NBTTagCompound();
			this.saveToNBT(tag);
			NetworkHandler.sendToServer(
					new SyncItemDataPacket(e, object, tag, slotInfo.getSlot(), slotInfo.getHandlerType().getId())
			);
		}
	}

	public void sendInformationToTracking(EntityLivingBase e) {
		final World world = e.getEntityWorld();
		if (!world.isRemote && (world instanceof WorldServer)) {
			final WorldServer w = (WorldServer) world;
			final NBTTagCompound tag = new NBTTagCompound();
			this.saveToNBT(tag);
			final int slot = slotInfo.getSlot();
			final int handler = slotInfo.getHandlerType().getId();
			NetworkHandler.sendToClients(
					w, e.getPosition(),
					new SyncItemDataPacket(
							e,
							object,
							tag,
							slot,
							handler,
							handler == ItemHandlerType.BAUBLES.getId(),
							this.isEquipped()
					)
			);
		}
	}

	public void scheduleResync() {
		sync = true;
	}

	//Handle Network stuff end.

	public boolean mainAbility() {
		return mainAbility;
	}

	public void toggleMainAbility(boolean bool) {
		if (mainAbility != bool) {
			mainAbility = bool;
			this.saveToNBT(this.getTag());
			//		this.getTag().setBoolean("main.ability", bool);
		}
	}

	public boolean altAbility() {
		return altAbility;
	}

	public void toggleAltAbility(boolean bool) {
		if (altAbility != bool) {
			altAbility = bool;
			this.saveToNBT(this.getTag());
		}
		//		this.getTag().setBoolean("alt.ability", bool);
	}

	public int StoredExp() {
		return exp;
	}

	public void setStoredExp(int integer) {
		if (exp != integer) {
			exp = integer;
			this.saveToNBT(this.getTag());
		}
	}

	public int getSlot() {
		return slotInfo.getSlot();
	}

	public float StoredMana() {
		return mana;
	}

	public void setStoredMana(float mana) {
		if (this.mana != mana) {
			this.mana = mana;
			this.saveToNBT(this.getTag());
		}
	}

	public final int getVariant() {
		return variant;
	}

	public final void setVariant(int variant) {
		if (this.variant != variant) {
			this.variant = variant;
			this.saveToNBT(this.getTag());
		}
	}

	/*
	 * int target = -1; int slot = -1; int count = 0; int exp = 0; boolean
	 * mainAbility = false; boolean altAbility = false;
	 */

	@Override
	public NBTTagCompound saveToNBT(NBTTagCompound compound) {
		final String capTag = Reference.MODID + ":Trinket";
		final NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("variant", variant);
		tag.setInteger("slot", slotInfo.getSlot());
		tag.setString("handler", slotInfo.getHandler());
		tag.setInteger("exp", exp);
		tag.setFloat("mana", mana);
		tag.setBoolean("main.ability", mainAbility);
		tag.setBoolean("alt.ability", altAbility);
		tag.setString("crafter.name", crafter);
		tag.setString("crafter.uuid", crafterUUID);
		try {
			tickHandler.saveCountersToNBT(tag);
		} catch (Exception e) {
			e.printStackTrace();
		}
		compound.setTag(capTag, tag);
		return compound;
	}

	@Override
	public void loadFromNBT(NBTTagCompound compound) {
		final String capTag = Reference.MODID + ":Trinket";
		if (compound.hasKey(capTag)) {
			final NBTTagCompound tag = compound.getCompoundTag(capTag);
			if (tag.hasKey("variant")) {
				variant = tag.getInteger("variant");
			}
			if (tag.hasKey("slot")) {
				slotInfo.setSlot(tag.getInteger("slot"));
			}
			if (tag.hasKey("handler")) {
				slotInfo.setHandler(tag.getString("handler"));
			}
			if (tag.hasKey("exp")) {
				exp = tag.getInteger("exp");
			}
			if (tag.hasKey("mana")) {
				mana = tag.getFloat("mana");
			}
			if (tag.hasKey("main.ability")) {
				mainAbility = tag.getBoolean("main.ability");
			}
			if (tag.hasKey("alt.ability")) {
				altAbility = tag.getBoolean("alt.ability");
			}
			if (tag.hasKey("crafter.name")) {
				crafter = (tag.getString("crafter.name"));
			}
			if (tag.hasKey("crafter.uuid")) {
				crafterUUID = (tag.getString("crafter.uuid"));
			}
			try {
				tickHandler.loadCountersFromNBT(tag);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
>>>>>>> Stashed changes
}