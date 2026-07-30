package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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

    protected ConfigAbilityElytraFlight CONFIG;
    protected float COST, LIFT_COST;
    protected double LIFT_STRENGTH;
    protected boolean GLIDING, LIFT_ENABLED, LIFT_REQUESTED, VANILLA_FLIGHT_ACTIVE;
    protected int VANILLA_FLIGHT_EXIT_TICKS, FIREWORK_BOOST_TICKS;

    public AbilityElytraFlight() {
        this(TrinketsConfig.SERVER.ABILITIES.ELYTRA_FLIGHT);
    }

    public AbilityElytraFlight(ConfigAbilityElytraFlight config) {
        super(TrinketsRegistryNames.ModAbilities.ELYTRA_FLIGHT);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
        this.setFlightCost(config.COST);
        this.setLiftEnabled(config.LIFT_ENABLED);
        this.setLiftCost(config.LIFT_COST);
        this.setLiftStrength(config.LIFT_STRENGTH);
        this.GLIDING = false;
        this.LIFT_REQUESTED = false;
        this.VANILLA_FLIGHT_ACTIVE = false;
        this.VANILLA_FLIGHT_EXIT_TICKS = 0;
    }

    public AbilityElytraFlight setFlightCost(float cost) {
        if (this.COST != cost) {
            this.COST = cost;
        }
        return this;
    }


    public AbilityElytraFlight setLiftCost(float cost) {
        if (this.LIFT_COST != cost) {
            this.LIFT_COST = cost;
        }
        return this;
    }

    public AbilityElytraFlight setLiftEnabled(boolean enabled) {
        if (this.LIFT_ENABLED != enabled) {
            this.LIFT_ENABLED = enabled;
        }
        return this;
    }

    public AbilityElytraFlight setLiftStrength(double strength) {
        if (this.LIFT_STRENGTH != strength) {
            this.LIFT_STRENGTH = strength;
        }
        return this;
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
    public void tickAbility(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            this.tickFlight((EntityPlayer) entity);
        }
    }

    @Override
    public boolean jump(Entity entity, int state) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) entity;
            if (state == 0) {
                if (this.GLIDING) {
                    if (!player.world.isRemote) {
                        this.LIFT_REQUESTED = true;
                    }
                } else if (!this.VANILLA_FLIGHT_ACTIVE && this.VANILLA_FLIGHT_EXIT_TICKS == 0) {
                    this.tryStartFlight(player);
                }
            }
        }
        return true;
    }

    @Override
    public void rightClickWithItem(EntityLivingBase entity, World world, ItemStack stack, EnumHand hand, EnumFacing face, BlockPos pos) {
        if (!(entity instanceof EntityPlayer) || world.isRemote || !this.GLIDING || stack.getItem() != Items.FIREWORKS) {
            return;
        }
        final EntityPlayer player = (EntityPlayer) entity;
        if (this.isUsingVanillaFlight(player)) {
            return;
        }
        this.FIREWORK_BOOST_TICKS = Math.max(this.FIREWORK_BOOST_TICKS, fireworkLifetime(stack, player));
        this.setChanged(true);
        world.spawnEntity(new EntityFireworkRocket(world, stack.copy(), player));
        if (!player.capabilities.isCreativeMode) {
            stack.shrink(1);
        }
    }

    protected void applyFireworkBoost(EntityPlayer player) {
        if (this.FIREWORK_BOOST_TICKS <= 0) {
            return;
        }
        this.FIREWORK_BOOST_TICKS--;
        final Vec3d look = player.getLookVec();
        player.motionX += look.x * 0.1D + (look.x * 1.5D - player.motionX) * 0.5D;
        player.motionY += look.y * 0.1D + (look.y * 1.5D - player.motionY) * 0.5D;
        player.motionZ += look.z * 0.1D + (look.z * 1.5D - player.motionZ) * 0.5D;
        player.velocityChanged = true;
    }

    protected int fireworkLifetime(ItemStack stack, EntityPlayer player) {
        final NBTTagCompound fireworks = stack.hasTagCompound() ? stack.getTagCompound().getCompoundTag("Fireworks") : null;
        final int flight = fireworks == null ? 0 : fireworks.getByte("Flight");
        return 10 * (1 + flight) + player.getRNG().nextInt(6) + player.getRNG().nextInt(7);
    }

    @Override
    public float fallDistance(EntityLivingBase entity, float distance) {
        if (this.GLIDING) {
            return 0F;
        }
        return distance;
    }

    @Override
    public boolean fall(EntityLivingBase entity, float distance, float multiplier, boolean cancel) {
        return cancel || this.GLIDING;
    }

    @Override
    public void onAbilityRemoved(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            this.stopFlight((EntityPlayer) entity);
        }
        this.LIFT_REQUESTED = false;
        this.VANILLA_FLIGHT_ACTIVE = false;
        this.VANILLA_FLIGHT_EXIT_TICKS = 0;
        this.tickHandler.removeCounter("elytra_flight_cost");
    }

    protected void tickFlight(EntityPlayer player) {
        if (this.isUsingVanillaFlight(player)) {
            this.VANILLA_FLIGHT_ACTIVE = true;
            this.VANILLA_FLIGHT_EXIT_TICKS = 0;
            this.LIFT_REQUESTED = false;
            this.stopFlight(player);
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
        if (!this.canKeepFlying(player) || !this.spendFlightCost(player)) {
            this.stopFlight(player);
            this.LIFT_REQUESTED = false;
            return;
        }
        this.applyElytraMotion(player);
        this.applyFireworkBoost(player);
        if (this.LIFT_REQUESTED) {
            this.LIFT_REQUESTED = false;
            final boolean liftApplied = this.LIFT_ENABLED && this.spendLiftCost(player);
            if (liftApplied) {
                player.motionY += this.LIFT_STRENGTH;
                player.velocityChanged = true;
            }
        }
        player.fallDistance = 0F;
    }

    protected void tryStartFlight(EntityPlayer player) {
        if (!this.canStartFlying(player) || !this.hasManaForFlight(player)) {
            return;
        }
        this.startFlight(player);
    }

    protected void startFlight(EntityPlayer player) {
        if (!this.GLIDING) {
            this.GLIDING = true;
            this.setChanged(true);
        }
        player.fallDistance = 0F;
    }

    protected void stopFlight(EntityPlayer player) {
        if (this.GLIDING) {
            this.GLIDING = false;
            this.setChanged(true);
        }
    }

    protected boolean canStartFlying(EntityPlayer player) {
        return !this.isUsingVanillaFlight(player) && this.canKeepFlying(player) && !this.GLIDING && (player.motionY < 0D);
    }

    protected boolean canKeepFlying(EntityPlayer player) {
        return !player.onGround && !player.isRiding() && !player.isInWater() && !player.isInLava();
    }

    protected boolean isUsingVanillaFlight(EntityPlayer player) {
        return player.capabilities.isFlying || player.isElytraFlying();
    }


    protected boolean hasManaForFlight(EntityPlayer player) {
        if (this.COST <= 0) {
            return true;
        }
        final MagicStats magic = Capabilities.getMagicStats(player);
        return (magic != null) && (magic.getMana() >= this.COST);
    }

    protected boolean spendFlightCost(EntityPlayer player) {
        if (this.COST <= 0 || player.world.isRemote || player.isRiding()) {
            return true;
        }
        final MagicStats magic = Capabilities.getMagicStats(player);
        if (magic == null) {
            return false;
        }
        final Counter counter = this.tickHandler.getCounter("elytra_flight_cost", 20, true, true, true, true);
        return (counter == null) || !counter.Tick() || magic.spendMana(this.COST);
    }

    protected boolean spendLiftCost(EntityPlayer player) {
        if (this.LIFT_COST <= 0 || player.world.isRemote) {
            return true;
        }
        final MagicStats magic = Capabilities.getMagicStats(player);
        if (magic == null) {
            return false;
        }
        return magic.spendMana(this.LIFT_COST);
    }

    protected void applyElytraMotion(EntityPlayer player) {
        this.restoreNormalAirMotion(player);
        final Vec3d look = player.getLookVec();
        final float pitch = player.rotationPitch * 0.017453292F;
        final double horizontalSpeed = Math.sqrt((player.motionX * player.motionX) + (player.motionZ * player.motionZ));
        final double lookHorizontal = Math.sqrt((look.x * look.x) + (look.z * look.z));
        final double lookLength = look.length();
        final float pitchCos = MathHelper.cos(pitch);
        final float lift = (float) ((double) pitchCos * (double) pitchCos * Math.min(1.0D, lookLength / 0.4D));

        player.motionY += -0.08D + (lift * 0.06D);
        if ((player.motionY < 0.0D) && (lookHorizontal > 0.0D)) {
            final double glidePull = player.motionY * -0.1D * lift;
            player.motionY += glidePull;
            player.motionX += (look.x * glidePull) / lookHorizontal;
            player.motionZ += (look.z * glidePull) / lookHorizontal;
        }
        if ((pitch < 0.0F) && (lookHorizontal > 0.0D)) {
            final double climbPull = horizontalSpeed * (double) (-MathHelper.sin(pitch)) * 0.04D;
            player.motionY += climbPull * 3.2D;
            player.motionX -= (look.x * climbPull) / lookHorizontal;
            player.motionZ -= (look.z * climbPull) / lookHorizontal;
        }
        if (lookHorizontal > 0.0D) {
            player.motionX += (((look.x / lookHorizontal) * horizontalSpeed) - player.motionX) * 0.1D;
            player.motionZ += (((look.z / lookHorizontal) * horizontalSpeed) - player.motionZ) * 0.1D;
        }
        player.motionX *= 0.99D;
        player.motionY *= 0.98D;
        player.motionZ *= 0.99D;
        player.velocityChanged = true;
    }

    protected void restoreNormalAirMotion(EntityPlayer player) {
        player.motionX /= 0.91D;
        player.motionY = (player.motionY / 0.98D) + 0.08D;
        player.motionZ /= 0.91D;
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
