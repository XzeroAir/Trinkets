package xzeroair.trinkets.items;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import xzeroair.trinkets.Main;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.IsModelLoaded;

public class glowing_ingot extends Item implements IsModelLoaded {
	
	
	
	public glowing_ingot(String name) {
		
		super();
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
}
