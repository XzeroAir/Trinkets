package xzeroair.trinkets.traits.abilities;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.traits.abilities.interfaces.IMiningAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IToggleAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityFlight;
import xzeroair.trinkets.util.handlers.Counter;
import xzeroair.trinkets.util.helpers.PotionHelper;

import javax.annotation.Nullable;

public class AbilityCreativeFlight extends Ability implements ITickableAbility, IPotionAbility, IMiningAbility, IToggleAbility {

    protected ConfigAbilityFlight CONFIG;
    protected float COST;
    protected boolean SELF_ADDED, TOGGLED;

    public AbilityCreativeFlight() {
        this(TrinketsConfig.SERVER.ABILITIES.CREATIVE_FLIGHT);
    }

    public AbilityCreativeFlight(ConfigAbilityFlight config) {
        super(TrinketsRegistryNames.ModAbilities.CREATIVE_FLIGHT);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
        this.setFlightCost(config.FLY_COST);
        this.SELF_ADDED = false;
        this.TOGGLED = false;
    }

    public AbilityCreativeFlight setFlightCost(float cost) {
        if (this.COST != cost) {
            this.COST = cost;
        }
        return this;
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            // TODO Fix this so it's not reliant on the Race capability
            final boolean flag = Capabilities.getEntityProperties(player, true, (prop, canFly) -> prop.getRaceHandler().canFly());
            if (flag && player.ticksExisted > 20) {
                this.addFlyingAbility(player);
            } else {
                this.removeCreativeFlight(player);
            }
        }
    }

    @Override
    public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
        if (effect.getPotion().getRegistryName().toString().contentEquals("minecraft:levitation")) {
            return true;
        }
        return cancel;
    }

    @Override
    public float breakingBlock(EntityLivingBase entity, IBlockState state, BlockPos pos, float originalSpeed, float newSpeed) {
        if (!entity.isInsideOfMaterial(Material.WATER)) {
            float speed = originalSpeed;
            if (!entity.onGround) {
                speed *= 5F;
            }
            if (newSpeed < speed) {
                return speed;
            }
        }
        return newSpeed;
    }

    protected void addFlyingAbility(EntityPlayer player) {
        if (this.isCreativePlayer(player)) {
            return;
        }

        if (this.COST <= 0) {
            this.giveCreativeFlight(player);
            if (this.SELF_ADDED && player.capabilities.isFlying) {
                player.fallDistance = 0F;
            }
        } else {
            final MagicStats magic = Capabilities.getMagicStats(player);
            if (magic != null) {
                final float mp = magic.getMana();
                if (mp >= this.COST) {
                    this.giveCreativeFlight(player);
                } else {
                    if (this.SELF_ADDED) {
                        this.removeCreativeFlight(player);
                        final Counter counter = this.tickHandler.getCounter("fly_timer", 20, true, true, true, true);
                        if ((counter != null)) {
                            counter.resetTick();
                        }
                    }
                    return;
                }
                if (PotionHelper.isModPotionActive(player, "potioncore", "flight")) {
                    return;
                }
                if (this.SELF_ADDED && player.capabilities.isFlying) {
                    player.fallDistance = 0F;
                    if (!player.isRiding()) {
                        final Counter counter = this.tickHandler.getCounter("fly_timer", 20, true, true, true, true);
                        if ((counter != null) && counter.Tick()) {
                            if (!magic.spendMana(this.COST)) {
                                this.removeCreativeFlight(player);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onAbilityRemoved(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) entity;
            this.removeCreativeFlight(player);
        }
        this.tickHandler.removeCounter("fly_timer");
    }

    private void removeCreativeFlight(EntityPlayer player) {
        if (this.isCreativePlayer(player)) {
            return;
        }
        if (PotionHelper.isModPotionActive(player, "potioncore", "flight")) {
            return;
        }
        if (!player.world.isRemote) {
            if (player.capabilities.allowFlying) {
                this.SELF_ADDED = false;
                player.capabilities.allowFlying = false;
                if (player.capabilities.isFlying) {
                    player.fallDistance = 0F;
                    player.capabilities.isFlying = false;
                }
                player.sendPlayerAbilities();
            }
        }
    }

    private void giveCreativeFlight(EntityPlayer player) {
        if (!player.capabilities.allowFlying) {
            this.SELF_ADDED = true;
            player.capabilities.allowFlying = true;
            if (!player.world.isRemote) {
                player.sendPlayerAbilities();
            }
        }
    }

    @Override
    public boolean isAbilityToggled() {
        return this.TOGGLED;
    }

    @Override
    public int getToggleMode() {
        return -1;
    }

    @Override
    public IToggleAbility toggleAbility(boolean enabled) {
        if (this.TOGGLED != enabled) {
            this.TOGGLED = enabled;
            this.setChanged(true);
        }
        return this;
    }

    @Override
    public IToggleAbility toggleAbility(int value) {
        return this;
    }

    @Override
    public void loadStorage(NBTTagCompound compound) {
        super.loadStorage(compound);
        if (compound.hasKey("COST")) {
            this.COST = compound.getFloat("COST");
        }
    }

    @Nullable
    @Override
    public NBTTagCompound sendAbilityData() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setFloat("COST", this.COST);
        return tag;
    }
}
