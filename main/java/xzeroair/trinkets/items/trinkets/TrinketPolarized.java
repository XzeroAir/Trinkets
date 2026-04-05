package xzeroair.trinkets.items.trinkets;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityMagnetic;
import xzeroair.trinkets.traits.abilities.AbilityRepel;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigPolarizedStone;

import javax.annotation.Nonnull;
import java.util.List;

public class TrinketPolarized extends AccessoryBase {

    protected final ConfigPolarizedStone CONFIG = TrinketsConfig.SERVER.ITEMS.POLARIZED_STONE;

    public TrinketPolarized(String name) {
        super(name);
        this.setUUID("1ed98d9e-3075-45e0-b6f7-fcdff24caed4");
    }

    @Override
    public void initAbilities(ItemStack stack, EntityLivingBase entity, @Nonnull List<IAbilityInterface> abilities) {
        abilities.add(new AbilityMagnetic(this.CONFIG.ABILITIES.MAGNETIC));
        abilities.add(new AbilityRepel(this.CONFIG.ABILITIES.REPEL));
        this.addSurvivalAbilities(stack, entity, abilities, this.getPrimaryElement(stack), this.CONFIG.COMPAT.SURVIVAL);
    }

    @Override
    public String getTranslationKey(@Nonnull ItemStack stack) {
        // Name + Item Damage equals the Lang File Name
        return super.getTranslationKey();
    }

    @Override
    public Element getPrimaryElement() {
        return Elements.EARTH;
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
        final ModelResourceLocation normal = new ModelResourceLocation(this.getRegistryName().toString(), "inventory");
        final ModelResourceLocation magnet = new ModelResourceLocation(this.getRegistryName().toString() + "_magnet", "inventory");
        final ModelResourceLocation repel = new ModelResourceLocation(this.getRegistryName().toString() + "_repell", "inventory");
        final ModelResourceLocation both = new ModelResourceLocation(this.getRegistryName().toString() + "_both", "inventory");
        ModelBakery.registerItemVariants(this, normal, magnet, repel, both);
        ModelLoader.setCustomMeshDefinition(this, stack -> {
            TrinketProperties prop = Capabilities.getTrinketProperties(stack, new TrinketProperties(stack), (prop1, emptyProp) -> prop1);
//            prop.loadFromNBT(prop.getTag());
            final boolean main = (prop != null) && prop.mainAbility();//this.getTagCompoundSafe(stack).getBoolean("main.ability");
            final boolean alt = (prop != null) && prop.altAbility();//this.getTagCompoundSafe(stack).getBoolean("alt.ability");
            if (main && alt) {
                return both;
            } else if (main) {
                return magnet;
            } else if (alt) {
                return repel;
            } else {
                return normal;
            }
        });
    }
}