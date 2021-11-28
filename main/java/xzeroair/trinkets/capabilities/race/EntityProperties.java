package xzeroair.trinkets.capabilities.race;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.api.events.TransformationEvent;
import xzeroair.trinkets.api.events.TransformationEvent.RaceChangedEvent;
import xzeroair.trinkets.api.events.TransformationEvent.endTransformationEvent;
import xzeroair.trinkets.api.events.TransformationEvent.startTransformationEvent;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.capabilities.CapabilityBase;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.SyncRaceDataPacket;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.races.EntityRaceHelper;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.human.RaceHuman;
import xzeroair.trinkets.traits.AbilityHandler;
import xzeroair.trinkets.traits.AbilityHandler.Storage;
import xzeroair.trinkets.traits.abilities.IAbilityHandler;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.compat.artemislib.SizeAttribute;
import xzeroair.trinkets.util.helpers.ColorHelper;
import xzeroair.trinkets.util.helpers.EyeHeightHandler;

public class EntityProperties extends CapabilityBase<EntityProperties, EntityLivingBase> {

	protected int size, width, height = 100;
	protected int target, targetWidth, targetHeight = 100;
	protected float defaultWidth;
	protected float defaultHeight;
	protected boolean first_login = true;
	protected boolean login = true;

	protected boolean changed = false;
	protected boolean sync = false;

	protected EntityRace previous, original, imbued, current;

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
		size = 100;
		width = 100;
		height = 100;
		target = 100;
		targetWidth = 100;
		targetHeight = 100;
		original = EntityRace.getByNameOrId(Reference.MODID + ":none");
		previous = EntityRace.getByNameOrId(Reference.MODID + ":none");
		imbued = EntityRace.getByNameOrId(Reference.MODID + ":none");
		current = EntityRace.getByNameOrId(Reference.MODID + ":none");
		//		magic = new MagicStats(e, this);
		properties = new RaceHuman(e, this);
		//		status = new StatusHandler(e, this);
		color = new ColorHelper().setColor(this.getTraitColor());
		equippedItems = new TreeMap<>();
		abilities = new AbilityHandler(e);
		keybindHandler = new KeybindHandler();

	}

	@Override
	public NBTTagCompound getTag() {
		if (object.getEntityData() != null) {
			tag = object.getEntityData();
			return tag;
		}
		return super.getTag();
	}

	protected Map<String, ItemStack> equippedItems;
	protected Map<Integer, ItemStack> equippedBaubles;
	protected Map<Integer, ItemStack> equippedTrinkets;

	public void parseEquipped() {
		if (object instanceof EntityPlayer) {
			//			if (equippedBaubles == null) {
			//				equippedBaubles = new TreeMap<>();
			//			}
			//			if (equippedTrinkets == null) {
			//				equippedTrinkets = new TreeMap<>();
			//			}
			//			TrinketHelper.getEquippedBaubles(equippedBaubles, (EntityPlayer) object);
			//			TrinketHelper.getEquippedTrinkets(equippedTrinkets, (EntityPlayer) object);
			TrinketHelper.getEquippedList(equippedItems, (EntityPlayer) object);
		}
	}

	public Map<String, ItemStack> getEquippedItems() {
		return equippedItems;
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
	}

	@Override
	public void onUpdate() {
		if ((object instanceof EntityPlayer) && !object.getEntityWorld().isRemote) {
			object.onGround = onGround;
		}
		this.parseEquipped();
		abilities.accessoriesCheck(equippedItems);
		abilities.cleanUp();
		color = color.setColor(this.getTraitColor());
		if (!(object instanceof EntityPlayer)) {
			if (this.isLogin()) {
				final IAttributeInstance stepheight = object.getAttributeMap().getAttributeInstance(JumpAttribute.stepHeight);
				if (stepheight != null) {
					stepheight.setBaseValue(object.stepHeight);
				}
				this.setDefaultHeight(object.height);
				this.setDefaultWidth(object.width);
				this.setLogin(false);
			}
		} else {
			this.setDefaultHeight(1.8f);
			this.setDefaultWidth(0.6f);
		}

		this.stepHeightHandler();

		final EntityRace race = this.getEntityRace();
		final TransformationEvent.RaceChangedEvent rc = new RaceChangedEvent(object, this, current, race);
		if (!MinecraftForge.EVENT_BUS.post(rc)) {
			if (rc.raceChanged()) {
				EntityRace newRace = rc.getNewRace();
				if (newRace == null) {
					newRace = EntityRace.getByNameOrId(Reference.MODID + ":none");
				}
				final SizeAttribute artemisLib = new SizeAttribute(object, 0, 0, 0);
				artemisLib.removeModifiers();
				properties.onTransformEnd();
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
				properties = newRace.getRaceHandler(object);
				properties.onTransform();

				this.setTargetSize(newRace.getRaceSize());

				final TransformationEvent.startTransformationEvent event = new startTransformationEvent(object, this, current);
				MinecraftForge.EVENT_BUS.post(event);
				sync = true;
			}
		}

		//Handle Races
		properties.onTick();
		this.sizeHandler();
		if (sync == true) {
			this.sendInformationToAll();
			sync = false;
		}
		abilities.tickAbilities(object);
		if (object instanceof EntityPlayer) {
			//			for (final IAbilityInterface ability : abilities.getAbilitiesList()) {
			//				System.out.println(ability.getName());
			//			}
			//			if (object.getEntityWorld().isRemote) {
			//				final boolean hasItem = TrinketHelper.AccessoryCheck(object, ModItems.RaceTrinkets.TrinketFaelisRing);
			//				System.out.println(hasItem);
			//			}
			//			try {
			//
			//				final int t = this.getTargetSize();
			//				final int s = this.getSize();
			//				final boolean tr = current.equals(EntityRaces.none);//this.isTransforming() || this.isTransformed();
			//				//				System.out.println(current.getName() + " | " + imbued.getName());
			//				int ta = t - s;
			//				if (s < 100) {
			//					ta = 100 - s;
			//				} else if (s == 100) {
			//					ta = 100;
			//				} else {
			//					ta = 100 - s;
			//				}
			//				if (ta < 0) {
			//					ta = -ta;
			//				}
			//				int st = 100;//t >= 100 ? t - 100 : 100 - t;
			//				// st is the static difference between the old target size, and current target size
			//				if (t < 100) {
			//					st = 100 - t; // if less then zero reverse it so it's a positive number
			//				} else if (t == 100) {
			//					st = 100; // if it's zero, set it to 100 so we don't end up dividing by zero
			//				} else {
			//					st = t - 100;
			//				}
			//				final float a = ta < st ? st - ta : ta - 100;
			//
			//				//				System.out.println(a + " | " + ta + " | " + st);
			//				/*
			//				 * if target > 100 then target - 100, if target < 100 then 100 - target
			//				 */
			//				//				float a = t >= s ? ((s * 1F) / (t * 1F)) : ((t * 1F) / (s * 1F));
			//				//				float a = t < s ? ((st * 1F) / (ta * 1F)) : ((ta * 1F) / (st * 1F));
			//				//				float a = t < s ? ((ta * 1F) / (st * 1F)) : ((st * 1F) / (ta * 1F));
			//				//		attributes.addAttributes((properties.getSize() * 100) / 100);
			//				final float percent = this.isTransformed() ? 1F : a;//(ta * 1F) / (st * 1F);//(properties.getSize() * 1) * 0.01F;
			//				//				System.out.println(percent + " | Size:" + s + " | Target:" + t);
			//				//				System.out.println(ta + " | " + st + " | " + percent);
			//			} catch (final Exception e) {
			//				System.out.println("Whoops");
			//			}
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
				original = EntityRace.getByNameOrId(Reference.MODID + ":none");
			}
			return original;
		}
	}

	private void stepHeightHandler() {
		/*
		 * Handles the Step Height Attribute
		 */
		//		if (entity instanceof EntityPlayer) {
		final IAttributeInstance attribute = object.getAttributeMap().getAttributeInstance(JumpAttribute.stepHeight);
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

	private void sizeHandler() {
		final int defaultSize = 100;
		if (this.isTransforming() || this.isTransformed()) {
			if ((this.getSize() > this.getTargetSize())) {
				this.setSize(this.getSize() - 1);
			}
			if ((this.getSize() < this.getTargetSize())) {
				this.setSize(this.getSize() + 1);
			}
			if (object instanceof EntityPlayer) {
				EyeHeightHandler.eyeHeightHandler((EntityPlayer) object, this);
			}
		}
	}

	public void sendInformationToAll() {
		if (!object.getEntityWorld().isRemote) {
			final NBTTagCompound tag = new NBTTagCompound();
			this.saveToNBT(tag);
			NetworkHandler.sendToAll(new SyncRaceDataPacket(object, tag));
		}
	}

	/**
	 * @param receiver Send Capability Information to receiver
	 */
	public void sendInformationToPlayer(EntityLivingBase receiver) {
		if (!object.getEntityWorld().isRemote && (receiver instanceof EntityPlayerMP)) {
			final NBTTagCompound tag = new NBTTagCompound();
			this.saveToNBT(tag);
			this.saveAbilitiesToNBT(tag);
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
			this.saveAbilitiesToNBT(tag);
			NetworkHandler.sendToClients(w, object.getPosition(), new SyncRaceDataPacket(object, tag));
		}
	}

	public void sendInformationToServer() {
		if (object.getEntityWorld().isRemote) {
			final NBTTagCompound tag = new NBTTagCompound();
			this.saveToNBT(tag);
			this.saveAbilitiesToNBT(tag);
			NetworkHandler.sendToServer(new SyncRaceDataPacket(object, tag));
		}
	}

	/*-----------------------------------Boolean Checks-------------------------------*/

	public boolean isNormalHeight() {
		return size == 100;
	}

	public boolean isTransforming() {
		return this.getSize() != this.getTargetSize();
	}

	public boolean isTransformed() {
		return current.equals(EntityRaces.none) ? false : this.getSize() == this.getTargetSize();
	}

	public boolean showTraits() {
		return traitShown;
	}

	/*-----------------------------------Boolean Checks-------------------------------*/

	public void setTraitsShown(boolean shown) {
		traitShown = shown;
	}

	public String getTraitColor() {
		return traitColor;
	}

	public ColorHelper getTraitColorHandler() {
		return color;
	}

	public void setTraitColor(String color) {
		traitColor = color;
	}

	//	public float getTraitOpacity() {
	//		return traitOpacity;
	//	}
	//
	//	public void setTraitOpacity(float alpha) {
	//		traitOpacity = alpha;
	//	}

	/**
	 * Get the Entity Race Handler
	 */
	public EntityRacePropertiesHandler getRaceProperties() {
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
			imbued = EntityRace.getByNameOrId(Reference.MODID + ":none");
		}
		return imbued;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTargetSize() {
		return target;
	}

	public void setTargetSize(int target) {
		this.target = target;
	}

	public float getHeight() {
		return (this.getDefaultHeight() * (this.getSize() * 0.01F));
	}

	public float getWidth() {
		return (this.getDefaultWidth() * (this.getSize() * 0.01F));
	}

	public float getDefaultWidth() {
		return defaultWidth;
	}

	public void setDefaultWidth(float defaultWidth) {
		this.defaultWidth = defaultWidth;
	}

	public float getDefaultHeight() {
		return defaultHeight;
	}

	public void setDefaultHeight(float defaultHeight) {
		this.defaultHeight = defaultHeight;
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
				size = source.size;
				previous = source.previous;
			} else {
				final boolean isNormal = imbued.equals(EntityRaces.none);
				if (isNormal) {
				} else {
					size = imbued.getRaceSize();
				}
				previous = source.current;
			}
		} else {
			size = source.size;
			target = source.target;
			previous = source.previous;
		}

		traitColor = source.traitColor;
		traitShown = source.traitShown;

		//		abilities = source.abilities;
		this.getRaceProperties().copyFrom(source.getRaceProperties(), wasDeath, keepInv);
	}

	@Override
	public void saveToNBT(NBTTagCompound compound) {
		compound.setInteger("size", size);
		compound.setInteger("target", target);
		compound.setString("original_race", original.getName());
		compound.setString("imbued_race", imbued.getName());
		compound.setString("prev_race", previous.getName());
		compound.setBoolean("trait_shown", traitShown);
		compound.setString("trait_color", traitColor);
		//		compound.setFloat("trait_opacity", traitOpacity);
		compound.setFloat("default_height", defaultHeight);
		compound.setFloat("default_width", defaultWidth);
		compound.setBoolean("login", login);
		compound.setBoolean("first_login", false);
		compound.setBoolean("fake", isFake);
		this.getRaceProperties().savedNBTData(compound);
	}

	public void saveAbilitiesToNBT(NBTTagCompound compound) {
		for (final Entry<IAbilityInterface, Storage> ability : abilities.getAbilities().entrySet()) {
			try {
				if (ability.getValue() != null) {
					final IAbilityHandler handler = ability.getValue().handler();
					if ((handler != null)) {
						handler.saveStorage(compound);
					}
				}
			} catch (final Exception e) {
				Trinkets.log.error("Error when saving ability:" + ability.getKey().getName());
				e.printStackTrace();
			}
		}
	}

	@Override
	public void loadFromNBT(NBTTagCompound compound) {
		if (compound.hasKey("first_login")) {
			first_login = compound.getBoolean("first_login");
		}
		if (compound.hasKey("login")) {
			login = compound.getBoolean("login");
		}
		if (compound.hasKey("size")) {
			size = compound.getInteger("size");
		}
		if (compound.hasKey("target")) {
			target = compound.getInteger("target");
		}
		if (compound.hasKey("original_race")) {
			final EntityRace originalRace = EntityRace.getByNameOrId(Reference.MODID + ":" + compound.getString("original_race"));
			if (originalRace == null) {
				original = EntityRace.getByNameOrId(Reference.MODID + ":none");
			} else {
				original = originalRace;
			}
		}
		if (compound.hasKey("imbued_race")) {
			final EntityRace imbuedRace = EntityRace.getByNameOrId(Reference.MODID + ":" + compound.getString("imbued_race"));
			if (imbuedRace == null) {
				imbued = EntityRace.getByNameOrId(Reference.MODID + ":none");
			} else {
				imbued = imbuedRace;
			}
		}
		if (compound.hasKey("prev_race")) {
			final EntityRace previousRace = EntityRace.getByNameOrId(Reference.MODID + ":" + compound.getString("prev_race"));
			if (previousRace == null) {
				previous = EntityRace.getByNameOrId(Reference.MODID + ":none");
			} else {
				previous = previousRace;
			}
		}

		if (compound.hasKey("trait_shown")) {
			traitShown = compound.getBoolean("trait_shown");
		}
		if (compound.hasKey("trait_color")) {
			traitColor = compound.getString("trait_color");
		}
		//		if (compound.hasKey("trait_opacity")) {
		//			traitOpacity = compound.getFloat("trait_opacity");
		//		}
		if (compound.hasKey("default_height")) {
			defaultHeight = compound.getFloat("default_height");
		}
		if (compound.hasKey("default_width")) {
			defaultWidth = compound.getFloat("default_width");
		}
		if (compound.hasKey("fake")) {
			isFake = compound.getBoolean("fake");
		}
		this.getRaceProperties().loadNBTData(compound);
	}

	public void loadAbilitiesFromNBT(NBTTagCompound compound) {
		for (final Entry<IAbilityInterface, Storage> ability : abilities.getAbilities().entrySet()) {
			try {
				if (ability.getValue() != null) {
					final IAbilityHandler handler = ability.getValue().handler();
					if ((handler != null)) {
						handler.loadStorage(compound);
					}
				}
			} catch (final Exception e) {
				Trinkets.log.error("Error when loading ability:" + ability.getKey().getName());
				e.printStackTrace();
			}
		}
	}

}
