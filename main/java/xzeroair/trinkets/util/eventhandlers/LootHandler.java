package xzeroair.trinkets.util.eventhandlers;

import java.util.Random;

import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerEvent.HarvestCheck;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.TrinketHelper;

public class LootHandler {

	public static int level, newLevel = 0;

	@SubscribeEvent
	public void blockEvent(BlockEvent event) {
	}

	@SubscribeEvent
	public void playerInteract(PlayerInteractEvent event) {
	}

	@SubscribeEvent
	public void HarvestCheck(HarvestCheck event) {
	}

	@SubscribeEvent
	public void BreakSpeed(BreakSpeed event) {
		if(event.getEntityPlayer() != null) {
			EntityPlayer player = (EntityPlayer) event.getEntityPlayer();
			if(TrinketHelper.baubleCheck(player, ModItems.dwarf_ring)) {
				Item heldItem = player.inventory.getCurrentItem().getItem();
				ItemStack heldItemStack = player.inventory.getCurrentItem();
				IBlockState state = event.getState();
				int level = state.getBlock().getHarvestLevel(state);
				int toolLevel = heldItem.getHarvestLevel(heldItemStack, "pickaxe", player, state);
				float hardness = state.getBlockHardness(event.getEntityPlayer().world, event.getPos());
				float o = event.getOriginalSpeed();

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
			EntityPlayer player = (EntityPlayer) event.getPlayer();
			IBlockState state = event.getState();
			Item heldItem = player.inventory.getCurrentItem().getItem();
			ItemStack heldItemStack = player.inventory.getCurrentItem();
			int level = state.getBlock().getHarvestLevel(state);
			int toolLevel = heldItem.getHarvestLevel(heldItemStack, "pickaxe", player, state);
			float hardness = state.getBlockHardness(event.getWorld(), event.getPos());

			if(TrinketHelper.baubleCheck(player, ModItems.dwarf_ring) && !player.capabilities.isCreativeMode) {
				if(toolLevel == (level - 1)) {
					TileEntity tile = event.getWorld().getTileEntity(event.getPos());
					Item drop = Item.getItemFromBlock(state.getBlock());
					if(drop != null) {
						event.getState().getBlock().harvestBlock(event.getWorld(), player, event.getPos(), state, tile, new ItemStack(drop));
					}
				}
				if(event.getState().getBlock() instanceof BlockOre) {
					int exp = state.getBlock().getExpDrop(state, event.getWorld(), event.getPos(), EnchantmentHelper.getLootingModifier(player));
					event.setExpToDrop(MathHelper.clamp(new Random().nextInt()+exp, 1, exp+10));
				}
				if(event.getState().getBlock() == Blocks.STONE) {
					event.setExpToDrop(MathHelper.clamp(new Random().nextInt(), 2, 3));
				}
			}
		}
	}

	@SubscribeEvent
	public void blockDrops(HarvestDropsEvent event) {
		if(event.getHarvester() != null) {
			EntityPlayer player = (EntityPlayer) event.getHarvester();
			if(TrinketHelper.baubleCheck(player, ModItems.dwarf_ring) && (TrinketsConfig.SERVER.C03_Fortune == true)) {
				if(!event.getDrops().isEmpty()) {
					if(!event.isSilkTouching() && (event.getState().getBlock() instanceof BlockOre)) {
						for(int i = 0; i < event.getDrops().size(); i++) {
							if(!event.getDrops().get(i).toString().contains("ore") || event.getDrops().get(i).toString().contains("Ore")) {
								event.getDrops().get(i).grow(MathHelper.clamp(new Random().nextInt(), 1, 2));
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onCrafted(ItemCraftedEvent event) {
	}

	@SubscribeEvent
	public void itemPickup(ItemPickupEvent event) {
	}

}
