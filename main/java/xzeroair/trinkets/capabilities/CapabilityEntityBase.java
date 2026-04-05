package xzeroair.trinkets.capabilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import xzeroair.trinkets.util.helpers.NBTHelper;

public abstract class CapabilityEntityBase<T extends CapabilityEntityBase, E extends EntityLivingBase> extends CapabilityBase<T, E> {

    public CapabilityEntityBase(E object) {
        super(object);
    }

    public E getEntity() {
        return this.getObject();
    }

    @Override
    public NBTTagCompound getTag() {
        return NBTHelper.getEntityTag(this.getEntity(), super.getTag());
    }

    protected boolean isCreativePlayer() {
        return this.getEntity() instanceof EntityPlayer && (((EntityPlayer) this.getEntity()).isCreative());
    }

    protected boolean isSpectatorPlayer() {
        return this.getEntity() instanceof EntityPlayer && (((EntityPlayer) this.getEntity()).isSpectator());
    }

    public void onUpdatePre() {

    }

    public abstract void onUpdate();

    public void onJoinWorld() {

    }

    public void onLogin() {

    }

    public void onLogoff() {

    }

    public void onChangedDimension(int from, int to) {

    }
}