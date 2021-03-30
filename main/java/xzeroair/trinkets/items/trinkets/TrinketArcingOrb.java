package xzeroair.trinkets.items.trinkets;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.arcingorb.ArcingOrbDodgePacket;
import xzeroair.trinkets.network.particles.LightningBoltPacket;
import xzeroair.trinkets.network.particles.LightningOrbPacket;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.lycanitesmobs.LycanitesCompat;
import xzeroair.trinkets.util.config.trinkets.ConfigArcingOrb;

public class TrinketArcingOrb extends AccessoryBase {

	private final ConfigArcingOrb serverConfig = TrinketsConfig.SERVER.Items.ARCING_ORB;

	public TrinketArcingOrb(String name) {
		super(name);
		this.setUUID("249e65db-7dea-4825-8489-e6aa99a70be1");
		this.setItemAttributes(serverConfig.Attributes);
		ModItems.trinkets.ITEMS.add(this);
	}

	int cd, clock = 0;

	int left, right, front, back = 0;

	int keyDirection = 0;

	boolean reset, trigger = false;

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		super.eventPlayerTick(stack, player);
		LycanitesCompat.removeParalysis(player);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void eventClientTick(ItemStack stack, EntityLivingBase entity) {
		if (!serverConfig.dodgeAbility || entity.isSneaking()) {
			return;
		}
		if ((left > 0) || (right > 0) || (front > 0) || (back > 0)) {
			clock++;
			if (clock >= 5) {
				left = 0;
				right = 0;
				front = 0;
				back = 0;
				clock = 0;
			}
			//					System.out.println(clock);
		}

		float yaw = entity.rotationYaw;

		boolean pressedLeft = Minecraft.getMinecraft().gameSettings.keyBindLeft.isPressed();
		boolean pressedRight = Minecraft.getMinecraft().gameSettings.keyBindRight.isPressed();
		boolean pressedBack = Minecraft.getMinecraft().gameSettings.keyBindBack.isPressed();
		boolean pressedForward = Minecraft.getMinecraft().gameSettings.keyBindForward.isPressed();

		Vec3d look = entity.getLookVec();
		EnumFacing.AxisDirection direction = entity.getHorizontalFacing().getAxisDirection();
		Axis axis = entity.getHorizontalFacing().getAxis();

		double x = 0;
		double z = 0;
		if ((axis == Axis.Z)) {
			if ((direction == AxisDirection.POSITIVE)) {
				x = entity.getLookVec().z;
				z = -entity.getLookVec().x;
			} else {
				x = entity.getLookVec().z;
				z = -entity.getLookVec().x;
			}
		} else {
			if ((direction == AxisDirection.POSITIVE)) {
				x = entity.getLookVec().z;
				z = -entity.getLookVec().x;
			} else {
				x = entity.getLookVec().z;
				z = -entity.getLookVec().x;
			}
		}
		// System.out.println(axis + " " + direction);
		// System.out.println(left + " " + right + " " + front + " " + back);
		// System.out.println(player.getLookVec().x);
		final double spd = 1.25;
		// Need a Timer to reset to 0 if no double tap after a few seconds
		double xV = 0;
		double yV = 0;
		double zV = 0;
		if (pressedLeft) {
			left++;
			right = 0;
			front = 0;
			back = 0;
		}
		if (pressedRight) {
			left = 0;
			right++;
			front = 0;
			back = 0;
		}
		if (pressedForward) {
			left = 0;
			right = 0;
			front++;
			back = 0;
		}
		if (pressedBack) {
			left = 0;
			right = 0;
			front = 0;
			back++;
		}
		if (pressedLeft) {
			keyDirection = 1;
			//Left
			xV = look.z * spd;
			yV = 0.3;
			zV = -look.x * spd;
		} else if (pressedRight) {
			keyDirection = 2;
			//Right
			xV = -look.z * spd;
			yV = 0.3;
			zV = look.x * spd;
			//					-look.z * spd, 0.3, look.x * spd
		} else if (pressedForward) {
			keyDirection = 3;
			//forwards
			xV = look.x * spd;
			yV = 0.3;
			zV = look.z * spd;
			//					player.setVelocity(look.x * spd, 0.3, look.z * spd);
		} else if (pressedBack) {
			keyDirection = 4;
			//backwards
			xV = -look.x * 2;
			yV = 0.5;
			zV = -look.z * 2;
			//					player.setVelocity(-look.x * 2, 0.5, -look.z * 2);
		} else {
			keyDirection = 0;
		}
		if (entity.onGround) {
			EntityProperties prop = Capabilities.getEntityRace(entity);
			float cost = serverConfig.dodgeCost;
			//					System.out.println(player.getName());
			if ((prop != null) && (prop.getMagic().getMana() >= cost)) {
				if ((keyDirection > 0) && ((left > 1) || (right > 1) || (front > 1) || (back > 1))) {
					//TODO Replace SetMana With Spend Mana, but Fix Side Issue
					prop.getMagic().setMana(prop.getMagic().getMana() - cost);
					prop.getMagic().setManaRegenTimeout();
					prop.sendInformationToServer();
					entity.setVelocity(xV, yV, zV);
					//TODO this Doesn't Render to other players
					//TODO it Now Renders for others, but I need to clean up the code
					//					this.generateEffect(entity.getEntityWorld(), entity.getPositionVector().add(0, entity.height * 0.5F, 0));
					sendEffectToServer(entity, new ArcingOrbDodgePacket(entity, keyDirection));//new LightningOrbPacket(entity, entity.getPositionVector()));
					keyDirection = 0;
				}
			} else {
				if ((keyDirection > 0) && ((left > 1) || (right > 1) || (front > 1) || (back > 1))) {
					String Message = "No MP";
					if (entity instanceof EntityPlayer) {
						((EntityPlayer) entity).sendStatusMessage(new TextComponentString(Message), true);
					}
					keyDirection = 0;
				}
			}
		} else {
			keyDirection = 0;
			left = 0;
			right = 0;
			front = 0;
			back = 0;
			clock = 0;
		}

	}

	public static void sendEffectToServer(EntityLivingBase entity, IMessage message) {
		if (entity.world.isRemote) {
			NetworkHandler.INSTANCE.sendToServer(message);
		}
	}

	public static void SyncLightningBolt(EntityLivingBase entity, Vec3d start, Vec3d end) {//, IMessage message) {
		if (!entity.world.isRemote) {
			LightningBoltPacket message = new LightningBoltPacket(entity, start, end, 15F, 2F);
			if (entity instanceof EntityPlayer) {
				NetworkHandler.INSTANCE.sendTo(message, (EntityPlayerMP) entity);
			}
			NetworkHandler.INSTANCE.sendToAllTracking(message, entity);
		}
	}

	public static void SyncLightningOrb(EntityLivingBase entity, Vec3d end) {//, IMessage message) {
		if (!entity.world.isRemote) {
			LightningOrbPacket message = new LightningOrbPacket(entity, end);
			if (entity instanceof EntityPlayer) {
				NetworkHandler.INSTANCE.sendTo(message, (EntityPlayerMP) entity);
			}
			NetworkHandler.INSTANCE.sendToAllTracking(message, entity);
		}
	}

	@SideOnly(Side.CLIENT)
	private void generateEffect(World world, Vec3d vec) {
		int color = 2515356;
		Trinkets.proxy.renderEffect(2, world, vec.x, vec.y, vec.z, vec.x, vec.y, vec.z, color, 0.9F, 1F);
	}

	@Override
	public void playerEquipped(ItemStack stack, EntityLivingBase player) {
		super.playerEquipped(stack, player);
	}

	@Override
	public void playerUnequipped(ItemStack stack, EntityLivingBase player) {
		super.playerUnequipped(stack, player);
	}

	@Override
	public boolean ItemEnabled() {
		return serverConfig.enabled;
	}

}
