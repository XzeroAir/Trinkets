package xzeroair.trinkets.items.trinkets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilitySturdy;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigInertiaNull;

public class TrinketInertiaNull extends AccessoryBase {

	public static final ConfigInertiaNull serverConfig = TrinketsConfig.SERVER.Items.INERTIA_NULL;

	public TrinketInertiaNull(String name) {
		super(name);
		this.setUUID("8192af5d-98de-4c1e-a125-e99864b99634");
		this.setItemAttributes(serverConfig.Attributes);
		ModItems.trinkets.ITEMS.add(this);
	}

	@Override
	public void initAbilities(EntityLivingBase entity) {
		final float fallMultiplier = serverConfig.fall_damage ? serverConfig.falldamage_amount : 0;
		this.addAbility(
				entity, Abilities.fallResistance,
				new AbilitySturdy().setFallMultiplier(fallMultiplier)
		);
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		super.eventPlayerTick(stack, player);
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