package pb.ajneb97.injector.modules;

import pb.ajneb97.commands.*;
import team.unnamed.inject.AbstractModule;

public final class CommandsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MainCommand.class).singleton();
        bind(EditCommand.class).singleton();
        bind(JoinCommand.class).singleton();
        bind(ListCommand.class).singleton();
        bind(ShopCommand.class).singleton();
        bind(LeaveCommand.class).singleton();
        bind(ReloadCommand.class).singleton();
        bind(CreateCommand.class).singleton();
        bind(DeleteCommand.class).singleton();
        bind(EnableCommand.class).singleton();
        bind(DisableCommand.class).singleton();
        bind(GiveCoinsCommand.class).singleton();
        bind(JoinRandomCommand.class).singleton();
        bind(SetMainLobbyCommand.class).singleton();
    }

}
