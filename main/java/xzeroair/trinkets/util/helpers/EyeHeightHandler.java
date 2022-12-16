package xzeroair.trinkets.util.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import xzeroair.trinkets.entity.AlphaWolf;
import xzeroair.trinkets.util.TrinketsConfig;

public class EyeHeightHandler {

	public static void eyeHeightHandler(EntityPlayer player, int size, int targetSize) {
		if (!TrinketsConfig.CLIENT.cameraHeight) {
			return;
		}
		final int defaultSize = 100;
		final float defaultEyeHeight = player.getDefaultEyeHeight();
		float ne = ((defaultEyeHeight) * (size * 0.01F));
		final float offset = 0.0050000099F;
		ne -= offset;
		ne -= 0.05F;
		final double t2 = (1F / (size * 0.01F));
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

	public static void eyeHeightHandler(EntityPlayer player, boolean isTransformed, boolean isTranforming, float defaultHeight, float currentHeight) {
		if (!TrinketsConfig.CLIENT.cameraHeight) {
			return;
		}
		//		isTransformed = true;
		//		isTranforming = false;
		final float defaultEyeHeight = player.getDefaultEyeHeight();
		//		System.out.println(player.getEyeHeight());
		if (isTranforming || isTransformed) {
			// 165 when sneaking
			// 162 eyeheight, sneaking is -0.8
			float f = (float) ((currentHeight * 0.9F) - 0.005D);

			if (player.isPlayerSleeping()) {
				f = 0.2F;
			} else if (!player.isSneaking()) {
				if (player.isElytraFlying()) {
					f *= 0.2F;//0.4F;
				}
			} else {
				f -= f / 20;//0.08F;
			}

			if (player.isRiding()) {
				final Entity mount = player.getRidingEntity();
				if (mount != null) {
					final float mountHeight = mount.height;
					final double mountOffset = mount.getMountedYOffset();
					final double t = mountHeight - mountOffset;
					//				if (f < mountHeight) {
					//					f = mountHeight;
					//				}
					//					f += t;
					f = MathHelper.clamp(f, mountHeight, f);
				}
			}

			//			final float multi = player.isSneaking() ? 0.9F : 0.9F;
			player.eyeHeight = f;
		} else {
			if (player.eyeHeight != defaultEyeHeight) {
				player.eyeHeight = defaultEyeHeight;
			}
		}
		//		if (player.height == 1.8F) {
		//			if (isTransformed) {
		//				float ne = ((defaultEyeHeight) * (race.getSize() * 0.01F));//player.height * 0.9F;//
		//				final float offset = 0.0050000099F;
		//				ne -= offset;
		//				ne -= 0.05F;
		//				final double t2 = (1F / (race.getSize() * 0.01F));
		//				if (player.isRiding() && !(player.getRidingEntity() instanceof AlphaWolf)) {
		//					if (race.getSize() < defaultSize) {
		//						ne *= t2 * 0.82;
		//					}
		//				} else {
		//					if (race.getSize() < (defaultSize / 2)) {
		//						if (player.world.isRemote) {
		//							if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
		//								ne -= 0.01F;
		//							} else {
		//								if (player.isSneaking()) {
		//									ne += ne * (0.05F + (0.05F * (race.getTargetSize() * 0.01F)));
		//								}
		//							}
		//						}
		//					} else {
		//					}
		//				}
		//				player.eyeHeight = ne;
		//			} else {
		//				player.eyeHeight = (defaultEyeHeight) * (race.getSize() * 0.01F);
		//			}
		//		} else {
		//			if (race.isTransforming()) {
		//				if (race.getSize() == (defaultSize - 1)) {
		//					player.eyeHeight = defaultEyeHeight;
		//				} else {
		//					player.eyeHeight = ((defaultEyeHeight) * (race.getSize() * 0.01F));
		//				}
		//			}
		//		}
	}
}
