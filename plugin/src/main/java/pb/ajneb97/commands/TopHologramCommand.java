package pb.ajneb97.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import pb.ajneb97.PaintballBattle;

public class TopHologramCommand extends MainCommand {

    private PaintballBattle plugin;
    private YamlDocument messages;

    public TopHologramCommand(PaintballBattle plugin) {
        super(plugin);
        this.plugin = plugin;
        this.messages = plugin.getMessagesDocument();
    }

    //TODO: Probably wont work on this, use placeholders to admins can use any hologram plugin they want to.
    @SubCommand(value = "createtophologram")
    public void createCommand(Player player, String option) {
        if (player.isOp() || player.hasPermission("paintball.admin")) {
            if (plugin.getServer().getPluginManager().getPlugin("HolographicDisplays") == null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou need HolographicDisplays plugin to use this feature."));
                return;
            }
            if (option.equalsIgnoreCase("kills") || option.equalsIgnoreCase("wins")) {
                //TODO Use hologram manager.
//                            TopHologram topHologram = plugin.getTopHologram(args[1]);
//                            if (topHologram == null) {
//                                String period = "global";
//                                if (args.length >= 4) {
//                                    period = args[3];
//                                }
//                                if (period.equalsIgnoreCase("global") || period.equalsIgnoreCase("monthly") || period.equalsIgnoreCase("weekly")) {
//                                    if (!MySQL.isEnabled(plugin.getConfigDocument()) && (period.equalsIgnoreCase("monthly") || period.equalsIgnoreCase("weekly"))) {
//                                        jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("topHologramPeriodSQLError")));
//                                        return true;
//                                    }
//                                    TopHologram hologram = new TopHologram(args[1], args[2], jugador.getLocation(), plugin, period);
//                                    //TODO Use hologram manager.
//                                    //plugin.agregarTopHolograma(hologram);
//                                    hologram.spawnHologram(plugin);
//                                    jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("topHologramCreated")));
//                                } else {
//                                    jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("commandCreateHologramErrorUse")));
//                                }
//                            } else {
//                                jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("topHologramAlreadyExists")));
//                            }
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("commandCreateHologramErrorUse")));
            }
            //player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("commandCreateHologramErrorUse")));
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("noPermissions")));
        }
    }

    @SubCommand(value = "deletetophologram")
    public void deleteCommand(Player player, String option) {
        if (player.isOp() || player.hasPermission("paintball.admin")) {
            if (plugin.getServer().getPluginManager().getPlugin("HolographicDisplays") == null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou need HolographicDisplays plugin to use this feature."));
                return;
            }
            //TODO Use hologram manager.
//                        TopHologram topHologram = plugin.getTopHologram(args[1]);
//                        if (topHologram != null) {
//                            plugin.eliminarTopHologama(args[1]);
//                            jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("topHologramRemoved")));
//                        } else {
//                            jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("topHologramDoesNotExists")));
//                        }
            //player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("commandRemoveHologramErrorUse")));
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("noPermissions")));
        }
    }
}
