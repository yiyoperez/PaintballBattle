package pb.ajneb97.commands;

import dev.triumphteam.cmd.core.annotation.SubCommand;
import org.bukkit.entity.Player;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.managers.Checks;
import pb.ajneb97.managers.shop.ShopManager;

public class ShopCommand extends MainCommand {

    private PaintballBattle plugin;
    private ShopManager shopManager;

    public ShopCommand(PaintballBattle plugin) {
        super(plugin);
        this.plugin = plugin;
        this.shopManager = plugin.getShopManager();
    }

    @SubCommand(value = "shop")
    public void command(Player player) {
        if (!Checks.checkTodo(plugin, player)) {
            return;
        }

        shopManager.openMainInventory(player);
    }
}
