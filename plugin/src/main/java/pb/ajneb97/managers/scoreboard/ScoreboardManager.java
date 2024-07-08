package pb.ajneb97.managers.scoreboard;


import dev.dejvokep.boostedyaml.YamlDocument;
import fr.mrmicky.fastboard.FastBoard;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.juego.GameState;
import pb.ajneb97.juego.PaintballPlayer;
import pb.ajneb97.juego.Partida;
import pb.ajneb97.juego.Team;
import pb.ajneb97.utils.UtilidadesOtros;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {

    private int taskID;
    private PaintballBattle plugin;
    private final Map<UUID, FastBoard> boards = new HashMap<>();
    private ScoreboardManager scoreboardTask;

    public ScoreboardManager(PaintballBattle plugin) {
        this.plugin = plugin;
        this.scoreboardTask = new ScoreboardManager(plugin);
        this.scoreboardTask.crearScoreboards();
    }

    public int getTaskID() {
        return this.taskID;
    }

    public void recargarScoreboard() {
        int taskID = scoreboardTask.getTaskID();
        Bukkit.getScheduler().cancelTask(taskID);
        scoreboardTask = new ScoreboardManager(plugin);
        scoreboardTask.crearScoreboards();
    }

    public void crearScoreboards() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        final YamlDocument messages = plugin.getMessagesDocument();
        final YamlDocument config = plugin.getConfigDocument();
        taskID = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    actualizarScoreboard(player, messages, config);
                }
            }
        }, 0, 20L);
    }

    protected void actualizarScoreboard(final Player player, final YamlDocument messages, final YamlDocument config) {
        Partida partida = plugin.getGameManager().getPartidaJugador(player.getName());
        FastBoard board = boards.get(player.getUniqueId());
        if (partida != null) {
            PaintballPlayer jugador = partida.getJugador(player.getName());
            if (board == null) {
                board = new FastBoard(player);
                board.updateTitle(ChatColor.translateAlternateColorCodes('&', messages.getString("gameScoreboardTitle")));
                boards.put(player.getUniqueId(), board);
            }

            List<String> lista = messages.getStringList("gameScoreboardBody");
            Team team1 = partida.getTeam1();
            Team team2 = partida.getTeam2();
            String equipo1Nombre = config.getString("teams." + team1.getTipo() + ".name");
            String equipo2Nombre = config.getString("teams." + team2.getTipo() + ".name");

            for (int i = 0; i < lista.size(); i++) {
                String message = ChatColor.translateAlternateColorCodes('&', lista.get(i).replace("%status%", getEstado(partida, messages)).replace("%team_1%", equipo1Nombre)
                        .replace("%team_2%", equipo2Nombre).replace("%team_1_lives%", team1.getVidas() + "").replace("%team_2_lives%", team2.getVidas() + "")
                        .replace("%kills%", jugador.getKills() + "").replace("%arena%", partida.getNombre()).replace("%current_players%", partida.getCantidadActualJugadores() + "")
                        .replace("%max_players%", partida.getCantidadMaximaJugadores() + ""));
                if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
                    message = PlaceholderAPI.setPlaceholders(player, message);
                }
                board.updateLine(i, message);
            }
        } else {
            if (board != null) {
                boards.remove(player.getUniqueId());
                board.delete();
            }
        }
    }

    private String getEstado(Partida partida, YamlDocument messages) {
        //Remplazar variables del %time%
        if (partida.getEstado().equals(GameState.WAITING)) {
            return messages.getString("statusWaiting");
        } else if (partida.getEstado().equals(GameState.STARTING)) {
            int tiempo = partida.getTiempo();
            return messages.getString("statusStarting").replace("%time%", UtilidadesOtros.getTiempo(tiempo));
        } else if (partida.getEstado().equals(GameState.ENDING)) {
            int tiempo = partida.getTiempo();
            return messages.getString("statusFinishing").replace("%time%", UtilidadesOtros.getTiempo(tiempo));
        } else {
            int tiempo = partida.getTiempo();
            return messages.getString("statusIngame").replace("%time%", UtilidadesOtros.getTiempo(tiempo));
        }
    }

}
