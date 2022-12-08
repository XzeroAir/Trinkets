package xzeroair.trinkets.traits.abilities.other;

import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.races.titan.config.TitanConfig;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;

public class AbilityHeavy extends Ability implements ITickableAbility {

	public static final TitanConfig serverConfig = TrinketsConfig.SERVER.races.titan;

	public AbilityHeavy() {
		super(Abilities.heavy);
	}

	@Override
	public void tickAbility(EntityLivingBase entity) {
		final boolean flag = this.isCreativePlayer(entity);// (entity instanceof EntityPlayer) && (((EntityPlayer) entity).isCreative() || ((EntityPlayer) entity).isSpectator());
		if (!flag) {
			this.trample(entity);
			if (serverConfig.sink && !TrinketHelper.AccessoryCheck(entity, ModItems.trinkets.TrinketSea)) {
				if (entity.isRiding()) {
					if (entity.getRidingEntity() instanceof EntityBoat) {
						final EntityBoat boat = (EntityBoat) entity.getRidingEntity();
						boat.motionY -= 0.02F;
					}
				} else if (entity.isInWater()) {
					if (entity instanceof EntityPlayer) {
						entity.motionY -= 0.2f;
					} else {
						entity.motionY -= 0.1f;
					}
				}
			}
		}
	}

	private void trample(EntityLivingBase entity) {
		if (serverConfig.trample && !entity.isSneaking()) {
			final AxisAlignedBB aabb = entity.getEntityBoundingBox().grow(1, 0, 1);
			final int i = MathHelper.floor(aabb.minX);
			final int j = MathHelper.floor(aabb.maxX + 1.0D);
			final int i1 = MathHelper.floor(aabb.minZ);
			final int j1 = MathHelper.floor(aabb.maxZ + 1.0D);
			for (int k1 = i; k1 < j; ++k1) {
				for (int i2 = i1; i2 < j1; ++i2) {
					final BlockPos pos = new BlockPos(k1, entity.getPosition().getY() - 1, i2);
					final IBlockState block = entity.world.getBlockState(pos);
					if (block.getBlock() == Blocks.FARMLAND) {
						entity.world.setBlockState(pos, Blocks.DIRT.getDefaultState());
					}
					final IBlockState block2 = entity.world.getBlockState(pos.add(0, 1, 0));
					if ((block2.getBlock() instanceof BlockBush) || (block2.getBlock() instanceof BlockDeadBush)) {
						if (block2.getBlock() instanceof BlockDoublePlant) {
							entity.world.destroyBlock(pos.add(0, 2, 0), true);
						} else {
							entity.world.destroyBlock(pos.add(0, 1, 0), true);
						}
					}
				}
			}
		}
	}

}
