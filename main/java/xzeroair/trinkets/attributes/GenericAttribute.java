package xzeroair.trinkets.attributes;

import java.util.Collections;
import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.world.WorldServer;
import xzeroair.trinkets.network.HealthUpdatePacket;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.util.Reference;

public class GenericAttribute {
	private final String id = Reference.MODID + ".";
	private EntityLivingBase entity;
	private String name;
	private UUID uuid;
	private double amount;
	private int operation;
	private IAttribute attrib;

	public GenericAttribute(EntityLivingBase entity, double amount, UUID uuid, int operation, IAttribute attribute) {
		this.entity = entity;
		name = id + attribute.getName();
		this.uuid = uuid;
		this.amount = amount;
		this.operation = operation;
		attrib = attribute;

	}

	private AttributeModifier createModifier() {
		return new AttributeModifier(uuid, name, amount, operation);
	}

	public void addModifier() {
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(attrib);
		if ((AttributeInstance == null) || (uuid.compareTo(UUID.fromString("00000000-0000-0000-0000-000000000000")) == 0)) {
			return;
		}
		if ((AttributeInstance.getModifier(uuid) != null)) {
			AttributeModifier m = AttributeInstance.getModifier(uuid);
			if ((m.getAmount() != amount) || (m.getOperation() != operation)) {
				this.removeModifier();
			}
		}
		if (amount != 0) {
			if (AttributeInstance.getModifier(uuid) == null) {
				if (attrib == SharedMonsterAttributes.MAX_HEALTH) {
					final AttributeModifier modifier = this.createModifier();
					float Health = entity.getHealth();
					AttributeInstance.applyModifier(modifier);
					if (!entity.world.isRemote) {
						if (Health > AttributeInstance.getAttributeValue()) {
							if (entity instanceof EntityPlayerMP) {
								entity.setHealth(entity.getMaxHealth());
								NetworkHandler.INSTANCE.sendTo(new HealthUpdatePacket(entity), (EntityPlayerMP) entity);
							}
						}
					}
				} else {
					AttributeInstance.applyModifier(this.createModifier());
				}
				if ((entity != null) && !entity.getEntityWorld().isRemote) {
					final SPacketEntityProperties packet = new SPacketEntityProperties(entity.getEntityId(), Collections.singleton(AttributeInstance));
					((WorldServer) entity.getEntityWorld()).getEntityTracker().sendToTrackingAndSelf(entity, packet);
				}
			}
		}
	}

	public void removeModifier() {
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(attrib);
		if ((AttributeInstance == null) || (AttributeInstance.getModifier(uuid) == null)) {
			return;
		}
		if (attrib == SharedMonsterAttributes.MAX_HEALTH) {
			float health = entity.getHealth();
			AttributeInstance.removeModifier(uuid);
			if (health > AttributeInstance.getAttributeValue()) {
				if (!entity.world.isRemote) {
					if (entity instanceof EntityPlayerMP) {
						entity.setHealth(entity.getMaxHealth());
						NetworkHandler.INSTANCE.sendTo(new HealthUpdatePacket(entity), (EntityPlayerMP) entity);
					}
				}
			}
		} else {
			AttributeInstance.removeModifier(uuid);
		}
		if ((entity != null) && !entity.getEntityWorld().isRemote) {
			final SPacketEntityProperties packet = new SPacketEntityProperties(entity.getEntityId(), Collections.singleton(AttributeInstance));
			((WorldServer) entity.getEntityWorld()).getEntityTracker().sendToTrackingAndSelf(entity, packet);
		}
	}
}
