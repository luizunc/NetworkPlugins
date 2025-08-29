package minecraft.core.core.utils;

import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###");
    
    public static String formatNumber(int number) {
        return DECIMAL_FORMAT.format(number);
    }
    
    public static String formatNumber(long number) {
        return DECIMAL_FORMAT.format(number);
    }
    
    public static String formatNumber(double number) {
        return DECIMAL_FORMAT.format(number);
    }

    private static final Pattern COLOR_PATTERN = Pattern.compile("(?i)(§)[0-9A-FK-OR]");
    
    public static String stripColors(final String input) {
        if (input == null) {
            return null;
        }
        return COLOR_PATTERN.matcher(input).replaceAll("");
    }

    public static String formatColors(String textToFormat) {
        return translateAlternateColorCodes('&', textToFormat);
    }
    
    public static String deformatColors(String textToDeFormat) {
        Matcher matcher = COLOR_PATTERN.matcher(textToDeFormat);
        while (matcher.find()) {
            String color = matcher.group();
            textToDeFormat = textToDeFormat.replaceFirst(Pattern.quote(color), Matcher.quoteReplacement("&" + color.substring(1)));
        }
        return textToDeFormat;
    }

    public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
        Pattern pattern = Pattern.compile("(?i)(" + String.valueOf(altColorChar) + ")[0-9A-FK-OR]");

        Matcher matcher = pattern.matcher(textToTranslate);
        while (matcher.find()) {
            String color = matcher.group();
            textToTranslate = textToTranslate.replaceFirst(Pattern.quote(color), Matcher.quoteReplacement("§" + color.substring(1)));
        }
        return textToTranslate;
    }

    public static String getFirstColor(String input) {
        Matcher matcher = COLOR_PATTERN.matcher(input);
        String first = "";
        if (matcher.find()) {
            first = matcher.group();
        }
        return first;
    }

    public static String getLastColor(String input) {
        Matcher matcher = COLOR_PATTERN.matcher(input);
        String last = "";
        while (matcher.find()) {
            last = matcher.group();
        }
        return last;
    }

    public static String repeat(String repeat, int amount) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < amount; i++) {
            sb.append(repeat);
        }
        return sb.toString();
    }

    public static <T> String join(T[] array, int index, String separator) {
        StringBuilder joined = new StringBuilder();
        for (int slot = index; slot < array.length; slot++) {
            joined.append(array[slot].toString()).append(slot + 1 == array.length ? "" : separator);
        }
        return joined.toString();
    }

    public static <T> String join(T[] array, String separator) {
        return join(array, 0, separator);
    }

    public static <T> String join(Collection<T> collection, String separator) {
        return join(collection.toArray(new Object[0]), separator);
    }

    public static String[] split(String toSplit, int length) {
        return split(toSplit, length, false);
    }

    public static String[] split(String toSplit, int length, boolean ignoreCompleteWords) {
        if (toSplit == null) {
            return new String[0];
        }
        if (length <= 0) {
            return new String[]{toSplit};
        }
        if (toSplit.length() <= length) {
            return new String[]{toSplit};
        }
        List<String> result = new ArrayList<>();
        int start = 0;
        int end = length;
        while (start < toSplit.length()) {
            if (end > toSplit.length()) {
                end = toSplit.length();
            }
            if (!ignoreCompleteWords && end != toSplit.length()) {
                int lastSpace = toSplit.lastIndexOf(' ', end);
                if (lastSpace > start) {
                    end = lastSpace;
                }
            }
            result.add(toSplit.substring(start, end));
            start = end;
            end += length;
        }
        return result.toArray(new String[0]);
    }

    public static String capitalise(String toCapitalise) {
        if (toCapitalise == null || toCapitalise.isEmpty()) {
            return toCapitalise;
        }
        return toCapitalise.substring(0, 1).toUpperCase() + toCapitalise.substring(1).toLowerCase();
    }
}
