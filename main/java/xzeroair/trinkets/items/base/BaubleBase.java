package xzeroair.trinkets.items.base;

import java.util.List;

import javax.annotation.Nullable;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.render.IRenderBauble;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.compatibilities.ItemCap.ItemProvider;
import xzeroair.trinkets.compatibilities.sizeCap.CapPro;
import xzeroair.trinkets.compatibilities.sizeCap.ICap;
import xzeroair.trinkets.network.CapDataMessage;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.PacketBaubleSync;
import xzeroair.trinkets.util.interfaces.IBaubleSlotInterface;

public class BaubleBase extends ItemBase implements IBauble, IRenderBauble, IBaubleSlotInterface {

	private int slot;

	public BaubleBase(String name) {
		super(name);
		setMaxStackSize(1);
		setMaxDamage(0);
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.TRINKET;
	}

	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.RARE;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
	}

	@Override
	public boolean isBookEnchantable(ItemStack itemstack, ItemStack book) {
		return false;
	}

	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 1.9f);
		EntityPlayer p = (EntityPlayer) player;
		if(!p.world.isRemote) {
			EntityPlayerMP pMP = (EntityPlayerMP) player;
			NetworkHandler.INSTANCE.sendToAll(new PacketBaubleSync(p, this.getEquippedSlot(itemstack, pMP)));
			if(p.hasCapability(CapPro.sizeCapability, null)) {
				ICap cap = pMP.getCapability(CapPro.sizeCapability, null);
				NetworkHandler.INSTANCE.sendToAll(new CapDataMessage(cap.getSize(), cap.getTrans(), cap.getTarget(), cap.getWidth(), cap.getHeight(), cap.getDefaultWidth(), cap.getDefaultHeight(), pMP.getEyeHeight(), pMP.getEntityId()));
			}
		}
	}

	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 2f);
		EntityPlayer p = (EntityPlayer) player;
		if(!p.world.isRemote) {
			EntityPlayerMP pMP = (EntityPlayerMP) player;
			NetworkHandler.INSTANCE.sendToAll(new PacketBaubleSync(p, this.getEquippedSlot(itemstack, pMP)));
			if(p.hasCapability(CapPro.sizeCapability, null)) {
				ICap cap = pMP.getCapability(CapPro.sizeCapability, null);
				NetworkHandler.INSTANCE.sendToAll(new CapDataMessage(cap.getSize(), cap.getTrans(), cap.getTarget(), cap.getWidth(), cap.getHeight(), cap.getDefaultWidth(), cap.getDefaultHeight(), pMP.getEyeHeight(), pMP.getEntityId()));
			}
		}
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
	public int getEquippedSlot(ItemStack itemstack, EntityLivingBase player) {
		IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityPlayer) player);
		for(int i = 0; i < baubles.getSlots(); i++) {
			if(baubles.getStackInSlot(i) == itemstack) {
				slot = i;
			}
		}
		return slot;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new ItemProvider();
	}

	
	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks) {
		
	}

}
