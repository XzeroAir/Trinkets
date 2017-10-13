package xzeroair.trinkets.items;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.Main;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.IsModelLoaded;

@Mod.EventBusSubscriber
public class inertia_stone extends Item implements IBauble, IsModelLoaded {
	
    public inertia_stone(String name) {
	
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
		// TODO Auto-generated method stub
		return BaubleType.CHARM;
	}
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if(!world.isRemote) { 
			IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			for(int i = 0; i < baubles.getSlots(); i++) 
				if((baubles.getStackInSlot(i) == null || baubles.getStackInSlot(i).isEmpty()) && baubles.isItemValidForSlot(i, player.getHeldItem(hand), player)) {
					baubles.setStackInSlot(i, player.getHeldItem(hand).copy());
					if(!player.capabilities.isCreativeMode){
						player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
					}
					onEquipped(player.getHeldItem(hand), player);
					break;
				}
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		if (itemstack.getItemDamage()==0 && player.ticksExisted%39==0) {
			player.addPotionEffect(new PotionEffect(MobEffects.SPEED,40,2,true,true));
			player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST,40,1,true,true));
		}
	}
	
	@SubscribeEvent
	public static void onPlayerAttacked(LivingFallEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
				for(int i = 0; i < baubles.getSlots(); i++) 
					if(baubles.getStackInSlot(i).getItem() == ModItems.inertia_stone)  //.inventory.getCurrentItem().getItem() == ModItems.inertia_stone)
						event.setCanceled(true);
			}
	}
	
    @SubscribeEvent
    public static void pickupItem(EntityItemPickupEvent event) {
    //	event.getEntityPlayer().addPotionEffect(new PotionEffect(MobEffects.WITHER, 5, 1));
    //   System.out.println("Item picked up!");
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
}