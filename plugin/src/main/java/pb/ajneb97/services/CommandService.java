package pb.ajneb97.services;

import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.core.BaseCommand;
import org.bukkit.command.CommandSender;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.commands.*;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class CommandService {

    private final Logger logger;
    private final PaintballBattle plugin;
    private final Set<BaseCommand> commandSet = new HashSet<>();
    private BukkitCommandManager<CommandSender> commandManager;

    public CommandService(PaintballBattle plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.commandManager = BukkitCommandManager.create(plugin);
    }

    public void start() {

        commandSet.add(new MainCommand(plugin));
        commandSet.add(new JoinCommand(plugin));
        commandSet.add(new ShopCommand(plugin));
        commandSet.add(new EditCommand(plugin));
        commandSet.add(new LeaveCommand(plugin));
        commandSet.add(new EnableCommand(plugin));
        commandSet.add(new CreateCommand(plugin));
        commandSet.add(new DeleteCommand(plugin));
        commandSet.add(new DisableCommand(plugin));
        commandSet.add(new GiveCoinsCommand(plugin));
        commandSet.add(new JoinRandomCommand(plugin));
        commandSet.add(new SetMainLobbyCommand(plugin));

        commandSet.forEach(command -> {
            logger.info("Trying to register command " + command.getClass().getSimpleName());
            commandManager.registerCommand(command);
        });
    }

    public void finish() {
        if (commandManager == null) return;
        if (!commandSet.isEmpty()) {
            commandSet.forEach(command -> {
                logger.info("Trying to un-register command " + command.getClass().getSimpleName());
                commandManager.unregisterCommand(command);
            });
        }
    }

}
