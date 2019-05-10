package xzeroair.trinkets.util.eventhandlers;

import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.helpers.TrinketHelper;

public class CombatHandler {

	@SubscribeEvent
	public void EnderTeleportEvent(EnderTeleportEvent event) {
		if((event.getEntity() instanceof EntityEnderman)) {
			final AxisAlignedBB bBox = event.getEntity().getEntityBoundingBox().grow(16, 4, 16);
			final List<EntityPlayer> entLivList = event.getEntity().getEntityWorld().getEntitiesWithinAABB(EntityPlayer.class, bBox);
			if(!entLivList.isEmpty()) {
				for(final EntityPlayer stuff : entLivList) {
					final EntityPlayer player = stuff;
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
				final EntityPlayer player = (EntityPlayer) event.getTarget();
				if(TrinketHelper.baubleCheck(player, ModItems.ender_tiara)) {
					((EntityLiving)event.getEntity()).setAttackTarget(null);;
				}
			}
		}
	}
}
