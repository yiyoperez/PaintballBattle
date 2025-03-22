package pb.ajneb97.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;
import pb.ajneb97.commands.extra.PaintCommand;
import pb.ajneb97.commons.cache.Cache;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.core.utils.message.Placeholder;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.structures.game.GameEditSessionBuilder;
import pb.ajneb97.utils.enums.Messages;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

import java.util.UUID;

@Command(name = "paintball setup")
@Description("Create a new arena.")
@Permission("paintball.admin.create")
public class SetupCommand implements PaintCommand {

    @Inject
    @Named("config")
    private YamlDocument config;
    @Inject
    private GameManager gameManager;
    @Inject
    private MessageHandler messageHandler;
    @Inject
    @Named("edit-session-cache")
    private Cache<UUID, GameEditSessionBuilder> editSession;

    @Execute
    public void command(@Context Player player, @Arg("arenaName") String arenaName) {
        if (!config.contains("MainLobby")) {
            messageHandler.sendMessage(player, Messages.NO_MAIN_LOBBY);
            return;
        }
        if (gameManager.gameExists(arenaName)) {
            messageHandler.sendMessage(player, Messages.ARENA_ALREADY_EXISTS);
            return;
        }
        if (editSession.exists(player.getUniqueId())) {
            messageHandler.sendManualMessage(player, "You are no longer in edit setup.");
            messageHandler.sendManualMessage(player, "NO changes have been made.");
            return;
        }

        editSession.add(player.getUniqueId(), new GameEditSessionBuilder(arenaName));
        messageHandler.sendManualMessage(player, "Please setup game lobby, team spawn and name.");
    }

    @Execute(name = "spawn")
    public void commandSpawn(@Context Player player) {
        editSession.find(player.getUniqueId()).ifPresentOrElse(gameBuilder -> {
                    gameBuilder.setLobby(player.getLocation().clone());
                    messageHandler.sendMessage(player, Messages.LOBBY_DEFINED);
                }
                , () -> messageHandler.sendManualMessage(player, "You are not in a setup."));
    }

    @Execute(name = "point1")
    public void commandPointOne(@Context Player player) {
        editSession.find(player.getUniqueId()).ifPresentOrElse(gameBuilder -> {
                    gameBuilder.setPointOne(player.getLocation().clone());
                    messageHandler.sendMessage(player, Messages.SPAWN_TEAM_DEFINED, new Placeholder("%number%", 1), new Placeholder("%name%", gameBuilder.getName()));
                }
                , () -> messageHandler.sendManualMessage(player, "You are not in a setup."));
    }

    @Execute(name = "point2")
    public void commandPointTwo(@Context Player player) {
        editSession.find(player.getUniqueId()).ifPresentOrElse(gameBuilder -> {
                    gameBuilder.setPointTwo(player.getLocation().clone());
                    messageHandler.sendMessage(player, Messages.SPAWN_TEAM_DEFINED, new Placeholder("%number%", 1), new Placeholder("%name%", gameBuilder.getName()));
                }
                , () -> messageHandler.sendManualMessage(player, "You are not in a setup."));
    }

    //TODO: add team.

    @Execute(name = "save")
    public void commandSave(@Context Player player) {
        editSession.find(player.getUniqueId()).ifPresentOrElse(gameBuilder -> {
                    // Enable when saving?
                    gameBuilder.setEnabled(true);
                    gameManager.addGame(gameBuilder.build());
                    messageHandler.sendManualMessage(player, "SETUP GAME SAVED", new Placeholder("%name%", gameBuilder.getName()));
                }
                , () -> messageHandler.sendManualMessage(player, "You are not in a setup."));
    }
}
