package xzeroair.trinkets.items.trinkets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityDodge;
import xzeroair.trinkets.traits.abilities.AbilityLightningBolt;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigArcingOrb;

import java.util.List;

public class TrinketArcingOrb extends AccessoryBase {

    public final ConfigArcingOrb serverConfig = TrinketsConfig.SERVER.Items.ARCING_ORB;

    public TrinketArcingOrb(String name) {
        super(name);
        this.setUUID("249e65db-7dea-4825-8489-e6aa99a70be1");
    }

    @Override
    public String[] getAttributeConfig() {
        return serverConfig.attributes;
    }

    @Override
    public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
        if (serverConfig.attackAbility) {
            abilities.add(new AbilityLightningBolt());
        }
        if (serverConfig.dodgeAbility) {
            abilities.add(new AbilityDodge());
        }
    }

    @Override
    public Element getPrimaryElement() {
        return Elements.LIGHTNING;
    }

    @Override
    public boolean ItemEnabled() {
        return serverConfig.enabled;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
    }

}
