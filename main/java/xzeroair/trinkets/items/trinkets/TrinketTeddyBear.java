package xzeroair.trinkets.items.trinkets;

import java.util.List;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
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
import xzeroair.trinkets.init.ModBlocks;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityWellRested;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigTeddyBear;

public class TrinketTeddyBear extends AccessoryBase {

	public static final ConfigTeddyBear serverConfig = TrinketsConfig.SERVER.Items.TEDDY_BEAR;

	public TrinketTeddyBear(String name) {
		super(name);
		this.setUUID("33b34669-715d-4caa-a31e-9c643c52ba66");
	}

	@Override
	public String[] getAttributeConfig() {
		return serverConfig.attributes;
	}

	@Override
	public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
		if (serverConfig.sleep_bonus) {
			abilities.add(new AbilityWellRested());
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return Capabilities.getTrinketProperties(stack, super.getItemStackDisplayName(stack), (prop, name) -> {
			final String crafter = prop.getCrafter();
			if (!crafter.isEmpty() && !stack.hasDisplayName()) {
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
			if ((crafterID.equalsIgnoreCase("854adc0b-ae55-48d6-b7ba-e641a1eebf42") && !hasDisplayName) || name.contains("nyan")) {
				return 10;
			} else if (name.contains("ken")) {
				return 9;
			} else if (name.contains("ryu")) {
				return 8;
			} else if ((crafterID.equalsIgnoreCase("b2b629a6-454e-4047-a143-4f357171d639") && !hasDisplayName) ||
					name.contains("twilight")) {
				return 7;
			} else if ((crafterID.equalsIgnoreCase("14bba455-affa-46d0-9cf0-806cc0f3d454") && !hasDisplayName) ||
					name.contains("artsy")) {
				return 6;
			} else if ((crafterID.equalsIgnoreCase("5a215d65-e57d-47c6-a322-9ede12a4a100") && !hasDisplayName) ||
					name.contains("potastic") ||
					name.contains("potasticP") ||
					name.contains("panda")) {
				return 5;
			} else if ((crafterID.equalsIgnoreCase("7d50a302-a01c-4e6a-8ea4-03f98662df28") && !hasDisplayName) ||
					name.contains("stingin") ||
					name.contains("bee") ||
					name.contains("bzzz")) {
				return 4;
			} else if ((crafterID.equalsIgnoreCase("cdfccefb-1a2e-4fb8-a3b5-041da27fde61") && !hasDisplayName) || name.contains("shivaxi")) {
				return 3;
			} else if ((crafterID.equalsIgnoreCase("6b5d5e9b-1fe8-4c61-a043-1d84ce95765d") && !hasDisplayName) ||
					name.contains("rembo") ||
					name.contains("cool") ||
					name.contains("badass")) {
				return 1;
			} else if (name.contains("scary") || name.contains("freddy") || name.contains("snuggles")) {
				return 2;
			} else {
				return 0;
			}
		}
		return 0;

	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
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
						world.playSound((EntityPlayer) null, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
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
	public boolean ItemEnabled() {
		return serverConfig.enabled;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		final ModelResourceLocation normal = new ModelResourceLocation(this.getRegistryName().toString(), "inventory");
		final ModelResourceLocation rembo = new ModelResourceLocation(this.getRegistryName().toString() + "_rembos", "inventory");
		final ModelResourceLocation scary = new ModelResourceLocation(this.getRegistryName().toString() + "_scary", "inventory");
		final ModelResourceLocation shivaxi = new ModelResourceLocation(this.getRegistryName().toString() + "_shivaxi", "inventory");
		final ModelResourceLocation bee = new ModelResourceLocation(this.getRegistryName().toString() + "_bee", "inventory");
		final ModelResourceLocation panda = new ModelResourceLocation(this.getRegistryName().toString() + "_panda", "inventory");
		final ModelResourceLocation artsy = new ModelResourceLocation(this.getRegistryName().toString() + "_artsy", "inventory");
		final ModelResourceLocation twilight = new ModelResourceLocation(this.getRegistryName().toString() + "_twilight", "inventory");
		final ModelResourceLocation ryu = new ModelResourceLocation(this.getRegistryName().toString() + "_ryu", "inventory");
		final ModelResourceLocation ken = new ModelResourceLocation(this.getRegistryName().toString() + "_ken", "inventory");
		final ModelResourceLocation nyan = new ModelResourceLocation(this.getRegistryName().toString() + "_nyan", "inventory");
		ModelBakery.registerItemVariants(this, normal, scary, rembo, shivaxi, bee, panda, artsy, twilight, ryu, ken, nyan);
		ModelLoader.setCustomMeshDefinition(this, stack -> {
			int type = this.getTeddyVariant(stack);
			if (type == 10) {
				return nyan;
			} else if (type == 9) {
				return ken;
			} else if (type == 8) {
				return ryu;
			} else if (type == 7) {
				return twilight;
			} else if (type == 6) {
				return artsy;
			} else if (type == 5) {
				return panda;
			} else if (type == 4) {
				return bee;
			} else if (type == 3) {
				return shivaxi;
			} else if (type == 2) {
				return scary;
			} else if (type == 1) {
				return rembo;
			} else {
				return normal;
			}
		});
	}
}