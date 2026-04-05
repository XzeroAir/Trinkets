package xzeroair.trinkets.capabilities;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import xzeroair.trinkets.util.helpers.NBTHelper;

public abstract class CapabilityItemStackBase<T extends CapabilityItemStackBase, E extends ItemStack> extends CapabilityBase<T, E> {

    public CapabilityItemStackBase(E object) {
        super(object);
    }

    @Override
    public NBTTagCompound getTag() {
        return NBTHelper.getTagCompoundSafe(this.getItemStack());
    }

    public ItemStack getItemStack() {
        return this.getObject();
    }

    public Item getItem() {
        return this.getItemStack().getItem();
    }
}