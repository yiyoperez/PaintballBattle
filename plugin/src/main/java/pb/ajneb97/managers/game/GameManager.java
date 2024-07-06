package pb.ajneb97.managers.game;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.juego.GameState;
import pb.ajneb97.juego.PaintballPlayer;
import pb.ajneb97.juego.Partida;

import java.io.IOException;
import java.util.ArrayList;

public class GameManager {

    private ArrayList<Partida> partidas;
    private final PaintballBattle plugin;
    private YamlDocument config;
    private YamlDocument arenaDocument;

    public GameManager(PaintballBattle plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigDocument();
        this.arenaDocument = plugin.getArenaDocument();
    }

    public void shutdownGames() {
        if (partidas != null) {
            for (int i = 0; i < partidas.size(); i++) {
                if (!partidas.get(i).getEstado().equals(GameState.DISABLED)) {
                    PartidaManager.finalizarPartida(partidas.get(i), plugin, true, null);
                }
            }
        }
    }

    public Partida getPartidaJugador(String jugador) {
        for (int i = 0; i < partidas.size(); i++) {
            ArrayList<PaintballPlayer> jugadores = partidas.get(i).getJugadores();
            for (int c = 0; c < jugadores.size(); c++) {
                if (jugadores.get(c).getPlayer().getName().equals(jugador)) {
                    return partidas.get(i);
                }
            }
        }
        return null;
    }

    public ArrayList<Partida> getPartidas() {
        return this.partidas;
    }

    public Partida getPartida(String nombre) {
        for (int i = 0; i < partidas.size(); i++) {
            if (partidas.get(i).getNombre().equals(nombre)) {
                return partidas.get(i);
            }
        }
        return null;
    }

    public void agregarPartida(Partida partida) {
        this.partidas.add(partida);
    }

    public void removerPartida(String nombre) {
        for (int i = 0; i < partidas.size(); i++) {
            if (partidas.get(i).getNombre().equals(nombre)) {
                partidas.remove(i);
            }
        }
    }

    public void cargarPartidas() {
        this.partidas = new ArrayList<Partida>();
        YamlDocument arenas = arenaDocument;
        if (arenas.contains("Arenas")) {
            for (String key : arenas.getSection("Arenas").getRoutesAsStrings(false)) {
                int min_players = Integer.valueOf(arenas.getString("Arenas." + key + ".min_players"));
                int max_players = Integer.valueOf(arenas.getString("Arenas." + key + ".max_players"));
                int time = Integer.valueOf(arenas.getString("Arenas." + key + ".time"));
                int vidas = Integer.valueOf(arenas.getString("Arenas." + key + ".lives"));

                Location lLobby = null;
                if (arenas.contains("Arenas." + key + ".Lobby")) {
                    double xLobby = Double.valueOf(arenas.getString("Arenas." + key + ".Lobby.x"));
                    double yLobby = Double.valueOf(arenas.getString("Arenas." + key + ".Lobby.y"));
                    double zLobby = Double.valueOf(arenas.getString("Arenas." + key + ".Lobby.z"));
                    String worldLobby = arenas.getString("Arenas." + key + ".Lobby.world");
                    float pitchLobby = Float.valueOf(arenas.getString("Arenas." + key + ".Lobby.pitch"));
                    float yawLobby = Float.valueOf(arenas.getString("Arenas." + key + ".Lobby.yaw"));
                    lLobby = new Location(Bukkit.getWorld(worldLobby), xLobby, yLobby, zLobby, yawLobby, pitchLobby);
                }


                String nombreTeam1 = arenas.getString("Arenas." + key + ".Team1.name");

                Location lSpawnTeam1 = null;
                if (arenas.contains("Arenas." + key + ".Team1.Spawn")) {
                    double xSpawnTeam1 = Double.valueOf(arenas.getString("Arenas." + key + ".Team1.Spawn.x"));
                    double ySpawnTeam1 = Double.valueOf(arenas.getString("Arenas." + key + ".Team1.Spawn.y"));
                    double zSpawnTeam1 = Double.valueOf(arenas.getString("Arenas." + key + ".Team1.Spawn.z"));
                    String worldSpawnTeam1 = arenas.getString("Arenas." + key + ".Team1.Spawn.world");
                    float pitchSpawnTeam1 = Float.valueOf(arenas.getString("Arenas." + key + ".Team1.Spawn.pitch"));
                    float yawSpawnTeam1 = Float.valueOf(arenas.getString("Arenas." + key + ".Team1.Spawn.yaw"));
                    lSpawnTeam1 = new Location(Bukkit.getWorld(worldSpawnTeam1), xSpawnTeam1, ySpawnTeam1, zSpawnTeam1, yawSpawnTeam1, pitchSpawnTeam1);
                }


                String nombreTeam2 = arenas.getString("Arenas." + key + ".Team2.name");
                Location lSpawnTeam2 = null;
                if (arenas.contains("Arenas." + key + ".Team2.Spawn")) {
                    double xSpawnTeam2 = Double.valueOf(arenas.getString("Arenas." + key + ".Team2.Spawn.x"));
                    double ySpawnTeam2 = Double.valueOf(arenas.getString("Arenas." + key + ".Team2.Spawn.y"));
                    double zSpawnTeam2 = Double.valueOf(arenas.getString("Arenas." + key + ".Team2.Spawn.z"));
                    String worldSpawnTeam2 = arenas.getString("Arenas." + key + ".Team2.Spawn.world");
                    float pitchSpawnTeam2 = Float.valueOf(arenas.getString("Arenas." + key + ".Team2.Spawn.pitch"));
                    float yawSpawnTeam2 = Float.valueOf(arenas.getString("Arenas." + key + ".Team2.Spawn.yaw"));
                    lSpawnTeam2 = new Location(Bukkit.getWorld(worldSpawnTeam2), xSpawnTeam2, ySpawnTeam2, zSpawnTeam2, yawSpawnTeam2, pitchSpawnTeam2);
                }

                Partida partida = new Partida(key, time, nombreTeam1, nombreTeam2, vidas);
                if (nombreTeam1.equalsIgnoreCase("random")) {
                    partida.getTeam1().setRandom(true);
                }
                if (nombreTeam2.equalsIgnoreCase("random")) {
                    partida.getTeam2().setRandom(true);
                }
                partida.modificarTeams(config);
                partida.setCantidadMaximaJugadores(max_players);
                partida.setCantidadMinimaJugadores(min_players);
                partida.setLobby(lLobby);
                partida.getTeam1().setSpawn(lSpawnTeam1);
                partida.getTeam2().setSpawn(lSpawnTeam2);
                String enabled = arenas.getString("Arenas." + key + ".enabled");
                if (enabled.equals("true")) {
                    partida.setEstado(GameState.WAITING);
                } else {
                    partida.setEstado(GameState.DISABLED);
                }

                this.partidas.add(partida);
            }
        }

    }

    public void guardarPartidas() {
        YamlDocument arenas = arenaDocument;
        arenas.set("Arenas", null);
        for (Partida p : this.partidas) {
            String nombre = p.getNombre();
            arenas.set("Arenas." + nombre + ".min_players", p.getCantidadMinimaJugadores() + "");
            arenas.set("Arenas." + nombre + ".max_players", p.getCantidadMaximaJugadores() + "");
            arenas.set("Arenas." + nombre + ".time", p.getTiempoMaximo() + "");
            arenas.set("Arenas." + nombre + ".lives", p.getVidasIniciales() + "");
            Location lLobby = p.getLobby();
            if (lLobby != null) {
                arenas.set("Arenas." + nombre + ".Lobby.x", lLobby.getX() + "");
                arenas.set("Arenas." + nombre + ".Lobby.y", lLobby.getY() + "");
                arenas.set("Arenas." + nombre + ".Lobby.z", lLobby.getZ() + "");
                arenas.set("Arenas." + nombre + ".Lobby.world", lLobby.getWorld().getName());
                arenas.set("Arenas." + nombre + ".Lobby.pitch", lLobby.getPitch());
                arenas.set("Arenas." + nombre + ".Lobby.yaw", lLobby.getYaw());
            }

            Location lSpawnTeam1 = p.getTeam1().getSpawn();
            if (lSpawnTeam1 != null) {
                arenas.set("Arenas." + nombre + ".Team1.Spawn.x", lSpawnTeam1.getX() + "");
                arenas.set("Arenas." + nombre + ".Team1.Spawn.y", lSpawnTeam1.getY() + "");
                arenas.set("Arenas." + nombre + ".Team1.Spawn.z", lSpawnTeam1.getZ() + "");
                arenas.set("Arenas." + nombre + ".Team1.Spawn.world", lSpawnTeam1.getWorld().getName());
                arenas.set("Arenas." + nombre + ".Team1.Spawn.pitch", lSpawnTeam1.getPitch());
                arenas.set("Arenas." + nombre + ".Team1.Spawn.yaw", lSpawnTeam1.getYaw());
            }
            if (p.getTeam1().esRandom()) {
                arenas.set("Arenas." + nombre + ".Team1.name", "random");
            } else {
                arenas.set("Arenas." + nombre + ".Team1.name", p.getTeam1().getTipo());
            }


            Location lSpawnTeam2 = p.getTeam2().getSpawn();
            if (lSpawnTeam2 != null) {
                arenas.set("Arenas." + nombre + ".Team2.Spawn.x", lSpawnTeam2.getX() + "");
                arenas.set("Arenas." + nombre + ".Team2.Spawn.y", lSpawnTeam2.getY() + "");
                arenas.set("Arenas." + nombre + ".Team2.Spawn.z", lSpawnTeam2.getZ() + "");
                arenas.set("Arenas." + nombre + ".Team2.Spawn.world", lSpawnTeam2.getWorld().getName());
                arenas.set("Arenas." + nombre + ".Team2.Spawn.pitch", lSpawnTeam2.getPitch());
                arenas.set("Arenas." + nombre + ".Team2.Spawn.yaw", lSpawnTeam2.getYaw());
            }
            if (p.getTeam2().esRandom()) {
                arenas.set("Arenas." + nombre + ".Team2.name", "random");
            } else {
                arenas.set("Arenas." + nombre + ".Team2.name", p.getTeam2().getTipo());
            }

            if (p.getEstado().equals(GameState.DISABLED)) {
                arenas.set("Arenas." + nombre + ".enabled", "false");
            } else {
                arenas.set("Arenas." + nombre + ".enabled", "true");
            }
        }

        try {
            arenaDocument.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
