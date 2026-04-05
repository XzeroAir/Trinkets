package xzeroair.trinkets.capabilities;

import net.minecraft.entity.player.EntityPlayer;

public abstract class CapabilityEntityPlayerBase<T extends CapabilityEntityPlayerBase, E extends EntityPlayer> extends CapabilityEntityBase<T, E> {

    public CapabilityEntityPlayerBase(E object) {
        super(object);
    }

    public E getPlayer() {
        return this.getEntity();
    }

    @Override
    protected boolean isCreativePlayer() {
        return this.getPlayer().isCreative();
    }

    @Override
    protected boolean isSpectatorPlayer() {
        return this.getPlayer().isSpectator();
    }

}
