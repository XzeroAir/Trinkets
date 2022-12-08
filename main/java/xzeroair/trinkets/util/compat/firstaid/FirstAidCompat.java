package xzeroair.trinkets.util.compat.firstaid;

public class FirstAidCompat {

	//	public static void resetHP(EntityLivingBase entity) {
	//		if (entity instanceof EntityPlayer) {
	//			if (!entity.hasCapability(CapabilityExtendedHealthSystem.INSTANCE, null)) {
	//				return;
	//			}
	//			final EntityPlayer player = (EntityPlayer) entity;
	//			final AbstractPlayerDamageModel aidCap = player.getCapability(CapabilityExtendedHealthSystem.INSTANCE, null);
	//			//			//			FirstAidRegistry.getImpl().getDamageDistribution(arg0)
	//			AbstractDamageablePart head = aidCap.HEAD;
	//			AbstractDamageablePart body = aidCap.BODY;
	//			AbstractDamageablePart lArm = aidCap.LEFT_ARM;
	//			AbstractDamageablePart rArm = aidCap.RIGHT_ARM;
	//			AbstractDamageablePart lLeg = aidCap.LEFT_LEG;
	//			AbstractDamageablePart rLeg = aidCap.RIGHT_LEG;
	//			AbstractDamageablePart lFoot = aidCap.LEFT_FOOT;
	//			AbstractDamageablePart rFoot = aidCap.RIGHT_FOOT;
	//			float headHP = aidCap.HEAD.currentHealth;
	//			float bodyHP = aidCap.BODY.currentHealth;
	//			float lArmHP = aidCap.LEFT_ARM.currentHealth;
	//			float rArmHP = aidCap.RIGHT_ARM.currentHealth;
	//			float lLegHP = aidCap.LEFT_LEG.currentHealth;
	//			float rLegHP = aidCap.RIGHT_LEG.currentHealth;
	//			float lFootHP = aidCap.LEFT_FOOT.currentHealth;
	//			float rFootHP = aidCap.RIGHT_FOOT.currentHealth;
	//			aidCap.tick(player.world, player);
	//			if (headHP > head.currentHealth) {
	//				aidCap.HEAD.heal(aidCap.HEAD.getMaxHealth(), player, false);
	//			}
	//			if (bodyHP > body.currentHealth) {
	//				aidCap.BODY.heal(aidCap.BODY.getMaxHealth(), player, false);
	//			}
	//			if (lArmHP > lArm.currentHealth) {
	//				aidCap.LEFT_ARM.heal(aidCap.LEFT_ARM.getMaxHealth(), player, false);
	//			}
	//			if (rArmHP > rArm.currentHealth) {
	//				aidCap.RIGHT_ARM.heal(aidCap.RIGHT_ARM.getMaxHealth(), player, false);
	//			}
	//			if (lLegHP > lLeg.currentHealth) {
	//				aidCap.LEFT_LEG.heal(aidCap.LEFT_LEG.getMaxHealth(), player, false);
	//			}
	//			if (rLegHP > rLeg.currentHealth) {
	//				aidCap.RIGHT_LEG.heal(aidCap.RIGHT_LEG.getMaxHealth(), player, false);
	//			}
	//			if (lFootHP > lFoot.currentHealth) {
	//				aidCap.LEFT_FOOT.heal(aidCap.LEFT_FOOT.getMaxHealth(), player, false);
	//			}
	//			if (rFootHP > rFoot.currentHealth) {
	//				aidCap.RIGHT_FOOT.heal(aidCap.RIGHT_FOOT.getMaxHealth(), player, false);
	//			}
	//		}
	//	}
}
