package xzeroair.trinkets.traits.abilities.base;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;

public interface ItemAbilityProvider {

	void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities);

}
