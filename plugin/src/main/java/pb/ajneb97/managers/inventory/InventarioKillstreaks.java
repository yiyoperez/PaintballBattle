package pb.ajneb97.managers.inventory;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.juego.Killstreak;
import pb.ajneb97.juego.PaintballPlayer;
import pb.ajneb97.juego.Partida;
import pb.ajneb97.utils.UtilidadesItems;

import java.util.ArrayList;
import java.util.List;

public class InventarioKillstreaks {

    int taskID;
    private PaintballBattle plugin;

    public InventarioKillstreaks(PaintballBattle plugin) {
        this.plugin = plugin;
    }

    public void actualizarInventario(final Player jugador, final Partida partida) {
        BukkitScheduler sh = Bukkit.getServer().getScheduler();
        final YamlDocument config = plugin.getConfigDocument();
        final YamlDocument messages = plugin.getMessagesDocument();
        taskID = sh.scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                if (!update(jugador, config, messages, partida)) {
                    Bukkit.getScheduler().cancelTask(taskID);
                    return;
                }
            }
        }, 0L, 20L);
    }

    protected boolean update(Player jugador, YamlDocument config, YamlDocument messages, Partida partida) {
        String pathInventory = ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', config.getString("killstreaks_inventory_title")));
        String pathInventoryM = ChatColor.stripColor(pathInventory);
        Inventory inv = jugador.getOpenInventory().getTopInventory();
        if (partida == null) {
            return false;
        }
        PaintballPlayer j = partida.getJugador(jugador.getName());
        if (j == null) {
            return false;
        }
        if (inv != null && ChatColor.stripColor(jugador.getOpenInventory().getTitle()).equals(pathInventoryM)) {

            for (String key : config.getSection("killstreaks_items").getRoutesAsStrings(false)) {
                ItemStack item = UtilidadesItems.crearItem(config, "killstreaks_items." + key);

                Killstreak k = j.getKillstreak(key);
                if (k != null) {
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = new ArrayList<String>();
                    lore.add(ChatColor.translateAlternateColorCodes('&', messages.getString("killstreakCurrentlyActive").replace("%time%", k.getTime() + "")));
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                }
                int slot = Integer.valueOf(config.getString("killstreaks_items." + key + ".slot"));
                if (slot != -1) {
                    inv.setItem(slot, item);
                }
            }
            return true;
        } else {
            return false;
        }
    }

}
