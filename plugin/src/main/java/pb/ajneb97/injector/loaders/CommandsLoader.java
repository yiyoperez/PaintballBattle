package pb.ajneb97.injector.loaders;

import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.schematic.SchematicFormat;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.commands.*;
import pb.ajneb97.commons.loader.Loader;
import pb.ajneb97.core.logger.Logger;
import pb.ajneb97.core.utils.message.MessageHandler;
import team.unnamed.inject.InjectAll;

@SuppressWarnings("unused")
@InjectAll
public final class CommandsLoader implements Loader {

    private PaintballBattle plugin;
    private MessageHandler messageHandler;

    private MainCommand mainCommand;

    private ListCommand listCommand;
    private EditCommand editCommand;
    private ShopCommand shopCommand;
    private JoinCommand joinCommand;
    private LeaveCommand leaveCommand;
    private CreateCommand createCommand;
    private DeleteCommand deleteCommand;
    private EnableCommand enableCommand;
    private ReloadCommand reloadCommand;
    private DisableCommand disableCommand;
    private GiveCoinsCommand giveCoinsCommand;
    private JoinRandomCommand joinRandomCommand;
    private SetMainLobbyCommand setMainLobbyCommand;

    @Override
    public void load() {
        Logger.info("Starting commands loader.");
        LiteBukkitFactory.builder()
                .settings(settings -> settings
                        .fallbackPrefix("paintball")
                        .nativePermissions(false)
                )

                .commands(
                        mainCommand,
                        listCommand,
                        editCommand,
                        shopCommand,
                        joinCommand,
                        leaveCommand,
                        reloadCommand,
                        createCommand,
                        deleteCommand,
                        enableCommand,
                        disableCommand,
                        giveCoinsCommand,
                        joinRandomCommand,
                        setMainLobbyCommand
                )

                //.message(LiteBukkitMessages.PLAYER_NOT_FOUND, input -> messageHandler.getMessage("PLAYER_NOT_FOUND", new Placeholder("%player%", input)))
                //.message(LiteBukkitMessages.PLAYER_ONLY, messageHandler.getMessage(Messages.COMMAND_MANAGER_ONLY_PLAYER.getPath()))

                //.argumentSuggestion(Player.class, JoinArgument.KEY, SuggestionResult.of(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toSet())))
                .schematicGenerator(SchematicFormat.angleBrackets())

                .build();
    }
}
