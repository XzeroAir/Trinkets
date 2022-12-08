package xzeroair.trinkets.attributes;

import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.world.World;
import xzeroair.trinkets.util.Reference;

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

	public UpdatingAttribute(String name, UUID uuid, String attributeName) {
		modifierName = name;
		this.uuid = uuid;
		attribute = attributeName;
		isSavedInNBT = true;
		amount = 0;
		operation = 0;
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
		if (!isSavedInNBT) {
			return new AttributeModifier(uuid, modifierName, amount, operation).setSaved(false);
		}
		return new AttributeModifier(uuid, modifierName, amount, operation);
	}

	public boolean isValidAttribute(EntityLivingBase entity) {
		if (entity != null) {
			final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstanceByName(attribute);
			return AttributeInstance != null;
		}
		return false;
	}

	public void addModifier(EntityLivingBase entity) {
		this.addModifier(entity, amount, operation);
	}

	public void addModifier(EntityLivingBase entity, double amount, int operation) {
		final World world = entity == null ? null : entity.getEntityWorld();
		if (((entity == null) || (world == null)) || (world.isRemote)) {
			return;
		}
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstanceByName(attribute);//.getAttributeMap().getAttributeInstance(attrib);
		if ((AttributeInstance == null) || (uuid.compareTo(UUID.fromString("00000000-0000-0000-0000-000000000000")) == 0)) {
			return;
		}
		if ((AttributeInstance.getModifier(uuid) != null)) {
			final AttributeModifier m = AttributeInstance.getModifier(uuid);
			if ((m.getAmount() != amount) || (m.getOperation() != operation)) {
				this.removeModifier(entity);
			}
			if (entity.isDead) {
				this.removeModifier(entity);
				return;
			}
		}
		if ((amount != 0) && !entity.isDead) {
			if (AttributeInstance.getModifier(uuid) == null) {
				if (AttributeInstance.getAttribute() == SharedMonsterAttributes.MAX_HEALTH) {
					final AttributeModifier modifier = this.createModifier(amount, operation);
					float oldHealth = entity.getHealth();
					System.out.println(amount);
					//					float oldMax = entity.getMaxHealth();
					AttributeInstance.applyModifier(modifier);
					//					float newMax = entity.getMaxHealth();
					//					float newHealth = entity.getHealth();
					//					float diff = newMax - newHealth;
					//					entity.setHealth(oldHealth);
					//					//					entity.setHealth((oldHealth + diff) + 1);
					//					//					System.out.println(diff);
					//					//					entity.heal(diff);
					//					//					System.out.println(oldHealth + "|" + newHealth + " | " + oldMax + "|" + newMax + " | " + beforeSet);
					//
					//					final float amountToHeal = newMax - oldMax;
					//					if (amountToHeal > 0) {
					//						entity.heal(amountToHeal);
					//					}
					//					if (!entity.getEntityWorld().isRemote) {
					//						if (entity.getEntityWorld() instanceof WorldServer) {
					//							final SPacketEntityProperties packet = new SPacketEntityProperties(entity.getEntityId(), Collections.singleton(AttributeInstance));
					//							((WorldServer) entity.getEntityWorld()).getEntityTracker().sendToTrackingAndSelf(entity, packet);
					//						}
					//					}
				} else {
					AttributeInstance.applyModifier(this.createModifier(amount, operation));
				}
			}
		}

	}

	public void removeModifier(EntityLivingBase entity) {
		final World world = entity == null ? null : entity.getEntityWorld();
		if (((entity == null) || (world == null)) || (world.isRemote)) {
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
				entity.setHealth(entity.getMaxHealth());
			}
		} else {
			if (AttributeInstance.getAttribute() == JumpAttribute.stepHeight) {
				if (entity.stepHeight != AttributeInstance.getBaseValue()) {
					entity.stepHeight = (float) AttributeInstance.getBaseValue();
				}
			}
			AttributeInstance.removeModifier(uuid);
		}
	}
}
