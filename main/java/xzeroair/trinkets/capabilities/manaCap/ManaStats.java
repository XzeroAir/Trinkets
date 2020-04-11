package xzeroair.trinkets.capabilities.manaCap;

import net.minecraft.nbt.NBTTagCompound;

public class ManaStats {

	private float mana = 100.0F;

	public ManaStats() {

	}

	public float getMana() {
		return mana;
	}

	public boolean needMana() {
		return mana < 100.0F;
	}

	public void setMana(float mana) {
		this.mana = mana;
	}

	public void copyFrom(ManaStats source) {
		mana = source.mana;
	}

	public void savedNBTData(NBTTagCompound compound) {
		compound.setFloat("mana", mana);
	}

	public void loadNBTData(NBTTagCompound compound) {
		mana = compound.getFloat("mana");
	}

}
