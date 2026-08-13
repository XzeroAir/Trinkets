package xzeroair.trinkets.items.trinkets;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.blocks.BlockTeddyBear;
import xzeroair.trinkets.blocks.tileentities.TileEntityTeddyBear;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.enums.CustomTeddyBearTypes;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.init.ModBlocks;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityWellRested;
import xzeroair.trinkets.traits.abilities.compat.firstaid.AbilityBlessingFirstAid;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.other.AbilityBlessing;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.firstaid.FirstAidCompat;
import xzeroair.trinkets.util.config.trinkets.ConfigTeddyBear;
import xzeroair.trinkets.util.helpers.TranslationHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TrinketTeddyBear extends AccessoryBase {

    protected final ConfigTeddyBear CONFIG = TrinketsConfig.SERVER.ITEMS.TEDDY_BEAR;

    public TrinketTeddyBear(String name) {
        super(name);
        this.setUUID("33b34669-715d-4caa-a31e-9c643c52ba66");
        this.setHasSubtypes(true);
    }

    @Override
    public void initAbilities(ItemStack stack, EntityLivingBase entity, @Nonnull List<IAbilityInterface> abilities) {
        abilities.add(new AbilityWellRested(this.CONFIG.ABILITIES.WELL_RESTED));
        this.addSurvivalAbilities(stack, entity, abilities, this.getPrimaryElement(stack), this.CONFIG.COMPAT.SURVIVAL);
        if (this.getTeddyVariant(stack) == CustomTeddyBearTypes.SNOWIE.getId()) {
            if (TrinketsConfig.SERVER.MISC.Blessings.length == 0) {
                if (FirstAidCompat.isModEnabled()) {
                    abilities.add(new AbilityBlessingFirstAid());
                } else {
                    abilities.add(new AbilityBlessing());
                }
            }
        }
    }

    @Override
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<String> tooltips, @Nonnull ITooltipFlag flagIn) {
        if (this.getTeddyVariant(stack) == CustomTeddyBearTypes.SNOWIE.getId()) {
            tooltips.add(TranslationHelper.INSTANCE.gold + "In Loving Memory of Stephanie");
            tooltips.add("");
        }
        super.addInformation(stack, world, tooltips, flagIn);
    }

    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        return Capabilities.getTrinketProperties(stack, super.getItemStackDisplayName(stack), (prop, name) -> {
            final String crafter = prop.getCrafter();
            if (!crafter.isEmpty() && !prop.getItemStack().hasDisplayName()) {
                return crafter + "'s " + name;
            }
            return name;
        });
    }

    public int getTeddyVariant(ItemStack stack) {
        if ((stack != null) && !stack.isEmpty()) {
            //			Item item = stack.getItem();
            final String name = stack.getDisplayName().toLowerCase();
            String crafterID = Capabilities.getTrinketProperties(stack, "", (prop, UUID) -> {
                final String ID = prop.getCrafterUUID();
                if (!ID.isEmpty()) {
                    return ID;
                }
                return UUID;
            });
            //			String xzeroair = "f5f28614-4e8b-4788-ae78-b020493dc5cb";
            final boolean hasDisplayName = stack.hasDisplayName();
            //		} else if
            if (name.equalsIgnoreCase("Stephanie's Snowie")) {
                return CustomTeddyBearTypes.SNOWIE.getId();
            } else if ((crafterID.equalsIgnoreCase("b4817e56-db30-4fdb-9c4c-bba3e0378e0a") && !hasDisplayName) || name.contains("rixxi")) {
                return CustomTeddyBearTypes.RIXXI.getId();
            } else if ((crafterID.equalsIgnoreCase("6e6cc84d-6d4d-41a4-ba8b-47d786b00bae") && !hasDisplayName) || name.contains("cowsaysboom")) {
                return CustomTeddyBearTypes.BOOM.getId();
            } else if (name.contains("nyan")) {
                return CustomTeddyBearTypes.NYAN_OLD.getId();
            } else if ((crafterID.equalsIgnoreCase("854adc0b-ae55-48d6-b7ba-e641a1eebf42") && !hasDisplayName)) {
                return CustomTeddyBearTypes.NYAN.getId();
            } else if (name.contains("ken")) {
                return CustomTeddyBearTypes.KEN.getId();
            } else if (name.contains("ryu")) {
                return CustomTeddyBearTypes.RYU.getId();
            } else if ((crafterID.equalsIgnoreCase("b2b629a6-454e-4047-a143-4f357171d639") && !hasDisplayName) || name.contains("twilight")) {
                return CustomTeddyBearTypes.TWILIGHT.getId();
            } else if ((crafterID.equalsIgnoreCase("14bba455-affa-46d0-9cf0-806cc0f3d454") && !hasDisplayName) || name.contains("artsy")) {
                return CustomTeddyBearTypes.ARTSY.getId();
            } else if ((crafterID.equalsIgnoreCase("5a215d65-e57d-47c6-a322-9ede12a4a100") && !hasDisplayName) || name.contains("potastic") || name.contains("panda")) {
                return CustomTeddyBearTypes.PANDA.getId();
            } else if ((crafterID.equalsIgnoreCase("7d50a302-a01c-4e6a-8ea4-03f98662df28") && !hasDisplayName) || name.contains("stingin") || name.contains("bee") || name.contains("bzzz")) {
                return CustomTeddyBearTypes.BEE.getId();
            } else if ((crafterID.equalsIgnoreCase("cdfccefb-1a2e-4fb8-a3b5-041da27fde61") && !hasDisplayName) || name.contains("shivaxi")) {
                return CustomTeddyBearTypes.SHIVAXI.getId();
            } else if ((crafterID.equalsIgnoreCase("6b5d5e9b-1fe8-4c61-a043-1d84ce95765d") && !hasDisplayName) || name.contains("rembo") || name.contains("cool") || name.contains("badass")) {
                return CustomTeddyBearTypes.REMBO.getId();
            } else if (name.contains("scary") || name.contains("freddy") || name.contains("snuggles")) {
                return CustomTeddyBearTypes.SCARY.getId();
            } else {
                return CustomTeddyBearTypes.NORMAL.getId();
            }
        }
        return CustomTeddyBearTypes.NORMAL.getId();
    }

    @Override
    public EnumActionResult onItemUse(@Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (facing == EnumFacing.DOWN) {
            return EnumActionResult.FAIL;
        } else {
            if (world.getBlockState(pos).getBlock().isReplaceable(world, pos)) {
                facing = EnumFacing.UP;
                pos = pos.down();
            }
            IBlockState iblockstate = world.getBlockState(pos);
            Block block = iblockstate.getBlock();
            boolean flag = block.isReplaceable(world, pos);

            if (!flag) {
                if (!world.getBlockState(pos).getMaterial().isSolid() && !world.isSideSolid(pos, facing, true)) {
                    return EnumActionResult.FAIL;
                }
                pos = pos.offset(facing);
            }

            ItemStack itemstack = player.getHeldItem(hand);
            BlockTeddyBear TeddyBlock = ModBlocks.Placeables.getTeddy(this.getTeddyVariant(itemstack));
            if (player.canPlayerEdit(pos, facing, itemstack) && TeddyBlock.canPlaceBlockAt(world, pos)) {
                if (world.isRemote) {
                    return EnumActionResult.SUCCESS;
                } else {
                    IBlockState iblockstate1 = TeddyBlock.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, 0, player, hand);
                    if (this.placeBlockAt(itemstack, player, world, pos, facing, hitX, hitY, hitZ, iblockstate1)) {
                        int i = 0;
                        if (facing == EnumFacing.UP) {
                            i = MathHelper.floor(((player.rotationYaw * 16.0F) / 360.0F) + 0.5D) & 15;
                        }
                        TileEntity tileentity = world.getTileEntity(pos);
                        if (tileentity instanceof TileEntityTeddyBear) {
                            TileEntityTeddyBear teddy = (TileEntityTeddyBear) tileentity;
                            teddy.setRotation(i);
                        }
                        iblockstate1 = world.getBlockState(pos);
                        SoundType soundtype = iblockstate1.getBlock().getSoundType(iblockstate1, world, pos, player);
                        world.playSound(null, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                        itemstack.shrink(1);
                        if (player instanceof EntityPlayerMP) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, itemstack);
                        }
                    }
                    return EnumActionResult.SUCCESS;
                }
            } else {
                return EnumActionResult.FAIL;
            }
        }
    }

    /**
     * Called to actually place the block, after the location is determined and all
     * permission checks have been made.
     *
     * @param stack  The item stack that was used to place the block. This can be
     *               changed inside the method.
     * @param player The player who is placing the block. Can be null if the block
     *               is not being placed by a player.
     * @param side   The side the player (or machine) right-clicked on.
     */
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        if (!world.setBlockState(pos, newState, 11)) {
            return false;
        }

        IBlockState state = world.getBlockState(pos);
        BlockTeddyBear myBlock = ModBlocks.Placeables.getTeddy(this.getTeddyVariant(stack));//ModBlocks.Placeables.TEDDYBEAR;
        if (state.getBlock() == myBlock) {
            ItemBlock.setTileEntityNBT(world, player, pos, stack);
            myBlock.onBlockPlacedBy(world, pos, state, player, stack);

            if (player instanceof EntityPlayerMP) {
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
            }
        }
        return true;
    }

    @Override
    public Element getPrimaryElement() {
        return Elements.LIGHT;
    }

    @Override
    public String[] getAttributeConfig() {
        return this.CONFIG.ATTRIBUTES;
    }

    @Override
    public String[] getEffectsToRemove() {
        return this.CONFIG.EFFECTS_TO_REMOVE;
    }

    @Override
    public String[] getEffectsToAdd() {
        return this.CONFIG.EFFECTS_TO_ADD;
    }

    @Override
    public String[] getDamageTypesToIgnoreConfig() {
        return this.CONFIG.DAMAGE_TYPES_TO_IGNORE;
    }

    @Override
    public boolean ItemEnabled() {
        return this.CONFIG.ENABLED;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        final ModelResourceLocation normal = new ModelResourceLocation(this.getRegistryName().toString(), "inventory");
        final ModelResourceLocation rembo = new ModelResourceLocation(this.getRegistryName().toString() + "_" + CustomTeddyBearTypes.REMBO.getName(), "inventory");
        final ModelResourceLocation scary = new ModelResourceLocation(this.getRegistryName().toString() + "_" + CustomTeddyBearTypes.SCARY.getName(), "inventory");
        final ModelResourceLocation shivaxi = new ModelResourceLocation(this.getRegistryName().toString() + "_" + CustomTeddyBearTypes.SHIVAXI.getName(), "inventory");
        final ModelResourceLocation bee = new ModelResourceLocation(this.getRegistryName().toString() + "_" + CustomTeddyBearTypes.BEE.getName(), "inventory");
        final ModelResourceLocation panda = new ModelResourceLocation(this.getRegistryName().toString() + "_" + CustomTeddyBearTypes.PANDA.getName(), "inventory");
        final ModelResourceLocation artsy = new ModelResourceLocation(this.getRegistryName().toString() + "_" + CustomTeddyBearTypes.ARTSY.getName(), "inventory");
        final ModelResourceLocation twilight = new ModelResourceLocation(this.getRegistryName().toString() + "_" + CustomTeddyBearTypes.TWILIGHT.getName(), "inventory");
        final ModelResourceLocation ryu = new ModelResourceLocation(this.getRegistryName().toString() + "_" + CustomTeddyBearTypes.RYU.getName(), "inventory");
        final ModelResourceLocation ken = new ModelResourceLocation(this.getRegistryName().toString() + "_" + CustomTeddyBearTypes.KEN.getName(), "inventory");
        final ModelResourceLocation nyan = new ModelResourceLocation(this.getRegistryName().toString() + "_" + CustomTeddyBearTypes.NYAN.getName(), "inventory");
        final ModelResourceLocation nyan_old = new ModelResourceLocation(this.getRegistryName().toString() + "_" + CustomTeddyBearTypes.NYAN_OLD.getName(), "inventory");
        final ModelResourceLocation boom = new ModelResourceLocation(this.getRegistryName().toString() + "_" + CustomTeddyBearTypes.BOOM.getName(), "inventory");
        final ModelResourceLocation rixxi = new ModelResourceLocation(this.getRegistryName().toString() + "_" + CustomTeddyBearTypes.RIXXI.getName(), "inventory");
        final ModelResourceLocation snowie = new ModelResourceLocation(this.getRegistryName().toString() + "_" + CustomTeddyBearTypes.SNOWIE.getName(), "inventory");
        ModelBakery.registerItemVariants(this, normal, scary, rembo, shivaxi, bee, panda, artsy, twilight, ryu, ken, nyan, nyan_old, boom, rixxi, snowie);
        ModelLoader.setCustomMeshDefinition(this, stack -> {
            CustomTeddyBearTypes type = CustomTeddyBearTypes.getType(this.getTeddyVariant(stack));
            switch (type) {
                case SNOWIE:
                    return snowie;
                case RIXXI:
                    return rixxi;
                case BOOM:
                    return boom;
                case NYAN_OLD:
                    return nyan_old;
                case NYAN:
                    return nyan;
                case KEN:
                    return ken;
                case RYU:
                    return ryu;
                case TWILIGHT:
                    return twilight;
                case ARTSY:
                    return artsy;
                case PANDA:
                    return panda;
                case BEE:
                    return bee;
                case SHIVAXI:
                    return shivaxi;
                case SCARY:
                    return scary;
                case REMBO:
                    return rembo;
                default:
                    return normal;
            }
        });
    }
}