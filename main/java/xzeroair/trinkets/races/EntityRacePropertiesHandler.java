package xzeroair.trinkets.races;

import java.util.function.BiFunction;

import javax.annotation.Nullable;
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
import xzeroair.trinkets.capabilities.race.ElementalAttributes;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.client.gui.entityPropertiesGui.GuiEntityProperties;
import xzeroair.trinkets.client.gui.hud.mana.ManaHud;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.network.IncreasedReachPacket;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.artemislib.SizeAttribute;
import xzeroair.trinkets.util.config.ConfigHelper;
import xzeroair.trinkets.util.config.ConfigHelper.AttributeEntry;
import xzeroair.trinkets.util.handlers.SizeHandler;
import xzeroair.trinkets.util.helpers.AttributeHelper;
import xzeroair.trinkets.util.helpers.ColorHelper;
import xzeroair.trinkets.util.helpers.EyeHeightHandler;
import xzeroair.trinkets.util.helpers.RayTraceHelper;

public abstract class EntityRacePropertiesHandler implements IRaceHandler {

	protected boolean firstUpdate;
	protected boolean firstTransformUpdate;

	protected EntityLivingBase entity;
	protected ElementalAttributes element;

	protected int targetWidth = 100;
	protected int targetHeight = 100;
	protected float defaultWidth;
	protected float defaultHeight;

	protected EntityRace race;

	protected SizeAttribute artemisSupport;

	protected boolean MainAbility = false;
	protected boolean SecondaryAbility = false;

	protected boolean showTraits;

	protected String traitColor;
	protected String traitColorAlt;
	protected EntityProperties properties;

	protected RaceAttributesWrapper raceAttributes;

	public EntityRacePropertiesHandler(EntityLivingBase e, EntityRace race) {
		entity = e;
		firstUpdate = true;
		firstTransformUpdate = true;
		showTraits = true;
		this.setDefaultHeight(e.height);
		this.setDefaultWidth(e.width);
		this.race = race;
		traitColor = ColorHelper.convertDecimalColorToHexadecimal(race.getPrimaryColor());
		traitColorAlt = ColorHelper.convertDecimalColorToHexadecimal(race.getSecondaryColor());
		this.setTargetHeight(race.getRaceHeight());
		this.setTargetWidth(race.getRaceWidth());
		if (Trinkets.ArtemisLib) {
			artemisSupport = this.getArtemisAttributeSize();
		}
	}

	protected void initAttributes() {
		final World world = entity.getEntityWorld();
		if (world.isRemote) {
			return;
		}
		String[] raceAttributes = race.getRaceAttributes().getAttributes();
		if (raceAttributes.length > 0) {
			for (String entry : raceAttributes) {
				AttributeEntry attributeShell = ConfigHelper.getAttributeEntry(entry);
				if (attributeShell != null) {
					String name = attributeShell.getAttribute();
					double amount = attributeShell.getAmount();
					int operation = attributeShell.getOperation();
					boolean isSaved = attributeShell.isSaved();
					UpdatingAttribute attribute = new UpdatingAttribute(race.getName() + "." + name, race.getUUID(), name).setSavedInNBT(isSaved);
					//					attribute.addModifier(entity, (amount), operation);
					attribute.addModifier(entity, (amount * MathHelper.clamp(this.TransformationProgress(), 0.1F, 1F)), operation);
				}
			}
		}
	}

	public EntityRacePropertiesHandler setEntityProperties(EntityProperties properties) {
		this.properties = properties;
		return this;
	}

	public EntityRacePropertiesHandler setFirstUpdate(boolean firstUpdate) {
		this.firstUpdate = firstUpdate;
		return this;
	}

	public EntityRacePropertiesHandler setDefaultHeight(float height) {
		defaultHeight = height;
		return this;
	}

	public EntityRacePropertiesHandler setDefaultWidth(float width) {
		defaultWidth = width;
		return this;
	}

	public EntityRace getRace() {
		return race;
	}

	public void addAbility(IAbilityInterface ability) {
		if (properties != null) {
			properties.getAbilityHandler().registerRaceAbility(this.getRace().getRegistryName().toString(), ability);
		}
	}

	@Nullable
	public IAbilityInterface getAbility(String ability) {
		if (properties != null) {
			return properties.getAbilityHandler().getAbility(ability);
		}
		return null;
	}

	public void removeAbility(String ability) {
		if (properties != null) {
			properties.getAbilityHandler().removeAbility(ability);
		}
	}

	/**
	 * Use {@link #startTransformation()} instead
	 */
	public void onTransform() {
		firstTransformUpdate = true;
		if (properties == null) {
			properties = Capabilities.getEntityProperties(entity);
		}
		try {
			this.loadNBTData(properties.getTag());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.startTransformation();
	}

	/**
	 * Use {@link #endTransformation()} instead
	 */
	public void onTransformEnd() {
		this.endTransformation();
		AttributeHelper.removeAttributesByUUID(entity, race.getUUID());
		if (Trinkets.ArtemisLib && (artemisSupport != null)) {
			artemisSupport.removeModifiers();
		}
		try {
			this.savedNBTData(properties.getTag());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onTick() {
		SizeHandler.setSize(entity, this.getHeight(), this.getWidth());
		this.initAttributes();
		//		System.out.println(traitColor);
		if (this.isTransformed()) {
			if (Trinkets.ArtemisLib && (artemisSupport != null)) {
				artemisSupport.addModifiers();
			}
			this.whileTransformed();
			firstTransformUpdate = false;
		}
		this.updateSize();
		if (cooldown > 0) {
			cooldown--;
		} else {
			cooldown = 0;
		}
	}

	protected float cooldown = 0;

	@Override
	public void interact(PlayerInteractEvent event) {
		if (TrinketsConfig.SERVER.misc.reach) {
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
		return (properties.getHeightValue() != this.getTargetHeight()) || (properties.getWidthValue() != this.getTargetWidth());//false;//this.getSize() != this.getTargetSize();
	}

	public boolean isTransformed() {
		return (properties.getHeightValue() == this.getTargetHeight()) && (properties.getWidthValue() == this.getTargetWidth());
	}

	protected float progress;

	public float TransformationProgress() {
		return progress;
	}

	protected void updateSize() {
		if (this.isTransforming()) {
			final BiFunction<Integer, Integer, Integer> increment = (x, y) -> {
				if (x < y) {
					return x + 1;
				} else if (x > y) {
					return x - 1;
				} else {
					return x;
				}
			};
			final int h = increment.apply(properties.getHeightValue(), this.getTargetHeight());
			properties.setHeightValue(h);
			final int w = increment.apply(properties.getWidthValue(), this.getTargetWidth());
			properties.setWidthValue(w);
		}
		try {
			int previousRaceTargetHeight = properties.getPreviousRace().getRaceHeight();
			int previousRaceTargetWidth = properties.getPreviousRace().getRaceWidth();
			float heightProgress = this.transformProgress(previousRaceTargetHeight, this.getTargetHeight(), properties.getHeightValue());
			float widthProgress = this.transformProgress(previousRaceTargetWidth, this.getTargetWidth(), properties.getWidthValue());
			progress = heightProgress * widthProgress;
		} catch (final Exception e) {
		}
		this.eyeHeightHandler();
	}

	protected float transformProgress(int previousTarget, int currentTarget, int currentValue) {
		float constant = previousTarget >= currentTarget ? (previousTarget - currentTarget) : (currentTarget - previousTarget);
		float rtn = 0;
		if (currentTarget == currentValue) {
		} else if (currentTarget > currentValue) {
			rtn = (currentTarget - currentValue) / constant;
		} else {
			rtn = (currentValue - currentTarget) / constant;
		}
		return 1 - rtn;
	}

	protected void eyeHeightHandler() {
		if (entity instanceof EntityPlayer) {
			EyeHeightHandler.eyeHeightHandler(
					(EntityPlayer) entity,
					this.isTransformed(),
					this.isTransforming(),
					0,
					this.getHeight()
			);
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
		final float TLHeight = (float) (defaultHeight * (properties.getHeightValue() * 0.01));
		return TLHeight;
	}

	public float getWidth() {
		final float TLWidth = (float) (defaultWidth * (properties.getWidthValue() * 0.01));
		return TLWidth;
	}

	private SizeAttribute getArtemisAttributeSize() {
		final double h = (this.getTargetHeight() - 100) * 0.01D;
		final double w = (this.getTargetWidth() - 100) * 0.01D;
		return new SizeAttribute(entity, h, w, 0);
	}

	public void copyFrom(EntityRacePropertiesHandler source, boolean wasDeath, boolean keepInv) {
		if (wasDeath) {
			if (keepInv) {
				targetHeight = source.getTargetHeight();
				targetWidth = source.getTargetWidth();
			} else {
				final boolean isNormal = race == null ? true : race.equals(EntityRaces.none);
				if (isNormal) {
					//
				} else {
					targetHeight = source.getTargetHeight();
					targetWidth = source.getTargetWidth();
				}
			}
		} else {
		}
	}

	public boolean isCreativePlayer() {
		final boolean flag = (entity instanceof EntityPlayer) && (((EntityPlayer) entity).isCreative() || ((EntityPlayer) entity).isSpectator());
		return flag;
	}

	public boolean canFly() {
		return race == null ? false : race.canFly();
	}

	/*------------------------------------------Race Handlers--------------------------------------------*/

	@Override
	public void savedNBTData(NBTTagCompound compound) {
		if ((race != null) && !race.isNone()) {
			final NBTTagCompound rTag = new NBTTagCompound();
			rTag.setBoolean("trait_shown", showTraits);
			rTag.setString("trait_color", traitColor);
			compound.setTag(race.getName(), rTag);
		}
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		if (race != null) {
			final String rName = race.getName();
			if (compound.hasKey(rName)) {
				final NBTTagCompound rTag = compound.getCompoundTag(race.getName());
				if (rTag.hasKey("trait_shown")) {
					showTraits = rTag.getBoolean("trait_shown");
				}
				if (rTag.hasKey("trait_color")) {
					traitColor = rTag.getString("trait_color");
				}
			}
		}
	}

	public boolean showTraits() {
		return showTraits;
	}

	public void setShowTraits(boolean showTraits) {
		this.showTraits = showTraits;
	}

	public String getTraitColor() {
		return traitColor;
	}

	public void setTraitColor(String color) {
		traitColor = color;
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
		if ((this.isTransforming() || this.isTransformed()) && !properties.isNormalSize()) {
			final double hScale = properties.getHeightValue() * 0.01D;
			final double wScale = properties.getWidthValue() * 0.01D;
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
			if ((this.isTransforming() || this.isTransformed()) && !properties.isNormalSize()) {
				GlStateManager.pushMatrix();
				final float t2 = defaultHeight - (entity.height);
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
			if ((this.isTransforming() || this.isTransformed()) && !properties.isNormalSize()) {
				GlStateManager.popMatrix();
			}
		}
	}

	/*
	 * Non Player Entities Only
	 */
	@Override
	public <T extends EntityLivingBase> void doRenderLivingPre(EntityLivingBase entity, double x, double y, double z, RenderLivingBase<T> renderer, float partialTick) {
		if ((this.isTransforming() || this.isTransformed()) && !properties.isNormalSize()) {
			final double hScale = properties.getHeightValue() * 0.01D;
			final double wScale = properties.getWidthValue() * 0.01D;
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
	 * Non Player Entities Only
	 */
	@Override
	public <T extends EntityLivingBase> void doRenderLivingPost(EntityLivingBase entity, double x, double y, double z, RenderLivingBase<T> renderer, float partialTick) {
	}

	//	@Override
	//	public void doRenderHand(EnumHand hand, ItemStack itemStack, float swingProgress, float interpolatedPitch, float equipProgress, float partialTicks) {
	//	}

}
