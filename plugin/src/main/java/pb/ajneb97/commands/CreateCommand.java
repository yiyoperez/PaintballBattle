package pb.ajneb97.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotation.Description;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import org.bukkit.entity.Player;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.juego.Partida;
import pb.ajneb97.managers.game.GameManager;
import pb.ajneb97.utils.MessageUtils;

public class CreateCommand extends MainCommand {

    private YamlDocument config;
    private YamlDocument messages;
    private GameManager gameManager;

    public CreateCommand(PaintballBattle plugin) {
        super(plugin);
        this.config = plugin.getConfigDocument();
        this.gameManager = plugin.getGameManager();
        this.messages = plugin.getMessagesDocument();
    }

    @SubCommand(value = "create")
    @Description("Create a new arena.")
    @Permission("paintball.admin.create")
    public void command(Player player, String arenaName) {
        if (gameManager.getPartida(arenaName) != null) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("arenaAlreadyExists")));
            return;
        }
        if (!config.contains("MainLobby")) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("noMainLobby")));
            return;
        }

        String equipo1 = "";
        String equipo2 = "";
        int i = 0;
        for (String key : config.getSection("teams").getRoutesAsStrings(false)) {
            if (i == 0) {
                equipo1 = key;
            } else {
                equipo2 = key;
                break;
            }
            i++;
        }

        Partida partida = new Partida(arenaName, config.getInt("arena_time_default"), equipo1, equipo2, config.getInt("team_starting_lives_default"));
        gameManager.agregarPartida(partida);
        player.sendMessage(MessageUtils.translateColor(messages.getString("arenaCreated").replace("%name%", arenaName)));
        player.sendMessage(MessageUtils.translateColor(messages.getString("arenaCreatedExtraInfo").replace("%name%", arenaName)));
    }
}
