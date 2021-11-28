package xzeroair.trinkets.traits.abilities.interfaces;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public interface IBowAbility extends IItemUseAbility {

	default ActionResult<ItemStack> knockArrow(EntityLivingBase entity, World world, ItemStack bow, EnumHand hand, ActionResult<ItemStack> action, boolean hasAmmo) {
		return action;
	}

	default void looseArrow(EntityLivingBase entity, World world, ItemStack bow, int charge) {

	}

	default void arrowImpact(EntityLivingBase entity, EntityArrow arrow, RayTraceResult rayTraceResult) {

	}

}
