package pb.ajneb97.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import org.bukkit.entity.Player;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.managers.game.GameManager;
import pb.ajneb97.utils.MessageUtils;


public class DeleteCommand extends MainCommand {

    private YamlDocument messages;
    private GameManager gameManager;

    public DeleteCommand(PaintballBattle plugin) {
        super(plugin);
        this.messages = plugin.getMessagesDocument();
        this.gameManager = plugin.getGameManager();
    }

    @SubCommand(value = "delete")
    public void command(Player player, String arenaName) {
        if (gameManager.getPartida(arenaName) == null) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("arenaDoesNotExists")));
            return;
        }

        gameManager.removerPartida(arenaName);
        player.sendMessage(MessageUtils.translateColor(messages.getString("arenaDeleted").replace("%name%", arenaName)));
    }
}
