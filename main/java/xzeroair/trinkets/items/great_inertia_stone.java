package xzeroair.trinkets.items;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.Main;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.IsModelLoaded;

@Mod.EventBusSubscriber
public class great_inertia_stone extends Item implements IBauble, IsModelLoaded {
	
		public great_inertia_stone(String name) {
	    	

	    	
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
		return BaubleType.CHARM;
		}
		@Override
		public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
			if (itemstack.getItemDamage()==0 && player.ticksExisted%39==0) {
				player.addPotionEffect(new PotionEffect(MobEffects.SPEED,80,3,true,true));
				player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST,80,3,true,true));
			}
		}
		
		@SubscribeEvent
		public static void onPlayerAttacked(LivingFallEvent event) {
			if(event.getEntityLiving() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) event.getEntityLiving();
				IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
					for(int i = 0; i < baubles.getSlots(); i++) 
						if(baubles.getStackInSlot(i).getItem() == ModItems.great_inertia_stone) {
							event.setDamageMultiplier(0);
						}
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
	        player.removeActivePotionEffect(MobEffects.JUMP_BOOST);
	        player.removeActivePotionEffect(MobEffects.SPEED);
		}
	
}
