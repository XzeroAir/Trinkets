package xzeroair.trinkets.items.base;

import javax.annotation.Nullable;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

public class BaseBow extends ItemBow implements IsModelLoaded, IAccessoryInterface {

	//	public enum BowType implements IStringSerializable {
	//		RIBBON, GLITTER, FRILLY;
	//
	//		@Override
	//		public String getName() {
	//			return this.name().toLowerCase() + "_bow";
	//		}
	//
	//		@Override
	//		public String toString() {
	//			return this.getName();
	//		}
	//
	//		public double getDamageInflicted() {
	//			switch (this) {
	//			case RIBBON:
	//				return 4.0D;
	//			case GLITTER:
	//				return 3.0D;
	//			case FRILLY:
	//				return 3.0D;
	//			default:
	//				return 2.0D;
	//			}
	//		}
	//
	//		public static BowType fromMeta(int meta) {
	//			return BowType.values()[meta % BowType.values().length];
	//		}
	//	}

	public BaseBow(String name) {
		this.setTranslationKey(name);
		this.setRegistryName(name);
		this.setMaxStackSize(1);
		this.setCreativeTab(Trinkets.trinketstab);
		//		ModItems.misc.ITEMS.add(this);

		this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
			@Override
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
				if (entityIn == null)
					return 0.0F;
				else
					return entityIn.getActiveItemStack().getItem() != Items.BOW ? 0.0F : (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F;
			}
		});
		this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter() {
			@Override
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
				return (entityIn != null) && entityIn.isHandActive() && (entityIn.getActiveItemStack() == stack) ? 1.0F : 0.0F;
			}
		});
	}
	//
	//	@Override
	//	@SideOnly(Side.CLIENT)
	//	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
	//		if (this.isInCreativeTab(tab)) {
	//			for (final BowType bowType : BowType.values()) {
	//				subItems.add(new ItemStack(this, 1, bowType.ordinal()));
	//			}
	//		}
	//	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		super.onArmorTick(world, player, itemStack);
	}

	@Override
	public ItemStack findAmmo(EntityPlayer player) {
		if (this.isArrow(player.getHeldItem(EnumHand.OFF_HAND)))
			return player.getHeldItem(EnumHand.OFF_HAND);
		else if (this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND)))
			return player.getHeldItem(EnumHand.MAIN_HAND);
		else {
			for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
				final ItemStack itemstack = player.inventory.getStackInSlot(i);

				if (this.isArrow(itemstack))
					return itemstack;
			}

			return ItemStack.EMPTY;
		}
	}

	@Override
	protected boolean isArrow(ItemStack stack) {
		return stack.getItem() instanceof ItemArrow;
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
			if (i < 0)
				return;

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

							if ((this.getMaxItemUseDuration(stack) - timeLeft) < 6)
								return;
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

	//	public static class PhaseArrow implements EntityArrowThrown.HitResult {
	//
	//		private final float power;
	//
	//		public PhaseArrow(float power) {
	//			this.power = power;
	//		}
	//
	//		@Override
	//		public void onHit(EntityArrowThrown entity, RayTraceResult result, boolean isServer) {
	//			if ((result.entityHit != null) && (result.entityHit != entity.getThrower())) {
	//				entity.setDead();
	//				if (result.entityHit.attackEntityFrom(new EntityDamageSourceIndirect("ribbon_bow", entity, entity.getThrower() == null ? entity : entity.getThrower()).setProjectile(), power * 20f)) {
	//					entity.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / ((itemRand.nextFloat() * 0.2F) + 0.9F));
	//				}
	//			}
	//		}
	//	}
	//
	//	protected EntityArrowThrown spawnThrownEntity(World worldIn, EntityLivingBase entityLiving, float speed, EntityArrowThrown.HitResult hitResult, EntityThrowSpawnData spawnData) {
	//		if (worldIn.isRemote) {
	//			return null;
	//		}
	//		final EntityArrowThrown thrown = new EntityArrowThrown(worldIn, entityLiving, hitResult, spawnData.isLocation ? spawnData.location : spawnData.stack);
	//		thrown.shoot(entityLiving, entityLiving.rotationPitch, entityLiving.rotationYaw, 0.0F, speed, 1f);
	//
	//		thrown.setIgnoreBlocks(spawnData.ignoreBlocks);
	//
	//		worldIn.spawnEntity(thrown);
	//
	//		//		HarshenNetwork.sendToPlayersInWorld(worldIn, new MessagePacketUpdateComplexEntity(thrown.getEntityId(), thrown.serializeNBT()));
	//		return thrown;
	//	}

	/**
	 * Gets the velocity of the arrow entity from the bow's charge
	 */
	public static float getArrowVelocity(int charge) {
		float f = charge / 20.0F;
		f = ((f * f) + (f * 2.0F)) / 3.0F;

		if (f > 1.0F) {
			f = 1.0F;
		}

		return f;
	}

	/**
	 * How long it takes to use or consume an item
	 */
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	/**
	 * returns the action that specifies what animation to play when the items is
	 * being used
	 */
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		//		System.out.println(base_bow.BowType.valueOf(int 0++));
		return EnumAction.BOW;
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		final ItemStack itemstack = playerIn.getHeldItem(handIn);
		final boolean flag = !this.findAmmo(playerIn).isEmpty();

		final ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, worldIn, playerIn, handIn, flag);
		if (ret != null)
			return ret;

		if (!playerIn.capabilities.isCreativeMode && !flag)
			return flag ? new ActionResult(EnumActionResult.PASS, itemstack) : new ActionResult(EnumActionResult.FAIL, itemstack);
		else {
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
	public int getMetadata(int metadata) {
		return metadata;
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		return super.getTranslationKey();
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