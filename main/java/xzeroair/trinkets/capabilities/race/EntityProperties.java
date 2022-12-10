package xzeroair.trinkets.capabilities.race;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.events.TransformationEvent;
import xzeroair.trinkets.api.events.TransformationEvent.EndTransformation;
import xzeroair.trinkets.api.events.TransformationEvent.RaceUpdateEvent;
import xzeroair.trinkets.api.events.TransformationEvent.StartTransformation;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.capabilities.CapabilityBase;
import xzeroair.trinkets.entity.AlphaWolf;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.SyncRaceDataPacket;
import xzeroair.trinkets.network.trinketcontainer.OpenTrinketGui;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.races.EntityRaceHelper;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.traits.AbilityHandler;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;

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
		abilities = new AbilityHandler(e, this);
		properties = current.getRaceHandler(object).setEntityProperties(this);
	}

	@Override
	public NBTTagCompound getTag() {
		final NBTTagCompound forgeData = object.getEntityData();
		if ((forgeData != null) && (object instanceof EntityPlayer)) {
			if (!forgeData.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
				forgeData.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
			}

			final NBTTagCompound persistentData = forgeData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
			final String capTag = Reference.MODID + ".race";
			if (!persistentData.hasKey(capTag)) {
				persistentData.setTag(capTag, this.saveToNBT(new NBTTagCompound()));
			}
			tag = persistentData.getCompoundTag(capTag);
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
		this.getRaceHandler().doRenderLayer(renderer, isFake, isSlim, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
	}

	private float stepHeightPrev = 0.6F;
	private boolean onGround;

	public void onUpdatePre() {
		final World world = object.getEntityWorld();
		final boolean isClient = world.isRemote;
		if (!(object instanceof FakePlayer) && (object instanceof EntityPlayer) && object.getName().equals("XzeroAir")) {
			final EntityPlayer player = (EntityPlayer) object;
		}
		onGround = object.onGround;
	}

	@Override
	public void onUpdate() {
		final World world = object.getEntityWorld();
		final boolean isClient = world.isRemote;
		if (!(object instanceof FakePlayer) && (object instanceof EntityPlayer) && object.getName().equals("XzeroAir")) {
			final EntityPlayer player = (EntityPlayer) object;
		}

		if (this.isLogin()) {
			if (!(object instanceof EntityPlayer)) {
				final IAttributeInstance stepheight = object.getAttributeMap().getAttributeInstance(JumpAttribute.stepHeight);
				if (stepheight != null) {
					stepheight.setBaseValue(object.stepHeight);
				}
				this.setDefaultHeight(object.height);
				this.setDefaultWidth(object.width);
			}
			this.getRaceHandler().setFirstUpdate(true);
			this.setLogin(false);
		}

		if ((object instanceof EntityPlayer) && !isClient) {
			object.onGround = onGround;
		}
		//		if (!(object instanceof EntityPlayer)) {
		//			List<SlotInformation> equipment = TrinketHelper.getSlotInfoForArmor(object, s -> !s.isEmpty() && (s.getItem() instanceof IAccessoryInterface));
		//			if (!equipment.isEmpty()) {
		//				for (SlotInformation info : equipment) {
		//					ItemStack equipStack = info.getStackFromHandler(object);
		//					if (!equipStack.isEmpty() && (equipStack.getItem() instanceof IAccessoryInterface)) {
		//						IAccessoryInterface item = (IAccessoryInterface) equipStack.getItem();
		//						item.onEntityArmorTick(world, object, equipStack);
		//					}
		//				}
		//			}
		//		}

		this.stepHeightHandler();
		this.updateRace();
		abilities.updateAbilityHandler();
		this.getRaceHandler().onTick();
		//		System.out.println(this.getRaceHandler().getRace().getName());
		if (sync == true) {
			if (!isClient) {
				this.sendInformationToPlayer(object);
				this.sendInformationToTracking();
			}
			sync = false;
		}
		if (!(object instanceof FakePlayer) && (object instanceof EntityPlayer) && object.getName().equals("XzeroAir")) {
			final EntityPlayer player = (EntityPlayer) object;
			//			if (isClient) {
			try {
				//				System.out.println(this.getTag());
				//				System.out.println(this.getRaceHandler().getRace().getName());
			} catch (Exception e) {
				// TODO: handle exception
			}
			//			}
		}
	}

	public void updateRace() {
		final EntityRace crr = this.getCurrentRace();
		final EntityRace pre = this.getPreviousRace();
		final EntityRace race = this.getEntityRace(); // Causes a lot of Overhead
		this.setCurrent(race);
		final TransformationEvent.RaceUpdateEvent UpdateEvent = new RaceUpdateEvent(object, this, crr, race);
		if (!MinecraftForge.EVENT_BUS.post(UpdateEvent)) {
			if (UpdateEvent.raceChanged()) {
				EntityRace newRace = UpdateEvent.getNewRace();
				if (newRace == null) {
					newRace = EntityRaces.none;
				}
				final EntityRacePropertiesHandler oldProperties = this.getRaceHandler();
				oldProperties.onTransformEnd();
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

				final TransformationEvent.EndTransformation end = new EndTransformation(object, this, crr);
				MinecraftForge.EVENT_BUS.post(end);
				this.setPreviousRace(end.getPreviousRace());
				this.setCurrent(newRace);
				properties = newRace.getRaceHandler(object).setEntityProperties(this);
				properties.onTransform();

				final TransformationEvent.StartTransformation start = new StartTransformation(object, this, this.getCurrentRace());
				MinecraftForge.EVENT_BUS.post(start);
				sync = true;
			} else {
				final EntityRace handlerRace = this.getRaceHandler().getRace();
				if (handlerRace != race) {
					properties = race.getRaceHandler(object).setEntityProperties(this);
					properties.onTransform();
					sync = true;
				}
			}
		}
	}

	public KeybindHandler getKeybindHandler() {
		if (keybindHandler == null) {
			keybindHandler = new KeybindHandler();
		}
		return keybindHandler;
	}

	private EntityRace getEntityRace() {
		final EntityRace potionRace = EntityRaceHelper.getAttributeRace(object);
		if ((potionRace != null) && !potionRace.equals(EntityRaces.none)) {
			this.setFake(true);
			return potionRace;
		}
		final EntityRace TrinketRace = EntityRaceHelper.getTrinketRace(object);
		if ((TrinketRace != null) && !TrinketRace.equals(EntityRaces.none)) {
			this.setFake(true);
			return TrinketRace;
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
		if (!world.isRemote) {
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
		//		if (properties == null) {
		//			properties = this.getEntityRace().getRaceHandler(object).setEntityProperties(this);
		//			properties.onTransform();
		//			sync = true;
		//		}
		return properties;
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
			this.saveToNBT(this.getTag());
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
			this.saveToNBT(this.getTag());
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
			this.saveToNBT(this.getTag());
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
			this.saveToNBT(this.getTag());
		}
	}

	public float getDefaultWidth() {
		return defaultWidth;
	}

	private void setDefaultWidth(float defaultWidth) {
		if (this.defaultWidth != defaultWidth) {
			this.defaultWidth = defaultWidth;
			this.saveToNBT(this.getTag());
		}
	}

	public float getDefaultHeight() {
		return defaultHeight;
	}

	private void setDefaultHeight(float defaultHeight) {
		if (this.defaultHeight != defaultHeight) {
			this.defaultHeight = defaultHeight;
			this.saveToNBT(this.getTag());
		}
	}

	public int getHeightValue() {
		return heightValue;//(this.getDefaultHeight() * (this.getSize() * 0.01F));
	}

	public void setHeightValue(int height) {
		if (heightValue != height) {
			heightValue = height;
			this.saveToNBT(this.getTag());
		}
	}

	public int getWidthValue() {
		return widthValue;//(this.getDefaultWidth() * (this.getSize() * 0.01F));
	}

	public void setWidthValue(int width) {
		if (widthValue != width) {
			widthValue = width;
			this.saveToNBT(this.getTag());
		}
	}

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		if (this.login != login) {
			this.login = login;
			this.saveToNBT(this.getTag());
		}
	}

	public boolean isFirstLogin() {
		return first_login;
	}

	public void setFirstLogin(boolean firstLogin) {
		if (first_login != firstLogin) {
			first_login = firstLogin;
			this.saveToNBT(this.getTag());
		}
	}

	public boolean isFake() {
		return isFake;
	}

	public void setFake(boolean isFake) {
		if (this.isFake != isFake) {
			this.isFake = isFake;
		}
	}

	public void scheduleResync() {
		sync = true;
	}

	@Override
	public void copyFrom(EntityProperties source, boolean wasDeath, boolean keepInv) {
		sync = true;
		login = source.login;
		first_login = source.first_login;
		defaultWidth = source.defaultWidth;
		defaultHeight = source.defaultHeight;
		original = source.original;
		imbued = source.imbued;

		if (wasDeath) {
			if (keepInv) {
				current = source.current;
				previous = source.previous;
				heightValue = source.heightValue;
				widthValue = source.widthValue;
				properties = source.properties;
				abilities = source.abilities;
			} else {
				previous = source.current;
				current = imbued.isNone() ? original : imbued;
				heightValue = current.getRaceHeight();
				widthValue = current.getRaceWidth();
				properties = current.getRaceHandler(object).setEntityProperties(this);
			}
		} else {
			previous = source.previous;
			current = source.current;
			abilities = source.abilities;
		}
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
		this.getRaceHandler().savedNBTData(compound);
		this.getAbilityHandler().saveAbilitiesToNBT(compound);
		return compound;
	}

	@Override
	public void loadFromNBT(NBTTagCompound compound) {
		if (compound.hasKey("first_login")) {
			first_login = false;//compound.getBoolean("first_login");
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
		this.getRaceHandler().loadNBTData(compound);
		this.getAbilityHandler().loadAbilitiesFromNBT(compound);
	}

}
