package pb.ajneb97.commands;

import dev.triumphteam.cmd.core.annotation.SubCommand;
import org.bukkit.entity.Player;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.managers.Checks;
import pb.ajneb97.managers.inventory.InventarioShop;

public class ShopCommand extends MainCommand {

    private PaintballBattle plugin;

    public ShopCommand(PaintballBattle plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @SubCommand(value = "shop")
    public void command(Player player) {
        if (!Checks.checkTodo(plugin, player)) {
            return;
        }

        InventarioShop.crearInventarioPrincipal(player, plugin);
    }
}
