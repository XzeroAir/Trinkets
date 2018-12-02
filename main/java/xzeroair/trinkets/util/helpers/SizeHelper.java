package xzeroair.trinkets.util.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import xzeroair.trinkets.compatibilities.sizeCap.ICap;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.handlers.ClimbHandler;
import xzeroair.trinkets.util.handlers.SizeHandler;

public class SizeHelper {

	public static void fairyRing(EntityPlayer player, ICap nbt) {

		if(nbt.getSize() == nbt.getTarget()) {
			SizeHandler.setSize(player, nbt);
		}
		if((player.stepHeight != 0.25f) && ((TrinketsConfig.SERVER.C01_Step_Height != false))){
			if(!player.isCreative()) {
				player.stepHeight = 0.25f;
			} else {
				player.stepHeight = 0.6f;
			}
		}

		if(!player.capabilities.isFlying) {
			if((ClimbHandler.canClimb(player) != false) && ((TrinketsConfig.SERVER.C02_Climbing != false))){
				if(!player.onGround){
					if(player.isCollidedHorizontally){
						if(!player.isSneaking()){
							player.motionY = 0.1f;
						}
						if(player.isSneaking()){
							player.motionY = 0f;
						}
					}
				}
			}
		}
		if(!player.isCreative() || player.capabilities.isFlying) {
			if(player.onGround) {
				player.motionX = player.motionX*0.75f;
				player.motionZ = player.motionZ*0.75f;
			}
		}
		if(player.isRiding()) {
			if(player.getRidingEntity() instanceof EntityBoat) {
				EntityBoat boat = (EntityBoat) player.getRidingEntity();
				if(boat.getControllingPassenger() == player) {
					player.dismountRidingEntity();
				}
			}
		}
	}

	public static void dwarfRing(EntityPlayer player, ICap nbt) {
		if(nbt.getSize() == nbt.getTarget()) {
			SizeHandler.setSize(player, nbt);
		}
	}

	public static void mobSize(EntityLivingBase entity, ICap nbt) {
//		if(nbt.getSize() == nbt.getTarget()) {
//			SizeHandler.mobSize(entity, nbt);
//		}
	}

	public static void eyeHeightHandler(EntityPlayer player, boolean trans, int target, int defaultSize, int size, ICap nbt) {
		if((TrinketsConfig.CLIENT.C00_Height != false)){
			float defaultEyeHeight = player.getDefaultEyeHeight();
			if(trans == true) {
				if(size > (defaultSize/4)) {
					if(size < target) {
						player.eyeHeight = (float) ((float)(nbt.getSize()*defaultEyeHeight)/100);
					} else {
						player.eyeHeight = (float)((float)(nbt.getSize()*defaultEyeHeight)/100);
					}
				} else {
					if(size < target) {
						player.eyeHeight = (float) (((float)(nbt.getSize()*defaultEyeHeight)/100)*0.92);
					} else {
						player.eyeHeight = (float)(((float)(nbt.getSize()*defaultEyeHeight)/100)*0.92);
					}
				}
				if(size == target) {
					if(size < (defaultSize/2)) {
						if(player.isRiding()) {
							player.eyeHeight = (float)(((float)(nbt.getSize()*defaultEyeHeight)/100)*2.5F);
						} else {
							if(!player.onGround) {
								player.eyeHeight = (float)(((float)(nbt.getSize()*defaultEyeHeight)/100)*0.82);
							} else {
								player.eyeHeight = (float)(((float)(nbt.getSize()*defaultEyeHeight)/100)*0.92);
							}
						}
					} else {
						player.eyeHeight = (float)(((float)(nbt.getSize()*defaultEyeHeight)/100)*0.92);
					}
				}
			} else {
				if((player.eyeHeight != defaultEyeHeight)){
					if(size != (defaultSize)) {
						if(size < (defaultSize-1)) {
							player.eyeHeight = (float) ((float)(nbt.getSize()*defaultEyeHeight)/100);
						}
						if(size > (defaultSize+1)) {
							player.eyeHeight = (float) ((float)(nbt.getSize()*defaultEyeHeight)/100);
						}
						if((size == (defaultSize-1)) || (size == (defaultSize+1))) {
							player.eyeHeight = defaultEyeHeight;
						}
					}
				}
			}
		}
	}

	public static void sizeHandler(Entity e, boolean trans, int target, int defaultSize, int size, ICap nbt) {
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
