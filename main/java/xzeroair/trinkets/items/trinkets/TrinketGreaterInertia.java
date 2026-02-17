package xzeroair.trinkets.items.trinkets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityReduceKinetic;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigGreaterInertia;

import java.util.List;

public class TrinketGreaterInertia extends AccessoryBase {

    public static final ConfigGreaterInertia serverConfig = TrinketsConfig.SERVER.Items.GREATER_INERTIA;

    public TrinketGreaterInertia(String name) {
        super(name);
        this.setUUID("e119ae9a-93b2-4053-ab3c-81108c16ff27");
    }

    @Override
    public String[] getAttributeConfig() {
        return serverConfig.attributes;
    }

    @Override
    public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
        final float fallMultiplier = serverConfig.fall_damage ? serverConfig.falldamage_amount : 0;
        abilities.add(new AbilityReduceKinetic().setFallMultiplier(fallMultiplier));
    }

    @Override
    public Element getPrimaryElement() {
        return Elements.AIR;
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
