package pb.ajneb97.managers.signs;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitScheduler;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.juego.GameState;
import pb.ajneb97.juego.Partida;

import java.util.ArrayList;
import java.util.List;

public class CartelesAdmin {

    private int taskID;
    private PaintballBattle plugin;

    public CartelesAdmin(PaintballBattle plugin) {
        this.plugin = plugin;
    }

    public int getTaskID() {
        return this.taskID;
    }

    public void actualizarCarteles() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                ejecutarActualizarCarteles();
            }
        }, 0, 30L);
    }

    protected void ejecutarActualizarCarteles() {
        YamlDocument config = plugin.getConfigDocument();
        YamlDocument messages = plugin.getMessagesDocument();
        if (config.contains("Signs")) {
            for (String arena : config.getSection("Signs").getRoutesAsStrings(false)) {
                Partida partida = plugin.getGameManager().getPartida(arena);
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
                            int chunkX = x >> 4;
                            int chunkZ = z >> 4;
                            if (!world.isChunkLoaded(chunkX, chunkZ)) {
                                continue;
                            }
                            Block block = world.getBlockAt(x, y, z);

                            if (block.getType().name().contains("SIGN")) {
                                Sign sign = (Sign) block.getState();
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
                                    sign.setLine(c, ChatColor.translateAlternateColorCodes('&', lista.get(c).replace("%arena%", arena).replace("%current_players%", partida.getCantidadActualJugadores() + "")
                                            .replace("%max_players%", partida.getCantidadMaximaJugadores() + "").replace("%status%", estado)));
                                }

                                sign.update();
                            }
                        }
                    }

                }
            }
        }

    }
}
