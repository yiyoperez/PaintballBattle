package pb.ajneb97.managers.perks;


import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.entity.Player;
import pb.ajneb97.PaintballBattle;

public class CooldownKillstreaksActionbar {

    int taskID;
    private final PaintballBattle plugin;

    public CooldownKillstreaksActionbar(PaintballBattle plugin) {
        this.plugin = plugin;
    }

    public void crearActionbars() {
//        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
//        final YamlDocument messages = plugin.getMessagesDocument();
//        final YamlDocument config = plugin.getConfigDocument();
//        taskID = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
//            public void run() {
//                for (Player player : Bukkit.getOnlinePlayers()) {
//                    actualizarActionbars(player, messages, config);
//                }
//            }
//        }, 0, 20L);
    }

    protected void actualizarActionbars(final Player player, final YamlDocument messages, final YamlDocument config) {
//        Game partida = plugin.getGameManager().getPlayerGame(player.getName());
//        if (partida != null) {
//            PaintballPlayer jugador = partida.getJugador(player.getName());
//            Killstreak ultima = jugador.getUltimaKillstreak();
//            if (ultima == null) return;
//
//            String name = config.getString("killstreaks_items." + ultima.getType() + ".name");
//            int tiempo = ultima.getTime();
//
//            ActionBar.sendActionBar(jugador.getPlayer(), MessageUtils.translateColor(messages.getString("killstreakActionbar")
//                    .replace("%killstreak%", name)
//                    .replace("%time%", tiempo + "")));
//        }
    }
}
