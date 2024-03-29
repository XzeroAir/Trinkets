package xzeroair.trinkets.traits.abilities.interfaces;

import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xzeroair.trinkets.traits.abilities.IAbilityHandler;

public interface IMiningAbility extends IAbilityHandler {

	// can cancel and can set if harvest so need 2 methods or 2 returns
	// is near worthless event, happens when you attempt to break a block
	//	default void canHarvest(EntityLivingBase entity, IBlockState targetBlock, boolean canHarvest) {
	//
	//	}

	// can cancel and can set mining speed
	// Happens when breaking a block
	default ImmutablePair<Boolean, Float> breakingBlock(EntityLivingBase entity, IBlockState state, BlockPos pos, float originalSpeed, float newSpeed) {
		return null;
	}

	// can cancel and can set exp to drop
	// Triggered only when the block is broken
	default ImmutablePair<Boolean, Integer> brokeBlock(EntityLivingBase entity, World world, IBlockState state, BlockPos pos, int expToDrop) {
		return null;
	}

	// can not cancel, can set drop chance
	// happens after the block is broken
	default float blockDrops(EntityLivingBase entity, World world, IBlockState state, BlockPos pos, List<ItemStack> drops, float dropChance, boolean silkTouching, int fortuneLevel) {
		return dropChance;
	}
}
