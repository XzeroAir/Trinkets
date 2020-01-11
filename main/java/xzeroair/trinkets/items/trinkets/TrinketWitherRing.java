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

public class TrinketWitherRing extends AccessoryBase {

	public TrinketWitherRing(String name) {
		super(name);
	}
	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		if(player.isPotionActive(MobEffects.WITHER)) {
			player.removeActivePotionEffect(MobEffects.WITHER);
		}
	}

	@Override
	public void eventLivingHurt(LivingHurtEvent event, ItemStack stack, EntityLivingBase player) {
		if(TrinketsConfig.SERVER.WITHER_RING.leech) {
			if(!event.getEntityLiving().isDead) {
				if(TrinketsConfig.SERVER.WITHER_RING.wither && !event.getEntityLiving().isPotionActive(MobEffects.WITHER)) {
					final Random rand = new Random();
					if(rand.nextInt(TrinketsConfig.SERVER.WITHER_RING.wither_chance) == 0) {
						event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.WITHER, 40, 0, false, true));
					}
				}
				if((event.getSource().getTrueSource() == player) && (player instanceof EntityPlayer)) {
					if(event.getEntityLiving().isPotionActive(MobEffects.WITHER)) {
						if(event.getAmount() >= TrinketsConfig.SERVER.WITHER_RING.leech_amount) {
							player.heal(TrinketsConfig.SERVER.WITHER_RING.leech_amount);
						} else {
							player.heal(event.getAmount());
						}
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
		return TrinketsConfig.SERVER.WITHER_RING.enabled;
	}

	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}