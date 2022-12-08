package xzeroair.trinkets.capabilities.race;

import java.util.List;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation;
import xzeroair.trinkets.api.events.TransformationEvent;
import xzeroair.trinkets.api.events.TransformationEvent.RaceChangedEvent;
import xzeroair.trinkets.api.events.TransformationEvent.endTransformationEvent;
import xzeroair.trinkets.api.events.TransformationEvent.startTransformationEvent;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.capabilities.CapabilityBase;
import xzeroair.trinkets.entity.AlphaWolf;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.SyncRaceDataPacket;
import xzeroair.trinkets.network.trinketcontainer.OpenTrinketGui;
import xzeroair.trinkets.races.EmptyHandler;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.races.EntityRaceHelper;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.traits.AbilityHandler;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.ColorHelper;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class EntityProperties extends CapabilityBase<EntityProperties, EntityLivingBase> {

	protected boolean first_login = true;
	protected boolean login = true;

	protected boolean changed = false;
	protected boolean sync = false;

	protected EntityRace previous;
	protected EntityRace original;
	protected EntityRace imbued;
	protected EntityRace current;

	protected int widthValue = 100;
	protected int heightValue = 100;
	protected float defaultWidth;
	protected float defaultHeight;

	protected boolean traitShown = true;
	protected String traitColor = "16777215";
	//	float traitOpacity = 1F;
	protected ColorHelper color;

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
		properties = new EmptyHandler(e).setEntityProperties(this);
		abilities = new AbilityHandler(e, this);
		keybindHandler = new KeybindHandler();
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
				persistentData.setTag(capTag, new NBTTagCompound());
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
		properties.doRenderLayer(renderer, isFake, isSlim, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
	}

	private float stepHeightPrev = 0.6F;
	private boolean onGround;

	public void onUpdatePre() {
		onGround = object.onGround;
		if (object.world.isRemote) {
			if (!(object instanceof FakePlayer) && (object instanceof EntityPlayer) && object.getName().equals("XzeroAir")) {
				//				System.out.println(properties.getTraitColor());
				//				System.out.println(this.getTag());
				//				NBTTagCompound nbt = object.getEntityData();
				//				if (nbt != null) {
				//					System.out.println(nbt);
				//				}
			}
		}
	}

	@Override
	public void onUpdate() {
		if (object.world.isRemote) {
			if (!(object instanceof FakePlayer) && (object instanceof EntityPlayer) && object.getName().equals("XzeroAir")) {
				//				System.out.println(properties.getTraitColor());
				//				System.out.println(this.getTag());
				//				NBTTagCompound nbt = object.getEntityData();
				//				if (nbt != null) {
				//					System.out.println(nbt);
				//				}
			}
		}
		//		object.world.profiler.startSection("xatCapability");
		//		object.world.profiler.startSection("login");
		if (this.isLogin()) {
			if (!(object instanceof EntityPlayer)) {
				final IAttributeInstance stepheight = object.getAttributeMap().getAttributeInstance(JumpAttribute.stepHeight);
				if (stepheight != null) {
					stepheight.setBaseValue(object.stepHeight);
				}
				this.setDefaultHeight(object.height);
				this.setDefaultWidth(object.width);
			}
			properties.setDefaultHeight(this.getDefaultHeight()).setDefaultWidth(this.getDefaultWidth()).setFirstUpdate(true);
			//			if ((object instanceof EntityPlayer)) {
			//				this.onLogin();
			//				sendInformationToPlayer(object, getTag());
			//			}
			this.setLogin(false);
		}
		//		object.world.profiler.endSection();

		if ((object instanceof EntityPlayer) && !object.getEntityWorld().isRemote) {
			object.onGround = onGround;
		}
		if (!(object instanceof EntityPlayer)) {
			//			object.world.profiler.startSection("entityArmorAbility");
			List<SlotInformation> equipment = TrinketHelper.getSlotInfoForArmor(object, s -> !s.isEmpty() && (s.getItem() instanceof IAccessoryInterface));
			if (!equipment.isEmpty()) {
				for (SlotInformation info : equipment) {
					ItemStack equipStack = info.getStackFromHandler(object);
					if (!equipStack.isEmpty() && (equipStack.getItem() instanceof IAccessoryInterface)) {
						IAccessoryInterface item = (IAccessoryInterface) equipStack.getItem();
						item.onEntityArmorTick(object.getEntityWorld(), object, equipStack);
					}
				}
			}
			//			object.world.profiler.endSection();
			//			Iterable<ItemStack> equipment = object.getEquipmentAndArmor();
			//			for(ItemStack stack : equipment) {
			//				if(!stack.isEmpty() && stack.getItem() instanceof IAccessoryInterface) {
			//					IAccessoryInterface item = (IAccessoryInterface) stack.getItem();
			//					TrinketHelper.equip
			//				}
			//			}
		} else {

			//			try {
			//				ItemStack stack = object.getHeldItemMainhand();
			//				if (!stack.isEmpty()) {
			//					String item = stack.getItem().getRegistryName().toString();
			//					System.out.println(item + "|" + stack.getMetadata() + "|");
			//				}
			//			} catch (Exception e) {
			//				// TODO: handle exception
			//			}
			//			System.out.println(this.getTag());
		}

		//		object.world.profiler.startSection("stepHandler");
		this.stepHeightHandler();
		//		object.world.profiler.endSection();

		//		object.world.profiler.startSection("raceTransformation");
		this.updateRace();
		//		object.world.profiler.endSection();

		//Handle Races
		//		object.world.profiler.startSection("abilityHandler");
		abilities.updateAbilityHandler();
		//		object.world.profiler.endSection();
		//		object.world.profiler.startSection("raceTick");
		properties.onTick();
		//		object.world.profiler.endSection();
		if (sync == true) {
			if (!object.world.isRemote) {
				this.sendInformationToPlayer(object);
				this.sendInformationToTracking();
			} else {
				//				this.saveToNBT(this.getTag());
			}
			sync = false;
		}
		if (object instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) object;
			//			try {
			//				final int total = player.experienceTotal;
			//				final int level = player.experienceLevel;
			//				final float exp = player.experience;
			//				if ((total > 0) || (exp > 0)) {
			//					System.out.println("total:" + total + ", level:" + level + ", exp:" + exp);
			//				}
			//			} catch (Exception e) {
			//			}
			//			try {
			//				IAttributeInstance speed = object.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
			//				System.out.println(speed.getBaseValue() + " | " + speed.getAttributeValue() + " | " + speed.getModifiers());
			//			} catch (Exception e) {
			//			}
			//			try {
			//				World world = object.getEntityWorld();
			//				BlockPos pos = new BlockPos(-64, 79, 351);
			//				IBlockState state = world.getBlockState(pos);
			//				TileEntity te = world.getTileEntity(pos);
			//				Block block = state.getBlock();
			//				boolean isNull = te == null;
			//				System.out.println("No Tile Entity? " + isNull + "|" + block.getLocalizedName());
			//			} catch (Exception e) {
			//			}
			//			System.out.println(object.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
		}
		//		object.world.profiler.endSection();
	}

	public void updateRace() {
		final EntityRace race = this.getEntityRace(); // Causes a lot of Overhead
		final TransformationEvent.RaceChangedEvent rc = new RaceChangedEvent(object, this, current, race);
		if (!MinecraftForge.EVENT_BUS.post(rc)) {
			if (rc.raceChanged()) {
				EntityRace newRace = rc.getNewRace();
				if (newRace == null) {
					newRace = EntityRaces.none;
				}
				final EntityRacePropertiesHandler oldProperties = properties;
				oldProperties.savedNBTData(this.getTag());
				properties.onTransformEnd();
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

				final TransformationEvent.endTransformationEvent end = new endTransformationEvent(object, this, current, previous);
				MinecraftForge.EVENT_BUS.post(end);
				previous = end.getPreviousRace();
				current = newRace;
				properties = newRace
						.getRaceHandler(object)
						.setEntityProperties(this)
						.setDefaultHeight(this.getDefaultHeight())
						.setDefaultWidth(this.getDefaultWidth())
						.setFirstUpdate(first_login);
				properties.onTransform();
				//				this.loadFromNBT(this.getTag());

				final TransformationEvent.startTransformationEvent event = new startTransformationEvent(object, this, current);
				MinecraftForge.EVENT_BUS.post(event);
				sync = true;
			}
		}
	}

	public KeybindHandler getKeybindHandler() {
		return keybindHandler;
	}

	private EntityRace getEntityRace() {
		final EntityRace potionRace = EntityRaceHelper.getAttributeRace(object);
		final EntityRace TrinketRace = EntityRaceHelper.getTrinketRace(object);
		if ((potionRace != null) && !potionRace.equals(EntityRaces.none)) {
			this.setFake(true);
			return potionRace;
		} else if ((TrinketRace != null) && !TrinketRace.equals(EntityRaces.none)) {
			this.setFake(true);
			return TrinketRace;
		} else if ((imbued != null) && !imbued.equals(EntityRaces.none)) {
			this.setFake(false);
			return imbued;
		} else {
			this.setFake(false);
			if (original == null) {
				original = EntityRaces.none;
			}
			return original;
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
		if (!object.getEntityWorld().isRemote && (object instanceof EntityPlayerMP)) {
			if (this.isFirstLogin()) {
				if (TrinketsConfig.SERVER.races.selectionMenu) {
					NetworkHandler.sendTo(new OpenTrinketGui(4), (EntityPlayerMP) object);
				}
			} else {
				//				this.sendInformationToTracking();
				//				System.out.println(this.getTag());
				this.sendInformationToPlayer(object);
				//				final NBTTagCompound tag = new NBTTagCompound();
				//				this.saveToNBT(tag);
				//				NetworkHandler.sendTo(new SyncRaceDataPacket(object, tag), (EntityPlayerMP) object);
			}
		}
	}

	public void onLogoff() {

	}

	public void sendInformationToAll() {
		//		if (!object.getEntityWorld().isRemote) {
		//			final NBTTagCompound tag = this.getTag();//new NBTTagCompound();
		//			this.saveToNBT(tag);
		//			NetworkHandler.sendToAll(new SyncRaceDataPacket(object, tag));
		//		}
	}

	/**
	 * @param receiver Send Capability Information to receiver
	 */
	public void sendInformationToPlayer(EntityLivingBase receiver) {
		if (!object.getEntityWorld().isRemote && (receiver instanceof EntityPlayerMP)) {
			final NBTTagCompound tag = new NBTTagCompound();//this.getTag();
			this.saveToNBT(tag);
			NetworkHandler.sendTo(new SyncRaceDataPacket(object, tag), (EntityPlayerMP) receiver);
		}
	}

	public void sendInformationToPlayer(EntityLivingBase receiver, NBTTagCompound tag) {
		if (!object.getEntityWorld().isRemote && (receiver instanceof EntityPlayerMP)) {
			NetworkHandler.sendTo(new SyncRaceDataPacket(object, tag), (EntityPlayerMP) receiver);
		}
	}

	public void sendInformationToTracking() {
		final World world = object.getEntityWorld();
		if (!world.isRemote && (world instanceof WorldServer)) {
			final WorldServer w = (WorldServer) world;
			final NBTTagCompound tag = new NBTTagCompound();
			this.saveToNBT(tag);
			NetworkHandler.sendToClients(w, object.getPosition(), new SyncRaceDataPacket(object, tag));
		}
	}

	public void sendInformationToServer() {
		if (object.getEntityWorld().isRemote) {
			final NBTTagCompound tag = new NBTTagCompound();
			this.saveToNBT(tag);
			NetworkHandler.sendToServer(new SyncRaceDataPacket(object, tag));
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
		return properties;
	}

	public EntityRace getPreviousRace() {
		return previous;
	}

	public EntityRace setPreviousRace(EntityRace race) {
		previous = race;
		return previous;
	}

	public EntityRace getOriginalRace() {
		return original;
	}

	public EntityRace getCurrentRace() {
		return current;
	}

	public EntityRace getImbuedRace() {
		return imbued;
	}

	public EntityRace setImbuedRace(EntityRace race) {
		if (race != null) {
			imbued = race;
		} else {
			imbued = EntityRaces.none;
		}
		return imbued;
	}

	public EntityRace setOriginalRace(EntityRace race) {
		if (race != null) {
			original = race;
		} else {
			original = EntityRaces.none;
		}
		return original;
	}

	public float getDefaultWidth() {
		return defaultWidth;
	}

	private void setDefaultWidth(float defaultWidth) {
		this.defaultWidth = defaultWidth;
	}

	public float getDefaultHeight() {
		return defaultHeight;
	}

	private void setDefaultHeight(float defaultHeight) {
		this.defaultHeight = defaultHeight;
	}

	public int getHeightValue() {
		return heightValue;//(this.getDefaultHeight() * (this.getSize() * 0.01F));
	}

	public void setHeightValue(int height) {
		heightValue = height;
	}

	public int getWidthValue() {
		return widthValue;//(this.getDefaultWidth() * (this.getSize() * 0.01F));
	}

	public void setWidthValue(int width) {
		widthValue = width;
	}

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}

	public boolean isFirstLogin() {
		return first_login;
	}

	public void setFirstLogin(boolean firstLogin) {
		first_login = firstLogin;
	}

	public boolean isFake() {
		return isFake;
	}

	public void setFake(boolean isFake) {
		this.isFake = isFake;
	}

	public void scheduleResync() {
		sync = true;
	}

	@Override
	public void copyFrom(EntityProperties source, boolean wasDeath, boolean keepInv) {
		login = source.login;
		first_login = source.first_login;
		defaultWidth = source.defaultWidth;
		defaultHeight = source.defaultHeight;

		original = source.original;
		imbued = source.imbued;

		if (wasDeath) {
			if (keepInv) {
				previous = source.previous;
				heightValue = source.heightValue;
				widthValue = source.widthValue;
			} else {
				previous = source.current;
			}
		} else {
			previous = source.previous;
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
		compound.setString("original_race", original.getRegistryName().toString());
		compound.setString("imbued_race", imbued.getRegistryName().toString());
		compound.setString("prev_race", previous.getRegistryName().toString());
		//		compound.setString("current_race", current.getRegistryName().toString());
		compound.setInteger("heightValue", heightValue);
		compound.setInteger("widthValue", widthValue);
		compound.setFloat("default_height", defaultHeight);
		compound.setFloat("default_width", defaultWidth);
		compound.setBoolean("login", login);
		compound.setBoolean("first_login", first_login);
		compound.setBoolean("fake", isFake);
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
			//			final EntityRace currentRace = EntityRace.getByNameOrId(compound.getString("current_race"));
			//			if (currentRace == null) {
			//				current = EntityRaces.none;
			//			} else {
			//				current = currentRace;
			//			}
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
