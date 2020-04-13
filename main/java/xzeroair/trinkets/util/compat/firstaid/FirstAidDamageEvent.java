package xzeroair.trinkets.util.compat.firstaid;

import java.util.List;
import java.util.Map;

import ichttt.mods.firstaid.api.CapabilityExtendedHealthSystem;
import ichttt.mods.firstaid.api.damagesystem.AbstractPlayerDamageModel;
import ichttt.mods.firstaid.api.event.FirstAidLivingDamageEvent;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.VIPHandler;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.TrinketsConfig;

public class FirstAidDamageEvent {

	@SubscribeEvent
	public void DamageEvent(FirstAidLivingDamageEvent event) {
		if (event.getEntityPlayer() instanceof EntityPlayer) {
			final EntityPlayer player = event.getEntityPlayer();
			String string = "Ouch!";
			if (VIPHandler.CheckPlayerVIPStatus(player.getUniqueID(), VIPHandler.getBro())) {
				string = "Zero Deaths!";
				if (!VIPHandler.getBro().isEmpty()) {
					Map<String, List<String>> map = VIPHandler.getBro().get(0);
					List<String> list = map.get(player.getUniqueID().toString());
					if (!list.isEmpty() && (list.size() >= 2)) {
						string = list.get(1);
					}
				}
			} else if (VIPHandler.CheckPlayerVIPStatus(player.getUniqueID(), VIPHandler.getPanda())) {
				string = "The Panda Queen calls me!";
				if (!VIPHandler.getPanda().isEmpty()) {
					Map<String, List<String>> map = VIPHandler.getPanda().get(0);
					List<String> list = map.get(player.getUniqueID().toString());
					if (!list.isEmpty() && (list.size() >= 2)) {
						string = list.get(1);
					}
				}
			} else if (VIPHandler.CheckPlayerVIPStatus(player.getUniqueID(), VIPHandler.getVIP())) {
				string = "Nani!";
				if (!VIPHandler.getVIP().isEmpty()) {
					Map<String, List<String>> map = VIPHandler.getVIP().get(0);
					List<String> list = map.get(player.getUniqueID().toString());
					if (!list.isEmpty() && (list.size() >= 2)) {
						string = list.get(1);
					}
				}
			}
			TextComponentString message = new TextComponentString(TextFormatting.BOLD + "" + TextFormatting.GOLD + string);
			if (event.getSource().getTrueSource() instanceof EntityLiving) {
				final AbstractPlayerDamageModel aidCap = player.getCapability(CapabilityExtendedHealthSystem.INSTANCE, null);
				if (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDamageShield)) {
					final ItemStack stack = TrinketHelper.getAccessory(player, ModItems.trinkets.TrinketDamageShield);
					TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
					if ((aidCap != null) && (iCap != null)) {
						if (TrinketsConfig.SERVER.DAMAGE_SHIELD.damage_ignore) {
							if ((event.getAfterDamage().HEAD.currentHealth < 1) || (event.getAfterDamage().BODY.currentHealth < 1)) {
								if (iCap.StoredExp() == 0) {
									event.setCanceled(true);
								}
								if (event.isCanceled()) {
									if (TrinketsConfig.SERVER.DAMAGE_SHIELD.special) {
										player.sendMessage(message);
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
