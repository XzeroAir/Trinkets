package xzeroair.trinkets.capabilities.Trinket;

import net.minecraft.nbt.NBTTagCompound;

public class TrinketProperties {

	int target = -1;
	int slot = -1;
	int count = 0;
	int exp = 0;
	int mana = 0;
	boolean mainAbility = false;
	boolean altAbility = false;

	public TrinketProperties() {

	}

	public boolean mainAbility() {
		return this.mainAbility;
	}

	public void toggleMainAbility(boolean bool) {
		if (this.mainAbility != bool) {
			this.mainAbility = bool;
		}
	}

	public boolean altAbility() {
		return this.altAbility;
	}

	public void toggleAltAbility(boolean bool) {
		if (this.altAbility != bool) {
			this.altAbility = bool;
		}
	}

	public int Target() {
		return this.target;
	}

	public void setTarget(int integer) {
		if (this.target != integer) {
			this.target = integer;
		}
	}

	public int Count() {
		return this.count;
	}

	public void setCount(int integer) {
		if (this.count != integer) {
			this.count = integer;
		}
	}

	public int StoredExp() {
		return this.exp;
	}

	public void setStoredExp(int integer) {
		if (this.exp != integer) {
			this.exp = integer;
		}
	}

	public int Slot() {
		return this.slot;
	}

	public void setSlot(int integer) {
		if (this.slot != integer) {
			this.slot = integer;
		}
	}

	public int StoredMana() {
		return this.mana;
	}

	public void setStoredMana(int integer) {
		if (this.mana != integer) {
			this.mana = integer;
		}
	}

	/*
	 * int target = -1; int slot = -1; int count = 0; int exp = 0; boolean
	 * mainAbility = false; boolean altAbility = false;
	 */

	public void savedNBTData(NBTTagCompound compound) {
		compound.setInteger("target", this.target);
		compound.setInteger("slot", this.slot);
		compound.setInteger("count", this.count);
		compound.setInteger("exp", this.exp);
		compound.setInteger("mana", this.mana);
		compound.setBoolean("main.ability", this.mainAbility);
		compound.setBoolean("alt.ability", this.altAbility);
	}

	public void loadNBTData(NBTTagCompound compound) {
		if (compound.hasKey("target")) {
			this.target = compound.getInteger("target");
		}
		if (compound.hasKey("slot")) {
			this.slot = compound.getInteger("slot");
		}
		if (compound.hasKey("count")) {
			this.count = compound.getInteger("count");
		}
		if (compound.hasKey("exp")) {
			this.exp = compound.getInteger("exp");
		}
		if (compound.hasKey("mana")) {
			this.mana = compound.getInteger("mana");
		}
		if (compound.hasKey("main.ability")) {
			this.mainAbility = compound.getBoolean("main.ability");
		}
		if (compound.hasKey("alt.ability")) {
			this.altAbility = compound.getBoolean("alt.ability");
		}
	}

}