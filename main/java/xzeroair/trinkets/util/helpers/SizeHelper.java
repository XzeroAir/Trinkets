package xzeroair.trinkets.util.helpers;

import net.minecraft.entity.player.EntityPlayer;
import xzeroair.trinkets.capabilities.sizeCap.ISizeCap;
import xzeroair.trinkets.util.TrinketsConfig;

public class SizeHelper {

	public static void eyeHeightHandler(EntityPlayer player, boolean trans, int target, int defaultSize, int size, ISizeCap nbt) {
		if((TrinketsConfig.CLIENT.cameraHeight != false)){
			final float defaultEyeHeight = player.getDefaultEyeHeight();
			if(trans == true) {
				if(size > (defaultSize/4)) {
					if(size < target) {
						player.eyeHeight = (nbt.getSize()*defaultEyeHeight)/100;
					} else {
						player.eyeHeight = (nbt.getSize()*defaultEyeHeight)/100;
					}
				} else {
					if(size < target) {
						player.eyeHeight = (float) (((nbt.getSize()*defaultEyeHeight)/100)*0.92);
					} else {
						player.eyeHeight = (float)(((nbt.getSize()*defaultEyeHeight)/100)*0.92);
					}
				}
				if(size == target) {
					if(size < (defaultSize/2)) {
						if(player.isRiding()) {
							player.eyeHeight = ((nbt.getSize()*defaultEyeHeight)/100)*2.5F;
						} else {
							if(!player.onGround) {
								player.eyeHeight = (float)(((nbt.getSize()*defaultEyeHeight)/100)*0.82);
							} else {
								player.eyeHeight = (float)(((nbt.getSize()*defaultEyeHeight)/100)*0.92);
							}
						}
					} else {
						player.eyeHeight = (float)(((nbt.getSize()*defaultEyeHeight)/100)*0.92);
					}
				}
			} else {
				if((player.eyeHeight != defaultEyeHeight)){
					if(size != (defaultSize)) {
						if(size < (defaultSize-1)) {
							player.eyeHeight = (nbt.getSize()*defaultEyeHeight)/100;
						}
						if(size > (defaultSize+1)) {
							player.eyeHeight = (nbt.getSize()*defaultEyeHeight)/100;
						}
						if((size == (defaultSize-1)) || (size == (defaultSize+1))) {
							player.eyeHeight = defaultEyeHeight;
						}
					}
				}
			}
		}
	}

	public static void sizeHandler(boolean trans, int target, int defaultSize, int size, ISizeCap nbt) {
		if(trans == true) {
			if(size != target) {
				if((size > target)) {
					nbt.setSize(size-1);
				}
				if((size < target)) {
					nbt.setSize(size+1);
				}
			}
		} else {
			if(size != defaultSize) {
				if((size < defaultSize)) {
					nbt.setSize(size+1);
				} else {
					nbt.setSize(size-1);
				}
			}
		}
	}
}
