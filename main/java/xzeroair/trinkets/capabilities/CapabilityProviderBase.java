package xzeroair.trinkets.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityProviderBase<Handler extends CapabilityBase<Handler, E>, E> implements ICapabilitySerializable<NBTBase> {

    protected final Capability<Handler> capability;
    protected final EnumFacing facing;
    protected final Handler handler;

    public CapabilityProviderBase(final Capability<Handler> capability, @Nullable final Handler handler) {
        this(capability, null, handler);
    }

    public CapabilityProviderBase(final Capability<Handler> capability, @Nullable final EnumFacing facing, @Nullable final Handler handler) {
        this.capability = capability;
        this.facing = facing;
        this.handler = handler;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
        return capability == this.getCapability();
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        if (capability == this.getCapability()) {
            return this.getCapability().cast(this.getInstance());
        }
        return null;
    }

    @Nullable
    @Override
    public NBTBase serializeNBT() {
        return this.getCapability().writeNBT(this.getInstance(), this.getFacing());
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        this.getCapability().readNBT(this.getInstance(), this.getFacing(), nbt);
    }

    public final Capability<Handler> getCapability() {
        return this.capability;
    }

    @Nullable
    public EnumFacing getFacing() {
        return this.facing;
    }

    @Nullable
    public Handler getInstance() {
        return this.handler;
    }

}
