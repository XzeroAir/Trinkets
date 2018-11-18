package xzeroair.trinkets.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraftforge.common.util.EnumHelper;
import xzeroair.trinkets.enchantments.boundItem;
import xzeroair.trinkets.items.dwarf_ring;

public class ModEnchantments {

	public static final EnumEnchantmentType Bound = EnumHelper.addEnchantmentType("Bound", (item) -> item instanceof dwarf_ring);

	public static final List<Enchantment> ENCHANTMENTS = new ArrayList<>();

	public static final Enchantment BOUND = addEnchantment(new boundItem(), "Bound");

	private static Enchantment addEnchantment(Enchantment enchantment, String name) {
		ENCHANTMENTS.add(enchantment);
		return enchantment.setRegistryName(name).setName(name);
	}
}
