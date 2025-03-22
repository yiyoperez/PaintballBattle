package pb.ajneb97.commands;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.entity.Player;
import pb.ajneb97.commands.extra.PaintCommand;
import pb.ajneb97.managers.ShopManager;
import team.unnamed.inject.Inject;

@Command(name = "paintball shop")
public class ShopCommand implements PaintCommand {

    @Inject
    private ShopManager shopManager;

    @Execute
    public void command(@Context Player player) {
        shopManager.openMainInventory(player);
    }
}
