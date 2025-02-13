package pb.ajneb97.core.utils.message;

import java.util.List;

public class StringUtils {

    public static String replace(String string, final Placeholder... placeholders) {
        return replace(string, List.of(placeholders));
    }

    public static String replace(String string, final List<Placeholder> placeholders) {
        for (final Placeholder placeholder : placeholders) {
            string = string.replace(placeholder.getReplaced(), placeholder.getReplacement());
        }

        return string;
    }

    public static List<String> replace(final List<String> strings, final Placeholder... placeholders) {
        return replace(strings, List.of(placeholders));
    }

    public static List<String> replace(final List<String> strings, final List<Placeholder> placeholders) {
        strings.replaceAll(string -> replace(string, placeholders));
        return strings;
    }
}
