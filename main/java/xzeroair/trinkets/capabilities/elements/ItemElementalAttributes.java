package xzeroair.trinkets.capabilities.elements;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.helpers.NBTHelper;

public class ItemElementalAttributes extends ElementalAttributes<ItemElementalAttributes, ItemStack> {

    public ItemElementalAttributes(Item item) {
        this(new ItemStack(item, 1, 0));
    }

    public ItemElementalAttributes(ItemStack stack) {
        super(stack);
    }

    @Override
    public NBTTagCompound getTag() {
        NBTTagCompound tag = NBTHelper.getTagCompoundSafe(this.getItemStack());
        if (!tag.hasKey(this.TAG_KEY)) {
            tag.setTag(this.TAG_KEY, new NBTTagCompound());
        }
        return tag.getCompoundTag(this.TAG_KEY);
    }

    public ItemStack getItemStack() {
        return this.getObject();
    }

    public Item getItem() {
        return this.getItemStack().getItem();
    }

    @Override
    public ItemElementalAttributes setPrimaryElement(Element element) {
        super.setPrimaryElement(element);
        return this;
    }

    @Override
    public ItemElementalAttributes setSecendaryElement(Element element) {
        super.setSecendaryElement(element);
        return this;
    }

    @Override
    public ItemElementalAttributes setTemporaryElement(Element element) {
        super.setTemporaryElement(element);
        return this;
    }

    @Override
    public ItemElementalAttributes saveNBT() {
        super.saveNBT();
        return this;
    }
}