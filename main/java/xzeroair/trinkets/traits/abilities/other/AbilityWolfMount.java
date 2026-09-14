package xzeroair.trinkets.traits.abilities.other;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.entity.EntityAlphaWolf;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.traits.abilities.interfaces.IKeyBindInterface;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityWolfRider;
import xzeroair.trinkets.util.handlers.Counter;
import xzeroair.trinkets.util.helpers.RayTraceHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class AbilityWolfMount extends Ability implements ITickableAbility, IKeyBindInterface {

    protected final ConfigAbilityWolfRider CONFIG;

    public AbilityWolfMount() {
        this(TrinketsConfig.SERVER.ABILITIES.WOLF_RIDER);
    }

    public AbilityWolfMount(ConfigAbilityWolfRider config) {
        super(TrinketsRegistryNames.ModAbilities.WOLF_RIDER);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        final TranslationHelper.KeyEntry raceKey = new TranslationHelper.KeyBindEntry("racekb", this.getKey());
        return helper.formatAddVariables(key, renderID, raceKey);
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        final Counter counter = tickHandler.getCounter("mountAtkCooldown", 40, true, true, false, true, false);
        if ((counter != null)) {
            if ((counter.getTick() > 0)) {
                counter.Tick();
            }
        }
    }

    @Override
    public boolean onKeyPress(Entity entity, boolean Aux) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) entity;
            final World world = player.getEntityWorld();
            double reachDistance = 5;
            final IAttributeInstance reachAttribe = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
            if (reachAttribe != null) {
                reachDistance = reachAttribe.getAttributeValue();
            }
            if (player.isRiding() && (player.getRidingEntity() instanceof EntityAlphaWolf)) {
                final Counter counter = tickHandler.getCounter("mountAtkCooldown", 40, true, true, false, true, false);
                final EntityAlphaWolf wolf = (EntityAlphaWolf) player.getRidingEntity();
                if (counter.Tick()) {
                    wolf.MountedAttack(player, reachDistance);
                    counter.resetTick();
                }
            } else if (!player.isRiding()) {
                RayTraceResult result = RayTraceHelper.rayTrace(world, player, reachDistance * 0.5D, true);
                if ((result != null) && (result.typeOfHit == Type.ENTITY)) {
                    if ((result.entityHit instanceof EntityWolf) && !(result.entityHit instanceof EntityAlphaWolf)) {
                        this.MountWolf(player, (EntityWolf) result.entityHit);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean onKeyDown(Entity entity, boolean Aux) {
        return true;
    }

    @Override
    public boolean onKeyRelease(Entity entity, boolean Aux) {
        return true;
    }

    public boolean MountWolf(EntityLivingBase entity, EntityWolf wolf) {
        final World world = entity.getEntityWorld();
        final boolean isOwner = (wolf.isTamed() && wolf.isOwner(entity));
        if (!isOwner || wolf.isChild() || wolf.isDead || !wolf.isEntityAlive()) {
            return false;
        }
        if (!world.isRemote) {
            if (!entity.isRiding()) {
                final EntityAlphaWolf newWolf = new EntityAlphaWolf(world);
                newWolf.setCustomNameTag(wolf.getCustomNameTag());
                newWolf.setLocationAndAngles(wolf.posX, wolf.posY, wolf.posZ, wolf.rotationYaw, 0F);
                newWolf.setTamedBy(entity);
                newWolf.setHealth(wolf.getHealth());
                if (world.spawnEntity(newWolf)) {
                    newWolf.getEntityData().setBoolean("xat:summoned", true);
                    if (entity.startRiding(newWolf)) {
                        final NBTTagCompound tag = new NBTTagCompound();
                        wolf.writeToNBT(tag);
                        tag.setString("id", EntityList.getKey(wolf.getClass()).toString());
                        newWolf.storeOldWolf(tag);
                        wolf.setDead();
                        return true;
                    }
                    newWolf.setDead();
                }
            }
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getKey() {
        return ModKeyBindings.RACE_ABILITY.getDisplayName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getAuxKey() {
        return ModKeyBindings.AUX_KEY.getDisplayName();
    }
}
