<<<<<<< Updated upstream
package xzeroair.trinkets.items.trinkets;

import java.util.List;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
		this.setAttributeConfig(serverConfig.attributes);
	}

	@Override
	public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
		if (serverConfig.sleep_bonus) {
			abilities.add(new AbilityWellRested());
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		//		String crafter = this.getCrafter(stack);
		//		if (!crafter.isEmpty()) {
		//			return crafter + "'s " + super.getItemStackDisplayName(stack);
		//		}
		return Capabilities.getTrinketProperties(stack, super.getItemStackDisplayName(stack), (prop, name) -> {
			final String crafter = prop.getCrafter();
			if (!crafter.isEmpty()) {
				return crafter;
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

			if (crafterID.equalsIgnoreCase("cdfccefb-1a2e-4fb8-a3b5-041da27fde61") || name.contains("shivaxi")) {
				return 3;
			} else if (crafterID.equalsIgnoreCase("6b5d5e9b-1fe8-4c61-a043-1d84ce95765d") ||
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
			Block TeddyBlock = ModBlocks.Placeables.TEDDYBEAR;
			if (player.canPlayerEdit(pos, facing, itemstack) && TeddyBlock.canPlaceBlockAt(world, pos)) {
				if (world.isRemote) {
					return EnumActionResult.SUCCESS;
				} else {
					int meta = this.getMetadata(itemstack.getMetadata());
					meta = this.getTeddyVariant(itemstack);
					IBlockState iblockstate1 = TeddyBlock.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, player, hand);
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
						itemstack.shrink(1);
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
		Block myBlock = ModBlocks.Placeables.TEDDYBEAR;
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
		ModelBakery.registerItemVariants(this, normal, scary, rembo, shivaxi);
		ModelLoader.setCustomMeshDefinition(this, stack -> {
			int type = this.getTeddyVariant(stack);
			if (type == 3) {
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
=======
package xzeroair.trinkets.items.trinkets;

import java.util.List;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
		this.setAttributeConfig(serverConfig.attributes);
	}

	@Override
	public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
		if (serverConfig.sleep_bonus) {
			abilities.add(new AbilityWellRested());
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		//		String crafter = this.getCrafter(stack);
		//		if (!crafter.isEmpty()) {
		//			return crafter + "'s " + super.getItemStackDisplayName(stack);
		//		}
		return Capabilities.getTrinketProperties(stack, super.getItemStackDisplayName(stack), (prop, name) -> {
			final String crafter = prop.getCrafter();
			if (!crafter.isEmpty()) {
				return crafter;
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

			if (crafterID.equalsIgnoreCase("cdfccefb-1a2e-4fb8-a3b5-041da27fde61") || name.contains("shivaxi")) {
				return 3;
			} else if (crafterID.equalsIgnoreCase("6b5d5e9b-1fe8-4c61-a043-1d84ce95765d") ||
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
			Block TeddyBlock = ModBlocks.Placeables.TEDDYBEAR;
			if (player.canPlayerEdit(pos, facing, itemstack) && TeddyBlock.canPlaceBlockAt(world, pos)) {
				if (world.isRemote) {
					return EnumActionResult.SUCCESS;
				} else {
					int meta = this.getMetadata(itemstack.getMetadata());
					meta = this.getTeddyVariant(itemstack);
					IBlockState iblockstate1 = TeddyBlock.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, player, hand);
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
						itemstack.shrink(1);
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
		Block myBlock = ModBlocks.Placeables.TEDDYBEAR;
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
		ModelBakery.registerItemVariants(this, normal, scary, rembo, shivaxi);
		ModelLoader.setCustomMeshDefinition(this, stack -> {
			int type = this.getTeddyVariant(stack);
			if (type == 3) {
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
>>>>>>> Stashed changes
}