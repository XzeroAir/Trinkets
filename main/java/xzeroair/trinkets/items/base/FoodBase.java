package xzeroair.trinkets.items.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.api.ITrinketInterface;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.CapabilityProviderBase;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.enums.EnumRenderLocation;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.traits.elements.IElementProvider;
import xzeroair.trinkets.util.ConstantsTextTranslations;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FoodBase extends ItemFood implements IsModelLoaded, ITrinketInterface, IElementProvider {

    private UUID uuid;
    private final List<Element> subElementTypes = new ArrayList<>();
    protected int cooldown = 0;
    protected boolean canEat = true;

    public FoodBase(String modid, String name, int heal, float saturation) {
        super(heal, saturation, false);
        this.setTranslationKey(name);
        this.setRegistryName(new ResourceLocation(modid, name));
    }

    public FoodBase(String name, int heal, float saturation) {
        this(Reference.MODID, name, heal, saturation);
        this.setCreativeTab(Trinkets.CREATIVE_TAB);
    }

    @Override
    public boolean getHasSubtypes() {
        return super.getHasSubtypes() || !this.getElementTypes().isEmpty();
    }

    public List<Element> getElementTypes() {
        return this.subElementTypes;
    }

    public FoodBase addElementSubType(@Nonnull Element... elements) {
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

        for (int i = 1; i < 4; i++) {
            String key = stack.getItem() instanceof RaceFood ? (Reference.MODID + ".transformation.food.tooltip" + i) : (Reference.MODID + ".food.item.tooltip" + i);
            String food = new TextComponentTranslation(key).getFormattedText().trim();
            if (!food.isEmpty()) {
                tooltips.add(food);
            }
        }

        final TranslationHelper helper = TranslationHelper.INSTANCE;
        String translationKey = stack.getTranslationKey();
        for (int i = 1; i < 10; i++) {
            final int index = i;
            final String string = helper.getLangTranslation(translationKey + ".tooltip" + i, lang -> this.customItemInformation(stack, world, flagIn, index, lang));
            if (!helper.isStringEmpty(string)) {
                tooltips.add(string);
            }
        }

        //(showAdvEnabled ? EnumRenderLocation.ITEM_ADVANCED : EnumRenderLocation.ALWAYS)
        EnumRenderLocation modifier = GuiScreen.isCtrlKeyDown() ? EnumRenderLocation.ITEM_CTRL : GuiScreen.isShiftKeyDown() ? EnumRenderLocation.ITEM_SHIFT : GuiScreen.isAltKeyDown() ? EnumRenderLocation.ITEM_ALT : EnumRenderLocation.NEVER;
        if (showAdvEnabled && TrinketsConfig.CLIENT.ITEMS.RENDER_ELEMENTS) {
            tooltips.add(ConstantsTextTranslations.KEY_CTRL.getFormattedText());
            if (modifier.equals(EnumRenderLocation.ITEM_CTRL)) {
                tooltips.add(this.getPrimaryElement(stack).getDisplayName());
            }
        }
    }

    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        return super.getItemStackDisplayName(stack);
    }

    @Override
    public String getTranslationKey(@Nonnull ItemStack stack) {
        return this.getTranslationKey();
    }

    @Override
    public String getTranslationKey() {
        return super.getTranslationKey() + ".food";
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public void setUUID(String uuid) {
        if (!uuid.isEmpty()) {
            this.uuid = UUID.fromString(uuid);
        } else {
            this.uuid = UUID.randomUUID();
        }
    }

    @Override
    public void onUpdate(@Nonnull ItemStack stack, @Nonnull World worldIn, @Nonnull Entity entityIn, int itemSlot, boolean isSelected) {
        if (this.cooldown > 0) {
            this.canEat = false;
            this.cooldown--;
        } else {
            this.canEat = true;
        }
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World worldIn, @Nonnull EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, @Nonnull World worldIn, @Nonnull EntityLivingBase entityLiving) {
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    @Override
    public void onCreated(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull EntityPlayer player) {
        super.onCreated(stack, world, player);
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public boolean getEdible() {
        return this.canEat;
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
    public Element getPrimaryElement(ItemStack stack) {
        return Capabilities.getTrinketProperties(stack, this.getPrimaryElement(), (prop, e) -> prop.getElementalAttributes().getPrimaryElement());
    }

    @Override
    public NBTTagCompound getNBTShareTag(@Nonnull ItemStack stack) {
        return Capabilities.getTrinketProperties(stack, super.getNBTShareTag(stack), (prop, rtnTag) -> prop.saveToNBT(prop.getTag()));
    }

    @Override
    public void readNBTShareTag(@Nonnull ItemStack stack, NBTTagCompound nbt) {
        super.readNBTShareTag(stack, nbt);
        if (nbt != null) {
            Capabilities.getTrinketProperties(stack, (prop) -> prop.loadFromNBT(nbt));
        }
    }

    @Override
    public void registerModels() {
        Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
    }

    @Override
    public int getSlot(ItemStack stack) {
        return Capabilities.getTrinketProperties(stack, -1, (prop, slot) -> prop.getSlot());
    }

    @Override
    public String getItemHandler(ItemStack stack) {
        return Capabilities.getTrinketProperties(stack, TrinketHelper.SlotInformation.ItemHandlerType.NONE.getName(), (prop, slot) -> prop.getSlotInfo().getHandler());
    }

}