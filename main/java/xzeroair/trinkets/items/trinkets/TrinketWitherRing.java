package xzeroair.trinkets.items.trinkets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityWitherAffinity;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigWitherRing;

import java.util.List;

public class TrinketWitherRing extends AccessoryBase {

    public static final ConfigWitherRing serverConfig = TrinketsConfig.SERVER.Items.WITHER_RING;

    public TrinketWitherRing(String name) {
        super(name);
        this.setUUID("bca63279-4a19-4891-b4b0-a5a2f76e4b90");
    }

    @Override
    public String[] getAttributeConfig() {
        return serverConfig.attributes;
    }

    @Override
    public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
        abilities.add(new AbilityWitherAffinity());
    }

    @Override
    public Element getPrimaryElement() {
        return Elements.DARK;
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