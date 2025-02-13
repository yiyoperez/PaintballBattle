package pb.ajneb97.core.utils.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class MessageUtils {

    public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    public static final LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.legacySection();

    public static String translateLegacyColor(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String[] translateLegacyToArray(String[] text) {
        return translateLegacyColor(Arrays.asList(text)).toArray(new String[text.length]);
    }

    public static List<String> translateLegacyColor(String[] lines) {
        List<String> toReturn = Arrays.asList(lines.clone());
        toReturn.replaceAll(MessageUtils::translateLegacyColor);
        return toReturn;
    }

    public static List<String> translateLegacyColor(List<String> list) {
        list.replaceAll(MessageUtils::translateLegacyColor);
        return list;
    }

    public static String strip(String text) {
        return ChatColor.stripColor(translateLegacyColor(text));
    }

    public static Component parse(String message) {
        return MINI_MESSAGE.deserialize(message);
    }

    public static @NotNull String parseMessage(String message) {
        return LEGACY_COMPONENT_SERIALIZER.serialize(parse(message));
    }
}
