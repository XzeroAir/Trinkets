package xzeroair.trinkets.items.trinkets;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.client.model.bowHat;
import xzeroair.trinkets.items.base.BaseBow;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

public class TrinketRibbonBow extends BaseBow implements IsModelLoaded, IAccessoryInterface {

	public TrinketRibbonBow(String name) {
		super(name);
		//		this.setItemAttributes(serverConfig.Attributes);
		//TODO Add Ribbon Bow
		//		ModItems.misc.ITEMS.add(this);
	}

	/**
	 * Called when the player stops using an Item (stops holding the right mouse
	 * button).
	 */
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		if (entityLiving instanceof EntityPlayer) {
			final EntityPlayer entityplayer = (EntityPlayer) entityLiving;
			final boolean flag = entityplayer.capabilities.isCreativeMode || (EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0);
			ItemStack itemstack = this.findAmmo(entityplayer);

			int i = this.getMaxItemUseDuration(stack) - timeLeft;
			i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, entityplayer, i, !itemstack.isEmpty() || flag);
			if (i < 0) {
				return;
			}

			if (!itemstack.isEmpty() || flag) {
				if (itemstack.isEmpty()) {
					itemstack = new ItemStack(Items.ARROW);
				}

				final float f = getArrowVelocity(i);

				if (f >= 0.1D) {
					final boolean flag1 = entityplayer.capabilities.isCreativeMode || ((itemstack.getItem() instanceof ItemArrow) && ((ItemArrow) itemstack.getItem()).isInfinite(itemstack, stack, entityplayer));

					if (!worldIn.isRemote) {
						final ItemArrow itemarrow = (ItemArrow) (itemstack.getItem() instanceof ItemArrow ? itemstack.getItem() : Items.ARROW);
						final EntityArrow entityarrow = itemarrow.createArrow(worldIn, itemstack, entityplayer);
						entityarrow.shoot(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, f * 3.0F, 1.0F);

						if (f == 1.0F) {
							entityarrow.setIsCritical(true);
						}

						final int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

						if (j > 0) {
							entityarrow.setDamage(entityarrow.getDamage() + (j * 0.5D) + 0.5D);
						}

						final int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

						if (k > 0) {
							entityarrow.setKnockbackStrength(k);
						}

						if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
							entityarrow.setFire(100);
						}

						stack.damageItem(1, entityplayer);

						if (flag1 || (entityplayer.capabilities.isCreativeMode && ((itemstack.getItem() == Items.SPECTRAL_ARROW) || (itemstack.getItem() == Items.TIPPED_ARROW)))) {
							entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
						}

						if (stack.getItemDamage() == 0) {

							if ((this.getMaxItemUseDuration(stack) - timeLeft) < 6) {
								return;
							}
							final float f1 = getArrowVelocity(this.getMaxItemUseDuration(stack) - timeLeft);
							//							this.spawnThrownEntity(worldIn, entityLiving, 3f * f1, new HarshenEnderArrow(f1), new EntityThrowSpawnData(1).setIgnoreBlocks(true));
							worldIn.playSound((EntityPlayer) null, entityLiving.posX, entityLiving.posY, entityLiving.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, (1.0F / ((itemRand.nextFloat() * 0.4F) + 1.2F)) + (f1 * 0.5F));
							stack.damageItem(1, entityLiving);

						}

						if (stack.getItemDamage() != 0) {
							worldIn.spawnEntity(entityarrow);
							worldIn.playSound((EntityPlayer) null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, (1.0F / ((itemRand.nextFloat() * 0.4F) + 1.2F)) + (f * 0.5F));
						}
					}

					if (!flag1 && !entityplayer.capabilities.isCreativeMode) {
						itemstack.shrink(1);

						if (itemstack.isEmpty()) {
							entityplayer.inventory.deleteStack(itemstack);
						}
					}

					entityplayer.addStat(StatList.getObjectUseStats(this));
				}
			}
		}
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		final ItemStack itemstack = playerIn.getHeldItem(handIn);
		final boolean flag = !this.findAmmo(playerIn).isEmpty();

		final ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, worldIn, playerIn, handIn, flag);
		if (ret != null) {
			return ret;
		}

		if (!playerIn.capabilities.isCreativeMode && !flag) {
			return flag ? new ActionResult(EnumActionResult.PASS, itemstack) : new ActionResult(EnumActionResult.FAIL, itemstack);
		} else {
			playerIn.setActiveHand(handIn);
			return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
		}
	}

	/**
	 * Return the enchantability factor of the item, most of the time is based on
	 * material.
	 */
	@Override
	public int getItemEnchantability() {
		return 1;
	}

	@Override
	public void registerModels() {
		Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void playerRender(ItemStack stack, EntityLivingBase player, RenderPlayer renderer, boolean isSlim, float partialTicks, float scale, boolean isTrinket) {

		final ModelBase bow = new bowHat();//CallHelper.getModel("bow");
		GlStateManager.pushMatrix();
		//		GlStateManager.rotate(90, 0, 1, 0);
		//		GlStateManager.translate(0, -0.54F, 0);
		if (player.isSneaking()) {
			GlStateManager.translate(0, 0.2, 0);
		}
		renderer.getMainModel().bipedHead.postRender(scale);
		if (player.hasItemInSlot(EntityEquipmentSlot.HEAD)) {
			GlStateManager.translate(0.0F, 0F, -0.12F);
			GlStateManager.scale(1.1F, 1.1F, 1.1F);
		}
		//		System.out.println(scale);
		//		GlStateManager.scale(scale, scale, scale);
		bow.render(player, player.limbSwing, player.limbSwingAmount, player.ticksExisted, player.rotationYaw, player.rotationPitch, 1F);
		GlStateManager.popMatrix();
		//
		//		final ModelBase bow2 = new TestModel();
		//		GlStateManager.pushMatrix();
		//		//		GlStateManager.rotate(90, 0, 1, 0);
		//		//		GlStateManager.rotate(90, 0, 0, 1);
		//		if (player.isSneaking()) {
		//			GlStateManager.translate(0, 0.2, 0);
		//		}
		//		renderer.getMainModel().bipedHead.postRender(scale);
		//		if (player.hasItemInSlot(EntityEquipmentSlot.HEAD)) {
		//			GlStateManager.translate(0.0F, 0F, -0.04F);
		//			GlStateManager.scale(1.1F, 1.1F, 1.1F);
		//		}
		//		GlStateManager.scale(scale, scale, scale);
		//		bow2.render(player, player.limbSwing, player.limbSwingAmount, player.ticksExisted, player.rotationYaw, player.rotationPitch, 1F);
		//		GlStateManager.popMatrix();
	}

}
