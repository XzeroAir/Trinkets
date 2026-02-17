package xzeroair.trinkets.traits.abilities.compat.firstaid;

import ichttt.mods.firstaid.api.damagesystem.AbstractPlayerDamageModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Vip.VipStatus;
import xzeroair.trinkets.enums.EnumRenderLocation;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.firstaid.IFirstAidAbility;
import xzeroair.trinkets.util.config.trinkets.ConfigDamageShield;
import xzeroair.trinkets.util.helpers.TranslationHelper;

@Interface(modid = "firstaid", iface = "xzeroair.trinkets.util.compat.firstaid.IFirstAidAbility", striprefs = true)
public class AbilityIgnoreHeadshot extends Ability implements IFirstAidAbility {

    protected static final ConfigDamageShield serverConfig = TrinketsConfig.SERVER.Items.DAMAGE_SHIELD;

    public AbilityIgnoreHeadshot() {
        super(Abilities.firstAidReflex);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        String langKey = getTranslationKey();
        if (renderID == EnumRenderLocation.GUI_BEFORE.getId()) {
            final String string = helper.getLangTranslation("itemGroup.firstaid");
            if (!helper.isStringEmpty(string)) {
                return (helper.gold + "(" + string + helper.gold + ")");
            }
        }
        final TranslationHelper.KeyEntry key1 = new TranslationHelper.LangEntry(langKey, "headshots", serverConfig.compat.firstaid.chance_ignore);
        final TranslationHelper.KeyEntry key2 = new TranslationHelper.OptionEntry("headshotchance", serverConfig.compat.firstaid.chance_ignore, MathHelper.clamp((1F / serverConfig.compat.firstaid.chance_headshots) * 100, Integer.MIN_VALUE, Integer.MAX_VALUE) + "%");
        return helper.formatAddVariables(key, renderID, key1, key2);
    }

    @Override
    @Method(modid = "firstaid")
    public boolean firstAidHit(EntityLivingBase entity, DamageSource source, float undistributedDmg, AbstractPlayerDamageModel before, AbstractPlayerDamageModel after) {
        if (TrinketsConfig.SERVER.Items.DAMAGE_SHIELD.damage_ignore) {
            final int rand = serverConfig.compat.firstaid.chance_headshots > 0 ? random.nextInt(serverConfig.compat.firstaid.chance_headshots) : 0;
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
            if ((after.HEAD.currentHealth < 1) && (after.BODY.currentHealth > 0)) {
                if (rand == 0) {
                    if (TrinketsConfig.SERVER.Items.DAMAGE_SHIELD.special && (entity instanceof EntityPlayer)) {
                        final TextComponentString message = new TextComponentString(TextFormatting.BOLD + "" + TextFormatting.GOLD + string);
                        ((EntityPlayer) entity).sendStatusMessage(message, true);
                    }
                    after.HEAD.currentHealth = before.HEAD.currentHealth;
                    after.scheduleResync();
                    return true;
                }
            }
        }
        return false;
    }

}
