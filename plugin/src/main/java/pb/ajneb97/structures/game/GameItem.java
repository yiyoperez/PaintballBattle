package pb.ajneb97.structures.game;

import org.bukkit.inventory.ItemStack;

public class GameItem {

    private final boolean enabled;
    private final ItemStack item;
    private final int slot;

    public GameItem(boolean enabled, ItemStack item, int slot) {
        this.enabled = enabled;
        this.item = item;
        this.slot = slot;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getSlot() {
        return slot;
    }
}
