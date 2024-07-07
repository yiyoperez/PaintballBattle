package pb.ajneb97.utils;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.database.JugadorDatos;

import java.util.ArrayList;

public class UtilidadesHologramas {

    public static int getCantidadLineasHolograma(PaintballBattle plugin) {
        YamlDocument config = plugin.getConfigDocument();
        YamlDocument messages = plugin.getMessagesDocument();
        int lineas = messages.getStringList("topHologramFormat").size();
        lineas = lineas + config.getInt("top_hologram_number_of_players");
        return lineas;
    }

    public static double determinarY(Location location, int cantidadLineasHolograma) {
        double cantidad = cantidadLineasHolograma * 0.15;
        return cantidad;
    }

    //Este metodo se usa solo para monthly o weekly
    public static void getTopPlayersSQL(final PaintballBattle plugin, final String tipo, final String periodo) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                final ArrayList<String> playersList = new ArrayList<String>();

                ArrayList<JugadorDatos> jugadores = new ArrayList<JugadorDatos>();
                for (JugadorDatos j : jugadores) {
                    String name = j.getName();
                    int total = 0;
                    if (tipo.equals("kills")) {
                        total = j.getKills();
                    } else if (tipo.equals("wins")) {
                        total = j.getWins();
                    }
                    playersList.add(name + ";" + total);
                }
                for (int i = 0; i < playersList.size(); i++) {
                    for (int k = i + 1; k < playersList.size(); k++) {
                        String[] separadosI = playersList.get(i).split(";");
                        int totalI = Integer.valueOf(separadosI[1]);
                        String[] separadosK = playersList.get(k).split(";");
                        int totalK = Integer.valueOf(separadosK[1]);
                        if (totalI < totalK) {
                            String aux = playersList.get(i);
                            playersList.set(i, playersList.get(k));
                            playersList.set(k, aux);
                        }
                    }
                }
            }
        });

    }

    public static void getTopPlayers(final PaintballBattle plugin, final ArrayList<JugadorDatos> jugadores, final String tipo) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                final ArrayList<String> playersList = new ArrayList<String>();
                for (JugadorDatos j : jugadores) {
                    String name = j.getName();
                    int total = 0;
                    if (tipo.equals("kills")) {
                        total = j.getKills();
                    } else if (tipo.equals("wins")) {
                        total = j.getWins();
                    }
                    playersList.add(name + ";" + total);
                }

                for (int i = 0; i < playersList.size(); i++) {
                    for (int k = i + 1; k < playersList.size(); k++) {
                        String[] separadosI = playersList.get(i).split(";");
                        int totalI = Integer.valueOf(separadosI[1]);
                        String[] separadosK = playersList.get(k).split(";");
                        int totalK = Integer.valueOf(separadosK[1]);
                        if (totalI < totalK) {
                            String aux = playersList.get(i);
                            playersList.set(i, playersList.get(k));
                            playersList.set(k, aux);
                        }
                    }
                }
            }
        });

    }
}
