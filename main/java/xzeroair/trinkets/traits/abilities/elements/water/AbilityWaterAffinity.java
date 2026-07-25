package xzeroair.trinkets.traits.abilities.elements.water;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.traits.abilities.interfaces.IMiningAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.ConstantsTextTranslations;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityAffinityWater;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class AbilityWaterAffinity extends Ability implements ITickableAbility, IMiningAbility {

    protected ConfigAbilityAffinityWater CONFIG;

    protected boolean UNDERWATER_MINING, VANILLA;
    protected int FULL_BUBBLES;

    public AbilityWaterAffinity() {
        this(TrinketsConfig.SERVER.ABILITIES.AFFINITY_WATER);
    }

    public AbilityWaterAffinity(ConfigAbilityAffinityWater config) {
        super(TrinketsRegistryNames.ModAbilities.AFFINITY_WATER);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
        this.FULL_BUBBLES = config.BUBBLES;
        this.UNDERWATER_MINING = config.UNDERWATER_MINING;
        this.VANILLA = config.VANILLA;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        String langKey = getTranslationKey();
        final TranslationHelper.KeyEntry key1 = new TranslationHelper.OptionEntry("bubbles", this.FULL_BUBBLES > 0 && !VANILLA, this.FULL_BUBBLES + "");
        final TranslationHelper.KeyEntry key2 = new TranslationHelper.OptionEntry("vanilla", VANILLA, "");
        final TranslationHelper.KeyEntry key3 = new TranslationHelper.OptionEntry("potion", VANILLA, ConstantsTextTranslations.TextMinecraft.MINECRAFT_WATER_BREATHING.getFormattedText());
        return helper.formatAddVariables(key, renderID, key1, key2, key3);
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        if (!entity.world.isRemote && CONFIG.BUBBLES > 0) {
            if (CONFIG.VANILLA || TrinketsConfig.getClientStore().MOD_COMPAT_BETTER_DIVING) {
                if (!((entity.ticksExisted % 59) == 0)) {
                    entity.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 60, 0, false, false));
                }
            } else {
                int air = entity.getAir();
                if (air < (FULL_BUBBLES * 30)) {
                    entity.setAir(FULL_BUBBLES * 30);
                }
            }
        }
    }

    @Override
    public void onAbilityRemoved(EntityLivingBase entity) {
        if (CONFIG.BUBBLES > 0) {
            if (CONFIG.VANILLA || TrinketsConfig.getClientStore().MOD_COMPAT_BETTER_DIVING) {
                if (entity.isPotionActive(MobEffects.WATER_BREATHING)) {
                    entity.removePotionEffect(MobEffects.WATER_BREATHING);
                }
            }
        }
    }

    @Override
    public float breakingBlock(EntityLivingBase entity, IBlockState state, BlockPos pos, float originalSpeed, float newSpeed) {
        if (UNDERWATER_MINING) {
            if (entity.isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier(entity)) {
                float speed = originalSpeed;
                speed *= 5F;
                if (!this.getAbilityHolder().getHandler().getParentProperties().isGrounded()) {
                    speed *= 5F;
                }
                if (newSpeed < speed) {
                    return speed;
                }
            }
        }
        return newSpeed;
    }

}
