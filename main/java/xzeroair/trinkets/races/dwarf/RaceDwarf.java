package xzeroair.trinkets.races.dwarf;

import java.util.Random;

import javax.annotation.Nonnull;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.dwarf.config.DwarfConfig;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.BlockHelper;

public class RaceDwarf extends EntityRacePropertiesHandler {

	public static final DwarfConfig serverConfig = TrinketsConfig.SERVER.races.dwarf;

	public RaceDwarf(@Nonnull EntityLivingBase e, EntityProperties properties) {
		super(e, properties, EntityRaces.dwarf);
	}

	@Override
	public void whileTransformed() {
	}

	int bonus = 0;

	@Override
	public void breakingBlock(BreakSpeed event) {
		if (serverConfig.static_mining) {
			if ((entity instanceof EntityPlayer) && (event.getEntityPlayer() == entity)) {
				final ItemStack heldItemStack = event.getEntityPlayer().inventory.getCurrentItem();
				final Item heldItem = heldItemStack.getItem();
				final IBlockState state = event.getState();
				final int toolLevel = heldItem.getHarvestLevel(heldItemStack, "pickaxe", (EntityPlayer) entity, state);
				final int level = state.getBlock().getHarvestLevel(state);
				final float hardness = state.getBlockHardness(event.getEntityPlayer().world, event.getPos());
				if (!heldItem.getToolClasses(heldItemStack).isEmpty() && heldItem.getToolClasses(heldItemStack).contains("pickaxe")) {
					if ((toolLevel >= (level)) || (toolLevel == (level - 1))) {
						event.setNewSpeed(hardness * 4F);
					}
				}
			}
		}
	}

	@Override
	public void blockBroken(BreakEvent event) {
		if (event.getPlayer() == null) {
			return;
		}
		final EntityPlayer player = event.getPlayer();
		EntityProperties cap = Capabilities.getEntityRace(player);
		if ((cap != null)) {
			final IBlockState state = event.getState();
			final ItemStack heldItemStack = player.inventory.getCurrentItem();
			final Item heldItem = heldItemStack.getItem();
			final int blockLevel = state.getBlock().getHarvestLevel(state);
			final int toolLevel = heldItem.getHarvestLevel(heldItemStack, "pickaxe", player, state);
			if (!heldItem.getToolClasses(heldItemStack).isEmpty() && heldItem.getToolClasses(heldItemStack).contains("pickaxe")) {
				if ((toolLevel == (blockLevel - 1)) && serverConfig.skilled_miner) {
					final TileEntity tile = event.getWorld().getTileEntity(event.getPos());
					if (((state.getBlock() instanceof BlockOre) || (state.getBlock() instanceof BlockRedstoneOre) || (state.getBlock() instanceof BlockQuartz) || (state.getBlock() instanceof BlockObsidian))) {
						event.getState().getBlock().harvestBlock(event.getWorld(), player, event.getPos(), state, tile, heldItemStack);
						event.getWorld().destroyBlock(event.getPos(), false);
					}
				}
				if (serverConfig.BLOCKS.bonus_exp) {
					final int bonusExp = serverConfig.BLOCKS.bonus_exp_max;
					final int min = serverConfig.BLOCKS.bonus_exp_min;
					final int rXP = new Random().nextInt(bonusExp);
					final int xp = MathHelper.clamp(rXP, min, rXP + min);
					final int exp = state.getBlock().getExpDrop(state, event.getWorld(), event.getPos(), EnchantmentHelper.getLootingModifier(player));
					if (BlockHelper.isBlockInList(event.getWorld(), state, event.getPos(), serverConfig.BLOCKS.xPBlocks)) {
						event.setExpToDrop(exp + xp);
					}
					if (serverConfig.BLOCKS.minXpBlocks) {
						if (BlockHelper.isBlockInList(event.getWorld(), state, event.getPos(), serverConfig.BLOCKS.MinBlocks)) {
							event.setExpToDrop(exp + 1);
						}
					}
				}

			}

		}
	}

	@Override
	public void blockDrops(HarvestDropsEvent event) {
		if ((event.getHarvester() != null) && (serverConfig.fortune == true)) {
			final EntityPlayer player = event.getHarvester();
			EntityProperties cap = Capabilities.getEntityRace(player);
			if (cap != null) {
				final int configchance = 100;
				final int randBonus = new Random().nextInt(configchance);
				final float floatBonus = (float) randBonus / 100;
				if (floatBonus > 0.25F) {
					bonus = 1;
					if (floatBonus > (0.50F)) {
						bonus = 2;
						if (floatBonus > (0.75F)) {
							bonus = 3;
							if (floatBonus > (0.90F)) {
								bonus = 4;
								if (floatBonus == (1F)) {
									bonus = 5;
								}
							}
						}
					}
				} else {
					bonus = 0;
				}
				final ItemStack heldItemStack = player.inventory.getCurrentItem();
				final Item heldItem = heldItemStack.getItem();
				final int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(35), heldItemStack);
				final boolean flag = serverConfig.fortune_mix ? true : fortuneLevel > 0 ? false : true;
				if (!heldItem.getToolClasses(heldItemStack).isEmpty() && heldItem.getToolClasses(heldItemStack).contains("pickaxe") && (flag == true)) {
					if (!event.getDrops().isEmpty()) {
						if (!event.isSilkTouching()) {
							if (BlockHelper.isBlockInList(event.getWorld(), event.getState(), event.getPos(), serverConfig.BLOCKS.Blocks)) {
								if (cap.isFake()) {
									bonus = MathHelper.clamp(bonus, 0, 2);
								}
								if (cap.getMagic().getMana() >= (10f * bonus)) {
									cap.getMagic().spendMana(10f * bonus);
								} else {
									bonus = 0;
								}
								for (ItemStack element : event.getDrops()) {
									element.grow(bonus);
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void doRenderPlayerPre(RenderPlayerEvent.Pre event) {
		if (entity.isRiding()) {
			double t = 0;
			t = (100 - properties.getSize()) * 0.01D;
			double t1 = (entity.height * 100) / (1.8 * 100);
			double t2 = (1.8 * 100) / (entity.height * 100);
			t *= t2 * 0.5D;
			GlStateManager.translate(0, t, 0);
		}
	}

}
