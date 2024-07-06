package pb.ajneb97.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import org.bukkit.entity.Player;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.juego.GameState;
import pb.ajneb97.juego.Partida;
import pb.ajneb97.managers.game.GameManager;
import pb.ajneb97.utils.MessageUtils;

public class DisableCommand extends MainCommand {

    private YamlDocument messages;
    private GameManager gameManager;

    public DisableCommand(PaintballBattle plugin) {
        super(plugin);

        this.gameManager = plugin.getGameManager();
        this.messages = plugin.getMessagesDocument();
    }

    @SubCommand(value = "disable")
    @Permission("paintball.admin.disable")
    public void command(Player player, String arenaName) {
        Partida partida = gameManager.getPartida(arenaName);
        if (partida == null) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("arenaDoesNotExists")));
            return;
        }

        if (!partida.estaActivada()) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("arenaAlreadyDisabled")));
            return;
        }

        partida.setEstado(GameState.DISABLED);
        player.sendMessage(MessageUtils.translateColor(messages.getString("arenaDisabled").replace("%name%", arenaName)));
    }
}
