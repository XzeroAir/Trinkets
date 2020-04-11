package xzeroair.trinkets.items.base;

import java.util.List;

import javax.annotation.Nullable;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.client.Minecraft;
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
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.ITrinketContainerHandler;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.PacketAccessorySync;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;
import xzeroair.trinkets.util.interfaces.IDescriptionInterface;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

public class AccessoryBase extends Item implements IsModelLoaded, IDescriptionInterface, IAccessoryInterface {

	public AccessoryBase(String name) {
		this.setTranslationKey(name);
		this.setRegistryName(name);
		this.setMaxStackSize(1);
		this.setCreativeTab(Trinkets.trinketstab);
		ModItems.trinkets.ITEMS.add(this);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return super.initCapabilities(stack, nbt);// new TrinketProviderOld(new DefaultTrinketCapability(-1, -1, 0, 0, false,
													// false));
	}

	@Override
	public void playerEquipped(ItemStack stack, EntityLivingBase player) {
		final boolean client = player.world.isRemote;
		TrinketProperties cap = Capabilities.getTrinketProperties(stack);
		if ((cap != null)) {
			cap.setSlot(this.getEquippedSlot(stack, player));
			if (!client) {
				boolean isTrinket = this.getIsTrinketOrBauble(stack, player).equalsIgnoreCase("Trinket");
				NetworkHandler.INSTANCE.sendToAll(new PacketAccessorySync((EntityPlayerMP) player, cap.Slot(), isTrinket, true));
				//				NetworkHandler.sendItemDataTo(player, stack, cap, isTrinket, (EntityPlayerMP) player);
			}
		}
	}

	@Override
	public void playerUnequipped(ItemStack stack, EntityLivingBase player) {
		final boolean client = player.world.isRemote;
		TrinketProperties cap = Capabilities.getTrinketProperties(stack);
		if ((cap != null)) {
			final boolean isTrinket = this.getIsTrinketOrBauble(stack, player).equalsIgnoreCase("Trinket");
			if ((cap.Slot() >= 0)) {
				if (!client) {
					NetworkHandler.INSTANCE.sendToAll(new PacketAccessorySync((EntityPlayerMP) player, cap.Slot(), isTrinket, false));
				}
				cap.setSlot(-1);
			}
		}
	}

	@Override
	public String getIsTrinketOrBauble(ItemStack stack, EntityLivingBase player) {
		if (player instanceof EntityPlayer) {
			boolean skip = false;
			final ITrinketContainerHandler Trinket = TrinketHelper.getTrinketHandler((EntityPlayer) player);
			if (Trinket != null) {
				for (int i = 0; i < Trinket.getSlots(); i++) {
					if (Trinket.getStackInSlot(i) == stack) {
						skip = true;
						return "Trinket";
					}
				}
			}
			if ((skip != true) && Loader.isModLoaded("baubles")) {
				final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityPlayer) player);
				if (baubles != null) {
					for (int i = 0; i < baubles.getSlots(); i++) {
						if (baubles.getStackInSlot(i) == stack) {
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
		if (player instanceof EntityPlayer) {
			boolean skip = false;
			final ITrinketContainerHandler Trinket = TrinketHelper.getTrinketHandler((EntityPlayer) player);
			if (Trinket != null) {
				for (int i = 0; i < Trinket.getSlots(); i++) {
					if (Trinket.getStackInSlot(i) == stack) {
						skip = true;
						TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
						iCap.setSlot(i);
						return i;
					}
				}
			}
			if ((skip != true) && Loader.isModLoaded("baubles")) {
				final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityPlayer) player);
				if (baubles != null) {
					for (int i = 0; i < baubles.getSlots(); i++) {
						if (baubles.getStackInSlot(i) == stack) {
							TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
							iCap.setSlot(i);
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
		if (TrinketsConfig.CLIENT.GUI.SLOT.showID) {
			tooltip.add("Currently Equipped in Slot: " + String.valueOf(this.getEquippedSlot(stack, Minecraft.getMinecraft().player)));
		}
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
		if ((EnchantmentHelper.hasBindingCurse(stack) == true) && (player.getHeldItem(EnumHand.MAIN_HAND).getItem() != Item.getItemById(399))) {
			return false;
		}
		if ((player instanceof EntityPlayer) && ((EntityPlayer) player).capabilities.isCreativeMode) {
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