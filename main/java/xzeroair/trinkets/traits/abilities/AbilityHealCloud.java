package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.WorldServer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.entity.AreaEffectEntity;
import xzeroair.trinkets.traits.abilities.base.AbilityRaceSpecific;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityHealCloud;
import xzeroair.trinkets.util.helpers.PotionHelper;
import xzeroair.trinkets.util.helpers.RayTraceHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AbilityHealCloud extends AbilityRaceSpecific {

    protected static final int DEFAULT_FIELD_COLOR = 12514535;

    protected float COST_PER_SECOND;
    protected float COST_PER_POTION_EFFECT;
    protected float COST_PER_EFFECT_LEVEL;

    protected float COST;
    protected float radius;
    protected float verticalRadius;
    protected int duration;
    protected int waitTime;
    protected int pulseInterval;
    protected int reapplicationDelay;
    protected int itemRepairAmount;
    protected int growthAttemptsPerPulse;
    protected double castRange;
    protected UUID activeFieldId;
    protected List<PotionEffect> configuredPotionEffects = new ArrayList<>();

    public AbilityHealCloud() {
        this(TrinketsConfig.SERVER.ABILITIES.RESTORATION_FIELD);
    }

    public AbilityHealCloud(ConfigAbilityHealCloud config) {
        super(TrinketsRegistryNames.ModAbilities.RESTORATION_FIELD);
        this.applyConfig(config);
    }

    protected void applyConfig(ConfigAbilityHealCloud config) {
        this.setAbilityEnabled(config.ENABLED);
        this.radius = config.RADIUS;
        this.verticalRadius = config.VERTICAL_RADIUS;
        this.duration = config.DURATION;
        this.waitTime = config.WAIT_TIME;
        this.pulseInterval = config.PULSE_INTERVAL;
        this.reapplicationDelay = config.REAPPLICATION_DELAY;
        this.itemRepairAmount = config.ITEM_REPAIR_AMOUNT;
        this.growthAttemptsPerPulse = config.GROWTH_ATTEMPTS_PER_PULSE;
        this.castRange = config.CAST_RANGE;
        this.COST_PER_SECOND = config.COST_PER_SECOND;
        this.COST_PER_POTION_EFFECT = config.COST_PER_POTION_EFFECT;
        this.COST_PER_EFFECT_LEVEL = config.COST_PER_EFFECT_LEVEL;

        this.configuredPotionEffects.clear();
        if (config.EFFECTS != null) {
            for (String effectId : config.EFFECTS) {
                if (effectId == null || effectId.trim().isEmpty()) {
                    continue;
                }

                final PotionHelper.PotionHolder potion = PotionHelper.getPotionHolder(effectId);
                if (potion.getPotion() != null) {
                    this.configuredPotionEffects.add(potion.getPotionEffect());
                }
            }
        }

        this.COST = this.calculateManaCost();
    }

    protected List<PotionEffect> getPotionEffects() {
        return this.configuredPotionEffects;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        final TranslationHelper.KeyEntry keybind = new TranslationHelper.KeyBindEntry("racekb", this.getKey());
        final TranslationHelper.KeyEntry effects = new TranslationHelper.OptionEntry("effects", !this.getPotionEffects().isEmpty(), "");
        final TranslationHelper.KeyEntry repair = new TranslationHelper.OptionEntry("repair", this.itemRepairAmount > 0, this.itemRepairAmount);
        final TranslationHelper.KeyEntry growth = new TranslationHelper.OptionEntry("growth", this.growthAttemptsPerPulse > 0, this.growthAttemptsPerPulse);
        final TranslationHelper.KeyEntry cost = new TranslationHelper.OptionEntry("cost", true, this.COST);
        final TranslationHelper.KeyEntry radius = new TranslationHelper.OptionEntry("radius", true, this.radius);
        final TranslationHelper.KeyEntry duration = new TranslationHelper.OptionEntry("duration", true, this.duration);
        return helper.formatAddVariables(key, renderID, keybind, effects, repair, growth, cost, radius, duration);
    }

    protected float calculateManaCost() {
        final int seconds = Math.max(1, (int) Math.ceil(this.duration / 20.0D));
        float cost = seconds * COST_PER_SECOND;

        for (final PotionEffect effect : this.getPotionEffects()) {
            cost += COST_PER_POTION_EFFECT;
            cost += Math.max(0, effect.getAmplifier()) * COST_PER_EFFECT_LEVEL;
        }

        return cost;
    }

    public boolean castHealCloud(EntityLivingBase entity, MagicStats magic, boolean def, boolean aux) {
        final Vec3d hitLoc = this.getHealCloudTarget(entity);
        if (hitLoc == null) {
            return false;
        }

        if (!entity.world.isRemote) {
            if (magic != null && !magic.spendMana(this.COST)) {
                return false;
            }
            return this.spawnHealCloud(entity, hitLoc);
        }

        return true;
    }

    protected Vec3d getHealCloudTarget(EntityLivingBase entity) {
        final double maxDist = Math.max(1D, this.castRange);
        final Vec3d start = entity.getPositionEyes(1F);
        final Vec3d lookVec = entity.getLookVec();
        final Vec3d end = start.add(lookVec.x * maxDist, lookVec.y * maxDist, lookVec.z * maxDist);
        final RayTraceResult result = RayTraceHelper.rayTrace(entity, maxDist);
        if (result != null) {
            if (result.typeOfHit == RayTraceResult.Type.ENTITY && result.entityHit != null && result.hitVec != null) {
                final Entity hit = result.entityHit;
                return result.hitVec.add(0, hit.height * 0.5F, 0);
            } else if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
                return this.getBlockCenteredHitLocation(result);
            }
        }
        return this.getGroundedRangeLimitLocation(entity, end);
    }

    protected Vec3d getGroundedRangeLimitLocation(EntityLivingBase entity, Vec3d rangeLimit) {
        final Vec3d groundProbeEnd = rangeLimit.add(0D, -Math.max(8D, this.verticalRadius + 3D), 0D);
        final RayTraceResult ground = entity.world.rayTraceBlocks(rangeLimit, groundProbeEnd, false, true, false);
        return ground != null && ground.typeOfHit == RayTraceResult.Type.BLOCK ? this.getBlockCenteredHitLocation(ground) : null;
    }

    protected Vec3d getBlockCenteredHitLocation(RayTraceResult result) {
        if (result == null || result.getBlockPos() == null || result.hitVec == null) {
            return result == null || result.hitVec == null ? null : result.hitVec;
        }
        final BlockPos pos = result.getBlockPos();
        return new Vec3d(pos.getX() + 0.5D, result.hitVec.y, pos.getZ() + 0.5D);
    }

    protected boolean spawnHealCloud(EntityLivingBase entity, Vec3d hitLoc) {
        final int color = this.getFieldColor(entity);
        if (!entity.getEntityWorld().isRemote) {
            return this.spawnServerHealCloud(entity, hitLoc, color);
        }
        return true;
    }

    protected boolean spawnServerHealCloud(EntityLivingBase entity, Vec3d hitLoc, int color) {
        AreaEffectEntity oldArea = this.getActiveField(entity);
        AreaEffectEntity newArea = this.createAreaEffect(entity, hitLoc, color);

        if (!entity.world.spawnEntity(newArea)) {
            return false;
        }

        if (oldArea != null && !oldArea.isDead) {
            oldArea.setDead();
        }

        this.activeFieldId = newArea.getUniqueID();
        return true;
    }

    protected AreaEffectEntity getActiveField(EntityLivingBase entity) {
        if (entity == null || entity.world.isRemote || this.activeFieldId == null || !(entity.world instanceof WorldServer)) {
            return null;
        }

        Entity activeEntity = ((WorldServer) entity.world).getEntityFromUuid(this.activeFieldId);
        return activeEntity instanceof AreaEffectEntity ? (AreaEffectEntity) activeEntity : null;
    }

    protected int getFieldColor(EntityLivingBase entity) {
        final EntityProperties properties = Capabilities.getEntityProperties(entity);
        return properties == null ? DEFAULT_FIELD_COLOR : properties.getRaceHandler().getPrimaryTraitColor();
    }

    public AreaEffectEntity createAreaEffect(EntityLivingBase owner, Vec3d position) {
        return this.createAreaEffect(owner, position, this.getFieldColor(owner));
    }

    public AreaEffectEntity createAreaEffect(EntityLivingBase owner, Vec3d position, int color) {
        AreaEffectEntity area = new AreaEffectEntity(owner.world, position.x, position.y, position.z);
        area.setOwner(owner);
        area.setRadius(this.radius);
        area.setVerticalRadius(this.verticalRadius);
        area.setWaitTime(this.waitTime);
        area.setDuration(this.duration);
        area.setPulseInterval(this.pulseInterval);
        area.setReapplicationDelay(this.reapplicationDelay);
        area.setColor(color);
        area.blacklistEntityRegistry("minecraft:item_frame", "minecraft:painting");

        for (final PotionEffect effect : this.getPotionEffects()) {
            area.addAction(new AreaEffectEntity.PotionAreaAction(effect));
        }

        if (this.itemRepairAmount > 0) {
            area.addAction(new AreaEffectEntity.RepairItemAreaAction(this.itemRepairAmount));
        }
        if (this.growthAttemptsPerPulse > 0) {
            area.addAction(new AreaEffectEntity.GrowBlockAreaAction(this.growthAttemptsPerPulse));
        }
        return area;
    }

    @Override
    public boolean onKeyPress(Entity entity, boolean Aux) {
        return true;
    }

    @Override
    public boolean onKeyDown(Entity entity, boolean Aux) {
        return true;
    }

    @Override
    public boolean onKeyRelease(Entity entity, boolean Aux) {
        if (entity.world.isRemote) {
            return true;
        }

        final MagicStats magic = Capabilities.getMagicStats(entity);
        if (magic == null) {
            return false;
        }
        return this.castHealCloud(magic.getEntity(), magic, false, Aux);
    }
}
