package xzeroair.trinkets.traits.abilities.other;

import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import xzeroair.trinkets.races.titan.config.TitanConfig;
import xzeroair.trinkets.traits.abilities.base.AbilityBase;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IMiningAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ConfigHelper;
import xzeroair.trinkets.util.config.ConfigHelper.ItemObjectHolder;

public class AbilityLargeHands extends AbilityBase implements IAttackAbility, IMiningAbility {

	public static final TitanConfig serverConfig = TrinketsConfig.SERVER.races.titan;

	@Override
	public float hurtEntity(EntityLivingBase target, DamageSource source, float dmg) {
		if (this.isIndirectDamage(source)) {
			return dmg;
		}
		final Entity attacker = source.getTrueSource();
		final AxisAlignedBB bb = target.getEntityBoundingBox().grow(1);
		final Predicate<Entity> Targets = Predicates.and(
				EntitySelectors.NOT_SPECTATING,
				ent -> (ent != null) && ent.canBeCollidedWith() && (ent != attacker) && (ent != target) && (ent.getEntityId() != target.getEntityId())
		);
		final List<Entity> splash = target.getEntityWorld().getEntitiesWithinAABB(Entity.class, bb, Targets);
		DamageSource sSource = DamageSource.GENERIC;
		if (attacker instanceof EntityLivingBase) {
			if (attacker instanceof EntityPlayer) {
				sSource = new EntityDamageSourceIndirect("player", attacker, attacker);
			} else {
				sSource = DamageSource.causeIndirectDamage(attacker, (EntityLivingBase) attacker);
			}
		}
		for (final Entity hit : splash) {
			hit.attackEntityFrom(sSource, dmg * 0.25F);
		}
		return dmg;
	}

	@Override
	public ImmutablePair<Boolean, Integer> brokeBlock(EntityLivingBase entity, World world, IBlockState state, BlockPos pos, int expToDrop) {
		if (!(entity instanceof EntityPlayer) || this.isCreativePlayer(entity)) {
			return null;
		}
		if (!serverConfig.miningExtend || ((serverConfig.miningExtendInverted && !entity.isSneaking()) || (!serverConfig.miningExtendInverted && entity.isSneaking()))) {
			return null;
		}
		for (final String s : serverConfig.miningAoEBlacklist) {
			final ItemObjectHolder object = ConfigHelper.parseConfigItem(s);
			if (object != null) {
				if (ConfigHelper.checkBlockInConfig(state, s)) {
					return null;
				}
			}
		}
		final BlockPos pos1 = pos.add(-1, -1, -1);
		final BlockPos pos2 = pos.add(1, 1, 1);
		final ItemStack heldItemStack = entity instanceof EntityPlayer ? ((EntityPlayer) entity).inventory.getCurrentItem() : entity.getActiveItemStack();

		final Iterable<BlockPos> blockList = BlockPos.getAllInBox(pos1, pos2);
		blockList.forEach(block -> {
			this.shouldHarvestBlock((EntityPlayer) entity, heldItemStack, world, state, pos, block);
		});
		return new ImmutablePair<>(false, expToDrop);
	}

	public void shouldHarvestBlock(EntityPlayer entity, ItemStack harvestTool, World world, IBlockState harvestedBlockState, BlockPos harvestedPos, BlockPos targetPos) {
		final IBlockState targetBlockState = world.getBlockState(targetPos);
		//				boolean isEffective = false;
		//				for (final String type : harvestTool.getItem().getToolClasses(harvestTool)) {
		//					if (targetBlockState.getBlock().isToolEffective(type, targetBlockState)) {
		//						isEffective = true;
		//						break;
		//					}
		//				}
		if (harvestedPos.equals(targetPos) || (world.getTileEntity(targetPos) != null) || world.isAirBlock(targetPos)) {
			return;
		}
		final boolean redstone1 = ((targetBlockState.getBlock() == Blocks.REDSTONE_ORE) || (targetBlockState.getBlock() == Blocks.LIT_REDSTONE_ORE));
		final boolean redstone2 = ((harvestedBlockState.getBlock() == Blocks.LIT_REDSTONE_ORE) || (harvestedBlockState.getBlock() == Blocks.REDSTONE_ORE));
		if (targetBlockState.getBlock() != harvestedBlockState.getBlock()) {
			if (redstone2) {
				if (redstone1) {

				} else {
					return;
				}
			} else {
				return;
			}
		}

		if (!redstone2) {
			if ((harvestedBlockState.getBlock().getMetaFromState(harvestedBlockState) != targetBlockState.getBlock().getMetaFromState(targetBlockState))) {
				return;
			}
		}
		//TODO Check this

		//		if (isEffective) {
		//			System.out.println(world.getBlockState(targetPos).getBlock().isWood(world, targetPos));
		//		if (!ForgeHooks.isToolEffective(world, targetPos, harvestTool)) {
		//			return;
		//		}

		if (ForgeHooks.canHarvestBlock(targetBlockState.getBlock(), entity, world, targetPos)) {
			if (targetBlockState.getBlock().removedByPlayer(targetBlockState, world, targetPos, entity, true)) {
				targetBlockState.getBlock().onPlayerDestroy(world, targetPos, targetBlockState);
				targetBlockState.getBlock().harvestBlock(world, entity, targetPos, targetBlockState, world.getTileEntity(targetPos), harvestTool);
				//			world.setBlockToAir(targetPos);
			}
		}
		//		}
		//Taken out of Forgehook.canHarvestBlock()
		//        IBlockState state = world.getBlockState(pos);
		//        state = state.getBlock().getActualState(state, world, pos);
		//        if (state.getMaterial().isToolNotRequired())
		//        {
		//            return true;
		//        }
		//
		//        ItemStack stack = player.getHeldItemMainhand();
		//        String tool = block.getHarvestTool(state);
		//        if (stack.isEmpty() || tool == null)
		//        {
		//            return player.canHarvestBlock(state);
		//        }
		//
		//        int toolLevel = stack.getItem().getHarvestLevel(stack, tool, player, state);
		//        if (toolLevel < 0)
		//        {
		//            return player.canHarvestBlock(state);
		//        }
		//
		//        return toolLevel >= block.getHarvestLevel(state);
	}

}
