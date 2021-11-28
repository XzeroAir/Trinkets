package xzeroair.trinkets.traits.abilities;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.races.dwarf.config.DwarfConfig;
import xzeroair.trinkets.traits.abilities.base.AbilityBase;
import xzeroair.trinkets.traits.abilities.interfaces.IMiningAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.BlockHelper;

public class AbilityPsudoFortune extends AbilityBase implements IMiningAbility {

	public static final DwarfConfig serverConfig = TrinketsConfig.SERVER.races.dwarf;

	private int bonus, extra = 0;

	@Override
	public float blockDrops(EntityLivingBase entity, World world, IBlockState state, BlockPos pos, List<ItemStack> drops, float dropChance, boolean silkTouching, int fortuneLevel) {
		final ItemStack heldItemStack = entity.getHeldItemMainhand();
		final Item heldItem = heldItemStack.getItem();
		if ((serverConfig.fortune == true)) {
			final boolean flag = serverConfig.fortune_mix ? true : fortuneLevel > 0 ? false : true;
			if (!heldItem.getToolClasses(heldItemStack).isEmpty() && heldItem.getToolClasses(heldItemStack).contains("pickaxe") && (flag == true)) {
				if (!silkTouching) {
					if (!drops.isEmpty()) {
						EntityProperties cap = Capabilities.getEntityRace(entity);
						if (cap != null) {
							final int configchance = 100;
							final int randBonus = random.nextInt(configchance);
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
							//				final int fortuneLevel1 = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(35), heldItemStack);
							//				System.out.println(fortuneLevel1 + " | " + fortuneLevel);
							if (BlockHelper.isBlockInList(world, state, pos, serverConfig.BLOCKS.Blocks)) {
								if (cap.isFake()) {
									bonus = MathHelper.clamp(bonus, 0, 2);
								}
								for (ItemStack element : drops) {
									element.grow(bonus);
								}
							}
						}
					}
				}
			}
		}
		return dropChance;
	}
}
