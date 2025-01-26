package pb.ajneb97.structures;

import org.bukkit.inventory.ItemStack;

public class GameItem {

    private boolean enabled;
    private ItemStack item;
    private int slot;


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
