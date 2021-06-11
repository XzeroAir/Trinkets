package xzeroair.trinkets.capabilities.Trinket;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Vip.VipStatus;
import xzeroair.trinkets.network.ItemCapDataMessage;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.PacketAccessorySync;
import xzeroair.trinkets.util.handlers.DodgeHandler;
import xzeroair.trinkets.util.handlers.TickHandler;

public class TrinketProperties {

	ItemStack stack;
	EntityLivingBase entity;
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
	private DodgeHandler dodge;

	public TrinketProperties(ItemStack stack) {
		this.stack = stack;
		Counters = new HashMap<>();
	}

	public void itemRightClicked() {

	}

	public void itemLeftClicked() {

	}

	public void itemUsed() {

	}

	public void onUpdate() {
		//		if(!Counters.isEmpty()) {
		//			for(Entry<String, TickHandler> counter:Counters.entrySet()) {
		//				counter.getValue().Tick();
		//			}
		//		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~Keybind handler~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public DodgeHandler DodgeHandler(World world) {
		if ((dodge == null) && world.isRemote) {
			dodge = new DodgeHandler();
		}
		return dodge;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Counters~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public void addCounter(String key, int length) {
		if (!Counters.isEmpty() && !Counters.containsKey(key)) {
			Counters.put(key, new TickHandler(key, length));
		}
	}

	public void removeCounter(String key) {
		if (!Counters.isEmpty() && Counters.containsKey(key)) {
			Counters.remove(key);
		}
	}

	public void clearCounters() {
		if (!Counters.isEmpty()) {
			Counters.clear();
		}
	}

	public TickHandler getCounter(String key, int length, boolean isCountdown) {
		TickHandler value;
		if (!Counters.isEmpty() && Counters.containsKey(key)) {
			value = Counters.get(key);//.setLength(length).setCountdown(isCountdown);
		} else {
			value = new TickHandler(key, length, isCountdown);
			Counters.put(key, value);
		}
		return value;
	}

	public TickHandler getCounter(String key) {
		return this.getCounter(key, 20, false);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Counters End~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public void itemEquipped(EntityLivingBase entity, int slot, int handler) {
		this.handler = handler;
		this.slot = slot;
		this.entity = entity;
		VipStatus status = Capabilities.getVipStatus(entity);
		if (status != null) {
			playerStatus = status.getStatus();
		}
		this.saveNBT();
		this.sendInformationToServer(entity);
	}

	public void itemUnequipped() {
		if (!this.getStackFromSlot(slot, handler).isEmpty() && this.getStackFromSlot(slot, handler).isItemEqual(stack)) {
			return;
		}
		slot = -1;
		entity = null;
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

	public boolean isEquipped(EntityLivingBase entity) {
		return ItemStack.areItemsEqual(stack, this.getStackFromSlot(slot, handler));//TrinketHelper.AccessoryCheck(entity, stack.getItem());
	}

	public ItemStack getStackFromSlot(int slot, int handler) {
		return TrinketHelper.getItemStackFromSlot(entity, slot, handler);
	}

	private void saveNBT() {
		this.savedNBTData(this.getTagCompoundSafe(stack));
	}

	//Handle Network stuff
	public void sendInformationToAll(EntityLivingBase entity) {
		if (!entity.getEntityWorld().isRemote) {
			NetworkHandler.INSTANCE.sendToAll(new ItemCapDataMessage(entity, stack, this, slot, handler));
		}
	}

	public void sendInformationToPlayer(EntityLivingBase entity, EntityLivingBase receiver) {
		if (!entity.getEntityWorld().isRemote && (receiver instanceof EntityPlayer)) {
			NetworkHandler.INSTANCE.sendTo(new ItemCapDataMessage(entity, stack, this, slot, handler), (EntityPlayerMP) receiver);
		}
	}

	public void sendInformationToServer(EntityLivingBase entity) {
		if (entity.getEntityWorld().isRemote) {
			NetworkHandler.INSTANCE.sendToServer(new ItemCapDataMessage(entity, stack, this, slot, handler));
		}
	}

	public void sendInformationToTracking(EntityLivingBase entity) {
		if (!entity.getEntityWorld().isRemote) {
			NetworkHandler.INSTANCE.sendToAllTracking(new ItemCapDataMessage(entity, stack, this, slot, handler), entity);
		}
	}

	//	public void syncStackToAll(EntityLivingBase entity, boolean equipping) {
	//		if (!entity.getEntityWorld().isRemote) {
	//			if (entity instanceof EntityPlayer) {
	//				NBTTagCompound tag = this.getTagCompoundSafe(stack);
	//				this.savedNBTData(tag);
	//				NetworkHandler.INSTANCE.sendToAll(new PacketAccessorySync((EntityPlayer) entity, stack, slot, isTrinket, tag, equipping));
	//			}
	//		}
	//	}

	public void syncStackTo(EntityLivingBase target, int slot, boolean equipping, int handler, EntityLivingBase self) {
		if (!target.getEntityWorld().isRemote) {
			if ((self instanceof EntityPlayerMP) && (target instanceof EntityPlayer)) {
				//				if (!client) {
				//					NetworkHandler.INSTANCE.sendTo(new PacketAccessorySync(player, stack, i, true, null, !empty), (EntityPlayerMP) player);
				//				}
				//				boolean isTrinket = handler.equalsIgnoreCase("Trinket");
				//				String hand = ((IAccessoryInterface) stack.getItem()).getIsTrinketOrBauble(stack, target);
				//				boolean isTrinket = hand.equalsIgnoreCase(hand);

				NBTTagCompound tag = this.getTagCompoundSafe(stack);
				this.savedNBTData(tag);
				NetworkHandler.INSTANCE.sendTo(new PacketAccessorySync((EntityPlayer) target, stack, tag, slot, handler, equipping), (EntityPlayerMP) self);
			}
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

	public void savedNBTData(NBTTagCompound compound) {
		compound.setInteger("target", target);
		compound.setInteger("slot", slot);
		compound.setInteger("handler", handler);
		compound.setInteger("count", count);
		compound.setInteger("exp", exp);
		compound.setInteger("mana", mana);
		compound.setBoolean("main.ability", mainAbility);
		compound.setBoolean("alt.ability", altAbility);
		compound.setInteger("player.status", playerStatus);
	}

	public void loadNBTData(NBTTagCompound compound) {
		if (compound.hasKey("target")) {
			target = compound.getInteger("target");
		}
		if (compound.hasKey("slot")) {
			slot = compound.getInteger("slot");
		}
		if (compound.hasKey("handler")) {
			handler = compound.getInteger("handler");
		}
		if (compound.hasKey("count")) {
			count = compound.getInteger("count");
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