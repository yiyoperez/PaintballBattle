package pb.ajneb97.core.utils.message;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pb.ajneb97.utils.enums.Messages;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MessageBuilder {

    private String path;
    private CommandSender sender;
    private Set<Player> playerSet;

    private final MessageHandler handler;
    private final List<Placeholder> placeholders = new ArrayList<>();

    public MessageBuilder(MessageHandler handler) {
        this.handler = handler;
    }

    public MessageBuilder toPlayerSet(Set<Player> playerSet) {
        this.playerSet = playerSet;
        return this;
    }

    public MessageBuilder toSender(CommandSender sender) {
        this.sender = sender;
        return this;
    }

    public MessageBuilder withPath(String path) {
        this.path = path;
        return this;
    }

    public MessageBuilder withMessage(Messages messages) {
        this.path = messages.getPath();
        return this;
    }

    public MessageBuilder addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public MessageBuilder withPlaceholders(Placeholder... placeholders) {
        this.placeholders.addAll(List.of(placeholders));
        return this;
    }

    public MessageBuilder withPlaceholders(List<Placeholder> placeholders) {
        this.placeholders.addAll(placeholders);
        return this;
    }

    private void sendToRecipients(MessageSenderFunction senderFunction) {
        if (path == null || (sender == null && (playerSet == null || playerSet.isEmpty()))) {
            return;
        }

        // Send to all players in playerSet if it's not empty
        if (playerSet != null && !playerSet.isEmpty()) {
            playerSet.forEach(player -> senderFunction.send(player, path, placeholders));
        }

        // Send to the sender if it's not null
        if (sender != null) {
            senderFunction.send(sender, path, placeholders);
        }
    }

    public void send() {
        sendToRecipients(handler::sendMessage);
    }

    public void sendCentered() {
        sendToRecipients(handler::sendCenteredMessage);
    }

    public void sendList() {
        sendToRecipients(handler::sendListMessage);
    }

    public void sendCenteredList() {
        sendToRecipients(handler::sendCenteredMessages);
    }

    public String build() {
        validatePath();
        return handler.getMessage(sender, path, placeholders);
    }

    public List<String> buildParsedList() {
        validatePath();
        return handler.getParsedMessages(sender, path, placeholders);
    }

    public List<String> buildList() {
        validatePath();
        return handler.getMessages(sender, path, placeholders);
    }

    public Component buildComponent() {
        validatePath();
        return handler.getComponent(sender, path, placeholders);
    }

    public List<Component> buildComponentList() {
        validatePath();
        return handler.getComponentMessages(sender, path, placeholders);
    }

    private void validatePath() {
        if (path == null) {
            throw new IllegalStateException("Message path must be provided.");
        }
    }

    @FunctionalInterface
    private interface MessageSenderFunction {
        void send(CommandSender sender, String path, List<Placeholder> placeholders);
    }
}