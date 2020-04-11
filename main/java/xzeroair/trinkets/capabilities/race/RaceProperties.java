package xzeroair.trinkets.capabilities.race;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;

public class RaceProperties {

	int size = 100;
	boolean transformed = false;
	String food = "none";
	int target = 100;
	float width;
	float height;
	float defaultWidth;
	float defaultHeight;

	public RaceProperties() {

	}

	public boolean getTrans() {
		return this.transformed;
	}

	public void setTrans(boolean transformed) {
		if (this.transformed != transformed) {
			this.transformed = transformed;
		}
	}

	public String getFood() {
		return this.food;
	}

	public void setFood(String food) {
		if (this.food != food) {
			this.food = food;
		}
	}

	public int getSize() {
		return this.size;
	}

	public void setSize(int size) {
		size = MathHelper.clamp(size, 20, 1000);
		if (this.size != size) {
			this.size = size;
		}
	}

	public int getTarget() {
		return this.target;
	}

	public void setTarget(int target) {
		target = MathHelper.clamp(target, 20, 1000);
		if (this.target != target) {
			this.target = target;
		}
	}

	public float getWidth() {
		return this.width;
	}

	public void setWidth(float width) {
		width = MathHelper.clamp(width, 0.1F, 1.8F);
		if (this.width != width) {
			this.width = width;
		}
	}

	public float getHeight() {
		return this.height;
	}

	public void setHeight(float height) {
		height = MathHelper.clamp(height, 0.1F, 5.4F);
		if (this.height != height) {
			this.height = height;
		}
	}

	public float getDefaultWidth() {
		return this.defaultWidth;
	}

	public void setDefaultWidth(float defaultWidth) {
		if (this.defaultWidth != defaultWidth) {
			this.defaultWidth = defaultWidth;
		}
	}

	public float getDefaultHeight() {
		return this.defaultHeight;
	}

	public void setDefaultHeight(float defaultHeight) {
		if (this.defaultHeight != defaultHeight) {
			this.defaultHeight = defaultHeight;
		}
	}

	public void copyFrom(RaceProperties source) {
		this.size = source.size;
		this.transformed = source.transformed;
		this.target = source.target;
		this.food = source.food;
		this.height = source.height;
		this.width = source.width;
		this.defaultHeight = source.defaultHeight;
		this.defaultWidth = source.defaultWidth;
	}

	public void savedNBTData(NBTTagCompound compound) {
		compound.setInteger("size", this.size);
		compound.setBoolean("transformed", this.transformed);
		compound.setInteger("target", this.target);
		compound.setString("food", this.food);
		compound.setFloat("height", this.height);
		compound.setFloat("width", this.width);
		compound.setFloat("default_height", this.defaultHeight);
		compound.setFloat("default_width", this.defaultWidth);
	}

	public void loadNBTData(NBTTagCompound compound) {
		if (compound.hasKey("size")) {
			this.size = compound.getInteger("size");
		}
		if (compound.hasKey("transformed")) {
			this.transformed = compound.getBoolean("transformed");
		}
		if (compound.hasKey("target")) {
			this.target = compound.getInteger("target");
		}
		if (compound.hasKey("food")) {
			this.food = compound.getString("food");
		}
		if (compound.hasKey("height")) {
			this.height = compound.getFloat("height");
		}
		if (compound.hasKey("width")) {
			this.width = compound.getFloat("width");
		}
		if (compound.hasKey("default_height")) {
			this.defaultHeight = compound.getFloat("default_height");
		}
		if (compound.hasKey("default_width")) {
			this.defaultWidth = compound.getFloat("default_width");
		}
	}

}
