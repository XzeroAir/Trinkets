package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.traits.abilities.interfaces.IJumpAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.compat.lycanitesmobs.LycanitesCompat;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class AbilityWeightless extends Ability implements ITickableAbility, IPotionAbility, IJumpAbility {

    public AbilityWeightless() {
        super(Abilities.weightless);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        return super.addCustomDescriptionTags(helper, key, rendMod, renderID, compatID);
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        if (!entity.onGround) {
            entity.motionY = 0;
            if ((!(entity.isSneaking())) && entity.isSwingInProgress) {
                entity.motionY += 0.1;
            }
            if (entity.isSneaking() && entity.isSwingInProgress) {
                entity.motionY -= 0.1;
            }
        }
    }

    @Override
    public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
        final String e = effect.getPotion().getRegistryName().toString();
        final Potion weight = LycanitesCompat.getPotionEffectByName("weight");
        if ((weight != null) && e.contentEquals(weight.getRegistryName().toString())) return true;
        return cancel;
    }

    @Override
    public boolean fall(EntityLivingBase entity, float distance, float damage, boolean cancel) {
        return true;
    }

}
