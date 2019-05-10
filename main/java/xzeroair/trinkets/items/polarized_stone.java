package xzeroair.trinkets.items;

import java.util.List;

import javax.annotation.Nullable;

import baubles.api.BaubleType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.ItemCap.IItemCap;
import xzeroair.trinkets.capabilities.ItemCap.ItemProvider;
import xzeroair.trinkets.items.base.BaubleBase;
import xzeroair.trinkets.util.handlers.ItemEffectHandler;

public class polarized_stone extends BaubleBase {

	public polarized_stone(String name) {
		super(name);
		addPropertyOverride(new ResourceLocation("in_use"), new IItemPropertyGetter()
		{

			@Override
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
			{
				final Entity entity = entityIn;
				final IItemCap cap = stack.getCapability(ItemProvider.itemCapability, null);

				if ((worldIn == null) && (entity != null))
				{
					worldIn = entity.world;
				}

				if (worldIn == null)
				{
					return 0.0F;
				}
				else
				{
					if((cap != null) && (cap.effect() == true)) {
						return 1F;
					}
					return 0F;
				}
			}
		});
	}
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.BELT;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		final String value = this.enabled ? "Magnatization on":"Magnatization Off";
		tooltip.add(value);
	}
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		final ItemStack itemstack = playerIn.getHeldItem(handIn);
		playerIn.setActiveHand(handIn);
		if(itemstack.hasCapability(ItemProvider.itemCapability, null)) {
			final IItemCap itemNBT = itemstack.getCapability(ItemProvider.itemCapability, null);
			itemNBT.setEffect(!itemNBT.effect());
			this.enabled = itemNBT.effect();
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
	}
	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		if(this.enabled) {
			final AxisAlignedBB bBox = player.getEntityBoundingBox();
			final List<Entity> entLivList = player.world.getEntitiesWithinAABB(Entity.class, bBox.grow(16, 8, 16));
			for(final Entity stuff : entLivList) {
				if((stuff instanceof EntityItem) || (stuff instanceof EntityXPOrb)) {
					ItemEffectHandler.pull(stuff, player.posX, player.posY, player.posZ);
				}
			}
		}
	}
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int itemSlot, boolean isSelected) {
		final EntityPlayer player = (EntityPlayer) entity;
		if(this.enabled) {
			final AxisAlignedBB bBox = player.getEntityBoundingBox();
			final List<Entity> entLivList = player.world.getEntitiesWithinAABB(Entity.class, bBox.grow(16, 8, 16));
			for(final Entity stuff : entLivList) {
				if((stuff instanceof EntityItem) || (stuff instanceof EntityXPOrb)) {
					ItemEffectHandler.pull(stuff, player.posX, player.posY, player.posZ);
				}
			}
			final List<EntityArrow> arwLivList = player.world.getEntitiesWithinAABB(EntityArrow.class, bBox.grow(3));
			for(final EntityArrow arrow : arwLivList) {
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
	}
	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}