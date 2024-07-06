package pb.ajneb97.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.triumphteam.cmd.core.annotation.Description;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pb.ajneb97.PaintballBattle;

public class GiveCoinsCommand extends MainCommand {

    private final PaintballBattle plugin;
    private YamlDocument messages;

    public GiveCoinsCommand(PaintballBattle plugin) {
        super(plugin);
        this.plugin = plugin;
        this.messages = plugin.getMessagesDocument();
    }

    @SubCommand(value = "givecoins", alias = "gc")
    @Description("Give coins to specified player.")
    // TODO:
    //  add amount message to validNumberError
    //  add wrong usage to commandGiveCoinsErrorUse
    public void command(CommandSender sender, Player target, int amount) {

        //TODO USAGE PLAYER DATA MANAGER
        //Si el jugador no esta en la base de datos, o en un archivo, DEBE estar conectado para darle coins.
//        plugin.registerPlayer(target.getUniqueId().toString() + ".yml");
//        if (plugin.getJugador(target.getName()) == null) {
//            plugin.agregarJugadorDatos(new JugadorDatos(target.getName(), target.getUniqueId().toString(), 0, 0, 0, 0, 0, new ArrayList<>(), new ArrayList<>()));
//        }
//        JugadorDatos jDatos = plugin.getJugador(target.getName());
//        jDatos.aumentarCoins(amount);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("giveCoinsMessage").replace("%player%", target.getName()).replace("%amount%", amount + "")));
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("receiveCoinsMessage").replace("%amount%", amount + "")));

//        if (!MySQL.isEnabled(plugin.getConfigDocument())) {
//            if (MySQL.jugadorExiste(plugin, player)) {
//                MySQL.agregarCoinsJugadorAsync(plugin, player, amount);
//                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("giveCoinsMessage").replace("%player%", player).replace("%amount%", amount + "")));
//                Player p = Bukkit.getPlayer(player);
//                if (p != null) {
//                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("receiveCoinsMessage").replace("%amount%", amount + "")));
//                }
//            } else {
//                Player p = Bukkit.getPlayer(player);
//                if (p != null) {
//                    MySQL.crearJugadorPartidaAsync(plugin, p.getUniqueId().toString(), p.getName(), "", 0, 0, 0, 0, amount, 1);
//                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("giveCoinsMessage").replace("%player%", player).replace("%amount%", amount + "")));
//                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("receiveCoinsMessage").replace("%amount%", amount + "")));
//                } else {
//                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("errorPlayerOnline")));
//                }
//            }
//        }
    }
}
