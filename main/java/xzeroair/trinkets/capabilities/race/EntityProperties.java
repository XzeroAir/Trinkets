package xzeroair.trinkets.capabilities.race;

import java.util.List;

import com.google.common.base.Objects;

import net.minecraft.client.renderer.entity.RenderLivingBase;
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
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation;
import xzeroair.trinkets.api.events.TransformationEvent;
import xzeroair.trinkets.api.events.TransformationEvent.EndTransformation;
import xzeroair.trinkets.api.events.TransformationEvent.RaceUpdateEvent;
import xzeroair.trinkets.api.events.TransformationEvent.StartTransformation;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.attributes.RaceAttribute.RaceAttribute;
import xzeroair.trinkets.capabilities.CapabilityBase;
import xzeroair.trinkets.entity.AlphaWolf;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.SyncRaceDataPacket;
import xzeroair.trinkets.network.trinketcontainer.OpenTrinketGui;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.IRaceProvider;
import xzeroair.trinkets.traits.AbilityHandler;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.AttributeHelper;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class EntityProperties extends CapabilityBase<EntityProperties, EntityLivingBase> {

	protected boolean first_login = true;
	protected boolean login = true;

	protected boolean changed = false;
	protected boolean sync = false;

	protected EntityRace previous = EntityRaces.none;
	protected EntityRace original = EntityRaces.none;
	protected EntityRace imbued = EntityRaces.none;
	protected EntityRace current = EntityRaces.none;

	protected int widthValue = 100;
	protected int heightValue = 100;
	protected float defaultWidth = 1.8F;
	protected float defaultHeight = 0.6F;

	protected boolean traitShown = true;
	protected String traitColor = "16777215";
	protected boolean isFake = false;

	protected KeybindHandler keybindHandler;
	protected EntityRacePropertiesHandler properties;
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
		original = EntityRaces.none;
		previous = EntityRaces.none;
		imbued = EntityRaces.none;
		current = EntityRaces.none;
		abilities = new AbilityHandler(object);
		properties = current.getRaceHandler(object).setEntityProperties(this);
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

	// ABILITIES END

	@SideOnly(Side.CLIENT)
	public void onRender(RenderLivingBase renderer, boolean isSlim, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		this.getRaceHandler().doRenderLayer(renderer, this.isFake(), isSlim, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
	}

	public void onUpdatePre() {
		final World world = object.getEntityWorld();
		final boolean isClient = world.isRemote;
		if (!(object instanceof FakePlayer) && (object instanceof EntityPlayer)) {
			//			object.setPositionAndUpdate(object.posX, object.posY, object.posZ);
			onGround = object.onGround;
		}
	}

	@Override
	public void onUpdate() {
		final World world = object.getEntityWorld();
		final boolean isClient = world.isRemote;
		this.stepHeightHandler();

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
			if (!this.getCurrentRace().isNone()) {
				player.onGround = onGround;
			}
		}
		if (this.isLogin()) {
			this.getRaceHandler().setFirstUpdate(true);
		}
		this.updateRace();
		abilities.updateAbilityHandler();
		this.getRaceHandler().onTick();

		// TODO Come back to this, Check
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

	public void onChildUpdate(World world, EntityLivingBase entity) {
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
		final EntityRace race = this.getEntityRace(); // Causes a lot of Overhead
		final TransformationEvent.RaceUpdateEvent UpdateEvent = new RaceUpdateEvent(object, this, this.getRaceHandler().getRace(), race);
		if (!MinecraftForge.EVENT_BUS.post(UpdateEvent)) {
			if (UpdateEvent.raceChanged()) {
				EntityRace newRace = UpdateEvent.getNewRace();
				if (newRace == null) {
					newRace = EntityRaces.none;
				}
				final EntityRacePropertiesHandler oldProperties = this.getRaceHandler();
				final TransformationEvent.EndTransformation end = new EndTransformation(object, this, oldProperties.getRace());
				if (MinecraftForge.EVENT_BUS.post(end)) {
					return;
				}
				oldProperties.onTransformEnd();
				AttributeHelper.removeAttributesByUUID(object, this.getPreviousRace().getUUID(), oldProperties.getRace().getUUID());
				this.setPreviousRace(oldProperties.getRace());
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
				properties = newRace.getRaceHandler(object).setEntityProperties(this);
				properties.onTransform();
				this.setCurrent(newRace);

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
		final EntityRace potionRace = this.getPotionRace();
		if ((potionRace != null) && !potionRace.isNone()) {
			this.setFake(true);
			return potionRace;
		}
		ItemStack raceProvider = this.getRaceProvider();
		if (!raceProvider.isEmpty()) {
			this.setFake(true);
			return ((IRaceProvider) raceProvider.getItem()).getRace();
		}
		final EntityRace imbued = this.getImbuedRace();
		if ((imbued != null) && !imbued.equals(EntityRaces.none)) {
			this.setFake(false);
			return imbued;
		} else {
			this.setFake(false);
			return this.getOriginalRace();
		}
	}

	protected EntityRace getPotionRace() {
		IAttributeInstance race = object.getEntityAttribute(RaceAttribute.ENTITY_RACE);
		if (race != null) {
			if (!race.getModifiers().isEmpty()) {
				for (final AttributeModifier modifier : race.getModifiers()) {
					return EntityRace.getByUUID(modifier.getID());
				}
			}
		}
		return null;
	}

	protected ItemStack getRaceProvider() {
		final int count = TrinketHelper.countAccessories(
				object, stack -> stack.getItem() instanceof IRaceProvider
		);
		if ((count > 1) || (count < 1)) {
			return ItemStack.EMPTY;
		} else {
			return TrinketHelper.getAccessory(object, stack -> stack.getItem() instanceof IRaceProvider);
		}
	}

	private void stepHeightHandler() {
		/*
		 * Handles the Step Height Attribute
		 */
		final IAttributeInstance attribute = object.getEntityAttribute(JumpAttribute.stepHeight);
		if ((attribute != null) && !attribute.getModifiers().isEmpty()) {
			final double f = attribute.getAttributeValue();
			final float defaultStepHeight = (float) attribute.getBaseValue();
			final float result = (float) (f);
			final float step = object.stepHeight;
			object.stepHeight = defaultStepHeight;
			if ((step - object.stepHeight) == 0F) {
				object.stepHeight = result;
				stepHeightPrev = defaultStepHeight;
			} else if (((step - object.stepHeight) - result) == -defaultStepHeight) {
				object.stepHeight = result;
				stepHeightPrev = defaultStepHeight;
			} else {
				final float stepP = stepHeightPrev;
				stepHeightPrev = step;
				if (((result - defaultStepHeight) + step) == ((result - defaultStepHeight) + stepP)) {
					object.stepHeight = (result - defaultStepHeight) + stepHeightPrev;
				} else {

				}
			}
		}
	}

	public void onLogin() {
		final World world = object.getEntityWorld();
		if (!world.isRemote && (object instanceof EntityPlayerMP)) {
			if (this.isFirstLogin()) {
				if (TrinketsConfig.SERVER.races.selectionMenu) {
					NetworkHandler.sendTo(new OpenTrinketGui(4), (EntityPlayerMP) object);
				}
			} else {
				this.sendInformationToPlayer(object, this.getTag());
			}
		}
	}

	public void onLogoff() {
		if (this.getCurrentRace().equals(EntityRaces.goblin)) {
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

	public void sendInformationToServer() {
		final World world = object.getEntityWorld();
		if (world.isRemote) {
			NetworkHandler.sendToServer(new SyncRaceDataPacket(object, this.saveToNBT(this.getTag())));
		}
	}

	/*-----------------------------------Boolean Checks-------------------------------*/

	@Deprecated
	public int getSize() {
		return this.getHeightValue();
	}

	public boolean isNormalSize() {
		return (object.height == this.getDefaultHeight()) && (object.width == this.getDefaultWidth());
	}

	public boolean hasRace() {
		return !current.equals(EntityRaces.none);
	}

	/*-----------------------------------Boolean Checks-------------------------------*/

	/**
	 * Get the Entity Race Handler
	 */
	public EntityRacePropertiesHandler getRaceHandler() {
		return properties.setEntityProperties(this);
	}

	public EntityRace getPreviousRace() {
		return previous;
	}

	public void setPreviousRace(EntityRace race) {
		if (previous != race) {
			if (race != null) {
				previous = race;
			} else {
				previous = EntityRaces.none;
			}
		}
	}

	public EntityRace getCurrentRace() {
		return current;
	}

	public void setCurrent(EntityRace race) {
		if (current != race) {
			if (race != null) {
				current = race;
			} else {
				current = EntityRaces.none;
			}
		}
	}

	public EntityRace getImbuedRace() {
		return imbued;
	}

	public void setImbuedRace(EntityRace race) {
		if (imbued != race) {
			if (race != null) {
				imbued = race;
			} else {
				imbued = EntityRaces.none;
			}
		}
	}

	public EntityRace getOriginalRace() {
		return original;
	}

	public void setOriginalRace(EntityRace race) {
		if (original != race) {
			if (race != null) {
				original = race;
			} else {
				original = EntityRaces.none;
			}
		}
	}

	public float getDefaultWidth() {
		return defaultWidth;
	}

	private void setDefaultWidth(float defaultWidth) {
		if (this.defaultWidth != defaultWidth) {
			this.defaultWidth = defaultWidth;
		}
	}

	public float getDefaultHeight() {
		return defaultHeight;
	}

	private void setDefaultHeight(float defaultHeight) {
		if (this.defaultHeight != defaultHeight) {
			this.defaultHeight = defaultHeight;
		}
	}

	public int getHeightValue() {
		return heightValue;
	}

	public void setHeightValue(int height) {
		if (heightValue != height) {
			heightValue = height;
		}
	}

	public int getWidthValue() {
		return widthValue;
	}

	public void setWidthValue(int width) {
		if (widthValue != width) {
			widthValue = width;
		}
	}

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

	public boolean isFake() {
		return isFake;
	}

	public void setFake(boolean isFake) {
		if (this.isFake != isFake) {
			this.isFake = isFake;
			this.scheduleResync();
		}
	}

	public boolean isChild() {
		return isChild;
	}

	public void setChild(boolean isChild) {
		if (this.isChild != isChild) {
			this.isChild = isChild;
		}
	}

	public void scheduleResync() {
		sync = true;
	}

	@Override
	public void copyFrom(EntityProperties source, boolean wasDeath, boolean keepInv) {
		first_login = source.first_login;
		login = source.login;
		defaultWidth = source.defaultWidth;
		defaultHeight = source.defaultHeight;
		original = source.original;
		imbued = source.imbued;
		isChild = source.isChild;

		if (wasDeath && !keepInv) {
			previous = source.current;
			current = imbued.isNone() ? original : imbued;
			heightValue = current.getRaceHeight();
			widthValue = current.getRaceWidth();
		} else {
			current = source.current;
			previous = source.previous;
			heightValue = source.heightValue;
			widthValue = source.widthValue;
		}
		properties = current.getRaceHandler(object).setEntityProperties(this);
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
		compound.setString("original_race", this.getOriginalRace().getRegistryName().toString());
		compound.setString("imbued_race", this.getImbuedRace().getRegistryName().toString());
		compound.setString("prev_race", this.getPreviousRace().getRegistryName().toString());
		compound.setString("current_race", this.getCurrentRace().getRegistryName().toString());
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
		if (compound.hasKey("original_race")) {
			final EntityRace originalRace = EntityRace.getByNameOrId(compound.getString("original_race"));
			if (originalRace == null) {
				original = EntityRaces.none;
			} else {
				original = originalRace;
			}
		}
		if (compound.hasKey("imbued_race")) {
			final EntityRace imbuedRace = EntityRace.getByNameOrId(compound.getString("imbued_race"));
			if (imbuedRace == null) {
				imbued = EntityRaces.none;
			} else {
				imbued = imbuedRace;
			}
		}
		if (compound.hasKey("prev_race")) {
			final EntityRace previousRace = EntityRace.getByNameOrId(compound.getString("prev_race"));
			if (previousRace == null) {
				previous = EntityRaces.none;
			} else {
				previous = previousRace;
			}
		}
		if (compound.hasKey("current_race")) {
			final EntityRace currentRace = EntityRace.getByNameOrId(compound.getString("current_race"));
			if (currentRace == null) {
				current = EntityRaces.none;
			} else {
				current = currentRace;
			}
			if (properties.getRace() != current) {
				properties = current.getRaceHandler(object).setEntityProperties(this);
				properties.onTransform();
			}
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
		this.getRaceHandler().loadNBTData(compound);
		this.getAbilityHandler().loadAbilitiesFromNBT(compound);
	}

	private static class TempCache<A, B> {

		private final A first;
		private final B second;

		public TempCache(A first, B second) {
			this.first = first;
			this.second = second;
		}

		public A getFirst() {
			return first;
		}

		public B getSecond() {
			return second;
		}

	}

}
