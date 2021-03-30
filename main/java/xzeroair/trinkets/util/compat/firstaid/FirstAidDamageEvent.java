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
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.capabilities.Vip.VipStatus;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.TrinketsConfig;

public class FirstAidDamageEvent {

	@SubscribeEvent
	public void DamageEvent(FirstAidLivingDamageEvent event) {
		if (event.getEntityPlayer() instanceof EntityPlayer) {
			if (event.getSource().getTrueSource() instanceof EntityLiving) {
				final EntityPlayer player = event.getEntityPlayer();
				final AbstractPlayerDamageModel aidCap = player.getCapability(CapabilityExtendedHealthSystem.INSTANCE, null);
				if (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDamageShield)) {
					String string = "Ouch!";
					//TODO Fix VIP
					VipStatus vip = Capabilities.getVipStatus(player);
					if (vip != null) {
						String quote = vip.getRandomQuote();
						if (!quote.isEmpty()) {
							string = quote;
						}
					}
					TextComponentString message = new TextComponentString(TextFormatting.BOLD + "" + TextFormatting.GOLD + string);
					final ItemStack stack = TrinketHelper.getAccessory(player, ModItems.trinkets.TrinketDamageShield);
					TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
					if ((aidCap != null) && (iCap != null)) {
						if (TrinketsConfig.SERVER.Items.DAMAGE_SHIELD.damage_ignore) {
							if ((event.getAfterDamage().HEAD.currentHealth < 1) || (event.getAfterDamage().BODY.currentHealth < 1)) {
								if (iCap.StoredExp() == 0) {
									event.setCanceled(true);
								}
								if (event.isCanceled()) {
									if (TrinketsConfig.SERVER.Items.DAMAGE_SHIELD.special) {
										player.sendStatusMessage(message, false);
									}
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
