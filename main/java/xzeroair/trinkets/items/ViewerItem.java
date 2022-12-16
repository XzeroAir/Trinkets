<<<<<<< Updated upstream
package xzeroair.trinkets.items;

import java.util.List;

import com.charles445.simpledifficulty.api.SDCapabilities;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.compat.simpledifficulty.SDCompat;
import xzeroair.trinkets.util.helpers.MagicHelper;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

public class ViewerItem extends Item implements IsModelLoaded, IAccessoryInterface {

	public ViewerItem(String name) {
		this.setTranslationKey(name);
		this.setRegistryName(name);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("" + TextFormatting.RED + "This is a Debug Item");
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		try {
			MagicHelper.refillMana(player);
			if (player.getHealth() < player.getMaxHealth()) {
				player.heal(player.getMaxHealth());
			}
			SDCompat.ClearTempurature(player);
			if ((player.ticksExisted % 300) == 0) {
				SDCompat.addThirst(player, 10, 0);
			}
			SDCapabilities.getThirstData(player).setThirstLevel(8);
			FoodStats food = player.getFoodStats();
			if (food.getFoodLevel() < 8) {
				food.addStats(2, 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//		player.motionY += 0.075;
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		super.onArmorTick(world, player, itemStack);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		return super.onLeftClickEntity(stack, player, entity);
	}

	@Override
	public void eventLivingHurtAttacked(ItemStack stack, EntityLivingBase entity, LivingHurtEvent event) {
		//		if (event.getEntityLiving() == player) {
		//			event.setAmount(0);
		//		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		//Hm
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
	}

	@Override
	public boolean ItemEnabled() {
		return true;
	}

	@Override
	public void registerModels() {
		Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
	}

	@Override
	public int getSlot(ItemStack stack) {
		return -1;
	}

	@Override
	public String getItemHandler(ItemStack stack) {
		return "None";
	}

}
=======
package xzeroair.trinkets.items;

import java.util.List;

import com.charles445.simpledifficulty.api.SDCapabilities;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.compat.simpledifficulty.SDCompat;
import xzeroair.trinkets.util.helpers.MagicHelper;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

public class ViewerItem extends Item implements IsModelLoaded, IAccessoryInterface {

	public ViewerItem(String name) {
		this.setTranslationKey(name);
		this.setRegistryName(name);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("" + TextFormatting.RED + "This is a Debug Item");
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		try {
			MagicHelper.refillMana(player);
			if (player.getHealth() < player.getMaxHealth()) {
				player.heal(player.getMaxHealth());
			}
			SDCompat.ClearTempurature(player);
			if ((player.ticksExisted % 300) == 0) {
				SDCompat.addThirst(player, 10, 0);
			}
			SDCapabilities.getThirstData(player).setThirstLevel(8);
			FoodStats food = player.getFoodStats();
			if (food.getFoodLevel() < 8) {
				food.addStats(2, 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//		player.motionY += 0.075;
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		super.onArmorTick(world, player, itemStack);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		return super.onLeftClickEntity(stack, player, entity);
	}

	@Override
	public void eventLivingHurtAttacked(ItemStack stack, EntityLivingBase entity, LivingHurtEvent event) {
		//		if (event.getEntityLiving() == player) {
		//			event.setAmount(0);
		//		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		//Hm
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
	}

	@Override
	public boolean ItemEnabled() {
		return true;
	}

	@Override
	public void registerModels() {
		Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
	}

	@Override
	public int getSlot(ItemStack stack) {
		return -1;
	}

	@Override
	public String getItemHandler(ItemStack stack) {
		return "None";
	}

}
>>>>>>> Stashed changes
