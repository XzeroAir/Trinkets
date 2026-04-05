package xzeroair.trinkets.traits.abilities.compat.firstaid;

import ichttt.mods.firstaid.api.damagesystem.AbstractPlayerDamageModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
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
import xzeroair.trinkets.compatibility.ModCompat;
import xzeroair.trinkets.enums.EnumRenderLocation;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.compat.firstaid.IFirstAidAbility;
import xzeroair.trinkets.util.config.abilities.external.firstaid.ConfigAbilityHardHead;
import xzeroair.trinkets.util.helpers.NBTHelper;
import xzeroair.trinkets.util.helpers.StringUtils;
import xzeroair.trinkets.util.helpers.TranslationHelper;

import javax.annotation.Nullable;

@Interface(modid = "firstaid", iface = "xzeroair.trinkets.util.compat.firstaid.IFirstAidAbility", striprefs = true)
public class AbilityHardHead extends Ability implements IFirstAidAbility {

    private final ConfigAbilityHardHead CONFIG;
    protected int CHANCE;

    public AbilityHardHead() {
        this(TrinketsConfig.SERVER.ABILITIES.EXTERNAL.HARD_HEAD);
    }

    public AbilityHardHead(ConfigAbilityHardHead config) {
        super(TrinketsRegistryNames.ModAbilities.FIRST_AID_HARD_HEAD);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
        this.CHANCE = config.CHANCE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        String langKey = this.getTranslationKey();
        if (renderID == EnumRenderLocation.GUI_BEFORE.getId()) {
            final String string = helper.getLangTranslation(ModCompat.ModNames.LANG_NAME_FIRST_AID);
            if (!helper.isStringEmpty(string)) {
                return (helper.gold + "(" + string + helper.gold + ")");
            }
        }
        final TranslationHelper.KeyEntry key1 = new TranslationHelper.LangEntry(langKey, "headshots", true);
        final TranslationHelper.KeyEntry key2 = new TranslationHelper.OptionEntry("headshotchance", this.CHANCE > 0, this.CHANCE < 1 ? 1 : MathHelper.clamp((1F / this.CHANCE) * 100, Integer.MIN_VALUE, Integer.MAX_VALUE) + "%");
        return helper.formatAddVariables(key, renderID, key1, key2);
    }

    @Override
    @Method(modid = "firstaid")
    public boolean firstAidHit(EntityLivingBase entity, DamageSource source, float undistributedDmg, AbstractPlayerDamageModel before, AbstractPlayerDamageModel after) {
        final int rand = this.CHANCE > 0 ? this.random.nextInt(this.CHANCE) : 0;
        String string = "Ouch!";
        if (TrinketsConfig.SERVER.MISC.VIPS) {
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
                final TextComponentString message = new TextComponentString(TextFormatting.BOLD + "" + TextFormatting.GOLD + string);
                StringUtils.sendMessageToPlayer(entity, message.getFormattedText(), true);
                after.HEAD.currentHealth = before.HEAD.currentHealth;
                after.scheduleResync();
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public NBTTagCompound sendAbilityData() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("CHANCE", this.CHANCE);
        return tag;
    }

    @Override
    public void loadDataCache(NBTTagCompound tag) {
        super.loadDataCache(tag);
        NBTHelper.hasInteger(tag, "CHANCE", (value) -> this.CHANCE = value);
    }
}
