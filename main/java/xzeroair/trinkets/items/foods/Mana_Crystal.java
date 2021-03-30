package xzeroair.trinkets.items.foods;

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
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.items.base.FoodBase;

public class Mana_Crystal extends FoodBase {

	public Mana_Crystal(String name) {
		super(name, 2, 1f);
		this.setAlwaysEdible();
		this.setUUID("5569090a-43b2-468e-918a-05df8570277c");
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 32;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if (entityLiving instanceof EntityPlayer) {
			EntityProperties prop = Capabilities.getEntityRace(entityLiving);
			if (prop != null) {
				//TODO Fix this
				prop.getMagic().setBonusMana(prop.getMagic().getBonusMana() + 1);
				prop.getMagic().sendManaToPlayer(entityLiving);
				prop.getMagic().syncToManaHud();
			}
		}
		this.setCooldown(20);
		super.onItemUseFinish(stack, worldIn, entityLiving);
		return stack;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn) {
		final ItemStack itemstack = player.getHeldItem(handIn);
		boolean flag = this.getEdible();
		if (player.canEat(flag)) {
			player.setActiveHand(handIn);
			return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
		} else {
			return new ActionResult<>(EnumActionResult.FAIL, itemstack);
		}
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.EAT;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		super.registerModels();
	}
}
