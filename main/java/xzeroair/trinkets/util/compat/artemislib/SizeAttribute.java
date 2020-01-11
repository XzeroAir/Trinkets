package xzeroair.trinkets.util.compat.artemislib;

import java.util.UUID;

import com.artemis.artemislib.util.attributes.ArtemisLibAttributes;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.math.MathHelper;
import xzeroair.trinkets.util.Reference;

public class SizeAttribute {
	private static final String id = Reference.MODID + ".";
	private static String name = "Size";
	protected static final UUID uuid = UUID.fromString("02a31a03-4d5b-46ed-947b-322c4f004a75");
	protected static final UUID uuidW = UUID.fromString("4ff77810-5a91-44ce-8e93-fc05a8d00def");
	private static double bonus;
	private static int operation;
	private static double getBonus() {
		return bonus;
	}
	private static void setBonus(double bonus) {
		SizeAttribute.bonus = MathHelper.clamp(bonus, -0.75D, 1024.0D);
	}
	protected static AttributeModifier createModifier(UUID uuid) {
		return new AttributeModifier(uuid, id+name, getBonus(), getOperation());
	}
	private static int getOperation() {
		return operation;
	}
	private static void setOperation(int operation) {
		operation = Reference.getOpModifier(operation);
	}
	public static void addModifier(EntityLivingBase entity, double height, double width, int operation) {
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(ArtemisLibAttributes.ENTITY_HEIGHT);
		final IAttributeInstance AttributeInstanceW = entity.getAttributeMap().getAttributeInstance(ArtemisLibAttributes.ENTITY_WIDTH);
		if((AttributeInstanceW.getModifier(uuidW) != null) || (AttributeInstance.getModifier(uuid) != null)) {
			if(!(((AttributeInstance.getModifier(uuid)).getAmount() != height) || (AttributeInstanceW.getModifier(uuidW).getAmount() == (width)))) {
				removeModifier(entity);
			}
		}
		setOperation(operation);
		if((AttributeInstance.getModifier(uuid) == null) && (height != 0)) {
			setBonus(height);
			AttributeInstance.applyModifier(createModifier(uuid));
		}
		if((AttributeInstanceW.getModifier(uuidW) == null) && (width != 0)) {
			setBonus(width);
			AttributeInstanceW.applyModifier(createModifier(uuidW));
		}
	}
	public static void removeModifier(EntityLivingBase entity) {
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(ArtemisLibAttributes.ENTITY_HEIGHT);
		final IAttributeInstance AttributeInstanceW = entity.getAttributeMap().getAttributeInstance(ArtemisLibAttributes.ENTITY_WIDTH);
		if(AttributeInstance.getModifier(uuid) != null) {
			AttributeInstance.removeModifier(uuid);
		}
		if(AttributeInstanceW.getModifier(uuidW) != null) {
			AttributeInstanceW.removeModifier(uuidW);
		}
	}
}
