package xzeroair.trinkets.capabilities.elements;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import xzeroair.trinkets.util.helpers.NBTHelper;

public class EntityElementalAttributes extends ElementalAttributes<EntityElementalAttributes, EntityLivingBase> {

    public EntityElementalAttributes(EntityLivingBase entity) {
        super(entity);
    }

    public EntityLivingBase getEntity() {
        return this.getObject();
    }

    @Override
    public NBTTagCompound getTag() {
        NBTTagCompound tag = NBTHelper.getEntityTag(this.getEntity(), super.getTag());
        if (!tag.hasKey(this.TAG_KEY)) {
            tag.setTag(this.TAG_KEY, new NBTTagCompound());
        }
        return tag.getCompoundTag(this.TAG_KEY);
    }

    protected boolean isCreativePlayer() {
        return this.getEntity() instanceof EntityPlayer && (((EntityPlayer) this.getEntity()).isCreative());
    }

    protected boolean isSpectatorPlayer() {
        return this.getEntity() instanceof EntityPlayer && (((EntityPlayer) this.getEntity()).isSpectator());
    }

    public void onUpdatePre() {

    }

    public void onUpdate() {

    }

    public void onJoinWorld() {

    }

    public void onLogin() {

    }

    public void onLogoff() {

    }

    public void onChangedDimension(int from, int to) {

    }
}