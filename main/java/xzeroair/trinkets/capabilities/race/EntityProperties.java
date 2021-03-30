package xzeroair.trinkets.capabilities.race;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.api.events.TransformationEvent;
import xzeroair.trinkets.api.events.TransformationEvent.RaceChangedEvent;
import xzeroair.trinkets.api.events.TransformationEvent.endTransformationEvent;
import xzeroair.trinkets.api.events.TransformationEvent.startTransformationEvent;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.SizeDataPacket;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.races.EntityRaceHelper;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.human.RaceHuman;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.compat.artemislib.SizeAttribute;
import xzeroair.trinkets.util.helpers.EyeHeightHandler;

public class EntityProperties {

	int size = 100;
	int target = 100;
	float defaultWidth;
	float defaultHeight;
	boolean first_login = true;
	boolean login = true;

	boolean changed = false;
	boolean sync = false;

	EntityRace previous, original, imbued, current;

	boolean traitShown = true;
	String traitColor = "16777215";
	float traitOpacity = 1F;

	boolean isFake = false;

	MagicStats magic;
	StatusHandler status;

	EntityLivingBase entity;
	EntityRacePropertiesHandler properties;

	public EntityProperties(EntityLivingBase e) {
		entity = e;
		original = EntityRace.getByNameOrId(Reference.MODID + ":none");
		previous = EntityRace.getByNameOrId(Reference.MODID + ":none");
		imbued = EntityRace.getByNameOrId(Reference.MODID + ":none");
		current = EntityRace.getByNameOrId(Reference.MODID + ":none");
		magic = new MagicStats(e, this);
		properties = new RaceHuman(e, this);
		status = new StatusHandler(e, this);
	}

	@SideOnly(Side.CLIENT)
	public void onRender(RenderLivingBase renderer, boolean isSlim, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		properties.doRenderLayer(renderer, isFake, isSlim, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
	}

	private float stepHeightPrev = 0.6F;

	public void onUpdate() {
		if (!(entity instanceof EntityPlayer)) {
			if (this.isLogin()) {
				this.setDefaultHeight(entity.height);
				this.setDefaultWidth(entity.width);
				this.setLogin(false);
			}
		} else {
			this.setDefaultHeight(1.8f);
			this.setDefaultWidth(0.6f);
		}

		this.stepHeightHandler();

		//		try {
		//			EntityRace race = EntityRace.Registry.getObjectByUUID(UUID.fromString("b01713bd-5af8-4786-bb88-0575df34ed74"));
		//			//			Trinkets.RaceRegistry.containsKey("artemislib:bob");
		//			if (race != null) {
		//				//				System.out.println("Woo it worked?");
		//				System.out.println(race.getName() + " | " + race.getRaceSize());
		//			} else {
		//				System.out.println("Failed");
		//			}
		//		} catch (Exception e) {
		//
		//		}

		if ((original == null) || (previous == null) || (imbued == null)) {
			Trinkets.log.warn(
					entity.getName() + " with ID " + entity.getEntityId() + " errored when adding Race Capability" +
							" Original:" + original +
							" | Previous:" + previous +
							" | Imbued:" + imbued

			);
			original = EntityRace.getByNameOrId(Reference.MODID + ":none");
			previous = EntityRace.getByNameOrId(Reference.MODID + ":none");
			imbued = EntityRace.getByNameOrId(Reference.MODID + ":none");
			return;
		}

		EntityRace race = this.getEntityRace();
		TransformationEvent.RaceChangedEvent rc = new RaceChangedEvent(entity, this, current, race);
		if (!MinecraftForge.EVENT_BUS.post(rc) && rc.raceChanged()) {
			EntityRace newRace = rc.getNewRace();
			if (!current.equals(newRace)) {
				SizeAttribute artemisLib = new SizeAttribute(entity, 0, 0, 0);
				artemisLib.removeModifiers();
				properties.onTransformEnd();
				if (entity.stepHeight != 0.6) {
					entity.stepHeight = 0.6F;
				}
				TransformationEvent.endTransformationEvent end = new endTransformationEvent(entity, this, current, previous);
				MinecraftForge.EVENT_BUS.post(end);
				previous = end.getPreviousRace();//EntityRace.getByUUID(current.getUUID());

				current = newRace;//race;//EntityRace.getByNameOrId(race.getName());
				properties = newRace.getRaceHandler(entity);
				properties.onTransform();
				this.setTargetSize(newRace.getRaceSize());

				TransformationEvent.startTransformationEvent event = new startTransformationEvent(entity, this, current);
				MinecraftForge.EVENT_BUS.post(event);
				magic.syncToManaHud();
				sync = true;
			}
		}

		//Handle Races
		properties.onTick();
		status.onUpdate();
		this.getMagic().regenMana();
		this.sizeHandler();
		if (sync == true) {
			this.sendInformationToAll();
			sync = false;
		}
		//TODO Lycanite thing shivaxi requested
		//LycanitesCompat.convertManaToSpirit(entity);
		keyPressed = -1;
		auxPressed = -1;
	}

	private EntityRace getEntityRace() {
		EntityRace potionRace = EntityRaceHelper.getAttributeRace(entity);
		EntityRace TrinketRace = EntityRaceHelper.getTrinketRace(entity);
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
			return original;
		}
	}

	private void stepHeightHandler() {
		/*
		 * Handles the Step Height Attribute
		 */
		if (entity instanceof EntityPlayer) {
			final IAttributeInstance attribute = entity.getAttributeMap().getAttributeInstance(JumpAttribute.stepHeight);
			if ((attribute != null) && !attribute.getModifiers().isEmpty()) {
				double f = attribute.getAttributeValue();
				float result = (float) (f);
				float step = entity.stepHeight;
				entity.stepHeight = 0.6F;
				if ((step - entity.stepHeight) == 0F) {
					entity.stepHeight = result;
					stepHeightPrev = 0.6F;
				} else if (((step - entity.stepHeight) - result) == -0.6F) {
					entity.stepHeight = result;
					stepHeightPrev = 0.6F;
				} else {
					float stepP = stepHeightPrev;
					stepHeightPrev = step;
					if (((result - 0.6F) + step) == ((result - 0.6F) + stepP)) {
						entity.stepHeight = (result - 0.6F) + stepHeightPrev;
					} else {

					}
				}
			}
			//			System.out.println(entity.stepHeight + " | Step Height Side");
		}
	}

	private void sizeHandler() {
		int defaultSize = 100;
		if (this.isTransforming() || this.isTransformed()) {
			if ((this.getSize() > this.getTargetSize())) {
				this.setSize(this.getSize() - 1);
			}
			if ((this.getSize() < this.getTargetSize())) {
				this.setSize(this.getSize() + 1);
			}
			if (entity instanceof EntityPlayer) {
				EyeHeightHandler.eyeHeightHandler((EntityPlayer) entity, this);
			}
		}
	}

	public void sendInformationToAll() {
		if (!entity.getEntityWorld().isRemote) {
			NetworkHandler.INSTANCE.sendToAll(new SizeDataPacket(entity, this));
		}
	}

	/**
	 * @param receiver Send Capability Information to receiver
	 */
	public void sendInformationToPlayer(EntityLivingBase receiver) {
		if (!entity.getEntityWorld().isRemote && (receiver instanceof EntityPlayer)) {
			NetworkHandler.INSTANCE.sendTo(new SizeDataPacket(entity, this), (EntityPlayerMP) receiver);
		}
	}

	public void sendInformationToTracking() {
		if (!entity.getEntityWorld().isRemote) {
			NetworkHandler.INSTANCE.sendToAllTracking(new SizeDataPacket(entity, this), entity);
		}
	}

	public void sendInformationToServer() {
		if (entity.getEntityWorld().isRemote) {
			NetworkHandler.INSTANCE.sendToServer(new SizeDataPacket(entity, this));
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

	int keyPressed = -1;
	int auxPressed = -1;

	public int getKeyPressed() {
		return keyPressed;
	}

	public int getAuxPressed() {
		return auxPressed;
	}

	public void KeyPressed(int key) {
		keyPressed = key;
	}

	public void AuxKeyPressed(int key) {
		auxPressed = key;
	}

	/*-----------------------------------Boolean Checks-------------------------------*/

	public void setTraitsShown(boolean shown) {
		traitShown = shown;
	}

	public String getTraitColor() {
		return traitColor;
	}

	public void setTraitColor(String color) {
		traitColor = color;
	}

	public float getTraitOpacity() {
		return traitOpacity;
	}

	public void setTraitOpacity(float alpha) {
		traitOpacity = alpha;
	}

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
		imbued = race;
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

	public MagicStats getMagic() {
		return magic;
	}

	public StatusHandler getStatusHandler() {
		return status;
	}

	public void copyFrom(EntityProperties source) {
		login = source.login;
		first_login = source.first_login;

		defaultWidth = source.defaultWidth;
		defaultHeight = source.defaultHeight;

		size = source.size;
		target = source.target;

		previous = source.previous;
		original = source.original;
		imbued = source.imbued;
		traitColor = source.traitColor;
		traitOpacity = source.traitOpacity;
		traitShown = source.traitShown;

		isFake = source.isFake;
		this.getMagic().copyFrom(source.getMagic());
		this.getRaceProperties().copyFrom(source.getRaceProperties());
	}

	public void savedNBTData(NBTTagCompound compound) {
		compound.setInteger("size", size);
		compound.setInteger("target", target);
		compound.setString("original_race", original.getName());
		compound.setString("imbued_race", imbued.getName());
		compound.setString("prev_race", previous.getName());
		compound.setBoolean("trait_shown", traitShown);
		compound.setString("trait_color", traitColor);
		compound.setFloat("trait_opacity", traitOpacity);
		compound.setFloat("default_height", defaultHeight);
		compound.setFloat("default_width", defaultWidth);
		compound.setBoolean("login", login);
		compound.setBoolean("first_login", false);
		compound.setBoolean("fake", isFake);
		this.getMagic().saveToNBT(compound);
		this.getRaceProperties().savedNBTData(compound);
	}

	public void loadNBTData(NBTTagCompound compound) {
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
			EntityRace originalRace = EntityRace.getByNameOrId(Reference.MODID + ":" + compound.getString("original_race"));
			if (originalRace == null) {
				original = EntityRace.getByNameOrId(Reference.MODID + ":human");
			} else {
				original = originalRace;
			}
		}
		if (compound.hasKey("imbued_race")) {
			EntityRace imbuedRace = EntityRace.getByNameOrId(Reference.MODID + ":" + compound.getString("imbued_race"));
			if (imbuedRace == null) {
				imbued = EntityRace.getByNameOrId(Reference.MODID + ":human");
			} else {
				imbued = imbuedRace;
			}
		}
		if (compound.hasKey("prev_race")) {
			EntityRace previousRace = EntityRace.getByNameOrId(Reference.MODID + ":" + compound.getString("prev_race"));
			if (previousRace == null) {
				previous = EntityRace.getByNameOrId(Reference.MODID + ":human");
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
		if (compound.hasKey("trait_opacity")) {
			traitOpacity = compound.getFloat("trait_opacity");
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
		this.getMagic().loadFromNBT(compound);
		this.getRaceProperties().loadNBTData(compound);
	}

}
