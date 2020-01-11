package xzeroair.trinkets.capabilities.ItemCap;

import net.minecraft.nbt.NBTTagCompound;

public class DefaultItemCapability implements IItemCap {

	int type = -1;
	int slot = -1;
	int hits = 0;
	int exp = 0;
	boolean abilityOn = false;
	boolean abilityOnAlt = false;

	public DefaultItemCapability(){

	}

	public DefaultItemCapability(int type, int slot, int hits, int exp, boolean ability, boolean altability) {
		this.type = type;
		this.slot = slot;
		this.hits = hits;
		this.exp = exp;
		this.abilityOn = ability;
		this.abilityOnAlt = altability;
	}

	@Override
	public boolean ability() {
		return this.abilityOn;
	}

	@Override
	public void setAbility(boolean ability) {
		if(this.abilityOn != ability) {
			this.abilityOn = ability;
		}
	}

	@Override
	public boolean altAbility() {
		return this.abilityOnAlt;
	}

	@Override
	public void setAltAbility(boolean altability) {
		if(this.abilityOnAlt != altability) {
			this.abilityOnAlt = altability;
		}
	}
	@Override
	public int oreTarget() {
		return this.type;
	}

	@Override
	public void setOreTarget(int type) {
		if (this.type != type) {
			this.type = type;
		}
	}

	@Override
	public int hitCount() {
		return this.hits;
	}

	@Override
	public void setHitCount(int hits) {
		if (this.hits != hits) {
			this.hits = hits;
		}
	}

	@Override
	public int storedExp() {
		return this.exp;
	}

	@Override
	public void setStoredExp(int exp) {
		if (this.exp != exp) {
			this.exp = exp;
		}
	}

	@Override
	public int wornSlot() {
		return this.slot;
	}

	@Override
	public void setWornSlot(int slot) {
		if (this.slot != slot) {
			this.slot = slot;
		}
	}

	@Override
	public NBTTagCompound saveNBT() {
		return (NBTTagCompound) ItemStorage.storage.writeNBT(ItemProvider.itemCapability, this, null);
	}

	@Override
	public void loadNBT(NBTTagCompound compound) {
		ItemStorage.storage.readNBT(ItemProvider.itemCapability, this, null, compound);
	}

}
