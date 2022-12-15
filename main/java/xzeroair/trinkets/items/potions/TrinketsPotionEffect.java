package xzeroair.trinkets.items.potions;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;

public class TrinketsPotionEffect extends PotionEffect {

	public TrinketsPotionEffect(Potion potionIn) {
		this(potionIn, 0, 0);
	}

	public TrinketsPotionEffect(Potion potionIn, int durationIn) {
		this(potionIn, durationIn, 0);
	}

	public TrinketsPotionEffect(Potion potionIn, int durationIn, int amplifierIn) {
		this(potionIn, durationIn, amplifierIn, false, true);
	}

	public TrinketsPotionEffect(PotionEffect other) {
		super(other);
	}

	public TrinketsPotionEffect(Potion potion, int duration, int amplifier, boolean ambient, boolean showParticles) {
		super(potion, duration, amplifier, ambient, showParticles);
	}

	@Override
	public boolean isCurativeItem(ItemStack stack) {
		try {
			final Item item = stack.getItem();
			if (item == Items.POTIONITEM) {
				PotionType type = PotionUtils.getPotionFromItem(stack);
				if (ItemStack.areItemStackShareTagsEqual(stack, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), type))) {
					return true;
				} else {
					return false;
				}
			}
			return super.isCurativeItem(stack);
		} catch (Exception e) {
			return super.isCurativeItem(stack);
		}
	}

}
