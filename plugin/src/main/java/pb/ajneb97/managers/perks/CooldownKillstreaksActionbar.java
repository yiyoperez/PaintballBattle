package pb.ajneb97.managers.perks;


import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.juego.Killstreak;
import pb.ajneb97.juego.PaintballPlayer;
import pb.ajneb97.juego.Partida;
import pb.ajneb97.lib.actionbarapi.ActionBarAPI;

public class CooldownKillstreaksActionbar {

    int taskID;
    private final PaintballBattle plugin;

    public CooldownKillstreaksActionbar(PaintballBattle plugin) {
        this.plugin = plugin;
    }

    public void crearActionbars() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        final YamlDocument messages = plugin.getMessagesDocument();
        final YamlDocument config = plugin.getConfigDocument();
        taskID = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    actualizarActionbars(player, messages, config);
                }
            }
        }, 0, 20L);
    }

    protected void actualizarActionbars(final Player player, final YamlDocument messages, final YamlDocument config) {
        Partida partida = plugin.getGameManager().getPartidaJugador(player.getName());
        if (partida != null) {
            PaintballPlayer jugador = partida.getJugador(player.getName());
            Killstreak ultima = jugador.getUltimaKillstreak();
            if (ultima == null) return;

            String name = config.getString("killstreaks_items." + ultima.getType() + ".name");
            int tiempo = ultima.getTime();
            ActionBarAPI.sendActionBar(jugador.getPlayer(), ChatColor.translateAlternateColorCodes('&', messages.getString("killstreakActionbar")
                    .replace("%killstreak%", name).replace("%time%", tiempo + "")));
        }
    }
}
