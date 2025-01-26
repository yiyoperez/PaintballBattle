package pb.ajneb97.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pb.ajneb97.core.utils.message.MessageUtils;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

@Command(name = "paintball givecoins", aliases = "paintball gc")
public class GiveCoinsCommand extends MainCommand {

    @Inject
    @Named("messages")
    private YamlDocument messages;

    @Execute
    @Description("Give coins to specified player.")
    // TODO:
    //  add amount message to validNumberError
    //  add wrong usage to commandGiveCoinsErrorUse
    public void command(@Context CommandSender sender, @OptionalArg Player target, @Arg int amount) {

        //TODO USAGE PLAYER DATA MANAGER
        //Si el jugador no esta en la base de datos, o en un archivo, DEBE estar conectado para darle coins.
//        plugin.registerPlayer(target.getUniqueId().toString() + ".yml");
//        if (plugin.getJugador(target.getName()) == null) {
//            plugin.agregarJugadorDatos(new JugadorDatos(target.getName(), target.getUniqueId().toString(), 0, 0, 0, 0, 0, new ArrayList<>(), new ArrayList<>()));
//        }
//        JugadorDatos jDatos = plugin.getJugador(target.getName());
//        jDatos.aumentarCoins(amount);
        sender.sendMessage(MessageUtils.translateColor(messages.getString("giveCoinsMessage").replace("%player%", target.getName()).replace("%amount%", amount + "")));
        target.sendMessage(MessageUtils.translateColor(messages.getString("receiveCoinsMessage").replace("%amount%", amount + "")));

//        if (!MySQL.isEnabled(plugin.getConfigDocument())) {
//            if (MySQL.jugadorExiste(plugin, player)) {
//                MySQL.agregarCoinsJugadorAsync(plugin, player, amount);
//                sender.sendMessage(MessageUtils.translateColor( messages.getString("giveCoinsMessage").replace("%player%", player).replace("%amount%", amount + "")));
//                Player p = Bukkit.getPlayer(player);
//                if (p != null) {
//                    p.sendMessage(MessageUtils.translateColor( messages.getString("receiveCoinsMessage").replace("%amount%", amount + "")));
//                }
//            } else {
//                Player p = Bukkit.getPlayer(player);
//                if (p != null) {
//                    MySQL.crearJugadorPartidaAsync(plugin, p.getUniqueId().toString(), p.getName(), "", 0, 0, 0, 0, amount, 1);
//                    sender.sendMessage(MessageUtils.translateColor( messages.getString("giveCoinsMessage").replace("%player%", player).replace("%amount%", amount + "")));
//                    p.sendMessage(MessageUtils.translateColor( messages.getString("receiveCoinsMessage").replace("%amount%", amount + "")));
//                } else {
//                    sender.sendMessage(MessageUtils.translateColor( messages.getString("errorPlayerOnline")));
//                }
//            }
//        }
    }
}
