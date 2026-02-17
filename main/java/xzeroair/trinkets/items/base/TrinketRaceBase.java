package xzeroair.trinkets.items.base;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.races.IRaceProvider;
import xzeroair.trinkets.races.RaceAttributesWrapper;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.shared.TransformationRingConfig;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

public class TrinketRaceBase extends AccessoryBase implements IRaceProvider {

    public TransformationRingConfig serverConfig;

    protected EntityRace race;
    protected RaceAttributesWrapper attributes;

    public TrinketRaceBase(String name, EntityRace race, TransformationRingConfig config) {
        super(name);
        this.race = race;
        this.attributes = race.getRaceAttributes();
        this.setUUID("892cfd1f-25c5-44a0-9154-f3b630538c82");
        this.serverConfig = config;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.getRegistryName().toString().contentEquals("xat:dragon_ring")) {
            if (tab == this.getCreativeTab()) {
                final ItemStack normal = new ItemStack(this, 1, 0);
                items.add(normal);
                if (TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.FIRE_VARIANT) {
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
                if (TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.ICE_VARIANT) {
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
                if (TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.LIGHTNING_VARIANT) {
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
        } else {
            super.getSubItems(tab, items);
        }
    }

    @Override
    public EntityRace getRace() {
        return race;
    }

    public RaceAttributesWrapper getAttributes() {
        if (attributes == null) {
            attributes = race.getRaceAttributes();
        }
        return attributes;
    }

    @Override
    public String[] getAttributeConfig() {
        return getAttributes().getAttributes();
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String customItemInformation(ItemStack stack, World world, ITooltipFlag flagIn, int index, String translation) {
        final EntityRace r = this.getRace();
        final TranslationHelper helper = TranslationHelper.INSTANCE;
        if (r != null) {
            final KeyEntry size = new OptionEntry("rsize", r.getRaceHeight() + "%");
            return helper.formatAddVariables(translation, size);
        } else {
            return helper.formatAddVariables(translation);
        }
    }

    @Override
    public Element getPrimaryElement() {
//        if (this.getRegistryName().toString().equals("xat:dragon_ring")) {
//            return Elements.VOID;
//        }
        return super.getPrimaryElement();
    }

    @Override
    public boolean ItemEnabled() {
        return serverConfig.enabled;
    }

    @Override
    public void registerModels() {
        if (this.getRegistryName().toString().equals("xat:dragon_ring")) {
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
        } else {
            Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
        }
    }

}
