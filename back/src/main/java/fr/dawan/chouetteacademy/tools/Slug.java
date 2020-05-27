package fr.dawan.chouetteacademy.tools;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class Slug {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static String make(String input) {

        if (input == null || input.length() < 1) return "";

        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");

        if (nowhitespace.length() < 1) return "";

        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");

        if (slug.length() < 1) return "";

        slug = slug.toLowerCase(Locale.ENGLISH);

        StringBuilder sb = new StringBuilder(slug);

        int index;
        while ((index = sb.indexOf("--")) >= 0) {
            sb.deleteCharAt(index);
            if (sb.length() < 1) return "";
        }

        while (sb.charAt(0) == '-') {
            sb.deleteCharAt(0);
            if (sb.length() < 1) return "";
        }

        while (sb.charAt(sb.length() - 1) == '-') {
            sb.deleteCharAt(sb.length() - 1);
            if (sb.length() < 1) return "";
        }

        return sb.toString();
    }
}
