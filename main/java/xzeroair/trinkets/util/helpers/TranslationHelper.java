package xzeroair.trinkets.util.helpers;

import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import xzeroair.trinkets.enums.EnumRenderLocation;
import xzeroair.trinkets.util.Reference;

import java.util.function.Function;

public class TranslationHelper {

    public TextFormatting black = TextFormatting.BLACK;
    public TextFormatting white = TextFormatting.WHITE;
    public TextFormatting red = TextFormatting.RED;
    public TextFormatting dRed = TextFormatting.DARK_RED;
    public TextFormatting blue = TextFormatting.BLUE;
    public TextFormatting dBlue = TextFormatting.DARK_BLUE;
    public TextFormatting green = TextFormatting.GREEN;
    public TextFormatting dGreen = TextFormatting.DARK_GREEN;
    public TextFormatting lPurple = TextFormatting.LIGHT_PURPLE;
    public TextFormatting dPurple = TextFormatting.DARK_PURPLE;
    public TextFormatting yellow = TextFormatting.YELLOW;
    public TextFormatting aqua = TextFormatting.AQUA;
    public TextFormatting dAqua = TextFormatting.DARK_AQUA;
    public TextFormatting gray = TextFormatting.GRAY;
    public TextFormatting dGray = TextFormatting.DARK_GRAY;
    public TextFormatting gold = TextFormatting.GOLD;
    public TextFormatting ST = TextFormatting.STRIKETHROUGH;
    public TextFormatting UL = TextFormatting.UNDERLINE;
    public TextFormatting italic = TextFormatting.ITALIC;
    public TextFormatting bold = TextFormatting.BOLD;
    public TextFormatting reset = TextFormatting.RESET;

    public static final TranslationHelper INSTANCE = new TranslationHelper();

    //	public String isKeyBind(String key) {
    //		return "*" + key + ":";
    //	}
    //
    //	public String isOption(String key) {
    //		return "$" + key + ":";
    //	}
    //
    //	public String isLang(String key) {
    //		return "@" + key + ":";
    //	}

    public String formatAddVariables(String string, KeyEntry... entries) {
        return this.formatAddVariables(string, 0, entries);
    }

    public String formatAddVariables(String string, int rendLoc, KeyEntry... entries) {
        if ((string.startsWith("$gui:") && !(rendLoc == EnumRenderLocation.GUI.getId())) || (string.startsWith("$item:") && !(rendLoc == EnumRenderLocation.ITEM.getId()))) {
            return "";
        }
        string = string.replace("$gui:", "").replace("$item:", "");
        for (final KeyEntry entry : entries) {
            if (entry instanceof LangEntry && this.hasLangReplacement(entry.key(), string)) {
                if (entry.enabled()) {
                    string = this.langKeyReplace(string, entry.enabled(), entry.key(), entry.option());
                } else {
                    return "";
                }
            }
            if (entry instanceof OptionEntry && this.hasOption(entry.key(), string)) {
                if (entry.enabled()) {
                    string = this.optionReplace(string, entry.enabled(), entry.key(), entry.option());
                } else {
                    return "";
                }
            }
            if (entry instanceof KeyBindEntry && this.hasKeyBind(entry.key(), string)) {
                if (entry.enabled()) {
                    string = this.optionReplace(string, entry.enabled(), entry.key(), entry.option());
                } else {
                    return "";
                }
            }
        }
        return string.replace("#underline:", this.UL + "").replace("#strikethrough:", this.ST + "").replace("#italic:", this.italic + "").replace("#bold:", this.bold + "");
    }

    // DELETE
    public String formatAddVariable(String string, int rendLoc, KeyEntry entry) {
        return this.formatAddVariables(string, rendLoc, entry);
    }


    public String getLangTranslation(TextComponentTranslation lang, Function<String, String> func) {
        final boolean isEmpty = (lang.getUnformattedComponentText().contentEquals(lang.getKey()) || lang.getUnformattedText().isEmpty());
        if (!isEmpty) {
            String text = func == null ? lang.getFormattedText() : func.apply(lang.getFormattedText());
            if (!this.isStringEmpty(text)) {
                return text;
            }
        }
        return "";
    }

    public String getLangTranslation(TextComponentTranslation lang) {
        return this.getLangTranslation(lang, null);
    }

    public String getLangTranslation(String key) {
        return this.getLangTranslation(key, null);
    }

    public String getLangTranslation(String key, Function<String, String> func) {
        final TextComponentTranslation lang = new TextComponentTranslation(key);
        return this.getLangTranslation(lang, func);
    }

    public static class KeyEntry {

        private final String key;
        private final String option;
        private final boolean enabled;

        public <T> KeyEntry(String key, T option) {
            this(key, true, option);
        }

        public <T> KeyEntry(String key, boolean enabled, T option) {
            this.key = key;
            this.enabled = enabled;
            this.option = option + "";
        }

        public String key() {
            return this.key;
        }

        public String option() {
            return this.option;
        }

        public boolean enabled() {
            return this.enabled;
        }
    }

    public static class OptionEntry extends KeyEntry {

        public <T> OptionEntry(String key, T option) {
            this(key, true, option);
        }

        public <T> OptionEntry(String key, boolean enabled, T option) {
            super("$" + key + ":", enabled, option);
        }

    }

    public static class KeyBindEntry extends KeyEntry {
        public KeyBindEntry(String key, String keybind) {
            this(key, true, keybind);
        }

        public KeyBindEntry(String key, boolean enabled, String keybind) {
            super("*" + key + ":", enabled, keybind);
        }

    }

    public static class LangEntry extends KeyEntry {

        public <T> LangEntry(String prefix, String key) {
            this(prefix, key, true);
        }

        public <T> LangEntry(String prefix, String key, boolean enabled) {
            super("@" + key + ":", enabled, prefix + "." + key);
        }

    }

    public boolean hasKey(String key, String string) {
        return string.contains(key);
    }

    public boolean hasKeyBind(String key, String string) {
        return string.contains("*" + key.replace("*", "").replace(":", "").toLowerCase() + ":");
    }

    public boolean hasOption(String key, String string) {
        return string.contains("$" + key.replace("$", "").replace(":", "").toLowerCase() + ":");
    }

    public boolean hasLangReplacement(String key, String string) {
        return string.contains("@" + key.replace("@", "").replace(":", "").toLowerCase() + ":");
    }

    public String langKeyReplace(String string, boolean enabled, String key, String replacementKey) {
        if (enabled) {
            final String translatedKey = new TextComponentTranslation(replacementKey).getFormattedText().trim();
            return string.replace(key, translatedKey);
        } else {
            return string.replace(key, "");
        }
    }

    public String optionReplace(String string, boolean enabled, String key, String Variable) {
        if (enabled) {
            return string.replace(key, Variable);
        } else {
            return string.replace(key, "");
        }
    }

    public boolean isStringEmpty(String string) {
        return string.replace("�r", "").replace("�6", "").replace("�", "").replace("§r", "").replaceAll(" ", "").isEmpty();
    }

    public String TranslationKeyReplace(String itemKey, boolean enabled, String key, String translation) {
        if (enabled) {
            final String translatedKey = new TextComponentTranslation(itemKey + "." + key).getFormattedText();
            return translation.replace("@" + key.toLowerCase() + ":", translatedKey);
        } else {
            return translation.replace("@" + key.toLowerCase() + ":", "");
        }
    }

    public String VariableReplace(boolean enabled, String key, String Variable, String translation) {
        if (enabled) {
            return translation.replace("$" + key.toLowerCase() + ":", Variable);
        } else {
            return translation.replace("$" + key.toLowerCase() + ":", "");
        }
    }

    public String booleanCheckTranslation(boolean bool) {
        final TextComponentTranslation enabled = new TextComponentTranslation(Reference.MODID + ".tooltip.enabled");
        final TextComponentTranslation disabled = new TextComponentTranslation(Reference.MODID + ".tooltip.disabled");
        if (bool) {
            return enabled.getFormattedText();
        } else {
            return disabled.getFormattedText();
        }
    }

    public String toggleCheckTranslation(boolean bool) {
        final TextComponentTranslation on = new TextComponentTranslation(Reference.MODID + ".tooltip.on");
        final TextComponentTranslation off = new TextComponentTranslation(Reference.MODID + ".tooltip.off");
        if (bool) {
            return on.getFormattedText();
        } else {
            return off.getFormattedText();
        }
    }

    @Deprecated
    public String addTextColorFromLangKey(String string) {
        return string.replace("#black:", "" + this.black) // §0
                .replace("#darkblue:", "" + this.dBlue) // §1
                .replace("#darkgreen:", "" + this.dGreen) // §2
                .replace("#darkaqua:", "" + this.dAqua) // §3
                .replace("#darkred:", "" + this.dRed) // §4
                .replace("#darkpurple:", "" + this.dPurple) // §5
                .replace("#gold:", "" + this.gold) // §6
                .replace("#gray:", "" + this.gray) // §7
                .replace("#darkgray:", "" + this.dGray) // §8
                .replace("#blue:", "" + this.blue) // §9
                .replace("#green:", "" + this.green) // §a
                .replace("#aqua:", "" + this.aqua) // §b
                .replace("#red:", "" + this.red) // §c
                .replace("#lightpurple:", "" + this.lPurple) // §d
                .replace("#yellow:", "" + this.yellow) // §e
                .replace("#white:", "" + this.white) // §f
                .replace("#obfuscated:", "" + this.reset) // §k
                .replace("#bold:", "" + this.bold) // §l
                .replace("#strikethrough:", "" + this.ST) // §m
                .replace("#underline:", "" + this.UL) // §n
                .replace("#italic:", "" + this.italic) // §o
                .replace("#reset:", "" + this.reset); // §r
    }

    public String translateAttributeValue(int OP, double Amount) {
        // float percentage = 100/MathHelper.clamp((1F/0)*100, 0, 1000);
        // float translatedFloat = MathHelper.clamp(percentage/100, Float.MIN_VALUE,
        // Float.MAX_VALUE);
        double TranslatedAmount = Amount;
        String string = "";
        if (OP > 0) {
            TranslatedAmount = Math.round(Amount * (100));
            if (TranslatedAmount > 0) {
                string = "+" + TranslatedAmount + "%";
            } else {
                string = TranslatedAmount + "%";
            }
        } else {
            if (TranslatedAmount > 0) {
                string = "+" + TranslatedAmount;
            } else {
                string = "" + TranslatedAmount;
            }
        }
        final String color = TranslatedAmount > 0 ? "" + this.green : "" + this.red;
        return color + string;
    }

    public double parseDoubleSafely(String d) {
        final String number = d.replaceAll("[^.\\d]", "");
        if (!number.isEmpty()) {
            try {
                return Double.parseDouble(number);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

}
