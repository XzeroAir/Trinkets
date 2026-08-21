package xzeroair.trinkets.races.elf;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.attributes.UpdatingAttribute;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.race.RaceCache;
import xzeroair.trinkets.client.races.IRenderRaceHandler;
import xzeroair.trinkets.client.races.elf.RaceElfRenderer;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.elf.config.ElfConfig;
import xzeroair.trinkets.traits.abilities.AbilitySkilledArcher;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.Utils;
import xzeroair.trinkets.util.helpers.AttributeHelper;
import xzeroair.trinkets.util.helpers.DamageTypeConfigParser;
import xzeroair.trinkets.util.helpers.EntityHelper;
import xzeroair.trinkets.util.helpers.PotionHelper;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class RaceElf extends EntityRacePropertiesHandler {

    private final ElfConfig CONFIG = TrinketsConfig.SERVER.RACES.ELF;

    protected final UpdatingAttribute bonusSpeed, bonusAtkSpeed, jump;

    public RaceElf(@Nonnull EntityLivingBase e, @Nonnull EntityProperties properties) {
        super(e, properties, new RaceCache(EntityRaces.elf));
        this.bonusSpeed = new UpdatingAttribute(UUID.fromString("628dedc0-5f63-4b45-bccb-ecb0fe881b49"), SharedMonsterAttributes.MOVEMENT_SPEED).setSavedInNBT(false);
        this.bonusAtkSpeed = new UpdatingAttribute(UUID.fromString("628dedc0-5f63-4b45-bccb-ecb0fe881b49"), SharedMonsterAttributes.ATTACK_SPEED).setSavedInNBT(false);
        this.jump = new UpdatingAttribute(UUID.fromString("628dedc0-5f63-4b45-bccb-ecb0fe881b49"), JumpAttribute.Jump).setSavedInNBT(false);
    }

    public RaceElf(@Nonnull EntityLivingBase e, @Nonnull EntityProperties properties, @Nonnull RaceCache raceCache) {
        super(e, properties, raceCache);
        this.bonusSpeed = new UpdatingAttribute(UUID.fromString("628dedc0-5f63-4b45-bccb-ecb0fe881b49"), SharedMonsterAttributes.MOVEMENT_SPEED).setSavedInNBT(false);
        this.bonusAtkSpeed = new UpdatingAttribute(UUID.fromString("628dedc0-5f63-4b45-bccb-ecb0fe881b49"), SharedMonsterAttributes.ATTACK_SPEED).setSavedInNBT(false);
        this.jump = new UpdatingAttribute(UUID.fromString("628dedc0-5f63-4b45-bccb-ecb0fe881b49"), JumpAttribute.Jump).setSavedInNBT(false);
    }

    @Override
    public void registerRaceAbilities() {
        this.addAbility(new AbilitySkilledArcher(this.CONFIG.ABILITIES.SKILLED_ARCHER));
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
        if (this.getEntity().world.isRemote) {
            return;
        }
        try {
            if (this.getEntity().world.getBiome(this.getEntity().getPosition()) != null) {
                final Set<Type> biomeType = BiomeDictionary.getTypes(this.getEntity().world.getBiome(this.getEntity().getPosition()));
                if (biomeType.contains(Type.FOREST)) {
                    this.bonusSpeed.addModifier(this.getEntity(), 0.2, 2);
                    this.bonusAtkSpeed.addModifier(this.getEntity(), 0.5, 2);
                    this.jump.addModifier(this.getEntity(), 0.2, 2);
                } else {
                    this.bonusSpeed.removeModifier(this.getEntity());
                    this.bonusAtkSpeed.removeModifier(this.getEntity());
                    this.jump.removeModifier(this.getEntity());
                }
            }
        } catch (final Exception e) {
        }
    }

    @Override
    public void endTransformation() {
        PotionHelper.removeAllPotionEffectsFromConfig(this.getEntity(), this.CONFIG.EFFECTS_TO_ADD);
        AttributeHelper.removeAttributes(this.getEntity(), UUID.fromString("628dedc0-5f63-4b45-bccb-ecb0fe881b49"));
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
        Utils.TempCache<Boolean, Float> output = DamageTypeConfigParser.parseDamageTypeConfig(1, source, dmg, this.raceCache.getPrimaryElement(), this.CONFIG.DAMAGE_TYPES_TO_IGNORE);
        return dmg * output.getSecond();
    }

    @Override
    public float isDamaged(DamageSource source, float dmg) {
        Utils.TempCache<Boolean, Float> output = DamageTypeConfigParser.parseDamageTypeConfig(2, source, dmg, this.raceCache.getPrimaryElement(), this.CONFIG.DAMAGE_TYPES_TO_IGNORE);
        return dmg * output.getSecond();
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
            this.RendererRace = new RaceElfRenderer(this.getEntity(), this);
        }
        return this.RendererRace;
    }
}
