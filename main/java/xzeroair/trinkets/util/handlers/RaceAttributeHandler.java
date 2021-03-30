package xzeroair.trinkets.util.handlers;

import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.attributes.UpdatingAttribute;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.races.RaceAttributesWrapper;
import xzeroair.trinkets.util.interfaces.IAttributeConfigHelper;

public class RaceAttributeHandler {

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
	private UpdatingAttribute stepHeight;
	private RaceAttributesWrapper attributes;

	public RaceAttributeHandler(EntityLivingBase entity, EntityRace race) {
		//		, DefaultRaceAttributes attributes, UUID uuid
		armor = new UpdatingAttribute(entity, race.getUUID(), SharedMonsterAttributes.ARMOR);
		attackDamage = new UpdatingAttribute(entity, race.getUUID(), SharedMonsterAttributes.ATTACK_DAMAGE);
		attackSpeed = new UpdatingAttribute(entity, race.getUUID(), SharedMonsterAttributes.ATTACK_SPEED);
		health = new UpdatingAttribute(entity, race.getUUID(), SharedMonsterAttributes.MAX_HEALTH);
		knockback = new UpdatingAttribute(entity, race.getUUID(), SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
		movementSpeed = new UpdatingAttribute(entity, race.getUUID(), SharedMonsterAttributes.MOVEMENT_SPEED);
		armorToughness = new UpdatingAttribute(entity, race.getUUID(), SharedMonsterAttributes.ARMOR_TOUGHNESS);
		swimSpeed = new UpdatingAttribute(entity, race.getUUID(), EntityLivingBase.SWIM_SPEED);
		luck = new UpdatingAttribute(entity, race.getUUID(), SharedMonsterAttributes.LUCK);
		reach = new UpdatingAttribute(entity, race.getUUID(), EntityPlayer.REACH_DISTANCE);
		jump = new UpdatingAttribute(entity, race.getUUID(), JumpAttribute.Jump);
		stepHeight = new UpdatingAttribute(entity, race.getUUID(), JumpAttribute.stepHeight);
		attributes = race.getRaceAttributes();
	}

	public RaceAttributeHandler(UUID uuid, IAttributeConfigHelper attributeConfig) {
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
		//Custom
		jump = new UpdatingAttribute(uuid, JumpAttribute.Jump);
		stepHeight = new UpdatingAttribute(uuid, JumpAttribute.stepHeight);
		//
		attributes = new RaceAttributesWrapper(attributeConfig);
	}

	//	this.setItemAttributes(serverConfig.Attributes);

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
		if (attributes.swimSpeedEnabled()) {
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
			stepHeight.addModifier(entity, amount, operation);
		} else {
			stepHeight.removeModifier();
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
		stepHeight.removeModifier(entity);
	}

	private void setArmor() {
		if (attributes.armorEnabled()) {
			final double amount = attributes.getArmor();
			final int operation = attributes.getArmorOperation();
			armor.addModifier(amount, operation);
		} else {
			armor.removeModifier();
		}
	}

	private void setAttackDamage() {
		if (attributes.attackDamageEnabled()) {
			final double amount = attributes.getAttackDamage();
			final int operation = attributes.getAttackDamageOperation();
			attackDamage.addModifier(amount, operation);
		} else {
			attackDamage.removeModifier();
		}
	}

	private void setAttackSpeed() {
		if (attributes.attackSpeedEnabled()) {
			final double amount = attributes.getAttackSpeed();
			final int operation = attributes.getAttackSpeedOperation();
			attackSpeed.addModifier(amount, operation);
		} else {
			attackSpeed.removeModifier();
		}
	}

	private void setHealth() {
		if (attributes.healthEnabled()) {
			final double amount = attributes.getHealth();
			final int operation = attributes.getHealthOperation();
			health.addModifier(amount, operation);
		} else {
			health.removeModifier();
		}
	}

	private void setHealth(double percent) {
		if (attributes.healthEnabled()) {
			final double amount = percent * attributes.getHealth();
			final int operation = attributes.getHealthOperation();
			health.addModifier(amount, operation);
		} else {
			health.removeModifier();
		}
	}

	private void setKnockback() {
		if (attributes.knockbackEnabled()) {
			final double amount = attributes.getKnockback();
			final int operation = attributes.getKnockbackOperation();
			knockback.addModifier(amount, operation);
		} else {
			knockback.removeModifier();
		}
	}

	private void setMovementSpeed() {
		if (attributes.movementSpeedEnabled()) {
			final double amount = attributes.getMovementSpeed();
			final int operation = attributes.getMovementSpeedOperation();
			movementSpeed.addModifier(amount, operation);
		} else {
			movementSpeed.removeModifier();
		}
	}

	private void setArmorToughness() {
		if (attributes.armorToughnessEnabled()) {
			final double amount = attributes.getArmorToughness();
			final int operation = attributes.getArmorToughnessOperation();
			armorToughness.addModifier(amount, operation);
		} else {
			armorToughness.removeModifier();
		}
	}

	private void setSwimSpeed() {
		if (attributes.swimSpeedEnabled()) {
			final double amount = attributes.getSwimSpeed();
			final int operation = attributes.getSwimSpeedOperation();
			swimSpeed.addModifier(amount, operation);
		} else {
			swimSpeed.removeModifier();
		}
	}

	private void setLuck() {
		if (attributes.luckEnabled()) {
			final double amount = attributes.getLuck();
			final int operation = attributes.getLuckOperation();
			luck.addModifier(amount, operation);
		} else {
			luck.removeModifier();
		}
	}

	private void setReach() {
		if (attributes.reachEnabled()) {
			final double amount = attributes.getReach();
			final int operation = attributes.getReachOperation();
			reach.addModifier(amount, operation);
		} else {
			reach.removeModifier();
		}
	}

	private void setJump() {
		if (attributes.jumpEnabled()) {
			final double amount = attributes.getJump();
			final int operation = attributes.getJumpOperation();
			jump.addModifier(amount, operation);
		} else {
			jump.removeModifier();
		}
	}

	private void setStepHeight() {
		if (attributes.stepHeightEnabled()) {
			final double amount = attributes.getStepHeight();
			final int operation = attributes.getStepHeightOperation();
			stepHeight.addModifier(amount, operation);
		} else {
			stepHeight.removeModifier();
		}
	}

	public void addAttributes() {
		this.setArmor();
		this.setArmorToughness();
		this.setAttackDamage();
		this.setAttackSpeed();
		this.setHealth();
		this.setKnockback();
		this.setMovementSpeed();
		this.setSwimSpeed();
		this.setLuck();
		if (reach != null) {
			this.setReach();
		}
		this.setJump();
		this.setStepHeight();
	}

	public void addAttributes(double percent) {
		this.setArmor();
		this.setArmorToughness();
		this.setAttackDamage();
		this.setAttackSpeed();
		this.setHealth(percent);
		this.setKnockback();
		this.setMovementSpeed();
		this.setSwimSpeed();
		this.setLuck();
		if (reach != null) {
			this.setReach();
		}
		this.setJump();
		this.setStepHeight();
	}

	//	Attributes

	public void removeAttibutes() {
		armor.removeModifier();
		attackDamage.removeModifier();
		attackSpeed.removeModifier();
		health.removeModifier();
		knockback.removeModifier();
		movementSpeed.removeModifier();
		armorToughness.removeModifier();
		swimSpeed.removeModifier();
		luck.removeModifier();
		if (reach != null) {
			reach.removeModifier();
		}
		jump.removeModifier();
		stepHeight.removeModifier();
	}

}
