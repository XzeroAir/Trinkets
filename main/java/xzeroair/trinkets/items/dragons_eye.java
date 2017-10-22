package xzeroair.trinkets.items;

import java.util.List;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import xzeroair.trinkets.Main;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.IsModelLoaded;
import xzeroair.trinkets.util.ItemEffectHandler;


public class dragons_eye extends Item implements IBauble, IsModelLoaded {
	
    public dragons_eye(String name) {
    	
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
		return BaubleType.HEAD;
	}
	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		if (itemstack.getItemDamage()==0 && player.ticksExisted%39==0) {
			EntityPlayer isPlayer = (EntityPlayer) player;
			AxisAlignedBB bBox = isPlayer.getEntityBoundingBox().grow(8, 8, 8);
			List<EntityLiving> entLivList = player.getEntityWorld().getEntitiesWithinAABB(EntityLiving.class, bBox);
			ItemEffectHandler.enemyDetect(isPlayer.world, bBox, isPlayer);
			ItemEffectHandler.blockdetect(isPlayer.world, bBox, isPlayer);
			player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE,160,0,true,true));
		}
	}
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5) {
		
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
        player.removeActivePotionEffect(MobEffects.FIRE_RESISTANCE);
	}
}