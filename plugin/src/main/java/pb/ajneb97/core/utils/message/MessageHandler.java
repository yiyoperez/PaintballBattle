package pb.ajneb97.core.utils.message;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pb.ajneb97.core.utils.SPlugin;
import pb.ajneb97.utils.enums.Messages;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MessageHandler {

    private final SPlugin plugin;
    private final YamlDocument messages;

    public MessageHandler(SPlugin plugin, YamlDocument messages) {
        this.plugin = plugin;
        this.messages = messages;
    }

    private String replacePlaceholders(String message, List<Placeholder> placeholders) {
        if (placeholders != null && !placeholders.isEmpty()) {
            message = StringUtils.replace(message, placeholders);
        }
        return MessageUtils.translateColor(message);
    }

    private String handlePAPIPlaceholders(CommandSender sender, String message) {
        if (plugin.isPluginEnabled("PlaceholderAPI") && sender instanceof Player) {
            message = PlaceholderAPI.setPlaceholders((Player) sender, message);
        }
        return message;
    }

    public String intercept(CommandSender sender, String message, List<Placeholder> placeholders) {
        return replacePlaceholders(intercept(sender, message), placeholders);
    }

    public String intercept(CommandSender sender, String message) {
        message = handlePAPIPlaceholders(sender, message);
        return replacePlaceholders(intercept(message), Collections.emptyList());
    }

    public String intercept(String message) {
        if (message.contains("%prefix%")) {
            String prefix = messages.getString("PREFIX", "");

            if (!prefix.isEmpty()) {
                message = StringUtils.replace(message, new Placeholder("%prefix%", prefix));
            }
        }
        return MessageUtils.translateColor(message);
    }

    public String getRawMessage(String path) {
        if (messages.isList(path) && !messages.getStringList(path).isEmpty()) {
            return String.join(" ", messages.getStringList(path));
        }
        return messages.getString(path, String.format("Message not found in path %s", path));
    }

    public String getRawMessage(String path, Placeholder... placeholders) {
        return getRawMessage(path, Arrays.asList(placeholders));
    }

    public String getRawMessage(String path, Object... placeholders) {
        return String.format(getRawMessage(path), placeholders);
    }

    public String getRawMessage(String path, List<Placeholder> placeholders) {
        return replacePlaceholders(getRawMessage(path), placeholders);
    }

    public String getMessage(Messages message, Placeholder... placeholders) {
        return intercept(getRawMessage(message.getPath(), placeholders));
    }

    public String getMessage(String path, Placeholder... placeholders) {
        return intercept(getRawMessage(path, placeholders));
    }

    public String getMessage(CommandSender sender, String path, Placeholder... placeholders) {
        return getMessage(sender, path, Arrays.asList(placeholders));
    }

    public String getMessage(CommandSender sender, String path, List<Placeholder> placeholders) {
        return intercept(sender, getRawMessage(path, placeholders), placeholders);
    }

    public List<String> getMessages(CommandSender sender, String path, Placeholder... placeholders) {
        return getMessages(sender, path, Arrays.asList(placeholders));
    }

    public List<String> getMessages(CommandSender sender, String path, List<Placeholder> placeholders) {
        List<String> rawMessages = messages.isList(path) ? messages.getStringList(path) : Collections.singletonList(getRawMessage(path));
        return rawMessages.stream()
                .map(message -> intercept(sender, message, placeholders))
                .collect(Collectors.toList());
    }

    private void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(MessageUtils.translateColor(message));
    }

    public void sendCenteredMessage(CommandSender sender, Messages messages, Placeholder... placeholders) {
        sendCenteredMessage(sender, messages.getPath(), placeholders);
    }

    public void sendCenteredMessage(CommandSender sender, String path, Placeholder... placeholders) {
        sendMessage(sender, CenteredString.formatMessage(getMessage(sender, path, placeholders)));
    }

    public void sendManualMessage(CommandSender sender, String message, Placeholder... placeholders) {
        sendManualMessage(sender, message, Arrays.asList(placeholders));
    }

    public void sendManualMessage(CommandSender sender, String message, List<Placeholder> placeholders) {
        sendMessage(sender, intercept(sender, message, placeholders));
    }

    public void sendMessage(CommandSender sender, Messages message, Placeholder... placeholders) {
        sendMessage(sender, message.getPath(), Arrays.asList(placeholders));
    }

    public void sendMessage(CommandSender sender, String path, Placeholder... placeholders) {
        sendMessage(sender, path, Arrays.asList(placeholders));
    }

    public void sendMessage(CommandSender sender, String path, List<Placeholder> placeholders) {
        sendMessage(sender, getMessage(sender, path, placeholders));
    }

    public List<String> getRawStringList(String path, Placeholder... placeholders) {
        return getRawStringList(path, Arrays.asList(placeholders));
    }

    public List<String> getRawStringList(String path, List<Placeholder> placeholders) {
        List<String> rawMessages = messages.isList(path) ? messages.getStringList(path) : Collections.singletonList(getRawMessage(path));
        return rawMessages.stream()
                .map(message -> replacePlaceholders(message, placeholders))
                .collect(Collectors.toList());
    }

    public void sendCenteredListMessage(CommandSender sender, Messages messages, Placeholder... placeholders) {
        sendCenteredListMessage(sender, messages.getPath(), placeholders);
    }

    public void sendCenteredListMessage(CommandSender sender, String path, Placeholder... placeholders) {
        getMessages(sender, path, placeholders).forEach(message -> sendMessage(sender, CenteredString.formatMessage(message)));
    }

    public void sendListMessage(CommandSender sender, Messages messages, Placeholder... placeholders) {
        sendListMessage(sender, messages.getPath(), Arrays.asList(placeholders));
    }

    public void sendListMessage(CommandSender sender, String path, Placeholder... placeholders) {
        sendListMessage(sender, path, Arrays.asList(placeholders));
    }

    public void sendListMessage(CommandSender sender, String path, List<Placeholder> placeholders) {
        getMessages(sender, path, placeholders).forEach(string -> sendMessage(sender, string));
    }
}
