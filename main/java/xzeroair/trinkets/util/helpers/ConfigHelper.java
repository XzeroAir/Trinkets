package xzeroair.trinkets.util.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import xzeroair.trinkets.util.TrinketsConfig;

public class ConfigHelper {

	private static boolean CHEST = true;

	public static void getServerConfig(World world) {
		if(!world.isRemote) {
			if(TrinketsConfig.SERVER.C04_DE_Chests != true) {
				CHEST = false;
			}
		} else {
			if(TrinketsConfig.SERVER.C04_DE_Chests == true) {
				TrinketsConfig.SERVER.C04_DE_Chests = getChestAbility();
			}
		}
	}

	public static boolean getChestAbility() {
		return CHEST;
	}

}
