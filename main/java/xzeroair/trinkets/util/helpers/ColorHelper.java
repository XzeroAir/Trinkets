package xzeroair.trinkets.util.helpers;

public class ColorHelper {

	static float[] ColorCode = new float[] {1,1,1,1};

	public static float[] getHexColor(String HexColor) {

		if(!HexColor.isEmpty()) {
			final boolean CheckforHash = !HexColor.startsWith("#");
			HexColor =  CheckforHash?("#" + HexColor) : HexColor.toLowerCase();
			try {
				final int i = Integer.decode(HexColor.substring(0, 7));
				final int r = (i & 16711680) >> 16;
				final int g = (i & 65280) >> 8;
				final int b = (i & 255) >> 0;
				ColorCode[0] = r;
				ColorCode[1] = g;
				ColorCode[2] = b;

			} catch(final NumberFormatException ex) {
			}
			if((HexColor.length() > 7)) {
				if(HexColor.contains(":")) {
					final int AlphaIndex = HexColor.lastIndexOf(":");
					if(!(AlphaIndex >= HexColor.length())) {
						HexColor = HexColor.substring(AlphaIndex+1);
						try {
							final Float f = Float.parseFloat(HexColor);
							ColorCode[3] = f;
						} catch(final NumberFormatException ex) {

						}
					}
				}
			}
		}
		return ColorCode;
	}

}
