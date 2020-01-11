package xzeroair.trinkets.util.eventhandlers;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.attributes.RaceAttribute.RaceAttribute;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.effects.EffectsFairyRing;
import xzeroair.trinkets.items.foods.Fairy_Food;
import xzeroair.trinkets.util.TrinketsConfig;

public class MovementHandler {

	@SubscribeEvent
	public void livingJump(LivingJumpEvent event){
		if(event.getEntityLiving().world.isRemote && (event.getEntityLiving() instanceof EntityPlayer)){
			final EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			if(player.getAttributeMap().getAttributeInstance(RaceAttribute.ENTITY_RACE) != null) {
				final AttributeModifier fairyFood = player.getAttributeMap().getAttributeInstance(RaceAttribute.ENTITY_RACE).getModifier(Fairy_Food.getUUID());
				final AttributeModifier fairyPotion = player.getAttributeMap().getAttributeInstance(RaceAttribute.ENTITY_RACE).getModifier(EffectsFairyRing.getUUID());
				if(((fairyFood != null) || (fairyPotion != null)) && !TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDwarfRing)) {
					if((TrinketsConfig.SERVER.FAIRY_RING.step_height != false)){
						player.motionY = player.motionY*0.5f;
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void livingFall(LivingFallEvent event) {
	}
}
