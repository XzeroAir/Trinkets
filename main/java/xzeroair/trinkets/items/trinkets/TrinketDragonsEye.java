package xzeroair.trinkets.items.trinkets;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.*;
import xzeroair.trinkets.traits.abilities.compat.survival.AbilityColdImmunity;
import xzeroair.trinkets.traits.abilities.compat.survival.AbilityHeatImmunity;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ClientConfig.ClientConfigItems.ClientConfigDragonsEye;
import xzeroair.trinkets.util.config.trinkets.ConfigDragonsEye;

import java.util.List;

public class TrinketDragonsEye extends AccessoryBase {

    public static final ConfigDragonsEye serverConfig = TrinketsConfig.SERVER.Items.DRAGON_EYE;
    public static final ClientConfigDragonsEye clientConfig = TrinketsConfig.CLIENT.items.DRAGON_EYE;

    public TrinketDragonsEye(String name) {
        super(name);
        this.setUUID("6a345136-49b7-4b71-88dc-87301e329ac1");
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (tab == this.getCreativeTab()) {
            final ItemStack normal = new ItemStack(this, 1, 0);
            items.add(normal);
            if (serverConfig.compat.iaf.FIRE_VARIANT) {
                final ItemStack fire = new ItemStack(this, 1, 0);
                NBTTagCompound tag = new NBTTagCompound();
                Capabilities.getTrinketProperties(fire, prop -> {
                    prop.setVariant(1);
                    prop.getElementAttributes().setPrimaryElement(Elements.FIRE);
                    prop.saveToNBT(tag);
                });
                fire.setTagCompound(tag);
                items.add(fire);
            }
            if (serverConfig.compat.iaf.ICE_VARIANT) {
                final ItemStack ice = new ItemStack(this, 1, 0);
                NBTTagCompound tag = new NBTTagCompound();
                Capabilities.getTrinketProperties(ice, prop -> {
                    prop.setVariant(2);
                    prop.getElementAttributes().setPrimaryElement(Elements.ICE);
                    prop.saveToNBT(tag);
                });
                ice.setTagCompound(tag);
                items.add(ice);
            }
            if (serverConfig.compat.iaf.LIGHTNING_VARIANT) {
                final ItemStack lightning = new ItemStack(this, 1, 0);
                NBTTagCompound tag = new NBTTagCompound();
                Capabilities.getTrinketProperties(lightning, prop -> {
                    prop.setVariant(3);
                    prop.getElementAttributes().setPrimaryElement(Elements.LIGHTNING);
                    prop.saveToNBT(tag);
                });
                lightning.setTagCompound(tag);
                items.add(lightning);
            }
        }
    }

    @Override
    public String[] getAttributeConfig() {
        return serverConfig.attributes;
    }

    @Override
    public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
        abilities.add(new AbilityNightVision().toggleAbility(true));
        final Element element = this.getPrimaryElement(stack);
        final boolean tanEnabled = (Trinkets.MOD_COMPAT.ToughAsNails && TrinketsConfig.getClientStore().MOD_COMPAT_TOUGHASNAILS);
        final boolean sdEnabled = (Trinkets.MOD_COMPAT.SimpleDifficulty && TrinketsConfig.getClientStore().MOD_COMPAT_SIMPLEDIFFICULTY);
        final boolean survival = tanEnabled || sdEnabled;
        if (serverConfig.compat.iaf.FIRE_VARIANT && (element == Elements.FIRE)) {
            abilities.add(new AbilityFireImmunity().setRequiredElement(Elements.FIRE));
            if (survival && serverConfig.compat.tan.immuneToHeat) {
                abilities.add(new AbilityHeatImmunity().setRequiredElement(Elements.FIRE));
            }
        } else if (serverConfig.compat.iaf.ICE_VARIANT && (element == Elements.ICE)) {
            abilities.add(new AbilityIceImmunity().setRequiredElement(Elements.ICE));
            if (survival && serverConfig.compat.tan.immuneToCold) {
                abilities.add(new AbilityColdImmunity().setRequiredElement(Elements.ICE));
            }
            if (serverConfig.compat.iaf.FROST_WALKER) {
                abilities.add(new AbilityFrostWalker().setRequiredElement(Elements.ICE));
            }
        } else if (serverConfig.compat.iaf.LIGHTNING_VARIANT && (element == Elements.LIGHTNING)) {
            abilities.add(new AbilityLightningImmunity().setRequiredElement(Elements.LIGHTNING));
        } else {
            if (serverConfig.compat.iaf.DE_FIRE_RESIST) {
                abilities.add(new AbilityFireImmunity().setRequiredElement(Elements.NEUTRAL));
                if (survival && serverConfig.compat.tan.immuneToHeat) {
                    abilities.add(new AbilityHeatImmunity().setRequiredElement(Elements.NEUTRAL));
                }
            }
        }
        if (serverConfig.oreFinder) {
            abilities.add(new AbilityBlockFinder());
        }
    }

    @Override
    public Element getPrimaryElement() {
//        return Elements.VOID;
        return super.getPrimaryElement();
    }

    @Override
    public boolean ItemEnabled() {
        return serverConfig.enabled;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        //		Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
        final ModelResourceLocation normal = new ModelResourceLocation(this.getRegistryName().toString(), "inventory");
        final ModelResourceLocation iceVariant = new ModelResourceLocation(this.getRegistryName().toString() + "_ice", "inventory");
        final ModelResourceLocation lightningVariant = new ModelResourceLocation(this.getRegistryName().toString() + "_lightning", "inventory");
        final ModelResourceLocation fireVariant = new ModelResourceLocation(this.getRegistryName().toString() + "_fire", "inventory");
        ModelBakery.registerItemVariants(this, normal, fireVariant, iceVariant, lightningVariant);
        ModelLoader.setCustomMeshDefinition(this, stack -> {
            TrinketProperties prop = Capabilities.getTrinketProperties(stack, new TrinketProperties(stack), (prop1, emptyProp) -> {
                return prop1;
            });
            prop.loadFromNBT(prop.getTag());
            Element element = prop.getElementAttributes().getPrimaryElement();
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