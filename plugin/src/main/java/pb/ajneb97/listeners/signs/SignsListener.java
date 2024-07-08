package pb.ajneb97.listeners.signs;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.juego.Partida;
import pb.ajneb97.managers.game.GameManager;
import pb.ajneb97.managers.game.PartidaManager;
import pb.ajneb97.managers.signs.SignManager;
import pb.ajneb97.utils.LocationUtils;
import pb.ajneb97.utils.MessageUtils;
import pb.ajneb97.utils.UtilidadesOtros;

import java.util.List;
import java.util.Objects;

public class SignsListener implements Listener {

    private final PaintballBattle plugin;
    private final GameManager gameManager;
    private final SignManager signManager;

    private final YamlDocument config;
    private final YamlDocument messages;

    public SignsListener(PaintballBattle plugin) {
        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
        this.signManager = plugin.getSignManager();
        this.config = plugin.getConfigDocument();
        this.messages = plugin.getMessagesDocument();
    }

    @EventHandler
    public void onSignCreate(SignChangeEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("paintball.admin")) return;

        String line = Objects.requireNonNull(event.getLine(0));
        if (line.equalsIgnoreCase("[Paintball]")) {

            String arena = Objects.requireNonNull(event.getLine(1));
            if (gameManager.getPartida(arena) != null) {

                Partida partida = gameManager.getPartida(arena);
                String state = switch (partida.getEstado()) {
                    case PLAYING -> messages.getString("signStatusIngame");
                    case STARTING -> messages.getString("signStatusStarting");
                    case WAITING -> messages.getString("signStatusWaiting");
                    case DISABLED -> messages.getString("signStatusDisabled");
                    case ENDING -> messages.getString("signStatusFinishing");
                };

                List<String> signFormat = messages.getStringList("signFormat").stream().limit(4).toList();
                signFormat.forEach(string ->
                        event.setLine(signFormat.indexOf(string), MessageUtils.translateColor(string
                                .replace("%arena%", arena)
                                .replace("%status%", state)
                                .replace("%max_players%", partida.getCantidadMaximaJugadores() + "")
                                .replace("%current_players%", partida.getCantidadActualJugadores() + "")
                        )));

                signManager.getLocationMap().put(partida.getNombre(), event.getBlock().getLocation());
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        World world = player.getWorld();
        Location blockLocation = block.getLocation();

        for (Location face : LocationUtils.getFaces(blockLocation)) {
            if (world.getBlockAt(face).getType().name().contains("SIGN")) {
                if (signManager.getLocationMap().values().stream().anyMatch(location -> location.equals(face))) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onSignBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location blockLocation = block.getLocation();

        if (!block.getType().name().contains("SIGN")) return;
        // Needs permission and shift to break.
        if (!player.isSneaking() && !player.hasPermission("paintball.admin")) {
            event.setCancelled(true);
            return;
        }

        signManager.getLocationMap().forEach((arena, location) -> {
            if (location.equals(blockLocation)) {
                signManager.getLocationMap().remove(arena);
            }
        });
    }

    @EventHandler
    public void onSignClickJoin(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (block == null || !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!block.getType().name().contains("SIGN")) return;

        signManager.getLocationMap().forEach((arena, location) -> {
            Partida partida = gameManager.getPartida(arena);
            handleGameJoin(partida, player);
        });
    }

    private void handleGameJoin(Partida partida, Player player) {
        if (partida == null) {
            player.sendMessage("Error.");
            return;
        }

        if (!partida.estaActivada()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("arenaDisabledError")));
            return;
        }

        if (gameManager.getPartidaJugador(player.getName()) != null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("alreadyInArena")));
            return;
        }

        if (partida.estaIniciada()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("arenaAlreadyStarted")));
            return;
        }

        if (partida.estaLlena()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("arenaIsFull")));
            return;
        }

        if (!UtilidadesOtros.pasaConfigInventario(player, config)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("errorClearInventory")));
            return;
        }

        PartidaManager.jugadorEntra(partida, player, plugin);
    }
}
