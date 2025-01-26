package pb.ajneb97.managers;


import dev.dejvokep.boostedyaml.YamlDocument;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.structures.Game;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {

    private int taskID;
    private PaintballBattle plugin;
    private final Map<UUID, FastBoard> boards = new HashMap<>();
    private ScoreboardManager scoreboardTask;

    @Inject
    @Named("config")
    private YamlDocument config;
    @Inject
    @Named("messages")
    private YamlDocument messages;
    @Inject
    private GameManager gameManager;

    public int getTaskID() {
        return this.taskID;
    }

    public void recargarScoreboard() {
        int taskID = scoreboardTask.getTaskID();
        Bukkit.getScheduler().cancelTask(taskID);
        scoreboardTask.crearScoreboards();
    }

    public void crearScoreboards() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    actualizarScoreboard(player, messages, config);
                }
            }
        }, 0, 20L);
    }

    protected void actualizarScoreboard(final Player player, final YamlDocument messages, final YamlDocument config) {
//        Game partida = gameManager.getPlayerGame(player);
//        FastBoard board = boards.get(player.getUniqueId());
//        if (partida != null) {
//            PaintballPlayer jugador = partida.getJugador(player.getName());
//            if (board == null) {
//                board = new FastBoard(player);
//                board.updateTitle(MessageUtils.translateColor(messages.getString("gameScoreboardTitle")));
//                boards.put(player.getUniqueId(), board);
//            }
//
//            List<String> lista = messages.getStringList("gameScoreboardBody");
//            Team team1 = partida.getFirstTeam();
//            Team team2 = partida.getSecondTeam();
//            String equipo1Nombre = config.getString("teams." + team1.getName() + ".name");
//            String equipo2Nombre = config.getString("teams." + team2.getName() + ".name");
//
//            for (int i = 0; i < lista.size(); i++) {
//                String message = MessageUtils.translateColor(lista.get(i).replace("%status%", getEstado(partida, messages)).replace("%team_1%", equipo1Nombre)
//                        .replace("%team_2%", equipo2Nombre).replace("%team_1_lives%", team1.getLives() + "").replace("%team_2_lives%", team2.getLives() + "")
//                        .replace("%kills%", jugador.getKills() + "").replace("%arena%", partida.getName()).replace("%current_players%", partida.getCurrentPlayersSize() + "")
//                        .replace("%max_players%", partida.getMaxPlayers() + ""));
//                if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
//                    message = PlaceholderAPI.setPlaceholders(player, message);
//                }
//                board.updateLine(i, message);
//            }
//        } else {
//            if (board != null) {
//                boards.remove(player.getUniqueId());
//                board.delete();
//            }
//        }
    }

    private String getEstado(Game partida, YamlDocument messages) {
        //Remplazar variables del %time%
//        if (partida.getState().equals(GameState.WAITING)) {
//            return messages.getString("statusWaiting");
//        } else if (partida.getState().equals(GameState.STARTING)) {
//            int tiempo = partida.getTime();
//            return messages.getString("statusStarting").replace("%time%", UtilidadesOtros.getTiempo(tiempo));
//        } else if (partida.getState().equals(GameState.ENDING)) {
//            int tiempo = partida.getTime();
//            return messages.getString("statusFinishing").replace("%time%", UtilidadesOtros.getTiempo(tiempo));
//        } else {
//            int tiempo = partida.getTime();
//            return messages.getString("statusIngame").replace("%time%", UtilidadesOtros.getTiempo(tiempo));
//        }
        return "";
    }

}
