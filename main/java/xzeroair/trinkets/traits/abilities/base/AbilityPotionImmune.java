package xzeroair.trinkets.traits.abilities.base;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class AbilityPotionImmune extends Ability implements IPotionAbility {

    public AbilityPotionImmune() {
    }

    private String[] potions;

    public AbilityPotionImmune(String... strings) {
        potions = strings;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        return super.addCustomDescriptionTags(helper, key, rendMod, renderID, compatID);
    }

    public AbilityPotionImmune setPotions(String... potions) {
        this.potions = potions;
        return this;
    }

    @Override
    public void onAbilityAdded(EntityLivingBase entity) {
        for (final String potion : potions) {
            final Potion pot = Potion.getPotionFromResourceLocation(potion);
            if (pot != null) {
                entity.removePotionEffect(pot);
            }
        }
    }

    @Override
    public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
        final String e = effect.getPotion().getRegistryName().toString();
        for (final String potion : potions) {
            if (e.contentEquals(potion)) {
                return true;
            }
        }
        return cancel;
    }

}
