package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.traits.abilities.interfaces.IKeyBindInterface;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IToggleAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityNightVision;
import xzeroair.trinkets.util.helpers.TranslationHelper;

import javax.annotation.Nonnull;

public class AbilityNightVision extends Ability implements ITickableAbility, IToggleAbility, IKeyBindInterface {

    protected final ConfigAbilityNightVision CONFIG;

    protected boolean toggled;
    protected int mode;

    public AbilityNightVision() {
        this(TrinketsConfig.SERVER.ABILITIES.NIGHT_VISION);
    }

    public AbilityNightVision(ConfigAbilityNightVision config) {
        super(TrinketsRegistryNames.ModAbilities.NIGHT_VISION);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
        this.toggled = true;
        this.mode = -1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(@Nonnull TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        final TranslationHelper.KeyEntry keybind1 = new TranslationHelper.KeyBindEntry("denvkb", ModKeyBindings.DRAGONS_EYE_ABILITY.getDisplayName());
        return helper.formatAddVariables(key, renderID, keybind1);
    }

    @Override
    public void tickAbility(@Nonnull EntityLivingBase entity) {
        boolean potActive = entity.isPotionActive(MobEffects.NIGHT_VISION);
        boolean blindness = entity.isPotionActive(MobEffects.BLINDNESS);
        boolean isClient = entity.getEntityWorld().isRemote;

        if (this.isAbilityToggled() && !blindness) {
            if (!isClient) {
                if (this.CONFIG.COST <= 0F) {
                    if ((!potActive || (entity.ticksExisted % (20 * 10)) == 0)) {
                        entity.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 400, 0, false, false));
                    }
                } else {
                    if (!potActive) {
                        if (Capabilities.getMagicStats(entity, false, (magic, rtn) -> magic.spendMana(this.CONFIG.COST))) {
                            entity.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 400, 0, false, false));
                        } else {
                            this.toggleAbility(false);
                        }
                    } else {
                        if ((entity.ticksExisted % 20) == 0) {
                            if (Capabilities.getMagicStats(entity, false, (magic, rtn) -> magic.spendMana(this.CONFIG.COST))) {
                                entity.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 400, 0, false, false));
                            } else {
                                this.toggleAbility(false);
                            }
                        }
                    }
                }
            }
            if (isClient && entity.isPotionActive(MobEffects.NIGHT_VISION)) {
                entity.getActivePotionEffect(MobEffects.NIGHT_VISION).setPotionDurationMax(true);
            }
        } else {
            if (potActive) {
                entity.removePotionEffect(MobEffects.NIGHT_VISION);
            }
        }
    }

    @Override
    public void onAbilityRemoved(@Nonnull EntityLivingBase entity) {
        if (entity.isPotionActive(MobEffects.NIGHT_VISION)) {
            entity.removePotionEffect(MobEffects.NIGHT_VISION);
        }
    }

    @Override
    public boolean isAbilityToggled() {
        return this.toggled;
    }

    @Override
    public int getToggleMode() {
        return this.mode;
    }

    @Override
    public IToggleAbility toggleAbility(boolean enabled) {
        if (this.toggled != enabled) {
            this.toggled = enabled;
            this.setChanged(true);
        }
        return this;
    }

    @Override
    public IToggleAbility toggleAbility(int value) {
        if (this.mode != value) {
            this.mode = value;
            this.setChanged(true);
        }
        return this.toggleAbility(value > 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getKey() {
        return ModKeyBindings.DRAGONS_EYE_ABILITY.getDisplayName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getAuxKey() {
        return ModKeyBindings.AUX_KEY.getDisplayName();
    }

    @Override
    public boolean onKeyPress(@Nonnull Entity entity, boolean Aux) {
        if (!entity.world.isRemote) {
            this.toggleAbility(!this.isAbilityToggled());
        }
        return true;
    }

    @Override
    public NBTTagCompound saveStorage(@Nonnull NBTTagCompound compound) {
        compound.setBoolean("isEnabled", this.isAbilityToggled());
        return compound;
    }

    @Override
    public void loadStorage(@Nonnull NBTTagCompound compound) {
        if (compound.hasKey("isEnabled")) {
            this.toggled = compound.getBoolean("isEnabled");
        }
    }

}
