package pb.ajneb97.managers.perks;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.structures.Game;
import pb.ajneb97.structures.Team;


public class CooldownManager {

    int taskID;
    int tiempo;
    private Game partida;
    private PaintballBattle plugin;

    public CooldownManager(PaintballBattle plugin) {
        this.plugin = plugin;
    }

    public void cooldownComenzarJuego(Game partida, int cooldown) {
//        this.partida = partida;
//        this.tiempo = cooldown;
//        partida.setTime(tiempo);
//        final YamlDocument messages = plugin.getMessagesDocument();
//        final YamlDocument config = plugin.getConfigDocument();
//        final String prefix = MessageUtils.translateColor(messages.getString("prefix")) + " ";
//        ArrayList<PaintballPlayer> jugadores = partida.getPlayers();
//        for (int i = 0; i < jugadores.size(); i++) {
//            jugadores.get(i).getPlayer().sendMessage(MessageUtils.translateColor(messages.getString("arenaStartingMessage").replace("%time%", tiempo + "")));
//        }
//        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
//        taskID = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
//            public void run() {
//                if (!ejecutarComenzarJuego(messages, config, prefix)) {
//                    Bukkit.getScheduler().cancelTask(taskID);
//                    return;
//                }
//            }
//        }, 0L, 20L);
    }

    protected boolean ejecutarComenzarJuego(YamlDocument messages, YamlDocument config, String prefix) {
//        if (partida != null && partida.getState().equals(GameState.STARTING)) {
//            if (tiempo <= 5 && tiempo > 0) {
//                ArrayList<PaintballPlayer> jugadores = partida.getPlayers();
//                for (int i = 0; i < jugadores.size(); i++) {
//                    jugadores.get(i).getPlayer().sendMessage(MessageUtils.translateColor(messages.getString("arenaStartingMessage").replace("%time%", tiempo + "")));
//                    String[] separados = config.getString("startCooldownSound").split(";");
//                    try {
//                        Sound sound = Sound.valueOf(separados[0]);
//                        jugadores.get(i).getPlayer().playSound(jugadores.get(i).getPlayer().getLocation(), sound, Float.valueOf(separados[1]), Float.valueOf(separados[2]));
//                    } catch (Exception ex) {
//                        Bukkit.getConsoleSender().sendMessage(MessageUtils.translateColor(PaintballBattle.prefix + "&7Sound Name: &c" + separados[0] + " &7is not valid."));
//                    }
//                }
//                partida.disminuirTiempo();
//                tiempo--;
//                return true;
//            } else if (tiempo <= 0) {
//                PartidaManager.iniciarPartida(partida, plugin);
//                return false;
//            } else {
//                partida.disminuirTiempo();
//                tiempo--;
//                return true;
//            }
//        } else {
//            ArrayList<PaintballPlayer> jugadores = partida.getPlayers();
//            for (int i = 0; i < jugadores.size(); i++) {
//                jugadores.get(i).getPlayer().sendMessage(MessageUtils.translateColor(messages.getString("gameStartingCancelled")));
//            }
//            return false;
//        }
        return false;
    }

    public void cooldownJuego(Game partida) {
        this.partida = partida;
        this.tiempo = partida.getMaxTime();
        //partida.setTime(tiempo);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                if (!ejecutarJuego()) {
                    Bukkit.getScheduler().cancelTask(taskID);
                    return;
                }
            }
        }, 0L, 20L);
    }

    protected boolean ejecutarJuego() {
//        if (partida != null && partida.getState().equals(GameState.PLAYING)) {
//            partida.disminuirTiempo();
//            if (tiempo == 0) {
//                PartidaManager.iniciarFaseFinalizacion(partida, plugin);
//                return false;
//            } else {
//                tiempo--;
//                return true;
//            }
//        } else {
//            return false;
//        }
        return false;
    }

    public void cooldownFaseFinalizacion(Game partida, int cooldown, final Team ganador) {
//        this.partida = partida;
//        this.tiempo = cooldown;
//        partida.setTime(tiempo);
//        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
//        taskID = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
//            public void run() {
//                if (!ejecutarComenzarFaseFinalizacion(ganador)) {
//                    Bukkit.getScheduler().cancelTask(taskID);
//                    return;
//                }
//            }
//        }, 0L, 20L);
    }

    protected boolean ejecutarComenzarFaseFinalizacion(Team ganador) {
//        if (partida != null && partida.getState().equals(GameState.ENDING)) {
//            partida.disminuirTiempo();
//            if (tiempo == 0) {
//                PartidaManager.finalizarPartida(partida, plugin, false, ganador);
//                return false;
//            } else {
//                tiempo--;
//                if (ganador != null) {
//                    PartidaManager.lanzarFuegos(ganador.getPlayers());
//                }
//                return true;
//            }
//        } else {
//            return false;
//        }
        return false;
    }
}
