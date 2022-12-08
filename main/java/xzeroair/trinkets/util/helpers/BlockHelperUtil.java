package xzeroair.trinkets.util.helpers;

import java.util.TreeMap;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import xzeroair.trinkets.network.NetworkHandler;

public class BlockHelperUtil {

	public static TreeMap<Double, Vec3d> findAll(final Vec3d origin, final World world, AxisAlignedBB aabb, BiPredicate<IBlockState, BlockPos> predicate) {
		final TreeMap<Double, Vec3d> collection = new TreeMap();
		if (predicate != null) {
			final int i = MathHelper.floor(aabb.minX);
			final int j = MathHelper.floor(aabb.maxX + 1.0D);
			final int k = MathHelper.floor(aabb.minY);
			final int l = MathHelper.floor(aabb.maxY + 1.0D);
			final int i1 = MathHelper.floor(aabb.minZ);
			final int j1 = MathHelper.floor(aabb.maxZ + 1.0D);
			for (int k1 = i; k1 < j; ++k1) {
				for (int l1 = k; l1 < l; ++l1) {
					for (int i2 = i1; i2 < j1; ++i2) {
						final Vec3d pos = new Vec3d(k1, l1, i2);
						final BlockPos bPos = new BlockPos(pos);
						final IBlockState state = world.getBlockState(bPos);
						if (predicate.test(state, bPos)) {
							double blockDist = pos.distanceTo(origin);
							collection.put(blockDist, pos);
						}
					}
				}
			}
		}
		return collection;
	}

	public static BlockPos findFirst(final Vec3d origin, final World world, AxisAlignedBB aabb, BiPredicate<IBlockState, BlockPos> predicate) {
		if (predicate != null) {
			final int i = MathHelper.floor(aabb.minX);
			final int j = MathHelper.floor(aabb.maxX + 1.0D);
			final int k = MathHelper.floor(aabb.minY);
			final int l = MathHelper.floor(aabb.maxY + 1.0D);
			final int i1 = MathHelper.floor(aabb.minZ);
			final int j1 = MathHelper.floor(aabb.maxZ + 1.0D);
			for (int k1 = i; k1 < j; ++k1) {
				for (int l1 = k; l1 < l; ++l1) {
					for (int i2 = i1; i2 < j1; ++i2) {
						final Vec3d pos = new Vec3d(k1, l1, i2);
						final BlockPos bPos = new BlockPos(pos);
						final IBlockState state = world.getBlockState(bPos);
						if (predicate.test(state, bPos)) {
							return bPos;
						}
					}
				}
			}
		}
		return new BlockPos(origin);
	}

	public static boolean isBlockNearby(final World world, final AxisAlignedBB aabb, BiPredicate<IBlockState, BlockPos> predicate) {
		if (predicate != null) {
			final int i = MathHelper.floor(aabb.minX);
			final int j = MathHelper.floor(aabb.maxX + 1.0D);
			final int k = MathHelper.floor(aabb.minY);
			final int l = MathHelper.floor(aabb.maxY + 1.0D);
			final int i1 = MathHelper.floor(aabb.minZ);
			final int j1 = MathHelper.floor(aabb.maxZ + 1.0D);
			for (int k1 = i; k1 < j; ++k1) {
				for (int l1 = k; l1 < l; ++l1) {
					for (int i2 = i1; i2 < j1; ++i2) {
						final Vec3d pos = new Vec3d(k1, l1, i2);
						final BlockPos bPos = new BlockPos(pos);
						final IBlockState state = world.getBlockState(bPos);
						if (predicate.test(state, bPos)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public static boolean canToolHarvestBlock(ItemStack stack, IBlockState state) {
		return canToolHarvestBlock(stack, state, 0);
	}

	public static boolean canToolHarvestBlock(ItemStack stack, IBlockState state, int bonusToolLevel) {
		Block block = state.getBlock();
		if (state.getMaterial().isToolNotRequired()) {
			return true;
		}
		String type = block.getHarvestTool(state);
		int level = block.getHarvestLevel(state);
		return (stack.getItem().getHarvestLevel(stack, type, null, state) + bonusToolLevel) >= level;
	}

	public static boolean canPlayerHarvestBlock(@Nonnull Block block, @Nonnull EntityPlayer player, ItemStack harvestTool, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
		return canPlayerHarvestBlock(block, player, harvestTool, world, pos, 0);
	}

	public static boolean canPlayerHarvestBlock(@Nonnull Block block, @Nonnull EntityPlayer player, ItemStack harvestTool, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, int bonusToolLevel) {
		IBlockState state = world.getBlockState(pos);
		state = state.getBlock().getActualState(state, world, pos);
		if (state.getMaterial().isToolNotRequired()) {
			return true;
		}

		String tool = block.getHarvestTool(state);
		if ((tool == null) || harvestTool.isEmpty()) {
			return player.canHarvestBlock(state);
		}

		int toolLevel = harvestTool.getItem().getHarvestLevel(harvestTool, tool, player, state) + bonusToolLevel;
		if (toolLevel < 0) {
			return player.canHarvestBlock(state);
		}

		return toolLevel >= block.getHarvestLevel(state);
	}

	public static boolean isToolEffective(ItemStack stack, IBlockState state) {
		for (String type : stack.getItem().getToolClasses(stack)) {
			if ((type != null) && type.equals(state.getBlock().getHarvestTool(state))) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Use for inside Item Classes
	 */
	public static float getEntityDigSpeed(EntityLivingBase entity, ItemStack itemstack, IBlockState state, BlockPos pos) {
		return getEntityDigSpeed(entity, itemstack, state, pos, true);
	}

	/*
	 * Do not call event when used in getBreakSpeed Event
	 */
	public static float getEntityDigSpeed(EntityLivingBase entity, ItemStack itemstack, IBlockState state, BlockPos pos, boolean callDigSpeedEvent) {
		float f = 1.0F;

		if (!itemstack.isEmpty()) {
			f *= itemstack.getDestroySpeed(state);
		}

		if (f > 1.0F) {
			int i = EnchantmentHelper.getEfficiencyModifier(entity);
			if ((i > 0) && !itemstack.isEmpty()) {
				f += (i * i) + 1;
			}
		}

		if (entity.isPotionActive(MobEffects.HASTE)) {
			f *= 1.0F + ((entity.getActivePotionEffect(MobEffects.HASTE).getAmplifier() + 1) * 0.2F);
		}

		if (entity.isPotionActive(MobEffects.MINING_FATIGUE)) {
			float f1;
			switch (entity.getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier()) {
			case 0:
				f1 = 0.3F;
				break;
			case 1:
				f1 = 0.09F;
				break;
			case 2:
				f1 = 0.0027F;
				break;
			case 3:
			default:
				f1 = 8.1E-4F;
			}

			f *= f1;
		}
		if (entity.isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier(entity)) {
			f /= 5.0F;
		}

		if (!entity.onGround) {
			f /= 5.0F;
		}
		if (callDigSpeedEvent && (entity instanceof EntityPlayer)) {
			f = net.minecraftforge.event.ForgeEventFactory.getBreakSpeed((EntityPlayer) entity, state, f, pos);
		}
		return (f < 0 ? 0 : f);
	}

	public static RayTraceResult rayTrace(World world, EntityPlayer player, boolean useLiquids) {
		float f = player.rotationPitch;
		float f1 = player.rotationYaw;
		double d0 = player.posX;
		double d1 = player.posY + player.getEyeHeight();
		double d2 = player.posZ;
		Vec3d vec3d = new Vec3d(d0, d1, d2);
		float f2 = MathHelper.cos((-f1 * 0.017453292F) - (float) Math.PI);
		float f3 = MathHelper.sin((-f1 * 0.017453292F) - (float) Math.PI);
		float f4 = -MathHelper.cos(-f * 0.017453292F);
		float f5 = MathHelper.sin(-f * 0.017453292F);
		float f6 = f3 * f4;
		float f7 = f2 * f4;
		double d3 = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
		Vec3d vec3d1 = vec3d.add(f6 * d3, f5 * d3, f7 * d3);
		return world.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);
	}

	public static ImmutableList<BlockPos> getBlockList(ItemStack stack, World world, EntityPlayer player, BlockPos origin, int width, int height, int depth) {
		return getBlockList(stack, world, player, origin, width, height, depth, -1, null);
	}

	public static ImmutableList<BlockPos> getBlockList(ItemStack stack, World world, EntityPlayer player, BlockPos origin, int width, int height, int depth, Predicate<BlockPos> predicate) {
		return getBlockList(stack, world, player, origin, width, height, depth, -1, predicate);
	}

	public static ImmutableList<BlockPos> getBlockList(ItemStack stack, World world, EntityPlayer player, BlockPos origin, int width, int height, int depth, int distance) {
		return getBlockList(stack, world, player, origin, width, height, depth, distance, null);
	}

	public static ImmutableList<BlockPos> getBlockList(ItemStack stack, World world, EntityPlayer player, BlockPos origin, int width, int height, int depth, int distance, Predicate<BlockPos> predicate) {
		IBlockState state = world.getBlockState(origin);

		if ((state.getMaterial() == Material.AIR) || !canToolHarvestBlock(stack, state)) {
			return ImmutableList.of();
		}

		// raytrace to get the side, but has to result in the same block
		RayTraceResult mop = rayTrace(world, player, true);
		if ((mop == null) || !origin.equals(mop.getBlockPos())) {
			mop = rayTrace(world, player, false);
			if ((mop == null) || !origin.equals(mop.getBlockPos())) {
				return ImmutableList.of();
			}
		}

		int x, y, z;
		BlockPos start = origin;
		switch (mop.sideHit) {
		case DOWN:
		case UP:
			// x y depends on the angle we look?
			Vec3i vec = player.getHorizontalFacing().getDirectionVec();
			x = (vec.getX() * height) + (vec.getZ() * width);
			y = mop.sideHit.getAxisDirection().getOffset() * -depth;
			z = (vec.getX() * width) + (vec.getZ() * height);
			start = start.add(-x / 2, 0, -z / 2);
			if ((x % 2) == 0) {
				if ((x > 0) && ((mop.hitVec.x - mop.getBlockPos().getX()) > 0.5d)) {
					start = start.add(1, 0, 0);
				} else if ((x < 0) && ((mop.hitVec.x - mop.getBlockPos().getX()) < 0.5d)) {
					start = start.add(-1, 0, 0);
				}
			}
			if ((z % 2) == 0) {
				if ((z > 0) && ((mop.hitVec.z - mop.getBlockPos().getZ()) > 0.5d)) {
					start = start.add(0, 0, 1);
				} else if ((z < 0) && ((mop.hitVec.z - mop.getBlockPos().getZ()) < 0.5d)) {
					start = start.add(0, 0, -1);
				}
			}
			break;
		case NORTH:
		case SOUTH:
			x = width;
			y = height;
			z = mop.sideHit.getAxisDirection().getOffset() * -depth;
			start = start.add(-x / 2, -y / 2, 0);
			if (((x % 2) == 0) && ((mop.hitVec.x - mop.getBlockPos().getX()) > 0.5d)) {
				start = start.add(1, 0, 0);
			}
			if (((y % 2) == 0) && ((mop.hitVec.y - mop.getBlockPos().getY()) > 0.5d)) {
				start = start.add(0, 1, 0);
			}
			break;
		case WEST:
		case EAST:
			x = mop.sideHit.getAxisDirection().getOffset() * -depth;
			y = height;
			z = width;
			start = start.add(-0, -y / 2, -z / 2);
			if (((y % 2) == 0) && ((mop.hitVec.y - mop.getBlockPos().getY()) > 0.5d)) {
				start = start.add(0, 1, 0);
			}
			if (((z % 2) == 0) && ((mop.hitVec.z - mop.getBlockPos().getZ()) > 0.5d)) {
				start = start.add(0, 0, 1);
			}
			break;
		default:
			x = y = z = 0;
		}

		ImmutableList.Builder<BlockPos> builder = ImmutableList.builder();
		for (int xp = start.getX(); xp != (start.getX() + x); xp += x / MathHelper.abs(x)) {
			for (int yp = start.getY(); yp != (start.getY() + y); yp += y / MathHelper.abs(y)) {
				for (int zp = start.getZ(); zp != (start.getZ() + z); zp += z / MathHelper.abs(z)) {
					// don't add the origin block
					if ((xp == origin.getX()) && (yp == origin.getY()) && (zp == origin.getZ())) {
						continue;
					}
					if ((distance > 0) && ((MathHelper.abs(xp - origin.getX()) + MathHelper.abs(yp - origin.getY()) + MathHelper.abs(
							zp - origin.getZ()
					)) > distance)) {
						continue;
					}
					BlockPos pos = new BlockPos(xp, yp, zp);
					if ((predicate != null) && !predicate.test(pos)) {
						continue;
					}
					if (isToolEffective(stack, world.getBlockState(pos))) {
						builder.add(pos);
					}
				}
			}
		}
		return builder.build();
	}

	public static boolean canBreakBlock(ItemStack stack, World world, EntityPlayer player, BlockPos harvestedPos, BlockPos targetPos) {
		return canBreakBlock(stack, world, player, harvestedPos, targetPos, 0);
	}

	public static boolean canBreakBlock(ItemStack stack, World world, EntityPlayer player, BlockPos harvestedPos, BlockPos targetPos, int bonusToolLevel) {
		if (world.isAirBlock(targetPos) || (world.getTileEntity(targetPos) != null)) {
			return false;
		}

		IBlockState state = world.getBlockState(targetPos);
		Block block = state.getBlock();

		// only effective materials
		if (!isToolEffective(stack, state)) {
			return false;
		}

		IBlockState refState = world.getBlockState(harvestedPos);
		float refStrength = ForgeHooks.blockStrength(refState, player, world, harvestedPos);
		float strength = ForgeHooks.blockStrength(state, player, world, targetPos);

		//		final boolean forgeSaidNo = !ForgeHooks.canHarvestBlock(block, player, world, targetPos);
		final boolean tooSlow = ((refStrength / strength) > 10f);

		// only harvestable blocks that aren't impossibly slow to harvest
		if (!canPlayerHarvestBlock(block, player, stack, world, targetPos, bonusToolLevel) || tooSlow) {
			return false;
		}

		// From this point on it's clear that the player CAN break the block

		if (player.capabilities.isCreativeMode) {
			block.onBlockHarvested(world, targetPos, state, player);
			if (block.removedByPlayer(state, world, targetPos, player, false)) {
				block.onPlayerDestroy(world, targetPos, state);
			}

			// send update to client
			if (!world.isRemote) {
				NetworkHandler.sendPacket(player, new SPacketBlockChange(world, targetPos));
			}
			return false;
		}
		return true;
	}

	/*
	 *
	 */
	public static void breakBlock(EntityPlayer entity, ItemStack harvestTool, World world, IBlockState harvestedBlockState, BlockPos harvestedPos, BlockPos targetPos) {
		breakBlock(entity, harvestTool, world, harvestedBlockState, harvestedPos, targetPos, true);
	}

	/*
	 * Be Careful calling on blockBreakEvent
	 */
	public static void breakBlock(EntityPlayer entity, ItemStack harvestTool, World world, IBlockState harvestedBlockState, BlockPos harvestedPos, BlockPos targetPos, boolean callBreakEvent) {
		breakBlock(entity, harvestTool, world, harvestedBlockState, harvestedPos, targetPos, true, 0, null);
	}

	public static void breakBlock(EntityPlayer entity, ItemStack harvestTool, World world, IBlockState harvestedBlockState, BlockPos harvestedPos, BlockPos targetPos, boolean callBreakEvent, int bonusToolLevel) {
		breakBlock(entity, harvestTool, world, harvestedBlockState, harvestedPos, targetPos, true, bonusToolLevel, null);
	}

	public static void breakBlock(EntityPlayer entity, ItemStack harvestTool, World world, IBlockState harvestedBlockState, BlockPos harvestedPos, BlockPos targetPos, boolean callBreakEvent, int bonusToolLevel, Function<Integer, Integer> handleXP) {
		if (!canBreakBlock(harvestTool, world, entity, harvestedPos, targetPos, bonusToolLevel)) {
			return;
		}
		IBlockState state = world.getBlockState(targetPos);
		final Block block = state.getBlock();

		// callback to the tool the player uses. Called on both sides. This damages the tool n stuff.
		harvestTool.onBlockDestroyed(world, state, targetPos, entity);

		// server sided handling
		if (!world.isRemote) {
			int xp = block.getExpDrop(state, world, targetPos, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, harvestTool));
			if (callBreakEvent) {
				// send the blockbreak event
				xp = ForgeHooks.onBlockBreakEvent(world, ((EntityPlayerMP) entity).interactionManager.getGameType(), (EntityPlayerMP) entity, targetPos);
			}
			if (handleXP != null) {
				xp = handleXP.apply(xp);
			}
			if (xp == -1) {
				return;
			}
			TileEntity tileEntity = world.getTileEntity(targetPos);
			// ItemInWorldManager.removeBlock
			if (block.removedByPlayer(state, world, targetPos, entity, true)) { // boolean is if block can be harvested, checked above
				block.onPlayerDestroy(world, targetPos, state);
				block.harvestBlock(world, entity, targetPos, state, tileEntity, harvestTool);
				block.dropXpOnBlockBreak(world, targetPos, xp);
			}

			// always send block update to client
			NetworkHandler.sendPacket(entity, new SPacketBlockChange(world, targetPos));
		}
		// client sided handling
		else {
			// clientside we do a "this clock has been clicked on long enough to be broken" call. This should not send any new packets
			// the code above, executed on the server, sends a block-updates that give us the correct state of the block we destroy.

			// following code can be found in PlayerControllerMP.onPlayerDestroyBlock
			world.playBroadcastSound(2001, targetPos, Block.getStateId(state));
			if (block.removedByPlayer(state, world, targetPos, entity, true)) {
				block.onPlayerDestroy(world, targetPos, state);
			}
			// callback to the tool
			harvestTool.onBlockDestroyed(world, state, targetPos, entity);

			if ((harvestTool.getCount() == 0) && (harvestTool == entity.getHeldItemMainhand())) {
				ForgeEventFactory.onPlayerDestroyItem(entity, harvestTool, EnumHand.MAIN_HAND);
				entity.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
			}

			// send an update to the server, so we get an update back
			NetHandlerPlayClient netHandlerPlayClient = Minecraft.getMinecraft().getConnection();
			assert netHandlerPlayClient != null;
			netHandlerPlayClient.sendPacket(
					new CPacketPlayerDigging(
							CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, targetPos, Minecraft
									.getMinecraft().objectMouseOver.sideHit
					)
			);
		}
	}

}
