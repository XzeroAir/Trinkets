package xzeroair.trinkets.items.trinkets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityWeightless;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigWeightlessStone;

import java.util.List;

public class TrinketWeightless extends AccessoryBase {

    public static final ConfigWeightlessStone serverConfig = TrinketsConfig.SERVER.Items.WEIGHTLESS_STONE;

    public TrinketWeightless(String name) {
        super(name);
        this.setUUID("ba6e840e-46b2-4cb7-af4a-5f681333abe5");
    }

    @Override
    public String[] getAttributeConfig() {
        return serverConfig.attributes;
    }

    @Override
    public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
        abilities.add(new AbilityWeightless());
    }

    @Override
    public Element getPrimaryElement() {
        return Elements.LIGHT;
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