package xzeroair.trinkets.util.compat.artemislib;

import java.util.UUID;

import com.artemis.artemislib.util.attributes.ArtemisLibAttributes;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import xzeroair.trinkets.util.Reference;

public class SizeAttribute {
	private static final String id = Reference.MODID + ".";
	private static String name = "Size";
	protected static final UUID uuid = UUID.fromString("02a31a03-4d5b-46ed-947b-322c4f004a75");
	protected static final UUID uuidW = UUID.fromString("4ff77810-5a91-44ce-8e93-fc05a8d00def");
	private static double amount;
	private static int operation;

	private static double getAmount() {
		return amount;
	}

	private static void setAmount(double amount) {
		SizeAttribute.amount = amount;
	}

	protected static AttributeModifier createModifier(UUID uuid) {
		return new AttributeModifier(uuid, id + name, getAmount(), getOperation());
	}

	private static int getOperation() {
		return operation;
	}

	private static void setOperation(int operation) {
		SizeAttribute.operation = operation;
	}

	public static void addModifier(EntityLivingBase entity, double height, double width, int operation) {
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(ArtemisLibAttributes.ENTITY_HEIGHT);
		final IAttributeInstance AttributeInstanceW = entity.getAttributeMap().getAttributeInstance(ArtemisLibAttributes.ENTITY_WIDTH);
		if ((AttributeInstance == null) || (AttributeInstanceW == null)) {
			return;
		}
		double clampH = -0.75;
		double clampW = -0.75;
		if (entity instanceof EntityPlayer) {
			clampH = MathHelper.clamp(height, -0.75, 1024.0D);
			clampW = MathHelper.clamp(width, -0.5, 1024.0D);
		} else {
			clampW = MathHelper.clamp(width, -0.55, 1024.0D);
		}
		if (((AttributeInstance.getModifier(uuid) != null) && (AttributeInstance.getModifier(uuid).getAmount() != clampH)) ||
				((AttributeInstanceW.getModifier(uuidW) != null) && (AttributeInstanceW.getModifier(uuidW).getAmount() != clampW)) ||
				(getOperation() != operation)) {
			removeModifier(entity);
		}
		setOperation(operation);
		if ((AttributeInstance.getModifier(uuid) == null) && (clampH != 0)) {
			setAmount(clampH);
			AttributeInstance.applyModifier(createModifier(uuid));
		}
		if ((AttributeInstanceW.getModifier(uuidW) == null) && (clampW != 0)) {
			setAmount(clampW);
			AttributeInstanceW.applyModifier(createModifier(uuidW));
		}
	}

	public static void removeModifier(EntityLivingBase entity) {
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(ArtemisLibAttributes.ENTITY_HEIGHT);
		final IAttributeInstance AttributeInstanceW = entity.getAttributeMap().getAttributeInstance(ArtemisLibAttributes.ENTITY_WIDTH);
		if ((AttributeInstance == null) || (AttributeInstanceW == null)) {
			return;
		}
		if (AttributeInstance.getModifier(uuid) != null) {
			AttributeInstance.removeModifier(uuid);
		}
		if (AttributeInstanceW.getModifier(uuidW) != null) {
			AttributeInstanceW.removeModifier(uuidW);
		}
	}
}
