package pb.ajneb97.utils;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pb.ajneb97.structures.PaintballPlayer;

import java.util.List;

public class UtilidadesItems {

    @SuppressWarnings("deprecation")
    public static ItemStack crearItem(YamlDocument config, String path) {
        String id = config.getString(path + ".item");
        String[] idsplit = new String[2];
        int DataValue = 0;
        ItemStack stack = null;
        if (id.contains(":")) {
            idsplit = id.split(":");
            String stringDataValue = idsplit[1];
            DataValue = Integer.valueOf(stringDataValue);
            Material mat = Material.getMaterial(idsplit[0].toUpperCase());
            stack = new ItemStack(mat, 1, (short) DataValue);
        } else {
            Material mat = Material.getMaterial(id.toUpperCase());
            stack = new ItemStack(mat, 1);
        }
        ItemMeta meta = stack.getItemMeta();
        if (config.contains(path + ".name")) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString(path + ".name")));
        }
        if (config.contains(path + ".lore")) {
            List<String> lore = config.getStringList(path + ".lore");
            for (int c = 0; c < lore.size(); c++) {
                lore.set(c, ChatColor.translateAlternateColorCodes('&', lore.get(c)));
            }
            meta.setLore(lore);

        }
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_POTION_EFFECTS);
        if (Bukkit.getVersion().contains("1.15") || Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17")
                || Bukkit.getVersion().contains("1.18") || Bukkit.getVersion().contains("1.19") || Bukkit.getVersion().contains("1.20")) {
            meta.setUnbreakable(true);
        }
        stack.setItemMeta(meta);

        return stack;
    }

    public static void crearItemKillstreaks(PaintballPlayer jugador, YamlDocument config) {
        if (config.getString("killstreaks_item_enabled").equals("true")) {
            int coins = jugador.getCoins();
            ItemStack item = UtilidadesItems.crearItem(config, "killstreaks_item");
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("killstreaks_item.name").replace("%amount%", coins + "")));
            item.setItemMeta(meta);
            if (coins <= 1) {
                item.setAmount(1);
            } else if (coins >= 64) {
                item.setAmount(64);
            } else {
                item.setAmount(coins);
            }
            jugador.getPlayer().getInventory().setItem(8, item);
        }
    }

}
