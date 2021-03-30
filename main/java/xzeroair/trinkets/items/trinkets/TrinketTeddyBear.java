package xzeroair.trinkets.items.trinkets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.lycanitesmobs.LycanitesCompat;
import xzeroair.trinkets.util.config.trinkets.ConfigTeddyBear;

public class TrinketTeddyBear extends AccessoryBase {

	public static final ConfigTeddyBear serverConfig = TrinketsConfig.SERVER.Items.TEDDY_BEAR;

	public TrinketTeddyBear(String name) {
		super(name);
		this.setUUID("33b34669-715d-4caa-a31e-9c643c52ba66");
		this.setItemAttributes(serverConfig.Attributes);
		ModItems.trinkets.ITEMS.add(this);
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		super.eventPlayerTick(stack, player);
		LycanitesCompat.removeFear(player);
		LycanitesCompat.removeInsomnia(player);
		//		if (player.isSneaking()) {
		//			player.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 20, 0, false, false));
		//		}
	}

	@Override
	public void eventPotionApplicable(PotionApplicableEvent event, ItemStack stack, EntityLivingBase player) {

	}

	@Override
	public void eventLivingHurt(LivingHurtEvent event, ItemStack stack, EntityLivingBase player) {
		//		if (serverConfig.bonus_damage) {
		//			if (!event.getEntityLiving().isDead) {
		//				if (event.getSource().getTrueSource() == player) {
		//					if (serverConfig.poison && !event.getEntityLiving().isPotionActive(MobEffects.POISON)) {
		//						final Random rand = new Random();
		//						if (rand.nextInt(serverConfig.poison_chance) == 0) {
		//							event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.POISON, 40, 0, false, true));
		//						}
		//					}
		//					if (event.getEntityLiving().isPotionActive(MobEffects.POISON)) {
		//						event.setAmount(event.getAmount() * serverConfig.bonus_damage_amount);
		//					}
		//				}
		//			}
		//		}
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