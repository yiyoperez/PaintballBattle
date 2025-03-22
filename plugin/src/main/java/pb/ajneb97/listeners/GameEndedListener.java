package pb.ajneb97.listeners;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import pb.ajneb97.core.utils.message.MessageUtils;
import pb.ajneb97.listeners.customevents.GameEndedEvent;
import pb.ajneb97.structures.PaintballPlayer;
import pb.ajneb97.utils.UtilidadesOtros;

import java.util.List;

public class GameEndedListener implements Listener {

    @EventHandler
    public void onGameEnded(GameEndedEvent event) {
        //TODO: runRewardActions(); ??
    }

    private static void givePlayerCoins(PaintballPlayer killer) {
        //int cantidadCoinsGanados = UtilidadesOtros.coinsGanados(killer.getPlayer(), config);
//        int nivelExtraKillCoins = PaintballAPI.getPerkLevel(killer.getPlayer(), "extra_killcoins");
//        if (nivelExtraKillCoins != 0) {
//            String linea = plugin.getShopDocument().getStringList("perks_upgrades.extra_killcoins").get(nivelExtraKillCoins - 1);
//            String[] sep = linea.split(";");
//            int cantidad = Integer.valueOf(sep[0]);
//            cantidadCoinsGanados = cantidadCoinsGanados + cantidad;
//        }
        //String lastKilledBy = killer.getLastKilledBy();
        /*if (lastKilledBy != null && lastKilledBy.equals(dead.getPlayer().getName())) {
            cantidadCoinsGanados = cantidadCoinsGanados + 1;
        }*/
        //killer.increaseCoins(cantidadCoinsGanados);
    }

    // TODO: Improve
    //
//            if (config.getString("rewards_executed_after_teleport").equals("false")) {
//                if (ganador != null) {
//                    if (ganador.getName().equals(teamJugador.getName())) {
//                        List<String> commands = config.getStringList("winners_command_rewards");
//                        runRewardActions(commands, j);
//                    } else {
//                        List<String> commands = config.getStringList("losers_command_rewards");
//                        runRewardActions(commands, j);
//                    }
//                } else {
//                    List<String> commands = config.getStringList("tie_command_rewards");
//                    runRewardActions(commands, j);
//                }
//            }
//        }

    public void runRewardActions(@NotNull List<String> commands, PaintballPlayer j) {
        CommandSender console = Bukkit.getServer().getConsoleSender();
        for (String command : commands) {
            if (command.startsWith("msg %player%")) {
                String mensaje = command.replace("msg %player% ", "");
                j.getPlayer().sendMessage(MessageUtils.translateLegacyColor(mensaje));
            } else {
                String comandoAEnviar = command.replaceAll("%player%", j.getPlayer().getName());
                if (comandoAEnviar.contains("%random")) {
                    int pos = comandoAEnviar.indexOf("%random");
                    int nextPos = comandoAEnviar.indexOf("%", pos + 1);
                    String variableCompleta = comandoAEnviar.substring(pos, nextPos + 1);
                    String variable = variableCompleta.replace("%random_", "").replace("%", "");
                    String[] sep = variable.split("-");
                    int cantidadMinima = 0;
                    int cantidadMaxima = 0;

                    //TODO Get rid of eval method.
                    try {
                        cantidadMinima = (int) UtilidadesOtros.eval(sep[0].replace("kills", j.getKills() + ""));
                        cantidadMaxima = (int) UtilidadesOtros.eval(sep[1].replace("kills", j.getKills() + ""));
                    } catch (Exception e) {

                    }
                    int num = UtilidadesOtros.getNumeroAleatorio(cantidadMinima, cantidadMaxima);
                    comandoAEnviar = comandoAEnviar.replace(variableCompleta, num + "");
                }
                Bukkit.dispatchCommand(console, comandoAEnviar);
            }
        }
    }
}
