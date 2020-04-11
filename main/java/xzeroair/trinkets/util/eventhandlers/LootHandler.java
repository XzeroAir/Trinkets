package xzeroair.trinkets.util.eventhandlers;

import java.util.Random;

import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.RaceProperties;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.effects.EffectsDwarfRing;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.EntityRaceHelper;

public class LootHandler {

	int bonus = 0;

	@SubscribeEvent
	public void BreakSpeed(BreakSpeed event) {
		if ((event.getEntityPlayer() != null)) {
			final EntityPlayer player = event.getEntityPlayer();
			RaceProperties cap = Capabilities.getEntityRace(player);
			if (cap != null) {
				if (((TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDwarfRing) || EntityRaceHelper.getRace(player).contentEquals("dwarf_stout")) &&
						TrinketsConfig.SERVER.DWARF_RING.static_mining) &&
						!TrinketHelper.AccessoryCheck(player, EffectsDwarfRing.incompatible)) {
					final Item heldItem = player.inventory.getCurrentItem().getItem();
					final ItemStack heldItemStack = player.inventory.getCurrentItem();
					final IBlockState state = event.getState();
					final int level = state.getBlock().getHarvestLevel(state);
					final int toolLevel = heldItem.getHarvestLevel(heldItemStack, "pickaxe", player, state);
					final float hardness = state.getBlockHardness(event.getEntityPlayer().world, event.getPos());

					if ((heldItem.getToolClasses(heldItemStack).toString().contains("pickaxe"))) {
						if ((toolLevel >= (level))) {
							event.setNewSpeed(hardness * 4F);
						}
						if ((toolLevel == (level - 1))) {
							event.setNewSpeed(hardness * 4F);
						}
					}
				}
				if (TrinketsConfig.SERVER.SEA_STONE.Swim_Tweaks && TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketSea)) {
					final float o = event.getOriginalSpeed();
					float ns = o;
					if (player.isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier(player)) {
						ns *= 5f;
						if (!player.onGround) {
							ns *= 5.0f;
						}
						event.setNewSpeed(ns);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void breakEvent(BreakEvent event) {
		if (event.getPlayer() != null) {
			final EntityPlayer player = event.getPlayer();
			RaceProperties cap = Capabilities.getEntityRace(player);
			final int configchance = 100;
			final int randBonus = new Random().nextInt(configchance);
			final float floatBonus = (float) randBonus / 100;
			if (floatBonus > (0.50F)) {
				this.bonus = 2;
				if (floatBonus > (0.75F)) {
					this.bonus = 3;
					if (floatBonus > (0.90F)) {
						this.bonus = 4;
						if (floatBonus == (1F)) {
							this.bonus = 5;
						}
					}
				}
			} else {
				this.bonus = 1;
			}
			if (cap != null) {
				final IBlockState state = event.getState();
				final Item heldItem = player.inventory.getCurrentItem().getItem();
				final ItemStack heldItemStack = player.inventory.getCurrentItem();
				final int level = state.getBlock().getHarvestLevel(state);
				final int toolLevel = heldItem.getHarvestLevel(heldItemStack, "pickaxe", player, state);

				if (((TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDwarfRing) || EntityRaceHelper.getRace(player).contentEquals("dwarf_stout")) &&
						TrinketsConfig.SERVER.DWARF_RING.static_mining) &&
						!TrinketHelper.AccessoryCheck(player, EffectsDwarfRing.incompatible)) {
					if (heldItem.getToolClasses(heldItemStack).contains("pickaxe")) {
						if ((toolLevel <= (level - 1)) && TrinketsConfig.SERVER.DWARF_RING.skilled_miner) {
							final TileEntity tile = event.getWorld().getTileEntity(event.getPos());
							final ItemStack stack = new ItemStack(state.getBlock(), 1);
							if ((stack != null) && ((state.getBlock() instanceof BlockOre) || (state.getBlock() instanceof BlockRedstoneOre) || (state.getBlock() instanceof BlockQuartz))) {
								event.getState().getBlock().harvestBlock(event.getWorld(), player, event.getPos(), state, tile, stack);
							}
						}
					}
					if (TrinketsConfig.SERVER.DWARF_RING.BLOCKS.bonus_exp) {
						final int bonusExp = TrinketsConfig.SERVER.DWARF_RING.BLOCKS.bonus_exp_max;
						final int min = TrinketsConfig.SERVER.DWARF_RING.BLOCKS.bonus_exp_min;
						final int rXP = new Random().nextInt(bonusExp);
						final int xp = MathHelper.clamp(rXP, min, rXP + min);
						final int exp = state.getBlock().getExpDrop(state, event.getWorld(), event.getPos(), EnchantmentHelper.getLootingModifier(player));
						for (final String string : TrinketsConfig.SERVER.DWARF_RING.BLOCKS.xPBlocks) {
							String Type = string.toLowerCase();
							String meta = "";
							if (Type.contains("[")) {
								final int metaStart = Type.indexOf("[");
								final int metaEnd = Type.lastIndexOf("]");
								meta = Type.substring(metaStart + 1, metaEnd);
								Type = Type.substring(0, metaStart);

							}
							if (state.getBlock().getRegistryName().toString().contentEquals(Type)) {
								if (meta.isEmpty()) {
									event.setExpToDrop(exp + xp);
								} else {
									if (state.getBlock().getMetaFromState(state) == Integer.parseInt(meta)) {
										event.setExpToDrop(exp + xp);
									}
								}
							}
						}

						if (TrinketsConfig.SERVER.DWARF_RING.BLOCKS.minXpBlocks) {
							for (final String string : TrinketsConfig.SERVER.DWARF_RING.BLOCKS.MinBlocks) {
								String Type = string.toLowerCase();
								String meta = "";
								if (Type.contains("[")) {
									final int metaStart = Type.indexOf("[");
									final int metaEnd = Type.lastIndexOf("]");
									meta = Type.substring(metaStart + 1, metaEnd);
									Type = Type.substring(0, metaStart);

								}
								if (state.getBlock().getRegistryName().toString().contentEquals(Type)) {
									if (meta.isEmpty()) {
										event.setExpToDrop(exp + 1);
									} else {
										if (state.getBlock().getMetaFromState(state) == Integer.parseInt(meta)) {
											event.setExpToDrop(exp + 1);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void blockDrops(HarvestDropsEvent event) {
		if ((event.getHarvester() != null) && (TrinketsConfig.SERVER.DWARF_RING.fortune == true)) {
			final EntityPlayer player = event.getHarvester();
			RaceProperties cap = Capabilities.getEntityRace(player);
			if (cap != null) {
				final Item heldItem = player.inventory.getCurrentItem().getItem();
				final ItemStack heldItemStack = player.inventory.getCurrentItem();
				if (((TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDwarfRing) || EntityRaceHelper.getRace(player).contentEquals("dwarf_stout")) &&
						TrinketsConfig.SERVER.DWARF_RING.static_mining) &&
						!TrinketHelper.AccessoryCheck(player, EffectsDwarfRing.incompatible)) {
					final int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(35), heldItemStack);
					final boolean flag = TrinketsConfig.SERVER.DWARF_RING.fortune_mix ? true : fortuneLevel > 0 ? false : true;
					if (heldItem.getToolClasses(heldItemStack).contains("pickaxe") && (flag == true)) {
						if (!event.getDrops().isEmpty()) {
							if (!event.isSilkTouching()) {
								for (final String string : TrinketsConfig.SERVER.DWARF_RING.BLOCKS.Blocks) {
									String Type = string.toLowerCase();
									String meta = "";
									if (Type.contains("[")) {
										final int metaStart = Type.indexOf("[");
										final int metaEnd = Type.lastIndexOf("]");
										meta = Type.substring(metaStart + 1, metaEnd);
										Type = Type.substring(0, metaStart);

									}
									if (event.getState().getBlock().getRegistryName().toString().contentEquals(Type)) {
										if (meta.isEmpty()) {
											for (ItemStack element : event.getDrops()) {
												element.grow(this.bonus);
											}
										} else {
											if (event.getState().getBlock().getMetaFromState(event.getState()) == Integer.parseInt(meta)) {
												for (ItemStack element : event.getDrops()) {
													element.grow(this.bonus);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
