package pb.ajneb97.commands.extra;

import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.permission.MissingPermissionsHandler;
import org.bukkit.command.CommandSender;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.core.utils.message.Placeholder;
import pb.ajneb97.utils.enums.Messages;
import team.unnamed.inject.Inject;

public class PaintMissingPermissionHandler implements MissingPermissionsHandler<CommandSender> {

    @Inject
    private MessageHandler messageHandler;

    @Override
    public void handle(Invocation<CommandSender> invocation, MissingPermissions missingPermissions, ResultHandlerChain<CommandSender> chain) {
        String permissions = missingPermissions.asJoinedText();
        CommandSender sender = invocation.sender();

        messageHandler.sendMessage(sender, Messages.COMMAND_MANAGER_NO_PERMISSION, new Placeholder("%permission%", permissions));
    }
}