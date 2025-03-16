package pb.ajneb97.inventories.util;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public interface BaseMenu {

    /**
     * @param player Player to open the menu to.
     */
    default void open(Player player) {

    }


    default void open(Player player, Object... objects) {

    }

    default void addMenuItem(Gui menu, GuiItem item, int slot, Consumer<InventoryClickEvent> clickAction) {

        item.setAction(event -> {
            ItemStack stack = event.getCurrentItem();
            if (stack != null && stack.isSimilar(item.getItemStack())) {
                clickAction.accept(event);
            }
        });

        menu.setItem(slot, item);
    }

    default GuiItem createGuiItem(Material material, Component name, List<Component> lore) {
        return ItemBuilder.from(material)
                .name(name)
                .lore(lore)
                .lore(lore)
                .asGuiItem();
    }
}