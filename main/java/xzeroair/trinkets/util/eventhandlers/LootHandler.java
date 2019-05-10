package xzeroair.trinkets.util.eventhandlers;

import java.util.Random;

import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.TrinketHelper;

public class LootHandler {

	@SubscribeEvent
	public void BreakSpeed(BreakSpeed event) {
		if(event.getEntityPlayer() != null) {
			final EntityPlayer player = event.getEntityPlayer();
			if(TrinketHelper.baubleCheck(player, ModItems.dwarf_ring)) {
				final Item heldItem = player.inventory.getCurrentItem().getItem();
				final ItemStack heldItemStack = player.inventory.getCurrentItem();
				final IBlockState state = event.getState();
				final int level = state.getBlock().getHarvestLevel(state);
				final int toolLevel = heldItem.getHarvestLevel(heldItemStack, "pickaxe", player, state);
				final float hardness = state.getBlockHardness(event.getEntityPlayer().world, event.getPos());

				if((heldItem.getToolClasses(heldItemStack).toString().contains("pickaxe"))) {
					if((toolLevel >= (level))) {
						event.setNewSpeed(hardness*4F);
					}
					if((toolLevel == (level -1))) {
						event.setNewSpeed(hardness*4F);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void breakEvent(BreakEvent event) {
		if(event.getPlayer() != null) {
			final EntityPlayer player = event.getPlayer();
			final IBlockState state = event.getState();
			final Item heldItem = player.inventory.getCurrentItem().getItem();
			final ItemStack heldItemStack = player.inventory.getCurrentItem();
			final int level = state.getBlock().getHarvestLevel(state);
			final int toolLevel = heldItem.getHarvestLevel(heldItemStack, "pickaxe", player, state);

			if(TrinketHelper.baubleCheck(player, ModItems.dwarf_ring)) {
				if(heldItem.getToolClasses(heldItemStack).contains("pickaxe")) {
					if(toolLevel <= (level - 1)) {
						final TileEntity tile = event.getWorld().getTileEntity(event.getPos());
						final Item drop = Item.getItemFromBlock(state.getBlock());
						if((drop != null) && ((state.getBlock() instanceof BlockOre) || (state.getBlock() instanceof BlockRedstoneOre) || (state.getBlock() instanceof BlockQuartz))) {
							event.getState().getBlock().harvestBlock(event.getWorld(), player, event.getPos(), state, tile, new ItemStack(drop));
						}
					}
					if(((state.getBlock() instanceof BlockOre) || (state.getBlock() instanceof BlockRedstoneOre) || (state.getBlock() instanceof BlockQuartz))) {
						final int exp = state.getBlock().getExpDrop(state, event.getWorld(), event.getPos(), EnchantmentHelper.getLootingModifier(player));
						event.setExpToDrop(exp + new Random().nextInt((3 - 1) + 1) + 1);
					}
					if(event.getState().getBlock() == Blocks.STONE) {
						event.setExpToDrop(new Random().nextInt((2 - 1) + 1) + 1);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void blockDrops(HarvestDropsEvent event) {
		if(event.getHarvester() != null) {
			final EntityPlayer player = event.getHarvester();
			final Item heldItem = player.inventory.getCurrentItem().getItem();
			final ItemStack heldItemStack = player.inventory.getCurrentItem();
			if(TrinketHelper.baubleCheck(player, ModItems.dwarf_ring) && (TrinketsConfig.SERVER.C03_Fortune == true)) {
				if(heldItem.getToolClasses(heldItemStack).contains("pickaxe")) {
					if(!event.getDrops().isEmpty()) {
						if(!event.isSilkTouching() && ((event.getState().getBlock() instanceof BlockOre) || (event.getState().getBlock() instanceof BlockRedstoneOre) || (event.getState().getBlock() instanceof BlockQuartz))) {
							for(int i = 0; i < event.getDrops().size(); i++) {
								if(!event.getDrops().get(i).toString().contains("ore") || event.getDrops().get(i).toString().contains("Ore")) {
									event.getDrops().get(i).grow(new Random().nextInt((3 - 1) + 1) + 1);
								}
							}
						}
					}
				}
			}
		}
	}
}
