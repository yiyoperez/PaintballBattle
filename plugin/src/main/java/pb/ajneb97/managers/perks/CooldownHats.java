package pb.ajneb97.managers.perks;


import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.structures.Game;
import pb.ajneb97.structures.PaintballPlayer;
import pb.ajneb97.utils.enums.GameState;

public class CooldownHats {

    int taskID;
    int tiempo;
    private PaintballPlayer jugador;
    private Game partida;
    private PaintballBattle plugin;

    public CooldownHats(PaintballBattle plugin) {
        this.plugin = plugin;
    }

    public void cooldownHat(final PaintballPlayer jugador, final Game partida, int tiempo) {
//        this.jugador = jugador;
//        this.tiempo = tiempo;
//        this.partida = partida;
//        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
//        taskID = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
//            public void run() {
//                if (!ejecutarCooldownHat()) {
//                    YamlDocument messages = plugin.getMessagesDocument();
//                    if (!partida.getState().equals(GameState.ENDING)) {
//                        jugador.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("hatCooldownFinished")));
//                    }
//
//                    Bukkit.getScheduler().cancelTask(taskID);
//                    return;
//                }
//            }
//        }, 0L, 20L);
    }

    protected boolean ejecutarCooldownHat() {
        if (partida != null && partida.getState().equals(GameState.PLAYING)) {
            if (tiempo <= 0) {
                jugador.setEfectoHatEnCooldown(false);
                return false;
            } else {
                tiempo--;
                jugador.setTiempoEfectoHat(tiempo);
                return true;
            }
        } else {
            jugador.setEfectoHatEnCooldown(false);
            return false;
        }
    }

    public void durationHat(final PaintballPlayer jugador, final Game partida, int tiempo) {
        this.jugador = jugador;
        this.tiempo = tiempo;
        this.partida = partida;
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                if (!ejecutarDurationHat()) {
                    Bukkit.getScheduler().cancelTask(taskID);
                    return;
                }
            }
        }, 0L, 20L);
    }

    protected boolean ejecutarDurationHat() {
        if (partida != null && partida.getState().equals(GameState.PLAYING)) {
            if (tiempo <= 0) {
                jugador.setEfectoHatActivado(false);
                return false;
            } else {
                tiempo--;
                return true;
            }
        } else {
            jugador.setEfectoHatActivado(false);
            return false;
        }
    }
}
