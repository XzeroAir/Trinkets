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
import xzeroair.trinkets.capabilities.ItemCap.IItemCap;
import xzeroair.trinkets.capabilities.ItemCap.ItemProvider;
import xzeroair.trinkets.capabilities.sizeCap.ISizeCap;
import xzeroair.trinkets.capabilities.sizeCap.SizeCapPro;
import xzeroair.trinkets.network.CapDataMessage;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.PacketBaubleSync;
import xzeroair.trinkets.util.interfaces.IBaubleSlotInterface;

public class BaubleBase extends ItemBase implements IBauble, IRenderBauble, IBaubleSlotInterface {

	private int slot;
	protected boolean enabled = false;
	protected int target = -1;

	public BaubleBase(String name) {
		super(name);
		setMaxStackSize(1);
		setMaxDamage(0);
	}

	public void effectEnabled(ItemStack itemStack, boolean value) {
		if(itemStack.hasCapability(ItemProvider.itemCapability, null)) {
			final IItemCap itemNBT = itemStack.getCapability(ItemProvider.itemCapability, null);
			itemNBT.setEffect(value);
			this.enabled = value;
		}
	}

	@Override
	public BaubleType getBaubleType(ItemStack par1ItemStack) {
		return BaubleType.TRINKET;
	}

	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.RARE;
	}
	@Override
	public boolean hasEffect(ItemStack par1ItemStack) {
		return false;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public String getTranslationKey(ItemStack par1ItemStack) {
		// Name + Item Damage equals the Lang File Name
		return super.getTranslationKey() + "." + par1ItemStack.getItemDamage();
	}

	@Override
	public boolean isBookEnchantable(ItemStack par1ItemStack, ItemStack book) {
		return false;
	}

	@Override
	public void onEquipped(ItemStack par1ItemStack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 1.9f);
		final EntityPlayer p = (EntityPlayer) player;
		if(!p.world.isRemote) {
			final EntityPlayerMP pMP = (EntityPlayerMP) player;
			NetworkHandler.INSTANCE.sendToAll(new PacketBaubleSync(p, getEquippedSlot(par1ItemStack, pMP)));
			if(p.hasCapability(SizeCapPro.sizeCapability, null)) {
				final ISizeCap cap = pMP.getCapability(SizeCapPro.sizeCapability, null);
				NetworkHandler.INSTANCE.sendToAll(new CapDataMessage(cap.getSize(), cap.getTrans(), cap.getTarget(), cap.getWidth(), cap.getHeight(), cap.getDefaultWidth(), cap.getDefaultHeight(), pMP.getEyeHeight(), pMP.getEntityId()));
			}
		}
	}

	@Override
	public void onUnequipped(ItemStack par1ItemStack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 2f);
		final EntityPlayer p = (EntityPlayer) player;
		if(!p.world.isRemote) {
			final EntityPlayerMP pMP = (EntityPlayerMP) player;
			NetworkHandler.INSTANCE.sendToAll(new PacketBaubleSync(p, getEquippedSlot(par1ItemStack, pMP)));
			if(p.hasCapability(SizeCapPro.sizeCapability, null)) {
				final ISizeCap cap = pMP.getCapability(SizeCapPro.sizeCapability, null);
				NetworkHandler.INSTANCE.sendToAll(new CapDataMessage(cap.getSize(), cap.getTrans(), cap.getTarget(), cap.getWidth(), cap.getHeight(), cap.getDefaultWidth(), cap.getDefaultHeight(), pMP.getEyeHeight(), pMP.getEntityId()));
			}
		}
	}

	@Override
	public boolean canUnequip(ItemStack par1ItemStack, EntityLivingBase player) {
		final EntityPlayer user = (EntityPlayer) player;
		if((EnchantmentHelper.hasBindingCurse(par1ItemStack) == true) && !user.capabilities.isCreativeMode && (player.getHeldItem(EnumHand.MAIN_HAND).getItem() != Item.getItemById(399))) {
			return false;
		}
		return true;
	}

	@Override
	public int getEquippedSlot(ItemStack par1ItemStack, EntityLivingBase player) {
		final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityPlayer) player);
		for(int i = 0; i < baubles.getSlots(); i++) {
			if(baubles.getStackInSlot(i) == par1ItemStack) {
				this.slot = i;
			}
		}
		return this.slot;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack par1ItemStack, NBTTagCompound nbt) {
		return new ItemProvider();
	}


	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack par1ItemStack, EntityPlayer player, RenderType type, float partialTicks) {

	}

}
