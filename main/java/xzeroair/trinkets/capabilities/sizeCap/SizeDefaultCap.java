package xzeroair.trinkets.capabilities.sizeCap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;

public class SizeDefaultCap implements ISizeCap {

	int size = 100;
	boolean transformed = false;
	String food = "none";
	int target = 100;
	float width;
	float height;
	float defaultWidth;
	float defaultHeight;

	public SizeDefaultCap(){

	}

	public SizeDefaultCap(int size, boolean transformed, String food, int target, float width, float height, float defaultWidth, float defaultHeight) {
		this.size = size;
		this.transformed = transformed;
		this.food = food;
		this.target = target;
		this.width = width;
		this.height = height;
		this.defaultWidth = defaultWidth;
		this.defaultHeight = defaultHeight;
	}

	@Override
	public boolean getTrans() {
		return this.transformed;
	}

	@Override
	public void setTrans(boolean transformed) {
		if(this.transformed != transformed) {
			this.transformed = transformed;
		}
	}
	@Override
	public String getFood() {
		return this.food;
	}

	@Override
	public void setFood(String food) {
		if(this.food != food) {
			this.food = food;
		}
	}
	@Override
	public int getSize() {
		return this.size;
	}

	@Override
	public void setSize(int size) {
		size = MathHelper.clamp(size, 20, 200);
		if (this.size != size) {
			this.size = size;
		}
	}
	@Override
	public int getTarget() {
		return this.target;
	}

	@Override
	public void setTarget(int target) {
		target = MathHelper.clamp(target, 20, 200);
		if (this.target != target) {
			this.target = target;
		}
	}

	@Override
	public float getWidth() {
		return this.width;
	}

	@Override
	public void setWidth(float width) {
		width = MathHelper.clamp(width, 0.1F, 1.2F);
		if(this.width != width){
			this.width = width;
		}
	}

	@Override
	public float getHeight() {
		return this.height;
	}

	@Override
	public void setHeight(float height) {
		height = MathHelper.clamp(height, 0.1F, 3.6F);
		if(this.height != height){
			this.height = height;
		}
	}

	@Override
	public float getDefaultWidth() {
		return this.defaultWidth;
	}

	@Override
	public void setDefaultWidth(float defaultWidth) {
		if(this.defaultWidth != defaultWidth){
			this.defaultWidth = defaultWidth;
		}
	}

	@Override
	public float getDefaultHeight() {
		return this.defaultHeight;
	}

	@Override
	public void setDefaultHeight(float defaultHeight) {
		if(this.defaultHeight != defaultHeight){
			this.defaultHeight = defaultHeight;
		}
	}

	@Override
	public NBTTagCompound saveNBT() {
		return (NBTTagCompound) SizeCapStorage.storage.writeNBT(SizeCapPro.sizeCapability, this, null);
	}

	@Override
	public void loadNBT(NBTTagCompound compound) {
		SizeCapStorage.storage.readNBT(SizeCapPro.sizeCapability, this, null, compound);
	}

	//	private static class Factory implements Callable<ICap> {
	//
	//		@Override
	//		public ICap call() throws Exception {
	//			return new DeCap();
	//		}
	//	}
}
