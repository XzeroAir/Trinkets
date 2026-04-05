package xzeroair.trinkets.items.base;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.races.IRaceProvider;
import xzeroair.trinkets.races.RaceDefaultInformationWrapper;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.trinkets.shared.TransformationRingConfig;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

import javax.annotation.Nonnull;

public class TrinketRaceBase extends AccessoryBase implements IRaceProvider {

    protected final TransformationRingConfig CONFIG;

    protected EntityRace race;
    protected RaceDefaultInformationWrapper attributes;

    public TrinketRaceBase(String modid, String name, EntityRace race, TransformationRingConfig config) {
        super(modid, name);
        this.race = race;
        this.attributes = race.getRaceInformation();
        this.setUUID("892cfd1f-25c5-44a0-9154-f3b630538c82");
        this.CONFIG = config;
    }

    public TrinketRaceBase(String name, EntityRace race, TransformationRingConfig config) {
        this(Reference.MODID, name, race, config);
        this.setCreativeTab(Trinkets.CREATIVE_TAB);
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        super.getSubItems(tab, items);
    }

    @Override
    public EntityRace getRace() {
        return this.race;
    }

    public RaceDefaultInformationWrapper getAttributes() {
        if (this.attributes == null) {
            this.attributes = this.race.getRaceInformation();
        }
        return this.attributes;
    }

    @Override
    public String[] getAttributeConfig() {
        return this.getAttributes().getAttributes();
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
    public boolean ItemEnabled() {
        return this.CONFIG.ENABLED;
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
        } else {
            Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
        }
    }

}
