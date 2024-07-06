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

public class EnableCommand extends MainCommand {

    private final PaintballBattle plugin;
    private GameManager gameManager;
    private YamlDocument messages;

    public EnableCommand(PaintballBattle plugin) {
        super(plugin);
        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
        this.messages = plugin.getMessagesDocument();
    }

    @SubCommand(value = "enable")
    @Permission("paintball.admin.enable")
    public void command(Player player, String arenaName) {
        Partida partida = gameManager.getPartida(arenaName);
        if (partida == null) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("arenaDoesNotExists")));
            return;
        }
        if (partida.estaActivada()) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("arenaAlreadyEnabled")));
            return;
        }
        if (partida.getLobby() == null) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("enableArenaLobbyError")));
            return;
        }
        if (partida.getTeam1().getSpawn() == null) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("enableArenaSpawnError").replace("%number%", "1")));
            return;
        }
        if (partida.getTeam2().getSpawn() == null) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("enableArenaSpawnError").replace("%number%", "2")));
            return;
        }

        partida.setEstado(GameState.WAITING);
        player.sendMessage(MessageUtils.translateColor(messages.getString("arenaEnabled").replace("%name%", arenaName)));
    }
}
