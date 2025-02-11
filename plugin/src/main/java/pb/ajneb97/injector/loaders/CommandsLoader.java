package pb.ajneb97.injector.loaders;

import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import dev.rollczi.litecommands.schematic.SchematicFormat;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.commands.extra.GameArgumentResolver;
import pb.ajneb97.commands.extra.PaintCommand;
import pb.ajneb97.commands.extra.PaintInvalidUsageHandler;
import pb.ajneb97.commands.extra.PaintMissingPermissionHandler;
import pb.ajneb97.commons.loader.Loader;
import pb.ajneb97.core.logger.Logger;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.core.utils.message.Placeholder;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.structures.Game;
import pb.ajneb97.utils.enums.Messages;
import team.unnamed.inject.Inject;

import java.util.Set;

public final class CommandsLoader implements Loader {

    @Inject
    private PaintballBattle plugin;
    @Inject
    private GameManager gameManager;
    @Inject
    private Set<PaintCommand> commandSet;
    @Inject
    private MessageHandler messageHandler;

    @Inject
    private GameArgumentResolver gameArgumentResolver;
    @Inject
    private PaintInvalidUsageHandler paintInvalidUsageHandler;
    @Inject
    private PaintMissingPermissionHandler paintMissingPermissionHandler;

    @Override
    public void load() {
        Logger.info("Starting commands loader.");

        //noinspection UnstableApiUsage
        LiteBukkitFactory.builder()
                .settings(settings -> settings
                        .fallbackPrefix("paintball")
                        .nativePermissions(false)
                )

                .commands(commandSet.toArray())

                // LiteCommands handlers.
                .argument(Game.class, gameArgumentResolver)
                .invalidUsage(paintInvalidUsageHandler)
                .missingPermission(paintMissingPermissionHandler)

                .message(LiteBukkitMessages.PLAYER_ONLY, messageHandler.getMessage(Messages.COMMAND_MANAGER_ONLY_PLAYER))
                .message(LiteBukkitMessages.PLAYER_NOT_FOUND, input -> messageHandler.getMessage(Messages.COMMAND_MANAGER_PLAYER_NOT_FOUND, new Placeholder("%player%", input)))

                .schematicGenerator(SchematicFormat.angleBrackets())

                .build();
    }
}
