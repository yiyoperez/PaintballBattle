package pb.ajneb97.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import org.bukkit.entity.Player;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.juego.GameEdit;
import pb.ajneb97.juego.Partida;
import pb.ajneb97.managers.Checks;
import pb.ajneb97.managers.game.GameManager;
import pb.ajneb97.managers.inventory.InventarioAdmin;
import pb.ajneb97.utils.MessageUtils;

public class EditCommand extends MainCommand {

    private PaintballBattle plugin;
    private GameManager gameManager;
    private YamlDocument messages;

    public EditCommand(PaintballBattle plugin) {
        super(plugin);

        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
        this.messages = plugin.getMessagesDocument();
    }

    @SubCommand(value = "edit")
    @Permission("paintball.admin.edit")
    public void command(Player player, String arenaName) {
        if (!Checks.checkTodo(plugin, player)) {
            return;
        }

        Partida partida = gameManager.getPartida(arenaName);
        if (partida == null) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("arenaDoesNotExists")));
            return;
        }

        if (partida.estaActivada()) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("arenaMustBeDisabled")));
            return;
        }

        GameEdit gameEdit = plugin.getPartidaEditando();
        if (gameEdit != null) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("arenaModifyingError")));
            return;
        }

        InventarioAdmin.crearInventario(player, partida, plugin);
    }
}
