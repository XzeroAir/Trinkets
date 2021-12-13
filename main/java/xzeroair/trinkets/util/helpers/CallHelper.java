package xzeroair.trinkets.util.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class CallHelper {

	public static String combineStringArray(String[] array) {
		if (array.length > 0) {
			String combinedArray = "";
			for (int i = array.length - 1; i >= 0; --i) {
				combinedArray = array[i] + ", " + combinedArray;
			}
			return combinedArray;
		}
		return "";
	}

	public static String[] deconstructStringArray(String string) {
		final String[] stringArray = string.split(", ");
		return stringArray;
	}

	public static String getStringFromArray(String[] array, int index) {
		if ((index >= 0) && (index < array.length)) {
			return array[index];
		} else {
			return "";
		}
	}

	public static String formatConfigString(Object... options) {
		String returnString = "";
		for (final Object obj : options) {
			if (!returnString.isEmpty()) {
				returnString += ";";
			}
			if (obj instanceof String) {
				returnString += obj;
			}
			if (obj instanceof Boolean) {
				returnString += "b=" + obj;
			}
			if (obj instanceof Integer) {
				returnString += "i=" + obj;
			}
			if (obj instanceof Float) {
				returnString += "f=" + obj;
			}
			if (obj instanceof Double) {
				returnString += "d=" + obj;
			}
		}
		return returnString;
	}

	public static double getDoubleFromString(String string) {
		if (string.startsWith("d=")) {
			try {
				return Double.parseDouble(string.replaceFirst("d=", ""));
			} catch (final Exception e) {
			}
		}
		return 0;
	}

	public static int getIntegerFromString(String string) {
		if (string.startsWith("i=")) {
			try {
				return Integer.parseInt(string.replaceFirst("i=", ""));
			} catch (final Exception e) {
			}
		}
		return Integer.MIN_VALUE;
	}

	public static float getFloatFromString(String string) {
		if (string.startsWith("f=")) {
			try {
				return Float.parseFloat(string.replaceFirst("f=", ""));
			} catch (final Exception e) {
			}
		}
		return 0;
	}

	public static boolean getBooleanFromString(String string) {
		if (string.startsWith("b=")) {
			try {
				return Boolean.parseBoolean(string.replaceFirst("b=", ""));
			} catch (final Exception e) {
			}
		}
		return false;
	}

	public static void sendMessageToPlayer(Entity entity, String msg, boolean onScreen) {
		if ((entity instanceof EntityPlayer) && !msg.isEmpty()) {
			final TextComponentString message = new TextComponentString(msg);
			((EntityPlayer) entity).sendStatusMessage(message, onScreen);
		}
	}

}
