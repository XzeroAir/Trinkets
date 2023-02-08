package xzeroair.trinkets.util.helpers;

public class ColorHelper {

	public static String getHexColorFromRGB(int r, int g, int b) {
		final int decimal = getDecimalFromRGB(r, g, b);
		final String hex = convertDecimalColorToHexadecimal(decimal);
		return hex;
	}

	public static String getHexColorFromRGB(float r, float g, float b) {
		final int rR = (int) (r * 255);
		final int rG = (int) (g * 255);
		final int rB = (int) (b * 255);
		return getHexColorFromRGB(rR, rG, rB);
	}

	public static int getDecimalFromRGB(int r, int g, int b) {
		return ((0xff << 24) | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff));
	}

	/**
	 * Experimental
	 */
	@Deprecated
	public static int getDecimalFromRGBA(int r, int g, int b, int a) {
		return (((a & 0xff) << 24) | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff));
	}

	public static int getDecimalFromRGB(float r, float g, float b) {
		final int rR = (int) (r * 255);
		final int rG = (int) (g * 255);
		final int rB = (int) (b * 255);
		return getDecimalFromRGB(rR, rG, rB);
	}

	/**
	 * Experimental
	 */
	@Deprecated
	public static int getDecimalFromRGBA(float r, float g, float b, float a) {
		final int rR = (int) (r * 255);
		final int rG = (int) (g * 255);
		final int rB = (int) (b * 255);
		final int rA = (int) (a * 255);
		return getDecimalFromRGBA(rR, rG, rB, rA);
	}

	public static float[] getRGBColor(String hex) {
		try {
			final String hexColor = hex.toLowerCase().replaceAll("[^#0-9a-f]", "");
			if (!hexColor.replace("#", "").isEmpty()) { //(0x)?[0-9a-f]
				final int i = Integer.decode(hexColor);
				final float r = ((i & 16711680) >> 16) / 255F;
				final float g = ((i & 65280) >> 8) / 255F;
				final float b = ((i & 255) >> 0) / 255F;
				return new float[] { r, g, b };
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return new float[] { 0, 0, 0 };
	}

	public static float[] getRGBColor(int decimal) {
		return getRGBColor(decimal + "");
	}

	public static String convertDecimalColorToHexadecimal(int color) {
		String hex = Integer.toHexString(color & 0xffffff);
		if (hex.length() < 6) {
			if (hex.length() == 5) {
				hex = "0" + hex;
			}
			if (hex.length() == 4) {
				hex = "00" + hex;
			}
			if (hex.length() == 3) {
				hex = "000" + hex;
			}
		}
		hex = "#" + hex;
		return hex;
	}

	public static int convertHexadecimalToDecimal(String color) {
		try {
			String hexColor = color.toLowerCase().replaceAll("[^#0-9a-f]", "");
			if (!color.isEmpty()) {
				final boolean CheckforHash = !color.startsWith("#");
				hexColor = CheckforHash ? ("#" + color) : color.toLowerCase();
				int length = hexColor.length();
				if (hexColor.length() >= 7) {
					length = 7;
				}
				final int i = Integer.decode(hexColor.substring(0, length));
				return i;

			}
		} catch (final NumberFormatException ex) {
		}
		return 0;
	}

	public static class ColorObject {

		private String hexadecimal;
		private int decimal;

		private float r = 1;
		private float g = 1;
		private float b = 1;

		public ColorObject(int decimal) {
			final float[] rgb = getRGBColor(decimal);
			this.decimal = decimal;
			hexadecimal = convertDecimalColorToHexadecimal(decimal);
			r = rgb[0];
			g = rgb[1];
			b = rgb[2];
		}

		public ColorObject(String hexadecimal) {
			final float[] rgb = getRGBColor(hexadecimal);
			this.hexadecimal = hexadecimal;
			decimal = convertHexadecimalToDecimal(hexadecimal);
			r = rgb[0];
			g = rgb[1];
			b = rgb[2];
		}

		public float getRed() {
			return r;
		}

		public float getGreen() {
			return g;
		}

		public float getBlue() {
			return b;
		}

		public int getDecimal() {
			return decimal;
		}

		public String getHexadecimal() {
			return hexadecimal;
		}
	}
}
