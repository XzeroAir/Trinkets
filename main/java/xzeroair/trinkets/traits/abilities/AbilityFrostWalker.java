package xzeroair.trinkets.traits.abilities;

import com.google.common.base.Objects;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentFrostWalker;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;

public class AbilityFrostWalker extends Ability implements ITickableAbility, IAttackAbility {

	public AbilityFrostWalker() {
		super(Abilities.frostWalker);
	}

	@Override
	public boolean attacked(EntityLivingBase attacked, DamageSource source, float dmg, boolean cancel) {
		if (source.damageType.contentEquals("hotFloor")) {
			return true;
		}
		return cancel;
	}

	@Override
	public void tickAbility(EntityLivingBase entity) {
		final World world = entity.getEntityWorld();
		if (!world.isRemote && !this.isSpectator(entity)) {
			if (EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.DEPTH_STRIDER, entity) > 0) {
				return;
			}
			final BlockPos prev = Capabilities.getEntityProperties(entity, entity.getPosition(), (prop, pos) -> prop.getPrevBlockpos());
			if (!Objects.equal(prev, entity.getPosition())) {
				int lvl = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FROST_WALKER, entity);
				EnchantmentFrostWalker.freezeNearby(entity, world, entity.getPosition(), lvl + 1);
				//			freezeNearby(entity, world, entity.getPosition(), lvl + 1);
			}
		}
	}

	public static void freezeNearby(EntityLivingBase living, World world, BlockPos pos, int level) {
		if (living.onGround) {
			float f = Math.min(16, 2 + level);
			BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(0, 0, 0);

			for (BlockPos.MutableBlockPos blockpos$mutableblockpos1 : BlockPos.getAllInBoxMutable(pos.add((-f), -1.0D, (-f)), pos.add(f, -1.0D, f))) {
				if (blockpos$mutableblockpos1.distanceSqToCenter(living.posX, living.posY, living.posZ) <= (f * f)) {
					blockpos$mutableblockpos.setPos(blockpos$mutableblockpos1.getX(), blockpos$mutableblockpos1.getY() + 1, blockpos$mutableblockpos1.getZ());
					IBlockState iblockstate = world.getBlockState(blockpos$mutableblockpos);

					if (iblockstate.getMaterial() == Material.AIR) {
						IBlockState iblockstate1 = world.getBlockState(blockpos$mutableblockpos1);

						if ((iblockstate1.getMaterial() == Material.WATER) && ((iblockstate1.getBlock() == net.minecraft.init.Blocks.WATER) || (iblockstate1.getBlock() == net.minecraft.init.Blocks.FLOWING_WATER)) && (iblockstate1.getValue(BlockLiquid.LEVEL).intValue() == 0) && world.mayPlace(Blocks.FROSTED_ICE, blockpos$mutableblockpos1, false, EnumFacing.DOWN, (Entity) null)) {
							world.setBlockState(blockpos$mutableblockpos1, Blocks.FROSTED_ICE.getDefaultState());
							world.scheduleUpdate(blockpos$mutableblockpos1.toImmutable(), Blocks.FROSTED_ICE, MathHelper.getInt(living.getRNG(), 60, 120));
						}
					}
				}
			}
		}
	}

}
