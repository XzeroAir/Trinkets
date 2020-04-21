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
		return transformed;
	}

	public void setTrans(boolean transformed) {
		if (this.transformed != transformed) {
			this.transformed = transformed;
		}
	}

	public String getFood() {
		return food;
	}

	public void setFood(String food) {
		if (this.food != food) {
			this.food = food;
		}
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		size = MathHelper.clamp(size, 20, 1000);
		if (this.size != size) {
			this.size = size;
		}
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		target = MathHelper.clamp(target, 20, 1000);
		if (this.target != target) {
			this.target = target;
		}
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		width = MathHelper.clamp(width, 0.1F, 1.8F);
		if (this.width != width) {
			this.width = width;
		}
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		height = MathHelper.clamp(height, 0.1F, 5.4F);
		if (this.height != height) {
			this.height = height;
		}
	}

	public float getDefaultWidth() {
		return defaultWidth;
	}

	public void setDefaultWidth(float defaultWidth) {
		if (this.defaultWidth != defaultWidth) {
			this.defaultWidth = defaultWidth;
		}
	}

	public float getDefaultHeight() {
		return defaultHeight;
	}

	public void setDefaultHeight(float defaultHeight) {
		if (this.defaultHeight != defaultHeight) {
			this.defaultHeight = defaultHeight;
		}
	}

	public void copyFrom(RaceProperties source) {
		size = source.size;
		transformed = source.transformed;
		target = source.target;
		food = source.food;
		height = source.height;
		width = source.width;
		defaultHeight = source.defaultHeight;
		defaultWidth = source.defaultWidth;
	}

	public void savedNBTData(NBTTagCompound compound) {
		compound.setInteger("size", size);
		compound.setBoolean("transformed", transformed);
		compound.setInteger("target", target);
		compound.setString("food", food);
		compound.setFloat("height", height);
		compound.setFloat("width", width);
		compound.setFloat("default_height", defaultHeight);
		compound.setFloat("default_width", defaultWidth);
	}

	public void loadNBTData(NBTTagCompound compound) {
		if (compound.hasKey("size")) {
			size = compound.getInteger("size");
		}
		if (compound.hasKey("transformed")) {
			transformed = compound.getBoolean("transformed");
		}
		if (compound.hasKey("target")) {
			target = compound.getInteger("target");
		}
		if (compound.hasKey("food")) {
			food = compound.getString("food");
		}
		if (compound.hasKey("height")) {
			height = compound.getFloat("height");
		}
		if (compound.hasKey("width")) {
			width = compound.getFloat("width");
		}
		if (compound.hasKey("default_height")) {
			defaultHeight = compound.getFloat("default_height");
		}
		if (compound.hasKey("default_width")) {
			defaultWidth = compound.getFloat("default_width");
		}
	}

}
