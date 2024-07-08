package pb.ajneb97.listeners.shop;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.realized.tokenmanager.api.TokenManager;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.api.PaintballAPI;
import pb.ajneb97.managers.shop.ShopManager;

import java.util.List;

public class ShopListeners implements Listener {

    private PaintballBattle plugin;

    private YamlDocument shop;
    private YamlDocument config;
    private YamlDocument messages;

    private ShopManager shopManager;

    public ShopListeners(PaintballBattle plugin) {
        this.plugin = plugin;
        this.shop = plugin.getShopDocument();
        this.config = plugin.getConfigDocument();
        this.messages = plugin.getMessagesDocument();
        this.shopManager = plugin.getShopManager();
    }

    @EventHandler
    public void clickInventarioPrincipal(InventoryClickEvent event) {
        String pathInventory = ChatColor.translateAlternateColorCodes('&', shop.getString("shopInventoryTitle"));
        String pathInventoryM = ChatColor.stripColor(pathInventory);
        if (ChatColor.stripColor(event.getView().getTitle()).equals(pathInventoryM)) {
            if (event.getCurrentItem() == null) {
                event.setCancelled(true);
                return;
            }
            if ((event.getSlotType() == null)) {
                event.setCancelled(true);
                return;
            } else {
                Player jugador = (Player) event.getWhoClicked();
                event.setCancelled(true);
                if (event.getClickedInventory().equals(jugador.getOpenInventory().getTopInventory())) {
                    int slot = event.getSlot();
                    for (String key : shop.getSection("shop_items").getRoutesAsStrings(false)) {
                        if (slot == Integer.valueOf(shop.getString("shop_items." + key + ".slot"))) {
                            if (key.equals("perks_items")) {
                                shopManager.openPerksInventory(jugador);
                            } else if (key.equals("hats_items")) {
                                shopManager.openHatsInventory(jugador);
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void clickInventarioPerks(InventoryClickEvent event) {
        String pathInventory = ChatColor.translateAlternateColorCodes('&', shop.getString("shopPerksInventoryTitle"));
        String pathInventoryM = ChatColor.stripColor(pathInventory);
        String prefix = ChatColor.translateAlternateColorCodes('&', messages.getString("prefix")) + " ";
        if (ChatColor.stripColor(event.getView().getTitle()).equals(pathInventoryM)) {
            if (event.getCurrentItem() == null) {
                event.setCancelled(true);
                return;
            }
            if ((event.getSlotType() == null)) {
                event.setCancelled(true);
                return;
            } else {
                final Player jugador = (Player) event.getWhoClicked();
                event.setCancelled(true);
                if (event.getClickedInventory().equals(jugador.getOpenInventory().getTopInventory())) {
                    if (!event.getCurrentItem().getType().equals(Material.AIR)) {
                        int slot = event.getSlot();
                        if (slot >= 9 && slot <= 17 || slot >= 18 && slot <= 26 || slot >= 27 && slot <= 35) {
                            int slotSum = 0;
                            String perk = "";
                            if (slot >= 9 && slot <= 17) {
                                //ExtraLives
                                slotSum = 9;
                                perk = "extra_lives";
                            } else if (slot >= 18 && slot <= 26) {
                                //Initial KillCoins
                                slotSum = 18;
                                perk = "initial_killcoins";
                            } else {
                                //Extra KillCoins
                                slotSum = 27;
                                perk = "extra_killcoins";
                            }

                            List<String> lista = shop.getStringList("perks_upgrades." + perk);
                            for (int i = 0; i < lista.size(); i++) {
                                String[] separados = lista.get(i).split(";");
                                if (slot == slotSum + i) {
                                    //Si es nivel 1 significa que el proximo nivel a desbloquear es el slot 10
                                    int nivel = PaintballAPI.getPerkLevel(jugador, perk);
                                    int slotADesbloquear = nivel + slotSum;
                                    if (slot == slotADesbloquear) {
                                        int cost = Integer.valueOf(separados[1]);
                                        double dinero = 0;
                                        if (config.getString("economy_used").equals("vault")) {
                                            Economy econ = plugin.getEconomy();
                                            dinero = econ.getBalance(jugador);
                                            if (dinero < cost) {
                                                jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("buyNoSufficientCoins")));
                                                return;
                                            }
                                            econ.withdrawPlayer(jugador, cost);
                                        } else if (config.getString("economy_used").equals("token_manager")) {
                                            TokenManager tokenManager = (TokenManager) Bukkit.getPluginManager().getPlugin("TokenManager");
                                            float dineroF = tokenManager.getTokens(jugador).orElse(0);
                                            if (dineroF < cost) {
                                                jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("buyNoSufficientCoins")));
                                                return;
                                            }
                                            tokenManager.removeTokens(jugador, cost);
                                        } else {
                                            dinero = PaintballAPI.getCoins(jugador);
                                            if (dinero < cost) {
                                                jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("buyNoSufficientCoins")));
                                                return;
                                            }
                                            PaintballAPI.removeCoins(jugador, cost);
                                        }
                                        //TODO
//                                            plugin.registerPlayer(jugador.getUniqueId().toString() + ".yml");
//                                            if (plugin.getJugador(jugador.getName()) == null) {
//                                                plugin.agregarJugadorDatos(new JugadorDatos(jugador.getName(), jugador.getUniqueId().toString(), 0, 0, 0, 0, 0, new ArrayList<Perk>(), new ArrayList<Hat>()));
//                                            }
//                                            JugadorDatos jDatos = plugin.getJugador(jugador.getName());
//                                            jDatos.setPerk(perk, nivel + 1);
                                        jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("perkUnlocked").replace("%name%", separados[2])));
                                        String[] separadosSound = config.getString("shopUnlockSound").split(";");
                                        try {
                                            Sound sound = Sound.valueOf(separadosSound[0]);
                                            jugador.playSound(jugador.getLocation(), sound, Float.valueOf(separadosSound[1]), Float.valueOf(separadosSound[2]));
                                        } catch (Exception ex) {
                                            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PaintballBattle.prefix + "&7Sound Name: &c" + separadosSound[0] + " &7is not valid."));
                                        }
                                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                            public void run() {
                                                shopManager.openPerksInventory(jugador);
                                            }
                                        }, 5L);
                                    } else if (slot > slotADesbloquear) {
                                        jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("perkErrorPrevious")));
                                        return;
                                    } else {
                                        jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("perkErrorUnlocked")));
                                        return;
                                    }

                                    return;
                                }
                            }
                        } else if (slot == Integer.valueOf(shop.getString("perks_items.go_to_menu.slot"))) {
                            shopManager.openMainInventory(jugador);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void clickInventarioHats(InventoryClickEvent event) {
        String pathInventory = ChatColor.translateAlternateColorCodes('&', shop.getString("shopHatsInventoryTitle"));
        String pathInventoryM = ChatColor.stripColor(pathInventory);
        String prefix = ChatColor.translateAlternateColorCodes('&', messages.getString("prefix")) + " ";
        if (ChatColor.stripColor(event.getView().getTitle()).equals(pathInventoryM)) {
            if (event.getCurrentItem() == null) {
                event.setCancelled(true);
                return;
            }
            if ((event.getSlotType() == null)) {
                event.setCancelled(true);
                return;
            } else {
                final Player jugador = (Player) event.getWhoClicked();
                event.setCancelled(true);
                if (event.getClickedInventory().equals(jugador.getOpenInventory().getTopInventory())) {
                    if (!event.getCurrentItem().getType().equals(Material.AIR)) {
                        int slot = event.getSlot();
                        for (String key : shop.getSection("hats_items").getRoutesAsStrings(false)) {
                            if (key.equals("go_to_menu")) {
                                if (slot == Integer.valueOf(shop.getString("hats_items." + key + ".slot"))) {
                                    shopManager.openHatsInventory(jugador);
                                    return;
                                }
                            } else if (!key.equals("coins_info")) {
                                if (slot == Integer.valueOf(shop.getString("hats_items." + key + ".slot"))) {
                                    if (PaintballAPI.hasHat(jugador, key)) {
                                        jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("hatErrorBought")));
                                        return;
                                    }
                                    int cost = Integer.valueOf(shop.getString("hats_items." + key + ".cost"));
                                    double dinero = 0;
                                    if (config.getString("economy_used").equals("vault")) {
                                        Economy econ = plugin.getEconomy();
                                        dinero = econ.getBalance(jugador);
                                        if (dinero < cost) {
                                            jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("buyNoSufficientCoins")));
                                            return;
                                        }
                                        econ.withdrawPlayer(jugador, cost);
                                    } else if (config.getString("economy_used").equals("token_manager")) {
                                        TokenManager tokenManager = (TokenManager) Bukkit.getPluginManager().getPlugin("TokenManager");
                                        float dineroF = tokenManager.getTokens(jugador).orElse(0);
                                        if (dineroF < cost) {
                                            jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("buyNoSufficientCoins")));
                                            return;
                                        }
                                        tokenManager.removeTokens(jugador, cost);
                                    } else {
                                        dinero = PaintballAPI.getCoins(jugador);
                                        if (dinero < cost) {
                                            jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("buyNoSufficientCoins")));
                                            return;
                                        }
                                        PaintballAPI.removeCoins(jugador, cost);
                                    }

                                    //TODO
//                                        plugin.registerPlayer(jugador.getUniqueId().toString() + ".yml");
//                                        if (plugin.getJugador(jugador.getName()) == null) {
//                                            plugin.agregarJugadorDatos(new JugadorDatos(jugador.getName(), jugador.getUniqueId().toString(), 0, 0, 0, 0, 0, new ArrayList<Perk>(), new ArrayList<Hat>()));
//                                        }
//                                        JugadorDatos jDatos = plugin.getJugador(jugador.getName());
//                                        jDatos.agregarHat(key);
                                    jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("hatBought").replace("%name%", shop.getString("hats_items." + key + ".name"))));
                                    String[] separadosSound = config.getString("shopUnlockSound").split(";");
                                    try {
                                        Sound sound = Sound.valueOf(separadosSound[0]);
                                        jugador.playSound(jugador.getLocation(), sound, Float.valueOf(separadosSound[1]), Float.valueOf(separadosSound[2]));
                                    } catch (Exception ex) {
                                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PaintballBattle.prefix + "&7Sound Name: &c" + separadosSound[0] + " &7is not valid."));
                                    }
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                        public void run() {
                                            shopManager.openHatsInventory(jugador);
                                        }
                                    }, 5L);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
