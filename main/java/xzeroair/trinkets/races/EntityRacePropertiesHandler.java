package xzeroair.trinkets.races;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
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
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.compat.SurvivalCompat;
import xzeroair.trinkets.util.compat.SwimmingSizeCompat;
import xzeroair.trinkets.util.compat.artemislib.SizeAttribute;
import xzeroair.trinkets.util.config.ConfigHelper;
import xzeroair.trinkets.util.config.ConfigHelper.AttributeEntry;
import xzeroair.trinkets.util.config.compat.ConfigSurvivalCompat;
import xzeroair.trinkets.util.helpers.AttributeHelper;
import xzeroair.trinkets.util.helpers.NBTHelper;
import xzeroair.trinkets.util.helpers.RayTraceHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.interfaces.IDescriptionInterface;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class EntityRacePropertiesHandler implements IRaceHandler, IDescriptionInterface {

    protected static final float MIN_PLAYER_WIDTH = 0.3F;
    protected static final float MIN_WIDTH = 0.252F;
    protected static final float MIN_HEIGHT = 0.45F;
    protected static final float MAX_SIZE = 5.4F;
    protected static final float SIZE_PRECISION = 1000.0F;

    protected boolean firstUpdate;
    protected boolean firstTransformUpdate;
    protected boolean adjustCamera;

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

    @SideOnly(Side.CLIENT)
    protected IRenderRaceHandler RendererRace;

    protected Map<Integer, IAbilityInterface> raceAbilities;
    private int index = 0;
    protected Map<String, IAbilityInterface> activeAbilities;
    protected String[] attributes;
    protected List<UpdatingAttribute> attributeList;

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
    }

    public EntityProperties getProperties() {
        return this.properties;
    }

    public String[] getAttributes() {
        return this.getRace().getRaceInformation().getAttributes();
    }

    protected void removeOldAttributes(double progress) {
        if (progress != 0) {
            final RaceCache previous = this.getProperties().getPreviousRaceCache();
            String[] raceAttributes = previous.getRace().getRaceHandler(this.getEntity(), this.getProperties(), previous).getAttributes();
            for (String entry : raceAttributes) {
                AttributeEntry attributeShell = ConfigHelper.getAttributeEntry(entry);
                if (attributeShell != null) {
                    String name = attributeShell.getAttribute();
                    double amount = attributeShell.getAmount();
                    int operation = attributeShell.getOperation();
                    UpdatingAttribute attribute = new UpdatingAttribute(previous.getRace().getName() + "." + name, previous.getRace().getUUID(), name).setSavedInNBT(false);
                    attribute.addModifier(this.entity, (amount * progress), operation);
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
                UpdatingAttribute attribute = new UpdatingAttribute(this.getRace().getName() + "." + name, this.getRace().getUUID(), name).setAmount(amount).setOperation(operation).setSavedInNBT(true);
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
            attribute.addModifier(this.entity, (attribute.amount * progress), attribute.operation);
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
                this.getProperties().getAbilityHandler().registerRaceAbility(this.entity, this.getRace().getRegistryName().toString(), ability);
            }
        }
    }

    /**
     * Initializes the internal race state after race data has loaded or a transformation has started.
     */
    public void initializeState() {
        this.firstTransformUpdate = true;
        this.healthBeforeTransformation = this.entity.getHealth();
        this.maxHealthBeforeTransformation = this.entity.getMaxHealth();
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
            this.raceAbilities.clear();
        }
        if (!this.getRaceAbilities().isEmpty()) {
            this.activeAbilities.clear();
        }
    }

    public void onTick() {
        if (!TrinketsConfig.EXPERIMENTAL_MIXINS.VANILLA_PLAYER_SIZE_UPDATES) {
            this.updateSize();
            this.applyCurrentSize();
        } else {
            this.modifyEyeHeight();
        }
        if (this.isTransforming()) {
            this.addNewAttributes();
            this.whileTranforming();
        } else if (this.isTransformed()) {
            this.addAttributes(1);
            if (this.firstTransformUpdate) {
                if (!this.entity.world.isRemote) {
                    float newMaxHealth = this.entity.getMaxHealth();
                    float difference = this.healthBeforeTransformation - this.maxHealthBeforeTransformation;
                    float healAmount = (this.maxHealthBeforeTransformation - newMaxHealth) + difference;
                    if (healAmount > 0) {
                        this.entity.heal(healAmount);
                    }
                }
                final EntityRace previous = this.getProperties().getPreviousRaceCache().getRace();
                AttributeHelper.removeAttributesByUUID(this.entity, previous.getUUID());
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
        if ((this.isTransforming() || this.isTransformed()) && !this.isSwimming()) {
            this.applyAdjustedSize();
            this.modifyEyeHeight();
        }
    }

    @Override
    public void whileTranforming() {
        try {
            if (this.entity.world.isRemote) {
                this.getRaceRenderer().whileTransforming(this.entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void whileTransformed() {
        try {
            if (this.entity.world.isRemote) {
                this.getRaceRenderer().whileTransformed(this.entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                final IAttributeInstance reach = player.getAttributeMap().getAttributeInstance(EntityPlayer.REACH_DISTANCE);
                if ((reach.getAttributeValue() > 5)) {
                    final RayTraceResult result = RayTraceHelper.rayTrace(player, reach.getAttributeValue() * 0.8);
                    if ((result != null) && (result.typeOfHit == Type.ENTITY)) {
                        final Entity entity = result.entityHit;
                        final Vec3d vec = result.hitVec;
                        if (lClick.isKeyDown()) {
                            NetworkHandler.sendToServer(new IncreasedReachPacket(player, EnumHand.MAIN_HAND, entity, vec.x, vec.y, vec.z));
                            this.cooldown = player.getCooldownPeriod();
                            Trinkets.proxy.renderEffect(3, player.getEntityWorld(), result.hitVec.x, result.hitVec.y + (entity.height * 0.5F), result.hitVec.z, 0, 0, 0, 0, 1, 1);
                        } else if (rClick.isKeyDown()) {
                            NetworkHandler.sendToServer(new IncreasedReachPacket(player, EnumHand.OFF_HAND, entity, vec.x, vec.y, vec.z));
                        }
                    }
                }
            }
        }
    }

    public boolean isTransforming() {
        return (this.getProperties().getHeightValue() != this.getTargetHeight()) || (this.getProperties().getWidthValue() != this.getTargetWidth());
    }

    public boolean isTransformed() {
        return (this.getProperties().getHeightValue() == this.getTargetHeight()) && (this.getProperties().getWidthValue() == this.getTargetWidth());
    }

    public double TransformationProgress() {
        return this.isTransformed() ? 1.0D : this.transformationProgress;
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

    protected double transformProgress(int previousTarget, int currentTarget, int currentValue) {
        double rtn = MathHelper.pct(currentValue + 0.0D, previousTarget + 0.0D, currentTarget + 0.0D);
        if (rtn < 0.01D) {
            return 0D;
        }
        return Math.min(rtn, 1D);
    }

    public boolean isSwimming() {
        return SwimmingSizeCompat.isSwimming(this.entity);
    }

    protected void modifyEyeHeight() {
        if (!(this.entity instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) this.entity;
        if (!this.adjustCamera) {
            return;
        }
        if (!TrinketsConfig.CLIENT.CAMERA_HEIGHT) {
            this.resetEyeHeight(player);
            this.adjustCamera = false;
            return;
        }
        if ((this.isTransforming() || this.isTransformed()) && this.getProperties().getHeightValue() != 100) {
            // 165 when sneaking
            // 162 eyeheight, sneaking is -0.8
            float eyeHeight = Math.round(this.getAdjustedHeight() * 0.85F * 1000F) / 1000F;
            if (player.isPlayerSleeping()) {
                eyeHeight = 0.2F;
            } else if (player.isSneaking()) {
                eyeHeight -= eyeHeight / 20F;
            } else if (this.isElytraPosture()) {
                eyeHeight *= 0.2F;
            }
            if (player.isRiding()) {
                Entity mount = player.getRidingEntity();
                if (mount != null) {
                    eyeHeight = MathHelper.clamp(eyeHeight, mount.height, eyeHeight);
                }
            }
            player.eyeHeight = Math.max(eyeHeight, 0.2F);
        } else {
            this.resetEyeHeight(player);
        }
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
        return (float) (this.getProperties().getDefaultHeight() * (this.getProperties().getHeightValue() * 0.01));//(float) StringUtils.getAccurateDouble(TLHeight, properties.getDefaultHeight());
    }

    public float getWidth() {
        return (float) (this.getProperties().getDefaultWidth() * (this.getProperties().getWidthValue() * 0.01));//(float) StringUtils.getAccurateDouble(TLWidth, properties.getDefaultWidth());
    }

    public float getAdjustedHeight() {
        if (this.entity.isPlayerSleeping()) {
            return this.clampHeight(0.2F);
        }
        final float height = this.getHeight();
        if (this.entity.isSneaking()) {
            final boolean flying = (this.entity instanceof EntityPlayer) && ((EntityPlayer) this.entity).capabilities.isFlying;
            return this.clampHeight(!flying ? height * 0.92F : height);
        }
        if (this.isElytraPosture()) {
            return this.clampHeight(height * 0.2F);
        }
        return this.clampHeight(height);
    }

    public float getAdjustedWidth() {
        if (this.entity.isPlayerSleeping()) {
            return this.clampWidth(0.2F);
        }
        return this.clampWidth(this.getWidth());
    }

    protected boolean isElytraPosture() {
        if (this.entity.isElytraFlying()) {
            return true;
        }
        final IAbilityInterface ability = this.getProperties().getAbilityHandler().getAbility(TrinketsRegistryNames.MODID + ":" + TrinketsRegistryNames.ModAbilities.ELYTRA_FLIGHT);
        return ability instanceof AbilityElytraFlight && ((AbilityElytraFlight) ability).isGliding();
    }

    public AxisAlignedBB getAdjustedBoundingBox() {
        final float width = this.getAdjustedWidth();
        final float height = this.getAdjustedHeight();
        final double halfWidth = width / 2.0D;
        return new AxisAlignedBB(this.entity.posX - halfWidth, this.entity.posY, this.entity.posZ - halfWidth, this.entity.posX + halfWidth, this.entity.posY + height, this.entity.posZ + halfWidth);
    }

    protected void applyAdjustedSize() {
        if (Trinkets.MOD_COMPAT.ArtemisLib && TrinketsConfig.compat.ARTEMIS_LIB) {
            return;
        }
        if (this.entity.isChild()) {
            return;
        }
        final float width = this.getAdjustedWidth();
        final float height = this.getAdjustedHeight();
        if ((width != this.entity.width) || (height != this.entity.height)) {
//            try {
//                TrinketReflectionHelper.ENTITY_SETSIZE.invoke(this.entity, width, height);
//            } catch (Exception ignored) {
//            }
            this.entity.width = width;
            this.entity.height = height;
        }
        this.entity.setEntityBoundingBox(this.getAdjustedBoundingBox());
    }

    protected float clampWidth(float width) {
        final float minWidth = this.entity instanceof EntityPlayer ? MIN_PLAYER_WIDTH : MIN_WIDTH;
        return MathHelper.clamp(roundSize(width), minWidth, MAX_SIZE);
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
            return new SizeAttribute(this.entity, h, w, 0);
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
        final String key = this.getRace().isNone() ? EntityRaces.human.getRegistryName().toString() : this.getRace().getRegistryName().toString();
        final NBTTagCompound tag = this.saveProfileData(new NBTTagCompound());
        tag.setDouble("TransformationProgress", this.transformationProgress);
        compound.setTag(key, tag);
        return compound;
    }

    @Override
    public void loadNBTData(@Nonnull NBTTagCompound compound) {
        final String key = this.getRace().isNone() ? EntityRaces.human.getRegistryName().toString() : this.getRace().getRegistryName().toString();
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
        if ((colorOption < 0) || (colorOption > 2)
                || !this.isValidVariant(primaryVariant, primaryVariants)
                || !this.isValidVariant(auxiliaryVariant, auxiliaryVariants)
                || !this.isValidGender(gender)) {
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
        return gender == 0;
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
                final TranslationHelper.KeyEntry key1 = new TranslationHelper.OptionEntry("element", true, this.raceCache.getPrimaryElement().getDisplayName());
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
            this.RendererRace = new RaceEmptyRenderer(this.entity, new EmptyHandler(this.entity, this.getProperties()));
        }
        return this.RendererRace;
    }

}
