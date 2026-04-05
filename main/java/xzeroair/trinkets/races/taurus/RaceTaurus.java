package xzeroair.trinkets.races.taurus;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.race.RaceCache;
import xzeroair.trinkets.client.races.IRenderRaceHandler;
import xzeroair.trinkets.client.races.taurus.RaceTaurusRenderer;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.taurus.config.TaurusConfig;
import xzeroair.trinkets.traits.abilities.AbilityStampede;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.DamageTypeConfigParser;
import xzeroair.trinkets.util.helpers.EntityHelper;
import xzeroair.trinkets.util.helpers.PotionHelper;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class RaceTaurus extends EntityRacePropertiesHandler {

    private final TaurusConfig CONFIG = TrinketsConfig.SERVER.RACES.TAURUS;

    public RaceTaurus(@Nonnull EntityLivingBase e, @Nonnull EntityProperties properties) {
        super(e, properties, new RaceCache(EntityRaces.taurus));
    }

    public RaceTaurus(@Nonnull EntityLivingBase e, @Nonnull EntityProperties properties, @Nonnull RaceCache raceCache) {
        super(e, properties, raceCache);
    }

    @Override
    public void startTransformation() {
        this.addAbility(new AbilityStampede(this.CONFIG.ABILITIES.STAMPEDE));
        this.addSurvivalAbilities(this.CONFIG.COMPAT.SURVIVAL);
    }

    @Override
    public void whileTransformed() {
        super.whileTransformed();
        PotionHelper.removeAllPotionEffectsFromConfig(this.getEntity(), this.CONFIG.EFFECTS_TO_REMOVE);
        PotionHelper.addAllPotionEffectsFromConfig(this.getEntity(), true, this.CONFIG.EFFECTS_TO_ADD);
        if (!this.getEntity().world.isRemote && this.getEntity().isRiding()) {
            final Entity mount = this.getEntity().getRidingEntity();
            if ((mount != null) && !this.mountEntity(mount)) {
                this.getEntity().dismountRidingEntity();
            }
        }
    }

    @Override
    public boolean isAttacked(DamageSource source, float dmg) {
        if (!this.getEntity().world.isRemote) {
            boolean result = DamageTypeConfigParser.parseDamageTypeConfig(0, source, dmg, this.raceCache.getPrimaryElement(), this.CONFIG.DAMAGE_TYPES_TO_IGNORE).getFirst();
            return !result;
        }
        return true;
    }

    @Override
    public float isHurt(DamageSource source, float dmg) {
        return dmg * DamageTypeConfigParser.parseDamageTypeConfig(1, source, dmg, this.raceCache.getPrimaryElement(), this.CONFIG.DAMAGE_TYPES_TO_IGNORE).getSecond();
    }

    @Override
    public float isDamaged(DamageSource source, float dmg) {
        return dmg * DamageTypeConfigParser.parseDamageTypeConfig(2, source, dmg, this.raceCache.getPrimaryElement(), this.CONFIG.DAMAGE_TYPES_TO_IGNORE).getSecond();
    }

    @Override
    public void endTransformation() {
        PotionHelper.removeAllPotionEffectsFromConfig(this.getEntity(), this.CONFIG.EFFECTS_TO_ADD);
    }

    @Override
    public boolean potionBeingApplied(PotionEffect effect) {
        return PotionHelper.isPotionEffect(effect, this.CONFIG.EFFECTS_TO_REMOVE);
    }

    @Override
    public boolean mountEntity(Entity mount) {
        if (EntityHelper.isCreative(this.getEntity())) {
            return true;
        } else if (!this.CONFIG.CAN_MOUNT) {
            return false;
        } else if (this.CONFIG.MOUNT_BLACKLIST.length > 0) {
            List<String> disallowedMounts = Arrays.asList(this.CONFIG.MOUNT_BLACKLIST);
            try {
                final ResourceLocation regName = EntityRegistry.getEntry(mount.getClass()).getRegistryName();
                final String modID = regName.getNamespace();
                final String entityID = regName.getPath();
                final boolean exists = disallowedMounts.contains(modID + ":*") || disallowedMounts.contains(regName.toString());
                if (exists) {
                    if (!this.CONFIG.CAN_CONTROL_BOATS && (mount instanceof EntityBoat)) {
                        final EntityBoat boat = (EntityBoat) mount;
                        final Entity controller = boat.getControllingPassenger();
                        if ((controller == null) || (controller == this.getEntity())) {
                            return false;
                        }
                    }
                    return this.CONFIG.MOUNT_WHITELIST;
                } else {
                    if (!this.CONFIG.CAN_CONTROL_BOATS && (mount instanceof EntityBoat)) {
                        final EntityBoat boat = (EntityBoat) mount;
                        final Entity controller = boat.getControllingPassenger();
                        if ((controller == null) || (controller == this.getEntity())) {
                            return false;
                        }
                    }
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
            return !this.CONFIG.MOUNT_WHITELIST;
        } else {
            if (!this.CONFIG.CAN_CONTROL_BOATS && (mount instanceof EntityBoat)) {
                final EntityBoat boat = (EntityBoat) mount;
                final Entity controller = boat.getControllingPassenger();
                return (controller != null) && (controller != this.getEntity());
            }
            return true;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IRenderRaceHandler getRaceRenderer() {
        if (this.RendererRace == null) {
            this.RendererRace = new RaceTaurusRenderer(this.getEntity(), this);
        }
        return this.RendererRace;
    }
}
