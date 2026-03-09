package xzeroair.trinkets.capabilities.race;

import com.google.common.base.Objects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation;
import xzeroair.trinkets.api.events.TransformationEvent;
import xzeroair.trinkets.api.events.TransformationEvent.EndTransformation;
import xzeroair.trinkets.api.events.TransformationEvent.RaceUpdateEvent;
import xzeroair.trinkets.api.events.TransformationEvent.StartTransformation;
import xzeroair.trinkets.attributes.FlyingAttribute;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.attributes.RaceAttribute.RaceAttribute;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.CapabilityBase;
import xzeroair.trinkets.entity.AlphaWolf;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.SyncRaceDataPacket;
import xzeroair.trinkets.network.transformation.OpenRaceSelectionScreen;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.IRaceProvider;
import xzeroair.trinkets.traits.AbilityHandler;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.Utils.TempCache;
import xzeroair.trinkets.util.helpers.AttributeHelper;
import xzeroair.trinkets.util.helpers.StringUtils;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

import javax.annotation.Nonnull;
import java.util.List;

public class EntityProperties extends CapabilityBase<EntityProperties, EntityLivingBase> {

    protected boolean first_login = true;
    protected boolean login = true;

    protected boolean changed = false;
    protected boolean sync = false;

    protected RaceCache originalRace = new RaceCache();
    protected RaceCache imbuedRace = new RaceCache();
    protected RaceCache potionRace = new RaceCache();
    protected RaceCache attributeRace = new RaceCache();
    protected RaceCache currentRace = new RaceCache();
    protected RaceCache previousRace = new RaceCache();

    protected int widthValue = 100;
    protected int heightValue = 100;
    protected float defaultWidth = 1.8F;
    protected float defaultHeight = 0.6F;

    protected boolean isFake = false;

    protected KeybindHandler keybindHandler;
    protected EntityRacePropertiesHandler properties;
    //    protected ElementalAttributes elementalAttributes;
    protected AbilityHandler abilities;

    protected float stepHeightPrev = 0.6F;
    protected boolean onGround;
    protected boolean isChild;
    protected BlockPos prevBlockpos;

    public EntityProperties(EntityLivingBase e) {
        super(e);
        widthValue = 100;
        heightValue = 100;
        defaultHeight = e.height;
        defaultWidth = e.width;
        originalRace = new RaceCache();
        imbuedRace = originalRace;
        potionRace = originalRace;
        attributeRace = originalRace;
        previousRace = originalRace;
        currentRace = originalRace;
        abilities = new AbilityHandler(object);
//        elementalAttributes = new ElementalAttributes();
        properties = currentRace.getRace().getRaceHandler(object);
    }

    @Override
    public NBTTagCompound getTag() {
        NBTTagCompound tag = object.getEntityData();
        if (tag != null) {
            final NBTTagCompound persistentData;
            if (object instanceof EntityPlayer) {
                if (!tag.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
                    tag.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
                }
                persistentData = tag.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
            } else {
                persistentData = tag;
            }
            final String capTag = Reference.MODID + ".race";
            if (!persistentData.hasKey(capTag)) {
                persistentData.setTag(capTag, new NBTTagCompound());
            }
            return persistentData.getCompoundTag(capTag);
        }
        return super.getTag();
    }

    // ABILITIES
    public AbilityHandler getAbilityHandler() {
        return abilities;
    }

//    public ElementalAttributes getElementalAttributes() {
//        return elementalAttributes;
//    }

    // ABILITIES END

    public void onUpdatePre() {
        final World world = object.getEntityWorld();
        final boolean isClient = world.isRemote;
        if (!(object instanceof FakePlayer) && (object instanceof EntityPlayer)) {
            onGround = object.onGround;
        }
    }

    @Override
    public void onUpdate() {
        final World world = object.getEntityWorld();
        final boolean isClient = world.isRemote;
        if (isClient && TrinketsConfig.CLIENT.debug.showMovementSpeed) {
            StringUtils.sendStatusMessageToPlayer(object, "Bp/t:" + this.entitySpeed(object), true);
        }
        this.stepHeightHandler();
        this.flySpeedHandler();

        if (!(object instanceof FakePlayer) && (object instanceof EntityPlayer)) {
            this.onPlayerUpdate(world, (EntityPlayer) object);
        } else {
            this.onEntityUpdate(world, object);
        }

        if (sync == true) {
            this.sendInformationToTracking();
            sync = false;
        }

        this.setLogin(false);
        this.setFirstLogin(false);
        if (!isClient) {
            BlockPos blockpos = new BlockPos(object);

            if (!Objects.equal(prevBlockpos, blockpos)) {
                prevBlockpos = blockpos;
            }
        }
    }

    public void onPlayerUpdate(World world, EntityPlayer player) {
        final boolean isClient = world.isRemote;
        if (!isClient) {
            if (!this.getCurrentRace().getRace().isNone()) {
                player.onGround = onGround;
            }
        }
        if (this.isLogin()) {
            this.getRaceHandler().setFirstUpdate(true);
        }
//        final IAttributeInstance atk = object.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE);
//        if (atk != null) {
//            System.out.println(atk.getAttributeValue() + "|" + atk.getModifiers());
//        }
//        if (player.getName().equalsIgnoreCase("xzeroair") && player.isSneaking() && ((player.ticksExisted % 40) == 0)) {
//            if (!world.isRemote && (object instanceof EntityPlayerMP)) {
//                NetworkHandler.sendTo(new OpenTrinketGui(Reference.GUI_RACE_SELECTION), (EntityPlayerMP) object);
//            }
//        }

        abilities.updateAbilityHandler();
        this.updateRace();
        this.getRaceHandler().onTick();

        if (sync == true) {
            if (!isClient) {
                this.sendInformationToPlayer(player);
            }
        }
    }

    public void onEntityUpdate(World world, EntityLivingBase entity) {
        if (object.isChild()) {
            //			TODO Fix Child Transformations
            //				this.onChildUpdate(world, object);
            return;
        }
        final boolean isClient = world.isRemote;
        final ResourceLocation regName = EntityRegistry.getEntry(entity.getClass()).getRegistryName();
        final String modID = regName.getNamespace();
        final String name = regName.getPath();
        if (modID.equalsIgnoreCase("iceandfire")) {
            return;
        }
        if (this.isFirstLogin()) {
            //			System.out.println("Is Entitiy First Update: H:" + entity.height + ", W:" + entity.width + ", Step:" + entity.stepHeight);
            final IAttributeInstance stepheight = object.getAttributeMap().getAttributeInstance(JumpAttribute.stepHeight);
            if (stepheight != null) {
                stepheight.setBaseValue(object.stepHeight);
            }
            stepHeightPrev = object.stepHeight;
            this.setDefaultHeight(object.height);
            this.setDefaultWidth(object.width);
            this.saveToNBT(this.getTag());
        }

        if (this.isLogin()) {
            //			System.out.println("Is Entitiy Login: H:" + entity.height + ", W:" + entity.width + ", Step:" + entity.stepHeight);
            this.scheduleResync();
        }

        this.updateRace();
        this.getRaceHandler().onTick();

        if (!(object instanceof EntityPlayer)) {
            List<SlotInformation> equipment = TrinketHelper.getSlotInfoForArmor(object, s -> !s.isEmpty() && (s.getItem() instanceof IAccessoryInterface));
            if (!equipment.isEmpty()) {
                for (SlotInformation info : equipment) {
                    ItemStack equipStack = info.getStackFromHandler(object);
                    if (!equipStack.isEmpty() && (equipStack.getItem() instanceof IAccessoryInterface)) {
                        IAccessoryInterface item = (IAccessoryInterface) equipStack.getItem();
                        item.onEntityArmorTick(world, object, equipStack);
                    }
                }
            }
        }
    }

    public void onChildUpdate(@Nonnull World world, EntityLivingBase entity) {
        final boolean isClient = world.isRemote;
        if (this.isFirstLogin()) {
            //			System.out.println("Is Child First Update: H:" + entity.height + ", W:" + entity.width + ", Step:" + entity.stepHeight);
            final IAttributeInstance stepheight = object.getAttributeMap().getAttributeInstance(JumpAttribute.stepHeight);
            if (stepheight != null) {
                stepheight.setBaseValue(object.stepHeight);
            }
            stepHeightPrev = object.stepHeight;
            this.setDefaultHeight(object.height);
            this.setDefaultWidth(object.width);
            this.saveToNBT(this.getTag());
        }

        if (this.isLogin()) {
            //			System.out.println("Is Child Login: H:" + entity.height + ", W:" + entity.width + ", Step:" + entity.stepHeight);
            this.scheduleResync();
        }

        this.updateRace();
        this.getRaceHandler().onTick();

        //		System.out.println(
        //				"H:" +
        //						object.height + "|W:" + object.width +
        //						"| - |H:" +
        //						this.getDefaultHeight() + " |W:" + this.getDefaultWidth()
        //						+ "| - |H:" +
        //						properties.getHeight() + " |H:" + properties.getTargetHeight()
        //						+ "| - |W:" +
        //						properties.getWidth() + " |W:" + properties.getTargetWidth()
        //						+ "| - |" +
        //						"Transforming: " + properties.isTransforming() + "| Progress: " + properties.TransformationProgress() + "| Transformed: " + properties.isTransformed()
        //						+ " | - |After"
        //		);
    }

    public void updateRace() {
        final World world = object.getEntityWorld();
        final boolean isClient = world.isRemote;
        if (isClient) {
            return;
        }
        final TempCache<EntityRace, Element> raceCache = this.getEntityRaceWithDetails();
        final TransformationEvent.RaceUpdateEvent UpdateEvent = new RaceUpdateEvent(object, this, raceCache.getFirst(), raceCache.getSecond());
        // If the Event is not canceled, Continue.
        if (!MinecraftForge.EVENT_BUS.post(UpdateEvent)) {
            Element newElement = UpdateEvent.getNewElement();
            if (newElement == null) {
                newElement = Elements.NEUTRAL;
            }
            // If the race is different from the current race.
            if (UpdateEvent.raceChanged() || UpdateEvent.ElementChanged()) {
                // Get the new race
                EntityRace newRace = UpdateEvent.getNewRace();
                // Check to see if it's null
                if (newRace == null) {
                    newRace = EntityRaces.none;
                }
                // Get the players current race handler and information.
                final EntityRacePropertiesHandler oldProperties = this.getRaceHandler();
                // Start the end transformation event for the current race.
                final TransformationEvent.EndTransformation end = new EndTransformation(object, this, oldProperties.getRace());
                // Bug? Supposed to only continue if the event is not canceled.
                if (MinecraftForge.EVENT_BUS.post(end)) {
                    return;
                }
                // RUn all the end race transformation methods and remove attributes.
                oldProperties.onTransformEnd();
                AttributeHelper.removeAttributesByUUID(object, this.getPreviousRace().getRace().getUUID(), oldProperties.getRace().getUUID());
                this.setPreviousRace(oldProperties.getRaceCache());
                final Entity mount = object.getRidingEntity();
                if (mount instanceof AlphaWolf) {
                    object.dismountRidingEntity();
                }
                final IAttributeInstance attribute = object.getAttributeMap().getAttributeInstance(JumpAttribute.stepHeight);
                if ((attribute != null)) {
                    if (object.stepHeight != attribute.getBaseValue()) {
                        object.stepHeight = (float) attribute.getBaseValue();
                    }
                }
                // officially start new transformation.
                properties = newRace.getRaceHandler(object, newElement);
                try {
                    properties.loadNBTData(this.getTag());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.setCurrent(new RaceCache(newRace, newElement));
                properties.onTransform();

                // Trigger start Transformation event. Maybe add a cancel scenario?
                final TransformationEvent.StartTransformation start = new StartTransformation(object, this, properties.getRace());
                MinecraftForge.EVENT_BUS.post(start);
                this.scheduleResync();
            }
        }
    }

    public BlockPos getPrevBlockpos() {
        if (prevBlockpos == null) {
            prevBlockpos = object.getPosition();
        }
        return prevBlockpos;
    }

    public KeybindHandler getKeybindHandler() {
        if (keybindHandler == null) {
            keybindHandler = new KeybindHandler();
        }
        return keybindHandler;
    }

    private EntityRace getEntityRace() {
        return getEntityRaceWithDetails().getFirst();
    }

    private Element getEntityElement() {
        return getEntityRaceWithDetails().getSecond();
    }

    private RaceCache getEntityRaceWithDetails() {
        final RaceCache attributeRace = this.getAttributeRace();
        setFake(true);
        if ((attributeRace != null) && !attributeRace.getRace().isNone()) {
            return attributeRace;
        }
        final RaceCache potionRace = this.getPotionRace();
        if ((potionRace != null) && !potionRace.getRace().isNone()) {
            return potionRace;
        }
        ItemStack provider = this.getRaceProvider();
        if (!provider.isEmpty()) {
            return new RaceCache(((IRaceProvider) provider.getItem()).getRace(), Capabilities.getTrinketProperties(provider, Elements.NEUTRAL, (prop, ele) -> {
                return prop.getElementAttributes().getPrimaryElement();
            }));
        }
        setFake(false);
        final RaceCache imbuedRace = this.getImbuedRace();
        if ((imbuedRace != null) && !imbuedRace.getRace().isNone()) {
            return imbuedRace;
        }
        return this.getOriginalRace();
    }

    public RaceCache getAttributeRace() {
        IAttributeInstance race = object.getEntityAttribute(RaceAttribute.ENTITY_RACE);
        if (race != null) {
            if (!race.getModifiers().isEmpty()) {
                for (final AttributeModifier modifier : race.getModifiers()) {
                    return new RaceCache(EntityRace.getByUUID(modifier.getID()));
                }
            }
        }
        return null;
    }

    public ItemStack getRaceProvider() {
        final int count = TrinketHelper.countAccessories(object, stack -> stack.getItem() instanceof IRaceProvider);
        if ((count > 1) || (count < 1)) {
            return ItemStack.EMPTY;
        } else {
            return TrinketHelper.getAccessory(object, stack -> stack.getItem() instanceof IRaceProvider);
        }
    }

    /**
     * Handles the Step Height Attribute.
     * I really don't understand why this works.
     * avoid touching it.
     */
    private void stepHeightHandler() {
        final IAttributeInstance attribute = object.getEntityAttribute(JumpAttribute.stepHeight);
        if ((attribute != null) && !attribute.getModifiers().isEmpty()) {
            final float defaultStepHeight = (float) attribute.getBaseValue();
            final float attributeStepHeight = (float) attribute.getAttributeValue();
            final float currentStepHeight = object.stepHeight;
            object.stepHeight = defaultStepHeight;
            if ((currentStepHeight - object.stepHeight) == 0F) {
                object.stepHeight = attributeStepHeight;
                stepHeightPrev = defaultStepHeight;
            } else if (((currentStepHeight - object.stepHeight) - attributeStepHeight) == -defaultStepHeight) {
                object.stepHeight = attributeStepHeight;
                stepHeightPrev = defaultStepHeight;
            } else {
                final float stepP = stepHeightPrev;
                stepHeightPrev = currentStepHeight;
                if (((attributeStepHeight - defaultStepHeight) + currentStepHeight) == ((attributeStepHeight - defaultStepHeight) + stepP)) {
                    object.stepHeight = (attributeStepHeight - defaultStepHeight) + stepHeightPrev;
                }
            }
        }
    }

    private void flySpeedHandler() {
        if (object instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) object;
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

    public void startVec(EntityLivingBase entity) {
        prev_posx = entity.posX;
        prev_posy = entity.posY;
        prev_posz = entity.posZ;
    }

    public Vec3d lastVec() {
        final Vec3d lastPosVec = new Vec3d(prev_posx, prev_posy, prev_posz);
        return lastPosVec;
    }

    public double entitySpeed(EntityLivingBase entity) {
        final Vec3d currentPosVec = new Vec3d(entity.posX, entity.posY, entity.posZ);
        final double distanceTraveled = this.lastVec().distanceTo(currentPosVec);

        this.startVec(entity);

        return distanceTraveled;
    }
    // Speed Checks END

    /**
     * Send Player information on Login
     *
     */
    public void onLogin() {
        final World world = object.getEntityWorld();
        if (!world.isRemote && (object instanceof EntityPlayerMP)) {
            if (this.isFirstLogin()) {
                if (TrinketsConfig.SERVER.races.selectionMenu) {
                    NetworkHandler.sendTo(new OpenRaceSelectionScreen(), (EntityPlayerMP) object);
                }
            } else {
                this.sendInformationToPlayer(object, this.getTag());
            }
        }
    }

    /**
     * Force Dismount Goblins
     *
     */
    public void onLogoff() {
        if (this.getCurrentRace().compareRace(EntityRaces.goblin)) {
            if (object.getRidingEntity() instanceof AlphaWolf) {
                object.dismountRidingEntity();
            }
        }
    }

    /**
     * @param receiver Send Capability Information to receiver
     */
    public void sendInformationToPlayer(EntityLivingBase receiver) {
        final World world = object.getEntityWorld();
        if (!world.isRemote) {
            this.sendInformationToPlayer(receiver, this.saveToNBT(this.getTag()));
        }
    }

    public void sendInformationToPlayer(EntityLivingBase receiver, NBTTagCompound tag) {
        final World world = object.getEntityWorld();
        if (!world.isRemote && (receiver instanceof EntityPlayerMP)) {
            NetworkHandler.sendTo(new SyncRaceDataPacket(object, tag), (EntityPlayerMP) receiver);
        }
    }

    public void sendInformationToTracking() {
        final World world = object.getEntityWorld();
        if (!world.isRemote && (world instanceof WorldServer)) {
            final WorldServer w = (WorldServer) world;
            NetworkHandler.sendToClients(w, object.getPosition(), new SyncRaceDataPacket(object, this.saveToNBT(this.getTag())));
        }
    }

    /**
     * Send information from the player, to the server, This might be pointless, But it's needed for Race Selection Gui
     *
     */
    public void sendInformationToServer() {
        final World world = object.getEntityWorld();
        if (world.isRemote) {
            NetworkHandler.sendToServer(new SyncRaceDataPacket(object, this.saveToNBT(this.getTag())));
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
     * @return
     */
    public boolean isNormalSize() {
        return (object.height == this.getDefaultHeight()) && (object.width == this.getDefaultWidth());
    }

    public boolean hasRace() {
        return !currentRace.compareRace(EntityRaces.none);
    }

    /*-----------------------------------Boolean Checks-------------------------------*/

    /**
     * Get the Entity Race Handler
     */
    public EntityRacePropertiesHandler getRaceHandler() {
        return properties;
    }

    public RaceCache getPreviousRace() {
        return previousRace;
    }

    public void setPreviousRace(RaceCache cache) {
        if (cache == null) {
            cache = new RaceCache();
        }
        if (!previousRace.compare(cache)) {
            previousRace = cache;
        }
    }

    /**
     * Get the current race of the Entity
     *
     * @return
     */
    public RaceCache getCurrentRace() {
        return currentRace;
    }

    public void setCurrent(RaceCache cache) {
        if (cache == null) {
            cache = new RaceCache();
        }
        if (!currentRace.compare(cache)) {
            currentRace = cache;
        }
    }

    /**
     * Get the current race of the entity given by eating a transformation item.
     *
     * @return
     */
    public RaceCache getImbuedRace() {
        return imbuedRace;
    }

    public void setImbuedRace(RaceCache cache) {
        if (cache == null) {
            cache = new RaceCache();
        }
        if (!imbuedRace.compare(cache)) {
            imbuedRace = cache;
        }
    }

    public RaceCache getPotionRace() {
        return potionRace;
    }

    public void setPotionRace(RaceCache cache) {
        if (cache == null) {
            cache = new RaceCache();
        }
        if (!potionRace.compare(cache)) {
            potionRace = cache;
        }
    }

    /**
     * Get the entities original race. By default, this is EntityRaces.none.
     * This changes based on if the race selection menu is enabled.
     *
     * @return
     */
    public RaceCache getOriginalRace() {
        return originalRace;
    }

    public void setOriginalRace(RaceCache cache) {
        if (cache == null) {
            cache = new RaceCache();
        }
        if (!originalRace.compare(cache)) {
            originalRace = cache;
        }
    }

    /**
     * Get the Entities default width.
     *
     * @return
     */
    public float getDefaultWidth() {
        return defaultWidth;
    }

    private void setDefaultWidth(float defaultWidth) {
        if (this.defaultWidth != defaultWidth) {
            this.defaultWidth = defaultWidth;
        }
    }

    /**
     * Get the Entities default height.
     *
     * @return
     */
    public float getDefaultHeight() {
        return defaultHeight;
    }

    private void setDefaultHeight(float defaultHeight) {
        if (this.defaultHeight != defaultHeight) {
            this.defaultHeight = defaultHeight;
        }
    }

    /**
     * Get the current height value based on percentage, the default value is 100
     *
     * @return
     */
    public int getHeightValue() {
        return heightValue;
    }

    public void setHeightValue(int height) {
        if (heightValue != height) {
            heightValue = height;
        }
    }

    /**
     * Get the current height value based on percentage, the default value is 100
     *
     * @return
     */
    public int getWidthValue() {
        return widthValue;
    }

    public void setWidthValue(int width) {
        if (widthValue != width) {
            widthValue = width;
        }
    }

    // Is Likely redundant and could be removed. Mainly kept for Non-Player Entities
    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        if (this.login != login) {
            this.login = login;
        }
    }

    public boolean isFirstLogin() {
        return first_login;
    }

    public void setFirstLogin(boolean firstLogin) {
        if (first_login != firstLogin) {
            first_login = firstLogin;
        }
    }

    /**
     * Check to see if the entity is currently a fake race, this was originally added for a system that was never implemented.
     * an Entity with a Fake Race is typically one that is wearing a transformation item, It might also apply to potion transformations as well.
     *
     * @return
     */
    public boolean isFake() {
        return isFake;
    }

    public void setFake(boolean isFake) {
        if (this.isFake != isFake) {
            this.isFake = isFake;
        }
    }

    /**
     * Sanity Check for Child Entities, Necessary due to how Minecraft handles child entities.
     *
     * @return
     */
    public boolean isChild() {
        return isChild;
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
        sync = true;
    }

    @Override
    public void copyFrom(EntityProperties source, boolean wasDeath, boolean keepInv) {
        first_login = source.first_login;
        login = source.login;
        defaultWidth = source.defaultWidth;
        defaultHeight = source.defaultHeight;

        originalRace = source.originalRace;
        imbuedRace = source.imbuedRace;

        isChild = source.isChild;

        if (wasDeath && !keepInv) {
            previousRace = source.currentRace;
            currentRace = imbuedRace.getRace().isNone() ? originalRace : imbuedRace;
            heightValue = currentRace.getRace().getRaceHeight();
            widthValue = currentRace.getRace().getRaceWidth();
        } else {
            currentRace = source.currentRace;
            previousRace = source.previousRace;
//            potionRace = source.potionRace;
            heightValue = source.heightValue;
            widthValue = source.widthValue;
        }

        properties = currentRace.getRace().getRaceHandler(object, currentRace.getElement());
        properties.onTransform();

        try {
            this.getRaceHandler().copyFrom(source.getRaceHandler(), wasDeath, keepInv);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.getAbilityHandler().copyFrom(source.getAbilityHandler(), wasDeath, keepInv);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.scheduleResync();
    }

    @Override
    public NBTTagCompound saveToNBT(NBTTagCompound compound) {
        compound.setTag("OriginalRace", getOriginalRace().saveToNBT(new NBTTagCompound()));
        compound.setTag("ImbuedRace", getImbuedRace().saveToNBT(new NBTTagCompound()));
//        compound.setTag("PotionRace", getPotionRace().saveToNBT(new NBTTagCompound()));
//        compound.setTag("AttributeRace", getAttributeRace().saveToNBT(new NBTTagCompound()));
        compound.setTag("PreviousRace", getPreviousRace().saveToNBT(new NBTTagCompound()));
        compound.setTag("CurrentRace", getCurrentRace().saveToNBT(new NBTTagCompound()));
        compound.setInteger("heightValue", this.getHeightValue());
        compound.setInteger("widthValue", this.getWidthValue());
        compound.setFloat("default_height", this.getDefaultHeight());
        compound.setFloat("default_width", this.getDefaultWidth());
        compound.setBoolean("login", this.isLogin());
        compound.setBoolean("first_login", this.isFirstLogin());
        compound.setBoolean("fake", this.isFake());
        compound.setBoolean("child", this.isChild());
        this.getRaceHandler().savedNBTData(compound);
        this.getAbilityHandler().saveAbilitiesToNBT(compound);
        return compound;
    }

    @Override
    public void loadFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("first_login")) {
            first_login = compound.getBoolean("first_login");
        }
        if (compound.hasKey("login")) {
            login = compound.getBoolean("login");
        }
        if (compound.hasKey("OriginalRace")) {
            originalRace = RaceCache.loadFromNBT(compound.getCompoundTag("OriginalRace"));
        }
        if (compound.hasKey("ImbuedRace")) {
            imbuedRace = RaceCache.loadFromNBT(compound.getCompoundTag("ImbuedRace"));
        }
        if (compound.hasKey("PotionRace")) {
            potionRace = RaceCache.loadFromNBT(compound.getCompoundTag("PotionRace"));
        }
        if (compound.hasKey("AttributeRace")) {
            attributeRace = RaceCache.loadFromNBT(compound.getCompoundTag("AttributeRace"));
        }
        if (compound.hasKey("PreviousRace")) {
            previousRace = RaceCache.loadFromNBT(compound.getCompoundTag("PreviousRace"));
        }

        if (compound.hasKey("CurrentRace")) {
            currentRace = RaceCache.loadFromNBT(compound.getCompoundTag("CurrentRace"));
            properties = currentRace.getRace().getRaceHandler(object, currentRace.getElement());
            this.getRaceHandler().loadNBTData(compound);
            properties.onTransform();
        }
        if (compound.hasKey("heightValue")) {
            heightValue = compound.getInteger("heightValue");
        }
        if (compound.hasKey("widthValue")) {
            widthValue = compound.getInteger("widthValue");
        }
        if (compound.hasKey("default_height")) {
            defaultHeight = compound.getFloat("default_height");
        }
        if (compound.hasKey("default_width")) {
            defaultWidth = compound.getFloat("default_width");
        }
        if (compound.hasKey("fake")) {
            isFake = compound.getBoolean("fake");
        }
        if (compound.hasKey("child")) {
            isChild = compound.getBoolean("child");
        }
        this.getAbilityHandler().loadAbilitiesFromNBT(compound);

    }

    public static class RaceCache extends TempCache<EntityRace, Element> {

        public RaceCache() {
            super(EntityRaces.none, Elements.NEUTRAL);
        }

        public RaceCache(EntityRace race, Element element) {
            super(race, element);
        }

        public RaceCache(Element element) {
            this(EntityRaces.none, element);
        }

        public RaceCache(EntityRace race) {
            this(race, Elements.NEUTRAL);
        }

        public EntityRace getRace() {
            return getFirst() == null ? EntityRaces.none : this.getFirst();
        }

        public Element getElement() {
            return getSecond() == null ? Elements.NEUTRAL : this.getSecond();
        }

        public NBTTagCompound saveToNBT(NBTTagCompound tag) {
            tag.setInteger("race", getRace().getID());
            tag.setInteger("element", getElement().getID());
            return tag;
        }

        public static RaceCache loadFromNBT(NBTTagCompound tag) {
            final EntityRace race = tag.hasKey("race") ? EntityRace.getRaceById(tag.getInteger("race")) : EntityRaces.none;
            final Element element = tag.hasKey("element") ? Element.getById(tag.getInteger("element")) : Elements.NEUTRAL;
            return new RaceCache(race == null ? EntityRaces.none : race, element == null ? Elements.NEUTRAL : element);
        }

        public boolean compare(RaceCache other) {
            return compareRace(other) && compareElement(other);
        }

        public boolean compareRace(RaceCache other) {
            return this.getRace().equals(other.getRace());
        }

        public boolean compareRace(EntityRace race) {
            return this.getRace().equals(race);
        }

        public boolean compareElement(RaceCache other) {
            return this.getElement().equals(other.getElement());
        }

        public boolean compareElement(Element element) {
            return this.getElement().equals(element);
        }
    }
}
