package xzeroair.trinkets.capabilities.Trinket;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.CapabilityBase;
import xzeroair.trinkets.capabilities.Vip.VipStatus;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.SyncItemDataPacket;
import xzeroair.trinkets.util.handlers.TickHandler;

public class TrinketProperties extends CapabilityBase<TrinketProperties, ItemStack> {

	//	EntityLivingBase entity;
	int target = -1;
	int slot = -1;
	int count = 0;
	int exp = 0;
	int mana = 0;
	boolean mainAbility = false;
	boolean altAbility = false;
	int playerStatus = -1;
	int handler = 0;
	Map<String, TickHandler> Counters;

	public TrinketProperties(ItemStack stack) {
		super(stack);
		Counters = new HashMap<>();
	}

	@Override
	public NBTTagCompound getTag() {
		//		if (object.getTagCompound() != null) {
		//			return object.getTagCompound();
		//		}
		return super.getTag();
	}

	public void itemRightClicked() {

	}

	public void itemLeftClicked() {

	}

	public void itemUsed() {

	}

	@Override
	public void onUpdate() {
		//		if(!Counters.isEmpty()) {
		//			for(Entry<String, TickHandler> counter:Counters.entrySet()) {
		//				counter.getValue().Tick();
		//			}
		//		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~Keybind handler~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public void itemEquipped(EntityLivingBase e, int slot, int handler) {
		this.handler = handler;
		this.slot = slot;
		//		this.entity = entity;
		final VipStatus status = Capabilities.getVipStatus(e);
		if (status != null) {
			playerStatus = status.getStatus();
		}
		this.saveNBT();
		try {
			final World world = e.getEntityWorld();
			if (!world.isRemote && (world instanceof WorldServer)) {
				final WorldServer w = (WorldServer) world;
				NetworkHandler.sendToClients(
						w, e.getPosition(),
						new SyncItemDataPacket(e, object, object.getTagCompound(), slot, handler, true, true)
				);
			}
		} catch (final Exception e2) {
			e2.printStackTrace();
		}
	}

	public void itemUnequipped(EntityLivingBase e) {
		final ItemStack stack = this.getStackFromSlot(e, slot, handler);
		if (!stack.isEmpty() && stack.isItemEqual(object)) {
			return;
		}
		try {
			final World world = e.getEntityWorld();
			if (!world.isRemote && (world instanceof WorldServer)) {
				final WorldServer w = (WorldServer) world;
				NetworkHandler.sendToClients(
						w, e.getPosition(),
						new SyncItemDataPacket(e, object, object.getTagCompound(), slot, handler, true, false)
				);
			}
		} catch (final Exception e2) {
			e2.printStackTrace();
		}
		slot = -1;
		playerStatus = -1;
		handler = 0;
		this.saveNBT();
		this.clearCounters();
	}

	public void turnOff() {
		this.toggleMainAbility(false);
		this.toggleAltAbility(false);
		this.setTarget(-1);
	}

	public boolean isEquipped(EntityLivingBase e) {
		return ItemStack.areItemsEqual(object, this.getStackFromSlot(e, slot, handler));//TrinketHelper.AccessoryCheck(entity, stack.getItem());
	}

	public ItemStack getStackFromSlot(EntityLivingBase e, int slot, int handler) {
		return TrinketHelper.getItemStackFromSlot(e, slot, handler);
	}

	private void saveNBT() {
		final NBTTagCompound tag = this.getTagCompoundSafe(object);
		this.saveToNBT(tag);
	}

	//Handle Network stuff

	public void sendInformationToPlayer(EntityLivingBase e, EntityLivingBase receiver) {
		if (!e.getEntityWorld().isRemote && (receiver instanceof EntityPlayer)) {
			final NBTTagCompound tag = new NBTTagCompound();
			this.saveToNBT(tag);
			NetworkHandler.sendTo(new SyncItemDataPacket(e, object, tag, slot, handler), (EntityPlayerMP) receiver);
		}
	}

	public void sendInformationToServer(EntityLivingBase e) {
		if (e.getEntityWorld().isRemote) {
			NetworkHandler.sendToServer(
					new SyncItemDataPacket(e, object, object.getTagCompound(), slot, handler)
			);
		}
	}

	public void sendInformationToTracking(EntityLivingBase e) {
		final World world = e.getEntityWorld();
		if (!world.isRemote && (world instanceof WorldServer)) {
			final WorldServer w = (WorldServer) world;
			final NBTTagCompound tag = new NBTTagCompound();
			this.saveToNBT(tag);
			//			NetworkHandler.INSTANCE.sendToAllTracking(
			NetworkHandler.sendToClients(
					w, e.getPosition(),
					new SyncItemDataPacket(e, object, tag, slot, handler)
			);
		}
	}

	//Handle Network stuff end.

	public boolean mainAbility() {
		return mainAbility;
	}

	public void toggleMainAbility(boolean bool) {
		mainAbility = bool;
	}

	public boolean altAbility() {
		return altAbility;
	}

	public void toggleAltAbility(boolean bool) {
		altAbility = bool;
	}

	public int Target() {
		return target;
	}

	public void setTarget(int integer) {
		if (target != integer) {
			target = integer;
			this.saveNBT();
		}
	}

	public int Count() {
		return count;
	}

	public void setCount(int integer) {
		if (count != integer) {
			count = integer;
			this.saveNBT();
		}
	}

	public int StoredExp() {
		return exp;
	}

	public void setStoredExp(int integer) {
		if (exp != integer) {
			exp = integer;
			this.saveNBT();
		}
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int integer) {
		if (slot != integer) {
			slot = integer;
			this.saveNBT();
		}
	}

	public int getHandler() {
		return handler;
	}

	public void setHandler(int integer) {
		if (handler != integer) {
			handler = integer;
			this.saveNBT();
		}
	}

	public int StoredMana() {
		return mana;
	}

	public void setStoredMana(int integer) {
		if (mana != integer) {
			mana = integer;
			this.saveNBT();
		}
	}

	public NBTTagCompound getTagCompoundSafe(ItemStack stack) {
		NBTTagCompound tagCompound = stack.getTagCompound();
		if (tagCompound == null) {
			tagCompound = new NBTTagCompound();
			stack.setTagCompound(tagCompound);
		}
		return tagCompound;
	}

	/*
	 * int target = -1; int slot = -1; int count = 0; int exp = 0; boolean
	 * mainAbility = false; boolean altAbility = false;
	 */

	@Override
	public void saveToNBT(NBTTagCompound compound) {
		compound.setInteger("target", target);
		compound.setInteger("slot", slot);
		compound.setInteger("handler", handler);
		compound.setInteger("hitcount", count);
		compound.setInteger("exp", exp);
		compound.setInteger("mana", mana);
		compound.setBoolean("main.ability", mainAbility);
		compound.setBoolean("alt.ability", altAbility);
		compound.setInteger("player.status", playerStatus);
	}

	@Override
	public void loadFromNBT(NBTTagCompound compound) {
		if (compound.hasKey("target")) {
			target = compound.getInteger("target");
		}
		if (compound.hasKey("slot")) {
			slot = compound.getInteger("slot");
		}
		if (compound.hasKey("handler")) {
			handler = compound.getInteger("handler");
		}
		if (compound.hasKey("hitcount")) {
			count = compound.getInteger("hitcount");
		}
		if (compound.hasKey("exp")) {
			exp = compound.getInteger("exp");
		}
		if (compound.hasKey("mana")) {
			mana = compound.getInteger("mana");
		}
		if (compound.hasKey("main.ability")) {
			mainAbility = compound.getBoolean("main.ability");
		}
		if (compound.hasKey("alt.ability")) {
			altAbility = compound.getBoolean("alt.ability");
		}
		if (compound.hasKey("player.status")) {
			playerStatus = compound.getInteger("player.status");
		}
	}
}