package xzeroair.trinkets.items.effects;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import scala.util.Random;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.capabilities.Vip.VipStatus;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigDamageShield;

public class EffectsDamageShield {

	private static final ConfigDamageShield serverConfig = TrinketsConfig.SERVER.Items.DAMAGE_SHIELD;

	public static void eventPlayerHurt(LivingHurtEvent event, ItemStack stack, EntityLivingBase player) {
		if (event.getSource().canHarmInCreative()) {
			return;
		}
		String string = "Ow!";
		//TODO Fix VIP
		VipStatus vip = Capabilities.getVipStatus(player);
		if (vip != null) {
			String quote = vip.getRandomQuote();
			if (!quote.isEmpty()) {
				string = quote;
			}
		}
		TextComponentString message = new TextComponentString(TextFormatting.BOLD + "" + TextFormatting.GOLD + string);
		TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
		int rChance = new Random().nextInt(serverConfig.compat.firstaid.chance_headshots);
		if (serverConfig.damage_ignore) {
			if (serverConfig.compat.firstaid.chance_ignore) {
				iCap.setStoredExp(rChance);
			}
			if (iCap.Count() < serverConfig.hits) {
				if (event.getAmount() > 1) {
					iCap.setCount(iCap.Count() + 1);
				}
			}
			if (iCap.Count() >= serverConfig.hits) {
				iCap.setCount(0);
				if (serverConfig.special && (player instanceof EntityPlayer)) {
					((EntityPlayer) player).sendStatusMessage(message, false);
				}
				event.setAmount(0);
			}
		}
		if (serverConfig.explosion_resist) {
			if (event.getSource().isExplosion()) {
				if (event.getAmount() > 1f) {
					event.setAmount(event.getAmount() * serverConfig.explosion_amount);
				}
			}
		}
	}
}
