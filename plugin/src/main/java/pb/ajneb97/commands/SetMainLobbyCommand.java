package pb.ajneb97.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.utils.MessageUtils;

import java.io.IOException;

public class SetMainLobbyCommand extends MainCommand {

    private YamlDocument config;
    private YamlDocument messages;

    public SetMainLobbyCommand(PaintballBattle plugin) {
        super(plugin);
        this.config = plugin.getConfigDocument();
        this.messages = plugin.getMessagesDocument();
    }

    @SubCommand(value = "setmainlobby")
    public void command(Player player) {
        Location l = player.getLocation();
        config.set("MainLobby.x", l.getX() + "");
        config.set("MainLobby.y", l.getY() + "");
        config.set("MainLobby.z", l.getZ() + "");
        config.set("MainLobby.world", l.getWorld().getName());
        config.set("MainLobby.pitch", l.getPitch());
        config.set("MainLobby.yaw", l.getYaw());
        try {
            config.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        player.sendMessage(MessageUtils.translateColor(messages.getString("mainLobbyDefined")));
    }
}
