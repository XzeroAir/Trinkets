package xzeroair.trinkets.traits.abilities;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.traits.abilities.interfaces.IMiningAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.handlers.Counter;
import xzeroair.trinkets.util.helpers.PotionHelper;

public class AbilityFlying extends Ability implements ITickableAbility, IPotionAbility, IMiningAbility {

    protected float cost = 0F;
    protected boolean selfAdded = false;

    public AbilityFlying() {
        super(Abilities.creativeFlight);
    }

    public AbilityFlying setFlightCost(float cost) {
        if (this.cost != cost) {
            this.cost = cost;
        }
        return this;
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (!this.isCreativePlayer(player)) {
                // TODO Fix this so it's not reliant on the Race capability
                final boolean flag = Capabilities.getEntityProperties(player, true, (prop, canFly) -> prop.getRaceHandler().canFly());
                if (flag) {
                    this.addFlyingAbility(player);
                } else {
                    this.removeCreativeFlight(player);
                }
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

        if (cost <= 0) {
            this.giveCreativeFlight(player);
            if (selfAdded && player.capabilities.isFlying) {
                player.fallDistance = 0F;
            }
        } else {
            final MagicStats magic = Capabilities.getMagicStats(player);
            if (magic != null) {
                final float mp = magic.getMana();
                if (mp >= cost) {
                    this.giveCreativeFlight(player);
                } else {
                    if (selfAdded) {
                        this.removeCreativeFlight(player);
                        final Counter counter = tickHandler.getCounter("fly_timer", 20, true, true, true, true);
                        if ((counter != null)) {
                            counter.resetTick();
                        }
                    }
                    return;
                }
                if (PotionHelper.isModPotionActive(player, "potioncore", "flight")) {
                    return;
                }
                if (selfAdded && player.capabilities.isFlying) {
                    player.fallDistance = 0F;
                    if (!player.isRiding()) {
                        final Counter counter = tickHandler.getCounter("fly_timer", 20, true, true, true, true);
                        if ((counter != null) && counter.Tick()) {
                            if (!magic.spendMana(cost)) {
                                this.removeCreativeFlight(player);
                                //							} else {
                                //		magic.setManaRegenTimeout(TrinketsConfig.SERVER.mana.mana_regen_timeout * 3);
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
            if (!this.isCreativePlayer(player)) {
                this.removeCreativeFlight(player);
            }
        }
        tickHandler.removeCounter("fly_timer");
    }

    private void removeCreativeFlight(EntityPlayer player) {
        if (PotionHelper.isModPotionActive(player, "potioncore", "flight")) {
            return;
        }
        if (player instanceof EntityPlayerMP) {
            if (player.capabilities.allowFlying) {
                selfAdded = false;
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
        if (player instanceof EntityPlayerMP) {
            if (!player.capabilities.allowFlying) {
                selfAdded = true;
                player.capabilities.allowFlying = true;
                player.sendPlayerAbilities();
            }
        }
    }
}
