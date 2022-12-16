<<<<<<< Updated upstream
package xzeroair.trinkets.traits.abilities.other;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.races.titan.config.TitanConfig;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IMiningAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ConfigHelper.ConfigObject;
import xzeroair.trinkets.util.helpers.BlockHelperUtil;

public class AbilityLargeHands extends Ability implements IAttackAbility, IMiningAbility {

	public static final TitanConfig serverConfig = TrinketsConfig.SERVER.races.titan;

	public AbilityLargeHands() {
		super(Abilities.largeHands);
	}

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
	public float breakingBlock(EntityLivingBase entity, IBlockState state, BlockPos pos, float originalSpeed, float newSpeed) {
		final World world = entity.getEntityWorld();
		final ItemStack heldItemStack = entity instanceof EntityPlayer ? ((EntityPlayer) entity).inventory.getCurrentItem() : entity.getActiveItemStack();
		if (heldItemStack.isEmpty()) {
			final Block block = state.getBlock();
			final String neededTool = block.getHarvestTool(state);
			final ItemStack toolUsed = this.getHarvestTool(neededTool, heldItemStack);
			final float newDigSpeed = BlockHelperUtil.getEntityDigSpeed(entity, toolUsed, state, pos, false);
			final float hardness = state.getBlockHardness(world, pos);
			if ((hardness > 0F) && (entity instanceof EntityPlayer)) {
				if (!ForgeHooks.canHarvestBlock(block, (EntityPlayer) entity, world, pos)) {
					return (newDigSpeed * 2F);
				}
			}
			return newDigSpeed;
		}
		return newSpeed;
	}

	@Override
	public int brokeBlock(EntityLivingBase entity, World world, IBlockState state, BlockPos pos, int expToDrop) {
		boolean flag = serverConfig.miningExtendInverted ? !entity.isSneaking() : entity.isSneaking();
		if (!serverConfig.miningExtend || flag || (expToDrop < 0)) {
			return expToDrop;
		}
		for (final String s : serverConfig.miningAoEBlacklist) {
			ConfigObject object = new ConfigObject(s);
			if (object.doesBlockMatchEntry(state)) {
				return expToDrop;
			}
		}
		final ItemStack heldItemStack = entity instanceof EntityPlayer ? ((EntityPlayer) entity).inventory.getCurrentItem() : entity.getActiveItemStack();
		final Block block = state.getBlock();
		final String neededTool = block.getHarvestTool(state);
		final ItemStack toolUsed = this.getHarvestTool(neededTool, heldItemStack);
		if (BlockHelperUtil.canToolHarvestBlock(toolUsed, state)) {
			if (BlockHelperUtil.isToolEffective(toolUsed, state)) {
				final ImmutableList<BlockPos> list = BlockHelperUtil.getBlockList(toolUsed, world, (EntityPlayer) entity, pos, 3, 3, 3, checkPos -> {
					boolean skip = false;
					for (final String s : serverConfig.miningAoEBlacklist) {
						ConfigObject object = new ConfigObject(s);
						if (object.doesBlockMatchEntry(world.getBlockState(pos))) {
							skip = true;
						}
					}
					return !skip;
				});
				for (BlockPos ePos : list) {
					BlockHelperUtil.breakBlock((EntityPlayer) entity, toolUsed, world, state, pos, ePos, true);
				}
				BlockHelperUtil.breakBlock((EntityPlayer) entity, toolUsed, world, state, pos, pos, false);
				return 0;
			}
		}
		return expToDrop;
	}

	public ItemStack getHarvestTool(String needed, ItemStack heldTool) {
		if (needed == null) {
			return heldTool.copy();
		}
		if (heldTool.isEmpty()) {
			switch (needed) {
			case "pickaxe":
				return new ItemStack(Items.WOODEN_PICKAXE);
			case "shovel":
				return new ItemStack(Items.WOODEN_SHOVEL);
			case "axe":
				return new ItemStack(Items.WOODEN_AXE);
			default:
				return heldTool.copy();
			}
		} else {
			return heldTool.copy();
		}
	}

}
=======
package xzeroair.trinkets.traits.abilities.other;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.races.titan.config.TitanConfig;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IMiningAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ConfigHelper.ConfigObject;
import xzeroair.trinkets.util.helpers.BlockHelperUtil;

public class AbilityLargeHands extends Ability implements IAttackAbility, IMiningAbility {

	public static final TitanConfig serverConfig = TrinketsConfig.SERVER.races.titan;

	public AbilityLargeHands() {
		super(Abilities.largeHands);
	}

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
	public float breakingBlock(EntityLivingBase entity, IBlockState state, BlockPos pos, float originalSpeed, float newSpeed) {
		final World world = entity.getEntityWorld();
		final ItemStack heldItemStack = entity instanceof EntityPlayer ? ((EntityPlayer) entity).inventory.getCurrentItem() : entity.getActiveItemStack();
		if (heldItemStack.isEmpty()) {
			final Block block = state.getBlock();
			final String neededTool = block.getHarvestTool(state);
			final ItemStack toolUsed = this.getHarvestTool(neededTool, heldItemStack);
			final float newDigSpeed = BlockHelperUtil.getEntityDigSpeed(entity, toolUsed, state, pos, false);
			final float hardness = state.getBlockHardness(world, pos);
			if ((hardness > 0F) && (entity instanceof EntityPlayer)) {
				if (!ForgeHooks.canHarvestBlock(block, (EntityPlayer) entity, world, pos)) {
					return (newDigSpeed * 2F);
				}
			}
			return newDigSpeed;
		}
		return newSpeed;
	}

	@Override
	public int brokeBlock(EntityLivingBase entity, World world, IBlockState state, BlockPos pos, int expToDrop) {
		boolean flag = serverConfig.miningExtendInverted ? !entity.isSneaking() : entity.isSneaking();
		if (!serverConfig.miningExtend || flag || (expToDrop < 0)) {
			return expToDrop;
		}
		for (final String s : serverConfig.miningAoEBlacklist) {
			ConfigObject object = new ConfigObject(s);
			if (object.doesBlockMatchEntry(state)) {
				return expToDrop;
			}
		}
		final ItemStack heldItemStack = entity instanceof EntityPlayer ? ((EntityPlayer) entity).inventory.getCurrentItem() : entity.getActiveItemStack();
		final Block block = state.getBlock();
		final String neededTool = block.getHarvestTool(state);
		final ItemStack toolUsed = this.getHarvestTool(neededTool, heldItemStack);
		if (BlockHelperUtil.canToolHarvestBlock(toolUsed, state)) {
			if (BlockHelperUtil.isToolEffective(toolUsed, state)) {
				final ImmutableList<BlockPos> list = BlockHelperUtil.getBlockList(toolUsed, world, (EntityPlayer) entity, pos, 3, 3, 3, checkPos -> {
					boolean skip = false;
					for (final String s : serverConfig.miningAoEBlacklist) {
						ConfigObject object = new ConfigObject(s);
						if (object.doesBlockMatchEntry(world.getBlockState(pos))) {
							skip = true;
						}
					}
					return !skip;
				});
				for (BlockPos ePos : list) {
					BlockHelperUtil.breakBlock((EntityPlayer) entity, toolUsed, world, state, pos, ePos, true);
				}
				BlockHelperUtil.breakBlock((EntityPlayer) entity, toolUsed, world, state, pos, pos, false);
				return 0;
			}
		}
		return expToDrop;
	}

	public ItemStack getHarvestTool(String needed, ItemStack heldTool) {
		if (needed == null) {
			return heldTool.copy();
		}
		if (heldTool.isEmpty()) {
			switch (needed) {
			case "pickaxe":
				return new ItemStack(Items.WOODEN_PICKAXE);
			case "shovel":
				return new ItemStack(Items.WOODEN_SHOVEL);
			case "axe":
				return new ItemStack(Items.WOODEN_AXE);
			default:
				return heldTool.copy();
			}
		} else {
			return heldTool.copy();
		}
	}

}
>>>>>>> Stashed changes
