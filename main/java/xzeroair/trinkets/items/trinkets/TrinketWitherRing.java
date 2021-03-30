package xzeroair.trinkets.items.trinkets;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigWitherRing;

public class TrinketWitherRing extends AccessoryBase {

	public static final ConfigWitherRing serverConfig = TrinketsConfig.SERVER.Items.WITHER_RING;

	public TrinketWitherRing(String name) {
		super(name);
		this.setUUID("bca63279-4a19-4891-b4b0-a5a2f76e4b90");
		this.setItemAttributes(serverConfig.Attributes);
		ModItems.trinkets.ITEMS.add(this);
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		super.eventPlayerTick(stack, player);
		if (player.isPotionActive(MobEffects.WITHER)) {
			player.removeActivePotionEffect(MobEffects.WITHER);
		}
		if (player.isPotionActive(MobEffects.NAUSEA)) {
			player.removeActivePotionEffect(MobEffects.NAUSEA);
		}
	}

	@Override
	public void eventLivingHurt(LivingHurtEvent event, ItemStack stack, EntityLivingBase player) {
		if (serverConfig.leech) {
			if (!event.getEntityLiving().isDead) {
				if (serverConfig.wither && !event.getEntityLiving().isPotionActive(MobEffects.WITHER)) {
					final Random rand = new Random();
					if (rand.nextInt(serverConfig.wither_chance) == 0) {
						event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.WITHER, 40, 0, false, true));
					}
				}
				if ((event.getSource().getTrueSource() == player) && (player instanceof EntityPlayer)) {
					if (event.getEntityLiving().isPotionActive(MobEffects.WITHER)) {
						if (event.getAmount() >= serverConfig.leech_amount) {
							player.heal(serverConfig.leech_amount);
						} else {
							player.heal(event.getAmount());
						}
					}
				}
			}
		}
	}

	@Override
	public void eventPotionApplicable(PotionApplicableEvent event, ItemStack stack, EntityLivingBase player) {
		List<Potion> effects = new ArrayList<>();
		effects.add(MobEffects.WITHER);
		effects.add(MobEffects.NAUSEA);
		for (Potion effect : effects) {
			if (event.getPotionEffect().getPotion().equals(effect)) {
				event.setResult(Result.DENY);
			}
		}
	}

	@Override
	public void playerEquipped(ItemStack stack, EntityLivingBase player) {
		super.playerEquipped(stack, player);
	}

	@Override
	public void playerUnequipped(ItemStack stack, EntityLivingBase player) {
		super.playerUnequipped(stack, player);
	}

	@Override
	public boolean ItemEnabled() {
		return serverConfig.enabled;
	}

	@Override
	public void registerModels() {
		Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
	}
}