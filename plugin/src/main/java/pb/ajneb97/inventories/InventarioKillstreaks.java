package pb.ajneb97.inventories;

public class InventarioKillstreaks {

//    int taskID;
//    private PaintballBattle plugin;
//
//    public void actualizarInventario(final Player jugador, final Game partida) {
//        BukkitScheduler sh = Bukkit.getServer().getScheduler();
//        taskID = sh.scheduleSyncRepeatingTask(plugin, new Runnable() {
//            public void run() {
//                if (!update(jugador, config, messages, partida)) {
//                    Bukkit.getScheduler().cancelTask(taskID);
//                    return;
//                }
//            }
//        }, 0L, 20L);
//    }
//
//    protected boolean update(Player jugador, YamlDocument config, YamlDocument messages, Game partida) {
//        String pathInventory = ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', config.getString("killstreaks_inventory_title")));
//        String pathInventoryM = ChatColor.stripColor(pathInventory);
//        Inventory inv = jugador.getOpenInventory().getTopInventory();
//        if (partida == null) {
//            return false;
//        }
//        PaintballPlayer j = partida.getJugador(jugador.getName());
//        if (j == null) {
//            return false;
//        }
//        if (inv != null && ChatColor.stripColor(jugador.getOpenInventory().getTitle()).equals(pathInventoryM)) {
//
//            for (String key : config.getSection("killstreaks_items").getRoutesAsStrings(false)) {
//                ItemStack item = UtilidadesItems.crearItem(config, "killstreaks_items." + key);
//
//                Killstreak k = j.getKillstreak(key);
//                if (k != null) {
//                    ItemMeta meta = item.getItemMeta();
//                    List<String> lore = new ArrayList<String>();
//                    lore.add(ChatColor.translateAlternateColorCodes('&', messages.getString("killstreakCurrentlyActive").replace("%time%", k.getTime() + "")));
//                    meta.setLore(lore);
//                    item.setItemMeta(meta);
//                }
//                int slot = Integer.valueOf(config.getString("killstreaks_items." + key + ".slot"));
//                if (slot != -1) {
//                    inv.setItem(slot, item);
//                }
//            }
//            return true;
//        } else {
//            return false;
//        }
//    }

}
