package xzeroair.trinkets.items.misc;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.other.AbilityLevelingBuff;

import javax.annotation.Nonnull;
import java.util.List;

public class TrinketExpDevice extends AccessoryBase {
    // TODO Fix
    //	public static final ConfigExpDevice serverConfig = TrinketsConfig.SERVER.Items.EXP_DEVICE;

    public TrinketExpDevice(String name) {
        super(name);
        this.setUUID("66b30f50-81d0-4a0a-8f1e-b7896962fffe");
    }

    @Override
    public String[] getAttributeConfig() {
        return new String[0];//serverConfig.attributes;
    }

    @Override
    public void initAbilities(ItemStack stack, EntityLivingBase entity, @Nonnull List<IAbilityInterface> abilities) {
        abilities.add(new AbilityLevelingBuff());
    }

    @Override
    public boolean ItemEnabled() {
        return false;//serverConfig.enabled;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
    }

}
