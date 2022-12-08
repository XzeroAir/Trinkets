package xzeroair.trinkets.traits.abilities;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ConfigHelper.ConfigObject;

public class AbilityClimbing extends Ability implements ITickableAbility {

	private float entityHeight;
	private BlockPos entityPos, bodyPos, headPos, headSpacePos, frontBodyPos, frontHeadPos;
	private IBlockState body, head, headSpace, frontBody, frontHead;
	private final String[] climbList = TrinketsConfig.SERVER.races.fairy.allowedBlocks;

	public AbilityClimbing() {
		super(Abilities.blockClimbing);
	}

	@Override
	public void tickAbility(EntityLivingBase entity) {
		final boolean flag = this.isCreativePlayer(entity);
		if (!flag) {
			if (!entity.onGround) {
				if (entity.collidedHorizontally) {
					if (this.canClimb(entity)) {
						if (!entity.isSneaking()) {
							entity.motionY = 0.1f;
						}
						if (entity.isSneaking()) {
							entity.motionY = 0f;
						}
						entity.fallDistance = 0;
					}
				}
			}
		}
	}

	protected boolean movingForward(EntityLivingBase entity, EnumFacing facing) {
		if (((facing.getDirectionVec().getX() * entity.motionX) > 0) || ((facing.getDirectionVec().getZ() * entity.motionZ) > 0))
			return true;
		// return ((facing.getDirectionVec().getX() * player.motionX) +
		// (facing.getDirectionVec().getZ() * player.motionZ)) > 0;
		return false;
	}

	protected boolean canClimb(EntityLivingBase entity) {
		final World world = entity.getEntityWorld();
		entityPos = new BlockPos(entity.getPositionVector());
		bodyPos = entityPos;
		frontBodyPos = entityPos.add(0, 0, 0).offset(entity.getAdjustedHorizontalFacing());
		body = world.getBlockState(bodyPos);
		frontBody = world.getBlockState(frontBodyPos);
		//		final boolean flag1 = BlockHelper.isBlockInList(world, body, bodyPos, climbList);
		//		final boolean flag2 = BlockHelper.isBlockInList(world, frontBody, frontBodyPos, climbList);
		final boolean whitelist = TrinketsConfig.SERVER.races.fairy.whitelistClimbables;
		for (String s : climbList) {
			ConfigObject object = new ConfigObject(s);
			if (object.doesBlockMatchEntry(body) || object.doesBlockMatchEntry(frontBody))
				return whitelist ? true : false;
		}
		//		if (flag1 || flag2)
		return whitelist ? false : true;
	}

}
