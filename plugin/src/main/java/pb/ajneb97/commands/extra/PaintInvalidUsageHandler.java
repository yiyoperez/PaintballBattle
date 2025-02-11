package pb.ajneb97.commands.extra;

import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invalidusage.InvalidUsageHandler;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.schematic.Schematic;
import org.bukkit.command.CommandSender;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.utils.enums.Messages;
import team.unnamed.inject.Inject;

public class PaintInvalidUsageHandler implements InvalidUsageHandler<CommandSender> {

    @Inject
    private MessageHandler messageHandler;

    @Override
    public void handle(Invocation<CommandSender> invocation, InvalidUsage<CommandSender> result, ResultHandlerChain<CommandSender> chain) {
        CommandSender sender = invocation.sender();
        Schematic schematic = result.getSchematic();

        //TODO: Remove debug
        messageHandler.sendManualMessage(sender, "Cause " + result.getCause().toString());

        messageHandler.sendMessage(sender, Messages.COMMAND_MANAGER_HEADER);
        String message = messageHandler.getMessage(Messages.COMMAND_MANAGER_MESSAGE.getPath());
        String prefix = messageHandler.getMessage(Messages.COMMAND_MANAGER_PREFIX.getPath());

        if (schematic.isOnlyFirst()) {
            messageHandler.sendManualMessage(sender, message + " " + prefix + schematic.first());
            return;
        }

        messageHandler.sendManualMessage(sender, message);
        for (String scheme : schematic.all()) {
            messageHandler.sendManualMessage(sender, prefix + scheme);
        }
    }
}
