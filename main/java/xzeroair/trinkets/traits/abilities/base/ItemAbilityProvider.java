package xzeroair.trinkets.traits.abilities.base;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;

import javax.annotation.Nullable;
import java.util.List;

public interface ItemAbilityProvider {

    default void initAbilities(ItemStack stack, @Nullable EntityLivingBase entity, List<IAbilityInterface> abilities) {

    }

}
