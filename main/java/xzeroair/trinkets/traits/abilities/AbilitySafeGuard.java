package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Vip.VipStatus;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilitySafeGuard;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class AbilitySafeGuard extends Ability implements ITickableAbility, IAttackAbility, IPotionAbility {

    protected final ConfigAbilitySafeGuard CONFIG;

    protected static final String COUNT_TAG = "COUNT";
    protected static final String COUNT_SELF_ADDED_TAG = "SELF_ADDED";
    protected static final String COUNT_MODIFIED_AMOUNT_TAG = "MODIFIED";
    protected static final String EFFECT_TAG = "EFFECT";

    protected final boolean EFFECT_STACKS;
    protected final int MAX_HIT_COUNT, EFFECT_STACK_LIMIT;
    protected final float MIN_DAMAGE_TO_COUNT, MIN_DAMAGE_TO_TRIGGER, EXPLOSION_REDUCED_AMOUNT;
    protected int HIT_COUNT, MODIFIED_AMOUNT;
    protected boolean SELF_ADDED;
    protected String EFFECT;

    public AbilitySafeGuard() {
        this(TrinketsConfig.SERVER.ABILITIES.SAFE_GUARD);
    }

    public AbilitySafeGuard(ConfigAbilitySafeGuard config) {
        super(TrinketsRegistryNames.ModAbilities.SAFE_GUARD);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
        this.MAX_HIT_COUNT = config.MAX_HITS;
        this.EFFECT_STACKS = config.EFFECT_STACKS;
        this.EXPLOSION_REDUCED_AMOUNT = config.EXPLOSION_REDUCED_AMOUNT;
        this.MIN_DAMAGE_TO_COUNT = config.MIN_DAMAGE_TO_COUNT;
        this.MIN_DAMAGE_TO_TRIGGER = config.MIN_DAMAGE_TO_TRIGGER;
        this.EFFECT = config.DEFAULT_EFFECT;
        this.EFFECT_STACK_LIMIT = config.EFFECT_STACKS_LIMIT;
        this.SELF_ADDED = false;
        this.MODIFIED_AMOUNT = 0;
        this.HIT_COUNT = 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        String langKey = this.getTranslationKey();
        final TranslationHelper.KeyEntry key1 = new TranslationHelper.LangEntry(langKey, "explosionresist", this.EXPLOSION_REDUCED_AMOUNT < 1);
        final TranslationHelper.KeyEntry key2 = new TranslationHelper.OptionEntry("explosionresistamount", this.EXPLOSION_REDUCED_AMOUNT < 1, ((100F - (this.CONFIG.EXPLOSION_REDUCED_AMOUNT * 100F)) + "%"));
        final TranslationHelper.KeyEntry key3 = new TranslationHelper.LangEntry(langKey, "damageignored", true);
        final TranslationHelper.KeyEntry key4 = new TranslationHelper.OptionEntry("hitcount", true, this.getHitCount());
        final TranslationHelper.KeyEntry key5 = new TranslationHelper.OptionEntry("cfghitcount", true, this.MAX_HIT_COUNT);
        String effect = "ERROR";
        try {
            final Potion peffect = Potion.getPotionFromResourceLocation(this.EFFECT);
            if (peffect != null) {
                effect = new TextComponentTranslation(peffect.getName()).getUnformattedText();
            }
        } catch (Exception e) {
        }
        final TranslationHelper.KeyEntry key6 = new TranslationHelper.OptionEntry("soheffect", true, effect);
        return helper.formatAddVariables(key, renderID, key1, key2, key3, key4, key5, key6);
    }

    public int getHitCount() {
        return this.HIT_COUNT;
    }

    public AbilitySafeGuard setHitCount(int hitCount) {
        if (this.HIT_COUNT != hitCount) {
            this.HIT_COUNT = hitCount;
            this.setChanged(true);
        }
        return this;
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        final World world = entity.getEntityWorld();
        if (world.isRemote) {
            return;
        }
        final int duration = 200;
        Potion resistance = Potion.getPotionFromResourceLocation(this.EFFECT);
        if (resistance == null) {
            resistance = MobEffects.RESISTANCE;
        }
        final boolean has = entity.isPotionActive(resistance);
        if (!has) {
            entity.addPotionEffect(new PotionEffect(resistance, duration, this.CONFIG.DEFAULT_EFFECT_LEVEL, false, false));
        } else {
        }
    }

    @Override
    public boolean attacked(EntityLivingBase attacked, DamageSource source, float dmg, boolean cancel) {
        if (!attacked.world.isRemote) {
            if (dmg >= this.MIN_DAMAGE_TO_COUNT) {
                final boolean indirect = this.isIndirectDamage(source);
                if (indirect) {
                    this.countHit(attacked, indirect);
                    if (dmg >= this.MIN_DAMAGE_TO_TRIGGER && this.trigger(attacked, indirect)) {
                        return true;
                    }
                }
            }
        }
        return cancel;
    }

    @Override
    public float hurt(EntityLivingBase attacked, DamageSource source, float dmg) {
        if (dmg >= this.MIN_DAMAGE_TO_COUNT) {
            final boolean indirect = this.isIndirectDamage(source);
            if (!indirect) {
                this.countHit(attacked, false);
                if (dmg >= this.MIN_DAMAGE_TO_TRIGGER && this.trigger(attacked, false)) {
                    dmg = 0;
                }
            }
        }
        if (this.CONFIG.EXPLOSION_REDUCED_AMOUNT < 1) {
            if (source.isExplosion()) {
                if (dmg > 0) {
                    return dmg * this.CONFIG.EXPLOSION_REDUCED_AMOUNT;
                }
            }
        }
        return dmg;
    }

    protected void countHit(EntityLivingBase attacked, boolean indirect) {
        this.setHitCount(this.getHitCount() + 1);
    }

    protected boolean trigger(EntityLivingBase attacked, boolean indirect) {
        if (this.getHitCount() > this.MAX_HIT_COUNT) {
            if (attacked instanceof EntityPlayer) {
                final EntityPlayer player = (EntityPlayer) attacked;
                if (TrinketsConfig.SERVER.MISC.VIPS) {
                    String string = "Ow!";
                    final VipStatus vip = Capabilities.getVipStatus(attacked);
                    if (vip != null) {
                        final String quote = vip.getRandomQuote();
                        if (!quote.isEmpty()) {
                            string = quote;
                        }
                    }
                    final TextComponentString message = new TextComponentString(TextFormatting.BOLD + "" + TextFormatting.GOLD + string);
                    player.sendStatusMessage(message, true);

                    if (this.CONFIG.CLIENT.VOLUME > 0) {
                        player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, this.CONFIG.CLIENT.VOLUME, this.CONFIG.CLIENT.PITCH);
                    }
                }
            }
            this.setHitCount(0);
            return true;
        }
        return false;
    }

    @Override
    public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
        final String AppliedEffect = effect.getPotion().getRegistryName().toString();
        final String targetEffect = this.EFFECT;
        if (!AppliedEffect.contentEquals(targetEffect)) {
            return cancel;
        }
        if (!entity.world.isRemote && this.EFFECT_STACKS) {
            Potion resistance = Potion.getPotionFromResourceLocation(targetEffect);
            if (resistance == null) {
                resistance = MobEffects.RESISTANCE;
            }
            final boolean has = entity.isPotionActive(resistance);

            final PotionEffect newEffect = effect;
            final int newAmp = newEffect.getAmplifier();
            final int newDur = newEffect.getDuration();

            if (!has) {
                //				System.out.println("We Don't Have it, Just apply");
                return cancel;
            } else {
                final PotionEffect oldEffect = entity.getActivePotionEffect(resistance);
                final int oldAmp = oldEffect.getAmplifier();
                final int oldDur = oldEffect.getDuration();
                int amp = newAmp + 1;
                int duration = newDur;

                if ((oldAmp > amp)) {
                    //					System.out.println("Don't Allow");
                    return true; // true because we're canceling it
                }
                if ((amp <= this.EFFECT_STACK_LIMIT)) {
                    try {
                        PotionEffect e = new PotionEffect(resistance, duration, // just give effect duration
                                amp, // just give effect amp  + 1
                                false, false);
                        effect.combine(e);
                    } catch (Exception e) {
                    }
                }
                return false; // false because we're not canceling it
            }
        }
        return cancel;
    }

    @Override
    public void onAbilityRemoved(EntityLivingBase entity) {
        Potion resistance = Potion.getPotionFromResourceLocation(this.CONFIG.DEFAULT_EFFECT);
        if (resistance == null) {
            resistance = MobEffects.RESISTANCE;
        }
        if (entity.isPotionActive(resistance)) {
            entity.removePotionEffect(resistance);
            if (entity.world.isRemote) {
                entity.addPotionEffect(new PotionEffect(resistance, 30, this.CONFIG.DEFAULT_EFFECT_LEVEL, false, false));
            }
        }
    }

    @Override
    public void loadStorage(NBTTagCompound compound) {
        super.loadStorage(compound);
        if (compound.hasKey(COUNT_TAG)) {
            this.HIT_COUNT = compound.getInteger(COUNT_TAG);
        }
        if (compound.hasKey(COUNT_MODIFIED_AMOUNT_TAG)) {
            this.MODIFIED_AMOUNT = compound.getInteger(COUNT_MODIFIED_AMOUNT_TAG);
        }
        if (compound.hasKey(COUNT_SELF_ADDED_TAG)) {
            this.SELF_ADDED = compound.getBoolean(COUNT_SELF_ADDED_TAG);
        }
        if (compound.hasKey(EFFECT_TAG)) {
            this.EFFECT = compound.getString(EFFECT_TAG);
        }
    }

    @Override
    public NBTTagCompound saveStorage(NBTTagCompound compound) {
        compound.setInteger(COUNT_TAG, this.getHitCount());
        return compound;
    }

}
