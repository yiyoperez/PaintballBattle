package pb.ajneb97.core.utils.message;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pb.ajneb97.core.logger.Logger;
import pb.ajneb97.utils.enums.Messages;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MessageBuilder {

    private final MessageHandler handler;
    private final List<Placeholder> placeholders = new ArrayList<>();

    private String path;
    private CommandSender sender;
    //TODO: Allow to send built message to a set of players.
    private Set<Player> playerSet;

    public MessageBuilder(MessageHandler handler) {
        this.handler = handler;
    }

    public MessageBuilder(MessageHandler handler, CommandSender sender) {
        this.handler = handler;
        this.sender = sender;
    }

    public MessageBuilder(MessageHandler handler, Set<Player> playerSet) {
        this.handler = handler;
        this.playerSet = playerSet;
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

    public void send() {
        if (sender == null) {
            Logger.info("Sender must be provided.", Logger.LogType.WARNING);
            return;
        }
        if (path == null) {
            Logger.info("Message path must be provided.", Logger.LogType.WARNING);
            return;
        }
        handler.sendMessage(sender, path, placeholders.toArray(new Placeholder[0]));
    }

    public void sendCentered() {
        if (sender == null) {
            Logger.info("Sender must be provided.", Logger.LogType.WARNING);
            return;
        }
        if (path == null) {
            Logger.info("Message path must be provided.", Logger.LogType.WARNING);
            return;
        }
        handler.sendCenteredMessage(sender, path, placeholders.toArray(new Placeholder[0]));
    }

    public void sendList() {
        if (sender == null) {
            Logger.info("Sender must be provided.", Logger.LogType.WARNING);
            return;
        }
        if (path == null) {
            Logger.info("Message path must be provided.", Logger.LogType.WARNING);
            return;
        }

        handler.sendListMessage(sender, path, placeholders.toArray(new Placeholder[0]));
    }

    public void sendCenteredList() {
        if (sender == null) {
            Logger.info("Sender must be provided.", Logger.LogType.WARNING);
            return;
        }
        if (path == null) {
            Logger.info("Message path must be provided.", Logger.LogType.WARNING);
            return;
        }
        handler.sendCenteredMessages(sender, path, placeholders.toArray(new Placeholder[0]));
    }

    public String build() {
        if (path == null) {
            throw new IllegalStateException("Message path must be provided.");
        }
        return handler.getMessage(sender, path, placeholders.toArray(new Placeholder[0]));
    }

    public List<String> buildParsedList() {
        if (path == null) {
            throw new IllegalStateException("Message path must be provided.");
        }
        return handler.getParsedMessages(sender, path, placeholders.toArray(new Placeholder[0]));
    }

    public List<String> buildList() {
        if (path == null) {
            throw new IllegalStateException("Message path must be provided.");
        }
        return handler.getMessages(sender, path, placeholders.toArray(new Placeholder[0]));
    }

    public Component buildComponent() {
        if (path == null) {
            throw new IllegalStateException("Message path must be provided.");
        }
        return handler.getComponent(sender, path, placeholders.toArray(new Placeholder[0]));
    }

    public List<Component> buildComponentList() {
        if (path == null) {
            throw new IllegalStateException("Message path must be provided.");
        }
        return handler.getComponentMessages(sender, path, placeholders.toArray(new Placeholder[0]));
    }
}