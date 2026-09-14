package xzeroair.trinkets.races.elf;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.attributes.UpdatingAttribute;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.race.RaceCache;
import xzeroair.trinkets.client.races.IRenderRaceHandler;
import xzeroair.trinkets.client.races.elf.RaceElfRenderer;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.elf.config.ElfConfig;
import xzeroair.trinkets.traits.abilities.AbilitySkilledArcher;
import xzeroair.trinkets.traits.abilities.elements.air.AbilityAirImmunity;
import xzeroair.trinkets.traits.abilities.elements.dark.AbilityDarkImmunity;
import xzeroair.trinkets.traits.abilities.elements.earth.AbilityEarthImmunity;
import xzeroair.trinkets.traits.abilities.elements.fire.AbilityFireImmunity;
import xzeroair.trinkets.traits.abilities.elements.ice.AbilityIceImmunity;
import xzeroair.trinkets.traits.abilities.elements.light.AbilityLightImmunity;
import xzeroair.trinkets.traits.abilities.elements.lightning.AbilityLightningImmunity;
import xzeroair.trinkets.traits.abilities.elements.poison.AbilityPoisonImmunity;
import xzeroair.trinkets.traits.abilities.elements.voids.AbilityVoidImmunity;
import xzeroair.trinkets.traits.abilities.elements.water.AbilityWaterImmunity;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.Utils;
import xzeroair.trinkets.util.helpers.AttributeHelper;
import xzeroair.trinkets.util.helpers.DamageTypeConfigParser;
import xzeroair.trinkets.util.helpers.PotionHelper;

import javax.annotation.Nonnull;
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
        this.addElementAbilities();
    }

    @Override
    protected void addAirAbilities(Element element) {
        this.addAbility(new AbilityAirImmunity(this.CONFIG.ELEMENTS.AIR.ABILITIES.AIR_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(this.CONFIG.ELEMENTS.AIR.COMPAT.SURVIVAL, element);
    }

    @Override
    protected void addDarkAbilities(Element element) {
        this.addAbility(new AbilityDarkImmunity(this.CONFIG.ELEMENTS.DARK.ABILITIES.DARK_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(this.CONFIG.ELEMENTS.DARK.COMPAT.SURVIVAL, element);
    }

    @Override
    protected void addEarthAbilities(Element element) {
        this.addAbility(new AbilityEarthImmunity(this.CONFIG.ELEMENTS.EARTH.ABILITIES.EARTH_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(this.CONFIG.ELEMENTS.EARTH.COMPAT.SURVIVAL, element);
    }

    @Override
    protected void addFireAbilities(Element element) {
        this.addAbility(new AbilityFireImmunity(this.CONFIG.ELEMENTS.FIRE.ABILITIES.FIRE_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(this.CONFIG.ELEMENTS.FIRE.COMPAT.SURVIVAL, element);
    }

    @Override
    protected void addIceAbilities(Element element) {
        this.addAbility(new AbilityIceImmunity(this.CONFIG.ELEMENTS.ICE.ABILITIES.ICE_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(this.CONFIG.ELEMENTS.ICE.COMPAT.SURVIVAL, element);
    }

    @Override
    protected void addLightAbilities(Element element) {
        this.addAbility(new AbilityLightImmunity(this.CONFIG.ELEMENTS.LIGHT.ABILITIES.LIGHT_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(this.CONFIG.ELEMENTS.LIGHT.COMPAT.SURVIVAL, element);
    }

    @Override
    protected void addLightningAbilities(Element element) {
        this.addAbility(new AbilityLightningImmunity(this.CONFIG.ELEMENTS.LIGHTNING.ABILITIES.LIGHTNING_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(this.CONFIG.ELEMENTS.LIGHTNING.COMPAT.SURVIVAL, element);
    }

    @Override
    protected void addPoisonAbilities(Element element) {
        this.addAbility(new AbilityPoisonImmunity(this.CONFIG.ELEMENTS.POISON.ABILITIES.POISON_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(this.CONFIG.ELEMENTS.POISON.COMPAT.SURVIVAL, element);
    }

    @Override
    protected void addVoidAbilities(Element element) {
        this.addAbility(new AbilityVoidImmunity(this.CONFIG.ELEMENTS.VOID.ABILITIES.VOID_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(this.CONFIG.ELEMENTS.VOID.COMPAT.SURVIVAL, element);
    }

    @Override
    protected void addWaterAbilities(Element element) {
        this.addAbility(new AbilityWaterImmunity(this.CONFIG.ELEMENTS.WATER.ABILITIES.WATER_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(this.CONFIG.ELEMENTS.WATER.COMPAT.SURVIVAL, element);
    }

    @Override
    protected void addNeutralAbilities() {
        this.addSurvivalAbilities(this.CONFIG.COMPAT.SURVIVAL);
    }

    @Override
    protected String[] getElementAttributes() {
        Element element = this.getRaceCache().getPrimaryElement();
        if (element == Elements.AIR) {
            return this.CONFIG.ELEMENTS.AIR.ATTRIBUTES;
        } else if (element == Elements.DARK) {
            return this.CONFIG.ELEMENTS.DARK.ATTRIBUTES;
        } else if (element == Elements.EARTH) {
            return this.CONFIG.ELEMENTS.EARTH.ATTRIBUTES;
        } else if (element == Elements.FIRE) {
            return this.CONFIG.ELEMENTS.FIRE.ATTRIBUTES;
        } else if (element == Elements.ICE) {
            return this.CONFIG.ELEMENTS.ICE.ATTRIBUTES;
        } else if (element == Elements.LIGHT) {
            return this.CONFIG.ELEMENTS.LIGHT.ATTRIBUTES;
        } else if (element == Elements.LIGHTNING) {
            return this.CONFIG.ELEMENTS.LIGHTNING.ATTRIBUTES;
        } else if (element == Elements.POISON) {
            return this.CONFIG.ELEMENTS.POISON.ATTRIBUTES;
        } else if (element == Elements.VOID) {
            return this.CONFIG.ELEMENTS.VOID.ATTRIBUTES;
        } else if (element == Elements.WATER) {
            return this.CONFIG.ELEMENTS.WATER.ATTRIBUTES;
        }
        return this.CONFIG.ATTRIBUTES;
    }

    @Override
    protected String[] getElementEffectsToAdd() {
        Element element = this.getRaceCache().getPrimaryElement();
        if (element == Elements.AIR) {
            return this.CONFIG.ELEMENTS.AIR.EFFECTS_TO_ADD;
        } else if (element == Elements.DARK) {
            return this.CONFIG.ELEMENTS.DARK.EFFECTS_TO_ADD;
        } else if (element == Elements.EARTH) {
            return this.CONFIG.ELEMENTS.EARTH.EFFECTS_TO_ADD;
        } else if (element == Elements.FIRE) {
            return this.CONFIG.ELEMENTS.FIRE.EFFECTS_TO_ADD;
        } else if (element == Elements.ICE) {
            return this.CONFIG.ELEMENTS.ICE.EFFECTS_TO_ADD;
        } else if (element == Elements.LIGHT) {
            return this.CONFIG.ELEMENTS.LIGHT.EFFECTS_TO_ADD;
        } else if (element == Elements.LIGHTNING) {
            return this.CONFIG.ELEMENTS.LIGHTNING.EFFECTS_TO_ADD;
        } else if (element == Elements.POISON) {
            return this.CONFIG.ELEMENTS.POISON.EFFECTS_TO_ADD;
        } else if (element == Elements.VOID) {
            return this.CONFIG.ELEMENTS.VOID.EFFECTS_TO_ADD;
        } else if (element == Elements.WATER) {
            return this.CONFIG.ELEMENTS.WATER.EFFECTS_TO_ADD;
        }
        return this.CONFIG.EFFECTS_TO_ADD;
    }

    @Override
    protected String[] getElementEffectsToRemove() {
        Element element = this.getRaceCache().getPrimaryElement();
        if (element == Elements.AIR) {
            return this.CONFIG.ELEMENTS.AIR.EFFECTS_TO_REMOVE;
        } else if (element == Elements.DARK) {
            return this.CONFIG.ELEMENTS.DARK.EFFECTS_TO_REMOVE;
        } else if (element == Elements.EARTH) {
            return this.CONFIG.ELEMENTS.EARTH.EFFECTS_TO_REMOVE;
        } else if (element == Elements.FIRE) {
            return this.CONFIG.ELEMENTS.FIRE.EFFECTS_TO_REMOVE;
        } else if (element == Elements.ICE) {
            return this.CONFIG.ELEMENTS.ICE.EFFECTS_TO_REMOVE;
        } else if (element == Elements.LIGHT) {
            return this.CONFIG.ELEMENTS.LIGHT.EFFECTS_TO_REMOVE;
        } else if (element == Elements.LIGHTNING) {
            return this.CONFIG.ELEMENTS.LIGHTNING.EFFECTS_TO_REMOVE;
        } else if (element == Elements.POISON) {
            return this.CONFIG.ELEMENTS.POISON.EFFECTS_TO_REMOVE;
        } else if (element == Elements.VOID) {
            return this.CONFIG.ELEMENTS.VOID.EFFECTS_TO_REMOVE;
        } else if (element == Elements.WATER) {
            return this.CONFIG.ELEMENTS.WATER.EFFECTS_TO_REMOVE;
        }
        return this.CONFIG.EFFECTS_TO_REMOVE;
    }

    @Override
    protected String[] getElementDamageTypesToIgnore() {
        Element element = this.getRaceCache().getPrimaryElement();
        if (element == Elements.AIR) {
            return this.CONFIG.ELEMENTS.AIR.DAMAGE_TYPES_TO_IGNORE;
        } else if (element == Elements.DARK) {
            return this.CONFIG.ELEMENTS.DARK.DAMAGE_TYPES_TO_IGNORE;
        } else if (element == Elements.EARTH) {
            return this.CONFIG.ELEMENTS.EARTH.DAMAGE_TYPES_TO_IGNORE;
        } else if (element == Elements.FIRE) {
            return this.CONFIG.ELEMENTS.FIRE.DAMAGE_TYPES_TO_IGNORE;
        } else if (element == Elements.ICE) {
            return this.CONFIG.ELEMENTS.ICE.DAMAGE_TYPES_TO_IGNORE;
        } else if (element == Elements.LIGHT) {
            return this.CONFIG.ELEMENTS.LIGHT.DAMAGE_TYPES_TO_IGNORE;
        } else if (element == Elements.LIGHTNING) {
            return this.CONFIG.ELEMENTS.LIGHTNING.DAMAGE_TYPES_TO_IGNORE;
        } else if (element == Elements.POISON) {
            return this.CONFIG.ELEMENTS.POISON.DAMAGE_TYPES_TO_IGNORE;
        } else if (element == Elements.VOID) {
            return this.CONFIG.ELEMENTS.VOID.DAMAGE_TYPES_TO_IGNORE;
        } else if (element == Elements.WATER) {
            return this.CONFIG.ELEMENTS.WATER.DAMAGE_TYPES_TO_IGNORE;
        }
        return this.CONFIG.DAMAGE_TYPES_TO_IGNORE;
    }

    @Override
    public void whileTransformed() {
        super.whileTransformed();
        this.applyEquipmentWeight(this.CONFIG.EQUIPMENT_WEIGHT.ENABLED, this.CONFIG.EQUIPMENT_WEIGHT.EQUIPMENT, false);
        PotionHelper.removeAllPotionEffectsFromConfig(this.getEntity(), this.getElementEffectsToRemove());
        PotionHelper.addAllPotionEffectsFromConfig(this.getEntity(), true, this.getElementEffectsToAdd());
        this.checkMount();
        if (this.getEntity().world.isRemote) {
            return;
        }
        try {
            if (this.getEntity().world.getBiome(this.getEntity().getPosition()) != null) {
                final Set<Type> biomeType = BiomeDictionary.getTypes(this.getEntity().world.getBiome(this.getEntity()
                        .getPosition()));
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
        this.clearEquipmentWeight();
        PotionHelper.removeAllPotionEffectsFromConfig(this.getEntity(), this.getElementEffectsToAdd());
        AttributeHelper.removeAttributes(this.getEntity(), UUID.fromString("628dedc0-5f63-4b45-bccb-ecb0fe881b49"));
    }

    @Override
    public boolean isAttacked(DamageSource source, float dmg) {
        if (!this.getEntity().world.isRemote) {
            boolean result = DamageTypeConfigParser.parseDamageTypeConfig(0, source, dmg, this.raceCache.getPrimaryElement(), this.getElementDamageTypesToIgnore())
                    .getFirst();
            return !result;
        }
        return true;
    }

    @Override
    public float isHurt(DamageSource source, float dmg) {
        Utils.TempCache<Boolean, Float> output = DamageTypeConfigParser.parseDamageTypeConfig(1, source, dmg, this.raceCache.getPrimaryElement(), this.getElementDamageTypesToIgnore());
        return dmg * output.getSecond();
    }

    @Override
    public float isDamaged(DamageSource source, float dmg) {
        Utils.TempCache<Boolean, Float> output = DamageTypeConfigParser.parseDamageTypeConfig(2, source, dmg, this.raceCache.getPrimaryElement(), this.getElementDamageTypesToIgnore());
        return dmg * output.getSecond();
    }

    @Override
    public boolean potionBeingApplied(PotionEffect effect) {
        return PotionHelper.isPotionEffect(effect, this.getElementEffectsToRemove());
    }

    @Override
    public boolean mountEntity(Entity mount) {
        return this.isMountAllowed(mount, this.CONFIG.CAN_MOUNT, this.CONFIG.CAN_CONTROL_BOATS, this.CONFIG.MOUNT_WHITELIST, this.CONFIG.MOUNT_BLACKLIST);
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
