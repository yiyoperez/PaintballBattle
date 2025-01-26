package pb.ajneb97.utils.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pb.ajneb97.core.utils.message.MessageUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemStackBuilder extends ItemStack {

    public ItemStackBuilder() {
        this(Material.STONE);
    }

    public ItemStackBuilder(Material type) {
        super(type);
        lore(new ArrayList<>());
    }

    public ItemStackBuilder material(Material material) {
        setType(material);
        return this;
    }

    public ItemStackBuilder addAmount(int change) {
        setAmount(getAmount() + change);
        return this;
    }

    public ItemStackBuilder amount(int amount) {
        setAmount(amount);
        return this;
    }

    public ItemStackBuilder name(String name) {
        if (name == null) return this;

        ItemMeta itemMeta = getItemMeta();
        itemMeta.setDisplayName(name.isEmpty() ? " " : MessageUtils.translateColor(name));
        setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder addBlankLineLore() {
        addLore(" ");
        return this;
    }

    public ItemStackBuilder addLore(String... lore) {
        if (lore == null) return this;
        ItemMeta itemMeta = getItemMeta();
        List<String> original = itemMeta.getLore();
        if (original == null) original = new ArrayList<>();
        Collections.addAll(original, MessageUtils.translateToArray(lore));
        itemMeta.setLore(original);
        setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder addLore(List<String> lore) {
        if (lore == null) return this;
        ItemMeta itemMeta = getItemMeta();
        List<String> original = itemMeta.getLore();
        if (original == null) original = new ArrayList<>();
        original.addAll(MessageUtils.translateColor(lore));
        itemMeta.setLore(original);
        setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder lore(String... lore) {
        if (lore == null) return this;
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setLore(MessageUtils.translateColor(lore));
        setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder lore(List<String> lore) {
        if (lore == null) return this;
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setLore(MessageUtils.translateColor(lore));
        setItemMeta(itemMeta);
        return this;
    }
}
