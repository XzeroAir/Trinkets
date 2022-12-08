package xzeroair.trinkets.items.base;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

public class BaseArrow extends ItemArrow implements IsModelLoaded {

	public static enum ArrowType implements IStringSerializable {
		RIBBON, GLITTER;

		@Override
		public String getName() {
			return this.name().toLowerCase() + "_arrow";
		}

		@Override
		public String toString() {
			return this.getName();
		}

		public double getDamageInflicted() {
			switch (this) {
			case RIBBON:
				return 4.0D;
			case GLITTER:
				return 3.0D;
			default:
				return 2.0D;
			}
		}

		public static ArrowType fromMeta(int meta) {
			return ArrowType.values()[meta % ArrowType.values().length];
		}
	}

	public BaseArrow(String name) {
		this.setTranslationKey(name);
		this.setRegistryName(name);
		this.setCreativeTab(Trinkets.trinketstab);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	//	@Override
	//	public EntityBaseArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
	//		EntityBaseArrow entityBaseArrow = new EntityBaseArrow(worldIn, shooter);
	//		entityBaseArrow.setArrowType(BaseArrow.ArrowType.fromMeta(stack.getMetadata()));
	//		entityBaseArrow.setDamage(BaseArrow.ArrowType.fromMeta(stack.getMetadata()).getDamageInflicted());
	//		return entityBaseArrow;
	//	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		if (this.isInCreativeTab(tab)) {
			for (ArrowType arrowType : ArrowType.values()) {
				subItems.add(new ItemStack(this, 1, arrowType.ordinal()));
			}
		}
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		ArrowType arrowtype = BaseArrow.ArrowType.fromMeta(stack.getMetadata());
		switch (arrowtype) {
		//		case FIRE: case ICE: case LIGHTNING:
		//			return true;
		default:
			return super.hasEffect(stack);
		}
	}

	// default behavior in Item is to return 0, but the meta value is important here because it determines which dart type to use
	@Override
	public int getMetadata(int metadata) {
		return metadata;
	}

	// get the correct name for this item by looking up the meta value in the DartType enum
	@Override
	public String getTranslationKey(ItemStack stack) {
		return "item." + ArrowType.fromMeta(stack.getMetadata()).toString();
	}

	@Override
	public boolean isInfinite(ItemStack stack, ItemStack bow, net.minecraft.entity.player.EntityPlayer player) {
		int enchant = net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(net.minecraft.init.Enchantments.INFINITY, bow);
		return enchant <= 0 ? false : this.getClass() == BaseArrow.class;
	}

	@Override
	public void registerModels() {
		Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
	}
}