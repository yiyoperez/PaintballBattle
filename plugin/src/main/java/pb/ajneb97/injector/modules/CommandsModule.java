package pb.ajneb97.injector.modules;

import pb.ajneb97.commands.*;
import pb.ajneb97.commands.extra.GameArgumentResolver;
import pb.ajneb97.commands.extra.PaintCommand;
import pb.ajneb97.commands.extra.PaintInvalidUsageHandler;
import pb.ajneb97.commands.extra.PaintMissingPermissionHandler;
import team.unnamed.inject.AbstractModule;

public final class CommandsModule extends AbstractModule {

    @Override
    protected void configure() {
        multibind(PaintCommand.class)
                .asSet()
                .to(MainCommand.class)
                .to(EditCommand.class)
                .to(JoinCommand.class)
                .to(ListCommand.class)
                .to(ShopCommand.class)
                .to(LeaveCommand.class)
                .to(ReloadCommand.class)
                .to(CreateCommand.class)
                .to(DeleteCommand.class)
                .to(EnableCommand.class)
                .to(DisableCommand.class)
                .to(GiveCoinsCommand.class)
                .to(JoinRandomCommand.class)
                .to(SetMainLobbyCommand.class)
                .singleton();

        bind(GameArgumentResolver.class).singleton();
        bind(PaintInvalidUsageHandler.class).singleton();
        bind(PaintMissingPermissionHandler.class).singleton();
    }

}
