package xzeroair.trinkets.traits.abilities.interfaces;

import org.apache.commons.lang3.tuple.ImmutablePair;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import xzeroair.trinkets.traits.abilities.IAbilityHandler;

public interface IInteractionAbility extends IAbilityHandler {

	default void interact(EntityLivingBase entityLiving, World world, ItemStack itemStack, EnumHand hand, EnumFacing face, BlockPos pos) {

	}

	default void interactEntity(EntityLivingBase entityLiving, World world, ItemStack itemStack, EnumHand hand, EnumFacing face, BlockPos pos, Entity target) {

	}

	default void interactEntitySpecific(EntityLivingBase entityLiving, World world, ItemStack itemStack, EnumHand hand, EnumFacing face, BlockPos pos, Entity target, Vec3d localPos) {

	}

	default void rightClickWithItem(EntityLivingBase entityLiving, World world, ItemStack itemStack, EnumHand hand, EnumFacing face, BlockPos pos) {

	}

	default ImmutablePair<Result, Result> rightClickBlock(EntityLivingBase entityLiving, World world, ItemStack itemStack, EnumHand hand, EnumFacing face, BlockPos pos, Vec3d hitVec, Result useBlock, Result useItem) {
		return null;
	}

	default ImmutablePair<Result, Result> leftClickBlock(EntityLivingBase entityLiving, World world, ItemStack itemStack, EnumHand hand, EnumFacing face, BlockPos pos, Vec3d hitVec, Result useBlock, Result useItem) {
		return null;
	}

}
