package xzeroair.trinkets.items.effects;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.client.particles.ParticleGreed;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsConfig.xClient.TrinketItems.Dragon;
import xzeroair.trinkets.util.config.trinkets.ConfigDragonsEye;
import xzeroair.trinkets.util.helpers.OreTrackingHelper;
import xzeroair.trinkets.util.helpers.RayTraceHelper;

public class EffectsDragonsEye {

	private static final ConfigDragonsEye serverConfig = TrinketsConfig.SERVER.Items.DRAGON_EYE;
	private static final Dragon clientConfig = TrinketsConfig.CLIENT.items.DRAGON_EYE;

	public static void playerTicks(int targetValue, EntityLivingBase entity) {
		if ((!serverConfig.oreFinder == false)) {
			final int length = serverConfig.BLOCKS.Blocks.length;
			if ((targetValue < 0) || (targetValue > length)) {
				return;
			}
			final int vd = serverConfig.BLOCKS.DR.C00_VD;
			final int hd = serverConfig.BLOCKS.DR.C001_HD;
			final int rf = clientConfig.C00_RR;
			if (!getTargetsList().isEmpty()) {
				getTargetsList().clear();
			}
			String Type = serverConfig.BLOCKS.Blocks[targetValue];
			final String getName = OreTrackingHelper.translateOreName(Type);
			int color = OreTrackingHelper.getColor(getName);
			Vec3d target = getClosestBlock(entity, entity.getEntityBoundingBox().grow(hd, vd, hd), Type, getMeta(Type));
			//						if ((player.ticksExisted % 8) == 0) {
			//						drawPath(player, target, color);
			//						}
			if ((entity.ticksExisted % rf) == 0) {
				if (serverConfig.BLOCKS.closest) {
					double d = entity.getDistance(target.x, target.y, target.z);
					if (d < 1.8) {
						return;
					}
					playSound(entity, new BlockPos(target), d);
					for (int i = 0; i < 3; i++) {
						SpawnParticle(entity.getEntityWorld(), target, color);
					}
				} else {
					if (!getTargetsList().isEmpty()) {
						playSound(entity, new BlockPos(target), 0.1);
						for (Vec3d pos : getTargetsList()) {
							for (int i = 0; i < 3; i++) {
								SpawnParticle(entity.getEntityWorld(), pos, color);
							}
						}
						getTargetsList().clear();
					}
				}
			}
		}
	}

	public static void playerTicks(ItemStack stack, EntityLivingBase entity) {
		EntityProperties prop = Capabilities.getEntityRace(entity);
		if ((prop != null) && prop.getCurrentRace().equals(EntityRaces.dragon)) {
			return;
		}
		TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
		if ((iCap != null) && (iCap.Target() != -1)) {
			playerTicks(iCap.Target(), entity);
		}
	}

	static List<Vec3d> list = new ArrayList<>();

	private static List<Vec3d> getTargetsList() {
		return list;
	}

	private static Vec3d chestCartDetect(EntityLivingBase player, Vec3d pos, AxisAlignedBB aabb) {
		final List<EntityMinecartChest> cartList = player.getEntityWorld().getEntitiesWithinAABB(EntityMinecartChest.class, aabb);
		Vec3d target = pos;
		double d = 100;
		if (!cartList.isEmpty()) {
			for (final EntityMinecartChest chestCart : cartList) {
				double distance = player.getDistance(chestCart);
				if (distance < d) {
					d = distance;
					target = chestCart.getPositionVector();
					if (!serverConfig.BLOCKS.closest) {
						if (!getTargetsList().contains(target)) {
							getTargetsList().add(target);
						}
					}
				}
			}
		}
		return target;
	}

	private static Vec3d getClosestBlock(EntityLivingBase player, AxisAlignedBB aabb, String target, String meta) {
		final int i = MathHelper.floor(aabb.minX);
		final int j = MathHelper.floor(aabb.maxX + 1.0D);
		final int k = MathHelper.floor(aabb.minY);
		final int l = MathHelper.floor(aabb.maxY + 1.0D);
		final int i1 = MathHelper.floor(aabb.minZ);
		final int j1 = MathHelper.floor(aabb.maxZ + 1.0D);
		Vec3d closest = new Vec3d(player.posX, player.posY, player.posZ);
		double d = 100;
		for (int k1 = i; k1 < j; ++k1) {
			for (int l1 = k; l1 < l; ++l1) {
				for (int i2 = i1; i2 < j1; ++i2) {
					final BlockPos pos = new BlockPos(k1, l1, i2);
					final IBlockState state = player.world.getBlockState(pos);
					final Block block = state.getBlock();
					double distance = player.getDistance(pos.getX(), pos.getY(), pos.getZ());
					if (distance < d) {
						if (!block.getRegistryName().toString().contentEquals("minecraft:air")) {
							if (target.contains(":")) {
								if (meta.isEmpty()) {
									if (block.getRegistryName().toString().contentEquals(target)) {
										if (serverConfig.BLOCKS.closest) {
											d = distance;
										}
										closest = new Vec3d(k1, l1, i2);
										if (!serverConfig.BLOCKS.closest) {
											if (!getTargetsList().contains(closest)) {
												getTargetsList().add(closest);
											}
										}
									}
								} else {
									if (block.getRegistryName().toString().contentEquals(target) && !meta.isEmpty()) {
										if (block.getMetaFromState(state) == Integer.parseInt(meta)) {
											if (serverConfig.BLOCKS.closest) {
												d = distance;
											}
											closest = new Vec3d(k1, l1, i2);
											if (!serverConfig.BLOCKS.closest) {
												if (!getTargetsList().contains(closest)) {
													getTargetsList().add(closest);
												}
											}
										}
									}
								}
							} else {
								final ItemStack blockStack = new ItemStack(block, 1, block.getMetaFromState(state));
								if ((blockStack != null) && !blockStack.isEmpty()) {
									for (final String oreDictionary : OreTrackingHelper.getOreNames(blockStack)) {
										if (oreDictionary.contentEquals(target)) {
											if (serverConfig.BLOCKS.closest) {
												d = distance;
											}
											closest = new Vec3d(k1, l1, i2);
											if (!serverConfig.BLOCKS.closest) {
												if (!getTargetsList().contains(closest)) {
													getTargetsList().add(closest);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (target.equalsIgnoreCase("minecraft:chest")) {
			Vec3d closestCart = chestCartDetect(player, closest, aabb);
			double distance = player.getDistance(closestCart.x, closestCart.y, closestCart.z);
			if (distance < d) {
				if (serverConfig.BLOCKS.closest) {
					d = distance;
				}
				closest = closestCart;
				if (!serverConfig.BLOCKS.closest) {
					if (!getTargetsList().contains(closest)) {
						getTargetsList().add(closest);
					}
				}
			}
		}
		return closest;
	}

	private static void drawPath(EntityLivingBase player, Vec3d target, int color) {
		double d = player.getDistance(target.x, target.y, target.z);
		if (d > 2) {
			RayTraceHelper.Beam beam = new RayTraceHelper.Beam(player.world, player, d, 1D, false);
			GlStateManager.pushMatrix();
			RayTraceHelper.drawPath(player.getPositionVector().add(0, player.getEyeHeight() * 0.8, 0), target, player.world, beam, color, 2);
			GlStateManager.popMatrix();
		}
	}

	public static void SpawnParticle(World world, Vec3d pos, int color) {
		double X = Reference.random.nextDouble() + pos.x;
		double Y = Reference.random.nextDouble() + pos.y;
		double Z = Reference.random.nextDouble() + pos.z;
		GlStateManager.pushMatrix();
		final ParticleGreed effect = new ParticleGreed(world, new Vec3d(X, Y, Z), color, 1F, false);
		Minecraft.getMinecraft().effectRenderer.addEffect(effect);
		GlStateManager.popMatrix();
	}

	public static void playSound(EntityLivingBase entity, BlockPos pos, double distance) {
		if (clientConfig.Dragon_Growl) {
			if (entity instanceof EntityPlayer) {
				final EntityPlayer player = (EntityPlayer) entity;
				final boolean sneaking = clientConfig.Dragon_Growl_Sneak.contentEquals("SNEAK") && player.isSneaking();
				final boolean standing = clientConfig.Dragon_Growl_Sneak.contentEquals("STAND") && !player.isSneaking();
				final boolean both = clientConfig.Dragon_Growl_Sneak.contentEquals("BOTH");
				float configVolume = clientConfig.Dragon_Growl_Volume;
				float volume = ((configVolume / 100));
				if (((sneaking && !standing) == true) || ((standing && !sneaking) == true) || (both == true)) {
					final float v = (float) MathHelper.clamp(((volume * 0.1F) * distance), 0.0F, 3F);
					if (pos != null) {
						player.world.playSound(player, pos, SoundEvents.ENTITY_ENDERDRAGON_GROWL, SoundCategory.RECORDS, v, 1.0F);
					}
				}
			}
		}
	}

	private static String getMeta(String target) {
		String meta = "";
		if (target.contains("[")) {
			final int metaStart = target.indexOf("[");
			final int metaEnd = target.lastIndexOf("]");
			meta = target.substring(metaStart + 1, metaEnd);
		}
		return meta;
	}
}
