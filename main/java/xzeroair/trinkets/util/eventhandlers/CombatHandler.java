package xzeroair.trinkets.util.eventhandlers;

import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.compatibilities.sizeCap.CapPro;
import xzeroair.trinkets.compatibilities.sizeCap.ICap;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.network.MobCapDataMessage;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.util.helpers.TrinketHelper;

public class CombatHandler {

	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent event) {
	}
	@SubscribeEvent
	public void EnderTeleportEvent(EnderTeleportEvent event) {
		if((event.getEntity() instanceof EntityEnderman)) {
			AxisAlignedBB bBox = event.getEntity().getEntityBoundingBox().grow(12, 4, 12);
			List<EntityPlayer> entLivList = event.getEntity().getEntityWorld().getEntitiesWithinAABB(EntityPlayer.class, bBox);
			if(!entLivList.isEmpty()) {
				for(EntityPlayer stuff : entLivList) {
					EntityPlayer player = (EntityPlayer) stuff;
					if(TrinketHelper.baubleCheck(player, ModItems.ender_tiara)) {
						if(!event.getEntity().isInWater()) {
							event.setCanceled(true);
						}
					}
				}
			}
		}
	}
	@SubscribeEvent
	public void TargetEvent(LivingSetAttackTargetEvent event) {
		if((event.getEntity() instanceof EntityEnderman)) {
			if(event.getTarget() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) event.getTarget();
				if(TrinketHelper.baubleCheck(player, ModItems.ender_tiara)) {
					((EntityLiving)event.getEntity()).setAttackTarget(null);;
				}
			}
		}
	}
	@SubscribeEvent
	public void AttackEvent(AttackEntityEvent event) {
//		if(event.getEntityPlayer() != null) {
//			if(event.getEntityPlayer().inventory.getCurrentItem().getItem() == ModItems.glowing_ingot) {
//				ICap NBT = event.getEntityPlayer().getCapability(CapPro.sizeCapability, null);
//				if((event.getTarget() instanceof EntityLiving) && !(event.getTarget() instanceof EntityPlayer)) {
//					EntityLiving attacked = (EntityLiving) event.getTarget();
//					ICap mobNBT = attacked.getCapability(CapPro.sizeCapability, null);
////					if(mobNBT.getTrans() == false) {
////						mobNBT.setTarget(25);
////						mobNBT.setTrans(true);
////						if(attacked.world.isRemote) {
////							NetworkHandler.INSTANCE.sendToServer(new MobCapDataMessage(mobNBT.getSize(), mobNBT.getTrans(), mobNBT.getTarget(), attacked.width, attacked.height, attacked.width, attacked.height, attacked.getEntityId()));
////							NetworkHandler.INSTANCE.sendToAll(new MobCapDataMessage(mobNBT.getSize(), mobNBT.getTrans(), mobNBT.getTarget(), attacked.width, attacked.height, attacked.width, attacked.height, attacked.getEntityId()));
////						}
////					}
//				}
//			}
//		}
	}
	@SubscribeEvent
	public void LivingHurt(LivingHurtEvent event) {
//		if(event.getSource().getTrueSource() instanceof EntityPlayer) {
//			EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
//			if(player.inventory.getCurrentItem().getItem() == ModItems.glowing_ingot) {
//				event.setCanceled(true);
//			}
//		}
	}
	@SubscribeEvent
	public void LivingDrops(LivingDropsEvent event) {
	}

}
