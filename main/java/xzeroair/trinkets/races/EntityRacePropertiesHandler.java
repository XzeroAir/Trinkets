package xzeroair.trinkets.races;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.*;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.attributes.UpdatingAttribute;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.race.RaceCache;
import xzeroair.trinkets.client.races.IRenderRaceHandler;
import xzeroair.trinkets.client.races.RaceEmptyRenderer;
import xzeroair.trinkets.enums.EnumRenderLocation;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.network.IncreasedReachPacket;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.traits.abilities.AbilityElytraFlight;
import xzeroair.trinkets.traits.abilities.compat.survival.AbilityColdImmunity;
import xzeroair.trinkets.traits.abilities.compat.survival.AbilityHeatImmunity;
import xzeroair.trinkets.traits.abilities.compat.survival.AbilityParasitesImmunity;
import xzeroair.trinkets.traits.abilities.compat.survival.AbilityThirstImmunity;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.compat.SurvivalCompat;
import xzeroair.trinkets.util.compat.SwimmingSizeCompat;
import xzeroair.trinkets.util.compat.artemislib.SizeAttribute;
import xzeroair.trinkets.util.config.ConfigDefaultReusedConstants;
import xzeroair.trinkets.util.config.ConfigHelper;
import xzeroair.trinkets.util.config.ConfigHelper.AttributeEntry;
import xzeroair.trinkets.util.config.compat.ConfigSurvivalCompat;
import xzeroair.trinkets.util.helpers.*;
import xzeroair.trinkets.util.interfaces.IDescriptionInterface;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

public abstract class EntityRacePropertiesHandler implements IRaceHandler, IDescriptionInterface {

    protected static final float MIN_WIDTH = 0.252F;
    protected static final float MIN_HEIGHT = 0.45F;
    protected static final float MAX_SIZE = 5.4F;
    protected static final float SIZE_PRECISION = 1000.0F;
    protected static final float SERVER_EYE_HEIGHT_STEP = 0.25F;
    protected static final float CLIENT_EYE_HEIGHT_STEP = 0.05F;

    protected boolean firstUpdate;
    protected boolean firstTransformUpdate;
    protected boolean adjustCamera;
    protected boolean swimming;
    private float previousClientCameraEyeHeight;
    private float clientCameraEyeHeight;
    private boolean hasClientCameraEyeHeight;

    private final EntityProperties properties;
    private final EntityLivingBase entity;

    protected int targetWidth = 100;
    protected int targetHeight = 100;

    protected RaceCache raceCache;
    protected int gender;

    protected boolean showTraits;
    protected boolean showPrimaryTraits;
    protected boolean showSecondaryTraits;
    protected int colorOption;
    protected int traitPrimaryColor;
    protected int traitSecondaryColor;
    protected int traitAuxColor;
    protected int traitVariant;
    protected int traitAuxVariant;

    protected float healthBeforeTransformation; // TODO Store the Health before, then calculate the health afterwards
    protected float maxHealthBeforeTransformation;
    protected double transformationProgress;
    protected float swimmingSizeProgress;
    protected float swimmingThrustProgress;

    @SideOnly(Side.CLIENT)
    protected IRenderRaceHandler RendererRace;

    protected Map<Integer, IAbilityInterface> raceAbilities;
    private int index = 0;
    protected Map<String, IAbilityInterface> activeAbilities;
    protected String[] attributes;
    protected List<UpdatingAttribute> attributeList;
    protected final UpdatingAttribute equipmentWeightMovement;
    protected final UpdatingAttribute equipmentWeightJump;
    private final Map<String, ConfigHelper.ConfigEquipmentObject> equipmentWeightEntries = new LinkedHashMap<>();
    private String[] equipmentWeightConfig;

    private int cachedConfigVersion = 1;

    public EntityRacePropertiesHandler(@Nonnull EntityLivingBase e, EntityProperties parentProperties, @Nonnull RaceCache cache) {
        this.entity = e;
        this.properties = parentProperties;
        this.firstUpdate = true;
        this.firstTransformUpdate = true;
        this.raceCache = cache;
        this.transformationProgress = 0D;
        this.setGender(0);
        this.setColorOption(0);
        this.setPrimaryTraitColor(cache.getPrimaryColor());
        this.setSecondaryTraitColor(cache.getSecondaryColor());
        this.setTraitAuxColor(cache.getRace().getRaceInformation().getOptionalColor());
        this.setTargetHeight(cache.getRace().getRaceHeight());
        this.setTargetWidth(cache.getRace().getRaceWidth());
        this.setShowTraits(true);
        this.setTraitVariant(0);
        this.adjustCamera = TrinketsConfig.CLIENT.CAMERA_HEIGHT;
        this.attributes = new String[0];
        this.attributeList = new ArrayList<>();
        this.raceAbilities = new TreeMap<>();
        this.activeAbilities = new TreeMap<>();
        final UUID equipmentWeightId = UUID.fromString("1c9ba72a-a558-4ccc-a997-777bf3a9859a");
        this.equipmentWeightMovement = new UpdatingAttribute(equipmentWeightId, SharedMonsterAttributes.MOVEMENT_SPEED).setSavedInNBT(false);
        this.equipmentWeightJump = new UpdatingAttribute(equipmentWeightId, JumpAttribute.Jump).setSavedInNBT(false);
    }

    public EntityProperties getProperties() {
        return this.properties;
    }

    public String[] getAttributes() {
        return this.getElementAttributes();
    }

    protected String[] getElementAttributes() {
        return this.getRace().getRaceInformation().getAttributes();
    }

    protected void removeOldAttributes(double progress) {
        if (progress != 0) {
            final RaceCache previous = this.getProperties().getPreviousRaceCache();
            String[] raceAttributes = previous.getRace()
                    .getRaceHandler(this.getEntity(), this.getProperties(), previous)
                    .getAttributes();
            for (String entry : raceAttributes) {
                AttributeEntry attributeShell = ConfigHelper.getAttributeEntry(entry);
                if (attributeShell != null) {
                    String name = attributeShell.getAttribute();
                    double amount = attributeShell.getAmount();
                    int operation = attributeShell.getOperation();
                    UpdatingAttribute attribute = new UpdatingAttribute(previous.getRace()
                            .getName() + "." + name, previous.getRace().getUUID(), name).setSavedInNBT(false);
                    attribute.addModifier(this.getEntity(), (amount * progress), operation);
                }
            }
        }
    }

    protected void rebuildAttributeCache() {
        if (!this.attributeList.isEmpty()) {
            this.attributeList.clear();
        }
        for (String entry : this.getAttributes()) {
            AttributeEntry attributeShell = ConfigHelper.getAttributeEntry(entry);
            if (attributeShell != null) {
                String name = attributeShell.getAttribute();
                double amount = attributeShell.getAmount();
                int operation = attributeShell.getOperation();
                UpdatingAttribute attribute = new UpdatingAttribute(this.getRace()
                        .getName() + "." + name, this.getRace().getUUID(), name).setAmount(amount)
                        .setOperation(operation)
                        .setSavedInNBT(true);
                this.attributeList.add(attribute);
            }
        }
    }

    protected void addAttributes(double progress) {
        if (this.cachedConfigVersion != TrinketsConfig.getConfigVersion()) {
            this.rebuildAttributeCache();
            this.cachedConfigVersion = TrinketsConfig.getConfigVersion();
        }
//        System.out.println(progress + "|");
        for (UpdatingAttribute attribute : this.attributeList) {
            attribute.addModifier(this.getEntity(), (attribute.amount * progress), attribute.operation);
        }
    }

    protected void addNewAttributes() {
        double d = Math.round(this.TransformationProgress() * 1000D) / 1000D;
        this.removeOldAttributes(1D - d);
        if (d != 0) {
            this.addAttributes(d);
        }
    }

    public EntityRace getRace() {
        return this.getRaceCache().getRace();
    }

    public RaceCache getRaceCache() {
        return this.raceCache;
    }

    public void addAbility(@Nonnull IAbilityInterface ability) {
        if (ability.getRequiredElement() != null) {
            if (!this.getRaceCache().comparePrimaryElement(ability.getRequiredElement())) {
                return;
            }
        }
        String key = ability.getRegistryName().toString();
        if (!this.getActiveAbilities().containsKey(key)) {
            final int i = this.index;
            if (!this.getRaceAbilities().containsKey(i)) {
                this.activeAbilities.put(key, ability);
                this.raceAbilities.put(i, ability);
                this.index++;
            }
        }
    }

    @Nullable
    public IAbilityInterface getAbility(String ability) {
        return this.getProperties().getAbilityHandler().getAbility(ability);
    }

    public void removeAbility(String ability) {
        this.getProperties().getAbilityHandler().removeAbility(ability);
    }

    protected void addSurvivalAbilities(ConfigSurvivalCompat config) {
        this.addSurvivalAbilities(config, Elements.NEUTRAL);
    }

    protected void addElementAbilities() {
        Element element = this.getRaceCache().getPrimaryElement();
        if (element == Elements.AIR) {
            this.addAirAbilities(element);
        } else if (element == Elements.DARK) {
            this.addDarkAbilities(element);
        } else if (element == Elements.EARTH) {
            this.addEarthAbilities(element);
        } else if (element == Elements.FIRE) {
            this.addFireAbilities(element);
        } else if (element == Elements.ICE) {
            this.addIceAbilities(element);
        } else if (element == Elements.LIGHT) {
            this.addLightAbilities(element);
        } else if (element == Elements.LIGHTNING) {
            this.addLightningAbilities(element);
        } else if (element == Elements.POISON) {
            this.addPoisonAbilities(element);
        } else if (element == Elements.VOID) {
            this.addVoidAbilities(element);
        } else if (element == Elements.WATER) {
            this.addWaterAbilities(element);
        } else {
            this.addNeutralAbilities();
        }
    }

    protected void addAirAbilities(Element element) {
    }

    protected void addDarkAbilities(Element element) {
    }

    protected void addEarthAbilities(Element element) {
    }

    protected void addFireAbilities(Element element) {
    }

    protected void addIceAbilities(Element element) {
    }

    protected void addLightAbilities(Element element) {
    }

    protected void addLightningAbilities(Element element) {
    }

    protected void addPoisonAbilities(Element element) {
    }

    protected void addVoidAbilities(Element element) {
    }

    protected void addWaterAbilities(Element element) {
    }

    protected void addNeutralAbilities() {
    }

    protected String[] getElementEffectsToAdd() {
        return ConfigDefaultReusedConstants.EMPTY_STRING_ARRAY;
    }

    protected String[] getElementEffectsToRemove() {
        return ConfigDefaultReusedConstants.EMPTY_STRING_ARRAY;
    }

    protected String[] getElementDamageTypesToIgnore() {
        return ConfigDefaultReusedConstants.EMPTY_STRING_ARRAY;
    }

    public String[] getActiveEffectsToAdd() {
        return this.getElementEffectsToAdd();
    }

    public String[] getActiveEffectsToRemove() {
        return this.getElementEffectsToRemove();
    }

    public String[] getActiveDamageTypesToIgnore() {
        return this.getElementDamageTypesToIgnore();
    }

    protected void addSurvivalAbilities(ConfigSurvivalCompat config, Element element) {
        if (SurvivalCompat.isSurvivalModsActive()) {
            if (config.immuneToHeat) {
                this.addAbility((new AbilityHeatImmunity(config.immuneToHeat).setRequiredElement(element)));
            }
            if (config.immuneToCold) {
                this.addAbility((new AbilityColdImmunity(config.immuneToCold).setRequiredElement(element)));
            }
            if (config.immuneToThirst) {
                this.addAbility((new AbilityThirstImmunity(config.immuneToThirst).setRequiredElement(element)));
            }
            if (config.immuneToParasites) {
                this.addAbility((new AbilityParasitesImmunity(config.immuneToParasites).setRequiredElement(element)));
            }
        }
    }

    protected boolean isMountAllowed(Entity mount, boolean canMount, boolean canControlBoats, boolean mountWhitelist, String[] mountList) {
        if (EntityHelper.isCreative(this.getEntity())) return true;
        if (!canMount) return false;
        if (mountList.length > 0) {
            try {
                final ResourceLocation registryName = EntityRegistry.getEntry(mount.getClass()).getRegistryName();
                final List<String> configuredMounts = Arrays.asList(mountList);
                final boolean listed = configuredMounts.contains(registryName.getNamespace() + ":*") || configuredMounts.contains(registryName.toString());
                if (listed) return this.canControlBoat(mount, canControlBoats) && mountWhitelist;
                if (!this.canControlBoat(mount, canControlBoats)) return false;
            } catch (final Exception e) {
                e.printStackTrace();
            }
            return !mountWhitelist;
        }
        return this.canControlBoat(mount, canControlBoats);
    }

    private boolean canControlBoat(Entity mount, boolean canControlBoats) {
        if (canControlBoats || !(mount instanceof EntityBoat)) return true;
        final Entity controller = ((EntityBoat) mount).getControllingPassenger();
        return controller != null && controller != this.getEntity();
    }

    protected void checkMount() {
        if (!this.getEntity().world.isRemote && this.getEntity().isRiding()) {
            final Entity mount = this.getEntity().getRidingEntity();
            if (mount != null && !this.mountEntity(mount)) this.getEntity().dismountRidingEntity();
        }
    }

    public void setFirstUpdate() {
        this.setFirstUpdate(true);
    }

    public void setFirstUpdate(boolean firstUpdate) {
        this.firstUpdate = firstUpdate;
    }

    public Map<Integer, IAbilityInterface> getRaceAbilities() {
        if (this.raceAbilities == null) {
            this.raceAbilities = new TreeMap<>();
        }
        return this.raceAbilities;
    }

    protected Map<String, IAbilityInterface> getActiveAbilities() {
        if (this.activeAbilities == null) {
            this.activeAbilities = new TreeMap<>();
        }
        return this.activeAbilities;
    }

    private void registerActiveRaceAbilities() {
        if (!this.getActiveAbilities().isEmpty()) {
            for (IAbilityInterface ability : this.activeAbilities.values()) {
                this.getProperties()
                        .getAbilityHandler()
                        .registerRaceAbility(this.getEntity(), this.getRace().getRegistryName().toString(), ability);
            }
        }
    }

    /**
     * Initializes the internal race state after race data has loaded or a transformation has started.
     */
    public void initializeState() {
        this.firstTransformUpdate = true;
        this.healthBeforeTransformation = this.getEntity().getHealth();
        this.maxHealthBeforeTransformation = this.getEntity().getMaxHealth();
        this.index = 0;
        if (!this.getRaceAbilities().isEmpty()) {
            this.raceAbilities.clear();
        }
        if (!this.getActiveAbilities().isEmpty()) {
            this.activeAbilities.clear();
        }
        this.rebuildAttributeCache();
        this.registerRaceAbilities();
        this.registerActiveRaceAbilities();
    }

    public void onTransform() {
        this.startTransformation();
        this.initializeState();
    }

    /**
     * Use {@link #endTransformation()} instead
     */
    public void onTransformEnd() {
        this.endTransformation();
        SizeAttribute artemis = this.getArtemisAttributeSize();
        if (artemis != null) {
            artemis.removeModifiers();
        }
        if (!this.getEntity().world.isRemote) {
            this.savedNBTData(this.getProperties().getTag());
        }
        if (!this.getActiveAbilities().isEmpty()) {
            this.activeAbilities.clear();
        }
        if (!this.getRaceAbilities().isEmpty()) {
            this.raceAbilities.clear();
        }
    }

    public void onTick() {
        if (!TrinketsConfig.EXPERIMENTAL_MIXINS.RACE_SCALED_PLAYER_DIMENSIONS) {
            this.updateSize();
            this.applyCurrentSize();
        } else {
            this.updateEyeHeight();
        }
        if (this.isTransforming()) {
            this.addNewAttributes();
            this.whileTranforming();
        } else if (this.isTransformed()) {
            this.addAttributes(1);
            if (this.firstTransformUpdate) {
                if (!this.getEntity().world.isRemote) {
                    float newMaxHealth = this.getEntity().getMaxHealth();
                    float difference = this.healthBeforeTransformation - this.maxHealthBeforeTransformation;
                    float healAmount = (this.maxHealthBeforeTransformation - newMaxHealth) + difference;
                    if (healAmount > 0) {
                        this.getEntity().heal(healAmount);
                    }
                }
                final EntityRace previous = this.getProperties().getPreviousRaceCache().getRace();
                AttributeHelper.removeAttributesByUUID(this.getEntity(), previous.getUUID());
            }
            SizeAttribute artemis = this.getArtemisAttributeSize();
            if (artemis != null) {
                artemis.addModifiers();
            }
            this.whileTransformed();
            this.firstTransformUpdate = false;
        }
        if (this.cooldown > 0) {
            this.cooldown--;
        } else {
            this.cooldown = 0;
        }
    }

    public void applyCurrentSize() {
        if ((this.isTransforming() || this.isTransformed()) && !SwimmingSizeCompat.isSwimming(this.getEntity())) {
            this.applySize();
            this.updateEyeHeight();
        }
    }

    @Override
    public void whileTranforming() {
        try {
            if (this.getEntity().world.isRemote) {
                this.getRaceRenderer().whileTransforming(this.getEntity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void whileTransformed() {
        try {
            if (this.getEntity().world.isRemote) {
                this.getRaceRenderer().whileTransformed(this.getEntity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void applyEquipmentWeight(boolean enabled, String[] equipment, boolean exempt) {
        if (this.getEntity().world.isRemote || !enabled || exempt) {
            this.clearEquipmentWeight();
            return;
        }
        final Map<String, ConfigHelper.ConfigEquipmentObject> entries = this.getEquipmentWeightEntries(equipment);
        double amount = 0D;
        for (final ItemStack stack : this.getEntity().getEquipmentAndArmor()) {
            if (!stack.isEmpty()) {
                final ConfigHelper.ConfigEquipmentObject entry = this.getEquipmentWeightEntry(entries, stack);
                if (entry != null) {
                    amount -= entry.getEquipmentWeight();
                }
            }
        }
        if (amount != 0D) {
            this.equipmentWeightMovement.addModifier(this.getEntity(), amount, 2);
            this.equipmentWeightJump.addModifier(this.getEntity(), amount, 2);
        } else {
            this.clearEquipmentWeight();
        }
    }

    protected void clearEquipmentWeight() {
        this.equipmentWeightMovement.removeModifier(this.getEntity());
        this.equipmentWeightJump.removeModifier(this.getEntity());
    }

    private Map<String, ConfigHelper.ConfigEquipmentObject> getEquipmentWeightEntries(String[] equipment) {
        if (this.equipmentWeightConfig == equipment) {
            return this.equipmentWeightEntries;
        }
        this.equipmentWeightConfig = equipment;
        this.equipmentWeightEntries.clear();
        for (final String value : equipment) {
            final ConfigHelper.ConfigEquipmentObject entry = new ConfigHelper.ConfigEquipmentObject(value);
            if (!entry.isEmpty()) {
                final String slot = entry.getEquipmentSlot();
                final String type = entry.getEquipmentType();
                this.equipmentWeightEntries.put(entry.getObjectRegistryName() + (slot.isEmpty() ? "" : ":" + slot) + (type.isEmpty() ? "" : ":" + type), entry);
            }
        }
        return this.equipmentWeightEntries;
    }

    private ConfigHelper.ConfigEquipmentObject getEquipmentWeightEntry(Map<String, ConfigHelper.ConfigEquipmentObject> entries, ItemStack stack) {
        final Item item = stack.getItem();
        final String registryName = item.getRegistryName().toString();
        final String itemType = ConfigHelper.ConfigEquipmentObject.getItemType(stack);
        if (itemType.isEmpty()) {
            return null;
        }
        final String material = ConfigHelper.ConfigEquipmentObject.getItemMaterial(stack).toLowerCase();
        if (item instanceof ItemArmor) {
            final String armorType = ((ItemArmor) item).armorType.getName();
            return ConfigHelper.TrinketConfigStorage.getListEntry(entries, (Predicate<ConfigHelper.ConfigEquipmentObject>) null, registryName + ":" + armorType, registryName, "ObjectMaterial:" + material + ":" + armorType, "ObjectMaterial:" + material);
        }
        final String hand = stack.isItemEqual(this.getEntity()
                .getHeldItemMainhand()) ? "mainhand" : stack.isItemEqual(this.getEntity()
                .getHeldItemOffhand()) ? "offhand" : "hand";
        final String[] matches = new String[]{registryName + ":" + hand, registryName, "ObjectMaterial:" + material + ":" + hand + ":" + itemType, "ObjectMaterial:" + material + ":" + hand + ":tool", "ObjectMaterial:" + material + ":hand:" + itemType, "ObjectMaterial:" + material + ":hand:tool", "ObjectMaterial:" + material + ":" + itemType, "ObjectMaterial:" + material + ":tool", "ObjectMaterial:" + material + ":" + hand, "ObjectMaterial:" + material + ":hand", "ObjectMaterial:" + material};
        return ConfigHelper.TrinketConfigStorage.getListEntry(entries, (key, value) -> value.doesItemMatchEntry(stack), matches);
    }

    protected float cooldown = 0;

    @Override
    public void interact(PlayerInteractEvent event) {
        if (TrinketsConfig.getClientStore().REACH_FIX) {
            final EntityPlayer player = event.getEntityPlayer();
            final boolean isClient = player.world.isRemote;
            if (isClient) {
                if ((this.cooldown != 0)) {
                    return;
                }
                final KeyBinding lClick = Minecraft.getMinecraft().gameSettings.keyBindAttack;
                final KeyBinding rClick = Minecraft.getMinecraft().gameSettings.keyBindUseItem;
                final IAttributeInstance reach = player.getAttributeMap()
                        .getAttributeInstance(EntityPlayer.REACH_DISTANCE);
                if ((reach.getAttributeValue() > 5)) {
                    final RayTraceResult result = RayTraceHelper.rayTrace(player, reach.getAttributeValue() * 0.8);
                    if ((result != null) && (result.typeOfHit == Type.ENTITY)) {
                        final Entity entity = result.entityHit;
                        final Vec3d vec = result.hitVec;
                        if (lClick.isKeyDown()) {
                            NetworkHandler.sendToServer(new IncreasedReachPacket(player, EnumHand.MAIN_HAND, entity, vec.x, vec.y, vec.z));
                            this.cooldown = player.getCooldownPeriod();
                            Trinkets.proxy.renderEffect(Reference.EFFECT_SWEEP_ATTACK, player.getEntityWorld(), result.hitVec.x, result.hitVec.y + (entity.height * 0.5F), result.hitVec.z, 0, 0, 0, 0, 1, 1);
                        } else if (rClick.isKeyDown()) {
                            NetworkHandler.sendToServer(new IncreasedReachPacket(player, EnumHand.OFF_HAND, entity, vec.x, vec.y, vec.z));
                        }
                    }
                }
            }
        }
    }

    public boolean isTransforming() {
        return (this.getProperties().getHeightValue() != this.getTargetHeight()) || (this.getProperties()
                .getWidthValue() != this.getTargetWidth());
    }

    public boolean isTransformed() {
        return (this.getProperties().getHeightValue() == this.getTargetHeight()) && (this.getProperties()
                .getWidthValue() == this.getTargetWidth());
    }

    public double TransformationProgress() {
        return this.isTransformed() ? 1.0D : this.transformationProgress;
    }

    public double getThirdPersonCameraScale() {
        final int previousTargetHeight = this.getProperties().getPreviousRaceCache().getRace().getRaceHeight();
        final int targetHeight = this.getTargetHeight();
        final double progress = this.TransformationProgress();
        return (previousTargetHeight + ((targetHeight - previousTargetHeight) * progress)) * 0.01D;
    }

    public void updateSize() {
        final int targetHeight = this.getTargetHeight();
        final int targetWidth = this.getTargetWidth();
        final int height = this.getProperties().getHeightValue();
        final int width = this.getProperties().getWidthValue();

        if ((height != targetHeight) || (width != targetWidth)) {
            final int h = stepTowards(height, targetHeight);
            final int w = stepTowards(width, targetWidth);

            this.getProperties().setHeightValue(h);
            this.getProperties().setWidthValue(w);

            final int previousRaceTargetHeight = this.getProperties().getPreviousRaceCache().getRace().getRaceHeight();
            final int previousRaceTargetWidth = this.getProperties().getPreviousRaceCache().getRace().getRaceWidth();

            final double heightProgress = this.transformProgress(previousRaceTargetHeight, targetHeight, height);
            final double widthProgress = this.transformProgress(previousRaceTargetWidth, targetWidth, width);
            final double finalValue = MathHelper.clamp((heightProgress + widthProgress) / 2.0D, 0.0D, 1.0D);

            if (this.transformationProgress != finalValue) {
                this.transformationProgress = finalValue;
            }
        }
    }

    private static int stepTowards(int current, int target) {
        if (current < target) {
            return current + 1;
        } else if (current > target) {
            return current - 1;
        }
        return current;
    }

    private static float stepTowards(float current, float target, float step) {
        if (Math.abs(target - current) <= step) {
            return target;
        }
        return current + Math.copySign(step, target - current);
    }

    protected double transformProgress(int previousTarget, int currentTarget, int currentValue) {
        double rtn = MathHelper.pct(currentValue + 0.0D, previousTarget + 0.0D, currentTarget + 0.0D);
        if (rtn < 0.01D) {
            return 0D;
        }
        return Math.min(rtn, 1D);
    }

    public boolean isSwimming() {
        return this.swimming;
    }

    public void updateSwimming() {
        final BlockPos bodyPosition = this.getEntity().getPosition();
        final IBlockState bodyBlock = this.getEntity().world.getBlockState(bodyPosition);
        final Block block = bodyBlock.getBlock();
        final boolean canSwim = this.isSwimmingEnabled() && !this.getEntity()
                .isRiding() && (this.getEntity().isInWater() || this.getEntity().isInLava()) && block != Blocks.AIR;
        final boolean continueSwimming = this.swimming && this.getEntity().isSneaking();
        final boolean activeSwimMotion = this.getEntity().isSprinting() || continueSwimming;
        if (canSwim && activeSwimMotion) {
            this.swimming = true;
            if (this.getEntity().isSprinting()) {
                this.swimmingSizeProgress = Math.min(this.swimmingSizeProgress + 0.05F, 1.0F);
            }
            this.swimmingThrustProgress = 1.0F;
        } else {
            this.swimmingSizeProgress = Math.max(this.swimmingSizeProgress - 0.05F, 0.0F);
            this.swimming = canSwim && this.swimmingSizeProgress > 0F;
            this.swimmingThrustProgress = this.swimming ? Math.max(this.swimmingThrustProgress - 0.075F, 0.0F) : 0.0F;
        }
        if (canSwim && !this.swimming && !this.getEntity().isSneaking() && this.getEntity().motionY < 0D) {
            this.getEntity().motionY += SwimmingMotionHelper.NORMAL_BUOYANCY;
        }
        this.applySwimmingMotion();
    }

    protected void applySwimmingMotion() {
        if (this.isSwimming() && this.swimmingThrustProgress > 0F) {
            SwimmingMotionHelper.apply(this.getEntity(), this.getProperties()
                    .getKeybindHandler(), false, this.swimmingThrustProgress);
        }
    }

    protected boolean isSwimmingEnabled() {
        return !SwimmingSizeCompat.hasExternalSwimmingMod()
                && (this.getEntity().world.isRemote ? TrinketsConfig.getClientStore().SWIMMING : TrinketsConfig.SERVER.MISC.SWIMMING);
    }

    protected void updateEyeHeight() {
        if (!(this.getEntity() instanceof EntityPlayer)) {
            return;
        }
        final EntityPlayer player = (EntityPlayer) this.getEntity();
        if (!this.adjustCamera) {
            if (this.getEntity().world.isRemote) {
                this.hasClientCameraEyeHeight = false;
            }
            return;
        }
        if (!TrinketsConfig.CLIENT.CAMERA_HEIGHT) {
            this.resetEyeHeight(player);
            this.adjustCamera = false;
            if (this.getEntity().world.isRemote) {
                this.hasClientCameraEyeHeight = false;
            }
            return;
        }
        final float targetEyeHeight = this.getTargetEyeHeight(player);
        if (this.getEntity().world.isRemote) {
            if (!this.hasClientCameraEyeHeight) {
                this.clientCameraEyeHeight = player.eyeHeight;
            }
            this.previousClientCameraEyeHeight = this.clientCameraEyeHeight;
            this.clientCameraEyeHeight = stepTowards(this.clientCameraEyeHeight, targetEyeHeight, CLIENT_EYE_HEIGHT_STEP);
            this.hasClientCameraEyeHeight = true;
            player.eyeHeight = targetEyeHeight;
        } else {
            player.eyeHeight = targetEyeHeight < player.eyeHeight ? targetEyeHeight : stepTowards(player.eyeHeight, targetEyeHeight, SERVER_EYE_HEIGHT_STEP);
        }
    }

    public float getClientEyeHeightOffset(float partialTicks) {
        if (!this.getEntity().world.isRemote || !this.hasClientCameraEyeHeight || !(this.getEntity() instanceof EntityPlayer)) {
            return 0F;
        }
        final EntityPlayer player = (EntityPlayer) this.getEntity();
        final float interpolatedEyeHeight = this.previousClientCameraEyeHeight + ((this.clientCameraEyeHeight - this.previousClientCameraEyeHeight) * partialTicks);
        return player.eyeHeight - interpolatedEyeHeight;
    }

    public float getTargetEyeHeight(@Nonnull EntityPlayer player) {
        final boolean adjustedSize = ((this.isTransforming() || this.isTransformed()) && this.getProperties()
                .getHeightValue() != 100) || (this.swimmingSizeProgress > 0F && !SwimmingSizeCompat.isSwimming(this.getEntity())) || this.isElytraFlying() || this.isGliding();
        if (!adjustedSize) {
            return player.getDefaultEyeHeight();
        }
        float eyeHeight = Math.round(this.getHeight() * 0.85F * 1000F) / 1000F;
        if (player.isPlayerSleeping()) {
            eyeHeight = 0.2F;
        } else if (player.isSneaking()) {
            eyeHeight -= eyeHeight / 20F;
        }
        if (player.isRiding()) {
            Entity mount = player.getRidingEntity();
            if (mount != null) {
                eyeHeight = MathHelper.clamp(eyeHeight, mount.height, eyeHeight);
            }
        }
        return Math.max(eyeHeight, 0.2F);
    }

    private void resetEyeHeight(@Nonnull EntityPlayer player) {
        if (player.eyeHeight != player.getDefaultEyeHeight()) {
            player.eyeHeight = player.getDefaultEyeHeight();
        }
    }

    public int getTargetHeight() {
        return this.targetHeight;
    }

    public void setTargetHeight(int targetHeight) {
        this.targetHeight = targetHeight;
    }

    public int getTargetWidth() {
        return this.targetWidth;
    }

    public void setTargetWidth(int targetWidth) {
        this.targetWidth = targetWidth;
    }

    public float getHeight() {
        if (this.getEntity().isPlayerSleeping()) {
            return this.clampHeight(0.2F);
        }
        final float height = (float) (this.getProperties().getDefaultHeight() * (this.getProperties()
                .getHeightValue() * 0.01));
        if (this.swimmingSizeProgress > 0F && !SwimmingSizeCompat.isSwimming(this.getEntity())) {
            return this.clampHeight(height - ((height * 0.8F) * this.swimmingSizeProgress));
        }
        if (this.getEntity().isSneaking()) {
            final boolean flying = (this.getEntity() instanceof EntityPlayer) && ((EntityPlayer) this.getEntity()).capabilities.isFlying;
            return this.clampHeight(!flying ? height * 0.92F : height);
        }
        if (this.isElytraFlying() || this.isGliding()) {
            return this.clampHeight(height * 0.2F);
        }
        return this.clampHeight(height);
    }

    public float getWidth() {
        if (this.getEntity().isPlayerSleeping()) {
            return this.clampWidth(0.2F);
        }
        final float width = (float) (this.getProperties().getDefaultWidth() * (this.getProperties()
                .getWidthValue() * 0.01));
        return this.clampWidth(width);
    }

    public boolean isElytraFlying() {
        return this.getEntity().isElytraFlying();
    }

    public boolean isGliding() {
        if (this.isElytraFlying()) {
            return false;
        }
        final IAbilityInterface ability = this.getProperties()
                .getAbilityHandler()
                .getAbility(TrinketsRegistryNames.MODID + ":" + TrinketsRegistryNames.ModAbilities.ELYTRA_FLIGHT);
        return ability instanceof AbilityElytraFlight && ((AbilityElytraFlight) ability).isGliding();
    }

    public AxisAlignedBB getBoundingBox() {
        final float width = this.getWidth();
        final float height = this.getHeight();
        final double halfWidth = width / 2.0D;
        return new AxisAlignedBB(this.getEntity().posX - halfWidth, this.getEntity().posY, this.getEntity().posZ - halfWidth, this.getEntity().posX + halfWidth, this.getEntity().posY + height, this.getEntity().posZ + halfWidth);
    }

    protected void applySize() {
        if (Trinkets.MOD_COMPAT.ArtemisLib && TrinketsConfig.compat.ARTEMIS_LIB) {
            return;
        }
        if (this.getEntity().isChild()) {
            return;
        }
        final float width = this.getWidth();
        final float height = this.getHeight();
        if ((width != this.getEntity().width) || (height != this.getEntity().height)) {
//            try {
//                TrinketReflectionHelper.ENTITY_SETSIZE.invoke(this.getEntity(), width, height);
//            } catch (Exception ignored) {
//            }
            this.getEntity().width = width;
            this.getEntity().height = height;
        }
        this.getEntity().setEntityBoundingBox(this.getBoundingBox());
    }

    protected float clampWidth(float width) {
        return MathHelper.clamp(roundSize(width), MIN_WIDTH, MAX_SIZE);
    }

    protected float clampHeight(float height) {
        return MathHelper.clamp(roundSize(height), MIN_HEIGHT, MAX_SIZE);
    }

    protected static float roundSize(float value) {
        return Math.round(value * SIZE_PRECISION) / SIZE_PRECISION;
    }

    @Nullable
    private SizeAttribute getArtemisAttributeSize() {
        if (Trinkets.MOD_COMPAT.ArtemisLib && TrinketsConfig.compat.ARTEMIS_LIB) {
            final double h = (this.getTargetHeight() - 100) * 0.01D;
            final double w = (this.getTargetWidth() - 100) * 0.01D;
            return new SizeAttribute(this.getEntity(), h, w, 0);
        }
        return null;
    }

    public void copyFrom(@Nonnull EntityRacePropertiesHandler source, boolean wasDeath, boolean keepInv) {
        final boolean isNormal = this.getRace().isNone();
        if (!isNormal) {
            this.transformationProgress = source.transformationProgress;
            this.healthBeforeTransformation = source.healthBeforeTransformation;
            this.maxHealthBeforeTransformation = source.maxHealthBeforeTransformation;
            this.firstTransformUpdate = source.firstTransformUpdate;
            if (this.getRaceCache().compareRace(source.getRaceCache())) {
                this.showTraits = source.showTraits;
                this.traitPrimaryColor = source.traitPrimaryColor;
                this.traitSecondaryColor = source.traitSecondaryColor;
                this.traitVariant = source.traitVariant;
                this.traitAuxColor = source.traitAuxColor;
                this.traitAuxVariant = source.traitAuxVariant;
                this.targetHeight = source.targetHeight;
                this.targetWidth = source.targetWidth;
            }
        }
    }

    public boolean canFly() {
        return this.getRace().canFly();
    }

    /*------------------------------------------Race Handlers--------------------------------------------*/

    @Override
    public NBTTagCompound savedNBTData(@Nonnull NBTTagCompound compound) {
        final String key = this.getRace().isNone() ? EntityRaces.human.getRegistryName().toString() : this.getRace()
                .getRegistryName()
                .toString();
        final NBTTagCompound tag = this.saveProfileData(new NBTTagCompound());
        tag.setDouble("TransformationProgress", this.transformationProgress);
        compound.setTag(key, tag);
        return compound;
    }

    @Override
    public void loadNBTData(@Nonnull NBTTagCompound compound) {
        final String key = this.getRace().isNone() ? EntityRaces.human.getRegistryName().toString() : this.getRace()
                .getRegistryName()
                .toString();
        if (NBTHelper.hasTagCompound(compound, key)) {
            final NBTTagCompound tag = compound.getCompoundTag(key);
            this.loadProfileData(tag);
            NBTHelper.hasDouble(tag, "TransformationProgress", (progress) -> this.transformationProgress = progress);
        }
    }

    public NBTTagCompound saveProfileData(@Nonnull NBTTagCompound tag) {
        tag.setBoolean("ShowTrait", this.showTraits);
        tag.setInteger("ColorOption", this.colorOption);
        tag.setInteger("ColorPrimary", this.traitPrimaryColor);
        tag.setInteger("ColorSecondary", this.traitSecondaryColor);
        tag.setInteger("AuxColor", this.traitAuxColor);
        tag.setInteger("TraitVariant", this.traitVariant);
        tag.setInteger("TraitAuxVariant", this.traitAuxVariant);
        tag.setInteger("Gender", this.gender);
        return tag;
    }

    public boolean loadProfileData(@Nonnull NBTTagCompound tag) {
        final int primaryVariants = this.getRace().getRaceInformation().getPrimaryTraitMaxVariants();
        final int auxiliaryVariants = this.getRace().getRaceInformation().getSecondaryTraitMaxVariants();
        final int colorOption = NBTHelper.getInteger(tag, "ColorOption", this.colorOption);
        final int primaryVariant = NBTHelper.getInteger(tag, "TraitVariant", this.traitVariant);
        final int auxiliaryVariant = NBTHelper.getInteger(tag, "TraitAuxVariant", this.traitAuxVariant);
        final int gender = NBTHelper.getInteger(tag, "Gender", this.gender);
        if ((colorOption < 0) || (colorOption > 2) || !this.isValidVariant(primaryVariant, primaryVariants) || !this.isValidVariant(auxiliaryVariant, auxiliaryVariants) || !this.isValidGender(gender)) {
            return false;
        }
        NBTHelper.hasBoolean(tag, "ShowTrait", value -> this.showTraits = value);
        NBTHelper.hasInteger(tag, "ColorOption", value -> this.colorOption = value);
        NBTHelper.hasInteger(tag, "ColorPrimary", value -> this.traitPrimaryColor = value);
        NBTHelper.hasInteger(tag, "ColorSecondary", value -> this.traitSecondaryColor = value);
        NBTHelper.hasInteger(tag, "AuxColor", value -> this.traitAuxColor = value);
        NBTHelper.hasInteger(tag, "TraitVariant", value -> this.traitVariant = value);
        NBTHelper.hasInteger(tag, "TraitAuxVariant", value -> this.traitAuxVariant = value);
        NBTHelper.hasInteger(tag, "Gender", this::setGender);
        return true;
    }

    protected boolean isValidVariant(int variant, int variants) {
        return (variant >= 0) && ((variants > 0) ? (variant < variants) : (variant == 0));
    }

    protected boolean isValidGender(int gender) {
        return (gender >= 0) && (gender <= 2);
    }

    public boolean showTraits() {
        return this.showTraits;
    }

    public void setShowTraits(boolean showTraits) {
        this.showTraits = showTraits;
    }

    public int getPrimaryTraitColor() {
        return this.traitPrimaryColor;
    }

    public void setPrimaryTraitColor(int color) {
        this.traitPrimaryColor = color;
    }

    public int getSecondaryTraitColor() {
        return this.traitSecondaryColor;
    }

    public void setSecondaryTraitColor(int color) {
        this.traitSecondaryColor = color;
    }

    public int getTraitAuxColor() {
        return this.traitAuxColor;
    }

    public void setTraitAuxColor(int traitAuxColor) {
        this.traitAuxColor = traitAuxColor;
    }

    public int getTraitVariant() {
        return this.traitVariant;
    }

    public int getColorOption() {
        return this.colorOption;
    }

    public void setColorOption(int option) {
        this.colorOption = option;
    }

    public int getTraitAuxVariant() {
        return this.traitAuxVariant;
    }

    public void setTraitAuxVariant(int traitAuxVariant) {
        this.traitAuxVariant = traitAuxVariant;
    }

    public void setTraitVariant(int variant) {
        this.traitVariant = variant;
    }

    public int getGender() {
        return this.gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getDescription(List<String> tooltips, int rendMod, int rendID) {
        String translationKey = this.raceCache.getRace().getTranslationKey();
        final TranslationHelper helper = TranslationHelper.INSTANCE;
        for (int i = 1; i <= 10; i++) {
            final String string = helper.getLangTranslation(translationKey + ".tooltip" + i, (lang) -> {
                final TranslationHelper.KeyEntry key1 = new TranslationHelper.OptionEntry("element", true, this.raceCache.getPrimaryElement()
                        .getDisplayName());
                return helper.formatAddVariable(lang, EnumRenderLocation.GUI.getId(), key1);
            });
            if (!helper.isStringEmpty(string)) {
                tooltips.add(string);
            }
        }
    }

    public EntityLivingBase getEntity() {
        return this.entity;
    }

    @Override
    public String getDisplayName() {
        return this.raceCache.getRace().getDisplayName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IRenderRaceHandler getRaceRenderer() {
        if (this.RendererRace == null) {
            this.RendererRace = new RaceEmptyRenderer(this.getEntity(), new EmptyHandler(this.getEntity(), this.getProperties()));
        }
        return this.RendererRace;
    }

}
