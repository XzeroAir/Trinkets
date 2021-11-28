package xzeroair.trinkets.traits.abilities;

import org.apache.commons.lang3.tuple.ImmutablePair;

import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import xzeroair.trinkets.races.dwarf.config.DwarfConfig;
import xzeroair.trinkets.traits.abilities.base.AbilityBase;
import xzeroair.trinkets.traits.abilities.interfaces.IMiningAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.BlockHelper;

public class AbilitySkilledMiner extends AbilityBase implements IMiningAbility {

	public static final DwarfConfig serverConfig = TrinketsConfig.SERVER.races.dwarf;

	@Override
	public ImmutablePair<Boolean, Float> breakingBlock(EntityLivingBase entity, IBlockState state, BlockPos pos, float originalSpeed, float newSpeed) {
		if (serverConfig.static_mining) {
			final ItemStack heldItemStack = entity.getHeldItemMainhand();
			final Item heldItem = heldItemStack.getItem();
			final int toolLevel = heldItem.getHarvestLevel(heldItemStack, "pickaxe", null, state);
			final int level = state.getBlock().getHarvestLevel(state);
			final float hardness = state.getBlockHardness(entity.world, pos);
			if (!heldItem.getToolClasses(heldItemStack).isEmpty() && heldItem.getToolClasses(heldItemStack).contains("pickaxe")) {
				if ((toolLevel >= (level)) || (toolLevel == (level - 1))) {
					newSpeed = hardness * 4F;
				}
			}
		}
		return new ImmutablePair<>(false, newSpeed);
	}

	@Override
	public ImmutablePair<Boolean, Integer> brokeBlock(EntityLivingBase entity, World world, IBlockState state, BlockPos pos, int expToDrop) {
		final ItemStack heldItemStack = entity.getHeldItemMainhand();
		final Item heldItem = heldItemStack.getItem();
		final Block block = state.getBlock();
		final int blockLevel = block.getHarvestLevel(state);
		final int toolLevel = heldItem.getHarvestLevel(heldItemStack, "pickaxe", null, state);
		final boolean silkTouching = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, heldItemStack) > 0;
		if (!heldItem.getToolClasses(heldItemStack).isEmpty() && heldItem.getToolClasses(heldItemStack).contains("pickaxe")) {
			if (!silkTouching && serverConfig.BLOCKS.bonus_exp) {
				final int bonusExp = serverConfig.BLOCKS.bonus_exp_max;
				final int min = serverConfig.BLOCKS.bonus_exp_min;
				final int rXP = random.nextInt(bonusExp);
				final int xp = MathHelper.clamp(rXP, min, rXP + min);
				final int exp = state.getBlock().getExpDrop(state, entity.world, pos, EnchantmentHelper.getLootingModifier(entity));
				if (BlockHelper.isBlockInList(world, state, pos, serverConfig.BLOCKS.xPBlocks)) {
					expToDrop = exp + xp;
				}
				if (serverConfig.BLOCKS.minXpBlocks) {
					if (BlockHelper.isBlockInList(world, state, pos, serverConfig.BLOCKS.MinBlocks)) {
						expToDrop = exp + 1;
					}
				}
			}
			if ((toolLevel == (blockLevel - 1)) && serverConfig.skilled_miner) {
				final TileEntity tile = world.getTileEntity(pos);
				if (((block instanceof BlockOre) || (block instanceof BlockRedstoneOre) || (block instanceof BlockQuartz) || (block instanceof BlockObsidian))) {
					block.onPlayerDestroy(world, pos, state);
					block.harvestBlock(world, (EntityPlayer) entity, pos, state, tile, heldItemStack);
					block.dropXpOnBlockBreak(world, pos, expToDrop);
					expToDrop = 0;
				}
			}
		}
		return new ImmutablePair<>(false, expToDrop);
	}
}
