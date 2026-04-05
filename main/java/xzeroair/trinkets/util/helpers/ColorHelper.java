package xzeroair.trinkets.util.helpers;

public class ColorHelper {

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
        if (color == null || color.isEmpty()) {
            return 0;
        }

        color = color.trim();

        try {
            if (color.startsWith("#")) {
                return Integer.parseInt(color.substring(1), 16);
            }

            return Integer.parseInt(color); // decimal only

        } catch (NumberFormatException ignored) {
            return 0;
        }
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
            // #RRGGBB or #RGB (optional, depending on your needs)
            if (color.matches("^#?[0-9a-fA-F]+$")) {
                String hex = color.startsWith("#") ? color.substring(1) : color;
                return Integer.parseInt(hex, 16);
            }

            // decimal
            if (color.matches("^[0-9]+$")) {
                return Integer.parseInt(color);
            }

        } catch (NumberFormatException ignored) {
            ignored.printStackTrace();
        }

        return 0;
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
