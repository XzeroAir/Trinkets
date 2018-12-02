package xzeroair.trinkets.items;

import java.util.List;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import xzeroair.trinkets.Main;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.BaubleBase;
import xzeroair.trinkets.util.handlers.ItemEffectHandler;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

public class polarized_stone extends BaubleBase {

	public polarized_stone(String name) {
		super(name);
	}
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.BELT;
	}
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.BOW; //EnumAction.BLOCK;
	}
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 72000;
	}
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		playerIn.setActiveHand(handIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
	}
	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		if (itemstack.getItemDamage()==0) {
			EntityPlayer isPlayer = (EntityPlayer) player;
			AxisAlignedBB bBox = isPlayer.getEntityBoundingBox().grow(12, 12, 12);
			List<EntityLiving> entLivList = player.getEntityWorld().getEntitiesWithinAABB(EntityLiving.class, bBox);
			if(!isPlayer.isSneaking()) {
				isPlayer.removeActivePotionEffect(MobEffects.UNLUCK);
				isPlayer.addPotionEffect(new PotionEffect(MobEffects.LUCK, 39, 1, false, true));
				for(EntityLiving stuff : entLivList) {
					ItemEffectHandler.pull(stuff, player.posX, player.posY, player.posZ);
				}
			}
			if(isPlayer.isSneaking()) {
				isPlayer.removeActivePotionEffect(MobEffects.LUCK);
				isPlayer.addPotionEffect(new PotionEffect(MobEffects.UNLUCK, 39, 1, false, false));
				for(EntityLiving stuff : entLivList) {
					ItemEffectHandler.push(stuff, player.posX, player.posY, player.posZ);
				}
			}
		}
	}
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int itemSlot, boolean isSelected) {
		boolean hasItem;
		EntityPlayer player = (EntityPlayer) entity;
		hasItem = (player.getHeldItemMainhand().getItem() == ModItems.polarized_stone) | (player.getHeldItemOffhand().getItem() == ModItems.polarized_stone);
		AxisAlignedBB bBox = player.getEntityBoundingBox().grow(16, 16, 16);
		List<EntityItem> entLivList = world.getEntitiesWithinAABB(EntityItem.class, bBox);
		for(EntityItem stuff : entLivList) {
			if ((hasItem == true) & (player.inventory.getFirstEmptyStack() >= 0)) {
				ItemEffectHandler.pull(stuff, player.posX, player.posY, player.posZ);
			}
		}
		AxisAlignedBB aBox = player.getEntityBoundingBox().grow(3, 3, 3);
		List<EntityArrow> arwLivList = player.getEntityWorld().getEntitiesWithinAABB(EntityArrow.class, aBox);
		for(EntityArrow arrow : arwLivList) {
			if(player.isHandActive() == true) {  //.isActiveItemStackBlocking() == true) {
				arrow.motionX = -0.1;
				arrow.motionZ = -0.1;
			}
		}
	}
	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 1.9f);
	}
	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 2f);
		player.removeActivePotionEffect(MobEffects.LUCK);
	}
}