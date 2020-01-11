package xzeroair.trinkets.items.trinkets;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.util.TrinketsConfig;

public class TrinketPoison extends AccessoryBase {

	public TrinketPoison(String name) {
		super(name);
	}
	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		if(player.isPotionActive(MobEffects.POISON)) {
			player.removeActivePotionEffect(MobEffects.POISON);
		}
		if(player.isPotionActive(MobEffects.HUNGER)) {
			player.removeActivePotionEffect(MobEffects.HUNGER);
		}
	}

	@Override
	public void eventLivingHurt(LivingHurtEvent event, ItemStack stack, EntityLivingBase player) {
		if(TrinketsConfig.SERVER.POISON_STONE.damage) {
			if(!event.getEntityLiving().isDead) {
				if(event.getSource().getTrueSource() == player) {
					if(TrinketsConfig.SERVER.POISON_STONE.poison && !event.getEntityLiving().isPotionActive(MobEffects.POISON)) {
						final Random rand = new Random();
						if(rand.nextInt(TrinketsConfig.SERVER.POISON_STONE.poison_chance) == 0) {
							event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.POISON, 40, 0, false, true));
						}
					}
					if(event.getEntityLiving().isPotionActive(MobEffects.POISON)) {
						event.setAmount(event.getAmount()*TrinketsConfig.SERVER.POISON_STONE.damage_amount);
					}
				}
			}
		}
	}

	@Override
	public boolean playerCanEquip(ItemStack stack, EntityLivingBase player) {
		if(TrinketHelper.AccessoryCheck(player, stack.getItem())) {
			return false;
		} else {
			return super.playerCanEquip(stack, player);
		}
	}
	@Override
	public void playerEquipped(ItemStack stack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 1.9f);
		super.playerEquipped(stack, player);
	}
	@Override
	public void playerUnequipped(ItemStack stack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 2f);
		super.playerUnequipped(stack, player);
	}

	@Override
	public boolean ItemEnabled() {
		return TrinketsConfig.SERVER.POISON_STONE.enabled;
	}

	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}