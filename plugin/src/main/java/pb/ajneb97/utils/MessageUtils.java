package pb.ajneb97.utils;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public class MessageUtils {

    public static String translateColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> translateColor(String... strings) {
        return translateColor(Arrays.asList(strings));
    }

    public static List<String> translateColor(List<String> list) {
        list.forEach(MessageUtils::translateColor);
        return list;
    }
}
