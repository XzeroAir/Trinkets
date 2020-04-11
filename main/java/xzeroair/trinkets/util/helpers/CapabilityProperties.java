package xzeroair.trinkets.util.helpers;

public class CapabilityProperties {

	private static int DamageShield_HitCount = 0;

	private static boolean DragonsEye_NightVision = false;

	public static int getDamageShield_HitCount() {
		return DamageShield_HitCount;
	}

	public static void setDamageShield_HitCount(int damageShield_HitCount) {
		DamageShield_HitCount = damageShield_HitCount;
	}

	public static boolean isDragonsEye_NightVision() {
		return DragonsEye_NightVision;
	}

	public static void setDragonsEye_NightVision(boolean dragonsEye_NightVision) {
		DragonsEye_NightVision = dragonsEye_NightVision;
	}


}
