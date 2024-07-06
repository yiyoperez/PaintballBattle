package pb.ajneb97.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import org.bukkit.entity.Player;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.juego.Partida;
import pb.ajneb97.managers.Checks;
import pb.ajneb97.managers.game.GameManager;
import pb.ajneb97.managers.game.PartidaManager;
import pb.ajneb97.utils.MessageUtils;
import pb.ajneb97.utils.UtilidadesOtros;

public class JoinCommand extends MainCommand {

    private YamlDocument config;
    private YamlDocument messages;
    private GameManager gameManager;
    private PaintballBattle plugin;

    public JoinCommand(PaintballBattle plugin) {
        super(plugin);

        this.plugin = plugin;
        this.config = plugin.getConfigDocument();
        this.gameManager = plugin.getGameManager();
        this.messages = plugin.getMessagesDocument();
    }

    @SubCommand(value = "join")
    public void command(Player player, String arenaName) {
        // /paintball join <arena>
        if (!Checks.checkTodo(plugin, player)) {
            return;
        }

        Partida partida = gameManager.getPartida(arenaName);
        if (partida == null) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("arenaDoesNotExists")));
            return;
        }

        if (!partida.estaActivada()) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("arenaDisabledError")));
            return;
        }

        if (gameManager.getPartidaJugador(player.getName()) != null) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("alreadyInArena")));
            return;
        }

        if (partida.estaIniciada()) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("arenaAlreadyStarted")));
            return;
        }

        if (partida.estaLlena()) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("arenaIsFull")));
            return;
        }

        if (!UtilidadesOtros.pasaConfigInventario(player, config)) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("errorClearInventory")));
            return;
        }

        PartidaManager.jugadorEntra(partida, player, plugin);
    }
}
