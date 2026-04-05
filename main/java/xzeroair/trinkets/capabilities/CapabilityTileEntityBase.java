package xzeroair.trinkets.capabilities;

import net.minecraft.tileentity.TileEntity;

public abstract class CapabilityTileEntityBase<T extends CapabilityTileEntityBase, E extends TileEntity> extends CapabilityBase<T, E> {

    public CapabilityTileEntityBase(E object) {
        super(object);
    }

    public TileEntity getTileEntity() {
        return this.getObject();
    }

    public abstract void onUpdate();

}