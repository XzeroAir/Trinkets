package xzeroair.trinkets.entity.camera;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityCamera extends EntityFlying implements IEntityAdditionalSpawnData {

	public EntityPlayer controller;

	private int thirdPersonBackup;
	private boolean hideGuiBackup;
	private int particleBackup;
	private float FOVBackup;
	private int mipmapBackup;
	private int widthBackup;
	private int heightBackup;

	@SideOnly(Side.CLIENT)
	public Framebuffer framebuffer;

	@SideOnly(Side.CLIENT)
	protected Minecraft mc = Minecraft.getMinecraft();

	/**
	 * Controls whether this camera
	 * should be attached to another
	 * entity, or simply exists in free space
	 */
	protected boolean freeCam;


	//TODO: Make this more abstract
	protected int renderHeight;
	protected int renderWidth;
	protected boolean shouldRender = false;

	/**Null for all cameras that have no viewport
	 * TODO: Implement system for this
	 */

	// Variables to be sent between client/server
	protected boolean isAngleLocked;
	protected int playerId;




	private static final DataParameter<Boolean> ANGLE_LOCK = EntityDataManager
			.createKey(EntityCamera.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> PLAYER = EntityDataManager
			.createKey(EntityCamera.class, DataSerializers.VARINT);

	public EntityCamera(World world, double x, double y, double z,
			EntityPlayer player, boolean freeCam) {
		this(world);
		setPosition(x, y, z);
		controller = player;
		setPlayerId(controller.getEntityId());
		this.freeCam = freeCam;
	}

	public EntityCamera(World world) {
		super(world);
	}


	@Override
	public void onUpdate() {
		super.onUpdate();
		if (world.isRemote) {
			doClientUpdate();
		} else {
			if (controller == null) {
				setDead();
				System.out.println("Null controller");
				return;
			}

		}

		if(isRiding() && getRidingEntity().isDead) {
			System.out.println("Tried to reset camera");
			resetCamera();
			setDead();
		}
		if (isDead) {
			resetCamera();
			System.out.println("Tried to reset camera");
		}

	}

	public void doReset() {
		resetCamera();
		setDead();
	}

	private void doClientUpdate() {

		//		if(!freeCam) {
		//			Minecraft mc = Minecraft.getMinecraft();
		//			GameSettings settings = mc.gameSettings;
		//
		//			settings.hideGUI = true;
		//		}
		// settings.fovSetting = 0F;

		/*
		//FIXER METHOD IS UNNECESSARY
		if(mc.player.getEntityId() == playerId) {
			if(mc.getRenderViewEntity() != this) {
				if(!(this instanceof EntityTargeter)) {
					this.setupCamera();
				}
			}
		}
		 */
	}

	public void resetCamera() {
		if (world.isRemote) {
			Minecraft mc = Minecraft.getMinecraft();
			GameSettings settings = mc.gameSettings;
			settings.thirdPersonView = thirdPersonBackup;
			settings.hideGUI = hideGuiBackup;
			settings.particleSetting = particleBackup;
			settings.fovSetting = FOVBackup;

			mc.setRenderViewEntity(controller);
			settings.mipmapLevels = mipmapBackup;
			mc.displayWidth = widthBackup;
			mc.displayHeight = heightBackup;

		} else {
		}
	}

	public void setupCamera() {
		if (world.isRemote) {
			Minecraft mc = Minecraft.getMinecraft();
			GameSettings settings = mc.gameSettings;

			if(controller == null) {
				controller = mc.player;
			}

			thirdPersonBackup = settings.thirdPersonView;
			hideGuiBackup = settings.hideGUI;
			particleBackup = settings.particleSetting;
			FOVBackup = settings.fovSetting;
			mipmapBackup = settings.mipmapLevels;
			widthBackup = mc.displayWidth;
			heightBackup = mc.displayHeight;

			mc.setRenderViewEntity(this);
		}
	}

	public void setWidthAndHeight(int w, int h) {
		setWidth(w);
		setHeight(h);
	}

	@SideOnly(Side.CLIENT)
	public void initFramebuffer() {
		framebuffer = new Framebuffer(renderWidth, renderHeight, true);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		setPlayerId(compound.getInteger("id"));
		Entity ent = world.getEntityByID(playerId);
		if (ent instanceof EntityPlayer) {
			controller = (EntityPlayer) ent;
		}
		setAngleLocked(compound.getBoolean("angle_lock"));
		thirdPersonBackup = compound.getInteger("3pb");
		hideGuiBackup = compound.getBoolean("hideGui");
		particleBackup = compound.getInteger("part");
		FOVBackup = compound.getFloat("fov");
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		compound.setInteger("id", playerId);
		compound.setBoolean("angle_lock", getAngleDataManager());
		compound.setInteger("3pb", thirdPersonBackup);
		compound.setInteger("part", particleBackup);
		compound.setBoolean("hideGui", hideGuiBackup);
		compound.setFloat("fov", FOVBackup);
	}

	@Override
	protected void entityInit() {
		dataManager.register(ANGLE_LOCK, false);
		dataManager.register(PLAYER, -1);
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		if (ANGLE_LOCK.equals(key)) {
			isAngleLocked = getAngleDataManager();
		}
		if (PLAYER.equals(key)) {
			setPlayerId(getPlayerDataManager());
		}
	}

	private boolean getAngleDataManager() {
		return dataManager.get(ANGLE_LOCK);
	}

	private int getPlayerDataManager() {
		return dataManager.get(PLAYER);
	}

	public boolean isAngleLocked() {
		return isAngleLocked;
	}

	public void setAngleLocked(boolean isAngleLocked) {
		dataManager.set(ANGLE_LOCK, isAngleLocked);
		dataManager.setDirty(ANGLE_LOCK);
		this.isAngleLocked = isAngleLocked;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		dataManager.set(PLAYER, playerId);
		dataManager.setDirty(PLAYER);
		this.playerId = playerId;
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeBoolean(freeCam);
		buffer.writeInt(playerId);
		buffer.writeInt(renderHeight);
		buffer.writeInt(renderWidth);
	}

	@Override
	/**
	 * Automatically sets the viewport var to this entity for the player
	 * who spawned it
	 */
	public void readSpawnData(ByteBuf buffer) {
		freeCam = buffer.readBoolean();
		playerId = buffer.readInt();
		setHeight(buffer.readInt());
		setWidth(buffer.readInt());
		if(freeCam) {
			if(Minecraft.getMinecraft().player.getEntityId() == playerId) {
				//TODO: Something with this method, like adding the camera to the maps(?)
			}
		}
	}

	public int getHeight() {
		return renderHeight;
	}

	public void setHeight(int height) {
		renderHeight = height;
	}

	public int getWidth() {
		return renderWidth;
	}

	public void setWidth(int width) {
		renderWidth = width;
	}

	public boolean shouldRender() {
		return shouldRender;
	}

	public void setShouldRender(boolean shouldRender) {
		this.shouldRender = shouldRender;
	}

	public boolean isFreeCam() {
		return freeCam;
	}

	public void setFreeCam(boolean freeCam) {
		this.freeCam = freeCam;
	}

}
