package pb.ajneb97.managers.signs;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitRunnable;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.juego.Partida;
import pb.ajneb97.managers.game.GameManager;
import pb.ajneb97.utils.MessageUtils;

import java.util.List;

public class SignUpdateRunnable extends BukkitRunnable {

    private final YamlDocument messages;
    private final SignManager signManager;
    private final GameManager gameManager;

    public SignUpdateRunnable(PaintballBattle plugin) {
        this.signManager = plugin.getSignManager();
        this.gameManager = plugin.getGameManager();
        this.messages = plugin.getMessagesDocument();
    }

    @Override
    @Deprecated
    public void run() {
        if (signManager.getLocationMap().isEmpty()) return;

        signManager.getLocationMap().forEach((arena, location) -> {
            if (!location.getChunk().isLoaded()) return;

            Block block = location.getBlock();
            Sign sign = (Sign) block.getState();

            Partida partida = gameManager.getPartida(arena);
            String state = switch (partida.getEstado()) {
                case PLAYING -> messages.getString("signStatusIngame");
                case STARTING -> messages.getString("signStatusStarting");
                case WAITING -> messages.getString("signStatusWaiting");
                case DISABLED -> messages.getString("signStatusDisabled");
                case ENDING -> messages.getString("signStatusFinishing");
            };

            List<String> signFormat = messages.getStringList("signFormat").stream().limit(4).toList();
            signFormat.forEach(line ->
                    sign.setLine(signFormat.indexOf(line), MessageUtils.translateColor(line
                            .replace("%arena%", arena)
                            .replace("%status%", state)
                            .replace("%max_players%", String.valueOf(partida.getCantidadMaximaJugadores()))
                            .replace("%current_players%", String.valueOf(partida.getCantidadActualJugadores())))));

            sign.update();
        });
    }
}
