package pb.ajneb97.managers.shop;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.realized.tokenmanager.api.TokenManager;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.api.PaintballAPI;
import pb.ajneb97.utils.UtilidadesItems;

import java.util.List;

public class ShopManager {

    private final PaintballBattle plugin;
    private YamlDocument shop;

    public ShopManager(PaintballBattle plugin) {
        this.plugin = plugin;
        this.shop = plugin.getShopDocument();
    }

    public void openMainInventory(Player jugador) {
        YamlDocument shop = plugin.getShopDocument();
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', shop.getString("shopInventoryTitle")));
        for (String key : shop.getSection("shop_items").getRoutesAsStrings(false)) {
            ItemStack item = UtilidadesItems.crearItem(shop, "shop_items." + key);
            int slot = Integer.valueOf(shop.getString("shop_items." + key + ".slot"));
            if (slot != -1) {
                inv.setItem(slot, item);
            }
        }

        jugador.openInventory(inv);
    }

    public void openPerksInventory(Player jugador) {
        YamlDocument shop = plugin.getShopDocument();
        YamlDocument config = plugin.getConfigDocument();
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', shop.getString("shopPerksInventoryTitle")));
        for (String key : shop.getSection("perks_items").getRoutesAsStrings(false)) {
            ItemStack item = UtilidadesItems.crearItem(shop, "perks_items." + key);
            if (key.equals("coins_info")) {
                ItemMeta meta = item.getItemMeta();
                if (config.getString("economy_used").equals("vault")) {
                    Economy econ = plugin.getEconomy();
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', meta.getDisplayName().replace("%coins%", econ.getBalance(jugador) + "")));
                } else if (config.getString("economy_used").equals("token_manager")) {
                    TokenManager tokenManager = (TokenManager) Bukkit.getPluginManager().getPlugin("TokenManager");
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', meta.getDisplayName().replace("%coins%", tokenManager.getTokens(jugador).orElse(0) + "")));
                } else {
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', meta.getDisplayName().replace("%coins%", PaintballAPI.getCoins(jugador) + "")));
                }

                item.setItemMeta(meta);
            }
            if (shop.contains("perks_items." + key + ".slot")) {
                int slot = Integer.valueOf(shop.getString("perks_items." + key + ".slot"));
                if (slot != -1) {
                    inv.setItem(slot, item);
                }
            }

        }
        ItemStack item = UtilidadesItems.crearItem(shop, "perks_items.decorative_item");
        for (int i = 0; i <= 8; i++) {
            inv.setItem(i, item);
        }
        for (int i = 36; i <= 44; i++) {
            inv.setItem(i, item);
        }

        int levelExtraLives = PaintballAPI.getPerkLevel(jugador, "extra_lives");
        List<String> lista = shop.getStringList("perks_upgrades.extra_lives");
        for (int i = 0; i < lista.size(); i++) {
            if (i > levelExtraLives - 1) {
                item = UtilidadesItems.crearItem(shop, "perks_items.extra_lives_perk_item");
            } else {
                item = UtilidadesItems.crearItem(shop, "perks_items.extra_lives_bought_perk_item");
            }
            ItemMeta meta = item.getItemMeta();
            String[] separados = lista.get(i).split(";");
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', meta.getDisplayName().replace("%name%", separados[2])));
            List<String> lore = meta.getLore();
            for (int c = 0; c < lore.size(); c++) {
                lore.set(c, lore.get(c).replace("%amount%", separados[0]).replace("%cost%", separados[1]));
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(9 + i, item);

            if (i == 8) {
                break;
            }
        }

        int levelInitialKillcoins = PaintballAPI.getPerkLevel(jugador, "initial_killcoins");
        lista = shop.getStringList("perks_upgrades.initial_killcoins");
        for (int i = 0; i < lista.size(); i++) {
            if (i > levelInitialKillcoins - 1) {
                item = UtilidadesItems.crearItem(shop, "perks_items.initial_killcoins_perk_item");
            } else {
                item = UtilidadesItems.crearItem(shop, "perks_items.initial_killcoins_bought_perk_item");
            }
            ItemMeta meta = item.getItemMeta();
            String[] separados = lista.get(i).split(";");
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', meta.getDisplayName().replace("%name%", separados[2])));
            List<String> lore = meta.getLore();
            for (int c = 0; c < lore.size(); c++) {
                lore.set(c, lore.get(c).replace("%amount%", separados[0]).replace("%cost%", separados[1]));
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(18 + i, item);

            if (i == 8) {
                break;
            }
        }

        int levelExtraKillcoins = PaintballAPI.getPerkLevel(jugador, "extra_killcoins");
        lista = shop.getStringList("perks_upgrades.extra_killcoins");
        for (int i = 0; i < lista.size(); i++) {
            if (i > levelExtraKillcoins - 1) {
                item = UtilidadesItems.crearItem(shop, "perks_items.extra_killcoins_perk_item");
            } else {
                item = UtilidadesItems.crearItem(shop, "perks_items.extra_killcoins_bought_perk_item");
            }
            ItemMeta meta = item.getItemMeta();
            String[] separados = lista.get(i).split(";");
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', meta.getDisplayName().replace("%name%", separados[2])));
            List<String> lore = meta.getLore();
            for (int c = 0; c < lore.size(); c++) {
                lore.set(c, lore.get(c).replace("%amount%", separados[0]).replace("%cost%", separados[1]));
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(27 + i, item);

            if (i == 8) {
                break;
            }
        }

        jugador.openInventory(inv);
    }

    public void openHatsInventory(Player jugador) {
        YamlDocument shop = plugin.getShopDocument();
        YamlDocument config = plugin.getConfigDocument();
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', shop.getString("shopHatsInventoryTitle")));
        for (String key : shop.getSection("hats_items").getRoutesAsStrings(false)) {
            ItemStack item = UtilidadesItems.crearItem(shop, "hats_items." + key);
            if (key.equals("coins_info")) {
                ItemMeta meta = item.getItemMeta();
                if (config.getString("economy_used").equals("vault")) {
                    Economy econ = plugin.getEconomy();
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', meta.getDisplayName().replace("%coins%", econ.getBalance(jugador) + "")));
                } else if (config.getString("economy_used").equals("token_manager")) {
                    TokenManager tokenManager = (TokenManager) Bukkit.getPluginManager().getPlugin("TokenManager");
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', meta.getDisplayName().replace("%coins%", tokenManager.getTokens(jugador).orElse(0) + "")));
                } else {
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', meta.getDisplayName().replace("%coins%", PaintballAPI.getCoins(jugador) + "")));
                }

                item.setItemMeta(meta);
            } else {
                if (!key.equals("go_to_menu")) {
                    if (PaintballAPI.hasHat(jugador, key)) {
                        ItemMeta meta = item.getItemMeta();
                        List<String> lore = shop.getStringList("hats_items." + key + ".bought_lore");
                        for (int i = 0; i < lore.size(); i++) {
                            lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));
                        }
                        meta.setLore(lore);
                        item.setItemMeta(meta);
                    }
                }
            }

            // TODO: Update head method
//            if (shop.contains("hats_items." + key + ".skull_id")) {
//                String id = shop.getString("hats_items." + key + ".skull_id");
//                String textura = shop.getString("hats_items." + key + ".skull_texture");
//                item = UtilidadesItems.getCabeza(item, textura);
//            }

            if (shop.contains("hats_items." + key + ".slot")) {
                int slot = Integer.valueOf(shop.getString("hats_items." + key + ".slot"));
                if (slot != -1) {
                    inv.setItem(slot, item);
                }
            }

        }

        jugador.openInventory(inv);
    }
}
