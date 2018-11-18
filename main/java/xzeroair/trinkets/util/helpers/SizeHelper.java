package xzeroair.trinkets.util.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import xzeroair.trinkets.compatibilities.sizeCap.ICap;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.handlers.ClimbHandler;
import xzeroair.trinkets.util.handlers.SizeHandler;

public class SizeHelper {

	public static void fairyRing(EntityPlayer player, int target, int size, ICap nbt) {

		if(size == target) {
			SizeHandler.setSize(player, nbt);
		}
		if((player.stepHeight != 0.25f) && ((TrinketsConfig.CLIENT.C01_Step_Height != false))){
			if(!player.isCreative()) {
				player.stepHeight = 0.25f;
			} else {
				player.stepHeight = 0.6f;
			}
		}

		if(!player.capabilities.isFlying) {
			if((ClimbHandler.canClimb(player) != false) && ((TrinketsConfig.CLIENT.C02_Climbing != false))){
				if(!player.onGround){
					if(player.collidedHorizontally){
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

		//		if((player.inventory.armorItemInSlot(2).getItem() instanceof ItemElytra) && !player.isCreative()){
		//			if(!player.isElytraFlying()){
		//				player.motionY += 0.02; // Check Fall Height Code in Rubber stone, Try to change fall speed based on height
		//			}
		//		}
	}

	public static void dwarfRing(EntityPlayer player, int target, int size, ICap nbt) {

		if(size == target) {
			SizeHandler.setSize(player, nbt);
		}

		Item heldItem = player.inventory.getCurrentItem().getItem();
		ItemStack heldItemStack = player.inventory.getCurrentItem();

		for(int i = 0; i < 2; i++) {
			String toolLevel = TrinketHelper.level(i);
			if(heldItem.getHarvestLevel(heldItemStack, toolLevel, player, null) == 0) {
				player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 1, 3, false, false));
			}
			if(heldItem.getHarvestLevel(heldItemStack, toolLevel, player, null) == 1) {
				player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 1, 2, false, false));
			}
			if(heldItem.getHarvestLevel(heldItemStack, toolLevel, player, null) == 2) {
				player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 1, 1, false, false));
			}
			if(heldItem.getHarvestLevel(heldItemStack, toolLevel, player, null) == 3) {
				player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 1, 0, false, false));
			}
		}
	}

	public static void mobSize(EntityLivingBase entity, ICap nbt) {
		SizeHandler.mobSize(entity, nbt);
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
					}
				}
			} else {
				if((player.eyeHeight != defaultEyeHeight)){
					if(size < (defaultSize)) {
						if(size < (defaultSize-1)) {
							player.eyeHeight = (float) ((float)(nbt.getSize()*defaultEyeHeight)/100);
						}
						if(size == (defaultSize-1)) {
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
				}
			}
		}
	}
}
