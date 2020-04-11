package xzeroair.trinkets.util.helpers;

import net.minecraft.client.model.ModelBase;
import xzeroair.trinkets.client.model.Tiara;
import xzeroair.trinkets.client.model.Wings;

public class CallHelper {

	private static final xzeroair.trinkets.client.model.Wings wings = new Wings();
	private static final xzeroair.trinkets.client.model.Tiara tiara = new Tiara();

	public static ModelBase getModel(String string) {
		switch (string) {
		case "wings":
			return wings;
		case "tiara":
			return tiara;
		default:
			return null;

		}
	}

}
