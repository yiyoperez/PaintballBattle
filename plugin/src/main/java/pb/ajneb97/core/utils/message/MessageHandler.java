package pb.ajneb97.core.utils.message;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pb.ajneb97.core.utils.SPlugin;
import pb.ajneb97.utils.enums.Messages;

import java.util.ArrayList;
import java.util.List;

public class MessageHandler {

    private final YamlDocument lang;
    private final boolean placeholderAPI;

    public MessageHandler(SPlugin plugin, YamlDocument messages) {
        this.lang = messages;
        this.placeholderAPI = plugin.isPluginEnabled("PlaceholderAPI");
    }

    // # ~ # -------- MESSAGE PROCESSING -------- # ~ # //

    private String processMessage(String message, Placeholder... placeholders) {
        // Uses processMessage instead of replace to reduce code repetition.
        return processMessage(message, List.of(placeholders));
    }

    private String processMessage(String message, List<Placeholder> placeholders) {
        return replacePlaceholders(message, placeholders);
    }

    private String processMessage(CommandSender sender, String message, Placeholder... placeholders) {
        // Uses processMessage instead of replace to reduce code repetition.
        return processMessage(sender, message, List.of(placeholders));
    }

    private String processMessage(CommandSender sender, String message, List<Placeholder> placeholders) {
        message = applyPlaceholderAPI(sender, message);
        return replacePlaceholders(message, placeholders);
    }

    // # ~ # -------- COMPONENT PROCESSING -------- # ~ # //

    private Component processComponent(String message, Placeholder... placeholders) {
        return processComponent(message, List.of(placeholders));
    }

    private Component processComponent(String message, List<Placeholder> placeholders) {
        message = replacePlaceholders(message, placeholders);
        return MessageUtils.parse(message);
    }

    private Component processComponent(CommandSender sender, String message, Placeholder... placeholders) {
        return processComponent(sender, message, List.of(placeholders));
    }

    private Component processComponent(CommandSender sender, String message, List<Placeholder> placeholders) {
        message = applyPlaceholderAPI(sender, message);
        message = replacePlaceholders(message, placeholders);

        return MessageUtils.parse(message);
    }


    // # ~ # -------- GET COMPLETELY MESSAGES -------- # ~ # //

    public String getMessage(Messages messages, Placeholder... placeholders) {
        return getMessage(messages.getPath(), placeholders);
    }

    public String getMessage(String path, Placeholder... placeholders) {
        String message = getParsedMessage(path, placeholders);
        return MessageUtils.parseMessage(message);
    }

    public String getMessage(CommandSender sender, String path, Placeholder... placeholders) {
        String message = getParsedMessage(sender, path, placeholders);
        return MessageUtils.parseMessage(message);
    }

    // # ~ # -------- GET PARSED MESSAGES -------- # ~ # //

    public String getParsedMessage(Messages messages, Placeholder... placeholders) {
        return getParsedMessage(messages.getPath(), placeholders);
    }

    public String getParsedMessage(String path, Placeholder... placeholders) {
        String message = getRawMessage(path);
        return replacePlaceholders(message, placeholders);
    }

    public String getParsedMessage(CommandSender sender, String path, Placeholder... placeholders) {
        String message = getRawMessage(path);
        return processMessage(sender, message, placeholders);
    }

    // # ~ # -------- GET COMPONENT MESSAGES -------- # ~ # //

    public Component getComponent(Messages messages, Placeholder... placeholders) {
        return getComponent(messages.getPath(), placeholders);
    }

    public Component getComponent(String path, Placeholder... placeholders) {
        String message = getParsedMessage(path, placeholders);
        return MessageUtils.parse(message);
    }

    public Component getComponent(CommandSender sender, String path, Placeholder... placeholders) {
        String message = getParsedMessage(sender, path, placeholders);
        return MessageUtils.parse(message);
    }

    // # ~ # -------- RAW MESSAGE -------- # ~ # //

    public String getRawMessage(Messages messages) {
        return getRawMessage(messages.getPath());
    }

    public String getRawMessage(String path) {
        if (lang.getString(path) == null) {
            return String.format("Message not found in path: %s", path);
        }

        return lang.getString(path);
    }

    // # ~ # -------- GET LISTS -------- # ~ # //

    public List<String> getMessages(Messages messages, Placeholder... placeholders) {
        return getMessages(messages.getPath(), placeholders);
    }

    public List<String> getMessages(String path, Placeholder... placeholders) {
        List<String> messages = getParsedMessages(path, placeholders);

        List<String> list = new ArrayList<>();
        for (String string : messages) {
            list.add(MessageUtils.parseMessage(string));
        }
        return list;
    }

    public List<String> getMessages(CommandSender sender, String path, Placeholder... placeholders) {
        List<String> messages = getParsedMessages(sender, path, placeholders);

        List<String> list = new ArrayList<>();
        for (String message : messages) {
            list.add(MessageUtils.parseMessage(message));
        }
        return list;
    }

    // # ~ # -------- GET PARSED LISTS -------- # ~ # //

    public List<String> getParsedMessages(Messages messages, Placeholder... placeholders) {
        return getParsedMessages(messages.getPath(), placeholders);
    }

    public List<String> getParsedMessages(String path, Placeholder... placeholders) {
        List<String> messages = getRawStringList(path);

        List<String> list = new ArrayList<>();
        for (String string : messages) {
            String s = replacePlaceholders(string, placeholders);
            list.add(s);
        }
        return list;
    }

    public List<String> getParsedMessages(CommandSender sender, String path, Placeholder... placeholders) {
        List<String> rawMessages = getRawStringList(path);

        List<String> processedMessages = new ArrayList<>();
        for (String message : rawMessages) {
            processedMessages.add(processMessage(sender, message, placeholders));
        }

        return processedMessages;
    }

    // # ~ # -------- GET COMPONENT LISTS -------- # ~ # //

    public List<Component> getComponentMessages(Messages messages, Placeholder... placeholders) {
        return getComponentMessages(messages.getPath(), placeholders);
    }

    public List<Component> getComponentMessages(String path, Placeholder... placeholders) {
        List<String> messages = getParsedMessages(path, placeholders);

        List<Component> list = new ArrayList<>();
        for (String string : messages) {
            list.add(MessageUtils.parse(string));
        }
        return list;
    }

    public List<Component> getComponentMessages(CommandSender sender, String path, Placeholder... placeholders) {
        List<String> messages = getParsedMessages(sender, path, placeholders);

        List<Component> list = new ArrayList<>();
        for (String message : messages) {
            list.add(MessageUtils.parse(message));
        }
        return list;
    }

    // # ~ # -------- RAW LISTS -------- # ~ # //

    public List<String> getRawStringList(Messages messages) {
        return getRawStringList(messages.getPath());
    }

    public List<String> getRawStringList(String path) {
        return lang.isList(path) ? lang.getStringList(path) : List.of(getRawMessage(path));
    }

    // # ~ # -------- SEND METHODS -------- # ~ # //

    public void sendMessage(CommandSender sender, Messages messages, Placeholder... placeholders) {
        sendMessage(sender, messages.getPath(), placeholders);
    }

    public void sendMessage(CommandSender sender, String path, Placeholder... placeholders) {
        sendManualMessage(sender, getMessage(sender, path, placeholders));
    }

    public void sendListMessage(CommandSender sender, Messages messages, Placeholder... placeholders) {
        sendListMessage(sender, messages.getPath(), placeholders);
    }

    public void sendListMessage(CommandSender sender, String path, Placeholder... placeholders) {
        List<String> messages = getMessages(sender, path, placeholders);
        for (String message : messages) {
            sendManualMessage(sender, message);
        }
    }

    // # ~ # -------- SPECIAL METHODS -------- # ~ # //

    public void sendManualMessage(CommandSender sender, String message, Placeholder... placeholders) {
        sender.sendMessage(processMessage(message, placeholders));
    }

    public void sendManualMessage(CommandSender sender, String message, List<Placeholder> placeholders) {
        sender.sendMessage(processMessage(message, placeholders));
    }

    public void sendCenteredMessage(CommandSender sender, Messages messages, Placeholder... placeholders) {
        sendCenteredMessage(sender, messages.getPath(), placeholders);
    }

    public void sendCenteredMessage(CommandSender sender, String path, Placeholder... placeholders) {
        String message = getParsedMessage(sender, path, placeholders);
        sendManualMessage(sender, CenteredString.formatMessage(message));
    }

    public void sendCenteredMessages(CommandSender sender, Messages message, Placeholder... placeholders) {
        sendCenteredMessages(sender, message.getPath(), placeholders);
    }

    public void sendCenteredMessages(CommandSender sender, String path, Placeholder... placeholders) {
        List<String> messages = getMessages(sender, path, placeholders);
        messages.forEach(message -> sendManualMessage(sender, CenteredString.formatMessage(message)));
    }

    // # ~ # -------------- * -------------- # ~ # //
    // # ~ # -------- UTILS METHODS -------- # ~ # //
    // # ~ # -------------- * -------------- # ~ # //

    public String applyPlaceholderAPI(CommandSender sender, String message) {
        if (!placeholderAPI || !(sender instanceof Player)) {
            return message;
        }

        message = PlaceholderAPI.setPlaceholders((Player) sender, message);
        return message;
    }

    public String replacePlaceholders(String message, Placeholder... placeholders) {
        return replacePlaceholders(message, List.of(placeholders));
    }

    public String replacePlaceholders(String message, List<Placeholder> placeholders) {
        message = replaceLocalPlaceholders(message);

        if (placeholders == null || placeholders.isEmpty()) {
            return message;
        }

        message = StringUtils.replace(message, placeholders);
        return message;
    }

    public String replaceLocalPlaceholders(String message) {
        String prefix = lang.getString("PREFIX", "");
        if (prefix.isEmpty()) {
            return message;
        }

        message = StringUtils.replace(message, new Placeholder("%prefix%", prefix));
        return message;
    }
}
