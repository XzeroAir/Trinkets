package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.init.ModPotionTypes;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.particles.EffectsRenderPacket;
import xzeroair.trinkets.traits.abilities.interfaces.IMovementAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityDodge;
import xzeroair.trinkets.util.handlers.Counter;
import xzeroair.trinkets.util.helpers.TranslationHelper;

import javax.annotation.Nullable;
import javax.annotation.Nonnull;

public class AbilityDodge extends Ability implements ITickableAbility, IMovementAbility {

    protected static final int DIRECTION_BACK = 0;
    protected static final int DIRECTION_LEFT = 1;
    protected static final int DIRECTION_FORWARD = 2;
    protected static final int DIRECTION_RIGHT = 3;
    protected static final String PRESSES_TAG = "Presses";
    protected static final String DIRECTION_TAG = "Direction";

    protected int keyPresses = 0;
    protected int direction = -1;
    protected int lastDodgeTick = -1;

    protected final ConfigAbilityDodge CONFIG;

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
        if ((counter != null) && counter.Tick()) {
            this.resetDodgePresses();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getKey() {
        return this.CONFIG.CLIENT.KEYBIND_MOVEMENT ? ModKeyBindings.ARCING_ORB_DODGE.getDisplayName() : "";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public NBTTagCompound createMovementPayload(Entity entity, int primaryState, boolean primaryDown, boolean auxiliaryDown, int left, int right, int forward, int back, int jump, int sneak) {
        final int direction = this.selectDodgeDirection(primaryState, left, right, forward, back);
        if (direction < 0) {
            return null;
        }
        final NBTTagCompound payload = new NBTTagCompound();
        final boolean keybindMode = this.CONFIG.CLIENT.KEYBIND_MOVEMENT;
        payload.setInteger(PRESSES_TAG, keybindMode ? (primaryDown ? 2 : 0) : 1);
        payload.setInteger(DIRECTION_TAG, direction);
        return payload;
    }

    protected void applyDodge(Entity entity, Vec3d motion) {
        entity.motionX = motion.x;
        entity.motionY = 0.3;
        entity.motionZ = motion.z;
        entity.velocityChanged = true;
        this.applyDodgeEffects(entity);
    }

    @Nullable
    protected Vec3d getDodgeMotion(Entity entity, int direction) {
        if (entity.world.isRemote) {
            return null;
        }
        final Vec3d look = entity.getLookVec();
        final Vec3d flatLook = new Vec3d(look.x, 0, look.z);
        if (flatLook.lengthSquared() < 1.0E-6) {
            return null;
        }
        final Vec3d normalizedLook = flatLook.normalize();
        final double speed = 1.25;
        switch (direction) {
            case DIRECTION_LEFT:
                return new Vec3d(normalizedLook.z, 0, -normalizedLook.x).scale(speed);
            case DIRECTION_RIGHT:
                return new Vec3d(-normalizedLook.z, 0, normalizedLook.x).scale(speed);
            case DIRECTION_FORWARD:
                return normalizedLook.scale(speed);
            case DIRECTION_BACK:
                return normalizedLook.scale(-speed);
            default:
                return null;
        }
    }

    public void applyDodgeEffects(Entity entity) {
        if (entity.world.isRemote) {
            return;
        }
        final World world = entity.getEntityWorld();
        if (world instanceof WorldServer) {
            NetworkHandler.sendToClients((WorldServer) world, entity.getPosition(), new EffectsRenderPacket(entity, entity.posX, entity.posY + (entity.height * 0.5F), entity.posZ, entity.posX, entity.posY, entity.posZ, 12648447, 2, 0.8F, 1));
        }
        if (!this.CONFIG.STUNS) {
            return;
        }
        final double distance = this.CONFIG.STUN_RADIUS;
        final Potion potion = ModPotionTypes.TrinketPotions.get(ModPotionTypes.paralysis);
        if (potion == null) {
            return;
        }
        for (final EntityLivingBase targetEntity : entity.world.getEntitiesWithinAABB(EntityLivingBase.class, entity.getEntityBoundingBox().grow(distance, 1, distance))) {
            if (targetEntity != entity) {
                final PotionEffect activeEffect = targetEntity.getActivePotionEffect(potion);
                final int duration = 3 * 20;
                if ((activeEffect == null) || (activeEffect.getDuration() < duration)) {
                    targetEntity.addPotionEffect(new PotionEffect(potion, duration, 0, false, false));
                }
            }
        }
    }

    protected boolean tryDodge(Entity entity, int direction) {
        if (!this.getAbilityHolder().getHandler().getParentProperties().isGrounded() || entity.isSneaking()) {
            return false;
        }
        if (Trinkets.MOD_COMPAT.ElenaiDodge1 && TrinketsConfig.getClientStore().MOD_COMPAT_ELENAI_DODGE) {
            return false;
        }
        if (entity.world.isRemote) {
            return Capabilities.getMagicStats(entity, false, (magic, rtn) -> magic.canSpendMana(this.CONFIG.COST));
        }
        if ((this.CONFIG.COOLDOWN > 0) && (this.lastDodgeTick >= 0) && ((entity.ticksExisted - this.lastDodgeTick) < this.CONFIG.COOLDOWN)) {
            return false;
        }
        final Vec3d motion = this.getDodgeMotion(entity, direction);
        if ((motion == null) || !Capabilities.getMagicStats(entity, true, (magic, rtn) -> magic.spendMana(this.CONFIG.COST))) {
            return false;
        }
        this.lastDodgeTick = entity.ticksExisted;
        this.applyDodge(entity, motion);
        return true;
    }

    @Override
    public boolean onMovement(Entity entity, int primaryState, boolean primaryDown, boolean auxiliaryDown, int left, int right, int forward, int back, int jump, int sneak, @Nullable NBTTagCompound payload) {
        final int direction = this.readDodgeDirection(payload);
        final int state = this.getDirectionState(direction, left, right, forward, back);
        final int presses = this.readDodgePresses(payload, direction);
        if (((state != 0) && (primaryState != 0)) || (presses <= 0) || !this.recordDodgePress(direction, presses)) {
            return true;
        }
        return this.tryDodge(entity, direction);
    }

    protected int selectDodgeDirection(int primaryState, int left, int right, int forward, int back) {
        if ((left == 0) || ((primaryState == 0) && (left == 1))) {
            return DIRECTION_LEFT;
        }
        if ((right == 0) || ((primaryState == 0) && (right == 1))) {
            return DIRECTION_RIGHT;
        }
        if ((forward == 0) || ((primaryState == 0) && (forward == 1))) {
            return DIRECTION_FORWARD;
        }
        if ((back == 0) || ((primaryState == 0) && (back == 1))) {
            return DIRECTION_BACK;
        }
        return -1;
    }

    protected int readDodgeDirection(@Nullable NBTTagCompound payload) {
        if ((payload == null) || !payload.hasKey(DIRECTION_TAG, 99)) {
            return -1;
        }
        final int direction = payload.getInteger(DIRECTION_TAG);
        return (direction >= DIRECTION_BACK) && (direction <= DIRECTION_RIGHT) ? direction : -1;
    }

    protected int getDirectionState(int direction, int left, int right, int forward, int back) {
        switch (direction) {
            case DIRECTION_LEFT:
                return left;
            case DIRECTION_RIGHT:
                return right;
            case DIRECTION_FORWARD:
                return forward;
            case DIRECTION_BACK:
                return back;
            default:
                return -1;
        }
    }

    protected int readDodgePresses(@Nullable NBTTagCompound payload, int direction) {
        if ((payload == null) || !payload.hasKey(PRESSES_TAG, 99) || (this.readDodgeDirection(payload) != direction)) {
            return 0;
        }
        final int presses = payload.getInteger(PRESSES_TAG);
        return (presses >= 0) && (presses <= 2) ? presses : 0;
    }

    protected void resetDodgePresses() {
        this.keyPresses = 0;
        this.direction = -1;
        this.tickHandler.removeCounter("lastKeyPress");
    }

    protected boolean recordDodgePress(int direction, int presses) {
        if (this.direction != direction) {
            this.resetDodgePresses();
        }
        final Counter counter = this.tickHandler.getCounter("lastKeyPress");
        if (counter == null) {
            this.tickHandler.getCounter("lastKeyPress", 3, true, true, true, false);
            this.direction = direction;
            this.keyPresses = presses;
        } else {
            this.keyPresses += presses;
        }
        if (this.keyPresses < 2) {
            return false;
        }
        this.resetDodgePresses();
        return true;
    }
}
