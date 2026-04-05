package xzeroair.trinkets.traits.abilities.compat.survival;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.enums.EnumRenderLocation;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.util.compat.SurvivalCompat;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class AbilitySurvivalMod extends Ability {

    public AbilitySurvivalMod(String name) {
        super(name);
    }

    public AbilitySurvivalMod(String modID, String name) {
        super(modID, name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        if (renderID == EnumRenderLocation.GUI_BEFORE.getId()) {
            final String string = helper.getLangTranslation(SurvivalCompat.getSurvivalMod());
            if (!helper.isStringEmpty(string)) {
                return (helper.gold + "(" + string + helper.gold + ")");
            }
        }
        return super.addCustomDescriptionTags(helper, key, rendMod, renderID, compatID);
    }

}
