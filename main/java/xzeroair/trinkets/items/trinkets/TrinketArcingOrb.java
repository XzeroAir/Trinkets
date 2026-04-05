package xzeroair.trinkets.items.trinkets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityDodge;
import xzeroair.trinkets.traits.abilities.elements.lightning.AbilityLightningBolt;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigArcingOrb;

import java.util.List;

public class TrinketArcingOrb extends AccessoryBase {

    protected final ConfigArcingOrb CONFIG = TrinketsConfig.SERVER.ITEMS.ARCING_ORB;

    public TrinketArcingOrb(String name) {
        super(name);
        this.setUUID("249e65db-7dea-4825-8489-e6aa99a70be1");
    }

    @Override
    public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
        this.addSurvivalAbilities(stack, entity, abilities, this.getPrimaryElement(stack), this.CONFIG.COMPAT.SURVIVAL);
        abilities.add(new AbilityLightningBolt(this.CONFIG.ABILITIES.LIGHTNING_BOLT));
        abilities.add(new AbilityDodge(this.CONFIG.ABILITIES.DODGE));
    }

    @Override
    public Element getPrimaryElement() {
        return Elements.LIGHTNING;
    }

    @Override
    public String[] getAttributeConfig() {
        return this.CONFIG.ATTRIBUTES;
    }

    @Override
    public String[] getEffectsToRemove() {
        return this.CONFIG.EFFECTS_TO_REMOVE;
    }

    @Override
    public String[] getEffectsToAdd() {
        return this.CONFIG.EFFECTS_TO_ADD;
    }

    @Override
    public String[] getDamageTypesToIgnoreConfig() {
        return this.CONFIG.DAMAGE_TYPES_TO_IGNORE;
    }

    @Override
    public boolean ItemEnabled() {
        return this.CONFIG.ENABLED;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
    }

}
