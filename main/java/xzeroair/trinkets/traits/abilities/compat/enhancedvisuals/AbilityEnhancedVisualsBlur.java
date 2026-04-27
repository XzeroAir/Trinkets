package xzeroair.trinkets.traits.abilities.compat.enhancedvisuals;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IToggleAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.external.enhancedvisuals.ConfigAbilityEnhancedVisualsBlur;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AbilityEnhancedVisualsBlur extends AbilityEnhancedVisualsMod implements IToggleAbility, ITickableAbility, IAttackAbility {

    protected ConfigAbilityEnhancedVisualsBlur CONFIG;
    protected boolean IsShield;
    protected boolean HasCombinedProtection;
    protected int COOLDOWN;

    public AbilityEnhancedVisualsBlur() {
        this(TrinketsConfig.SERVER.ABILITIES.EXTERNAL.CLEAR_VISION);
    }

    public AbilityEnhancedVisualsBlur(ConfigAbilityEnhancedVisualsBlur config) {
        super(TrinketsRegistryNames.ModAbilities.ENHANCED_VISUALS_BLUR);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
        this.COOLDOWN = 0;
    }

    @Override
    public void onAbilityAdded(EntityLivingBase entity) {
        this.SOURCE = getAbilityHolder().getSourceID();
        this.IsShield = this.SOURCE.compareTo(TrinketsRegistryNames.ModItems.ModItemsReg.REG_HONOR_SHIELD) == 0;
        this.HasCombinedProtection = this.hasCombinedProtection(entity);
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        this.HasCombinedProtection = this.hasCombinedProtection(entity);
        if (isAbilityToggled()) {
            if (COOLDOWN > 0) {
                this.COOLDOWN--;
            }
        }
    }

    @Override
    public boolean attacked(EntityLivingBase attacked, DamageSource source, float dmg, boolean cancel) {
        if (source.isExplosion()) {
            if (this.IsShield) {
                this.toggleAbility((20 * 10));
            }
        }
        return cancel;
    }


    @Override
    public boolean isAbilityToggled() {
        return this.HasCombinedProtection || !IsShield || this.COOLDOWN > 0;
    }

    @Override
    public int getToggleMode() {
        return -1;
    }

    @Override
    public IToggleAbility toggleAbility(boolean enabled) {
        return this;
    }

    @Override
    public IToggleAbility toggleAbility(int value) {
        if (this.COOLDOWN != value) {
            this.COOLDOWN = value;
            this.setChanged(true);
        }
        return this;
    }


    @Override
    public void loadDataCache(@Nonnull NBTTagCompound tag) {
        if (tag.hasKey(this.COOLDOWN_TAG)) {
            this.COOLDOWN = tag.getInteger(this.COOLDOWN_TAG);
        }
    }

    @Nullable
    @Override
    public NBTTagCompound sendAbilityData() {
        NBTTagCompound tag = super.sendAbilityData();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setInteger(COOLDOWN_TAG, this.COOLDOWN);
        return tag;
    }

    protected boolean hasCombinedProtection(EntityLivingBase entity) {
        return this.hasAccessory(entity, TrinketsRegistryNames.ModItems.ModItemsReg.REG_SEA_STONE)
                && this.hasAccessory(entity, TrinketsRegistryNames.ModItems.ModItemsReg.REG_HONOR_SHIELD);
    }

    protected boolean hasAccessory(EntityLivingBase entity, String registryName) {
        return TrinketHelper.AccessoryCheck(entity, this.matchesRegistryName(registryName));
    }

    protected java.util.function.Predicate<ItemStack> matchesRegistryName(String registryName) {
        return stack -> !stack.isEmpty()
                && stack.getItem().getRegistryName() != null
                && stack.getItem().getRegistryName().toString().contentEquals(registryName);
    }
}
