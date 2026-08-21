package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.traits.abilities.interfaces.IInteractionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IJumpAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IMovementAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityElytraFlight;
import xzeroair.trinkets.util.handlers.Counter;
import xzeroair.trinkets.util.helpers.TranslationHelper;

import javax.annotation.Nullable;

public class AbilityElytraFlight extends Ability implements ITickableAbility, IMovementAbility, IJumpAbility, IInteractionAbility {

    protected static final String GLIDING_TAG = "GLIDING";
    protected static final String LIFT_COST_TAG = "LIFT_COST";
    protected static final String LIFT_STRENGTH_TAG = "LIFT_STRENGTH";
    protected static final String FIREWORK_BOOST_TICKS_TAG = "FIREWORK_BOOST_TICKS";
    protected static final int VANILLA_FLIGHT_EXIT_DELAY = 7;

    protected float COST, LIFT_COST;
    protected double LIFT_STRENGTH;
    protected boolean GLIDING, LIFT_ENABLED, COLLISION_DAMAGE, LIFT_REQUESTED, VANILLA_FLIGHT_ACTIVE;
    protected int VANILLA_FLIGHT_EXIT_TICKS, FIREWORK_BOOST_TICKS;
    protected double GLIDE_HORIZONTAL_SPEED;

    public AbilityElytraFlight() {
        this(TrinketsConfig.SERVER.ABILITIES.ELYTRA_FLIGHT);
    }

    public AbilityElytraFlight(ConfigAbilityElytraFlight config) {
        super(TrinketsRegistryNames.ModAbilities.ELYTRA_FLIGHT);
        this.setAbilityEnabled(config.ENABLED);
        this.COST = config.COST;
        this.LIFT_ENABLED = config.LIFT_ENABLED;
        this.COLLISION_DAMAGE = config.COLLISION_DAMAGE;
        this.LIFT_COST = config.LIFT_COST;
        this.LIFT_STRENGTH = config.LIFT_STRENGTH;
    }

    public boolean isGliding() {
        return this.GLIDING;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        final TranslationHelper.OptionEntry liftEntry = new TranslationHelper.OptionEntry("lift", this.LIFT_ENABLED, "");
        return helper.formatAddVariables(key, renderID, liftEntry);
    }

    @Override
    public void tickAbilityPre(EntityLivingBase entity) {
        if (this.GLIDING) {
            if (entity.motionY > -0.5D) {
                entity.fallDistance = 1F;
            }
            if (this.COLLISION_DAMAGE && !entity.world.isRemote) {
                this.GLIDE_HORIZONTAL_SPEED = Math.sqrt((entity.motionX * entity.motionX) + (entity.motionZ * entity.motionZ));
            }
        }
    }

    @Override
    public boolean onMovement(Entity entity, int primaryState, boolean primaryDown, boolean auxiliaryDown, int left, int right, int forward, int back, int jump, int sneak, @Nullable NBTTagCompound payload) {
        // Glide owns only Jump presses; every other movement state needs no server packet.
        return (jump == 0) && this.jump(entity, jump, primaryState, primaryDown, auxiliaryDown, payload);
    }

    @Override
    public boolean jump(Entity entity, int state, int primaryState, boolean primaryDown, boolean auxiliaryDown, @Nullable NBTTagCompound payload) {
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase living = (EntityLivingBase) entity;
            if (state == 0) {
                if (this.GLIDING) {
                    if (!living.world.isRemote) {
                        this.LIFT_REQUESTED = true;
                    }
                } else if (!this.VANILLA_FLIGHT_ACTIVE
                        && this.VANILLA_FLIGHT_EXIT_TICKS == 0
                        && this.canStartFlying(living)) {
                    final MagicStats magic = Capabilities.getMagicStats(living);
                    if (this.COST <= 0 || (magic != null && magic.canSpendMana(this.COST))) {
                        this.startFlight(living);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void rightClickWithItem(EntityLivingBase entity, World world, ItemStack stack, EnumHand hand, EnumFacing face, BlockPos pos) {
        if (world.isRemote || !this.GLIDING || stack.getItem() != Items.FIREWORKS) {
            return;
        }
        if (this.isUsingVanillaFlight(entity)) {
            return;
        }
        final NBTTagCompound fireworks = stack.hasTagCompound() ? stack.getTagCompound().getCompoundTag("Fireworks") : null;
        final int flight = fireworks == null ? 0 : fireworks.getByte("Flight");
        this.FIREWORK_BOOST_TICKS = Math.max(this.FIREWORK_BOOST_TICKS, 10 * (1 + flight) + entity.getRNG().nextInt(6) + entity.getRNG().nextInt(7));
        this.setChanged(true);
        world.spawnEntity(new EntityFireworkRocket(world, stack.copy(), entity));
        if (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).capabilities.isCreativeMode) {
            stack.shrink(1);
        }
    }

    protected void applyFireworkBoost(EntityLivingBase entity) {
        if (this.FIREWORK_BOOST_TICKS <= 0) {
            return;
        }
        this.FIREWORK_BOOST_TICKS--;
        final Vec3d look = entity.getLookVec();
        entity.motionX += look.x * 0.1D + (look.x * 1.5D - entity.motionX) * 0.5D;
        entity.motionY += look.y * 0.1D + (look.y * 1.5D - entity.motionY) * 0.5D;
        entity.motionZ += look.z * 0.1D + (look.z * 1.5D - entity.motionZ) * 0.5D;
        entity.velocityChanged = true;
    }



    @Override
    public void onAbilityRemoved(EntityLivingBase entity) {
        this.stopFlight(entity);
        this.LIFT_REQUESTED = false;
        this.VANILLA_FLIGHT_ACTIVE = false;
        this.VANILLA_FLIGHT_EXIT_TICKS = 0;
        this.tickHandler.removeCounter("elytra_flight_cost");
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        if (this.GLIDING && entity.isPotionActive(MobEffects.LEVITATION)) {
            this.stopFlight(entity);
            this.LIFT_REQUESTED = false;
            return;
        }
        if (this.isUsingVanillaFlight(entity)) {
            this.VANILLA_FLIGHT_ACTIVE = true;
            this.VANILLA_FLIGHT_EXIT_TICKS = 0;
            this.LIFT_REQUESTED = false;
            this.stopFlight(entity);
            return;
        }
        if (this.VANILLA_FLIGHT_ACTIVE) {
            this.VANILLA_FLIGHT_ACTIVE = false;
            this.VANILLA_FLIGHT_EXIT_TICKS = VANILLA_FLIGHT_EXIT_DELAY;
        } else if (this.VANILLA_FLIGHT_EXIT_TICKS > 0) {
            this.VANILLA_FLIGHT_EXIT_TICKS--;
        }
        if (!this.GLIDING) {
            return;
        }
        if (this.COLLISION_DAMAGE && !entity.world.isRemote && entity.collidedHorizontally) {
            final double horizontalSpeed = Math.sqrt((entity.motionX * entity.motionX) + (entity.motionZ * entity.motionZ));
            final float crashDamage = (float) ((this.GLIDE_HORIZONTAL_SPEED - horizontalSpeed) * 10.0D - 3.0D);
            if (crashDamage > 0F) {
                entity.attackEntityFrom(DamageSource.FLY_INTO_WALL, crashDamage);
            }
        }
        if (!this.canKeepFlying(entity)) {
            this.stopFlight(entity);
            this.LIFT_REQUESTED = false;
            return;
        }
        if (!entity.world.isRemote && this.COST > 0) {
            final MagicStats magic = Capabilities.getMagicStats(entity);
            final Counter counter = this.tickHandler.getCounter("elytra_flight_cost", 20, true, true, true, true);
            if (magic == null || (counter != null && counter.Tick() && !magic.spendMana(this.COST))) {
                this.stopFlight(entity);
                this.LIFT_REQUESTED = false;
                return;
            }
        }
        this.applyElytraMotion(entity);
        this.applyFireworkBoost(entity);
        if (this.LIFT_REQUESTED) {
            this.LIFT_REQUESTED = false;
            this.applyLift(entity);
        }
    }

    protected void applyLift(EntityLivingBase entity) {
        if (this.LIFT_ENABLED) {
            final MagicStats magic = Capabilities.getMagicStats(entity);
            if (this.LIFT_COST <= 0 || (magic != null && magic.spendMana(this.LIFT_COST))) {
                entity.motionY += this.LIFT_STRENGTH;
                entity.fallDistance = 0F;
                entity.velocityChanged = true;
            }
        }
    }


    protected void startFlight(EntityLivingBase entity) {
        if (!this.GLIDING) {
            this.GLIDING = true;
            this.setChanged(true);
        }
        entity.fallDistance = 0F;
    }

    protected void stopFlight(EntityLivingBase entity) {
        if (this.GLIDING) {
            this.GLIDING = false;
            this.GLIDE_HORIZONTAL_SPEED = 0D;
            this.setChanged(true);
        }
    }

    protected boolean canStartFlying(EntityLivingBase entity) {
        return !entity.isPotionActive(MobEffects.LEVITATION)
                && !this.isUsingVanillaFlight(entity)
                && this.canKeepFlying(entity)
                && !this.GLIDING
                && (entity.motionY < 0D);
    }

    protected boolean canKeepFlying(EntityLivingBase entity) {
        return !entity.onGround && !entity.isRiding() && !entity.isInWater() && !entity.isInLava();
    }

    protected boolean isUsingVanillaFlight(EntityLivingBase entity) {
        return entity.isElytraFlying() || this.isCreativeFlying(entity);
    }
    protected void applyElytraMotion(EntityLivingBase entity) {
        entity.motionX /= 0.91D;
        entity.motionY = (entity.motionY / 0.98D) + 0.08D;
        entity.motionZ /= 0.91D;
        final Vec3d look = entity.getLookVec();
        final float pitch = entity.rotationPitch * 0.017453292F;
        final double horizontalSpeed = Math.sqrt((entity.motionX * entity.motionX) + (entity.motionZ * entity.motionZ));
        final double lookHorizontal = Math.sqrt((look.x * look.x) + (look.z * look.z));
        final double lookLength = look.length();
        final float pitchCos = MathHelper.cos(pitch);
        final float lift = (float) ((double) pitchCos * (double) pitchCos * Math.min(1.0D, lookLength / 0.4D));

        entity.motionY += -0.08D + (lift * 0.06D);
        if ((entity.motionY < 0.0D) && (lookHorizontal > 0.0D)) {
            final double glidePull = entity.motionY * -0.1D * lift;
            entity.motionY += glidePull;
            entity.motionX += (look.x * glidePull) / lookHorizontal;
            entity.motionZ += (look.z * glidePull) / lookHorizontal;
        }
        if ((pitch < 0.0F) && (lookHorizontal > 0.0D)) {
            final double climbPull = horizontalSpeed * (double) (-MathHelper.sin(pitch)) * 0.04D;
            entity.motionY += climbPull * 3.2D;
            entity.motionX -= (look.x * climbPull) / lookHorizontal;
            entity.motionZ -= (look.z * climbPull) / lookHorizontal;
        }
        if (lookHorizontal > 0.0D) {
            entity.motionX += (((look.x / lookHorizontal) * horizontalSpeed) - entity.motionX) * 0.1D;
            entity.motionZ += (((look.z / lookHorizontal) * horizontalSpeed) - entity.motionZ) * 0.1D;
        }
        entity.motionX *= 0.99D;
        entity.motionY *= 0.98D;
        entity.motionZ *= 0.99D;
        entity.velocityChanged = true;
    }


    @Override
    public void loadStorage(NBTTagCompound compound) {
        super.loadStorage(compound);
        if (compound.hasKey(COST_TAG)) {
            this.COST = compound.getFloat(COST_TAG);
        }
        if (compound.hasKey(LIFT_COST_TAG)) {
            this.LIFT_COST = compound.getFloat(LIFT_COST_TAG);
        }
        if (compound.hasKey(LIFT_STRENGTH_TAG)) {
            this.LIFT_STRENGTH = compound.getDouble(LIFT_STRENGTH_TAG);
        }
        if (compound.hasKey(GLIDING_TAG)) {
            this.GLIDING = compound.getBoolean(GLIDING_TAG);
        }
    }

    @Override
    public void loadDataCache(NBTTagCompound tag) {
        if (tag != null && tag.hasKey(FIREWORK_BOOST_TICKS_TAG)) {
            this.FIREWORK_BOOST_TICKS = Math.max(0, tag.getInteger(FIREWORK_BOOST_TICKS_TAG));
        }
    }

    @Nullable
    @Override
    public NBTTagCompound sendAbilityData() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setFloat(COST_TAG, this.COST);
        tag.setFloat(LIFT_COST_TAG, this.LIFT_COST);
        tag.setDouble(LIFT_STRENGTH_TAG, this.LIFT_STRENGTH);
        tag.setBoolean(GLIDING_TAG, this.GLIDING);
        tag.setInteger(FIREWORK_BOOST_TICKS_TAG, this.FIREWORK_BOOST_TICKS);
        return tag;
    }
}
