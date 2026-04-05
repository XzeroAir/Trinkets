package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.statushandler.StatusHandler;
import xzeroair.trinkets.capabilities.statushandler.TrinketStatusEffect;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.particles.EffectsRenderPacket;
import xzeroair.trinkets.traits.abilities.interfaces.IMovementAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.traits.statuseffects.StatusEffectsEnum;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityDodge;
import xzeroair.trinkets.util.handlers.Counter;
import xzeroair.trinkets.util.helpers.TranslationHelper;

import javax.annotation.Nonnull;
import java.util.List;

public class AbilityDodge extends Ability implements ITickableAbility, IMovementAbility {

    protected int keyPresses = 0;
    protected int direction = -1;
    protected Long lastKeyPress = -1L;
    protected boolean trigger;

    protected ConfigAbilityDodge CONFIG;

    public AbilityDodge() {
        this(TrinketsConfig.SERVER.ABILITIES.DODGE);
    }

    public AbilityDodge(@Nonnull ConfigAbilityDodge config) {
        super(TrinketsRegistryNames.ModAbilities.DODGING);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(@Nonnull TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        String lang = this.getTranslationKey();
        final TranslationHelper.KeyEntry key1 = new TranslationHelper.LangEntry(lang, "dodge", !(TrinketsConfig.getClientStore().MOD_COMPAT_ELENAI_DODGE));
        final TranslationHelper.KeyEntry key2 = new TranslationHelper.LangEntry(lang, "dodge.stun", this.CONFIG.STUNS);
        final TranslationHelper.KeyEntry key3 = new TranslationHelper.OptionEntry("dodgecost", true, this.CONFIG.COST);
        return helper.formatAddVariables(key, renderID, key1, key2, key3);
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        final Counter counter = this.tickHandler.getCounter("lastKeyPress");
        if (counter != null) {
            if (counter.Tick()) {
                this.reset();
            }
        }
        //		long time = System.currentTimeMillis();
        //		if (time > (lastKeyPress + 200)) {
        //		trigger = false;
        //		if (!entity.onGround) {
        //			this.reset();
        //		}
    }

    public void handleDodge(Entity entity, int direction) {
        if (entity == null || !entity.onGround) {
            return;
        }
        if (!entity.world.isRemote) {
            Vec3d look = entity.getLookVec();
            Vec3d flatLook = new Vec3d(look.x, 0, look.z);
            if (flatLook.lengthSquared() < 1.0E-6) {
                return;
            }
            flatLook = flatLook.normalize();
            Vec3d left = new Vec3d(flatLook.z, 0, -flatLook.x);
            Vec3d right = new Vec3d(-flatLook.z, 0, flatLook.x);

            final double spd = 1.25;
            Vec3d motion;
            switch (direction) {
                case 1: // Left
                    motion = left.scale(spd);
                    break;
                case 3: // Right
                    motion = right.scale(spd);
                    break;
                case 2: // Forward
                    motion = flatLook.scale(spd);
                    break;
                case 0: // Backward
                    motion = flatLook.scale(-spd);
                    break;
                default:
                    return;
            }
            double yBoost = 0.3;
            entity.motionX = motion.x;
            entity.motionY = yBoost;
            entity.motionZ = motion.z;
            entity.velocityChanged = true;
        }
        this.dodge(entity);
    }

    public void dodge(Entity entity) {
        if (!entity.getEntityWorld().isRemote) {
            final World w = entity.getEntityWorld();
            if (w instanceof WorldServer) {
                final WorldServer world = (WorldServer) w;
                NetworkHandler.sendToClients(world, entity.getPosition(), new EffectsRenderPacket(entity, entity.posX, entity.posY + (entity.height * 0.5F), entity.posZ, entity.posX, entity.posY, entity.posZ, 12648447, 2, 0.8F, 1));
            }
        }
        final double distance = this.CONFIG.STUN_RADIUS;
        final List<EntityLivingBase> stunTargets = entity.world.getEntitiesWithinAABB(EntityLivingBase.class, entity.getEntityBoundingBox().grow(distance, 1, distance));
        for (final EntityLivingBase targetEntity : stunTargets) {
            if (targetEntity != entity) {
                final StatusHandler status = Capabilities.getStatusHandler(targetEntity);
                if (status != null) {
                    final TrinketStatusEffect effect = new TrinketStatusEffect(StatusEffectsEnum.paralysis, 3 * 20, 1, entity);
                    status.apply(effect);
                }
            }
        }
    }

    public boolean triggerDodge(Entity entity) {
        if (this.trigger) {
            this.trigger = false;
            if (!entity.onGround || entity.isSneaking()) {
                return false;
            }
            if (Trinkets.MOD_COMPAT.ElenaiDodge1 && TrinketsConfig.getClientStore().MOD_COMPAT_ELENAI_DODGE) {
                return false;
            }
            return Capabilities.getMagicStats(entity, true, (magic, rtn) -> magic.spendMana(this.CONFIG.COST));
        } else {
            return false;
        }
    }

    @Override
    public boolean left(Entity entity, int state) {
        if (state == 0) {
            if (this.handleKeys(EnumDirection.Left)) {
                if (this.triggerDodge(entity)) {
                    this.handleDodge(entity, 1);
                } else {
                    return false;
                }
            }
            return true;
        } else if (state == 1) {
            return true;
        } else {
            return true;
        }
    }

    @Override
    public boolean right(Entity entity, int state) {
        if (state == 0) {
            if (this.handleKeys(EnumDirection.Right)) {
                if (this.triggerDodge(entity)) {
                    this.handleDodge(entity, 3);
                } else {
                    return false;
                }
            }
            return true;
        } else if (state == 1) {
            return true;
        } else {
            return true;
        }
    }

    @Override
    public boolean forward(Entity entity, int state) {
        if (state == 0) {
            if (this.handleKeys(EnumDirection.Forward)) {
                if (this.triggerDodge(entity)) {
                    this.handleDodge(entity, 2);
                } else {
                    return false;
                }
            }
            return true;
        } else if (state == 1) {
            return true;
        } else {
            return true;
        }
    }

    @Override
    public boolean back(Entity entity, int state) {
        if (state == 0) {
            if (this.handleKeys(EnumDirection.Back)) {
                if (this.triggerDodge(entity)) {
                    this.handleDodge(entity, 0);
                } else {
                    return false;
                }
            }
            return true;
        } else if (state == 1) {
            return true;
        } else {
            return true;
        }
    }

    @Override
    public boolean jump(Entity entity, int state) {
        return false;
    }

    @Override
    public boolean sneak(Entity entity, int state) {
        return false;
    }

    private void reset() {
        this.trigger = false;
        this.keyPresses = 0;
        this.lastKeyPress = -1L;
        this.direction = -1;
        this.tickHandler.removeCounter("lastKeyPress");
    }

    private boolean handleKeys(int direction) {
        if (this.direction != direction) {
            this.reset();
        }
        final Counter counter = this.tickHandler.getCounter("lastKeyPress");
        if (counter != null) {
            this.keyPresses++;
        } else {
            final Counter newCounter = this.tickHandler.getCounter("lastKeyPress", 3, true, true, true, false);
            this.direction = direction;
            this.keyPresses = 1;
        }
        if (this.keyPresses >= 2) {
            this.reset();
            this.trigger = true;
        }
        return this.trigger;
    }

    private boolean handleKeys(EnumDirection direction) {
        return this.handleKeys(direction.getID());
    }

    private enum EnumDirection {

        Left(1), Right(3), Forward(2), Back(0), Up(4), Down(5);

        private final int ID;

        EnumDirection(int direction) {
            this.ID = direction;
        }

        public int getID() {
            return this.ID;
        }
    }

}
