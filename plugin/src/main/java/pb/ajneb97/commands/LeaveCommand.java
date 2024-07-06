package pb.ajneb97.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import org.bukkit.entity.Player;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.juego.Partida;
import pb.ajneb97.managers.game.GameManager;
import pb.ajneb97.managers.game.PartidaManager;
import pb.ajneb97.utils.MessageUtils;

public class LeaveCommand extends MainCommand {

    private PaintballBattle plugin;
    private GameManager gameManager;
    private YamlDocument messages;

    public LeaveCommand(PaintballBattle plugin) {
        super(plugin);
        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
        this.messages = plugin.getMessagesDocument();
    }

    @SubCommand(value = "leave")
    public void command(Player player) {
        Partida partida = gameManager.getPartidaJugador(player.getName());
        if (partida == null) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("notInAGame")));
            return;
        }
        
        PartidaManager.jugadorSale(partida, player, false, plugin, false);
    }
}
