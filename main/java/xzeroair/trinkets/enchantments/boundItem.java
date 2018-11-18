package xzeroair.trinkets.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class boundItem extends Enchantment {

	public boundItem() {
		super(Rarity.VERY_RARE, EnumEnchantmentType.WEARABLE, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
		setName("Bound");
	}

	/**
	 * Returns the minimal value of enchantability needed on the enchantment level passed.
	 */
	@Override
	public int getMinEnchantability(int enchantmentLevel)
	{
		return 25;
	}

	/**
	 * Returns the maximum value of enchantability nedded on the enchantment level passed.
	 */
	@Override
	public int getMaxEnchantability(int enchantmentLevel)
	{
		return 50;
	}

	/**
	 * Returns the maximum level that the enchantment can have.
	 */
	@Override
	public int getMaxLevel()
	{
		return 1;
	}

	@Override
	public boolean isTreasureEnchantment()
	{
		return true;
	}

	@Override
	public boolean isCurse()
	{
		return true;
	}
}
