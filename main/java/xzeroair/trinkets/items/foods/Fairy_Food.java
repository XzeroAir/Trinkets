package xzeroair.trinkets.items.foods;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.attributes.RaceAttribute.RaceAttribute;
import xzeroair.trinkets.capabilities.sizeCap.ISizeCap;
import xzeroair.trinkets.capabilities.sizeCap.SizeCapPro;
import xzeroair.trinkets.items.base.FoodBase;
import xzeroair.trinkets.util.TrinketsConfig;

public class Fairy_Food extends FoodBase {

	protected static UUID uuid = UUID.fromString("ec989890-2bdb-42bc-a2a1-d6b10bb9a220");

	public Fairy_Food(String name) {
		super(name, 5, 0);
		setAlwaysEdible();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);

		final String value = "I might be able to remove the effects";
		final String value2 = "by drinking some sort of potion";
		tooltip.add(value);
		tooltip.add(value2);
	}

	public static UUID getUUID() {
		return uuid;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 16;
	}
	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		super.onFoodEaten(stack, worldIn, player);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		if(entityLiving instanceof EntityPlayer) {
			if(TrinketsConfig.SERVER.Food.food_effects) {
				RaceAttribute.removeModifier(entityLiving, Dwarf_Stout.getUUID());
				RaceAttribute.addModifier(entityLiving, 1, uuid, 2);
			}
		}
		setCooldown(20);
		super.onItemUseFinish(stack, worldIn, entityLiving);
		return stack;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		final ItemStack itemstack = playerIn.getHeldItem(handIn);
		boolean flag = getEdible();
		final ISizeCap cap = playerIn.getCapability(SizeCapPro.sizeCapability, null);
		if(playerIn instanceof EntityPlayer) {
			if((cap != null) && (cap.getFood().contains("dwarf_stout"))) {
				flag = false;
			}
		}

		if (playerIn.canEat(flag))
		{
			playerIn.setActiveHand(handIn);
			return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
		}
		else
		{
			return new ActionResult<>(EnumActionResult.FAIL, itemstack);
		}
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.DRINK;
	}

	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}