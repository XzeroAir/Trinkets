package xzeroair.trinkets.util.compat.firstaid;

import ichttt.mods.firstaid.api.CapabilityExtendedHealthSystem;
import ichttt.mods.firstaid.api.damagesystem.AbstractPlayerDamageModel;
import ichttt.mods.firstaid.api.event.FirstAidLivingDamageEvent;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.TrinketCap.TrinketProvider;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class FirstAidDamageEvent {

	@SubscribeEvent
	public void DamageEvent(FirstAidLivingDamageEvent event) {
		if(event.getEntityPlayer() instanceof EntityPlayer) {
			if(event.getSource().getTrueSource() instanceof EntityLiving) {
				final EntityPlayer player = event.getEntityPlayer();
				final AbstractPlayerDamageModel aidCap = player.getCapability(CapabilityExtendedHealthSystem.INSTANCE, null);
				if(TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDamageShield)) {
					final ItemStack stack = TrinketHelper.getAccessory(player, ModItems.trinkets.TrinketDamageShield);
					final IAccessoryInterface iCap = stack.getCapability(TrinketProvider.itemCapability, null);
					if((aidCap != null) && (iCap != null)) {
						if(TrinketsConfig.SERVER.DAMAGE_SHIELD.damage_ignore) {
							if((event.getAfterDamage().HEAD.currentHealth < 1) || (event.getAfterDamage().BODY.currentHealth < 1)) {
								if(iCap.storedExp() == 0) {
									event.setCanceled(true);
								}
								if(event.isCanceled()) {
									if(TrinketsConfig.SERVER.DAMAGE_SHIELD.special) {
										if(player.getUniqueID().toString().contentEquals("7f184d63-9f9c-47a7-be03-8382145fb2c2") || player.getUniqueID().toString().contentEquals("cdfccefb-1a2e-4fb8-a3b5-041da27fde61") || player.getUniqueID().toString().contentEquals("f5f28614-4e8b-4788-ae78-b020493dc5cb")) {
											player.sendMessage(new TextComponentString( TextFormatting.BOLD + "" + TextFormatting.GOLD + "Zero Deaths!"));
										}
									}
									aidCap.scheduleResync();
								}
							}
						}
						if(!TrinketsConfig.SERVER.DAMAGE_SHIELD.damage_ignore && TrinketsConfig.SERVER.DAMAGE_SHIELD.special) {
							if(player.getUniqueID().toString().contentEquals("7f184d63-9f9c-47a7-be03-8382145fb2c2")) {// || player.getUniqueID().toString().contentEquals("f5f28614-4e8b-4788-ae78-b020493dc5cb")) {
								if((event.getAfterDamage().HEAD.currentHealth < 1) || (event.getAfterDamage().BODY.currentHealth < 1)) {
									if(iCap.storedExp() == 0) {
										event.setCanceled(true);
									}
									if(event.isCanceled()) {
										player.sendMessage(new TextComponentString( TextFormatting.BOLD + "" + TextFormatting.GOLD + "Zero Deaths!"));
										aidCap.scheduleResync();
									}
								}
							}
						}
					}
				}
			}
		}
	}

}
