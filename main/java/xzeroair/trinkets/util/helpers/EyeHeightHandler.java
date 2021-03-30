package xzeroair.trinkets.util.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.entity.AlphaWolf;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.util.TrinketsConfig;

public class EyeHeightHandler {

	public static void eyeHeightHandler(EntityPlayer player, int size, int targetSize) {
		if (!TrinketsConfig.CLIENT.cameraHeight) {
			return;
		}
		int defaultSize = 100;
		final float defaultEyeHeight = player.getDefaultEyeHeight();
		float ne = ((defaultEyeHeight) * (size * 0.01F));
		float offset = 0.0050000099F;
		ne -= offset;
		ne -= 0.05F;
		double t2 = (1F / (size * 0.01F));
		if (player.isRiding() && !(player.getRidingEntity() instanceof AlphaWolf)) {
			if (size < defaultSize) {
				ne *= t2 * 0.82;
			}
		} else {
			if (size < (defaultSize / 2)) {
				if (player.world.isRemote) {
					if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
						ne -= 0.01F;
					} else {
						if (player.isSneaking()) {
							ne += ne * (0.05F + (0.05F * (targetSize * 0.01F)));
						}
					}
				}
			} else {
			}
		}
		player.eyeHeight = ne;
		//			} else {
		//				player.eyeHeight = (defaultEyeHeight) * (race.getSize() * 0.01F);
		//			}
		//		} else {
		//			if (race.isTransforming()) {
		//				if (race.getSize() >= (defaultSize - 1)) {
		//					player.eyeHeight = defaultEyeHeight;
		//				} else {
		//					player.eyeHeight = ((defaultEyeHeight) * (race.getSize() * 0.01F));
		//				}
		//			}
		//		}
	}

	public static void eyeHeightHandler(EntityPlayer player, EntityProperties race) {
		if (!TrinketsConfig.CLIENT.cameraHeight) {
			return;
		}
		int defaultSize = 100;
		final float defaultEyeHeight = player.getDefaultEyeHeight();
		if (!race.getCurrentRace().equals(EntityRaces.none) && !race.isNormalHeight()) {
			if (race.isTransformed()) {
				float ne = ((defaultEyeHeight) * (race.getSize() * 0.01F));
				float offset = 0.0050000099F;
				ne -= offset;
				ne -= 0.05F;
				double t2 = (1F / (race.getSize() * 0.01F));
				if (player.isRiding() && !(player.getRidingEntity() instanceof AlphaWolf)) {
					if (race.getSize() < defaultSize) {
						ne *= t2 * 0.82;
					}
				} else {
					if (race.getSize() < (defaultSize / 2)) {
						if (player.world.isRemote) {
							if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
								ne -= 0.01F;
							} else {
								if (player.isSneaking()) {
									ne += ne * (0.05F + (0.05F * (race.getTargetSize() * 0.01F)));
								}
							}
						}
					} else {
					}
				}
				player.eyeHeight = ne;
			} else {
				player.eyeHeight = (defaultEyeHeight) * (race.getSize() * 0.01F);
			}
		} else {
			if (race.isTransforming()) {
				if (race.getSize() == (defaultSize - 1)) {
					player.eyeHeight = defaultEyeHeight;
				} else {
					player.eyeHeight = ((defaultEyeHeight) * (race.getSize() * 0.01F));
				}
			}
		}
	}
}
