package xzeroair.trinkets.attributes;

import java.util.Collections;
import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.world.WorldServer;
import xzeroair.trinkets.util.Reference;

public class UpdatingAttribute {
	private final String id = Reference.MODID + ".";
	private EntityLivingBase entity;
	private String name;
	private String attribute;
	private UUID uuid;
	private double amount;
	private int operation;
	private boolean isSavedInNBT;

	public UpdatingAttribute(EntityLivingBase entity, UUID uuid, IAttribute attribute) {
		this(uuid, attribute);
		this.entity = entity;
	}

	public UpdatingAttribute(UUID uuid, IAttribute attribute) {
		this(Reference.MODID + "." + attribute.getName(), uuid, attribute.getName());
	}

	public UpdatingAttribute(String name, UUID uuid, String attributeName) {
		this.name = name;
		this.uuid = uuid;
		attribute = attributeName;
		isSavedInNBT = true;
	}

	public UpdatingAttribute setSavedInNBT(boolean isSavedInNBT) {
		this.isSavedInNBT = isSavedInNBT;
		return this;
	}

	private AttributeModifier createModifier(double amount, int operation) {
		if (!isSavedInNBT) {
			return new AttributeModifier(uuid, name, amount, operation).setSaved(false);
		}
		return new AttributeModifier(uuid, name, amount, operation);
	}

	public void addModifier(EntityLivingBase entity, double amount, int operation) {
		this.entity = entity;
		this.addModifier(amount, operation);
	}

	public void addModifier(double amount, int operation) {
		if (entity == null) {
			return;
		}
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstanceByName(attribute);//.getAttributeMap().getAttributeInstance(attrib);
		if ((AttributeInstance == null) || (uuid.compareTo(UUID.fromString("00000000-0000-0000-0000-000000000000")) == 0)) {
			return;
		}
		if ((AttributeInstance.getModifier(uuid) != null)) {
			final AttributeModifier m = AttributeInstance.getModifier(uuid);
			if ((m.getAmount() != amount) || (m.getOperation() != operation)) {
				this.removeModifier();
			}
		}
		if (amount != 0) {
			if (AttributeInstance.getModifier(uuid) == null) {
				if (AttributeInstance.getAttribute() == SharedMonsterAttributes.MAX_HEALTH) {
					final AttributeModifier modifier = this.createModifier(amount, operation);
					final float Health = entity.getHealth();
					AttributeInstance.applyModifier(modifier);
					//					if (!entity.world.isRemote) {
					//					if (Health > AttributeInstance.getAttributeValue()) {
					//						if (entity instanceof EntityPlayerMP) {
					//							entity.setHealth(entity.getMaxHealth());
					//							//							System.out.println("Sending?");
					//							NetworkHandler.INSTANCE.sendTo(new HealthUpdatePacket(entity), (EntityPlayerMP) entity);
					//						}
					//					}
					if ((entity != null) && !entity.getEntityWorld().isRemote) {
						if (entity.getEntityWorld() instanceof WorldServer) {
							final SPacketEntityProperties packet = new SPacketEntityProperties(entity.getEntityId(), Collections.singleton(AttributeInstance));
							((WorldServer) entity.getEntityWorld()).getEntityTracker().sendToTrackingAndSelf(entity, packet);
						}
					}
					//					}
				} else {
					AttributeInstance.applyModifier(this.createModifier(amount, operation));
				}
				//				if ((entity != null) && !entity.getEntityWorld().isRemote) {
				//					if (entity.getEntityWorld() instanceof WorldServer) {
				//						final SPacketEntityProperties packet = new SPacketEntityProperties(entity.getEntityId(), Collections.singleton(AttributeInstance));
				//						((WorldServer) entity.getEntityWorld()).getEntityTracker().sendToTrackingAndSelf(entity, packet);
				//					}
				//				}
			}
		}
	}

	public void removeModifier() {
		if (entity == null) {
			return;
		}
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstanceByName(attribute);//.getAttributeMap().getAttributeInstance(attrib);
		if ((AttributeInstance == null) || (AttributeInstance.getModifier(uuid) == null)) {
			return;
		}
		if (AttributeInstance.getAttribute() == SharedMonsterAttributes.MAX_HEALTH) {
			final float health = entity.getHealth();
			AttributeInstance.removeModifier(uuid);
			if (health > AttributeInstance.getAttributeValue()) {
				//				if (!entity.world.isRemote) {
				//					if (entity instanceof EntityPlayerMP) {
				entity.setHealth(entity.getMaxHealth());
				//						NetworkHandler.INSTANCE.sendTo(new HealthUpdatePacket(entity), (EntityPlayerMP) entity);
				//			}
				//							}
			}
		} else {
			if (AttributeInstance.getAttribute() == JumpAttribute.stepHeight) {
				if (entity.stepHeight != AttributeInstance.getBaseValue()) {
					entity.stepHeight = (float) AttributeInstance.getBaseValue();
				}
			}
			AttributeInstance.removeModifier(uuid);
		}
		//		if ((entity != null) && !entity.getEntityWorld().isRemote) {
		//			final SPacketEntityProperties packet = new SPacketEntityProperties(entity.getEntityId(), Collections.singleton(AttributeInstance));
		//			((WorldServer) entity.getEntityWorld()).getEntityTracker().sendToTrackingAndSelf(entity, packet);
		//		}
	}

	public void removeModifier(EntityLivingBase entity) {
		this.entity = entity;
		this.removeModifier();
	}
}
