package xzeroair.trinkets.attributes;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.compat.firstaid.FirstAidCompat;

import java.util.Collections;
import java.util.UUID;

public class UpdatingAttribute {

    public String modifierName;
    public String attribute;
    public UUID uuid;
    public double amount;
    public int operation;
    public boolean isSavedInNBT;

    public UpdatingAttribute(UUID uuid, IAttribute attribute) {
        this(Reference.MODID + "." + attribute.getName() + ".modifier", uuid, attribute.getName());
    }

    public UpdatingAttribute(String name, UUID uuid, IAttribute attribute) {
        this(name, uuid, attribute.getName());
    }

    public UpdatingAttribute(String name, UUID uuid, String attributeName) {
        this.modifierName = name;
        this.uuid = uuid;
        this.attribute = attributeName;
        this.isSavedInNBT = true;
        this.amount = 0;
        this.operation = 0;
    }

    public UpdatingAttribute setSavedInNBT(boolean isSavedInNBT) {
        this.isSavedInNBT = isSavedInNBT;
        return this;
    }

    public UpdatingAttribute setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public UpdatingAttribute setOperation(int operation) {
        this.operation = operation;
        return this;
    }

    private AttributeModifier createModifier(double amount, int operation) {
        if (!this.isSavedInNBT) {
            return new AttributeModifier(this.uuid, this.modifierName, amount, operation).setSaved(false);
        }
        return new AttributeModifier(this.uuid, this.modifierName, amount, operation);
    }

    public boolean isValidAttribute(EntityLivingBase entity) {
        if (entity != null) {
            final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstanceByName(this.attribute);
            return AttributeInstance != null;
        }
        return false;
    }

    public void addModifier(EntityLivingBase entity) {
        this.addModifier(entity, this.amount, this.operation);
    }

    public void addModifier(EntityLivingBase entity, double amount, int operation) {
        final World world = entity == null ? null : entity.getEntityWorld();
        if ((entity == null) || (world == null)) {
            return;
        }
        final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstanceByName(this.attribute);
        if ((AttributeInstance == null) || (this.uuid.compareTo(UUID.fromString("00000000-0000-0000-0000-000000000000")) == 0)) {
            return;
        }
        AttributeModifier existingModifier = AttributeInstance.getModifier(this.uuid);
        if (existingModifier != null) {
            if (amount == 0 || existingModifier.getAmount() != amount || existingModifier.getOperation() != operation) {
                this.removeModifier(entity);
                existingModifier = null;
            }
        }
        if (amount != 0 && existingModifier == null) {
            if (AttributeInstance.getAttribute() == SharedMonsterAttributes.MAX_HEALTH) {
                final AttributeModifier modifier = this.createModifier(amount, operation);
                //					float oldHealth = entity.getHealth();
                //					float oldMax = entity.getMaxHealth();
                AttributeInstance.applyModifier(modifier);
                //					entity.setHealth(oldHealth);
                //					float newMax = entity.getMaxHealth();
                //					float newHealth = entity.getHealth();
                //					float diff = newMax - oldMax;
                //					if (diff > 0) {
                //						entity.heal(diff);
                //					} else {
                //					}
                FirstAidCompat.rescale(entity);
                if (!world.isRemote) {
                    if (world instanceof WorldServer) {
                        final SPacketEntityProperties packet = new SPacketEntityProperties(entity.getEntityId(), Collections.singleton(AttributeInstance));
                        ((WorldServer) world).getEntityTracker().sendToTrackingAndSelf(entity, packet);
                    }
                }
            } else {
                AttributeInstance.applyModifier(this.createModifier(amount, operation));
            }
        }
    }

    public void removeModifier(EntityLivingBase entity) {
        final World world = entity == null ? null : entity.getEntityWorld();
        if (((entity == null) || (world == null))) {
            return;
        }
        final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstanceByName(this.attribute);
        if ((AttributeInstance == null) || (AttributeInstance.getModifier(this.uuid) == null)) {
            return;
        }
        if (AttributeInstance.getAttribute() == JumpAttribute.stepHeight) {
            if (entity.stepHeight != AttributeInstance.getBaseValue()) {
                entity.stepHeight = (float) AttributeInstance.getBaseValue();
            }
        }
        AttributeInstance.removeModifier(this.uuid);
    }
}
