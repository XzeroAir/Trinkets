package xzeroair.trinkets.util.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import xzeroair.trinkets.capabilities.race.RaceProperties;
import xzeroair.trinkets.util.TrinketsConfig;

public class SizeHelper {

	public static void eyeHeightHandler(EntityPlayer player, RaceProperties race) {
		int defaultSize = 100;
		if ((TrinketsConfig.CLIENT.cameraHeight != false)) {
			final float defaultEyeHeight = player.getDefaultEyeHeight();
			if (race.getTrans() == true) {
				if (race.getSize() == race.getTarget()) {
					if (race.getSize() < (defaultSize / 2)) {
						if (player.isRiding()) {
							float mount = player.getRidingEntity().height;
							float me = ((mount * (race.getSize() * 0.01F)) + ((defaultEyeHeight) * (race.getSize() * 0.01F)));
							player.eyeHeight = (me * 1.05F);
						} else {
							float ne = ((defaultEyeHeight) * (race.getSize() * 0.01F)) - 0.005F;
							if (player.isSneaking()) {
								ne -= ne * (0.025F + (0.025F * (race.getTarget() * 0.01F)));
							}
							if (player.world.isRemote) {
								if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
									ne = ne - 0.1F;
								}
							} else {
								ne = ne - 0.04F;
							}
							if (player.eyeHeight != ne) {
								player.eyeHeight = ne;
							}
						}
					} else {
						float ne = (defaultEyeHeight) * (race.getSize() * 0.01F);
						if (player.isSneaking()) {
							ne -= ne * (0.05F + (0.05F * (race.getTarget() * 0.01F)));
						}
						player.eyeHeight = ne;
					}
				} else {
					player.eyeHeight = (defaultEyeHeight) * (race.getSize() * 0.01F);
				}
			} else {
				if ((player.eyeHeight != defaultEyeHeight)) {
					if (race.getSize() != (100)) {
						//if ((race.getSize() == (defaultSize - 1)) || (race.getSize() == (defaultSize + 1))) {
						if (race.getSize() == defaultSize) {
							player.eyeHeight = defaultEyeHeight;
						} else {
							player.eyeHeight = ((defaultEyeHeight) * (race.getSize() * 0.01F));
						}
					}
				}
			}
		}
	}

	public static void sizeHandler(RaceProperties race) {
		int defaultSize = 100;
		if (race.getTrans()) {
			if (race.getSize() != race.getTarget()) {
				if ((race.getSize() > race.getTarget())) {
					race.setSize(race.getSize() - 1);
				}
				if ((race.getSize() < race.getTarget())) {
					race.setSize(race.getSize() + 1);
				}
			}
		} else {
			if (race.getSize() != defaultSize) {
				if ((race.getSize() < defaultSize)) {
					race.setSize(race.getSize() + 1);
				} else {
					race.setSize(race.getSize() - 1);
				}
			}
		}
	}
}
