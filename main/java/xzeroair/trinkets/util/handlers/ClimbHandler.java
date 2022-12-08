package xzeroair.trinkets.util.handlers;

import javax.annotation.Nonnull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ConfigHelper.ConfigObject;

public class ClimbHandler {

	private EntityLivingBase entity;
	private float entityHeight;
	private World world;
	private EnumFacing facing;
	private Vec3d loc;
	private BlockPos entityPos, bodyPos, headPos, headSpacePos, frontBodyPos, frontHeadPos;
	private IBlockState body, head, headSpace, frontBody, frontHead;
	private String[] climbList = TrinketsConfig.SERVER.races.fairy.allowedBlocks;

	//	private ClimbingConfig config = ClimbingConfig;

	public ClimbHandler(@Nonnull EntityLivingBase e, World w) {
		entity = e;
		world = w;
	}

	public void Climbing() {
		boolean flag = (entity instanceof EntityPlayer) ? !((EntityPlayer) entity).capabilities.isFlying : true;
		//		if (((serverConfig.climbing != false))) {
		if (flag) {
			if (!entity.onGround) {
				if (entity.collidedHorizontally) {
					if (this.canClimb()) {
						if (!entity.isSneaking()) {
							entity.motionY = 0.1f;
						}
						if (entity.isSneaking()) {
							entity.motionY = 0f;
						}
					}
				}
			}
		}
		//		}
	}

	public boolean canClimb() {
		if (!entity.isDead && !((entity instanceof EntityPlayer) && ((EntityPlayer) entity).capabilities.isFlying)) {
			loc = entity.getPositionVector();
			entityPos = new BlockPos(loc);
			facing = entity.getAdjustedHorizontalFacing();
			bodyPos = entityPos;
			frontBodyPos = entityPos.add(0, 0, 0).offset(facing);
			body = world.getBlockState(bodyPos);
			frontBody = world.getBlockState(frontBodyPos);
			for (String s : climbList) {
				ConfigObject object = new ConfigObject(s);
				if (object.doesBlockMatchEntry(body) || object.doesBlockMatchEntry(frontBody))
					return true;
			}
			//			boolean flag1 = BlockHelper.isBlockInList(world, body, bodyPos, climbList);
			//			boolean flag2 = BlockHelper.isBlockInList(world, frontBody, frontBodyPos, climbList);
			//			if (flag1 || flag2) {
			//				return true;
			//			}
			//			for (final String blocks : TrinketsConfig.SERVER.races.fairy.allowedBlocks) {
			//				if (body.getBlock().getRegistryName().toString().contentEquals(blocks) || frontBody.getBlock().getRegistryName().toString().contentEquals(blocks)) {
			//					return true;
			//				}
			//			}
		}
		return false;
	}

	//	public static boolean Climb(EntityPlayer player) {
	//		//		final EnumFacing facing = player.getHorizontalFacing();
	//		//		final BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);
	//		//		final IBlockState state = player.world.getBlockState(pos.add(0, 0, 0).offset(facing));
	//		//		final Block block = state.getBlock();
	//		//		final boolean canPass = block.isPassable(player.world, pos.offset(facing));
	//		// Player = player;
	//		// PlayerHeight = player.height;
	//		if ((player != null) && !player.isDead) {
	//			world = player.world;
	//
	//			playerPos = new BlockPos(player.posX, player.posY, player.posZ);
	//			facing = player.getHorizontalFacing();
	//			bodyPos = playerPos;
	//			// headSpacePos = playerPos.add(0, player.height, 0);
	//			// headPos = playerPos.add(0, 1, 0);
	//			frontBodyPos = playerPos.add(0, 0, 0).offset(facing);
	//			// frontHeadPos = playerPos.add(0, 1, 0).offset(facing);
	//			body = world.getBlockState(bodyPos);
	//			// headSpace = world.getBlockState(headSpacePos);
	//			// head = world.getBlockState(headPos);
	//			frontBody = world.getBlockState(frontBodyPos);
	//			// frontHead = world.getBlockState(frontHeadPos);
	//
	//			for (final String blocks : TrinketsConfig.SERVER.Items.FAIRY_RING.allowedBlocks) {
	//				if (body.getBlock().getRegistryName().toString().contentEquals(blocks) || frontBody.getBlock().getRegistryName().toString().contentEquals(blocks)) {
	//					return true;
	//				}
	//			}
	//		}
	//
	//		// if (Body() && Head() && FrontBody() && FrontHead()) {
	//		// // System.out.println("true");
	//		// return true;
	//		// }
	//		// validHeadSpace(player);
	//		return false;
	//	}
	//
	//	public static void validHeadSpace(EntityPlayer player) {
	//		final AxisAlignedBB bb = player.getEntityBoundingBox();
	//		final AxisAlignedBB headSpaceTest = head.getSelectedBoundingBox(world, headPos);
	//		if (headSpaceTest.intersects(bb)) {
	//			if (!(headSpace.getBlock() instanceof BlockAir)) {
	//				// System.out.println(headSpace.getBlock());
	//			}
	//		}
	//	}
	//
	//	private static boolean Body() {
	//		// final IBlockState iblockstate = world.getBlockState(pos);
	//		final IBlockState Body = body;
	//		// return !iblockstate.getBlock().isNormalCube(iblockstate, world, pos);
	//		if ((Body.getBlock() instanceof BlockPane) || (Body.getBlock() instanceof BlockFence) || (Body.getBlock() instanceof BlockWall)) {
	//			return true;
	//		}
	//		if (Body.getBlock() instanceof BlockStairs) {
	//			if ((Body.getValue(BlockStairs.FACING) == facing) && (Body.getValue(BlockStairs.HALF) != EnumHalf.TOP)) {
	//				return true;
	//			}
	//		}
	//		if ((Body.getBlock() instanceof BlockSlab)) {
	//			if (!Body.isNormalCube()) {
	//				if (Body.getValue(BlockSlab.HALF) == EnumBlockHalf.BOTTOM) {
	//					return true;
	//				}
	//			}
	//		}
	//		return Body.getBlock().isPassable(world, bodyPos);
	//	}
	//
	//	private static boolean Head() {
	//		// final IBlockState iblockstate = world.getBlockState(pos.add(0, Player.height,
	//		// 0));
	//		final IBlockState Head = head;
	//		// return !iblockstate.getBlock().isNormalCube(iblockstate, world, pos);
	//		// Check for Body
	//		if (((Head.getBlock() instanceof BlockPane) || (Head.getBlock() instanceof BlockFence) || (Head.getBlock() instanceof BlockWall))) {
	//			if ((body.getBlock() instanceof BlockStairs)) {
	//				if (body.getValue(BlockStairs.FACING) == facing) {
	//					return true;
	//				}
	//			}
	//			return false;
	//		}
	//		if ((Head.getBlock() instanceof BlockStairs)) {
	//			if ((Head.getValue(BlockStairs.FACING) == facing.getOpposite()) && (Head.getValue(BlockStairs.HALF) != EnumHalf.BOTTOM)) {
	//				return true;
	//			}
	//		}
	//		if ((Head.getBlock() instanceof BlockSlab)) {
	//			if (!Head.isNormalCube()) {
	//				if (Head.getValue(BlockSlab.HALF) == EnumBlockHalf.TOP) {
	//					return true;
	//				}
	//			}
	//		}
	//		return Head.getBlock().isPassable(world, headPos);
	//		// return false;
	//	}
	//
	//	private static boolean HeadSpace() {
	//		return false;
	//	}
	//
	//	private static boolean FrontBody() {
	//		// final IBlockState iblockstate = world.getBlockState(pos.offset(facing));
	//		final IBlockState FrontBody = frontBody;
	//		// if(iblockstate.isOpaqueCube()) {
	//		//
	//		// }
	//		if ((body.getBlock() instanceof BlockStairs) && FrontBody.getBlock().isPassable(world, frontBodyPos)) {
	//			if (body.getValue(BlockStairs.FACING) == facing) {
	//				return true;
	//			}
	//		}
	//		if (((body.getBlock() instanceof BlockPane) || (body.getBlock() instanceof BlockFence) || (body.getBlock() instanceof BlockWall)) && Head()) {
	//			// System.out.println("Trigger");
	//			return true;
	//		}
	//		// if(iblockstate.getBlock() instanceof BlockSlab) {
	//		// if(!iblockstate.isNormalCube()){
	//		// if(iblockstate.getValue(BlockSlab.HALF) == EnumBlockHalf.BOTTOM) {
	//		//
	//		// }
	//		// }
	//		// }
	//		return !FrontBody.getBlock().isPassable(world, frontBodyPos);
	//	}
	//
	//	private static boolean FrontHead() {
	//		// final IBlockState iblockstate = world.getBlockState(pos.add(0, 1,
	//		// 0).offset(facing));
	//		final IBlockState FrontHead = frontHead;
	//		if (FrontHead.getBlock() instanceof BlockStairs) {
	//			if ((FrontHead.getValue(BlockStairs.FACING) == facing.getOpposite()) || (FrontHead.getValue(BlockStairs.FACING) == facing)) {
	//				return true;
	//			}
	//		}
	//		// if((body.getBlock() instanceof BlockStairs)) {
	//		// if(body.getValue(BlockStairs.FACING) == facing) {
	//		// if(Head()) {
	//		// return true;
	//		// }
	//		// }
	//		// }
	//		if (Body() && FrontBody() && !FrontHead.isNormalCube()) {
	//			return true;
	//		}
	//		return FrontHead.getBlock().isPassable(world, frontHeadPos);
	//	}
	//
	public boolean movingForward(EnumFacing facing) {
		if (((facing.getDirectionVec().getX() * entity.motionX) > 0) || ((facing.getDirectionVec().getZ() * entity.motionZ) > 0))
			return true;
		// return ((facing.getDirectionVec().getX() * player.motionX) +
		// (facing.getDirectionVec().getZ() * player.motionZ)) > 0;
		return false;
	}
	//
	//	public static boolean isHeadspaceFree(World world, BlockPos pos, int height) {
	//		for (int y = 0; y < (height); y++) {
	//			if (!isOpenBlockSpace(world, pos.add(0, y, 0))) {
	//				return false;
	//			}
	//		}
	//		return true;
	//	}
	//
	//	private static boolean isOpenBlockSpace(World world, BlockPos pos) {
	//		final IBlockState iblockstate = world.getBlockState(pos);
	//		return !iblockstate.getBlock().isNormalCube(iblockstate, world, pos);
	//	}
	//
	//	public static boolean canClimb(EntityPlayer player, EnumFacing facing) {
	//		final World world = player.getEntityWorld();
	//		final BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);
	//		final IBlockState f = world.getBlockState(pos.add(0, 0, 0).offset(facing));
	//		final IBlockState t = world.getBlockState(pos.add(0, 1, 0).offset(facing));
	//		final IBlockState h = world.getBlockState(pos.add(0, 1, 0));
	//		final IBlockState b = world.getBlockState(pos.add(0, 0, 0));
	//		final Block fb = f.getBlock();
	//		final Block tb = t.getBlock();
	//		final Block hb = h.getBlock();
	//		final Block bb = b.getBlock();
	//		final boolean fbpass = fb.isPassable(world, pos.offset(facing));
	//		final boolean tbpass = tb.isPassable(world, pos.add(0, 1, 0).offset(facing));
	//		final boolean hbpass = hb.isPassable(world, pos.add(0, 1, 0));
	//		final boolean bbpass = bb.isPassable(world, pos);
	//
	//		// if Body Block is Passable Continue
	//		if (bbpass) {
	//			// if Front Block Is Not Passable Continue
	//			// You Have something to Climb on
	//			if (!fbpass) {
	//				if (!hbpass) {
	//					if ((tb instanceof BlockPane)) {
	//
	//					}
	//					if ((hb instanceof BlockStairs)) {
	//						if (h.getValue(BlockStairs.FACING) == facing.getOpposite()) {
	//							return true;
	//						}
	//					}
	//					if ((hb instanceof BlockSlab)) {
	//						if (!h.isNormalCube()) {
	//							if (h.getValue(BlockSlab.HALF) == EnumBlockHalf.TOP) {
	//								return true;
	//							}
	//						}
	//					}
	//					// return false;
	//				}
	//				if (!tbpass) {
	//					if ((tb instanceof BlockStairs)) {
	//						if ((t.getValue(BlockStairs.FACING) == facing.getOpposite()) || (t.getValue(BlockStairs.FACING) == facing)) {
	//							return true;
	//						}
	//					}
	//				}
	//				if (tbpass && hbpass) {
	//					return true;
	//				}
	//			}
	//			if ((fb instanceof BlockSlab)) {
	//				if (!f.isNormalCube()) {
	//					if (f.getValue(BlockSlab.HALF) == EnumBlockHalf.BOTTOM) {
	//						if (hbpass) {
	//							if ((tb instanceof BlockStairs)) {
	//								if (t.getValue(BlockStairs.FACING) == facing.getOpposite()) {
	//									return true;
	//								}
	//							}
	//						}
	//					}
	//				}
	//			}
	//		}
	//		if ((bb instanceof BlockPane) && !(hb instanceof BlockPane)) {
	//			return true;
	//		}
	//		if (bb instanceof BlockStairs) {
	//			if ((b.getValue(BlockStairs.FACING) == facing) && (b.getValue(BlockStairs.HALF) != EnumHalf.TOP)) {
	//				return true;
	//			}
	//		}
	//		if ((bb instanceof BlockSlab)) {
	//			if (!h.isNormalCube()) {
	//				if (b.getValue(BlockSlab.HALF) == EnumBlockHalf.BOTTOM) {
	//					return true;
	//				}
	//			}
	//		}
	//		return false;
	//	}
}