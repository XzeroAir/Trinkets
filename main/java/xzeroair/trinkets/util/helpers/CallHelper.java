package xzeroair.trinkets.util.helpers;

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
		String[] stringArray = string.split(", ");
		return stringArray;
	}

}
