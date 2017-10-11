package xzeroair.trinkets.examples;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FallDamage {
	
	@SubscribeEvent
    public void cancelPlayerFallDamage(LivingFallEvent event)
    {
		if (event.getEntity() instanceof EntityPlayer)
		{
        	EntityPlayer player = (EntityPlayer) event.getEntity();
 
        	if (player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == xzeroair.trinkets.init.ModItems.glowing_ingot)
        	{
            	event.setCanceled(true); //Sets canceled for ALL players if only one player is wearing the item. See my point?
        	}
	    }
    }

}
