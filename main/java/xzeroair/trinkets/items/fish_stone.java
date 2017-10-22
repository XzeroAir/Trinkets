package xzeroair.trinkets.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.Main;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.IsModelLoaded;


public class fish_stone extends Item implements IBauble, IsModelLoaded{
	
    public fish_stone(String name) {
	
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
		return BaubleType.AMULET;
	}

	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		if (itemstack.getItemDamage()==0) {
			EntityPlayer isPlayer = (EntityPlayer) player;
			AxisAlignedBB bBox = isPlayer.getEntityBoundingBox().grow(0, 2, 0);
			//worldhelper.reverseMaterialAcceleration(isPlayer.world, bBox, MaterialLiquid.WATER, isPlayer);
			if(player.getAir() < 30)
			{
				player.setAir(20);
			}
			//player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING,40,0,true,true));
		}
	}

    @SubscribeEvent
    public static void pickupItem(EntityItemPickupEvent event) {
    	
    }
	@Override
	public boolean hasEffect(ItemStack par1ItemStack) {
		return true;
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
	}
}
