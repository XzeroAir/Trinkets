package xzeroair.trinkets.util.helpers;

public class ColorHelper {

    private static final int MAX_RGB_COLOR = 0xFFFFFF;
    private static final int MAX_DECIMAL_COLOR_LENGTH = String.valueOf(MAX_RGB_COLOR).length();
    private static final int MAX_HEX_COLOR_LENGTH = 6;

    public static String getHexFromRGB(int r, int g, int b) {
        final int decimal = getDecimalFromRGB(r, g, b);
        return convertDecimalColorToHexadecimal(decimal);
    }

    public static String getHexFromRGB(float r, float g, float b) {
        final int rR = Math.round(r * 255);
        final int rG = Math.round(g * 255);
        final int rB = Math.round(b * 255);
        return getHexFromRGB(rR, rG, rB);
    }

    public static int getDecimalFromRGB(int r, int g, int b) {
        return (((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff));
    }

    /**
     * Experimental
     */
    @Deprecated
    public static int getDecimalFromRGBA(int r, int g, int b, int a) {
        return (((a & 0xff) << 24) | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff));
    }

    public static int getDecimalFromRGB(float r, float g, float b) {
        final int rR = Math.round(r * 255);
        final int rG = Math.round(g * 255);
        final int rB = Math.round(b * 255);
        return getDecimalFromRGB(rR, rG, rB);
    }

    /**
     * Experimental
     */
    @Deprecated
    public static int getDecimalFromRGBA(float r, float g, float b, float a) {
        final int rR = Math.round(r * 255);
        final int rG = Math.round(g * 255);
        final int rB = Math.round(b * 255);
        final int rA = Math.round(a * 255);
        return getDecimalFromRGBA(rR, rG, rB, rA);
    }

    public static float[] getRGBColor(String hex) {
        return getRGBColor(getColorFromString(hex));
    }

    public static float[] getRGBColor(int decimal) {
        final float r = ((decimal & 0xFF0000) >> 16) / 255f;
        final float g = ((decimal & 0x00FF00) >> 8) / 255f;
        final float b = (decimal & 0x0000FF) / 255f;
        return new float[]{r, g, b};
    }

    public static String convertDecimalColorToHexadecimal(int color) {
        String hex = Integer.toHexString(color & 0xFFFFFF);
        return "#" + ("000000" + hex).substring(hex.length());
//        String hex = Integer.toHexString(color & 0xffffff);
//        if (hex.length() < 6) {
//            if (hex.length() == 5) {
//                hex = "0" + hex;
//            }
//            if (hex.length() == 4) {
//                hex = "00" + hex;
//            }
//            if (hex.length() == 3) {
//                hex = "000" + hex;
//            }
//        }
//        hex = "#" + hex;
//        return hex;
    }

    public static int convertHexToDecimal(String color) {
        return getColorFromString(color);
    }

    public static int getColorFromString(String color) {
        if (color == null) {
            return 0;
        }

        color = color.trim();
        if (color.isEmpty()) {
            return 0;
        }

        try {
            if (color.contains("#")) {
                final String hex = sanitizeHexColor(color);
                if (hex.isEmpty()) {
                    return 0;
                }

                return Integer.parseInt(hex, 16);
            }

            final String decimal = sanitizeDecimalColor(color);
            if (decimal.isEmpty()) {
                return 0;
            }

            final int value = Integer.parseInt(decimal);
            return value <= MAX_RGB_COLOR ? value : 0;
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    private static String sanitizeHexColor(String color) {
        final char[] sanitized = new char[Math.min(color.length(), MAX_HEX_COLOR_LENGTH)];
        int length = 0;
        for (int i = 0; i < color.length() && length < MAX_HEX_COLOR_LENGTH; i++) {
            final char character = color.charAt(i);
            if (Character.digit(character, 16) != -1) {
                sanitized[length++] = character;
            }
        }
        return length > 0 ? new String(sanitized, 0, length) : "";
    }

    private static String sanitizeDecimalColor(String color) {
        final char[] sanitized = new char[Math.min(color.length(), MAX_DECIMAL_COLOR_LENGTH)];
        int length = 0;
        for (int i = 0; i < color.length() && length < MAX_DECIMAL_COLOR_LENGTH; i++) {
            final char character = color.charAt(i);
            if (Character.isDigit(character)) {
                sanitized[length++] = character;
            }
        }
        return length > 0 ? new String(sanitized, 0, length) : "";
    }

    public static class ColorObject {

        private final String hexadecimal;
        private final int decimal;

        private float r = 1;
        private float g = 1;
        private float b = 1;

        public ColorObject(int decimal) {
            final float[] rgb = getRGBColor(decimal);
            this.decimal = decimal;
            this.hexadecimal = convertDecimalColorToHexadecimal(decimal);
            this.r = rgb[0];
            this.g = rgb[1];
            this.b = rgb[2];
        }

        public ColorObject(String hexadecimal) {
            final float[] rgb = getRGBColor(hexadecimal);
            this.hexadecimal = hexadecimal;
            this.decimal = convertHexToDecimal(hexadecimal);
            this.r = rgb[0];
            this.g = rgb[1];
            this.b = rgb[2];
        }

        public float getRed() {
            return this.r;
        }

        public float getGreen() {
            return this.g;
        }

        public float getBlue() {
            return this.b;
        }

        public int getDecimal() {
            return this.decimal;
        }

        public String getHexadecimal() {
            return this.hexadecimal;
        }
    }
}
