package xzeroair.trinkets.items.base;

import java.util.List;

import javax.annotation.Nullable;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.IContainerHandler;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.TrinketContainerProvider;
import xzeroair.trinkets.capabilities.TrinketCap.DefaultTrinketCapability;
import xzeroair.trinkets.capabilities.TrinketCap.TrinketProvider;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.PacketAccessorySync;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;
import xzeroair.trinkets.util.interfaces.IDescriptionInterface;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

public class AccessoryBase extends Item implements IsModelLoaded, IDescriptionInterface, IAccessoryInterface {

	public AccessoryBase(String name) {
		setTranslationKey(name);
		this.setRegistryName(name);
		setMaxStackSize(1);
		setCreativeTab(Trinkets.trinketstab);
		ModItems.trinkets.ITEMS.add(this);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new TrinketProvider(new DefaultTrinketCapability(-1, -1, 0, 0, false, false));
	}

	@Override
	public void playerEquipped(ItemStack stack, EntityLivingBase player) {
		final boolean client = player.world.isRemote;
		final IAccessoryInterface cap = stack.getCapability(TrinketProvider.itemCapability, null);
		if((cap != null)) {
			final int slot = getEquippedSlot(stack, player);
			final boolean isTrinket = getIsTrinketOrBauble(stack, player).equalsIgnoreCase("Trinket");
			if(!client) {
				NetworkHandler.sendItemDataTo(player, stack, cap, isTrinket, (EntityPlayerMP) player);
			}
			if(player instanceof EntityPlayerMP) {
				NetworkHandler.INSTANCE.sendToAll(new PacketAccessorySync((EntityPlayerMP)player, slot, isTrinket, true));
			}
		}
	}

	@Override
	public void playerUnequipped(ItemStack stack, EntityLivingBase player) {
		final boolean client = player.world.isRemote;
		final IAccessoryInterface cap = stack.getCapability(TrinketProvider.itemCapability, null);
		final boolean isTrinket = getIsTrinketOrBauble(stack, player).equalsIgnoreCase("Trinket");
		if((cap != null) && (cap.wornSlot() >= 0)) {
			if(player instanceof EntityPlayerMP) {
				NetworkHandler.INSTANCE.sendToAll(new PacketAccessorySync((EntityPlayerMP)player, cap.wornSlot(), isTrinket, false));
			}
		}
	}

	@Override
	public String getIsTrinketOrBauble(ItemStack stack, EntityLivingBase player) {
		if(player instanceof EntityPlayer) {
			boolean skip = false;
			final IContainerHandler Trinket = player.getCapability(TrinketContainerProvider.containerCap, null);
			if(Trinket != null) {
				for (int i = 0; i < Trinket.getSlots(); i++) {
					if(Trinket.getStackInSlot(i) == stack) {
						skip = true;
						return "Trinket";
					}
				}
			}
			if((skip != true) && Loader.isModLoaded("baubles")) {
				final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityPlayer)player);
				if(baubles != null) {
					for(int i = 0; i < baubles.getSlots(); i++) {
						if(baubles.getStackInSlot(i) == stack) {
							return "Bauble";
						}
					}
				}
			}
		}
		return "Else";
	}

	@Override
	public int getEquippedSlot(ItemStack stack, EntityLivingBase player) {
		if(player instanceof EntityPlayer) {
			boolean skip = false;
			final IContainerHandler Trinket = player.getCapability(TrinketContainerProvider.containerCap, null);
			if(Trinket != null) {
				for (int i = 0; i < Trinket.getSlots(); i++) {
					if(Trinket.getStackInSlot(i) == stack) {
						skip = true;
						final IAccessoryInterface iCap = stack.getCapability(TrinketProvider.itemCapability, null);
						iCap.setWornSlot(i);
						return i;
					}
				}
			}
			if((skip != true) && Loader.isModLoaded("baubles")) {
				final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityPlayer)player);
				if(baubles != null) {
					for(int i = 0; i < baubles.getSlots(); i++) {
						if(baubles.getStackInSlot(i) == stack) {
							final IAccessoryInterface iCap = stack.getCapability(TrinketProvider.itemCapability, null);
							iCap.setWornSlot(i);
							return i;
						}
					}
				}
			}
		}
		return -1;
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.RARE;
	}
	@Override
	public boolean hasEffect(ItemStack stack) {
		return false;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		// Name + Item Damage equals the Lang File Name
		return super.getTranslationKey() + "." + stack.getItemDamage();
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

	@Override
	public boolean playerCanUnequip(ItemStack stack, EntityLivingBase player) {
		if((EnchantmentHelper.hasBindingCurse(stack) == true) && (player.getHeldItem(EnumHand.MAIN_HAND).getItem() != Item.getItemById(399))) {
			return false;
		}
		if((player instanceof EntityPlayer) && ((EntityPlayer)player).capabilities.isCreativeMode) {
			return true;
		}
		return true;
	}

	@Override
	public void registerModels() {
		Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
	}
	@Override
	public boolean hasDiscription(ItemStack stack) {
		return false;
	}

}