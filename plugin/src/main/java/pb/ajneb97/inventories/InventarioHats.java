package pb.ajneb97.inventories;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.event.Listener;
import pb.ajneb97.PaintballBattle;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

public class InventarioHats implements Listener {

    @Inject
    private PaintballBattle plugin;

    @Inject
    @Named("config")
    private YamlDocument config;
    @Inject
    @Named("messages")
    private YamlDocument messages;

//
//    public static void crearInventario(Player jugador, PaintballBattle plugin) {
//        Inventory inv = Bukkit.createInventory(null, 27, MessageUtils.translateColor(config.getString("hats_inventory_title")));
//        ArrayList<Hat> hats = PaintballAPI.getHats(jugador);
//        int slot = 0;
//        if (hats.isEmpty()) {
//            ItemStack item = UtilidadesItems.crearItem(config, "hats_items.no_hats");
//            inv.setItem(13, item);
//        } else {
//            for (Hat h : hats) {
//                String name = h.getName();
//                ItemStack item = UtilidadesItems.crearItem(config, "hats_items." + name);
//                ItemMeta meta = item.getItemMeta();
//                List<String> lore = meta.getLore();
//                String status = "";
//                if (h.isSelected()) {
//                    status = messages.getString("hatStatusSelected");
//                } else {
//                    status = messages.getString("hatStatusNotSelected");
//                }
//                for (int i = 0; i < lore.size(); i++) {
//                    lore.set(i, MessageUtils.translateColor(lore.get(i).replace("%status%", status)));
//                }
//                meta.setLore(lore);
//                item.setItemMeta(meta);
//
//                if (config.contains("hats_items." + name + ".skull_id")) {
//                    String id = config.getString("hats_items." + name + ".skull_id");
//                    String textura = config.getString("hats_items." + name + ".skull_texture");
//                    item = SkullUtils.getCustomSkull(textura);
//                }
//
//                inv.setItem(slot, item);
//                slot++;
//            }
//
//            ItemStack item = UtilidadesItems.crearItem(config, "hats_items.remove_hat");
//            inv.setItem(26, item);
//        }
//
//        jugador.openInventory(inv);
//    }
//
//    @EventHandler
//    public void clickInventario(InventoryClickEvent event) {
//        YamlDocument config = plugin.getHatsDocument();
//        String pathInventory = MessageUtils.translateColor(config.getString("hats_inventory_title"));
//        String pathInventoryM = ChatColor.stripColor(pathInventory);
//        YamlDocument messages = plugin.getMessagesDocument();
//        String prefix = MessageUtils.translateColor(messages.getString("prefix")) + " ";
//        if (ChatColor.stripColor(event.getView().getTitle()).equals(pathInventoryM)) {
//            if (event.getCurrentItem() == null) {
//                event.setCancelled(true);
//                return;
//            }
//            if ((event.getSlotType() == null)) {
//                event.setCancelled(true);
//                return;
//            } else {
//                final Player jugador = (Player) event.getWhoClicked();
//                event.setCancelled(true);
//                if (event.getClickedInventory().equals(jugador.getOpenInventory().getTopInventory())) {
//                    ArrayList<Hat> hats = PaintballAPI.getHats(jugador);
//                    ItemStack item = event.getCurrentItem();
//                    if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
//                        if (event.getSlot() == 26) {
//                            //TODO
////                                JugadorDatos jDatos = plugin.getJugador(jugador.getName());
////                                jDatos.deseleccionarHats();
//                            jugador.sendMessage(prefix + MessageUtils.translateColor(messages.getString("hatRemoved")));
//                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
//                                public void run() {
//                                    InventarioHats.crearInventario(jugador, plugin);
//                                }
//                            }, 5L);
//                            return;
//                        }
//                        for (Hat h : hats) {
//                            ItemStack itemConfig = UtilidadesItems.crearItem(config, "hats_items." + h.getName());
//                            ItemMeta meta = item.getItemMeta();
//                            ItemMeta metaConfig = itemConfig.getItemMeta();
//                            if (item.getType().equals(itemConfig.getType()) && meta.getDisplayName().equals(metaConfig.getDisplayName())) {
//                                //Seleccionar hat
//                                if (PaintballAPI.hasHatSelected(jugador, h.getName())) {
//                                    jugador.sendMessage(prefix + MessageUtils.translateColor(messages.getString("hatAlreadySelected")));
//                                    return;
//                                }
//                                //TODO
////                                    JugadorDatos jDatos = plugin.getJugador(jugador.getName());
////                                    jDatos.seleccionarHat(h.getName());
//                                jugador.sendMessage(prefix + MessageUtils.translateColor(messages.getString("hatSelected").replace("%name%", config.getString("hats_items." + h.getName() + ".name"))));
//                                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
//                                    public void run() {
//                                        InventarioHats.crearInventario(jugador, plugin);
//                                    }
//                                }, 5L);
//                                return;
//                            }
//
//                        }
//                    }
//                }
//            }
//        }
//    }
}
