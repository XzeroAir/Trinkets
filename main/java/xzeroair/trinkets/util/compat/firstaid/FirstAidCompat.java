package xzeroair.trinkets.util.compat.firstaid;

import ichttt.mods.firstaid.api.CapabilityExtendedHealthSystem;
import ichttt.mods.firstaid.api.damagesystem.AbstractPlayerDamageModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.network.NetworkHandler;

public class FirstAidCompat {

	public static void resetHP(EntityLivingBase entity) {
		if (entity instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) entity;
			final AbstractPlayerDamageModel aidCap = player.getCapability(CapabilityExtendedHealthSystem.INSTANCE, null);
			if (aidCap != null) {
				aidCap.tick(player.world, player);
				aidCap.HEAD.heal(2, player, false);
				aidCap.tick(player.world, player);
			}
		}
	}

	public static void sendHpPacket(EntityPlayer player, ItemStack stack) {
		if (player instanceof EntityPlayerMP) {
			NetworkHandler.INSTANCE.sendTo(new FirstAidSyncHPPacket(player, false, stack), (EntityPlayerMP) player);
		}
	}

	public static void resetHPPacket(EntityPlayer player, ItemStack stack) {
		if (player instanceof EntityPlayerMP) {
			NetworkHandler.INSTANCE.sendTo(new FirstAidSyncHPPacket(player, true, stack), (EntityPlayerMP) player);
		}
	}

}
