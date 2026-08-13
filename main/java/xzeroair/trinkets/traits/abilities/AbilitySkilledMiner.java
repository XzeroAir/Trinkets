package xzeroair.trinkets.traits.abilities;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.traits.abilities.interfaces.IMiningAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.ConfigHelper.ConfigObject;
import xzeroair.trinkets.util.config.abilities.ConfigAbilitySkilledMiner;
import xzeroair.trinkets.util.helpers.BlockHelperUtil;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class AbilitySkilledMiner extends Ability implements IMiningAbility {

    private static final String PICKAXE_TOOL_CLASS = "pickaxe";
    private static final float STATIC_MINING_TARGET_MULTIPLIER = 5F;
    private static final float STATIC_MINING_MAX_SAFE_MULTIPLIER = 20F;

    protected final ConfigAbilitySkilledMiner CONFIG;

    public AbilitySkilledMiner() {
        this(TrinketsConfig.SERVER.ABILITIES.SKILLED_MINER);
    }

    public AbilitySkilledMiner(ConfigAbilitySkilledMiner config) {
        super(TrinketsRegistryNames.ModAbilities.SKILLED_MINER);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        final boolean naturalFortune = this.CONFIG.fortune;
        final TranslationHelper.OptionEntry fortuneEntry = new TranslationHelper.OptionEntry("fortune", naturalFortune, "");
        return helper.formatAddVariables(key, renderID, fortuneEntry);
    }

    @Override
    public float breakingBlock(EntityLivingBase entity, IBlockState state, BlockPos pos, float originalSpeed, float newSpeed) {
        final boolean staticMining = this.CONFIG.static_mining;
        if (!staticMining) {
            return newSpeed;
        }

        final ItemStack heldItemStack = entity.getHeldItemMainhand();
        if (!this.canUsePickaxeOn(heldItemStack, state, 1)) {
            return newSpeed;
        }

        final float hardness = state.getBlockHardness(entity.world, pos);
        if (hardness <= 0F) {
            return newSpeed;
        }

        final float targetSpeed = hardness * STATIC_MINING_TARGET_MULTIPLIER;
        final float maxSafeSpeed = hardness * STATIC_MINING_MAX_SAFE_MULTIPLIER;
        return Math.min(Math.max(newSpeed, targetSpeed), maxSafeSpeed);
    }

    @Override
    public int brokeBlock(EntityLivingBase entity, World world, IBlockState state, BlockPos pos, int expToDrop) {
        final ItemStack heldItemStack = entity.getHeldItemMainhand();
        final Block block = state.getBlock();
        final String harvestTool = block.getHarvestTool(state);
        if (!heldItemStack.isEmpty() && !PICKAXE_TOOL_CLASS.equals(harvestTool)) {
            return expToDrop;
        }

        final ItemStack toolUsed = heldItemStack.copy();
        if (!this.isPickaxe(toolUsed)) {
            return expToDrop;
        }

        final int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, heldItemStack);
        final boolean silkTouching = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, toolUsed) > 0;
        this.applyNaturalFortune(toolUsed, state, fortuneLevel, silkTouching);

        final int droppedExp = this.getAdditionalMiningXp(world, state, silkTouching);
        final boolean reducedRequirement = this.CONFIG.skilled_miner;
        if (reducedRequirement && (entity instanceof EntityPlayer) && BlockHelperUtil.canBreakBlock(toolUsed, world, (EntityPlayer) entity, pos, pos, 1)) {
            BlockHelperUtil.breakBlock((EntityPlayer) entity, toolUsed, world, state, pos, pos, false, 1, xp -> {
                if (xp < -1) {
                    return droppedExp;
                }
                return xp + droppedExp;
            });
            return 0;
        }
        return expToDrop + droppedExp;
    }

    /*
     * Future Natural Fortune HarvestDropsEvent path.
     * This was compiled successfully, but is intentionally disabled until the drop/config structure is ready.
     * @Override
     * public float blockDrops(EntityLivingBase entity, World world, IBlockState state, BlockPos pos, java.util.List<ItemStack> drops, float dropChance, boolean silkTouching, int fortuneLevel) {
     *     if (world.isRemote || !this.canApplyNaturalFortune(state, silkTouching) || !(entity instanceof EntityPlayer)) {
     *         return dropChance;
     *     }
     *
     *     final ItemStack heldItemStack = entity.getHeldItemMainhand();
     *     if (!this.isPickaxe(heldItemStack)) {
     *         return dropChance;
     *     }
     *
     *     final int naturalFortuneLevel = this.getNaturalFortuneLevel(fortuneLevel);
     *     if (naturalFortuneLevel <= fortuneLevel) {
     *         return dropChance;
     *     }
     *
     *     final net.minecraft.util.NonNullList<ItemStack> normalDrops = net.minecraft.util.NonNullList.create();
     *     final net.minecraft.util.NonNullList<ItemStack> naturalFortuneDrops = net.minecraft.util.NonNullList.create();
     *     state.getBlock().getDrops(normalDrops, world, pos, state, fortuneLevel);
     *     state.getBlock().getDrops(naturalFortuneDrops, world, pos, state, naturalFortuneLevel);
     *     this.addAdditionalFortuneDrops(drops, normalDrops, naturalFortuneDrops);
     *     return dropChance;
     * }
     */

    private void applyNaturalFortune(ItemStack toolUsed, IBlockState state, int fortuneLevel, boolean silkTouching) {
        if (!this.canApplyNaturalFortune(state, silkTouching)) {
            return;
        }

        final Enchantment fortune = Enchantments.FORTUNE;
        final int naturalFortuneLevel = this.getNaturalFortuneLevel(fortuneLevel);
        if (fortuneLevel > 0) {
            if (naturalFortuneLevel > fortuneLevel) {
                NBTTagList enchantments = toolUsed.getEnchantmentTagList();
                for (int i = 0; i < enchantments.tagCount(); ++i) {
                    NBTTagCompound enchantmentTag = enchantments.getCompoundTagAt(i);
                    Enchantment enchantment = Enchantment.getEnchantmentByID(enchantmentTag.getShort("id"));
                    if (enchantment == fortune) {
                        enchantmentTag.setShort("lvl", (short) naturalFortuneLevel);
                    }
                }
            }
        } else {
            toolUsed.addEnchantment(fortune, naturalFortuneLevel);
        }
    }

    private boolean canApplyNaturalFortune(IBlockState state, boolean silkTouching) {
        return this.CONFIG.fortune && !silkTouching && this.blockMatchesAny(state, this.CONFIG.BLOCKS.Blocks);
    }

    private int getNaturalFortuneLevel(int fortuneLevel) {
        final int naturalFortuneLevel = Enchantments.FORTUNE.getMaxLevel();
        if (fortuneLevel <= 0) {
            return naturalFortuneLevel;
        }
        if (!this.CONFIG.fortune_mix) {
            return fortuneLevel;
        }
        return fortuneLevel + naturalFortuneLevel;
    }

    private int getAdditionalMiningXp(World world, IBlockState state, boolean silkTouching) {
        if (world.isRemote || silkTouching) {
            return 0;
        }

        int droppedExp = 0;
        if (this.CONFIG.BLOCKS.bonus_exp && this.blockMatchesAny(state, this.CONFIG.BLOCKS.xPBlocks)) {
            droppedExp += this.rollMiningXp();
        }
        if (this.CONFIG.BLOCKS.minXpBlocks && droppedExp < 1 && this.blockMatchesAny(state, this.CONFIG.BLOCKS.MinBlocks)) {
            droppedExp = 1;
        }
        return droppedExp;
    }

    private boolean canUsePickaxeOn(ItemStack stack, IBlockState state, int bonusToolLevel) {
        if (!this.isPickaxe(stack)) {
            return false;
        }

        final int toolLevel = stack.getItem().getHarvestLevel(stack, PICKAXE_TOOL_CLASS, null, state) + bonusToolLevel;
        final int blockHarvestLevel = state.getBlock().getHarvestLevel(state);
        return toolLevel >= blockHarvestLevel;
    }

    private boolean isPickaxe(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem().getToolClasses(stack).contains(PICKAXE_TOOL_CLASS);
    }

    private boolean blockMatchesAny(IBlockState state, String[] entries) {
        for (String entry : entries) {
            ConfigObject object = new ConfigObject(entry);
            if (object.doesBlockMatchEntry(state)) {
                return true;
            }
        }
        return false;
    }

    /*
     * Future Natural Fortune drop-diff helpers.
     * This was compiled successfully, but is intentionally disabled until the drop/config structure is ready.
     * private void addAdditionalFortuneDrops(java.util.List<ItemStack> drops, java.util.List<ItemStack> normalDrops, java.util.List<ItemStack> naturalFortuneDrops) {
     *     for (ItemStack naturalFortuneDrop : naturalFortuneDrops) {
     *         final int normalCount = this.countMatchingDrops(normalDrops, naturalFortuneDrop);
     *         final int existingCount = this.countMatchingDrops(drops, naturalFortuneDrop);
     *         final int additionalCount = naturalFortuneDrop.getCount() - Math.max(normalCount, existingCount);
     *         if (additionalCount > 0) {
     *             ItemStack additionalDrop = naturalFortuneDrop.copy();
     *             additionalDrop.setCount(additionalCount);
     *             drops.add(additionalDrop);
     *         }
     *     }
     * }
     *
     * private int countMatchingDrops(java.util.List<ItemStack> drops, ItemStack target) {
     *     int count = 0;
     *     for (ItemStack drop : drops) {
     *         if (this.areSameDrop(drop, target)) {
     *             count += drop.getCount();
     *         }
     *     }
     *     return count;
     * }
     *
     * private boolean areSameDrop(ItemStack first, ItemStack second) {
     *     return ItemStack.areItemsEqual(first, second) && ItemStack.areItemStackTagsEqual(first, second);
     * }
     */

    private int rollMiningXp() {
        final int miningXpChanceBound = this.CONFIG.BLOCKS.bonus_exp_max;
        final int miningXpMinimum = this.CONFIG.BLOCKS.bonus_exp_min;
        final int rolledXp = miningXpChanceBound < 1 ? miningXpMinimum : this.random.nextInt(miningXpChanceBound);
        return Math.max(miningXpMinimum, rolledXp);
    }
}
