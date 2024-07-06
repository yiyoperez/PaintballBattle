package pb.ajneb97.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.Description;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import org.bukkit.command.CommandSender;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.utils.MessageUtils;

import java.io.IOException;

@Command(value = "paintball", alias = {"pb", "pball"})
@Description("Main command of PaintballBattle")
public class MainCommand extends BaseCommand {

    //TODO IMPLEMENT MESSAGE HANDLER.
    private final PaintballBattle plugin;
    private YamlDocument messages;

    public MainCommand(PaintballBattle plugin) {
        this.plugin = plugin;
        this.messages = plugin.getMessagesDocument();
    }

    @Default
    public void command(CommandSender sender) {
        usageMessage(sender);
    }

    @SubCommand(value = "reload")
    @Permission("paintball.admin.reload")
    public void reloadCommand(CommandSender sender) {
        try {
            plugin.getMessagesDocument().reload();
            plugin.getConfigDocument().reload();
            plugin.getShopDocument().reload();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        plugin.recargarCarteles();
        // TODO: usage scoreboard manager.
        //plugin.recargarScoreboard();
        //TODO: use hologram manager.
        //plugin.recargarHologramas();
        sender.sendMessage(MessageUtils.translateColor(messages.getString("configReloaded")));
    }

    public void usageMessage(CommandSender sender) {
        sender.sendMessage(MessageUtils.translateColor("&7[ [ &4[&fPaintball Battle&4] &7] ]"));
        sender.sendMessage(MessageUtils.translateColor(""));
        sender.sendMessage(MessageUtils.translateColor("&6/paintball create <arena> &8Creates a new arena."));
        sender.sendMessage(MessageUtils.translateColor("&6/paintball delete <arena> &8Deletes an arena."));
        sender.sendMessage(MessageUtils.translateColor("&6/paintball join <arena> &8Joins an arena."));
        sender.sendMessage(MessageUtils.translateColor("&6/paintball joinrandom &8Joins a random arena."));
        sender.sendMessage(MessageUtils.translateColor("&6/paintball leave &8Leaves from the arena."));
        sender.sendMessage(MessageUtils.translateColor("&6/paintball shop &8Opens the Paintball Shop."));
        sender.sendMessage(MessageUtils.translateColor("&6/paintball givecoins <player> <amount>"));
        sender.sendMessage(MessageUtils.translateColor("&6/paintball setmainlobby &8Defines the minigame main lobby."));
        sender.sendMessage(MessageUtils.translateColor("&6/paintball enable <arena> &8Enables an arena."));
        sender.sendMessage(MessageUtils.translateColor("&6/paintball disable <arena> &8Disables an arena."));
        sender.sendMessage(MessageUtils.translateColor("&6/paintball edit <arena> &8Edit the properties of an arena."));
        sender.sendMessage(MessageUtils.translateColor("&6/paintball createtophologram <name> <kills/wins> <global/monthly/weekly>"));
        sender.sendMessage(MessageUtils.translateColor("&6/paintball removetophologram <name>"));
        sender.sendMessage(MessageUtils.translateColor("&6/paintball reload &8Reloads the configuration files."));
        sender.sendMessage(MessageUtils.translateColor(""));
        sender.sendMessage(MessageUtils.translateColor("&7[ [ &4[&fPaintball Battle&4] &7] ]"));
    }
}
