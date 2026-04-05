package xzeroair.trinkets.capabilities.InventoryContainerCapability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import xzeroair.trinkets.container.TrinketContainerHandler;

import javax.annotation.Nonnull;

public class TrinketContainerProvider implements ICapabilitySerializable<NBTTagCompound> {

    @CapabilityInject(ITrinketContainerHandler.class)
    public static final Capability<ITrinketContainerHandler> containerCap = null;

    private final TrinketContainerHandler container;

    public TrinketContainerProvider(TrinketContainerHandler container) {
        this.container = container;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
        return capability == containerCap;
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        if (capability == containerCap) {
            return (T) this.container;
        }
        return null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return this.container.serializeNBT();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.container.deserializeNBT(nbt);
    }
}