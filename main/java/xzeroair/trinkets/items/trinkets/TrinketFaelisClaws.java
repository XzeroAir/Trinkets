package xzeroair.trinkets.items.trinkets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Loader;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.statuseffects.StatusEffectsEnum;
import xzeroair.trinkets.util.TrinketStatusEffect;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.lycanitesmobs.LycanitesCompat;
import xzeroair.trinkets.util.config.trinkets.ConfigFaelisClaw;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

public class TrinketFaelisClaws extends AccessoryBase implements IsModelLoaded, IAccessoryInterface {

	public static final ConfigFaelisClaw serverConfig = TrinketsConfig.SERVER.Items.FAELIS_CLAW;

	public TrinketFaelisClaws(String name) {
		super(name);
		this.setUUID("4959ec73-142d-4b82-bd0d-cd6cd7431611");
		this.setItemAttributes(serverConfig.Attributes);
		ModItems.trinkets.ITEMS.add(this);
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		super.eventPlayerTick(stack, player);
	}

	@Override
	public void eventLivingHurt(LivingHurtEvent event, ItemStack stack, EntityLivingBase player) {
		EntityProperties prop = Capabilities.getEntityRace(player);
		if ((prop != null)) {
			EntityProperties p = Capabilities.getEntityRace(event.getEntityLiving());
			if ((p != null)) {
				boolean faelis = prop.getCurrentRace().equals(EntityRaces.faelis);
				int duration = (int) (faelis ? serverConfig.duration : serverConfig.duration * 0.33);
				int level = 1;//faelis ? 2 : 1;
				if (Loader.isModLoaded("lycanitesmobs")) {
					LycanitesCompat.applyEffect(event.getEntityLiving(), "bleed", duration, 0);
				} else {
					if (!p.getStatusHandler().getActiveEffects().containsKey(StatusEffectsEnum.bleed.getName())) {
						TrinketStatusEffect effect = new TrinketStatusEffect(StatusEffectsEnum.bleed, duration, level, player);
						p.getStatusHandler().apply(effect);
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
