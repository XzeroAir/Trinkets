package xzeroair.trinkets.traits.abilities;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.races.dwarf.config.DwarfConfig;
import xzeroair.trinkets.traits.abilities.interfaces.IMiningAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ConfigHelper.ConfigObject;
import xzeroair.trinkets.util.helpers.BlockHelperUtil;

public class AbilitySkilledMiner extends Ability implements IMiningAbility {

	public static final DwarfConfig serverConfig = TrinketsConfig.SERVER.races.dwarf;

	public AbilitySkilledMiner() {
		super(Abilities.skilledMiner);
	}

	@Override
	public float breakingBlock(EntityLivingBase entity, IBlockState state, BlockPos pos, float originalSpeed, float newSpeed) {
		if (serverConfig.static_mining) {
			final ItemStack heldItemStack = entity.getHeldItemMainhand();
			final Item heldItem = heldItemStack.getItem();
			final int toolLevel = heldItem.getHarvestLevel(heldItemStack, "pickaxe", null, state);
			final int level = state.getBlock().getHarvestLevel(state);
			final float hardness = state.getBlockHardness(entity.world, pos);
			if (!heldItem.getToolClasses(heldItemStack).isEmpty() && heldItem.getToolClasses(heldItemStack).contains("pickaxe")) {
				if ((toolLevel >= (level)) || (toolLevel == (level - 1))) {
					newSpeed = hardness * 5F;
				}
			}
		}
		return newSpeed;
	}

	@Override
	public int brokeBlock(EntityLivingBase entity, World world, IBlockState state, BlockPos pos, int expToDrop) {
		final boolean isClient = world.isRemote;
		final ItemStack heldItemStack = entity.getHeldItemMainhand();
		final Block block = state.getBlock();
		if (!heldItemStack.isEmpty() && !block.getHarvestTool(state).contentEquals("pickaxe")) {
			return expToDrop;
		}
		final ItemStack toolUsed = heldItemStack.copy();

		final int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, heldItemStack);
		final boolean silkTouching = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, toolUsed) > 0;
		try {
			if (serverConfig.fortune && !silkTouching) {
				final Enchantment fortune = Enchantments.FORTUNE;
				final int fortuneMaxLevel = fortune.getMaxLevel();
				if (fortuneLevel > 0) {
					if (serverConfig.fortune_mix) {
						NBTTagList nbttaglist = toolUsed.getEnchantmentTagList();

						for (int i = 0; i < nbttaglist.tagCount(); ++i) {
							NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
							Enchantment enchantment = Enchantment.getEnchantmentByID(nbttagcompound.getShort("id"));
							if (enchantment == fortune) {
								nbttagcompound.setShort("lvl", (short) ((byte) fortuneLevel + fortuneMaxLevel));
							}
						}
					}
				} else {
					toolUsed.addEnchantment(fortune, fortuneMaxLevel);
				}
			}
		} catch (Exception e) {
		}
		final Item toolItem = toolUsed.getItem();
		if (!toolItem.getToolClasses(toolUsed).isEmpty() && toolItem.getToolClasses(toolUsed).contains("pickaxe")) {
			int tempExp = 0;
			if (!silkTouching && !isClient) {
				if (serverConfig.BLOCKS.bonus_exp) {
					for (String s : serverConfig.BLOCKS.xPBlocks) {
						ConfigObject object = new ConfigObject(s);
						if (object.doesBlockMatchEntry(state)) {
							final int bonusExp = serverConfig.BLOCKS.bonus_exp_max;
							final int min = serverConfig.BLOCKS.bonus_exp_min;
							final int rXP = bonusExp < 1 ? min : random.nextInt(bonusExp);
							tempExp += Math.max(min, rXP);
							break;
						}
					}
				}
				if (serverConfig.BLOCKS.minXpBlocks) {
					if (tempExp < 1) {
						for (String s : serverConfig.BLOCKS.MinBlocks) {
							ConfigObject object = new ConfigObject(s);
							if (object.doesBlockMatchEntry(state)) {
								tempExp = 1;
								break;
							}
						}
					}
				}
			}
			final int droppedExp = tempExp;
			if (serverConfig.skilled_miner && (entity instanceof EntityPlayer)) {
				if (BlockHelperUtil.canBreakBlock(toolUsed, world, (EntityPlayer) entity, pos, pos, 1)) {
					BlockHelperUtil.breakBlock(
							(EntityPlayer) entity,
							toolUsed,
							world,
							state,
							pos,
							pos,
							false,
							1,
							xp -> {
								if (xp < -1) {
									return droppedExp;
								}
								return xp + droppedExp;
							}
					);
					return 0;
				}
			}
			return expToDrop + droppedExp;
		}
		return expToDrop;
	}
}
