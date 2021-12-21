package xzeroair.trinkets.util.helpers;

public class ColorHelper {

	private String hexColor;
	private int decimalColor;

	private float r = 1;
	private float g = 1;
	private float b = 1;
	private float a = 1;

	public ColorHelper() {
	}

	public ColorHelper setAlpha(float f) {
		//		a = f * 0.01F;
		a = f;
		//			if (hexColor.contains(":")) {
		//				final int AlphaIndex = hexColor.lastIndexOf(":");
		//				hexColor = hexColor.substring(AlphaIndex + 1);
		//				try {
		//					a = Float.parseFloat(hexColor);
		//				} catch (final NumberFormatException ex) {
		//
		//				}
		//			}
		return this;
	}

	public ColorHelper setColor(String string) {
		if (string.startsWith("#")) {
			hexColor = string;
			this.setHexColor();
		} else {
			if (!string.replaceAll("[\\D]", "").isEmpty()) {
				decimalColor = Integer.parseInt(string.replaceAll("[\\D]", ""));
				this.setDecimalColor();
			}
		}
		return this;
	}

	public ColorHelper setColor(int color) {
		decimalColor = color;
		hexColor = this.convertColorToHexadeimal(decimalColor);
		r = ((decimalColor & 16711680) >> 16) / 255F;
		g = ((decimalColor & 65280) >> 8) / 255F;
		b = ((decimalColor & 255) >> 0) / 255F;
		return this;
	}

	public ColorHelper setColorFromRGB(int r, int g, int b) {
		this.setColor(((0xff << 24) | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff)));
		this.setHexColor(this.convertColorToHexadeimal(decimalColor));
		return this;
	}

	private ColorHelper setColorFromRGBFloat(float r, float g, float b) {
		this.setColorFromRGB((int) (r * 255), (int) (g * 255), (int) (b * 255));
		return this;
	}

	public ColorHelper setRed(float r) {
		this.setColorFromRGBFloat(r, g, b);
		return this;
	}

	public ColorHelper setGreen(float g) {
		this.setColorFromRGBFloat(r, g, b);
		return this;
	}

	public ColorHelper setBlue(float b) {
		this.setColorFromRGBFloat(r, g, b);
		return this;
	}

	public int getDecimal() {
		return decimalColor;
	}

	public String getHex() {
		return hexColor;
	}

	private String convertColorToHexadeimal(int color) {
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

	private int getDecimalFromHex(String color) {
		if (!color.isEmpty()) {
			final boolean CheckforHash = !color.startsWith("#");
			hexColor = CheckforHash ? ("#" + color) : color.toLowerCase();
			try {
				int length = hexColor.length();
				if (hexColor.length() >= 7) {
					length = 7;
				}
				final int i = Integer.decode(hexColor.substring(0, length));
				return i;
				//				decimalColor = i;
				//				r = ((i & 16711680) >> 16) / 255F;
				//				g = ((i & 65280) >> 8) / 255F;
				//				b = ((i & 255) >> 0) / 255F;

			} catch (final NumberFormatException ex) {
			}
		}
		return 0;
	}

	private void setHexColor(String color) {
		if (!color.isEmpty()) {
			final boolean CheckforHash = !color.startsWith("#");
			hexColor = CheckforHash ? ("#" + color) : color.toLowerCase();
			try {
				int length = hexColor.length();
				if (hexColor.length() >= 7) {
					length = 7;
				}
				final int i = Integer.decode(hexColor.substring(0, length));
				decimalColor = i;
				r = ((i & 16711680) >> 16) / 255F;
				g = ((i & 65280) >> 8) / 255F;
				b = ((i & 255) >> 0) / 255F;

			} catch (final NumberFormatException ex) {
			}
		}
	}

	private void setHexColor() {

		if (!hexColor.isEmpty()) {
			final boolean CheckforHash = !hexColor.startsWith("#");
			hexColor = CheckforHash ? ("#" + hexColor) : hexColor.toLowerCase();
			try {
				int length = hexColor.length();
				if (hexColor.length() >= 7) {
					length = 7;
				}
				final int i = Integer.decode(hexColor.substring(0, length));
				decimalColor = i;
				r = ((i & 16711680) >> 16) / 255F;
				g = ((i & 65280) >> 8) / 255F;
				b = ((i & 255) >> 0) / 255F;

			} catch (final NumberFormatException ex) {
			}
		}
	}

	private void setDecimalColor() {
		hexColor = this.convertColorToHexadeimal(decimalColor);
		r = ((decimalColor & 16711680) >> 16) / 255F;
		g = ((decimalColor & 65280) >> 8) / 255F;
		b = ((decimalColor & 255) >> 0) / 255F;
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

	public float getAlpha() {
		return a;
	}
}
