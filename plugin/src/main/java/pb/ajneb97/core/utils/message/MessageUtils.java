package pb.ajneb97.core.utils.message;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public class MessageUtils {

    public static String translateColor(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String[] translateToArray(String[] text) {
        return translateColor(Arrays.asList(text)).toArray(new String[text.length]);
    }

    public static List<String> translateColor(String[] lines) {
        List<String> toReturn = Arrays.asList(lines.clone());
        toReturn.replaceAll(MessageUtils::translateColor);
        return toReturn;
    }

    public static List<String> translateColor(List<String> list) {
        list.replaceAll(MessageUtils::translateColor);
        return list;
    }

    public static String strip(String text) {
        return ChatColor.stripColor(translateColor(text));
    }
}
