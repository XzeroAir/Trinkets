package xzeroair.trinkets.races;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.attributes.UpdatingAttribute;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.race.EntityProperties.RaceCache;
import xzeroair.trinkets.client.races.IRenderRaceHandler;
import xzeroair.trinkets.client.races.RaceEmptyRenderer;
import xzeroair.trinkets.enums.EnumRenderLocation;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.network.IncreasedReachPacket;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.artemislib.SizeAttribute;
import xzeroair.trinkets.util.config.ConfigHelper;
import xzeroair.trinkets.util.config.ConfigHelper.AttributeEntry;
import xzeroair.trinkets.util.handlers.SizeHandler;
import xzeroair.trinkets.util.helpers.*;
import xzeroair.trinkets.util.interfaces.IDescriptionInterface;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiFunction;

public abstract class EntityRacePropertiesHandler implements IRaceHandler, IDescriptionInterface {

    protected boolean firstUpdate;
    protected boolean firstTransformUpdate;

    protected EntityLivingBase entity;

    protected int targetWidth = 100;
    protected int targetHeight = 100;

    protected RaceCache raceCache;
    protected RaceAttributesWrapper attributes;

    protected boolean showTraits;
    protected int traitPrimaryColor;
    protected int traitSecondaryColor;
    protected int traitVariant;
    protected int traitVariantMax;

    protected float healthBeforeTransformation; // TODO Store the Health before, then calculate the health afterwards
    protected float maxHealthBeforeTranformation;

    protected double progress = 0D;

    @SideOnly(Side.CLIENT)
    protected IRenderRaceHandler RendererRace;

    protected Map<Integer, IAbilityInterface> raceAbilities;
    private int index = 0;
    protected Map<String, IAbilityInterface> activeAbilities;

    public EntityRacePropertiesHandler(@Nonnull EntityLivingBase e, @Nonnull EntityRace race, Element element) {
        this.entity = e;
        this.firstUpdate = true;
        this.firstTransformUpdate = true;
        this.showTraits = true;
        this.raceCache = new RaceCache(race, element);
        this.attributes = race.getRaceAttributes();
        this.traitPrimaryColor = race.getPrimaryColor();
        this.traitSecondaryColor = race.getSecondaryColor();
        this.traitVariant = 0;
        this.traitVariantMax = 3;
        this.setTargetHeight(race.getRaceHeight());
        this.setTargetWidth(race.getRaceWidth());
        this.raceAbilities = new TreeMap<>();
        this.activeAbilities = new TreeMap<>();
    }

    public EntityRacePropertiesHandler(EntityLivingBase e, RaceCache cache) {
        this(e, cache.getRace(), cache.getElement());
        this.raceCache = cache;
    }

    public EntityRacePropertiesHandler(EntityLivingBase e, EntityRace race) {
        this(e, race, Elements.NEUTRAL);
    }

    public EntityRacePropertiesHandler(EntityLivingBase e) {
        this(e, EntityRaces.none, Elements.NEUTRAL);
    }

    protected void initAttributes() {
        final EntityRace previous = this.getEntityProperties().getPreviousRace().getRace();
        double d1 = Double.parseDouble(Reference.DECIMALFORMAT.format(1D - this.TransformationProgress()));
        if (d1 != 0) {
            String[] raceAttributes = previous.getRaceAttributes().getAttributes();
            if (raceAttributes.length > 0) {
                for (String entry : raceAttributes) {
                    AttributeEntry attributeShell = ConfigHelper.getAttributeEntry(entry);
                    if (attributeShell != null) {
                        String name = attributeShell.getAttribute();
                        double amount = attributeShell.getAmount();
                        int operation = attributeShell.getOperation();
                        boolean isSaved = attributeShell.isSaved();
                        UpdatingAttribute attribute = new UpdatingAttribute(previous.getName() + "." + name, previous.getUUID(), name).setSavedInNBT(false);
                        //					attribute.addModifier(entity, (amount), operation);
                        attribute.addModifier(entity, (amount * d1), operation);
                    }
                }
            }
        }
        double d = Double.parseDouble(Reference.DECIMALFORMAT.format(this.TransformationProgress()));
        if (d != 0) {
            final World world = entity.getEntityWorld();
            String[] raceAttributes = raceCache.getRace().getRaceAttributes().getAttributes();
            if (raceAttributes.length > 0) {
                for (String entry : raceAttributes) {
                    AttributeEntry attributeShell = ConfigHelper.getAttributeEntry(entry);
                    if (attributeShell != null) {
                        String name = attributeShell.getAttribute();
                        double amount = attributeShell.getAmount();
                        int operation = attributeShell.getOperation();
                        boolean isSaved = attributeShell.isSaved();
                        UpdatingAttribute attribute = new UpdatingAttribute(getRace().getName() + "." + name, getRace().getUUID(), name).setSavedInNBT(true);
                        attribute.addModifier(entity, (amount * d), operation);
                    }
                }
            }
        }
    }

    public RaceAttributesWrapper getRaceAttributes() {
        if (attributes == null) {
            attributes = getRace().getRaceAttributes();
        }
        return attributes;
    }

    public EntityRace getRace() {
        return this.getRaceCache().getRace();
    }

    public RaceCache getRaceCache() {
        return raceCache;
    }

    public void addAbility(IAbilityInterface ability) {
        if (ability.getRequiredElement() != null) {
            if (!getRaceCache().compareElement(ability.getRequiredElement())) {
                return;
            }
        }
        String key = ability.getRegistryName().toString();
        if (!getActiveAbilities().containsKey(key)) {
            if (!getRaceAbilities().containsKey(this.index)) {
                activeAbilities.put(key, ability);
                raceAbilities.put(this.index, ability);
                index++;
            }
        }
    }

    @Nullable
    public IAbilityInterface getAbility(String ability) {
        return this.getEntityProperties().getAbilityHandler().getAbility(ability);
    }

    public void removeAbility(String ability) {
        this.getEntityProperties().getAbilityHandler().removeAbility(ability);
    }


    public EntityProperties getEntityProperties() {
        return Capabilities.getEntityProperties(entity, new EntityProperties(entity), (prop, prop2) -> {
            return prop;
        });
    }

    public EntityRacePropertiesHandler setFirstUpdate(boolean firstUpdate) {
        this.firstUpdate = firstUpdate;
        return this;
    }

    public Map<Integer, IAbilityInterface> getRaceAbilities() {
        if (raceAbilities == null) {
            raceAbilities = new TreeMap<>();
        }
        return raceAbilities;
    }

    protected Map<String, IAbilityInterface> getActiveAbilities() {
        if (activeAbilities == null) {
            activeAbilities = new TreeMap<>();
        }
        return activeAbilities;
    }

    /**
     * Use {@link #startTransformation()} instead
     */
    public void onTransform() {
        firstTransformUpdate = true;
        healthBeforeTransformation = entity.getHealth();
        maxHealthBeforeTranformation = entity.getMaxHealth();
        index = 0;
        if (!getRaceAbilities().isEmpty()) {
            raceAbilities.clear();
        }
        if (!getActiveAbilities().isEmpty()) {
            activeAbilities.clear();
        }
        this.startTransformation();
        if (!getActiveAbilities().isEmpty()) {
            for (IAbilityInterface ability : activeAbilities.values()) {
                this.getEntityProperties().getAbilityHandler().registerRaceAbility(this.getRace().getRegistryName().toString(), ability);
            }
        }
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
        try {
            this.savedNBTData(this.getEntityProperties().getTag());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!getActiveAbilities().isEmpty()) {
            raceAbilities.clear();
        }
        if (!getRaceAbilities().isEmpty()) {
            activeAbilities.clear();
        }
    }

    public void onTick() {
        this.updateSize();
        SizeHandler.setSize(entity, this.getHeight(), this.getWidth());
        this.initAttributes();
        this.eyeHeightHandler();
        if (this.isTransforming()) {
            this.whileTranforming();
        } else if (this.isTransformed()) {
            if (firstTransformUpdate) {
                if (!entity.world.isRemote) {
                    float newMaxHealth = entity.getMaxHealth();
                    float difference = healthBeforeTransformation - maxHealthBeforeTranformation;
                    float healAmount = (maxHealthBeforeTranformation - newMaxHealth) + difference;
                    if (healAmount > 0) {
                        entity.heal(healAmount);
                    }
                }
                final EntityRace previous = this.getEntityProperties().getPreviousRace().getRace();
                AttributeHelper.removeAttributesByUUID(entity, previous.getUUID());
            }
            SizeAttribute artemis = this.getArtemisAttributeSize();
            if (artemis != null) {
                artemis.addModifiers();
            }
            this.whileTransformed();
            firstTransformUpdate = false;
        }
        if (cooldown > 0) {
            cooldown--;
        } else {
            cooldown = 0;
        }
    }

    @Override
    public void whileTranforming() {
        try {
            if (entity.world.isRemote) {
                getRaceRenderer().whileTransforming(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void whileTransformed() {
        try {
            if (entity.world.isRemote) {
                getRaceRenderer().whileTransformed(entity);
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
                if ((cooldown != 0)) {
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
                            cooldown = player.getCooldownPeriod();
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
        return (this.getEntityProperties().getHeightValue() != this.getTargetHeight()) || (this.getEntityProperties().getWidthValue() != this.getTargetWidth());
    }

    public boolean isTransformed() {
        return (this.getEntityProperties().getHeightValue() == this.getTargetHeight()) && (this.getEntityProperties().getWidthValue() == this.getTargetWidth());
    }

    public double TransformationProgress() {
        if (!this.isTransformed() && !this.isTransforming()) {
            return 1D;
        }
        return progress;
    }

    // TODO HERE
    protected void updateSize() {
        if ((!this.isTransformed() && this.isTransforming()) || (this.TransformationProgress() < 1D)) {
            final int height = this.getEntityProperties().getHeightValue();
            final int width = this.getEntityProperties().getWidthValue();
            final BiFunction<Integer, Integer, Integer> increment = (x, y) -> {
                if (x < y) {
                    return x + 1;
                } else if (x > y) {
                    return x - 1;
                } else {
                    return x;
                }
            };
            final int h = increment.apply(height, this.getTargetHeight());
            this.getEntityProperties().setHeightValue(h);
            final int w = increment.apply(width, this.getTargetWidth());
            this.getEntityProperties().setWidthValue(w);
            int previousRaceTargetHeight = this.getEntityProperties().getPreviousRace().getRace().getRaceHeight();
            int previousRaceTargetWidth = this.getEntityProperties().getPreviousRace().getRace().getRaceWidth();
            double heightProgress = this.transformProgress(previousRaceTargetHeight, this.getTargetHeight(), height);
            double widthProgress = this.transformProgress(previousRaceTargetWidth, this.getTargetWidth(), width);
            double finalValue = this.isTransformed() ? 1D : StringUtils.getAccurateDouble(heightProgress * widthProgress);
            if ((finalValue >= 0D) && (finalValue <= 1D) && (progress != finalValue)) {
                progress = finalValue;
            }
        }
    }

    protected double transformProgress(int previousTarget, int currentTarget, int currentValue) {
        double rtn = (MathHelper.pct(currentValue + 0.0D, previousTarget + 0.0D, currentTarget + 0.0D));
        if (rtn < 0.01) {
            return 0D;
        }
        if (rtn > 1D) {
            return 1D;
        }
        return rtn;
    }

    /**
     * Jank McJank Eyeheight Handling.
     */
    protected void eyeHeightHandler() {
        if (!(entity instanceof EntityPlayer)) {
            return;
        }

        EntityPlayer player = (EntityPlayer) entity;

        if (!TrinketsConfig.CLIENT.cameraHeight) {
            this.resetEyeHeight(player);
            return;
        }

        if ((this.isTransforming() || this.isTransformed()) && !this.getEntityProperties().isNormalSize()) {
            // 165 when sneaking
            // 162 eyeheight, sneaking is -0.8
            float f = (float) StringUtils.getAccurateDouble(((this.getHeight() * 0.85F)));

            if (player.isPlayerSleeping()) {
                f = 0.2F;
            } else if (!player.isSneaking()) {
                if (player.isElytraFlying()) {
                    f *= 0.2F;//0.4F;
                }
            } else {
                f -= f / 20;//0.08F;
            }
            if (player.isRiding()) {
                final Entity mount = player.getRidingEntity();
                if (mount != null) {
                    final float mountHeight = mount.height;
                    //					final double mountOffset = mount.getMountedYOffset();
                    //					final double t = mountHeight - mountOffset;
                    //					if (f < mountHeight) {
                    //						f = mountHeight;
                    //					}
                    //					f += t;
                    f = MathHelper.clamp(f, mountHeight, f);
                }
            }
            player.eyeHeight = f;
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
        return targetHeight;
    }

    public void setTargetHeight(int targetHeight) {
        this.targetHeight = targetHeight;
    }

    public int getTargetWidth() {
        return targetWidth;
    }

    public void setTargetWidth(int targetWidth) {
        this.targetWidth = targetWidth;
    }

    public float getHeight() {
        final float TLHeight = (float) (this.getEntityProperties().getDefaultHeight() * (this.getEntityProperties().getHeightValue() * 0.01));
        return TLHeight;//(float) StringUtils.getAccurateDouble(TLHeight, properties.getDefaultHeight());
    }

    public float getWidth() {
        final float TLWidth = (float) (this.getEntityProperties().getDefaultWidth() * (this.getEntityProperties().getWidthValue() * 0.01));
        return TLWidth;//(float) StringUtils.getAccurateDouble(TLWidth, properties.getDefaultWidth());
    }

    @Nullable
    private SizeAttribute getArtemisAttributeSize() {
        if (Trinkets.MOD_COMPAT.ArtemisLib && TrinketsConfig.compat.artemislib) {
            final double h = (this.getTargetHeight() - 100) * 0.01D;
            final double w = (this.getTargetWidth() - 100) * 0.01D;
            return new SizeAttribute(entity, h, w, 0);
        }
        return null;
    }

    public void copyFrom(@Nonnull EntityRacePropertiesHandler source, boolean wasDeath, boolean keepInv) {
        final boolean isNormal = getRace().isNone();
        if (!isNormal) {
            progress = source.progress;
            if (getRaceCache().compareRace(source.getRaceCache())) {
                showTraits = source.showTraits;
                traitPrimaryColor = source.traitPrimaryColor;
                traitSecondaryColor = source.traitSecondaryColor;
                traitVariant = source.traitVariant;
                targetHeight = source.targetHeight;
                targetWidth = source.targetWidth;
            }
        }
    }

    public boolean canFly() {
        return getRace().canFly();
    }

    /*------------------------------------------Race Handlers--------------------------------------------*/

    @Override
    public NBTTagCompound savedNBTData(NBTTagCompound compound) {
        if (!getRace().isNone()) {
            final String key = getRace().getRegistryName().toString();
            final NBTTagCompound tag = new NBTTagCompound();
            tag.setBoolean("ShowTrait", showTraits);
            tag.setInteger("ColorPrimary", traitPrimaryColor);
            tag.setInteger("ColorSecondary", traitSecondaryColor);
            tag.setInteger("TraitVariant", traitVariant);
            tag.setDouble("TransformationProgress", progress);
            compound.setTag(key, tag);
        }
        return compound;
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        if (!getRace().isNone()) {
            final String key = getRace().getRegistryName().toString();
            if (compound.hasKey(key)) {
                final NBTTagCompound tag = compound.getCompoundTag(key);
                NBTHelper.hasBoolean(tag, "ShowTrait", (bool) -> {
                    showTraits = bool;
                });
                NBTHelper.hasInteger(tag, "ColorPrimary", (color) -> {
                    traitPrimaryColor = color;
                });
                NBTHelper.hasInteger(tag, "ColorSecondary", (color) -> {
                    traitSecondaryColor = color;
                });
                NBTHelper.hasInteger(tag, "TraitVariant", (variant) -> {
                    traitVariant = variant;
                });
                NBTHelper.hasInteger(tag, "TransformationProgress", (prog) -> {
                    this.progress = prog;
                });
            }
        }
    }

    public boolean showTraits() {
        return showTraits;
    }

    public void setShowTraits(boolean showTraits) {
        this.showTraits = showTraits;
    }

    public int getPrimaryTraitColor() {
        return traitPrimaryColor;
    }

    public void setPrimaryTraitColor(int color) {
        traitPrimaryColor = color;
    }

    public int getSecondaryTraitColor() {
        return traitSecondaryColor;
    }

    public void setSecondaryTraitColor(int color) {
        traitSecondaryColor = color;
    }

    public int getTraitVariant() {
        return traitVariant;
    }

    /**
     * Lazy way to solve problem for cosmetic slider issue.
     *
     * @return
     */
    public int getMaxTraitVariant() {
        if (traitVariantMax <= 0) {
            traitVariantMax = 1;
        }
        return traitVariantMax;
    }

    public void setTraitVariant(int variant) {
        traitVariant = variant;
    }

    public void setMaxTraitVariant(int variant) {
        traitVariantMax = variant;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getDescription(List<String> tooltips, int rendMod, int rendID) {
        String translationKey = raceCache.getRace().getTranslationKey();
        final TranslationHelper helper = TranslationHelper.INSTANCE;
        for (int i = 1; i <= 10; i++) {
            final String string = helper.getLangTranslation(translationKey + ".tooltip" + i, (lang) -> {
                final TranslationHelper.KeyEntry key1 = new TranslationHelper.OptionEntry("element", true, raceCache.getElement().getDisplayName());
                return helper.formatAddVariable(lang, EnumRenderLocation.GUI.getId(), key1);
            });
            if (!helper.isStringEmpty(string)) {
                tooltips.add(string);
            }
        }
    }

    @Override
    public String getDisplayName() {
        return raceCache.getRace().getDisplayName();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IRenderRaceHandler getRaceRenderer() {
        if (this.RendererRace == null) {
            this.RendererRace = new RaceEmptyRenderer(entity, new EmptyHandler(entity));
        }
        return RendererRace;
    }

}
