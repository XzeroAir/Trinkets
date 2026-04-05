package xzeroair.trinkets.items.trinkets;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityGreedyEyes;
import xzeroair.trinkets.traits.abilities.AbilityNightVision;
import xzeroair.trinkets.traits.abilities.elements.fire.AbilityFireImmunity;
import xzeroair.trinkets.traits.abilities.elements.ice.AbilityFrostWalker;
import xzeroair.trinkets.traits.abilities.elements.ice.AbilityIceImmunity;
import xzeroair.trinkets.traits.abilities.elements.lightning.AbilityLightningImmunity;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.dragoneye.ConfigDragonsEye;

import javax.annotation.Nonnull;
import java.util.List;

public class TrinketDragonsEye extends AccessoryBase {

    protected final ConfigDragonsEye CONFIG = TrinketsConfig.SERVER.ITEMS.DRAGON_EYE;

    public TrinketDragonsEye(String name) {
        super(name);
        this.setUUID("6a345136-49b7-4b71-88dc-87301e329ac1");
    }

    @Override
    public void initAbilities(ItemStack stack, EntityLivingBase entity, @Nonnull List<IAbilityInterface> abilities) {
        abilities.add(new AbilityNightVision(this.CONFIG.ABILITIES.NIGHT_VISION));
        final Element element = this.getPrimaryElement(stack);
        if (element == Elements.FIRE) {
            this.addFireAbilities(stack, entity, abilities, element);
        } else if (element == Elements.ICE) {
            this.addIceAbilities(stack, entity, abilities, element);
        } else if (element == Elements.LIGHTNING) {
            this.addLightningAbilities(stack, entity, abilities, element);
        } else {
            this.addDefaultAbilities(stack, entity, abilities, element);
        }
        abilities.add(new AbilityGreedyEyes(this.CONFIG.ABILITIES.GREEDY_EYES));
    }

    private void addDefaultAbilities(ItemStack stack, EntityLivingBase entity, @Nonnull List<IAbilityInterface> abilities, Element element) {
        abilities.add(new AbilityFireImmunity(this.CONFIG.ABILITIES.FIRE_IMMUNITY));
        this.addSurvivalAbilities(stack, entity, abilities, element, this.CONFIG.COMPAT.SURVIVAL);
    }

    private void addFireAbilities(ItemStack stack, EntityLivingBase entity, @Nonnull List<IAbilityInterface> abilities, Element element) {
        abilities.add(new AbilityFireImmunity(this.CONFIG.ELEMENTS.FIRE.ABILITIES.FIRE_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(stack, entity, abilities, element, this.CONFIG.ELEMENTS.FIRE.COMPAT.SURVIVAL);
    }

    private void addIceAbilities(ItemStack stack, EntityLivingBase entity, @Nonnull List<IAbilityInterface> abilities, Element element) {
        abilities.add(new AbilityIceImmunity(this.CONFIG.ELEMENTS.ICE.ABILITIES.ICE_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(stack, entity, abilities, element, this.CONFIG.ELEMENTS.ICE.COMPAT.SURVIVAL);
        abilities.add(new AbilityFrostWalker(this.CONFIG.ELEMENTS.ICE.ABILITIES.FROST_WALKER).setRequiredElement(element));
    }

    private void addLightningAbilities(ItemStack stack, EntityLivingBase entity, @Nonnull List<IAbilityInterface> abilities, Element element) {
        abilities.add(new AbilityLightningImmunity(this.CONFIG.ELEMENTS.LIGHTNING.ABILITIES.LIGHTNING_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(stack, entity, abilities, element, this.CONFIG.ELEMENTS.LIGHTNING.COMPAT.SURVIVAL);
    }

    @Override
    public Element getPrimaryElement() {
        return Elements.VOID;
    }

    @Override
    public String[] getAttributeConfig() {
        return this.CONFIG.ATTRIBUTES;
    }

    @Override
    public String[] getAttributeConfig(ItemStack stack) {
        final Element element = this.getPrimaryElement(stack);
        if (element == Elements.FIRE) {
            return this.CONFIG.ELEMENTS.FIRE.ATTRIBUTES;
        } else if (element == Elements.ICE) {
            return this.CONFIG.ELEMENTS.ICE.ATTRIBUTES;
        } else if (element == Elements.LIGHTNING) {
            return this.CONFIG.ELEMENTS.LIGHTNING.ATTRIBUTES;
        } else {
            return this.getAttributeConfig();
        }
    }

    @Override
    public String[] getEffectsToRemove() {
        return this.CONFIG.EFFECTS_TO_REMOVE;
    }

    @Override
    public String[] getEffectsToRemove(ItemStack stack) {
        final Element element = this.getPrimaryElement(stack);
        if (element == Elements.FIRE) {
            return this.CONFIG.ELEMENTS.FIRE.EFFECTS_TO_REMOVE;
        } else if (element == Elements.ICE) {
            return this.CONFIG.ELEMENTS.ICE.EFFECTS_TO_REMOVE;
        } else if (element == Elements.LIGHTNING) {
            return this.CONFIG.ELEMENTS.LIGHTNING.EFFECTS_TO_REMOVE;
        } else {
            return this.getEffectsToRemove();
        }
    }

    @Override
    public String[] getEffectsToAdd() {
        return this.CONFIG.EFFECTS_TO_ADD;
    }

    @Override
    public String[] getEffectsToAdd(ItemStack stack) {
        final Element element = this.getPrimaryElement(stack);
        if (element == Elements.FIRE) {
            return this.CONFIG.ELEMENTS.FIRE.EFFECTS_TO_ADD;
        } else if (element == Elements.ICE) {
            return this.CONFIG.ELEMENTS.ICE.EFFECTS_TO_ADD;
        } else if (element == Elements.LIGHTNING) {
            return this.CONFIG.ELEMENTS.LIGHTNING.EFFECTS_TO_ADD;
        } else {
            return this.getEffectsToAdd();
        }
    }

    @Override
    public String[] getDamageTypesToIgnoreConfig() {
        return this.CONFIG.DAMAGE_TYPES_TO_IGNORE;
    }

    @Override
    public String[] getDamageTypesToIgnoreConfig(ItemStack stack) {
        final Element element = this.getPrimaryElement(stack);
        if (element == Elements.FIRE) {
            return this.CONFIG.ELEMENTS.FIRE.DAMAGE_TYPES_TO_IGNORE;
        } else if (element == Elements.ICE) {
            return this.CONFIG.ELEMENTS.ICE.DAMAGE_TYPES_TO_IGNORE;
        } else if (element == Elements.LIGHTNING) {
            return this.CONFIG.ELEMENTS.LIGHTNING.DAMAGE_TYPES_TO_IGNORE;
        } else {
            return this.getDamageTypesToIgnoreConfig();
        }
    }

    @Override
    public boolean ItemEnabled() {
        return this.CONFIG.ENABLED;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        //		Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
        final ModelResourceLocation normal = new ModelResourceLocation(this.getRegistryName().toString(), "inventory");
        final ModelResourceLocation iceVariant = new ModelResourceLocation(this.getRegistryName().toString() + "_ice", "inventory");
        final ModelResourceLocation lightningVariant = new ModelResourceLocation(this.getRegistryName().toString() + "_lightning", "inventory");
        final ModelResourceLocation fireVariant = new ModelResourceLocation(this.getRegistryName().toString() + "_fire", "inventory");
//        ModelLoader.setCustomModelResourceLocation();
        ModelBakery.registerItemVariants(this, normal, fireVariant, iceVariant, lightningVariant);
        ModelLoader.setCustomMeshDefinition(this, stack -> {
            Element element = this.getPrimaryElement(stack);
            if (element == Elements.LIGHTNING) {
                return lightningVariant;
            } else if (element == Elements.ICE) {
                return iceVariant;
            } else if (element == Elements.FIRE) {
                return fireVariant;
            } else {
                return normal;
            }
        });
    }
}