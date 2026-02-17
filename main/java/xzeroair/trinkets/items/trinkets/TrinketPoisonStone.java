package xzeroair.trinkets.items.trinkets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityPoisonAffinity;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigPoisonStone;

import java.util.List;

public class TrinketPoisonStone extends AccessoryBase {

    public static final ConfigPoisonStone serverConfig = TrinketsConfig.SERVER.Items.POISON_STONE;

    public TrinketPoisonStone(String name) {
        super(name);
        this.setUUID("e86e5b58-1b62-4a54-bba1-6594de844c2e");
    }

    @Override
    public String[] getAttributeConfig() {
        return serverConfig.attributes;
    }

    @Override
    public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
        abilities.add(new AbilityPoisonAffinity());
    }

    @Override
    public Element getPrimaryElement() {
        return Elements.POISON;
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