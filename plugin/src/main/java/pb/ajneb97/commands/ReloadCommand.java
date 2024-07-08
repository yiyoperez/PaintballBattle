package pb.ajneb97.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import pb.ajneb97.PaintballBattle;

import java.io.IOException;

public class ReloadCommand extends MainCommand {

    private PaintballBattle plugin;
    private YamlDocument messages;

    public ReloadCommand(PaintballBattle plugin) {
        super(plugin);
        this.plugin = plugin;
        this.messages = plugin.getMessagesDocument();
    }

    @SubCommand(value = "reload")
    public void command(Player player) {
        if (player.isOp() || player.hasPermission("paintball.admin")) {
            try {
                plugin.getShopDocument().reload();
                plugin.getConfigDocument().reload();
                plugin.getMessagesDocument().reload();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //TODO
            //plugin.recargarCarteles();
            //plugin.recargarScoreboard();
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("configReloaded")));
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("noPermissions")));
        }
    }
}
