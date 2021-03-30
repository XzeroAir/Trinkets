package xzeroair.trinkets.util.handlers;

import java.util.UUID;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import xzeroair.trinkets.attributes.AttributeConfigWrapper;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.attributes.UpdatingAttribute;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.interfaces.IAttributeConfigHelper;

public class ItemAttributeHandler {

	private UpdatingAttribute armor;
	private UpdatingAttribute attackDamage;
	private UpdatingAttribute attackSpeed;
	private UpdatingAttribute health;
	private UpdatingAttribute knockback;
	private UpdatingAttribute movementSpeed;
	private UpdatingAttribute armorToughness;
	private UpdatingAttribute swimSpeed;
	private UpdatingAttribute luck;
	private UpdatingAttribute reach;
	private UpdatingAttribute jump;
	private UpdatingAttribute stepheight;
	private AttributeConfigWrapper attributes;

	public ItemAttributeHandler() {
		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
		this.setupHandler(uuid, new AttributeConfigWrapper());
	}

	public ItemAttributeHandler(UUID uuid, IAttributeConfigHelper attributeConfig) {
		this.setupHandler(uuid, attributeConfig);
	}

	public ItemAttributeHandler(UUID uuid, AttributeConfigWrapper attributeConfig) {
		this.setupHandler(uuid, attributeConfig);
	}

	private void setupHandler(UUID uuid, IAttributeConfigHelper attributeConfig) {
		this.setupHandler(uuid, new AttributeConfigWrapper(attributeConfig));
	}

	private void setupHandler(UUID uuid, AttributeConfigWrapper attributeConfig) {
		armor = new UpdatingAttribute(uuid, SharedMonsterAttributes.ARMOR);
		attackDamage = new UpdatingAttribute(uuid, SharedMonsterAttributes.ATTACK_DAMAGE);
		attackSpeed = new UpdatingAttribute(uuid, SharedMonsterAttributes.ATTACK_SPEED);
		health = new UpdatingAttribute(uuid, SharedMonsterAttributes.MAX_HEALTH);
		knockback = new UpdatingAttribute(uuid, SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
		movementSpeed = new UpdatingAttribute(uuid, SharedMonsterAttributes.MOVEMENT_SPEED);
		armorToughness = new UpdatingAttribute(uuid, SharedMonsterAttributes.ARMOR_TOUGHNESS);
		swimSpeed = new UpdatingAttribute(uuid, EntityLivingBase.SWIM_SPEED);
		luck = new UpdatingAttribute(uuid, SharedMonsterAttributes.LUCK);
		reach = new UpdatingAttribute(uuid, EntityPlayer.REACH_DISTANCE);
		jump = new UpdatingAttribute(uuid, JumpAttribute.Jump);
		stepheight = new UpdatingAttribute(uuid, JumpAttribute.stepHeight);
		attributes = attributeConfig;
	}

	private void setArmor(EntityLivingBase entity) {
		if (attributes.armorEnabled()) {
			final double amount = attributes.getArmor();
			final int operation = attributes.getArmorOperation();
			armor.addModifier(entity, amount, operation);
		} else {
			armor.removeModifier(entity);
		}
	}

	private void setAttackDamage(EntityLivingBase entity) {
		if (attributes.attackDamageEnabled()) {
			final double amount = attributes.getAttackDamage();
			final int operation = attributes.getAttackDamageOperation();
			attackDamage.addModifier(entity, amount, operation);
		} else {
			attackDamage.removeModifier(entity);
		}
	}

	private void setAttackSpeed(EntityLivingBase entity) {
		if (attributes.attackSpeedEnabled()) {
			final double amount = attributes.getAttackSpeed();
			final int operation = attributes.getAttackSpeedOperation();
			attackSpeed.addModifier(entity, amount, operation);
		} else {
			attackSpeed.removeModifier(entity);
		}
	}

	private void setHealth(EntityLivingBase entity) {
		if (attributes.healthEnabled()) {
			final double amount = attributes.getHealth();
			final int operation = attributes.getHealthOperation();
			health.addModifier(entity, amount, operation);
		} else {
			health.removeModifier();
		}
	}

	private void setKnockback(EntityLivingBase entity) {
		if (attributes.knockbackEnabled()) {
			final double amount = attributes.getKnockback();
			final int operation = attributes.getKnockbackOperation();
			knockback.addModifier(entity, amount, operation);
		} else {
			knockback.removeModifier(entity);
		}
	}

	private void setMovementSpeed(EntityLivingBase entity) {
		if (attributes.movementSpeedEnabled()) {
			final double amount = attributes.getMovementSpeed();
			final int operation = attributes.getMovementSpeedOperation();
			movementSpeed.addModifier(entity, amount, operation);
		} else {
			movementSpeed.removeModifier(entity);
		}
	}

	private void setArmorToughness(EntityLivingBase entity) {
		if (attributes.armorToughnessEnabled()) {
			final double amount = attributes.getArmorToughness();
			final int operation = attributes.getArmorToughnessOperation();
			armorToughness.addModifier(entity, amount, operation);
		} else {
			armorToughness.removeModifier(entity);
		}
	}

	private void setSwimSpeed(EntityLivingBase entity) {
		boolean flag1 = EnchantmentHelper.getDepthStriderModifier(entity) > 0;
		boolean flag2 = TrinketsConfig.SERVER.misc.depthStacks;
		boolean flag3 = flag2 ? true : !flag1;
		boolean flag4 = (entity instanceof EntityPlayer) && ((EntityPlayer) entity).capabilities.isFlying;
		if (attributes.swimSpeedEnabled() && flag3 && !flag4) {
			final double amount = attributes.getSwimSpeed();
			final int operation = attributes.getSwimSpeedOperation();
			swimSpeed.addModifier(entity, amount, operation);
		} else {
			swimSpeed.removeModifier();
		}
	}

	private void setLuck(EntityLivingBase entity) {
		if (attributes.luckEnabled()) {
			final double amount = attributes.getLuck();
			final int operation = attributes.getLuckOperation();
			luck.addModifier(entity, amount, operation);
		} else {
			luck.removeModifier(entity);
		}
	}

	private void setReach(EntityLivingBase entity) {
		if (attributes.reachEnabled()) {
			final double amount = attributes.getReach();
			final int operation = attributes.getReachOperation();
			reach.addModifier(entity, amount, operation);
		} else {
			reach.removeModifier(entity);
		}
	}

	private void setJump(EntityLivingBase entity) {
		if (attributes.jumpEnabled()) {
			final double amount = attributes.getJump();
			final int operation = attributes.getJumpOperation();
			jump.addModifier(entity, amount, operation);
		} else {
			jump.removeModifier(entity);
		}
	}

	private void setStepHeight(EntityLivingBase entity) {
		if (attributes.stepHeightEnabled()) {
			final double amount = attributes.getStepHeight();
			final int operation = attributes.getStepHeightOperation();
			stepheight.addModifier(entity, amount, operation);
		} else {
			stepheight.removeModifier();
		}
	}

	public void addAttributes(EntityLivingBase entity) {
		this.setArmor(entity);
		this.setArmorToughness(entity);
		this.setAttackDamage(entity);
		this.setAttackSpeed(entity);
		this.setHealth(entity);
		this.setKnockback(entity);
		this.setMovementSpeed(entity);
		this.setSwimSpeed(entity);
		this.setLuck(entity);
		if (entity instanceof EntityPlayer) {
			this.setReach(entity);
		}
		this.setJump(entity);
		this.setStepHeight(entity);
	}

	public void removeAttibutes(EntityLivingBase entity) {
		armor.removeModifier(entity);
		attackDamage.removeModifier(entity);
		attackSpeed.removeModifier(entity);
		health.removeModifier(entity);
		knockback.removeModifier(entity);
		movementSpeed.removeModifier(entity);
		armorToughness.removeModifier(entity);
		swimSpeed.removeModifier(entity);
		luck.removeModifier(entity);
		if (entity instanceof EntityPlayer) {
			reach.removeModifier(entity);
		}
		jump.removeModifier(entity);
		stepheight.removeModifier(entity);
	}

	//	public void removeAttribute() {
	//
	//	}

}
