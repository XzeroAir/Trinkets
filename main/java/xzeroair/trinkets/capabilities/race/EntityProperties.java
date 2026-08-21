package xzeroair.trinkets.capabilities.race;

import com.google.common.base.Objects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.api.events.TransformationEvent;
import xzeroair.trinkets.api.events.TransformationEvent.EndTransformation;
import xzeroair.trinkets.api.events.TransformationEvent.RaceUpdateEvent;
import xzeroair.trinkets.api.events.TransformationEvent.StartTransformation;
import xzeroair.trinkets.attributes.FlyingAttribute;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.attributes.UpdatingAttribute;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.CapabilityEntityBase;
import xzeroair.trinkets.entity.AlphaWolf;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.SyncRaceDataPacket;
import xzeroair.trinkets.network.trinketcontainer.OpenTrinketGui;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.IRaceProvider;
import xzeroair.trinkets.traits.AbilityHandler;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.handlers.Counter;
import xzeroair.trinkets.util.helpers.AttributeHelper;
import xzeroair.trinkets.util.helpers.EntityHelper;
import xzeroair.trinkets.util.helpers.NBTHelper;
import xzeroair.trinkets.util.helpers.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityProperties extends CapabilityEntityBase<EntityProperties, EntityLivingBase> {

    public static final String TAG_KEY = Reference.MODID + ":race";

    protected boolean first_login = true;
    protected boolean login = true;

    protected boolean sync = false;
    protected boolean syncTracking = false;
    protected int raceSelectionAuthorizationTicks = 0;

    protected RaceCache originalRace = new RaceCache();
    protected RaceCache imbuedRace = new RaceCache();
    protected RaceCache potionRace = new RaceCache();
    protected RaceCache currentRace = new RaceCache();
    protected RaceCache previousRace = new RaceCache();

    protected int widthValue = 100;
    protected int heightValue = 100;
    protected float defaultWidth = 1.8F;
    protected float defaultHeight = 0.6F;

    protected boolean isFake = false;

    protected KeybindHandler keybindHandler;
    protected EntityRacePropertiesHandler properties;
    protected final AbilityHandler abilities;

    protected float stepHeightPrev = 0.6F;
    protected boolean isChild;
    protected BlockPos prevBlockpos;
    @Nullable
    protected World activePlayerWorld;

    protected ClientInfo clientInfo;

    public EntityProperties(EntityLivingBase e) {
        super(e);
        this.widthValue = 100;
        this.heightValue = 100;
        this.defaultHeight = e.height;
        this.defaultWidth = e.width;
        this.isChild = e.isChild();
        this.originalRace = new RaceCache();
        this.imbuedRace = this.originalRace;
        this.potionRace = this.originalRace;
        this.previousRace = this.originalRace;
        this.currentRace = this.originalRace;
        this.abilities = new AbilityHandler(this);
        this.properties = this.currentRace.getRace().getRaceHandler(e, this, this.currentRace);
        this.clientInfo = new ClientInfo();
    }

    @Override
    public NBTTagCompound getTag() {
        final NBTTagCompound tag = NBTHelper.getEntityTag(this.getEntity());
        if (!NBTHelper.hasTagCompound(tag, TAG_KEY)) {
            tag.setTag(TAG_KEY, new NBTTagCompound());
        }
        return tag.getCompoundTag(TAG_KEY);
    }

    // ABILITIES
    public AbilityHandler getAbilityHandler() {
        return this.abilities;
    }

    // ABILITIES END

    @Override
    public void onUpdatePre() {
        this.getAbilityHandler().onUpdatePre(this.getEntity());
    }

    @Override
    public void onUpdate() {
        final World world = this.getEntity().getEntityWorld();

        final boolean isClient = world.isRemote;

        if (!(this.getEntity() instanceof FakePlayer) && (this.getEntity() instanceof EntityPlayer)) {
            if (!this.isReadyForPlayerUpdate(world, (EntityPlayer) this.getEntity())) {
                return;
            }
            this.onPlayerUpdate(world, (EntityPlayer) this.getEntity());
        } else {
            return;
        }

        if (!isClient && !this.isNormalSize() && !this.getEntity().onGround) {
            final boolean groundProbe = this.isGrounded();
            if (groundProbe) {
                this.getEntity().onGround = true;
            }
        }

        if (isClient && TrinketsConfig.CLIENT.debug.showMovementSpeed) {
            StringUtils.sendStatusMessageToPlayer(this.getEntity(), "Bp/t:" + this.entitySpeed(this.getEntity()), true);
        }
        this.stepHeightHandler();
        this.flySpeedHandler();
        if (!isClient && (this.raceSelectionAuthorizationTicks > 0)) {
            this.raceSelectionAuthorizationTicks--;
        }
        if (this.sync) {
            this.sync = false;
            this.sendInformationToPlayer(this.getEntity());
            this.sendHealthStateToPlayer();
            this.scheduleResyncTracking();
        }
        if (this.syncTracking) {
            this.sendInformationToTracking();
            this.syncTracking = false;
        }

        this.setLogin(false);
        this.setFirstLogin(false);
        if (!isClient) {
            BlockPos blockpos = new BlockPos(this.getEntity());

            if (!Objects.equal(this.prevBlockpos, blockpos)) {
                this.prevBlockpos = blockpos;
            }
        }
    }

    // Wait for the player list only while entering a world; later ticks only need to reject chunk removal.
    private boolean isReadyForPlayerUpdate(World world, EntityPlayer player) {
        if (this.activePlayerWorld == world && player.addedToChunk) {
            return true;
        }
        if (!player.addedToChunk || !world.playerEntities.contains(player)) {
            this.activePlayerWorld = null;
            return false;
        }
        this.activePlayerWorld = world;
        return true;
    }

    public void onPlayerUpdate(World world, EntityPlayer player) {

        if (this.isLogin()) {
            this.getRaceHandler().setFirstUpdate(true);
        }

        this.getAbilityHandler().onUpdate(player);
        this.updateRace();
        this.getRaceHandler().onTick();
        this.getAbilityHandler().onUpdatePost(player);

    }

    public void updateRace() {
        final World world = this.getEntity().getEntityWorld();
        final boolean isClient = world.isRemote;
        final Counter counter = this.getTickHandler().getCounter("TempRace");
        if (counter != null) {
            if (counter.Tick()) {
                this.setPotionRaceCache(null);
                this.getTickHandler().removeCounter("TempRace");
            }
        }
        if (isClient) {
            return;
        }
        final RaceCache raceCache = this.getEntityRaceWithDetails();
        final TransformationEvent.RaceUpdateEvent UpdateEvent = new RaceUpdateEvent(this.getEntity(), this, raceCache);
        // If the Event is not canceled, Continue.
        if (!MinecraftForge.EVENT_BUS.post(UpdateEvent)) {
            // If the race is different from the current race.
            if (UpdateEvent.raceChanged()) {
                // Get the new race
                RaceCache newRace = UpdateEvent.getNewRaceCache();
                // Check to see if it's null
                if (newRace == null) {
                    newRace = new RaceCache();
                }
                // Get the players current race handler and information.
                // Start the end transformation event for the current race.
                final TransformationEvent.EndTransformation end = new EndTransformation(this.getEntity(), this, this.getCurrentRaceCache());
                // Bug? Supposed to only continue if the event is not canceled.
                if (MinecraftForge.EVENT_BUS.post(end)) {
                    return;
                }
                final EntityRacePropertiesHandler oldProperties = this.getRaceHandler();
                // RUn all the end race transformation methods and remove attributes.
                oldProperties.onTransformEnd();
                AttributeHelper.removeAttributesByUUID(this.getEntity(), this.getPreviousRaceCache().getRace().getUUID(), oldProperties.getRace().getUUID());
                this.setPreviousRaceCache(this.getCurrentRaceCache());
                final Entity mount = this.getEntity().getRidingEntity();
                if (mount instanceof AlphaWolf) {
                    this.getEntity().dismountRidingEntity();
                }
                final IAttributeInstance stepHeight = this.getEntity().getAttributeMap().getAttributeInstance(JumpAttribute.stepHeight);
                if ((stepHeight != null)) {
                    if (this.getEntity().stepHeight != stepHeight.getBaseValue()) {
                        this.getEntity().stepHeight = (float) stepHeight.getBaseValue();
                    }
                }
                // officially start new transformation.
                this.properties = newRace.getRace().getRaceHandler(this.getEntity(), this, newRace);
                this.properties.loadNBTData(this.getTag());

                this.setCurrentRaceCache(newRace);
                this.properties.onTransform();

                if (newRace.isTemporary() && newRace.getDuration() > 0) {
                    Counter tempPot = this.getTickHandler().getCounter("TempRace", newRace.getDuration(), true, true, true, true);
                    tempPot.resetTick();
                }

                // Trigger start Transformation event. Maybe add a cancel scenario?
                final TransformationEvent.StartTransformation start = new StartTransformation(this.getEntity(), this, this.getCurrentRaceCache());
                MinecraftForge.EVENT_BUS.post(start);
                this.scheduleResync();
            }
        }
    }

    public BlockPos getPrevBlockpos() {
        if (this.prevBlockpos == null) {
            this.prevBlockpos = this.getEntity().getPosition();
        }
        return this.prevBlockpos;
    }

    public KeybindHandler getKeybindHandler() {
        if (this.keybindHandler == null) {
            this.keybindHandler = new KeybindHandler(this);
        }
        return this.keybindHandler;
    }

    private EntityRace getEntityRace() {
        return this.getEntityRaceWithDetails().getRace();
    }

    private Element getEntityElement() {
        return this.getEntityRaceWithDetails().getPrimaryElement();
    }

    private Element getSecondaryElement() {
        return this.getEntityRaceWithDetails().getSecondaryElement();
    }

    private RaceCache getEntityRaceWithDetails() {
        this.setFake(true);
        final RaceCache potionRace = this.getPotionRaceCache();
        if ((potionRace != null) && !potionRace.getRace().isNone()) {
            return potionRace;
        }
        ItemStack provider = this.getRaceProvider();
        if (!provider.isEmpty()) {
            return new RaceCache(((IRaceProvider) provider.getItem()).getRace(), Capabilities.getTrinketProperties(provider, Elements.NEUTRAL, (prop, ele) -> prop.getElementalAttributes().getPrimaryElement()));
        }
        this.setFake(false);
        final RaceCache imbuedRace = this.getImbuedRaceCache();
        if ((imbuedRace != null) && !imbuedRace.getRace().isNone()) {
            return imbuedRace;
        }
        return this.getOriginalRaceCache();
    }

    public ItemStack getRaceProvider() {
        final int count = TrinketHelper.countAccessories(this.getEntity(), stack -> stack.getItem() instanceof IRaceProvider);
        if (count != 1) {
            return ItemStack.EMPTY;
        } else {
            return TrinketHelper.getAccessory(this.getEntity(), stack -> stack.getItem() instanceof IRaceProvider);
        }
    }

    /**
     * Handles the Step Height Attribute.
     * I really don't understand why this works.
     * avoid touching it.
     */
    private void stepHeightHandler() {
        final IAttributeInstance attribute = this.getEntity().getEntityAttribute(JumpAttribute.stepHeight);
        if ((attribute != null) && !attribute.getModifiers().isEmpty()) {
            final float defaultStepHeight = (float) attribute.getBaseValue();
            final float attributeStepHeight = (float) attribute.getAttributeValue();
            final float currentStepHeight = this.getEntity().stepHeight;
            this.getEntity().stepHeight = defaultStepHeight;
            if ((currentStepHeight - this.getEntity().stepHeight) == 0F) {
                this.getEntity().stepHeight = attributeStepHeight;
                this.stepHeightPrev = defaultStepHeight;
            } else if (((currentStepHeight - this.getEntity().stepHeight) - attributeStepHeight) == -defaultStepHeight) {
                this.getEntity().stepHeight = attributeStepHeight;
                this.stepHeightPrev = defaultStepHeight;
            } else {
                final float stepP = this.stepHeightPrev;
                this.stepHeightPrev = currentStepHeight;
                if (((attributeStepHeight - defaultStepHeight) + currentStepHeight) == ((attributeStepHeight - defaultStepHeight) + stepP)) {
                    this.getEntity().stepHeight = (attributeStepHeight - defaultStepHeight) + this.stepHeightPrev;
                }
            }
        }
    }

    private void flySpeedHandler() {
        if (this.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) this.getEntity();
            if (player.isCreative() || player.isSpectator()) {
                return;
            }
            final IAttributeInstance attribute = player.getAttributeMap().getAttributeInstance(FlyingAttribute.Fly_Speed);
            if ((attribute != null) && !attribute.getModifiers().isEmpty()) {
                if (player.getEntityWorld().isRemote) {
                    final float flySpeed = player.capabilities.getFlySpeed();
                    float newSpeed = (float) StringUtils.getAccurateDouble(attribute.getAttributeValue());
                    if (flySpeed != newSpeed) {
                        player.capabilities.setFlySpeed(newSpeed);
                    }
                }
            }
        }
    }

    // Speed Checks
    protected double prev_posx;
    protected double prev_posy;
    protected double prev_posz;

    public void startVec(@Nonnull EntityLivingBase entity) {
        this.prev_posx = entity.posX;
        this.prev_posy = entity.posY;
        this.prev_posz = entity.posZ;
    }

    public Vec3d lastVec() {
        return new Vec3d(this.prev_posx, this.prev_posy, this.prev_posz);
    }

    public double entitySpeed(@Nonnull EntityLivingBase entity) {
        final Vec3d currentPosVec = new Vec3d(entity.posX, entity.posY, entity.posZ);
        final double distanceTraveled = this.lastVec().distanceTo(currentPosVec);

        this.startVec(entity);

        return distanceTraveled;
    }

    public double getHorizontalSpeed() {
        final EntityLivingBase entity = this.getEntity();
        final double x = entity.posX - entity.prevPosX;
        final double z = entity.posZ - entity.prevPosZ;
        return Math.sqrt((x * x) + (z * z));
    }
    // Speed Checks END

    public boolean isGrounded() {
        final EntityLivingBase entity = this.getEntity();
        final EntityRacePropertiesHandler handler = this.getRaceHandler();

        if ((Trinkets.MOD_COMPAT.ArtemisLib && TrinketsConfig.compat.ARTEMIS_LIB)
                || this.isChild()
                || (!handler.isTransforming() && !handler.isTransformed())) {
            return EntityHelper.isGrounded(entity);
        }

        return EntityHelper.isGrounded(entity, handler.getAdjustedBoundingBox());
    }

    /**
     * Send Player information on Login
     *
     */
    @Override
    public void onLogin() {
        final World world = this.getEntity().getEntityWorld();
        if (!world.isRemote && (this.getEntity() instanceof EntityPlayerMP)) {
            if (this.isFirstLogin()) {
                if (TrinketsConfig.SERVER.RACES.SELECTION_MENU) {
                    this.authorizeRaceSelection();
                    NetworkHandler.sendTo(new OpenTrinketGui(Reference.GUI_RACE_SELECTION), (EntityPlayerMP) this.getEntity());
                }
//            } else {
//                this.sendInformationToPlayer(this.getEntity(), this.getTag());
            }
        }
    }

    @Override
    public void onJoinWorld() {
        this.sendInformationToPlayer(this.getEntity(), this.saveToNBT(this.getTag()));
        this.sendHealthStateToPlayer();
    }

    @Override
    public void onChangedDimension(int from, int to) {
        this.activePlayerWorld = null;
        this.scheduleResync();
    }

    /**
     * Force Dismount Goblins
     *
     */
    @Override
    public void onLogoff() {
        this.activePlayerWorld = null;
        if (this.getCurrentRaceCache().compareRace(EntityRaces.goblin)) {
            if (this.getEntity().getRidingEntity() instanceof AlphaWolf) {
                this.getEntity().dismountRidingEntity();
            }
        }
    }

    public void sendInformationToPlayer() {
        this.sendInformationToPlayer(this.getEntity(), this.saveToNBT(this.getTag()));
    }

    public void sendInformationToPlayer(EntityLivingBase receiver) {
        final World world = this.getEntity().getEntityWorld();
        if (!world.isRemote) {
            this.sendInformationToPlayer(receiver, this.saveToNBT(this.getTag()));
        }
    }

    public void sendInformationToPlayer(EntityLivingBase receiver, NBTTagCompound tag) {
        final World world = this.getEntity().getEntityWorld();
        if (!world.isRemote) {
            NetworkHandler.sendTo(new SyncRaceDataPacket(this.getEntity(), tag), (EntityPlayerMP) receiver);
        }
    }

    public void sendInformationToTracking() {
        final World world = this.getEntity().getEntityWorld();
        if (!world.isRemote) {
            this.sendInformationToTracking(this.saveToNBT(this.getTag()));
        }
    }

    public void sendInformationToTracking(NBTTagCompound tag) {
        final World world = this.getEntity().getEntityWorld();
        if (!world.isRemote && (world instanceof WorldServer)) {
            final WorldServer w = (WorldServer) world;
            NetworkHandler.sendToClients(w, this.getEntity().getPosition(), new SyncRaceDataPacket(this.getEntity(), tag));
        }
    }

    /*-----------------------------------Boolean Checks-------------------------------*/

    /**
     * Avoid Using, kept purely for compatibility.
     */
    @Deprecated
    public int getSize() {
        return this.getHeightValue();
    }

    /**
     * A quick height check to see if the players height has changed.
     *
     */
    public boolean isNormalSize() {
        return (this.getEntity().height == this.getDefaultHeight()) && (this.getEntity().width == this.getDefaultWidth());
    }

    public boolean isBig() {
        return (this.getEntity().width > this.getDefaultWidth());
    }

    public boolean isSmall() {
        return (this.getEntity().height < this.getDefaultHeight());
    }

    public boolean hasRace() {
        return !this.currentRace.compareRace(EntityRaces.none);
    }

    /*-----------------------------------Boolean Checks-------------------------------*/

    /**
     * Get the Entity Race Handler
     */
    public EntityRacePropertiesHandler getRaceHandler() {
        if (this.properties == null) {
            this.properties = this.currentRace.getRace().getRaceHandler(this.getEntity(), this, this.currentRace);
        }
        return this.properties;
    }

    public EntityRace getPreviousRace() {
        return this.getPreviousRaceCache().getRace();
    }

    public RaceCache getPreviousRaceCache() {
        return this.previousRace;
    }

    public void setPreviousRaceCache(RaceCache cache) {
        if (cache == null) {
            cache = new RaceCache();
        }
        if (!this.previousRace.compare(cache)) {
            this.previousRace = cache;
        }
    }

    /**
     * Get the current race of the Entity
     *
     */
    public RaceCache getCurrentRaceCache() {
        return this.currentRace;
    }

    public EntityRace getCurrentRace() {
        return this.getCurrentRaceCache().getRace();
    }

    public void setCurrentRaceCache(RaceCache cache) {
        if (cache == null) {
            cache = new RaceCache();
        }
        if (!this.currentRace.compare(cache)) {
            this.currentRace = cache;
        }
    }

    /**
     * Get the current race of the entity given by eating a transformation item.
     *
     */
    public RaceCache getImbuedRaceCache() {
        return this.imbuedRace;
    }

    public void setImbuedRaceCache(@Nullable RaceCache cache) {
        if (cache == null) {
            cache = new RaceCache();
        }
        if (!this.imbuedRace.compare(cache)) {
            this.imbuedRace = cache;
        }
    }

    public RaceCache getPotionRaceCache() {
        return this.potionRace;
    }

    public void setPotionRaceCache(@Nullable RaceCache cache) {
        if (cache == null) {
            cache = new RaceCache();
        }
        if (!this.potionRace.compare(cache)) {
            this.potionRace = cache;
        }
    }

    public EntityRace getOriginalRace() {
        return this.getOriginalRaceCache().getRace();
    }

    /**
     * Get the entities original race. By default, this is EntityRaces.none.
     * This changes based on if the race selection menu is enabled.
     *
     */
    public RaceCache getOriginalRaceCache() {
        return this.originalRace;
    }

    public void setOriginalRaceCache(@Nullable RaceCache cache) {
        if (cache == null) {
            cache = new RaceCache();
        }
        if (!this.originalRace.compare(cache)) {
            this.originalRace = cache;
        }
    }

    /**
     * Get the Entities default width.
     *
     */
    public float getDefaultWidth() {
        return this.defaultWidth;
    }

    private void setDefaultWidth(float defaultWidth) {
        if (this.defaultWidth != defaultWidth) {
            this.defaultWidth = defaultWidth;
        }
    }

    /**
     * Get the Entities default height.
     *
     */
    public float getDefaultHeight() {
        return this.defaultHeight;
    }

    private void setDefaultHeight(float defaultHeight) {
        if (this.defaultHeight != defaultHeight) {
            this.defaultHeight = defaultHeight;
        }
    }

    /**
     * Get the current height value based on percentage, the default value is 100
     *
     */
    public int getHeightValue() {
        return this.heightValue;
    }

    public void setHeightValue(int height) {
        if (this.heightValue != height) {
            this.heightValue = height;
        }
    }

    /**
     * Get the current height value based on percentage, the default value is 100
     *
     */
    public int getWidthValue() {
        return this.widthValue;
    }

    public void setWidthValue(int width) {
        if (this.widthValue != width) {
            this.widthValue = width;
        }
    }

    // Is Likely redundant and could be removed. Mainly kept for Non-Player Entities
    public boolean isLogin() {
        return this.login;
    }

    public void setLogin(boolean login) {
        if (this.login != login) {
            this.login = login;
        }
    }

    public boolean isFirstLogin() {
        return this.first_login;
    }

    public void setFirstLogin(boolean firstLogin) {
        if (this.first_login != firstLogin) {
            this.first_login = firstLogin;
        }
    }

    /**
     * Check to see if the entity is currently a fake race, this was originally added for a system that was never implemented.
     * an Entity with a Fake Race is typically one that is wearing a transformation item, It might also apply to potion transformations as well.
     *
     */
    public boolean isFake() {
        return this.isFake;
    }

    public void setFake(boolean isFake) {
        if (this.isFake != isFake) {
            this.isFake = isFake;
        }
    }

    /**
     * Sanity Check for Child Entities, Necessary due to how Minecraft handles child entities.
     *
     */
    public boolean isChild() {
        return this.isChild;
    }

    public void setChild(boolean isChild) {
        if (this.isChild != isChild) {
            this.isChild = isChild;
        }
    }

    /**
     * Schedule a forced resync.
     */
    public void scheduleResync() {
        this.sync = true;
    }

    public void scheduleResyncTracking() {
        this.syncTracking = true;
    }

    public void authorizeRaceSelection() {
        this.raceSelectionAuthorizationTicks = 20 * 60;
    }

    public boolean isRaceSelectionAuthorized() {
        return this.raceSelectionAuthorizationTicks > 0;
    }

    public void consumeRaceSelectionAuthorization() {
        this.raceSelectionAuthorizationTicks = 0;
    }

    private void sendHealthStateToPlayer() {
        UpdatingAttribute.syncMaxHealthState(this.getEntity(), this.getEntity().getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH));
    }

    @Override
    public void copyFrom(@Nonnull EntityProperties source, boolean wasDeath, boolean keepInv) {
        this.first_login = source.first_login;
        this.login = source.login;
        this.defaultWidth = source.defaultWidth;
        this.defaultHeight = source.defaultHeight;
        this.originalRace = source.originalRace;

        this.isChild = source.isChild;

        if (wasDeath && !keepInv) {
            this.previousRace = source.currentRace;
            this.currentRace = this.imbuedRace.getRace().isNone() ? this.originalRace : this.imbuedRace;
            this.heightValue = this.currentRace.getRace().getRaceHeight();
            this.widthValue = this.currentRace.getRace().getRaceWidth();
            this.properties = this.currentRace.getRace().getRaceHandler(this.getEntity(), this, this.currentRace);
            if (TrinketsConfig.SERVER.FOOD.KEEP_EFFECTS) {
                this.imbuedRace = source.imbuedRace;
            }
        } else {
            this.currentRace = source.currentRace;
            this.previousRace = source.previousRace;
            this.imbuedRace = source.imbuedRace;
            this.potionRace = source.potionRace;
            this.heightValue = source.heightValue;
            this.widthValue = source.widthValue;
            this.properties = this.currentRace.getRace().getRaceHandler(this.getEntity(), this, this.currentRace);
        }

        this.getRaceHandler().copyFrom(source.getRaceHandler(), wasDeath, keepInv);
        this.getAbilityHandler().copyFrom(source.getAbilityHandler(), wasDeath, keepInv);
        this.scheduleResync();
    }

    @Override
    public NBTTagCompound saveToNBT(NBTTagCompound compound) {
        this.getClientInfo().saveInfo(compound);
        compound.setTag("OriginalRace", this.getOriginalRaceCache().saveToNBT(new NBTTagCompound()));
        compound.setTag("ImbuedRace", this.getImbuedRaceCache().saveToNBT(new NBTTagCompound()));
        compound.setTag("PreviousRace", this.getPreviousRaceCache().saveToNBT(new NBTTagCompound()));
        compound.setTag("CurrentRace", this.getCurrentRaceCache().saveToNBT(new NBTTagCompound()));
        compound.setInteger("heightValue", this.getHeightValue());
        compound.setInteger("widthValue", this.getWidthValue());
        compound.setFloat("default_height", this.getDefaultHeight());
        compound.setFloat("default_width", this.getDefaultWidth());
        compound.setBoolean("first_login", this.isFirstLogin());
        compound.setBoolean("fake", this.isFake());
        compound.setBoolean("child", this.isChild());
        this.getRaceHandler().savedNBTData(compound);
        this.getAbilityHandler().saveToNBT(compound);
        return compound;
    }

    @Override
    public void loadFromNBT(NBTTagCompound compound) {
        this.getClientInfo().loadInfo(compound);
        boolean changed = false;
        NBTHelper.hasBoolean(compound, "first_login", value -> this.first_login = value);
        NBTHelper.hasTag(compound, "OriginalRace", tag -> this.originalRace = RaceCache.loadFromNBT(tag));
        NBTHelper.hasTag(compound, "ImbuedRace", tag -> this.imbuedRace = RaceCache.loadFromNBT(tag));
        NBTHelper.hasTag(compound, "PreviousRace", tag -> this.previousRace = RaceCache.loadFromNBT(tag));
        if (NBTHelper.hasTagCompound(compound, "CurrentRace")) {
            this.currentRace = RaceCache.loadFromNBT(compound.getCompoundTag("CurrentRace"));
            this.properties = this.currentRace.getRace().getRaceHandler(this.getEntity(), this, this.currentRace);
            changed = true;
        }
        NBTHelper.hasInteger(compound, "heightValue", value -> this.heightValue = value);
        NBTHelper.hasInteger(compound, "widthValue", value -> this.widthValue = value);
        NBTHelper.hasFloat(compound, "default_height", value -> this.defaultHeight = value);
        NBTHelper.hasFloat(compound, "default_width", value -> this.defaultWidth = value);
        NBTHelper.hasBoolean(compound, "fake", value -> this.isFake = value);
        NBTHelper.hasBoolean(compound, "child", value -> this.isChild = value);
        this.getRaceHandler().loadNBTData(compound);
        if (changed) {
            this.properties.initializeState();
        }
        this.getAbilityHandler().loadFromNBT(compound);
    }

    public ClientInfo getClientInfo() {
        return this.clientInfo;
    }

    public static class ClientInfo {
        private boolean onGround = true;
        private double smoothedMotion, amplitude, tiltX, tiltY, tiltZ, motion, forwardMotion, motionX, motionY, motionZ, lookVecX, lookVecY, lookVecZ = 0;

        public ClientInfo() {
        }

        @SideOnly(Side.CLIENT)
        public void updateInfo(@Nonnull EntityLivingBase entity, float partialTicks) {
            if (entity instanceof EntityLivingBase) {
                double dx = entity.posX - entity.prevPosX;
                double dy = entity.posY - entity.prevPosY;
                double dz = entity.posZ - entity.prevPosZ;
                this.onGround = entity.onGround;
                this.motionX = dx;
                this.motionY = dy;
                this.motionZ = dz;
                this.motion = Math.sqrt(dx * dx + dz * dz);
                Vec3d look = entity.getLook(partialTicks);
                this.forwardMotion = dx * look.x + dz * look.z;
                this.lookVecX = look.x;
                this.lookVecY = look.y;
                this.lookVecZ = look.z;
            }
        }

        public NBTTagCompound saveInfo() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setTag(TAG_KEY, new NBTTagCompound());
            return this.saveInfo(tag.getCompoundTag(TAG_KEY));
        }

        public NBTTagCompound saveInfo(@Nonnull NBTTagCompound tag) {
            NBTTagCompound info = new NBTTagCompound();
            info.setBoolean("onGround", this.onGround);
            info.setDouble("amplitude", this.amplitude);
            info.setDouble("motX", this.motionX);
            info.setDouble("motY", this.motionY);
            info.setDouble("motZ", this.motionZ);
            info.setDouble("lookX", this.lookVecX);
            info.setDouble("lookY", this.lookVecY);
            info.setDouble("lookZ", this.lookVecZ);
            info.setDouble("motion", this.motion);
            info.setDouble("forwardMotion", this.forwardMotion);
            tag.setTag("ClientInfo", info);
            return tag;
        }

        private void loadInfo(NBTTagCompound tag) {
            NBTHelper.hasTag(tag, "ClientInfo", (info) -> {
                NBTHelper.hasBoolean(info, "onGround", (bool) -> this.onGround = bool);
                NBTHelper.hasDouble(info, "amplitude", (value) -> this.amplitude = value);
                NBTHelper.hasDouble(info, "motion", (value) -> this.motion = value);
                NBTHelper.hasDouble(info, "forwardMotion", (value) -> this.forwardMotion = value);
                NBTHelper.hasDouble(info, "motX", (value) -> this.motionX = value);
                NBTHelper.hasDouble(info, "motY", (value) -> this.motionY = value);
                NBTHelper.hasDouble(info, "motZ", (value) -> this.motionZ = value);
                NBTHelper.hasDouble(info, "lookX", (value) -> this.lookVecX = value);
                NBTHelper.hasDouble(info, "lookY", (value) -> this.lookVecY = value);
                NBTHelper.hasDouble(info, "lookZ", (value) -> this.lookVecZ = value);
            });
        }

        public void resetInfo() {
            this.onGround = true;
            this.amplitude = 0D;
            this.motion = 0D;
            this.motionX = 0D;
            this.motionY = 0D;
            this.motionZ = 0D;
            this.forwardMotion = 0D;
            this.lookVecX = 0D;
            this.lookVecY = 0D;
            this.lookVecZ = 0D;
            this.tiltX = 0D;
            this.tiltY = 0D;
            this.tiltZ = 0D;
            this.smoothedMotion = 0D;
        }

        public boolean onGround() {
            return this.onGround;
        }

        public double getMotion() {
            return this.motion;
        }

        public double getMotionX() {
            return this.motionX;
        }

        public double getMotionY() {
            return this.motionY;
        }

        public double getMotionZ() {
            return this.motionZ;
        }

        public double getForwardMotion() {
            return this.forwardMotion;
        }

        public double getAmplitude() {
            return this.amplitude;
        }

        public double getTiltX() {
            return this.tiltX;
        }

        public double getTiltY() {
            return this.tiltY;
        }

        public double getTiltZ() {
            return this.tiltZ;
        }

        public double getLookVecX() {
            return this.lookVecX;
        }

        public double getLookVecY() {
            return this.lookVecY;
        }

        public double getLookVecZ() {
            return this.lookVecZ;
        }

        public ClientInfo setAmplitude(double amplitude) {
            this.amplitude = amplitude;
            return this;
        }

        public ClientInfo setSmoothedMotion(double smoothedMotion) {
            this.smoothedMotion = smoothedMotion;
            return this;
        }

        public ClientInfo setTiltX(double tiltX) {
            this.tiltX = tiltX;
            return this;
        }

        public ClientInfo setTiltY(double tiltY) {
            this.tiltY = tiltY;
            return this;
        }

        public ClientInfo setTiltZ(double tiltZ) {
            this.tiltZ = tiltZ;
            return this;
        }

        @Override
        public String toString() {
            return "OnGround:" + this.onGround() + ", motX:" + this.getMotionX() + ", motY:" + this.getMotionY() + ", motZ:" + this.getMotionZ() + ", Motion:" + this.getMotion();
        }
    }
}
