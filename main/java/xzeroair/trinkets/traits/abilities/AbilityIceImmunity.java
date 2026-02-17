package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation.ItemHandlerType;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.init.ModPotionTypes;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigDragonsEye;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class AbilityIceImmunity extends Ability implements ITickableAbility, IPotionAbility, IAttackAbility {

    public static ConfigDragonsEye serverConfig = TrinketsConfig.SERVER.Items.DRAGON_EYE;
    Potion ice_resist;

    public AbilityIceImmunity() {
        super(Abilities.iceImmunity);
        ice_resist = ModPotionTypes.TrinketPotions.get(ModPotionTypes.iceResist);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        String langKey = getTranslationKey();
//        if (compatID == EnumModCompat.IceAndFire.getId()) {
//            final TranslationHelper.KeyEntry key3 = new TranslationHelper.OptionEntry("damagetype3", helper.getLangTranslation("tile.iceandfire.dragon_ice.name"));
//            return helper.formatAddVariables(key, key3);
//        } else if (compatID == EnumModCompat.Lycanites.getId()) {
//            final TranslationHelper.KeyEntry key4 = new TranslationHelper.OptionEntry("damagetype2", helper.getLangTranslation("tile.icefire.name"));
//            final TranslationHelper.KeyEntry key5 = new TranslationHelper.OptionEntry("damagetype1", helper.getLangTranslation("tile.ooze.name"));
//            return helper.formatAddVariables(key, key4, key5);
//        } else {
        final TranslationHelper.KeyEntry key1 = new TranslationHelper.OptionEntry("potion", new TextComponentTranslation("xat.effect." + Elements.ICE.getName().toLowerCase() + "_resistance").getFormattedText());
        final TranslationHelper.KeyEntry key2 = new TranslationHelper.LangEntry(langKey + ".compat.iaf.ice", "frostwalker", serverConfig.compat.iaf.ICE_VARIANT && serverConfig.compat.iaf.FROST_WALKER);
        return helper.formatAddVariables(key, renderID, key1, key2);
//        }
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        // Immune to slowness?
        final AbilityHolder holder = this.getAbilityHolder();
        if (holder.getInfo().getHandlerType() == ItemHandlerType.POTION) {
            return;
        }
        if (ice_resist != null) {
            if (!entity.isPotionActive(ice_resist)) {
                entity.addPotionEffect(new PotionEffect(ice_resist, 400, 0, false, false));
            }
            if (entity.world.isRemote) {
                if (entity.isPotionActive(ice_resist)) {
                    entity.getActivePotionEffect(ice_resist).setPotionDurationMax(true);
                }
            }
        }
    }

    @Override
    public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
        return cancel;
    }

    @Override
    public boolean attacked(EntityLivingBase attacked, DamageSource source, float dmg, boolean cancel) {
        if (source.damageType.contentEquals("ooze") || source.damageType.contentEquals("dragon_ice") || source.damageType.contentEquals("cold_fire")) {
            return true;
        }
        //icefireball
        return cancel;
    }

    @Override
    public void onAbilityRemoved(EntityLivingBase entity) {
        if (ice_resist != null) {
            entity.removePotionEffect(ice_resist);
        }
    }
}
