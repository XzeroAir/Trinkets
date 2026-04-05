package xzeroair.trinkets.traits.abilities.compat.enhancedvisuals;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.enums.EnumRenderLocation;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.util.compat.enhancedvisuals.EnhancedVisualsCompat;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class AbilityEnhancedVisualsMod extends Ability {

    public AbilityEnhancedVisualsMod(String name) {
        super(name);
    }

    public AbilityEnhancedVisualsMod(String modID, String name) {
        super(modID, name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        if (renderID == EnumRenderLocation.GUI_BEFORE.getId()) {
            return (helper.gold + "(" + EnhancedVisualsCompat.getModName() + ")");
        }
        return super.addCustomDescriptionTags(helper, key, rendMod, renderID, compatID);
    }
}