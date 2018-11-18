package xzeroair.trinkets.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import xzeroair.trinkets.Main;
import xzeroair.trinkets.compatibilities.sizeCap.CapPro;
import xzeroair.trinkets.compatibilities.sizeCap.ICap;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.helpers.TrinketHelper;
import xzeroair.trinkets.util.helpers.TrinketHelper.TrinketType;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

public class small_ring extends Item implements IBauble, IsModelLoaded {

	public small_ring(String name) {

		setUnlocalizedName(name);
		setRegistryName(name);
		setMaxStackSize(1);
		setMaxDamage(0);
		setCreativeTab(Main.trinketstab);

		ModItems.ITEMS.add(this);
	}
	@Override
	public void registerModels() {
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.RING;
	}
	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		EntityPlayer p = (EntityPlayer) player;
		if(p.hasCapability(CapPro.sizeCapability, null)) {
			ICap nbt = p.getCapability(CapPro.sizeCapability, null);
			if(nbt.getTrans() == true) {
				if(nbt.getSize() == nbt.getTarget()) {
					if(!p.isCreative() && (p.capabilities.allowFlying != true)) {
						p.capabilities.allowFlying = true;
					}
				}
			}
		}
		//	if (itemstack.getItemDamage()==0) {// && player.ticksExisted%39==0) {
		//	}
	}
	@Override
	public boolean hasEffect(ItemStack par1ItemStack) {
		return true;
	}
	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		if((EnchantmentHelper.getEnchantments(book).containsKey(Enchantments.BINDING_CURSE))){
			return true;
		}
		return false;
	}
	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.RARE;
	}
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
	}
	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 1.9f);
	}
	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 2f);
		EntityPlayer p = (EntityPlayer) player;
		if(p.hasCapability(CapPro.sizeCapability, null)) {
			ICap nbt = p.getCapability(CapPro.sizeCapability, null);
			if(!p.isCreative()) {
				p.capabilities.isFlying = false;
				p.capabilities.allowFlying = false;
			}
		}
		//		if(MorphCompat.isEntityMorph(player, Side.SERVER) == null){
		//			EntityPlayer user = (EntityPlayer) player;
		//			float defaultEyeHeight = user.getDefaultEyeHeight();
		//			float defaultStepHeight = 0.6f;
		//			//Default Eye Height
		//			if(user.eyeHeight != defaultEyeHeight){
		//				user.eyeHeight = defaultEyeHeight;
		//			}
		//			if(user.stepHeight != defaultStepHeight){
		//				user.stepHeight = 0.6f;
		//			}
		//		}
	}
	@Override
	public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
		if(itemstack.getItemDamage()==0) {
			EntityPlayer user = (EntityPlayer) player;
			Item ringCheck = TrinketHelper.getBaubleType(user, TrinketType.rings);
			if((ringCheck == ModItems.dwarf_ring) || (ringCheck == ModItems.small_ring)) {
				return false;
			}
		}
		return true;
	}
	@Override
	public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
		EntityPlayer user = (EntityPlayer) player;
		if((EnchantmentHelper.hasBindingCurse(itemstack) == true) && !user.capabilities.isCreativeMode && (player.getHeldItem(EnumHand.MAIN_HAND).getItem() != Item.getItemById(399))) {
			return false;
		}
		return true;
	}

	@Override
	public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) {
		return false;
	}
}
