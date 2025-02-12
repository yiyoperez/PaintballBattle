package pb.ajneb97.commands;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.ExecuteDefault;
import dev.rollczi.litecommands.invocation.Invocation;
import jdk.jfr.Description;
import org.bukkit.command.CommandSender;
import pb.ajneb97.commands.extra.PaintCommand;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.utils.enums.Messages;
import team.unnamed.inject.Inject;

@Command(name = "paintball", aliases = {"pb", "pball"})
@Description("Main command of PaintballBattle")
public class MainCommand implements PaintCommand {

    @Inject
    private MessageHandler messageHandler;

    @ExecuteDefault
    public void command(@Context CommandSender sender, @Context Invocation<CommandSender> invocation) {
        //TODO: Maybe get argument from invocation and set specific usages per command?
        messageHandler.sendListMessage(sender, Messages.HELP_MESSAGE);
    }
}
