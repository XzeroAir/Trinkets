package xzeroair.trinkets.traits.abilities.compat.firstaid;

import ichttt.mods.firstaid.api.damagesystem.AbstractPlayerDamageModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.Optional.Method;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Vip.VipStatus;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.firstaid.IFirstAidAbility;
import xzeroair.trinkets.util.config.trinkets.ConfigDamageShield;

@Interface(modid = "firstaid", iface = "xzeroair.trinkets.util.compat.firstaid.IFirstAidAbility", striprefs = true)
public class AbilityIgnoreHeadshot extends Ability implements IFirstAidAbility {

	protected static final ConfigDamageShield serverConfig = TrinketsConfig.SERVER.Items.DAMAGE_SHIELD;

	public AbilityIgnoreHeadshot() {
		super(Abilities.firstAidReflex);
	}

	@Override
	@Method(modid = "firstaid")
	public boolean firstAidHit(EntityLivingBase entity, DamageSource source, float undistributedDmg, AbstractPlayerDamageModel before, AbstractPlayerDamageModel after) {
		if (TrinketsConfig.SERVER.Items.DAMAGE_SHIELD.damage_ignore) {
			final int rand = random.nextInt(serverConfig.compat.firstaid.chance_headshots);
			String string = "Ouch!";
			if (TrinketsConfig.SERVER.misc.retrieveVIP) {
				final VipStatus vip = Capabilities.getVipStatus(entity);
				if (vip != null) {
					final String quote = vip.getRandomQuote();
					if (!quote.isEmpty()) {
						string = quote;
					}
				}
			}
			if ((after.HEAD.currentHealth < 1) || (after.BODY.currentHealth < 1)) {
				if (rand == 0) {
					if (TrinketsConfig.SERVER.Items.DAMAGE_SHIELD.special && (entity instanceof EntityPlayer)) {
						final TextComponentString message = new TextComponentString(TextFormatting.BOLD + "" + TextFormatting.GOLD + string);
						((EntityPlayer) entity).sendStatusMessage(message, true);
					}
					return true;
				}
			}
		}
		return false;
	}

}
