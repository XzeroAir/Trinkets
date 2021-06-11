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
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
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
import xzeroair.trinkets.util.handlers.DodgeHandler;

public class TrinketArcingOrb extends AccessoryBase {

	public final ConfigArcingOrb serverConfig = TrinketsConfig.SERVER.Items.ARCING_ORB;

	public TrinketArcingOrb(String name) {
		super(name);
		this.setUUID("249e65db-7dea-4825-8489-e6aa99a70be1");
		this.setItemAttributes(serverConfig.Attributes);
		ModItems.trinkets.ITEMS.add(this);
	}

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
		if (!serverConfig.dodgeAbility || entity.isSneaking() || !Minecraft.getMinecraft().inGameHasFocus) {
			return;
		}

		TrinketProperties cap = Capabilities.getTrinketProperties(stack);
		if (cap != null) {
			DodgeHandler d = cap.DodgeHandler(entity.world);
			if (d != null) {

				d.handleDodge();

				float yaw = entity.rotationYaw;

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
				final double spd = 1.25;
				double xV = 0;
				double yV = 0;
				double zV = 0;
				if (d.getDirection() == 1) {
					//Left
					xV = look.z * spd;
					yV = 0.3;
					zV = -look.x * spd;
				} else if (d.getDirection() == 3) {
					//Right
					xV = -look.z * spd;
					yV = 0.3;
					zV = look.x * spd;
				} else if (d.getDirection() == 2) {
					//forwards
					xV = look.x * spd;
					yV = 0.3;
					zV = look.z * spd;
				} else if (d.getDirection() == 0) {
					//backwards
					xV = -look.x * 2;
					yV = 0.5;
					zV = -look.z * 2;
				} else {
					xV = 0;
					yV = 0;
					zV = 0;
				}
				if (entity.onGround) {
					EntityProperties prop = Capabilities.getEntityRace(entity);
					float cost = serverConfig.dodgeCost;
					if ((prop != null) && (d.triggerDodge()) && prop.getMagic().spendMana(cost)) {
						entity.setVelocity(xV, yV, zV);
						sendEffectToServer(entity, new ArcingOrbDodgePacket(entity, d.getDirection()));//new LightningOrbPacket(entity, entity.getPositionVector()));
					} else {

					}
				}
			}
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
