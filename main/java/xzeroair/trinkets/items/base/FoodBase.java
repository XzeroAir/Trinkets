package xzeroair.trinkets.items.base;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.api.ITrinketInterface;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.ElementalAttributes;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.traits.elements.IElementProvider;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class FoodBase extends ItemFood implements IsModelLoaded, ITrinketInterface, IElementProvider {

    private UUID uuid;
    protected int cooldown = 0;
    protected boolean canEat = true;
    protected ElementalAttributes elements;

    public FoodBase(String name, int heal, float saturation) {
        super(heal, saturation, false);
        this.setTranslationKey(name);
        this.setRegistryName(name);
        this.setCreativeTab(Trinkets.trinketstab);
        elements = new ElementalAttributes();
    }

    @SideOnly(Side.CLIENT)
    protected String customItemInformation(ItemStack stack, World world, ITooltipFlag flagIn, int index, String translation) {
        final TranslationHelper helper = TranslationHelper.INSTANCE;
        return helper.formatAddVariables(translation);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltips, ITooltipFlag flagIn) {
        super.addInformation(stack, world, tooltips, flagIn);
        if (world == null) {
            return;
        }
        final TranslationHelper helper = TranslationHelper.INSTANCE;
        for (int i = 1; i < 10; i++) {
            final int index = i;
            final String string = helper.getLangTranslation(stack.getTranslationKey() + ".tooltip" + i, lang -> this.customItemInformation(stack, world, flagIn, index, lang));
            if (!helper.isStringEmpty(string)) {
                tooltips.add(string);
            }
        }
        final TextComponentTranslation ctrl = new TextComponentTranslation(Reference.MODID + ".holdctrl");
        final boolean tanEnabled = (Trinkets.MOD_COMPAT.ToughAsNails && TrinketsConfig.getClientStore().MOD_COMPAT_TOUGHASNAILS);
        final boolean sdEnabled = (Trinkets.MOD_COMPAT.SimpleDifficulty && TrinketsConfig.getClientStore().MOD_COMPAT_SIMPLEDIFFICULTY);
        final boolean faEnabled = Trinkets.MOD_COMPAT.FirstAid;
        final boolean evEnabled = Trinkets.MOD_COMPAT.EnhancedVisuals && TrinketsConfig.getClientStore().MOD_COMPAT_ENHANCED_VISUALS;
        final String TAN = !(tanEnabled || sdEnabled) ? "" : helper.getLangTranslation(stack.getTranslationKey() + ".compat.tan", lang -> this.customItemInformation(stack, world, flagIn, 11, lang));
        final String FA = !faEnabled ? "" : helper.getLangTranslation(stack.getTranslationKey() + ".compat.firstaid", lang -> this.customItemInformation(stack, world, flagIn, 12, lang));
        final String EV = !evEnabled ? "" : helper.getLangTranslation(stack.getTranslationKey() + ".compat.enhancedvisuals", lang -> this.customItemInformation(stack, world, flagIn, 13, lang));
        if (GuiScreen.isCtrlKeyDown()) {
            if (!helper.isStringEmpty(TAN)) {
                final String modifier = sdEnabled ? " (Simple Difficulty)" : tanEnabled ? " (Tough as Nails)" : "";
                tooltips.add(TAN + helper.gold + modifier);
            }
            if (!helper.isStringEmpty(FA)) {
                tooltips.add(FA + helper.gold + " (First Aid)");
            }
            if (!helper.isStringEmpty(EV)) {
                tooltips.add(EV + helper.gold + " (Enhanced Visuals)");
            }
        } else if (GuiScreen.isShiftKeyDown()) {
            if (!stack.isEmpty() && stack.getItem() instanceof IElementProvider) {
                IElementProvider elementProvider = (IElementProvider) stack.getItem();
                tooltips.add(elementProvider.getPrimaryElement(stack).getDisplayName() + "");
            }
        } else {
            if ((!helper.isStringEmpty(TAN)) || (!helper.isStringEmpty(EV)) || (!helper.isStringEmpty(FA))) {
                tooltips.add(helper.reset + "" + helper.dGray + ctrl.getFormattedText());
            }
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return super.getItemStackDisplayName(stack);
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        if (!uuid.isEmpty()) {
            this.uuid = UUID.fromString(uuid);
        } else {
            this.uuid = UUID.randomUUID();
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (cooldown > 0) {
            canEat = false;
            cooldown--;
        } else {
            canEat = true;
        }
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player) {
        super.onCreated(stack, world, player);
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public boolean getEdible() {
        return canEat;
    }

    @Override
    public Element getPrimaryElement(ItemStack stack) {
        return Capabilities.getTrinketProperties(stack, this.getPrimaryElement(), (prop, element) -> prop.getElementAttributes().getPrimaryElement());
    }

    @Override
    public Element getSecondaryElement(ItemStack stack) {
        return Capabilities.getTrinketProperties(stack, this.getSecondaryElement(), (prop, element) -> prop.getElementAttributes().getSecondaryElement());
    }

    @Override
    public Element[] getSubElements(ItemStack stack) {
        return Capabilities.getTrinketProperties(stack, this.getSubElements(), (prop, element) -> prop.getElementAttributes().getSubElements().values().toArray(new Element[0]));
    }

    @Override
    public NBTTagCompound getNBTShareTag(@Nonnull ItemStack stack) {
        return Capabilities.getTrinketProperties(stack, super.getNBTShareTag(stack), (prop, tag) -> {
            if (tag == null) {
                tag = new NBTTagCompound();
            }
            return prop.saveToNBT(tag);
        });
    }

    @Override
    public void readNBTShareTag(@Nonnull ItemStack stack, NBTTagCompound nbt) {
        super.readNBTShareTag(stack, nbt);
        Capabilities.getTrinketProperties(stack, prop -> prop.loadFromNBT(nbt));
    }

    @Override
    public void registerModels() {
        Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
    }

    @Override
    public int getSlot(ItemStack stack) {
        return -1;
    }

    @Override
    public String getItemHandler(ItemStack stack) {
        return TrinketHelper.SlotInformation.ItemHandlerType.NONE.getName();
    }
}