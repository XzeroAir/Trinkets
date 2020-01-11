package xzeroair.trinkets.util.eventhandlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;
import net.minecraftforge.event.brewing.PotionBrewEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionExpiryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.attributes.RaceAttribute.RaceAttribute;
import xzeroair.trinkets.capabilities.sizeCap.ISizeCap;
import xzeroair.trinkets.capabilities.sizeCap.SizeCapPro;
import xzeroair.trinkets.init.ModPotionTypes;
import xzeroair.trinkets.items.effects.EffectsDwarfRing;
import xzeroair.trinkets.items.effects.EffectsFairyRing;

public class PotionEventHandlers {

	@SubscribeEvent
	public void PotionBrewPreEvent(PotionBrewEvent.Pre event) {
	}

	@SubscribeEvent
	public void PotionBrewPostEvent(PotionBrewEvent.Post event) {
	}

	@SubscribeEvent
	public void PotionAddedEvent(PotionAddedEvent event) {
		if((event.getEntityLiving() instanceof EntityPlayer) && (event.getPotionEffect() != null)) {
			final EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);
			if(event.getPotionEffect().getEffectName().contentEquals("effect.fairy")) {
				if(player.isPotionActive(ModPotionTypes.Dwarf)) {
					player.removeActivePotionEffect(ModPotionTypes.Dwarf);
					RaceAttribute.removeModifier(player, EffectsDwarfRing.getUUID());
					EffectsDwarfRing.DwarfEquip(null, player);
				}
			}
			if(event.getPotionEffect().getEffectName().contentEquals("effect.dwarf")) {
				if(player.isPotionActive(ModPotionTypes.Fairy)) {
					player.removeActivePotionEffect(ModPotionTypes.Fairy);
					RaceAttribute.removeModifier(player, EffectsFairyRing.getUUID());
					EffectsFairyRing.FairyEquip(null, player);
				}
			}
		}
	}

	@SubscribeEvent
	public void potionEndedEvent(PotionExpiryEvent event) {
	}

	@SubscribeEvent
	public void playerPotionBrewEvent(PlayerBrewedPotionEvent event) {
	}

}
