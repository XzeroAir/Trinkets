package xzeroair.trinkets.util.compat.firstaid;

import ichttt.mods.firstaid.api.CapabilityExtendedHealthSystem;
import ichttt.mods.firstaid.api.damagesystem.AbstractPlayerDamageModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.network.NetworkHandler;

public class FirstAidCompat {

	//	public static void setFairyHP(EntityLivingBase player) {
	//		final AbstractPlayerDamageModel aidCap = player.getCapability(CapabilityExtendedHealthSystem.INSTANCE, null);
	//		if(aidCap != null) {
	//			//			aidCap.HEAD.setAbsorption(0);
	//			//			aidCap.HEAD.setMaxHealth(6);
	//			//
	//			//			aidCap.BODY.setAbsorption(0);
	//			//			aidCap.BODY.setMaxHealth(4);
	//			//
	//			//			aidCap.LEFT_ARM.setAbsorption(0);
	//			//			aidCap.LEFT_ARM.setMaxHealth(2);
	//			//
	//			//			aidCap.LEFT_LEG.setAbsorption(0);
	//			//			aidCap.LEFT_LEG.setMaxHealth(1);
	//			//
	//			//			aidCap.LEFT_FOOT.setAbsorption(0);
	//			//			aidCap.LEFT_FOOT.setMaxHealth(1);
	//			//
	//			//			aidCap.RIGHT_ARM.setAbsorption(0);
	//			//			aidCap.RIGHT_ARM.setMaxHealth(2);
	//			//
	//			//			aidCap.RIGHT_LEG.setAbsorption(0);
	//			//			aidCap.RIGHT_LEG.setMaxHealth(1);
	//			//
	//			//			aidCap.RIGHT_FOOT.setAbsorption(0);
	//			//			aidCap.RIGHT_FOOT.setMaxHealth(1);
	//			//			aidCap.runScaleLogic(player);
	//			aidCap.tick(player.world, player);
	//			aidCap.HEAD.heal(2, player, false);
	//			//			aidCap.scheduleResync();
	//		}
	//	}

	//	public static void setDwarfHP(EntityLivingBase player) {
	//		final AbstractPlayerDamageModel aidCap = player.getCapability(CapabilityExtendedHealthSystem.INSTANCE, null);
	//		if(aidCap != null) {
	//			//			aidCap.HEAD.setMaxHealth(3);
	//			//			aidCap.BODY.setMaxHealth(3);
	//			//			aidCap.LEFT_ARM.setMaxHealth(4);
	//			//			aidCap.LEFT_LEG.setMaxHealth(4);
	//			//			aidCap.LEFT_FOOT.setMaxHealth(2);
	//			//			aidCap.RIGHT_ARM.setMaxHealth(4);
	//			//			aidCap.RIGHT_LEG.setMaxHealth(4);
	//			//			aidCap.RIGHT_FOOT.setMaxHealth(2);
	//			//			aidCap.runScaleLogic(player);
	//			aidCap.tick(player.world, player);
	//			aidCap.HEAD.heal(2, player, false);
	//			//			aidCap.scheduleResync();
	//		}
	//	}

	public static void resetHP(EntityLivingBase entity) {
		if(entity instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) entity;
			final AbstractPlayerDamageModel aidCap = player.getCapability(CapabilityExtendedHealthSystem.INSTANCE, null);
			if(aidCap != null) {
				aidCap.HEAD.heal(2, player, false);
				aidCap.tick(player.world, player);
				//				aidCap.applyMorphine(player);
			}
		}
	}

	public static void sendHpPacket(EntityPlayer player, ItemStack stack) {
		if(player instanceof EntityPlayerMP) {
			NetworkHandler.INSTANCE.sendTo(new FirstAidSyncHPPacket(player, false, stack), (EntityPlayerMP) player);
		}
	}

	public static void resetHPPacket(EntityPlayer player, ItemStack stack) {
		if(player instanceof EntityPlayerMP) {
			NetworkHandler.INSTANCE.sendTo(new FirstAidSyncHPPacket(player, true, stack), (EntityPlayerMP) player);
		}
	}

}
