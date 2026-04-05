package xzeroair.trinkets.items.trinkets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.elements.dark.AbilityAffinityDark;
import xzeroair.trinkets.traits.abilities.elements.dark.AbilityDarkImmunity;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigWitherRing;

import javax.annotation.Nonnull;
import java.util.List;

public class TrinketWitherRing extends AccessoryBase {

    protected final ConfigWitherRing CONFIG = TrinketsConfig.SERVER.ITEMS.WITHER_RING;

    public TrinketWitherRing(String name) {
        super(name);
        this.setUUID("bca63279-4a19-4891-b4b0-a5a2f76e4b90");
    }

    @Override
    public void initAbilities(ItemStack stack, EntityLivingBase entity, @Nonnull List<IAbilityInterface> abilities) {
        abilities.add(new AbilityAffinityDark(this.CONFIG.ABILITIES.AFFINITY_DARK));
        abilities.add(new AbilityDarkImmunity(this.CONFIG.ABILITIES.IMMUNITY_DARK));
        this.addSurvivalAbilities(stack, entity, abilities, this.getPrimaryElement(stack), this.CONFIG.COMPAT.SURVIVAL);
    }

    @Override
    public Element getPrimaryElement() {
        return Elements.DARK;
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