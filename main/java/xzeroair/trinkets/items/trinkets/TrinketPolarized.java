package xzeroair.trinkets.items.trinkets;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.TrinketCap.TrinketProvider;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.items.effects.EffectsPolarizedStone;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class TrinketPolarized extends AccessoryBase {

	public TrinketPolarized(String name) {
		super(name);
		addPropertyOverride(new ResourceLocation("in_use"), new IItemPropertyGetter()
		{

			@Override
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
			{
				final Entity entity = entityIn;

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
					if(stack.getItemDamage() == 1) {
						return 1F;
					}
					if(stack.getItemDamage() == 2) {
						return 2F;
					}
					if(stack.getItemDamage() == 3) {
						return 3F;
					}
					return 0F;
				}
			}

		});
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		final IAccessoryInterface iCap = stack.getCapability(TrinketProvider.itemCapability, null);
		if(iCap != null) {
			final String value = ((stack.getItemDamage() == 1) || (stack.getItemDamage() == 3))? "Magnatization on":"Magnatization Off";
			final String altValue = ((stack.getItemDamage() == 2) || (stack.getItemDamage() == 3)) ? "Repell on":"Repell Off";
			tooltip.add(value);
			tooltip.add(altValue);
			//			final IItemPropertyGetter test = getPropertyGetter(new ResourceLocation("in_use"));
			//			tooltip.add(test.toString());
			//			tooltip.add(String.valueOf(stack.getItemDamage()));
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn)
	{
		final ItemStack stack = player.getHeldItem(handIn);
		final IAccessoryInterface iCap = stack.getCapability(TrinketProvider.itemCapability, null);
		if((stack != null) && (iCap != null)) {
			if(stack.getItemDamage() == 0) {
				if(!player.isSneaking()) {
					stack.setItemDamage(1);
					iCap.setAbility(true);
					if(player.world.isRemote) {
						player.sendMessage(new TextComponentString("Collection Mode on (On)"));
					}
					return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(handIn));
				} else {
					stack.setItemDamage(2);
					iCap.setAltAbility(true);
					if(player.world.isRemote) {
						player.sendMessage(new TextComponentString("Repell Mode on (On)"));
					}
					return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(handIn));
				}
			}
			if(stack.getItemDamage() == 2) {
				if(player.isSneaking()) {
					stack.setItemDamage(0);
					iCap.setAltAbility(false);
					if(player.world.isRemote) {
						player.sendMessage(new TextComponentString("Repell Mode off (off)"));
					}
					return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(handIn));
				} else {
					stack.setItemDamage(3);
					iCap.setAbility(true);
					if(player.world.isRemote) {
						player.sendMessage(new TextComponentString("Collection Mode on (On)"));
					}
					return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(handIn));
				}
			}
			if(stack.getItemDamage() == 1) {
				if(!player.isSneaking()) {
					stack.setItemDamage(0);
					iCap.setAbility(false);
					if(player.world.isRemote) {
						player.sendMessage(new TextComponentString("Collection Mode off (off)"));
					}
					return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(handIn));
				} else {
					stack.setItemDamage(3);
					iCap.setAltAbility(true);
					if(player.world.isRemote) {
						player.sendMessage(new TextComponentString("Repell Mode on (On)"));
					}
					return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(handIn));
				}
			}
			if(stack.getItemDamage() == 3) {
				if(player.isSneaking()) {
					stack.setItemDamage(1);
					iCap.setAltAbility(false);
					if(player.world.isRemote) {
						player.sendMessage(new TextComponentString("Repell Mode off (off)"));
					}
					return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(handIn));
				} else {
					stack.setItemDamage(2);
					iCap.setAbility(false);
					if(player.world.isRemote) {
						player.sendMessage(new TextComponentString("Collection Mode off (off)"));
					}
					return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(handIn));
				}
			}
			if(player.world.isRemote) {
				NetworkHandler.sendItemDataServer(player, stack, iCap, false);
			}
		}
		player.setActiveHand(handIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(handIn));
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		EffectsPolarizedStone.collectDrops(stack, player);
		if(TrinketsConfig.SERVER.POLARIZED_STONE.repell) {
			EffectsPolarizedStone.blockArrows(stack, player);
		}
	}

	@Override
	public boolean playerCanEquip(ItemStack stack, EntityLivingBase player) {
		if(TrinketHelper.AccessoryCheck(player, stack.getItem())) {
			return false;
		} else {
			return super.playerCanEquip(stack, player);
		}
	}
	@Override
	public void playerEquipped(ItemStack stack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 1.9f);
		super.playerEquipped(stack, player);
	}
	@Override
	public void playerUnequipped(ItemStack stack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 2f);
		super.playerUnequipped(stack, player);
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		// Name + Item Damage equals the Lang File Name
		return super.getTranslationKey() + "." + 0;
	}

	@Override
	public void registerModels() {
		Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
		Trinkets.proxy.registerItemRenderer(this, 1, "inventory");
		Trinkets.proxy.registerItemRenderer(this, 2, "inventory");
		Trinkets.proxy.registerItemRenderer(this, 3, "inventory");
	}

	@Override
	public boolean ItemEnabled() {
		return TrinketsConfig.SERVER.POLARIZED_STONE.enabled;
	}

	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}