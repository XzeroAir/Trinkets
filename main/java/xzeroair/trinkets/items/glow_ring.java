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
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import xzeroair.trinkets.Main;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.IsModelLoaded;

public class glow_ring extends Item implements IBauble, IsModelLoaded {
	
	

    public glow_ring(String name) {
    	
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
		return BaubleType.RING;
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
			player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION,240,0,true,true));
			player.addPotionEffect(new PotionEffect(MobEffects.GLOWING,240,0,true,true));
		}
	}

// Start of Fall Damage Code	
	
	
//	@SubscribeEvent
//	public static void onPlayerAttacked(LivingFallEvent event) {
//		if(event.getEntityLiving() instanceof EntityPlayer) {
//			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
//			if(getGlowRing(player) == null)
//				event.setCanceled(true);
//			}
//	}
//		public static ItemStack getGlowRing(EntityPlayer player) {
//			IItemHandler baubles = BaublesApi.getBaublesHandler(player);
//			ItemStack stack1 = baubles.getStackInSlot(1);
//			ItemStack stack2 = baubles.getStackInSlot(2);
//			return isGlowRing(stack1) ? stack1 : isGlowRing(stack2) ? stack2 : null;
//	}
//		private static boolean isGlowRing(ItemStack stack) {
//			return !stack.isEmpty() && stack.getItem() == xzeroair.trinkets.init.ModItems.glow_ring;
//	}
		
// End of Fall Damage Code
		
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