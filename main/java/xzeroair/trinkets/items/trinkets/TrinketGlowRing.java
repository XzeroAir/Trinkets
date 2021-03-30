package xzeroair.trinkets.items.trinkets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigGlowRing;

public class TrinketGlowRing extends AccessoryBase {

	public static final ConfigGlowRing serverConfig = TrinketsConfig.SERVER.Items.GLOW_RING;
	//	public static final GlowRing clientConfig = TrinketsConfig.CLIENT.GLOW_RING;

	public TrinketGlowRing(String name) {
		super(name);
		this.setUUID("c7100557-afaf-4e69-b538-ef4ed550b470");
		this.setItemAttributes(serverConfig.Attributes);
		ModItems.trinkets.ITEMS.add(this);
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		super.eventPlayerTick(stack, player);
		player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 250, 0, false, false));
		if (serverConfig.prevent_blindness) {
			if (player.isPotionActive(MobEffects.BLINDNESS)) {
				player.removePotionEffect(MobEffects.BLINDNESS);
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