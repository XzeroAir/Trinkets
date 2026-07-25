package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.traits.abilities.base.AbilityRaceSpecific;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityStampede;
import xzeroair.trinkets.util.handlers.Counter;
import xzeroair.trinkets.util.helpers.StringUtils;
import xzeroair.trinkets.util.helpers.TranslationHelper;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class AbilityStampede extends AbilityRaceSpecific {

    protected ConfigAbilityStampede CONFIG;

    protected int maxChargeTicks;
    protected double minVelocity, maxVelocity, baseKnockback, baseYKnockback;
    protected float minDamage, maxDamage, cost;

    public AbilityStampede() {
        this(TrinketsConfig.SERVER.ABILITIES.STAMPEDE);
    }

    public AbilityStampede(ConfigAbilityStampede config) {
        super(TrinketsRegistryNames.ModAbilities.STAMPEDE);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
        this.maxChargeTicks = config.CHARGE_TIME;
        this.cost = config.ATTACK_COST;
        this.minDamage = 1F;
        this.maxDamage = config.ATTACK_DAMAGE;
        this.minVelocity = config.VELOCITY_MIN;
        this.maxVelocity = config.VELOCITY_MAX;
        this.baseKnockback = 0.6D;
        this.baseYKnockback = 0.3D;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        final TranslationHelper.KeyEntry key1 = new TranslationHelper.KeyBindEntry("rkey", ModKeyBindings.RACE_ABILITY.getDisplayName());
        final TranslationHelper.KeyEntry key2 = new TranslationHelper.KeyBindEntry("auxkb", ModKeyBindings.AUX_KEY.getDisplayName());
        return helper.formatAddVariables(key, renderID, key1, key2);
    }

    private final Set<Entity> hitEntities = new HashSet<>();

    public void handleChargeTick(EntityLivingBase player, double curve, BiConsumer<Entity, Double> consumer) {
        AxisAlignedBB swept = player.getEntityBoundingBox().union(player.getEntityBoundingBox().offset(player.motionX, player.motionY, player.motionZ)).grow(0.5D);

        List<Entity> nearby = player.world.getEntitiesWithinAABBExcludingEntity(player, swept);

        for (Entity target : nearby) {
            if (!this.canChargeHit(player, target)) {
                continue;
            }
            if (!this.hitEntities.contains(target)) {
                this.hitEntities.add(target);
                if (target instanceof EntityPlayer) {
                    boolean pvpEnabled = false;
                    try {
                        if (player instanceof EntityPlayerMP) {
                            pvpEnabled = ((EntityPlayerMP) player).getServer().isPVPEnabled();
                        }
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                    if (pvpEnabled) {
                        this.applyChargeHit(player, target, curve, consumer);
                    }
                } else {
                    this.applyChargeHit(player, target, curve, consumer);
                }
            }
        }
    }

    private boolean canChargeHit(EntityLivingBase player, Entity target) {
        if ((target == null) || target.isDead || (target.world != player.world)) {
            return false;
        }
        if (target instanceof EntityHanging) {
            return false;
        }
        return this.hasChargeLineOfSight(player, target);
    }

    private boolean hasChargeLineOfSight(EntityLivingBase player, Entity target) {
        final AxisAlignedBB targetBox = target.getEntityBoundingBox();
        final Vec3d start = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        final Vec3d end = new Vec3d(
                (targetBox.minX + targetBox.maxX) * 0.5D,
                (targetBox.minY + targetBox.maxY) * 0.5D,
                (targetBox.minZ + targetBox.maxZ) * 0.5D
        );
        final RayTraceResult result = player.world.rayTraceBlocks(start, end, false, true, false);
        return result == null;
    }

    private void applyChargeHit(EntityLivingBase player, Entity target, double curve, BiConsumer<Entity, Double> consumer) {
        if (consumer != null) {
            consumer.accept(target, curve);
        }
        Vec3d look = player.getLookVec().normalize();
        double strength = this.baseKnockback * curve;
        target.motionX += look.x * strength;
        target.motionY += this.baseYKnockback * curve;
        target.motionZ += look.z * strength;
        target.velocityChanged = true;
    }

    public void applyChargedPush(@Nonnull EntityLivingBase player, int chargeTicks, int maxChargeTicks, double minVelocity, double maxVelocity, BiConsumer<Entity, Double> consumer) {

        // Clamp charge
        int clampChargeTicks = Math.min(chargeTicks, maxChargeTicks);

        // Normalize to 0.0 → 1.0
        double normalizedCurve = (double) clampChargeTicks / (double) maxChargeTicks;

        normalizedCurve = normalizedCurve * normalizedCurve;
//        normalizedCurve = normalizedCurve - Math.pow(1 - normalizedCurve, 2); // ease-out

        double velocity = minVelocity + (maxVelocity - minVelocity) * normalizedCurve;

        Vec3d look = player.getLookVec();
        look = new Vec3d(look.x, 0, look.z).normalize(); // horizontal only

        player.motionX = look.x * velocity;
        player.motionY = look.y * velocity;
        player.motionZ = look.z * velocity;
        this.handleChargeTick(player, normalizedCurve, consumer);
        player.velocityChanged = true;
    }

    public void resetCharge() {
        this.hitEntities.clear();
    }

    private double prev_posx;
    private double prev_posy;
    private double prev_posz;

    public void startVec(EntityLivingBase player) {
        this.prev_posx = player.posX;
        this.prev_posy = player.posY;
        this.prev_posz = player.posZ;
    }

    public Vec3d lastVec() {
        return new Vec3d(this.prev_posx, this.prev_posy, this.prev_posz);
    }

    protected boolean prepareCharge(EntityLivingBase entity, MagicStats magic, boolean def, boolean aux) {
        if (aux) {
            return false;
        }
        final double pct = Math.min(MathHelper.pct(magic.getMana(), 0, this.cost), 1D);
        final int length = Math.min((int) (this.maxChargeTicks * pct), this.maxChargeTicks);
        final Counter counter = this.tickHandler.getCounter("heldCounter", length, false, true, false, true, false);
        counter.resetTick();
        counter.setLength(length);
        this.startVec(entity);
        if (pct < 0.10D) {
            return false;
        }
        return def;
    }

    protected boolean charge(EntityLivingBase entity, MagicStats magic, boolean def, boolean aux) {
        if (aux) {
            return false;
        }
        final float mp = magic.getMana();
        final Counter counter = this.tickHandler.getCounter("heldCounter");
        final int tick = counter.getTick();
        final float multi = (float) MathHelper.pct(tick, 0, counter.getLength());
        final float realCost = (float) (this.cost * (MathHelper.pct(tick, 0, this.maxChargeTicks)));
        if (!counter.Tick()) {
            if ((entity instanceof EntityPlayer) && entity.world.isRemote) {
                if (entity.ticksExisted % 4 == 0) {
                    entity.world.playSound((EntityPlayer) entity, entity.getPosition(), SoundEvents.ENTITY_POLAR_BEAR_STEP, SoundCategory.PLAYERS, 0.3F, (float) StringUtils.getAccurateDouble((Math.min(0.4F + (0.6F * multi), 1F))));
                }
                entity.setVelocity(0, entity.motionY, 0);
                entity.setPosition(this.prev_posx, entity.posY, this.prev_posz);
                entity.velocityChanged = true;
            }
            magic.syncToManaCostToHud(realCost);
            if (realCost == mp) {
                return false;
            }
        } else {
            if ((entity instanceof EntityPlayer) && entity.world.isRemote) {
                entity.world.playSound((EntityPlayer) entity, entity.getPosition(), SoundEvents.ENTITY_ZOMBIE_PIG_ANGRY, SoundCategory.PLAYERS, 0.4F, 0.2F);
            }
            magic.syncToManaCostToHud(realCost);
            return false;
        }
        return def;
    }

    protected boolean doCharge(EntityLivingBase entity, MagicStats magic, boolean def, boolean aux) {
        if (aux) {
            return false;
        }
        final Counter counter = this.tickHandler.getCounter("heldCounter");
        final int tick = counter.getTick();
        final float multi = (float) MathHelper.pct(tick, 0, this.maxChargeTicks);
        final float realCost = (float) StringUtils.getAccurateDouble(this.cost * multi);
        if (tick > (counter.getLength() * 0.1)) {
            if (magic.spendMana(realCost)) {
                double groundMulti = this.getAbilityHolder().getHandler().getParentProperties().isGrounded() ? 1D : 0.25D;
                this.applyChargedPush(entity, tick, this.maxChargeTicks, this.minVelocity * groundMulti, this.maxVelocity * groundMulti, (e, d) -> {
                    float damage = (float) (this.minDamage + (this.maxDamage - this.minDamage) * d);
                    if (entity instanceof EntityPlayer) {
                        e.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) entity), damage);
                    } else {
                        e.attackEntityFrom(DamageSource.FLY_INTO_WALL, damage);
                    }
                });
                this.reset(entity);
                return true;
            }
        }
        this.reset(entity);
        return def;
    }

    protected void reset(Entity entity) {
        final Counter counter = this.tickHandler.getCounter("heldCounter");
        if (counter != null) {
            counter.resetTick();
            counter.setLength(this.maxChargeTicks);
        }
        this.resetCharge();
        if (entity instanceof EntityLivingBase) {
            Capabilities.getMagicStats(entity, magic -> magic.syncToManaCostToHud(0));
        }
    }

    @Override
    public boolean onKeyPress(Entity entity, boolean Aux) {
        return Capabilities.getMagicStats(entity, true, (magic, ret) -> this.prepareCharge((EntityLivingBase) entity, magic, ret, Aux));
    }

    @Override
    public boolean onKeyDown(Entity entity, boolean Aux) {
        return Capabilities.getMagicStats(entity, true, (magic, ret) -> this.charge((EntityLivingBase) entity, magic, ret, Aux));
    }

    @Override
    public boolean onKeyRelease(Entity entity, boolean Aux) {
        return Capabilities.getMagicStats(entity, true, (magic, ret) -> this.doCharge((EntityLivingBase) entity, magic, ret, Aux));
    }

}
