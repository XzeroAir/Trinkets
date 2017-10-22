package xzeroair.trinkets.util.handlers;

import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class EventHandler {

	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent event) {
		
	//	if (event.getEntity() instanceof EntityPlayer) {
	//		
	//		EntityPlayer player = (EntityPlayer) event.getEntity();
	//		player.inventory.addItemStackToInventory(new ItemStack(Items.APPLE));
	//	}
		
	}
	@SubscribeEvent
	public void makeNoise(PlaySoundAtEntityEvent event) {

	}
	@SubscribeEvent
	public void LivingDrops(LivingDropsEvent event) {
		
		//if (event.getEntity() instanceof EntityDragon) {
		//	event.getDrops().add(new EntityItem(event.getEntity().world, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, new ItemStack(ModItems.dragons_eye)));
		//}
		
	}
	@SubscribeEvent
	public void LivingHurt(LivingHurtEvent event) {
		
	}
	@SubscribeEvent
	public void itemPickup(PlayerEvent.ItemPickupEvent event) {
		
	}
	@SubscribeEvent
	public void livingUpdate(LivingEvent.LivingUpdateEvent event) {

	}
	@SubscribeEvent
	public void AttackEvent(LivingSetAttackTargetEvent event) {

	}
	@SubscribeEvent
	public void itemToolTip(ItemTooltipEvent event) {
	
	}
	@SubscribeEvent
	public void AttackEvent(AttackEntityEvent event) {
//		int posA = event.getTarget().getPosition().getX();
//		int posB = event.getTarget().getPosition().getY();
//		int posC = event.getTarget().getPosition().getZ();
//		EntityLiving attacked = (EntityLiving) event.getTarget();
//			if(event.getTarget() instanceof EntityLiving)
//					if(event.getEntityPlayer().inventory.getCurrentItem().getItem() == ModItems.glowing_ingot) {
//						attacked.world.createExplosion(null, posA, posB, posC, 1.0f, true);
//						}
	}
	@SubscribeEvent
	public void breakEvent(BlockEvent.BreakEvent event) {
//		int posA = event.getPlayer().getPosition().getX();
//		int posB = event.getPlayer().getPosition().getY();
//		int posC = event.getPlayer().getPosition().getZ();
//		EntityPlayer player = (EntityPlayer) event.getPlayer();
//			if(event.getPlayer() instanceof EntityPlayer) {
//				if(player.inventory.getCurrentItem().getItem() == ModItems.glowing_ingot) {
//				player.world.createExplosion(null, posA, posB, posC, 1.0f, true);
//				}
//			}
		
		
	}
}

