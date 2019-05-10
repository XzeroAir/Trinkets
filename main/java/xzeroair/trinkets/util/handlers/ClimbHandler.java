package xzeroair.trinkets.util.handlers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStairs.EnumHalf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ClimbHandler {

	public static boolean movingForward(EntityLivingBase player, EnumFacing facing) {
		if (((facing.getDirectionVec().getX() * player.motionX) > 0) || ((facing.getDirectionVec().getZ() * player.motionZ) > 0)) {
			return true;
		}
		//		return ((facing.getDirectionVec().getX() * player.motionX) + (facing.getDirectionVec().getZ() * player.motionZ)) > 0;
		return false;
	}

	public static double moveX(EntityLivingBase player) {
		if(player.motionX < 0) {
			return -player.motionX;
		}
		return player.motionX;
	}

	public static double moveZ(EntityLivingBase player) {
		if(player.motionZ < 0) {
			return -player.motionZ;
		}
		return player.motionZ;
	}

	public static double moveY(EntityLivingBase player) {
		if(player.motionY < 0) {
			return -player.motionY;
		}
		return player.motionY;
	}

	public static boolean isHeadspaceFree(World world, BlockPos pos, int height)
	{
		for (int y = 0; y < (height); y++)
		{
			if (!isOpenBlockSpace(world, pos.add(0, y, 0))) {
				return false;
			}
		}
		return true;
	}

	private static boolean isOpenBlockSpace(World world, BlockPos pos)
	{
		final IBlockState iblockstate = world.getBlockState(pos);
		return !iblockstate.getBlock().isNormalCube(iblockstate, world, pos);
	}


	public static boolean canClimb(EntityPlayer player, EnumFacing facing) {
		final World world = player.getEntityWorld();
		final BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);
		final IBlockState f = world.getBlockState(pos.add(0, 0, 0).offset(facing));
		final IBlockState t = world.getBlockState(pos.add(0, 1, 0).offset(facing));
		final IBlockState h = world.getBlockState(pos.add(0, 1, 0));
		final IBlockState b = world.getBlockState(pos.add(0, 0, 0));
		final Block fb = f.getBlock();
		final Block tb = t.getBlock();
		final Block hb = h.getBlock();
		final Block bb = b.getBlock();
		final boolean fbpass = fb.isPassable(world, pos.offset(facing));
		final boolean tbpass = tb.isPassable(world, pos.add(0, 1, 0).offset(facing));
		final boolean hbpass = hb.isPassable(world, pos.add(0, 1, 0));
		final boolean bbpass = bb.isPassable(world, pos);

		if(bbpass) {
			if(!fbpass) {
				if((!(tbpass || hbpass))) {
					if((tb instanceof BlockPane)) {

					}
					if((hb instanceof BlockStairs)) {
						if(h.getValue(BlockStairs.FACING) == facing.getOpposite()) {
							return true;
						}
					}
					if((hb instanceof BlockSlab)) {
						if(h.getValue(BlockSlab.HALF) == EnumBlockHalf.TOP) {
							return true;
						}
					}
					return false;
				}
				return true;
			}
		}
		if((bb instanceof BlockPane) && !(hb instanceof BlockPane)) {
			return true;
		}
		if(bb instanceof BlockStairs) {
			if((b.getValue(BlockStairs.FACING) == facing) && (b.getValue(BlockStairs.HALF) != EnumHalf.TOP)) {
				return true;
			}
		}
		if((bb instanceof BlockSlab)) {
			if(b.getValue(BlockSlab.HALF) == EnumBlockHalf.BOTTOM) {
				return true;
			}
		}
		return false;
	}
}