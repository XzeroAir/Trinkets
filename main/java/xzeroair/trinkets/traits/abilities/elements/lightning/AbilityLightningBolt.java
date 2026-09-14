package xzeroair.trinkets.traits.abilities.elements.lightning;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.client.events.ScreenOverlayEvents;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.entity.EntityRangedAttack;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.traits.abilities.interfaces.IKeyBindInterface;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityLightningBolt;
import xzeroair.trinkets.util.handlers.Counter;
import xzeroair.trinkets.util.helpers.StringUtils;
import xzeroair.trinkets.util.helpers.TranslationHelper;

import javax.annotation.Nonnull;

public class AbilityLightningBolt extends Ability implements IKeyBindInterface {

    private static final String CHARGE_COUNTER = "heldCounter";
    private static final float MIN_CHARGE_FRACTION = 0.10F;
    private static final float BOLT_SPEED = 5.0F;
    private static final int BOLT_LIFETIME_TICKS = 3;
    private static final int LIGHTNING_COLOR = 2515356;
    protected final ConfigAbilityLightningBolt CONFIG;

    protected float COST, DAMAGE;
    protected int CHARGE_TIME;

    public AbilityLightningBolt() {
        this(TrinketsConfig.SERVER.ABILITIES.LIGHTNING_BOLT);
    }

    public AbilityLightningBolt(@Nonnull ConfigAbilityLightningBolt config) {
        super(TrinketsRegistryNames.ModAbilities.LIGHTNING_BOLT);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
        this.COST = config.ATTACK_COST;
        this.DAMAGE = config.ATTACK_DAMAGE;
        this.CHARGE_TIME = config.CHARGE_TIME;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(@Nonnull TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        String langKey = this.getTranslationKey();
        final TranslationHelper.KeyEntry key1 = new TranslationHelper.LangEntry(langKey, "boltattack", true);
        final TranslationHelper.KeyEntry key2 = new TranslationHelper.OptionEntry("boltcost", true, this.COST);
        final TranslationHelper.KeyEntry key3 = new TranslationHelper.OptionEntry("boltdamage", true, this.DAMAGE);
        final TranslationHelper.KeyEntry key4 = new TranslationHelper.KeyBindEntry("arckb", true, ModKeyBindings.ARCING_ORB_ABILITY.getDisplayName());
        return helper.formatAddVariables(key, renderID, key1, key2, key3, key4);
    }

    protected boolean prepareBolt(EntityLivingBase entity, @Nonnull MagicStats magic, boolean def, boolean aux) {
        if (!magic.canSpendMana(this.COST * MIN_CHARGE_FRACTION)) {
            return false;
        }
        final Counter counter = this.tickHandler.getCounter(CHARGE_COUNTER, this.CHARGE_TIME, false, true, false, true, false);
        counter.resetTick();
        counter.setLength(this.CHARGE_TIME);
        return def;
    }

    protected boolean chargeBolt(EntityLivingBase entity, @Nonnull MagicStats magic, boolean def, boolean aux) {
        final Counter counter = this.tickHandler.getCounter(CHARGE_COUNTER);
        if (counter == null || !magic.canSpendMana(1F)) {
            this.reset(entity);
            return false;
        }
        final int tick = counter.getTick();
        final float charge = this.getCharge(tick);
        final float realCost = this.getManaCost(tick);
        if (!counter.Tick()) {
            if ((entity instanceof EntityPlayer) && entity.world.isRemote && entity.ticksExisted % 2 == 0) {
                entity.world.playSound((EntityPlayer) entity, entity.getPosition(), SoundEvents.BLOCK_NOTE_BASS, SoundCategory.PLAYERS, 0.2F, (float) StringUtils.getAccurateDouble(Math.min(0.4F + (0.6F * charge), 1F)));
                ScreenOverlayEvents.instance.SyncCost(realCost);
            }
        } else {
            if ((entity instanceof EntityPlayer) && entity.world.isRemote) {
                entity.world.playSound((EntityPlayer) entity, entity.getPosition(), SoundEvents.BLOCK_END_GATEWAY_SPAWN, SoundCategory.PLAYERS, 0.3F, 0.3F);
            }
            if (!entity.world.isRemote) {
                this.castBolt(entity, magic, false, aux);
            } else {
                this.reset(entity);
            }
            return false;
        }
        return def;
    }

    protected boolean castBolt(EntityLivingBase entity, @Nonnull MagicStats magic, boolean def, boolean aux) {
        if (entity.world.isRemote) {
            this.reset(entity);
            return true;
        }
        final Counter counter = this.tickHandler.getCounter(CHARGE_COUNTER);
        if (counter == null) {
            return false;
        }
        final int tick = counter.getTick();
        final float charge = this.getCharge(tick);
        final float realCost = this.getManaCost(tick);
        if (charge >= MIN_CHARGE_FRACTION) {
            final float finalDamage = this.DAMAGE * charge;
            if (magic.spendMana(realCost)) {
                final Vec3d start = entity.getPositionEyes(1F);
                final Vec3d lookVec = entity.getLookVec();
                final EntityRangedAttack bolt = new EntityRangedAttack(entity.world, entity, LIGHTNING_COLOR).setColor(LIGHTNING_COLOR)
                                                                                                              .setElement(Elements.LIGHTNING)
                                                                                                              .setVisualType(EntityRangedAttack.VISUAL_LIGHTNING_BOLT)
                                                                                                              .setVisualOrigin(start)
                                                                                                              .setDamage(finalDamage)
                                                                                                              .setChargeCreepers(charge > 0.33F)
                                                                                                              .setImpactTemplate(EntityRangedAttack.ImpactTemplate.LIGHTNING_BOLT)
                                                                                                              .setRenderImpactAreaCircle(false)
                                                                                                              .setPhysicsTemplate(new EntityRangedAttack.PhysicsTemplate().setGravity(0.0F)
                                                                                                                                                                          .setDrag(Material.AIR, 1.0F)
                                                                                                                                                                          .setLifetimeTicks(BOLT_LIFETIME_TICKS));
                bolt.setPosition(start.x, start.y, start.z);
                bolt.shoot(lookVec.x, lookVec.y, lookVec.z, BOLT_SPEED, 0.0F);
                entity.world.spawnEntity(bolt);
                this.reset(entity);
                return true;
            }
        }
        this.reset(entity);
        return def;
    }

    protected void reset(Entity entity) {
        final Counter counter = this.tickHandler.getCounter(CHARGE_COUNTER);
        if (counter != null) {
            counter.resetTick();
            counter.setLength(this.CHARGE_TIME);
        }
        if (entity.world.isRemote) {
            ScreenOverlayEvents.instance.SyncCost(0);
        }
    }

    private float getCharge(int chargeTicks) {
        return (float) Math.min(MathHelper.pct(chargeTicks, 0, this.CHARGE_TIME), 1.0D);
    }

    private float getManaCost(int chargeTicks) {
        return (float) StringUtils.getAccurateDouble(this.COST * this.getCharge(chargeTicks));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getKey() {
        return ModKeyBindings.ARCING_ORB_ABILITY.getDisplayName();
    }

    @Override
    public boolean onKeyPress(Entity entity, boolean Aux) {
        return Capabilities.getMagicStats(entity, true, (magic, ret) -> this.prepareBolt((EntityLivingBase) entity, magic, ret, Aux));
    }

    @Override
    public boolean onKeyDown(Entity entity, boolean Aux) {
        return Capabilities.getMagicStats(entity, true, (magic, ret) -> this.chargeBolt((EntityLivingBase) entity, magic, ret, Aux));
    }

    @Override
    public boolean onKeyRelease(Entity entity, boolean Aux) {
        if (entity.world.isRemote) {
            this.reset(entity);
            return true;
        }
        return Capabilities.getMagicStats(entity, true, (magic, ret) -> this.castBolt((EntityLivingBase) entity, magic, ret, Aux));
    }
}
