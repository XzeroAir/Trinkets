package xzeroair.trinkets.util.compat.elenaidodge;

import com.elenai.elenaidodge.ModConfig;
import com.elenai.elenaidodge.util.Keybinds;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ElenaiDodgeCompat {

	@SideOnly(Side.CLIENT)
	public static boolean isDodgeKeyDown() {
		if (Loader.isModLoaded("elenaidodge")) {
			try {
				return (ModConfig.client.controls.doubleTapMode == 0) && Keybinds.dodge.isKeyDown();
			} catch (Exception e) {

			}
		}
		return false;
	}

}
