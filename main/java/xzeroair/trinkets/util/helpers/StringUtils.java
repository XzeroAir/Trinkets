package xzeroair.trinkets.util.helpers;

import java.util.function.BiFunction;
import java.util.function.Function;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class StringUtils {

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
		if ((array != null) && (index >= 0) && (index < array.length))
			return array[index];
		else
			return "";
	}

	public static String getCmdSubstring(String command, String cmdRegex) {
		final Function<String, String> getRegexCmd = cmd -> {
			final String regex = convertToRegex(cmdRegex, false);
			return regex.isEmpty() ? regex : regex + " ";
		};
		return getSubstring(command, getRegexCmd.apply(cmdRegex));
	}

	public static String convertToRegex(String regexInput, boolean caseSensitive) {
		final Function<String, String> getRegexCmd = cmd -> {
			if (!cmd.isEmpty()) {
				final String[] array = cmd.split("");
				String finalString = "";
				for (final String character : array) {
					if (!caseSensitive && character.matches("[a-zA-Z]")) {
						final String lower = character.toLowerCase();
						final String upper = character.toUpperCase();
						finalString += "[" + lower + upper + "]";
					} else if (character.matches("[- ]")) {
						finalString += "" + character + "?";
					} else {
						finalString += "[" + character + "]";
					}
				}
				if (!finalString.isEmpty())
					return "(" + finalString + ")";
			}
			return "";
		};
		return getRegexCmd.apply(regexInput);
	}

	public static String getSubstring(String string, String regex) {
		if (!regex.isEmpty()) {
			final String[] array = string.split(regex, 2);
			if ((array.length > 1)) {
				if (array[0].replace(" ", "").isEmpty())
					return array[1];
			}
			return "";
		} else
			return "";
	}

	public static StringCommand getStringVariables(String string, String regex) {
		String prefix = "";
		String suffix = "";
		if (!regex.isEmpty()) {
			final String[] array = string.split(regex, 2);
			String pre = array[0].replace("  ", " ").trim();
			if (!pre.isEmpty()) {
				prefix = pre;
			}
			if ((array.length > 1)) {
				suffix = array[1];
			}
		}
		return new StringCommand(prefix, suffix);
	}

	public static <T> T continueCommand(String string, Function<String, T> func) {
		return func.apply(string);
	}

	public static <T> T continueCommand(String arg1, String arg2, BiFunction<String, String, T> func) {
		return func.apply(arg1, arg2);
	}

	public static class StringCommand {

		String Prefix;
		String Suffix;

		public StringCommand(String prefix, String suffix) {
			Prefix = prefix;
			Suffix = suffix;
		}

		public String getPrefix() {
			return Prefix;
		}

		public String getSuffix() {
			return Suffix;
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
				return Integer.parseInt(string.replaceFirst("i=", "").replaceAll("[^.\\d]", "").replaceAll("(\\..*)", ""));
			} catch (final Exception e) {
			}
		}
		return 0;
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
