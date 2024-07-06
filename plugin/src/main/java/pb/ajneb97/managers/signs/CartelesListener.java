package pb.ajneb97.managers.signs;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import pb.ajneb97.juego.GameState;
import pb.ajneb97.juego.Partida;
import pb.ajneb97.managers.Checks;
import pb.ajneb97.managers.game.GameManager;
import pb.ajneb97.managers.game.PartidaManager;
import pb.ajneb97.utils.UtilidadesOtros;

import java.util.ArrayList;
import java.util.List;

public class CartelesListener implements Listener {

    private PaintballBattle plugin;
    private GameManager gameManager;

    public CartelesListener(PaintballBattle plugin) {
        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
    }

    //Al poner Cartel
    @EventHandler
    public void crearCartel(SignChangeEvent event) {
        Player jugador = event.getPlayer();
        if (jugador.isOp() || jugador.hasPermission("paintball.admin")) {
            if (event.getLine(0).equals("[Paintball]")) {
                String arena = event.getLine(1);
                if (arena != null && gameManager.getPartida(arena) != null) {
                    YamlDocument messages = plugin.getMessagesDocument();
                    Partida partida = gameManager.getPartida(arena);
                    String estado = "";
                    if (partida.getEstado().equals(GameState.PLAYING)) {
                        estado = messages.getString("signStatusIngame");
                    } else if (partida.getEstado().equals(GameState.STARTING)) {
                        estado = messages.getString("signStatusStarting");
                    } else if (partida.getEstado().equals(GameState.WAITING)) {
                        estado = messages.getString("signStatusWaiting");
                    } else if (partida.getEstado().equals(GameState.DISABLED)) {
                        estado = messages.getString("signStatusDisabled");
                    } else if (partida.getEstado().equals(GameState.ENDING)) {
                        estado = messages.getString("signStatusFinishing");
                    }

                    List<String> lista = messages.getStringList("signFormat");
                    for (int c = 0; c < lista.size(); c++) {
                        event.setLine(c, ChatColor.translateAlternateColorCodes('&', lista.get(c).replace("%arena%", arena).replace("%current_players%", partida.getCantidadActualJugadores() + "")
                                .replace("%max_players%", partida.getCantidadMaximaJugadores() + "").replace("%status%", estado)));
                    }

                    YamlDocument config = plugin.getConfigDocument();
                    List<String> listaCarteles = new ArrayList<String>();
                    if (config.contains("Signs." + arena)) {
                        listaCarteles = config.getStringList("Signs." + arena);
                    }
                    listaCarteles.add(event.getBlock().getX() + ";" + event.getBlock().getY() + ";" + event.getBlock().getZ() + ";" + event.getBlock().getWorld().getName());
                    config.set("Signs." + arena, listaCarteles);
                    plugin.saveConfig();
                }
            }
        }
    }

    @EventHandler
    public void eliminarCartel(BlockBreakEvent event) {
        Player jugador = event.getPlayer();
        Block block = event.getBlock();
        if (jugador.isOp() || jugador.hasPermission("paintball.admin")) {
            if (block.getType().name().contains("SIGN")) {
                YamlDocument config = plugin.getConfigDocument();
                if (config.contains("Signs")) {
                    for (String arena : config.getSection("Signs").getRoutesAsStrings(false)) {
                        List<String> listaCarteles = new ArrayList<>();
                        if (config.contains("Signs." + arena)) {
                            listaCarteles = config.getStringList("Signs." + arena);
                        }
                        for (int i = 0; i < listaCarteles.size(); i++) {
                            String[] separados = listaCarteles.get(i).split(";");
                            int x = Integer.parseInt(separados[0]);
                            int y = Integer.parseInt(separados[1]);
                            int z = Integer.parseInt(separados[2]);
                            World world = Bukkit.getWorld(separados[3]);
                            if (world != null) {
                                if (block.getX() == x && block.getY() == y && block.getZ() == z && world.getName().equals(block.getWorld().getName())) {
                                    listaCarteles.remove(i);
                                    config.set("Signs." + arena, listaCarteles);
                                    plugin.saveConfig();
                                    return;
                                }
                            }
                        }

                    }
                }

            }
        }
    }

    @EventHandler
    public void entrarPartida(PlayerInteractEvent event) {
        Player jugador = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (block.getType().name().contains("SIGN")) {
                YamlDocument config = plugin.getConfigDocument();
                if (config.contains("Signs")) {
                    YamlDocument messages = plugin.getMessagesDocument();
                    String prefix = ChatColor.translateAlternateColorCodes('&', messages.getString("prefix")) + " ";
                    for (String arena : config.getSection("Signs").getRoutesAsStrings(false)) {
                        Partida partida = gameManager.getPartida(arena);
                        if (partida != null) {
                            List<String> listaCarteles = new ArrayList<String>();
                            if (config.contains("Signs." + arena)) {
                                listaCarteles = config.getStringList("Signs." + arena);
                            }
                            for (int i = 0; i < listaCarteles.size(); i++) {
                                String[] separados = listaCarteles.get(i).split(";");
                                int x = Integer.parseInt(separados[0]);
                                int y = Integer.parseInt(separados[1]);
                                int z = Integer.parseInt(separados[2]);
                                World world = Bukkit.getWorld(separados[3]);
                                if (world != null) {
                                    if (block.getX() == x && block.getY() == y && block.getZ() == z && world.getName().equals(block.getWorld().getName())) {
                                        if (partida != null) {
                                            if (!Checks.checkTodo(plugin, jugador)) {
                                                return;
                                            }
                                            if (partida.estaActivada()) {
                                                if (gameManager.getPartidaJugador(jugador.getName()) == null) {
                                                    if (!partida.estaIniciada()) {
                                                        if (!partida.estaLlena()) {
                                                            if (!UtilidadesOtros.pasaConfigInventario(jugador, config)) {
                                                                jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("errorClearInventory")));
                                                                return;
                                                            }
                                                            PartidaManager.jugadorEntra(partida, jugador, plugin);
                                                        } else {
                                                            jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("arenaIsFull")));
                                                        }
                                                    } else {
                                                        jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("arenaAlreadyStarted")));
                                                    }
                                                } else {
                                                    jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("alreadyInArena")));
                                                }
                                            } else {
                                                jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("arenaDisabledError")));
                                            }
                                        }
                                        return;
                                    }
                                }
                            }

                        }
                    }
                }

            }
        }
    }
}
