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
import xzeroair.trinkets.util.config.trinkets.ConfigPoisonStone;

public class TrinketPoison extends AccessoryBase {

	public static final ConfigPoisonStone serverConfig = TrinketsConfig.SERVER.Items.POISON_STONE;

	public TrinketPoison(String name) {
		super(name);
		this.setUUID("e86e5b58-1b62-4a54-bba1-6594de844c2e");
		this.setItemAttributes(serverConfig.Attributes);
		ModItems.trinkets.ITEMS.add(this);
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		super.eventPlayerTick(stack, player);
		if (player.isPotionActive(MobEffects.POISON)) {
			player.removeActivePotionEffect(MobEffects.POISON);
		}
		if (player.isPotionActive(MobEffects.HUNGER)) {
			player.removeActivePotionEffect(MobEffects.HUNGER);
		}
	}

	@Override
	public void eventPotionApplicable(PotionApplicableEvent event, ItemStack stack, EntityLivingBase player) {
		List<Potion> effects = new ArrayList<>();
		effects.add(MobEffects.POISON);
		effects.add(MobEffects.HUNGER);
		for (Potion effect : effects) {
			if (event.getPotionEffect().getPotion().equals(effect)) {
				event.setResult(Result.DENY);
			}
		}
	}

	@Override
	public void eventLivingHurt(LivingHurtEvent event, ItemStack stack, EntityLivingBase player) {
		if (serverConfig.bonus_damage) {
			if (!event.getEntityLiving().isDead) {
				if (event.getSource().getTrueSource() == player) {
					if (serverConfig.poison && !event.getEntityLiving().isPotionActive(MobEffects.POISON)) {
						final Random rand = new Random();
						if (rand.nextInt(serverConfig.poison_chance) == 0) {
							event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.POISON, 40, 0, false, true));
						}
					}
					if (event.getEntityLiving().isPotionActive(MobEffects.POISON)) {
						event.setAmount(event.getAmount() * serverConfig.bonus_damage_amount);
					}
				}
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