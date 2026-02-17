package xzeroair.trinkets.traits.abilities.compat.survival;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.enums.EnumRenderLocation;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.SurvivalCompat;
import xzeroair.trinkets.util.config.trinkets.ConfigDragonsEye;
import xzeroair.trinkets.util.helpers.TranslationHelper;

import java.util.List;

public class AbilityColdImmunity extends Ability implements ITickableAbility, IPotionAbility {

    public static ConfigDragonsEye serverConfig = TrinketsConfig.SERVER.Items.DRAGON_EYE;

    public AbilityColdImmunity() {
        super(Abilities.survivalColdImmunity);
    }


    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        if (renderID == EnumRenderLocation.GUI_BEFORE.getId()) {
            final boolean tanEnabled = (Trinkets.MOD_COMPAT.ToughAsNails && TrinketsConfig.getClientStore().MOD_COMPAT_TOUGHASNAILS);
            final boolean sdEnabled = (Trinkets.MOD_COMPAT.SimpleDifficulty && TrinketsConfig.getClientStore().MOD_COMPAT_SIMPLEDIFFICULTY);
            final boolean survival = tanEnabled || sdEnabled;
            final String modifier = sdEnabled ? "itemGroup.tabSimpleDifficulty" : tanEnabled ? "itemGroup.tabToughAsNails" : "";
            if (survival) {
                final String string = helper.getLangTranslation(modifier);
                if (!helper.isStringEmpty(string)) {
                    return (helper.gold + "(" + string + helper.gold + ")");
                }
            }
        }
        return super.addCustomDescriptionTags(helper, key, rendMod, renderID, compatID);
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        SurvivalCompat.immuneToCold(entity);
    }

    @Override
    public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
        final List<Potion> hypothermia = SurvivalCompat.getHypothermiaEffects();
        if (hypothermia.contains(effect.getPotion())) return true;
        return cancel;
    }

}
