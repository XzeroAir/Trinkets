package xzeroair.trinkets.races;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
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
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.attributes.UpdatingAttribute;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.race.EntityProperties.RaceCache;
import xzeroair.trinkets.client.gui.entityPropertiesGui.GuiEntityProperties;
import xzeroair.trinkets.client.gui.hud.mana.ManaHud;
import xzeroair.trinkets.network.IncreasedReachPacket;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.artemislib.SizeAttribute;
import xzeroair.trinkets.util.config.ConfigHelper;
import xzeroair.trinkets.util.config.ConfigHelper.AttributeEntry;
import xzeroair.trinkets.util.handlers.SizeHandler;
import xzeroair.trinkets.util.helpers.NBTHelper;
import xzeroair.trinkets.util.helpers.RayTraceHelper;
import xzeroair.trinkets.util.helpers.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiFunction;

public abstract class EntityRacePropertiesHandler implements IRaceHandler {

    protected boolean firstUpdate;
    protected boolean firstTransformUpdate;

    protected EntityLivingBase entity;

    protected int targetWidth = 100;
    protected int targetHeight = 100;

    protected RaceCache raceCache;

    protected boolean showTraits;
    protected int traitPrimaryColor;
    protected int traitSecondaryColor;
    protected int traitVariant;
    protected int traitVariantMax;

    protected EntityProperties properties;

    protected float healthBeforeTransformation; // TODO Store the Health before, then calculate the health afterwards
    protected float maxHealthBeforeTranformation;

    protected double progress = 0D;

    public EntityRacePropertiesHandler(@Nonnull EntityLivingBase e, @Nonnull RaceCache cache) {
        entity = e;
        firstUpdate = true;
        firstTransformUpdate = true;
        showTraits = true;
        this.raceCache = cache;
        traitPrimaryColor = cache.getRace().getPrimaryColor();
        traitSecondaryColor = cache.getRace().getSecondaryColor();
        traitVariant = 0;
        traitVariantMax = 3;
        this.setTargetHeight(cache.getRace().getRaceHeight());
        this.setTargetWidth(cache.getRace().getRaceWidth());
    }

    public EntityRacePropertiesHandler(EntityLivingBase e, EntityRace race) {
        this(e, new RaceCache(race));
    }

    protected void initAttributes() {
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

    public EntityRacePropertiesHandler setEntityProperties(EntityProperties properties) {
        this.properties = properties;
        return this;
    }

    protected EntityProperties getEntityProperties() {
        if (properties != null) {
            return properties;
        } else {
            EntityProperties tmp = Capabilities.getEntityProperties(entity);
            if (tmp == null) {
                tmp = new EntityProperties(entity);
            }
            return tmp;
        }
    }

    public EntityRacePropertiesHandler setFirstUpdate(boolean firstUpdate) {
        this.firstUpdate = firstUpdate;
        return this;
    }

    public EntityRace getRace() {
        return this.getRaceCache().getRace();
    }

    public RaceCache getRaceCache() {
        return raceCache;
    }

    public void addAbility(IAbilityInterface ability) {
        if (ability.getRequiredElement() != null) {
            if (!getEntityProperties().getCurrentRace().compareElement(ability.getRequiredElement())) {
                return;
            }
        }
        this.getEntityProperties().getAbilityHandler().registerRaceAbility(this.getRace().getRegistryName().toString(), ability);
    }

    @Nullable
    public IAbilityInterface getAbility(String ability) {
        return this.getEntityProperties().getAbilityHandler().getAbility(ability);
    }

    public void removeAbility(String ability) {
        this.getEntityProperties().getAbilityHandler().removeAbility(ability);
    }

    /**
     * Use {@link #startTransformation()} instead
     */
    public void onTransform() {
        firstTransformUpdate = true;
        healthBeforeTransformation = entity.getHealth();
        maxHealthBeforeTranformation = entity.getMaxHealth();
        this.startTransformation();
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
    }

    public void onTick() {
        this.updateSize();
        SizeHandler.setSize(entity, this.getHeight(), this.getWidth());
        this.initAttributes();
        this.eyeHeightHandler();
        if (this.isTransformed()) {
            if (firstTransformUpdate && !entity.world.isRemote) {
                float newMaxHealth = entity.getMaxHealth();
                float difference = healthBeforeTransformation - maxHealthBeforeTranformation;
                float healAmount = (maxHealthBeforeTranformation - newMaxHealth) + difference;
                if (healAmount > 0) {
                    entity.heal(healAmount);
                }
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
        //false;//this.getSize() != this.getTargetSize();
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
            tag.setBoolean("trait_shown", showTraits);
            tag.setInteger("ColorPrimary", traitPrimaryColor);
            tag.setInteger("ColorSecondary", traitSecondaryColor);
            tag.setInteger("trait_variant", traitVariant);
            tag.setDouble("transformation_progress", progress);
            compound.setTag(key, tag);
        }
        return compound;
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        if (!getRace().isNone()) {
            final String key = getRace().getRegistryName().toString();
            if (compound.hasKey(key)) {
                final NBTTagCompound rTag = compound.getCompoundTag(key);
                NBTHelper.hasBoolean(rTag, "trait_shown", (bool) -> {
                    showTraits = bool;
                });
//                if (rTag.hasKey("trait_shown")) {
//                    showTraits = rTag.getBoolean("trait_shown");
//                }
                NBTHelper.hasInteger(rTag, "ColorPrimary", (color) -> {
                    traitPrimaryColor = color;
                });
                NBTHelper.hasInteger(rTag, "ColorSecondary", (color) -> {
                    traitSecondaryColor = color;
                });
//                if (rTag.hasKey("trait_color")) {
//                    traitColor = rTag.getString("trait_color");
//                }
//                if (rTag.hasKey("trait_color_alt")) {
//                    traitColorAlt = rTag.getString("trait_color_alt");
//                }
                NBTHelper.hasInteger(rTag, "TraitVariant", (variant) -> {
                    traitVariant = variant;
                });
//                if (rTag.hasKey("trait_variant")) {
//                    traitVariant = rTag.getInteger("trait_variant");
//                }
                NBTHelper.hasInteger(rTag, "TransformationProgress", (progress) -> {
                    progress = progress;
                });
//                if (rTag.hasKey("transformation_progress")) {
//                    progress = rTag.getDouble("transformation_progress");
//                }
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

    /*
     * Player Entities Only
     */
    @Override
    public void doRenderPlayerPre(EntityPlayer entity, double x, double y, double z, RenderPlayer renderer, float partialTick) {
        final GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        if ((entity == Minecraft.getMinecraft().player) && (screen != null) && !((screen instanceof GuiChat) || (screen instanceof GuiEntityProperties) || (screen instanceof ManaHud))) {
            return;
        }
        if ((this.isTransforming() || this.isTransformed()) && !this.getEntityProperties().isNormalSize()) {
            final double hScale = this.getEntityProperties().getHeightValue() * 0.01D;
            final double wScale = this.getEntityProperties().getWidthValue() * 0.01D;
            final double xLoc = (x / wScale) - x;
            final double yLoc = (y / hScale) - y;
            final double zLoc = (z / wScale) - z;

            final double yOffset = entity.getYOffset();
            final Entity mount = entity.getRidingEntity();
            //			double vanillaOffset = mount.posY + mount.getMountedYOffset() + entity.getYOffset();// + 0.15 * prevRearingAmount
            final double mountedOffset = entity.isRiding() && (mount != null) ? (mount.getMountedYOffset()) : 0;
            //			final double offsetDifference = entity.isRiding() && (mount != null) ? (mount.height - mountedOffset) : 0;
            //						final double retMountedOffset = mountedOffset - ((offsetDifference) * 0.66D);
            final double retMountedOffset = -(mountedOffset + yOffset) - 0.1D;
            if (entity.isRiding()) {
                GlStateManager.translate(0, mountedOffset, 0);
                GlStateManager.translate(0, -yOffset, 0);
                GlStateManager.translate(0, retMountedOffset, 0);
            }
            GlStateManager.scale(wScale, hScale, wScale);
            if (entity.isRiding()) {
                GlStateManager.translate(0, -retMountedOffset, 0);
                GlStateManager.translate(0, yOffset, 0);
                GlStateManager.translate(0, -mountedOffset, 0);
            }
            GlStateManager.translate(xLoc, yLoc, zLoc);
        }
    }

    /*
     * Player Entities Only
     */
    @Override
    public void doRenderPlayerPost(EntityPlayer entity, double x, double y, double z, RenderPlayer renderer, float partialTick) {
    }

    /*
     * Both Player and Non Player Entities
     */
    @Override
    public <T extends EntityLivingBase> void doRenderLivingSpecialsPre(EntityLivingBase entity, double x, double y, double z, RenderLivingBase<T> renderer, float partialTick) {
        if (entity instanceof EntityPlayer) {
            if ((this.isTransforming() || this.isTransformed()) && !this.getEntityProperties().isNormalSize()) {
                GlStateManager.pushMatrix();
                final float t2 = this.getEntityProperties().getDefaultHeight() - (entity.height);
                GlStateManager.translate(0, t2, 0);
            }
        }
    }

    /*
     * Both Player and Non Player Entities
     */
    @Override
    public <T extends EntityLivingBase> void doRenderLivingSpecialsPost(EntityLivingBase entity, double x, double y, double z, RenderLivingBase<T> renderer, float partialTick) {
        if (entity instanceof EntityPlayer) {
            if ((this.isTransforming() || this.isTransformed()) && !this.getEntityProperties().isNormalSize()) {
                GlStateManager.popMatrix();
            }
        }
    }

    /*
     * Non Player Entities Only
     */
    @Override
    public <T extends EntityLivingBase> void doRenderLivingPre(EntityLivingBase entity, double x, double y, double z, RenderLivingBase<T> renderer, float partialTick) {
        if ((this.isTransforming() || this.isTransformed()) && !this.getEntityProperties().isNormalSize()) {
            GlStateManager.pushMatrix();
            final double hScale = this.getEntityProperties().getHeightValue() * 0.01D;
            final double wScale = this.getEntityProperties().getWidthValue() * 0.01D;
            final double xLoc = (x / wScale) - x;
            final double yLoc = (y / hScale) - y;
            final double zLoc = (z / wScale) - z;

            final double yOffset = entity.getYOffset();
            final Entity mount = entity.getRidingEntity();
            //			double vanillaOffset = mount.posY + mount.getMountedYOffset() + entity.getYOffset();// + 0.15 * prevRearingAmount
            final double mountedOffset = entity.isRiding() && (mount != null) ? (mount.getMountedYOffset()) : 0;
            //			final double offsetDifference = entity.isRiding() && (mount != null) ? (mount.height - mountedOffset) : 0;
            //						final double retMountedOffset = mountedOffset - ((offsetDifference) * 0.66D);
            final double retMountedOffset = -(mountedOffset + yOffset) - 0.1D;
            //			GlStateManager.translate(-xLoc, -yLoc, -zLoc);
            if (entity.isRiding()) {
                GlStateManager.translate(0, mountedOffset, 0);
                GlStateManager.translate(0, -yOffset, 0);
                GlStateManager.translate(0, retMountedOffset, 0);
            }
            GlStateManager.scale(wScale, hScale, wScale);
            if (entity.isRiding()) {
                GlStateManager.translate(0, -retMountedOffset, 0);
                GlStateManager.translate(0, yOffset, 0);
                GlStateManager.translate(0, -mountedOffset, 0);
            }
            GlStateManager.translate(xLoc, yLoc, zLoc);
            //			System.out.println(hScale + "|" + wScale + "| X:" + xLoc + "| Y:" + yLoc + "| Z:" + zLoc);
        }
    }

    /*
     * Non Player Entities Only
     */
    @Override
    public <T extends EntityLivingBase> void doRenderLivingPost(EntityLivingBase entity, double x, double y, double z, RenderLivingBase<T> renderer, float partialTick) {
        if ((this.isTransforming() || this.isTransformed()) && !this.getEntityProperties().isNormalSize()) {
            GlStateManager.popMatrix();
        }
    }

    //	@Override
    //	public void doRenderHand(EnumHand hand, ItemStack itemStack, float swingProgress, float interpolatedPitch, float equipProgress, float partialTicks) {
    //	}

}
