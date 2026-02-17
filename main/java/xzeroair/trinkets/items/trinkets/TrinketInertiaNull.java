package xzeroair.trinkets.items.trinkets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityNullKinetic;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigInertiaNull;

import java.util.List;

public class TrinketInertiaNull extends AccessoryBase {

    public static final ConfigInertiaNull serverConfig = TrinketsConfig.SERVER.Items.INERTIA_NULL;

    public TrinketInertiaNull(String name) {
        super(name);
        this.setUUID("8192af5d-98de-4c1e-a125-e99864b99634");
    }

    @Override
    public String[] getAttributeConfig() {
        return serverConfig.attributes;
    }

    @Override
    public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
        final float fallMultiplier = serverConfig.fall_damage ? serverConfig.falldamage_amount : 0;
        abilities.add(new AbilityNullKinetic().setFallMultiplier(fallMultiplier));
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