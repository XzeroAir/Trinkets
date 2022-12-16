package xzeroair.trinkets.traits.abilities;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.races.dwarf.config.DwarfConfig;
import xzeroair.trinkets.traits.abilities.interfaces.IMiningAbility;
import xzeroair.trinkets.util.TrinketsConfig;

public class AbilityPsudoFortune extends Ability implements IMiningAbility {

	// TODO Fix this ability or Remove it, Currently added functions to SkilledMiner

	public static final DwarfConfig serverConfig = TrinketsConfig.SERVER.races.dwarf;

	public AbilityPsudoFortune() {
		super(Abilities.psudoFortune);
	}

	private int bonus, extra = 0;

	@Override
	public float blockDrops(EntityLivingBase entity, World world, IBlockState state, BlockPos pos, List<ItemStack> drops, float dropChance, boolean silkTouching, int fortuneLevel) {
		final ItemStack heldItemStack = entity.getHeldItemMainhand();
		final Item heldItem = heldItemStack.getItem();
		//		if ((serverConfig.fortune == true)) {
		//			final boolean flag = serverConfig.fortune_mix ? true : fortuneLevel > 0 ? false : true;
		//			if (!heldItem.getToolClasses(heldItemStack).isEmpty() && heldItem.getToolClasses(heldItemStack).contains("pickaxe") && (flag == true)) {
		//				if (!silkTouching) {
		//					if (!drops.isEmpty()) {
		//						boolean isFake = Capabilities.getEntityProperties(entity, false, (prop, fake) -> prop.isFake());
		//						final int configchance = 100;
		//						final int randBonus = random.nextInt(configchance);
		//						final float floatBonus = (float) randBonus / 100;
		//						if (floatBonus > 0.25F) {
		//							bonus = 1;
		//							if (floatBonus > (0.50F)) {
		//								bonus = 2;
		//								if (floatBonus > (0.75F)) {
		//									bonus = 3;
		//									if (floatBonus > (0.90F)) {
		//										bonus = 4;
		//										if (floatBonus == (1F)) {
		//											bonus = 5;
		//										}
		//									}
		//								}
		//							}
		//						} else {
		//							bonus = 0;
		//						}
		//
		//						//TODO Add a Check to make sure the drop is not the block
		//
		//						//				final int fortuneLevel1 = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(35), heldItemStack);
		//						//				System.out.println(fortuneLevel1 + " | " + fortuneLevel);
		//						for (String s : serverConfig.BLOCKS.Blocks) {
		//							ConfigObject object = new ConfigObject(s);
		//							if (object.doesBlockMatchEntry(state)) {
		//								if (isFake) {
		//									bonus = MathHelper.clamp(bonus, 0, 2);
		//								}
		//								Item blockItem = Item.getItemFromBlock(state.getBlock());
		//								for (final ItemStack element : drops) {
		//									int isBlockItem = blockItem.getRegistryName().compareTo(element.getItem().getRegistryName());
		//									if (isBlockItem != 0) {
		//										element.grow(bonus);
		//									}
		//								}
		//								break;
		//							}
		//						}
		//					}
		//				}
		//			}
		//		}
		return dropChance;
	}
}
