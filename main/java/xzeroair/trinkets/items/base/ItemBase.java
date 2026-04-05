package xzeroair.trinkets.items.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.CapabilityProviderBase;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.traits.elements.IElementProvider;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemBase extends Item implements IsModelLoaded, IElementProvider {

    private final List<Element> subElementTypes = new ArrayList<>();

    public ItemBase(String modid, String name) {
        this.setTranslationKey(name);
        this.setRegistryName(new ResourceLocation(modid, name));
    }

    public ItemBase(String name) {
        this(Reference.MODID, name);
        this.setCreativeTab(Trinkets.CREATIVE_TAB);
    }

    @Override
    public boolean getHasSubtypes() {
        return super.getHasSubtypes() || !this.getElementTypes().isEmpty();
    }

    public List<Element> getElementTypes() {
        return this.subElementTypes;
    }

    public ItemBase addElementSubType(@Nonnull Element... elements) {
        for (Element element : elements) {
            this.getElementTypes().add(element);
        }
        return this;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (tab == this.getCreativeTab()) {
            final ItemStack normal = new ItemStack(this, 1, 0);
            items.add(normal);
            int index = 1;
            for (Element element : this.getElementTypes()) {
                ItemStack stack = new ItemStack(this, 1, index++);
                items.add(Capabilities.getTrinketProperties(stack, stack, (properties, rtn) -> {
                    properties.getElementalAttributes().setPrimaryElement(element);
                    return properties.getItemStack();
                }));
            }
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable NBTTagCompound nbt) {
        TrinketProperties newProperties = new TrinketProperties(stack);
        if (stack.getItem() instanceof IElementProvider) {
            newProperties.getElementalAttributes().setPrimaryElement(((IElementProvider) stack.getItem()).getPrimaryElement());
        }
        return new CapabilityProviderBase<>(Capabilities.ITEM_TRINKET, newProperties);
    }

    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        return super.getItemStackDisplayName(stack);
    }

    @SideOnly(Side.CLIENT)
    protected String customItemInformation(ItemStack stack, World world, ITooltipFlag flagIn, int index, String translation) {
        final TranslationHelper helper = TranslationHelper.INSTANCE;
        return helper.formatAddVariables(translation);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<String> tooltips, @Nonnull ITooltipFlag flagIn) {
        if (world == null) {
            return;
        }
        EntityPlayer entity = null;
        try {
            entity = Minecraft.getMinecraft().player;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (entity == null) {
            return;
        }
        final EntityPlayer player = entity;
        boolean showAdvEnabled = flagIn != null && flagIn.isAdvanced();
        super.addInformation(stack, world, tooltips, flagIn);
        final TranslationHelper helper = TranslationHelper.INSTANCE;
        for (int i = 1; i < 10; i++) {
            final int index = i;
            final String string = helper.getLangTranslation(stack.getTranslationKey() + ".tooltip" + i, lang -> this.customItemInformation(stack, world, flagIn, index, lang));
            if (!helper.isStringEmpty(string)) {
                tooltips.add(string);
            }
        }

        //(showAdvEnabled ? EnumRenderLocation.ITEM_ADVANCED : EnumRenderLocation.ALWAYS)
//        EnumRenderLocation modifier = GuiScreen.isCtrlKeyDown() ? EnumRenderLocation.ITEM_CTRL : GuiScreen.isShiftKeyDown() ? EnumRenderLocation.ITEM_SHIFT : GuiScreen.isAltKeyDown() ? EnumRenderLocation.ITEM_ALT : EnumRenderLocation.NEVER;
//        if (showAdvEnabled && TrinketsConfig.CLIENT.ITEMS.RENDER_ELEMENTS) {
//            tooltips.add(ConstantsTextTranslations.KEY_CTRL.getFormattedText());
//            if (modifier.equals(EnumRenderLocation.ITEM_CTRL)) {
//                tooltips.add(this.getPrimaryElement(stack).getDisplayName());
//            }
//        }
    }

    @Override
    public boolean getShareTag() {
        return super.getShareTag();
    }

    @Nullable
    @Override
    public NBTTagCompound getNBTShareTag(@Nonnull ItemStack stack) {
//        return super.getNBTShareTag(stack);
        return Capabilities.getTrinketProperties(stack, super.getNBTShareTag(stack), (prop, rtn) -> prop.saveToNBT(prop.getTag()));
    }

    @Override
    public void readNBTShareTag(@Nonnull ItemStack stack, @Nullable NBTTagCompound nbt) {
        super.readNBTShareTag(stack, nbt);
        if (nbt != null) {
            Capabilities.getTrinketProperties(stack, (prop) -> prop.loadFromNBT(nbt));
        }
    }

    @Override
    public Element getPrimaryElement(ItemStack stack) {
        return Capabilities.getTrinketProperties(stack, this.getPrimaryElement(), (prop, e) -> prop.getElementalAttributes().getPrimaryElement());
    }

    @Override
    public void registerModels() {
        Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
    }

}
