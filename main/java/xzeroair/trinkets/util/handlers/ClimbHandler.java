package xzeroair.trinkets.util.handlers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ClimbHandler {

	public static boolean movingForward(EntityPlayer player) {
		if(player.getHorizontalFacing().getAxisDirection() == player.getHorizontalFacing().getAxisDirection().POSITIVE) {
			if(player.getHorizontalFacing().getAxis() == player.getHorizontalFacing().getAxis().X) {
				if(player.motionX > 0) {
					//					System.out.println("Positive X");
					return true;
				}
			}
			if(player.getHorizontalFacing().getAxis() == player.getHorizontalFacing().getAxis().Z) {
				if(player.motionZ > 0) {
					//					System.out.println("Positive Z");
					return true;
				}
			}
		} else {
			if(player.getHorizontalFacing().getAxis() == player.getHorizontalFacing().getAxis().X) {
				if(player.motionX < 0) {
					//					System.out.println("Negative X");
					return true;
				}
			}
			if(player.getHorizontalFacing().getAxis() == player.getHorizontalFacing().getAxis().Z) {
				if(player.motionZ < 0) {
					//					System.out.println("Negative Z");
					return true;
				}
			}
		}
		return false;
	}

	public static boolean canClimb(EntityPlayer player){
		World world = player.getEntityWorld();

		//North
		if(player.getHorizontalFacing() == EnumFacing.NORTH) {

			//			System.out.println(block + "  " + front.getBlock() + "  " + pos.north());
			BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);
			IBlockState front = world.getBlockState(pos.add(0, 0, 0).north());
			IBlockState top = world.getBlockState(pos.add(0, 1, 0).north());
			IBlockState head = world.getBlockState(pos.add(0, 1, 0));
			IBlockState body = world.getBlockState(pos.add(0, 0, 0));
			Block block = front.getBlock();
			Block topBlock = top.getBlock();
			Block headBlock = head.getBlock();
			Block bodyBlock = body.getBlock();
			if((bodyBlock.isPassable(world, pos))){
				if(!(block.isPassable(world, pos.north()))){
					if((!(topBlock.isPassable(world, pos.add(0, 1, 0).north()) || (topBlock instanceof BlockPane))) || (!(headBlock.isPassable(world, pos.add(0, 1, 0)) || (headBlock instanceof BlockSlab) || (headBlock instanceof BlockStairs)))){
						return false;
					}
					if(topBlock instanceof BlockPane){
						return true;
					}
					return true;
				}
			}
			if((bodyBlock instanceof BlockStairs) || (bodyBlock instanceof BlockPane)){
				if(!(headBlock.isPassable(world, pos.add(0, 1, 0)))){
					return false;
				}
				return true;
			}
			if((headBlock instanceof BlockStairs) || (headBlock instanceof BlockSlab)){
				if(headBlock.canPlaceTorchOnTop(head, world, pos.add(0, 1, 0))){
					return true;
				}
			}
		}

		//South
		if(player.getHorizontalFacing() == EnumFacing.SOUTH){
			BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);
			IBlockState front = world.getBlockState(pos.add(0, 0, 0).south());
			IBlockState top = world.getBlockState(pos.add(0, 1, 0).south());
			IBlockState head = world.getBlockState(pos.add(0, 1, 0));
			IBlockState body = world.getBlockState(pos.add(0, 0, 0));
			Block block = front.getBlock();
			Block topBlock = top.getBlock();
			Block headBlock = head.getBlock();
			Block bodyBlock = body.getBlock();
			if((bodyBlock.isPassable(world, pos))){
				if((!(block.isPassable(world, pos.south())) || block.isPassable(world, pos.south()))){
					if((!(topBlock.isPassable(world, pos.add(0, 1, 0).south()) || (topBlock instanceof BlockPane))) || (!(headBlock.isPassable(world, pos.add(0, 1, 0)) || (headBlock instanceof BlockSlab) || (headBlock instanceof BlockStairs)))){
						return false;
					}
					if(topBlock instanceof BlockPane){
						return true;
					}
					return true;
				}
			}
			if((bodyBlock instanceof BlockStairs) || (bodyBlock instanceof BlockPane)){
				if(!(headBlock.isPassable(world, pos.add(0, 1, 0)))){
					return false;
				}
				return true;
			}
			if((headBlock instanceof BlockStairs) || (headBlock instanceof BlockSlab)){
				if(headBlock.canPlaceTorchOnTop(head, world, pos.add(0, 1, 0))){
					return true;
				}
			}
		}

		//East
		if(player.getHorizontalFacing() == EnumFacing.EAST){
			BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);
			IBlockState front = world.getBlockState(pos.add(0, 0, 0).east());
			IBlockState top = world.getBlockState(pos.add(0, 1, 0).east());
			IBlockState head = world.getBlockState(pos.add(0, 1, 0));
			IBlockState body = world.getBlockState(pos.add(0, 0, 0));
			Block block = front.getBlock();
			Block topBlock = top.getBlock();
			Block headBlock = head.getBlock();
			Block bodyBlock = body.getBlock();

			if((bodyBlock.isPassable(world, pos))){
				if(!(block.isPassable(world, pos.east()))){
					if((!(topBlock.isPassable(world, pos.add(0, 1, 0).east()) || (topBlock instanceof BlockPane))) || (!(headBlock.isPassable(world, pos.add(0, 1, 0)) || (headBlock instanceof BlockSlab) || (headBlock instanceof BlockStairs)))){
						return false;
					}
					if(topBlock instanceof BlockPane){
						return true;
					}
					return true;
				}
			}
			if((bodyBlock instanceof BlockStairs) || (bodyBlock instanceof BlockPane)){
				if(!(headBlock.isPassable(world, pos.add(0, 1, 0)))){
					return false;
				}
				return true;
			}
			if((headBlock instanceof BlockStairs) || (headBlock instanceof BlockSlab)){
				if(headBlock.canPlaceTorchOnTop(head, world, pos.add(0, 1, 0))){
					return true;
				}
			}
		}

		//West
		if(player.getHorizontalFacing() == EnumFacing.WEST){
			BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);
			IBlockState front = world.getBlockState(pos.add(0, 0, 0).west());
			IBlockState top = world.getBlockState(pos.add(0, 1, 0).west());
			IBlockState head = world.getBlockState(pos.add(0, 1, 0));
			IBlockState body = world.getBlockState(pos.add(0, 0, 0));
			Block block = front.getBlock();
			Block topBlock = top.getBlock();
			Block headBlock = head.getBlock();
			Block bodyBlock = body.getBlock();

			if((bodyBlock.isPassable(world, pos))){
				if(!(block.isPassable(world, pos.west()))){
					if((!(topBlock.isPassable(world, pos.add(0, 1, 0).west()) || (topBlock instanceof BlockPane))) || (!(headBlock.isPassable(world, pos.add(0, 1, 0)) || (headBlock instanceof BlockSlab) || (headBlock instanceof BlockStairs)))){
						return false;
					}
					if(topBlock instanceof BlockPane){
						return true;
					}
					return true;
				}
			}
			if((bodyBlock instanceof BlockStairs) || (bodyBlock instanceof BlockPane)){
				if(!(headBlock.isPassable(world, pos.add(0, 1, 0))) && !(headBlock instanceof BlockPane)){
					return false;
				}
				if((headBlock instanceof BlockPane) && topBlock.isPassable(world, pos.add(0, 1, 0).west())){
					if(!(block.isPassable(world, pos.west()))){
						//						if(player.capabilities.)
						//						return true;
					}
					return false;
				}
				return true;
			}
			if((headBlock instanceof BlockStairs) || (headBlock instanceof BlockSlab)){
				if(headBlock.canPlaceTorchOnTop(head, world, pos.add(0, 1, 0))){
					return true;
				}
			}
		}
		return false;

	}
}
