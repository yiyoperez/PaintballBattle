package pb.ajneb97.utils.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Comparator;

public class ItemStackComparator implements Comparator<ItemStack> {

    @Override
    public int compare(ItemStack o1, ItemStack o2) {
        return (isSame(o1, o2) ? 0 : 1);
    }

    public boolean isSame(ItemStack a, ItemStack b) {
        if (a == null || b == null) return false;

        boolean type = a.getType() == b.getType();
        boolean amount = a.getAmount() == b.getAmount();
        boolean itemMeta = a.hasItemMeta() == b.hasItemMeta();

        return (type) && (amount) && (itemMeta) && isSameMeta(a.getItemMeta(), b.getItemMeta());
    }

    private boolean isSameMeta(ItemMeta a, ItemMeta b) {
        if (a == null || b == null) return false;

        boolean decide = true;

        boolean displayName = a.hasDisplayName() == b.hasDisplayName();
        boolean lore = a.hasLore() == b.hasLore();

        if (displayName) if (a.hasDisplayName()) if (!a.getDisplayName().equals(b.getDisplayName())) decide = false;
        if (lore) if (a.hasLore()) if (!a.getLore().equals(b.getLore())) decide = false;

        return decide;
    }
}