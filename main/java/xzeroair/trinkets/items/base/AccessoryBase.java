package xzeroair.trinkets.items.base;

import java.util.List;
import java.util.UUID;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.attributes.AttributeConfigWrapper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.ITrinketContainerHandler;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.shared.BaubleCompat;
import xzeroair.trinkets.util.handlers.ItemAttributeHandler;
import xzeroair.trinkets.util.helpers.AttributeHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;
import xzeroair.trinkets.util.interfaces.IAttributeConfigHelper;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

public abstract class AccessoryBase extends Item implements IsModelLoaded, IAccessoryInterface {

	protected UUID uuid;
	//	private int slot = -1;
	//	private int handler = 0;
	private BaubleCompat BaubleType;
	protected AttributeConfigWrapper attributesConfig;
	protected ItemAttributeHandler attributes;

	public AccessoryBase(String name) {
		this.setTranslationKey(name);
		this.setRegistryName(name);
		this.setMaxStackSize(1);
		this.setCreativeTab(Trinkets.trinketstab);
		attributes = new ItemAttributeHandler();
	}

	protected void setItemAttributes(IAttributeConfigHelper attributes) {
		attributesConfig = new AttributeConfigWrapper(attributes);
		this.attributes = new ItemAttributeHandler(this.getUUID(), attributesConfig);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return TranslationHelper.addTextColorFromLangKey(super.getItemStackDisplayName(stack));
	}

	public String getType() {
		return BaubleType.bauble_type;
	}

	public UUID getUUID() {
		return uuid;
	}

	protected void setUUID(String uuid) {
		this.uuid = UUID.fromString(uuid);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
	}

	@Override
	public void playerEquipped(ItemStack stack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 1.9f);
		TrinketProperties cap = Capabilities.getTrinketProperties(stack);
		if ((cap != null)) {
			cap.itemEquipped(player, this.getEquippedSlot(stack, player), this.getIsTrinketOrBauble(stack, player));
		}
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		attributes.addAttributes(player);
		TrinketProperties cap = Capabilities.getTrinketProperties(stack);
		if ((cap != null)) {
			cap.onUpdate();
		}
	}

	@Override
	public void playerUnequipped(ItemStack stack, EntityLivingBase player) {
		TrinketProperties cap = Capabilities.getTrinketProperties(stack);
		if ((cap != null)) {
			cap.itemUnequipped();
			if (!cap.isEquipped(player)) {
				player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 2f);
			}
		}
		AttributeHelper.removeAttributes(player, this.getUUID());
	}

	@Override
	public void eventPlayerLogout(ItemStack stack, EntityLivingBase player) {
		AttributeHelper.removeAttributes(player, this.getUUID());
	}

	public int getIsTrinketOrBauble(ItemStack stack, EntityLivingBase player) {
		if (player instanceof EntityPlayer) {
			boolean skip = false;
			final ITrinketContainerHandler Trinket = TrinketHelper.getTrinketHandler((EntityPlayer) player);
			if (Trinket != null) {
				for (int i = 0; i < Trinket.getSlots(); i++) {
					if (Trinket.getStackInSlot(i) == stack) {
						skip = true;
						return 1;
					}
				}
			}
			if ((skip != true) && Loader.isModLoaded("baubles")) {
				final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityPlayer) player);
				if (baubles != null) {
					for (int i = 0; i < baubles.getSlots(); i++) {
						if (baubles.getStackInSlot(i) == stack) {
							return 2;
						}
					}
				}
			}
		}
		return 0;
	}

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
						iCap.setHandler(1);
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
							iCap.setHandler(2);
							return i;
						}
					}
				}
			}
		}
		return -1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		if (TrinketsConfig.CLIENT.GUI.SLOT.showID) {
			tooltip.add("Currently Equipped in Slot: " + String.valueOf(this.getEquippedSlot(stack, Minecraft.getMinecraft().player)));
		}
		TranslationHelper.addTooltips(stack, worldIn, tooltip);
		if ((attributes != null) && (attributesConfig != null)) {
			TranslationHelper.addOtherTooltips(stack, worldIn, attributesConfig, tooltip);
		}
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

	public NBTTagCompound getTagCompoundSafe(ItemStack stack) {
		NBTTagCompound tagCompound = stack.getTagCompound();
		if (tagCompound == null) {
			tagCompound = new NBTTagCompound();
			stack.setTagCompound(tagCompound);
		}
		return tagCompound;
	}

	@Override
	public boolean playerCanEquip(ItemStack stack, EntityLivingBase player) {
		if ((BaubleType != null) && BaubleType.equip_multiple) {
			return true;
		} else {
			if (TrinketHelper.AccessoryCheck(player, stack.getItem())) {
				return false;
			} else {
				return true;
			}
		}

	}

	/*
	 * Don't Edit Below this
	 */

	@Override
	public String getTranslationKey(ItemStack stack) {
		// Name + Item Damage equals the Lang File Name
		return super.getTranslationKey() + "." + stack.getItemDamage();
	}

	//	@Override
	//	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
	//		if ((enchantment == Enchantments.BINDING_CURSE) || (enchantment == Enchantments.VANISHING_CURSE)) {
	//			return true;
	//		} else {
	//			return super.canApplyAtEnchantingTable(stack, enchantment);
	//		}
	//	}
	//
	//	@Override
	//	public int getItemEnchantability() {
	//		return 1;//super.getItemEnchantability();
	//	}
	//
	//	@Override
	//	public int getItemEnchantability(ItemStack stack) {
	//		return super.getItemEnchantability(stack);
	//	}
	//
	//	@Override
	//	public boolean isEnchantable(ItemStack stack) {
	//		return true;//super.isEnchantable(stack);
	//	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		if ((EnchantmentHelper.getEnchantments(book).containsKey(Enchantments.BINDING_CURSE)) || (EnchantmentHelper.getEnchantments(book).containsKey(Enchantments.VANISHING_CURSE))) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.RARE;
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return super.hasEffect(stack);
	}
}