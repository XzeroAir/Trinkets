package xzeroair.trinkets.traits.abilities.other;

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
import xzeroair.trinkets.enums.ActivationMethod;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IMiningAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.ConfigHelper.ConfigObject;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityLargeHands;
import xzeroair.trinkets.util.helpers.BlockHelperUtil;

import java.util.List;

public class AbilityLargeHands extends Ability implements IAttackAbility, IMiningAbility {

    protected final ConfigAbilityLargeHands CONFIG;

    public AbilityLargeHands() {
        this(TrinketsConfig.SERVER.ABILITIES.LARGE_HANDS);
    }

    public AbilityLargeHands(ConfigAbilityLargeHands config) {
        super(TrinketsRegistryNames.ModAbilities.LARGE_HANDS);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
    }

    @Override
    public float hurtEntity(EntityLivingBase target, DamageSource source, float dmg) {
        if (this.isIndirectDamage(source)) {
            return dmg;
        }
        final Entity attacker = source.getTrueSource();
        final AxisAlignedBB bb = target.getEntityBoundingBox().grow(1);
        final Predicate<Entity> Targets = Predicates.and(EntitySelectors.NOT_SPECTATING, ent -> (ent != null) && ent.canBeCollidedWith() && (ent != attacker) && (ent != target) && (ent.getEntityId() != target.getEntityId()));
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
        if (world.isRemote || !(entity instanceof EntityPlayer)) {
            return expToDrop;
        }

        final EntityPlayer player = (EntityPlayer) entity;
        final ActivationMethod method = CONFIG.MINING_EXTENDED;
        if (method == ActivationMethod.NEVER || expToDrop <= 0) {
            return expToDrop;
        }
        final boolean sneaking = method == ActivationMethod.SNEAK && entity.isSneaking();
        final boolean standing = method == ActivationMethod.STAND && !entity.isSneaking();
        if (!(method == ActivationMethod.ALWAYS || sneaking || standing)) {
            return expToDrop;
        }
        if (this.isBlacklisted(state)) {
            return expToDrop;
        }
        final ItemStack heldItemStack = player.inventory.getCurrentItem();
        final Block block = state.getBlock();
        final String neededTool = block.getHarvestTool(state);
        final ItemStack toolUsed = this.getHarvestTool(neededTool, heldItemStack);
        if (BlockHelperUtil.canToolHarvestBlock(toolUsed, state)) {
            if (BlockHelperUtil.isToolEffective(toolUsed, state)) {
                final ImmutableList<BlockPos> list = BlockHelperUtil.getBlockList(toolUsed, world, player, pos, 3, 3, 3, checkPos -> !this.isBlacklisted(world.getBlockState(checkPos)));
                for (BlockPos ePos : list) {
                    if (BlockHelperUtil.canBreakBlock(toolUsed, world, player, pos, ePos)) {
                        BlockHelperUtil.breakBlock(player, toolUsed, world, state, pos, ePos, true);
                    }
                }
                BlockHelperUtil.breakBlock(player, toolUsed, world, state, pos, pos, false);
                return 0;
            }
        }
        return expToDrop;
    }

    protected boolean isBlacklisted(IBlockState state) {
        for (final String s : CONFIG.MINING_EXTENDED_BLACKLIST) {
            final ConfigObject object = new ConfigObject(s);
            if (object.doesBlockMatchEntry(state)) {
                return true;
            }
        }
        return false;
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
