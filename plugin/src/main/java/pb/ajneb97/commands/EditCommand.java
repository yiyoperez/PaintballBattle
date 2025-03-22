package pb.ajneb97.commands;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.commands.extra.PaintCommand;
import pb.ajneb97.commons.cache.Cache;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.inventories.EditInventory;
import pb.ajneb97.structures.game.Game;
import pb.ajneb97.structures.game.GameEditSessionBuilder;
import pb.ajneb97.utils.enums.Messages;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

import java.util.UUID;

@Command(name = "paintball edit")
public class EditCommand implements PaintCommand {

    @Inject
    private PaintballBattle plugin;
    @Inject
    private MessageHandler messageHandler;
    @Inject
    @Named("edit-session-cache")
    private Cache<UUID, GameEditSessionBuilder> editSession;

    @Inject
    private EditInventory editInventory;

    @Execute
    @Permission("paintball.admin.edit")
    public void command(@Context Player player, @Arg() Game game) {
        if (game.isEnabled()) {
            messageHandler.sendMessage(player, Messages.ARENA_MUST_BE_DISABLED);
            return;
        }

        if (editSession.exists(player.getUniqueId())) {
            messageHandler.sendManualMessage(player, "You already are in an edit session.");
            return;
        }

        editSession.add(player.getUniqueId(), new GameEditSessionBuilder(game));
        Bukkit.getScheduler().runTaskLater(plugin, () -> editInventory.open(player), 5L);
    }
}
