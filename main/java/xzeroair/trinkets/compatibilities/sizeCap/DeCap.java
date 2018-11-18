package xzeroair.trinkets.compatibilities.sizeCap;

import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;

public class DeCap implements ICap {

	int size = 100;
	boolean transformed = false;
	int target = 100;
	float width;
	float height;
	float defaultWidth;
	float defaultHeight;

	public DeCap(){

	}

	public DeCap(int size, boolean transformed, int target, float width, float height, float defaultWidth, float defaultHeight) {
		this.size = size;
		this.transformed = transformed;
		this.target = target;
		this.width = width;
		this.height = height;
		this.defaultWidth = defaultWidth;
		this.defaultHeight = defaultHeight;
	}

	@Override
	public boolean getTrans() {
		return transformed;
	}

	@Override
	public void setTrans(boolean transformed) {
		if(this.transformed != transformed) {
			this.transformed = transformed;
		}
	}
	@Override
	public int getSize() {
		return size;
	}

	@Override
	public void setSize(int size) {
		size = MathHelper.clamp(size, 0, 100);
		if (this.size != size) {
			this.size = size;
		}
	}
	@Override
	public int getTarget() {
		return target;
	}

	@Override
	public void setTarget(int target) {
		target = MathHelper.clamp(target, 22, 100);
		if (this.target != target) {
			this.target = target;
		}
	}

	@Override
	public float getWidth() {
		return width;
	}

	@Override
	public void setWidth(float width) {
		width = MathHelper.clamp(width, 0.1F, 3.0F);
		if(this.width != width){
			this.width = width;
		}
	}

	@Override
	public float getHeight() {
		return height;
	}

	@Override
	public void setHeight(float height) {
		height = MathHelper.clamp(height, 0.1F, 3.0F);
		if(this.height != height){
			this.height = height;
		}
	}

	@Override
	public float getDefaultWidth() {
		return defaultWidth;
	}

	@Override
	public void setDefaultWidth(float defaultWidth) {
		if(this.defaultWidth != defaultWidth){
			this.defaultWidth = defaultWidth;
		}
	}

	@Override
	public float getDefaultHeight() {
		return defaultHeight;
	}

	@Override
	public void setDefaultHeight(float defaultHeight) {
		if(this.defaultHeight != defaultHeight){
			this.defaultHeight = defaultHeight;
		}
	}

	@Override
	public NBTTagCompound saveNBT() {
		return (NBTTagCompound) CapStorage.storage.writeNBT(CapPro.sizeCapability, this, null);
	}

	@Override
	public void loadNBT(NBTTagCompound compound) {
		CapStorage.storage.readNBT(CapPro.sizeCapability, this, null, compound);
	}

	//	private static class Factory implements Callable<ICap> {
	//
	//		@Override
	//		public ICap call() throws Exception {
	//			return new DeCap();
	//		}
	//	}
}
