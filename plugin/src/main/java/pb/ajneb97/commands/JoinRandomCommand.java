package pb.ajneb97.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.juego.Partida;
import pb.ajneb97.managers.game.GameManager;
import pb.ajneb97.managers.game.PartidaManager;
import pb.ajneb97.utils.MessageUtils;

public class JoinRandomCommand extends MainCommand {

    private PaintballBattle plugin;
    private GameManager gameManager;
    private YamlDocument messages;

    public JoinRandomCommand(PaintballBattle plugin) {
        super(plugin);

        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
        this.messages = plugin.getMessagesDocument();
    }

    @SubCommand(value = "joinrandom")
    public void commnad(Player player) {
        // /paintball joinrandom
        if (gameManager.getPartidaJugador(player.getName()) != null) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("alreadyInArena")));
            return;
        }

        Partida partidaNueva = PartidaManager.getPartidaDisponible(plugin);
        if (partidaNueva == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("noArenasAvailable")));
            return;
        }

        PartidaManager.jugadorEntra(partidaNueva, player, plugin);
    }
}
