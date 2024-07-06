package pb.ajneb97.managers.inventory;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.juego.GameEdit;
import pb.ajneb97.juego.Partida;

import java.util.ArrayList;
import java.util.List;

public class InventarioAdmin implements Listener {

    private final PaintballBattle plugin;
    private YamlDocument config;

    public InventarioAdmin(PaintballBattle plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigDocument();
    }

    public static void crearInventario(Player jugador, Partida partida, PaintballBattle plugin) {
        Inventory inv = Bukkit.createInventory(null, 36, ChatColor.translateAlternateColorCodes('&', "&2Editing Arena: &7" + partida.getNombre()));
        ItemStack item = new ItemStack(Material.BEACON, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lSet Lobby"));
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to define the arena Lobby in your"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7current position."));
        lore.add(ChatColor.translateAlternateColorCodes('&', ""));
        Location lobby = partida.getLobby();
        if (lobby == null) {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&9Current Position: &7NONE"));
        } else {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&9Current Position:"));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eX: &7" + lobby.getX()));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eY: &7" + lobby.getY()));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eZ: &7" + lobby.getZ()));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eWorld: &7" + lobby.getWorld().getName()));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eYaw: &7" + lobby.getYaw()));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&ePitch: &7" + lobby.getPitch()));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(10, item);

        item = new ItemStack(Material.QUARTZ_BLOCK, 1);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lSet Team 1 Spawn"));
        lore = new ArrayList<String>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to define the arena team 1 Spawn"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7in your current position."));
        lore.add(ChatColor.translateAlternateColorCodes('&', ""));
        Location spawn = partida.getTeam1().getSpawn();
        if (spawn == null) {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&9Current Position: &7NONE"));
        } else {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&9Current Position:"));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eX: &7" + spawn.getX()));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eY: &7" + spawn.getY()));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eZ: &7" + spawn.getZ()));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eWorld: &7" + spawn.getWorld().getName()));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eYaw: &7" + spawn.getYaw()));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&ePitch: &7" + spawn.getPitch()));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(11, item);

        item = new ItemStack(Material.QUARTZ_BLOCK, 1);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lSet Team 2 Spawn"));
        lore = new ArrayList<String>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to define the arena team 2 Spawn"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7in your current position."));
        lore.add(ChatColor.translateAlternateColorCodes('&', ""));
        spawn = partida.getTeam2().getSpawn();
        if (spawn == null) {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&9Current Position: &7NONE"));
        } else {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&9Current Position:"));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eX: &7" + spawn.getX()));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eY: &7" + spawn.getY()));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eZ: &7" + spawn.getZ()));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eWorld: &7" + spawn.getWorld().getName()));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eYaw: &7" + spawn.getYaw()));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&ePitch: &7" + spawn.getPitch()));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(12, item);

        item = new ItemStack(Material.GHAST_TEAR, partida.getCantidadMinimaJugadores());
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lSet Min Players"));
        lore = new ArrayList<String>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to define the arena minimum number"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7of players."));
        lore.add(ChatColor.translateAlternateColorCodes('&', ""));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&9Current Value: &7" + partida.getCantidadMinimaJugadores()));
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(13, item);

        item = new ItemStack(Material.GHAST_TEAR, partida.getCantidadMaximaJugadores());
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lSet Max Players"));
        lore = new ArrayList<String>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to define the arena maximum number"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7of players."));
        lore.add(ChatColor.translateAlternateColorCodes('&', ""));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&9Current Value: &7" + partida.getCantidadMaximaJugadores()));
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(14, item);

        item = new ItemStack(Material.NAME_TAG, 1);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lSet Team 1 Color"));
        lore = new ArrayList<String>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to define the arena team 1 Color."));
        lore.add(ChatColor.translateAlternateColorCodes('&', ""));
        if (partida.getTeam1().esRandom()) {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&9Current Value: &7random"));
        } else {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&9Current Value: &7" + partida.getTeam1().getTipo()));
        }


        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(15, item);

        item = new ItemStack(Material.NAME_TAG, 1);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lSet Team 2 Color"));
        lore = new ArrayList<String>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to define the arena team 2 Color."));
        lore.add(ChatColor.translateAlternateColorCodes('&', ""));
        if (partida.getTeam2().esRandom()) {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&9Current Value: &7random"));
        } else {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&9Current Value: &7" + partida.getTeam2().getTipo()));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(16, item);

        if (Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15")
                || Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17") || Bukkit.getVersion().contains("1.18")
                || Bukkit.getVersion().contains("1.19") || Bukkit.getVersion().contains("1.20")) {
            item = new ItemStack(Material.CLOCK, 1);
        } else {
            item = new ItemStack(Material.valueOf("WATCH"), 1);
        }

        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lSet Time"));
        lore = new ArrayList<String>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to define the arena time in seconds."));
        lore.add(ChatColor.translateAlternateColorCodes('&', ""));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&9Current Value: &7" + partida.getTiempoMaximo()));
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(21, item);

        item = new ItemStack(Material.REDSTONE_BLOCK, 1);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lSet Starting Lives"));
        lore = new ArrayList<String>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to define the starting amount of lives"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7for both teams."));
        lore.add(ChatColor.translateAlternateColorCodes('&', ""));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&9Current Value: &7" + partida.getVidasIniciales()));
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(23, item);

        jugador.openInventory(inv);

        GameEdit p = new GameEdit(jugador, partida);
        plugin.setPartidaEditando(p);
    }

    @EventHandler
    public void alCerrarInventario(InventoryCloseEvent event) {
        Player jugador = (Player) event.getPlayer();
        String pathInventory = ChatColor.translateAlternateColorCodes('&', "&2Editing Arena:");
        String pathInventoryM = ChatColor.stripColor(pathInventory);
        GameEdit partida = plugin.getPartidaEditando();
        if (partida != null && partida.getPlayer().getName().equals(jugador.getName())) {
            if (ChatColor.stripColor(event.getView().getTitle()).contains(pathInventoryM)) {
                plugin.removerPartidaEditando();
            }
        }
    }

    @EventHandler
    public void alSalir(PlayerQuitEvent event) {
        GameEdit partida = plugin.getPartidaEditando();
        Player jugador = event.getPlayer();
        if (partida != null && partida.getPlayer().getName().equals(jugador.getName())) {
            plugin.removerPartidaEditando();
        }
    }

    @EventHandler
    public void clickInventario(InventoryClickEvent event) {
        String pathInventory = ChatColor.translateAlternateColorCodes('&', "&2Editing Arena:");
        String pathInventoryM = ChatColor.stripColor(pathInventory);
        YamlDocument messages = plugin.getMessagesDocument();
        String prefix = ChatColor.translateAlternateColorCodes('&', messages.getString("prefix")) + " ";
        if (ChatColor.stripColor(event.getView().getTitle()).contains(pathInventoryM)) {
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
                    GameEdit partida = plugin.getPartidaEditando();
                    if (partida != null && partida.getPlayer().getName().equals(jugador.getName())) {
                        int slot = event.getSlot();
                        if (slot == 10) {
                            partida.getMatch().setLobby(jugador.getLocation().clone());
                            jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("lobbyDefined").replace("%name%", partida.getMatch().getNombre())));
                            InventarioAdmin.crearInventario(jugador, partida.getMatch(), plugin);
                        } else if (slot == 11) {
                            partida.getMatch().getTeam1().setSpawn(jugador.getLocation().clone());
                            jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("spawnTeamDefined").replace("%number%", "1").replace("%name%", partida.getMatch().getNombre())));
                            InventarioAdmin.crearInventario(jugador, partida.getMatch(), plugin);
                        } else if (slot == 12) {
                            partida.getMatch().getTeam2().setSpawn(jugador.getLocation().clone());
                            jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("spawnTeamDefined").replace("%number%", "2").replace("%name%", partida.getMatch().getNombre())));
                            InventarioAdmin.crearInventario(jugador, partida.getMatch(), plugin);
                        } else if (slot == 13) {
                            jugador.closeInventory();
                            GameEdit p = new GameEdit(jugador, partida.getMatch());
                            p.setStep("min");
                            plugin.setPartidaEditando(p);
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aWrite an even number."));
                        } else if (slot == 14) {
                            jugador.closeInventory();
                            GameEdit p = new GameEdit(jugador, partida.getMatch());
                            p.setStep("max");
                            plugin.setPartidaEditando(p);
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aWrite an even number."));
                        } else if (slot == 15) {
                            jugador.closeInventory();
                            GameEdit p = new GameEdit(jugador, partida.getMatch());
                            p.setStep("team1name");
                            plugin.setPartidaEditando(p);
                            String lista = "";
                            for (String key : config.getSection("teams").getRoutesAsStrings(false)) {
                                lista = lista + key + " ";
                            }
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aWrite one of these team names: &7random " + lista));
                        } else if (slot == 16) {
                            jugador.closeInventory();
                            GameEdit p = new GameEdit(jugador, partida.getMatch());
                            p.setStep("team2name");
                            plugin.setPartidaEditando(p);
                            String lista = "";
                            for (String key : config.getSection("teams").getRoutesAsStrings(false)) {
                                lista = lista + key + " ";
                            }
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aWrite one of these team names: &7random " + lista));
                        } else if (slot == 21) {
                            jugador.closeInventory();
                            GameEdit p = new GameEdit(jugador, partida.getMatch());
                            p.setStep("time");
                            plugin.setPartidaEditando(p);
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aWrite a number. This will be the arena time in seconds."));
                        } else if (slot == 23) {
                            jugador.closeInventory();
                            GameEdit p = new GameEdit(jugador, partida.getMatch());
                            p.setStep("lives");
                            plugin.setPartidaEditando(p);
                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aWrite a number. This will be the amount of starting lives for each team."));
                        }
                    }

                }
            }
        }
    }

    @EventHandler
    public void capturarChat(AsyncPlayerChatEvent event) {
        final GameEdit partida = plugin.getPartidaEditando();
        final Player jugador = event.getPlayer();
        String message = ChatColor.stripColor(event.getMessage());
        if (partida != null && partida.getPlayer().getName().equals(jugador.getName())) {
            event.setCancelled(true);
            YamlDocument messages = plugin.getMessagesDocument();
            String prefix = ChatColor.translateAlternateColorCodes('&', messages.getString("prefix")) + " ";
            String paso = partida.getStep();
            if (paso.equals("min")) {
                try {
                    int num = Integer.valueOf(message);
                    if (num >= 2 && num % 2 == 0) {
                        jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("minPlayersDefined").replace("%name%", partida.getMatch().getNombre())));
                        partida.getMatch().setCantidadMinimaJugadores(num);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                InventarioAdmin.crearInventario(jugador, partida.getMatch(), plugin);
                            }
                        }, 3L);
                    } else {
                        jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("validNumberError")));
                    }
                } catch (NumberFormatException e) {
                    jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("validNumberError")));
                }
            } else if (paso.equals("max")) {
                try {
                    int num = Integer.valueOf(message);
                    if (num >= 2 && num % 2 == 0) {
                        jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("maxPlayersDefined").replace("%name%", partida.getMatch().getNombre())));
                        partida.getMatch().setCantidadMaximaJugadores(num);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                InventarioAdmin.crearInventario(jugador, partida.getMatch(), plugin);
                            }
                        }, 3L);
                    } else {
                        jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("validNumberError")));
                    }
                } catch (NumberFormatException e) {
                    jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("validNumberError")));
                }
            } else if (paso.equals("team1name")) {

                if (config.contains("teams." + message) || message.equalsIgnoreCase("random")) {
                    jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("typeDefined").replace("%number%", "1").replace("%name%", partida.getMatch().getNombre())));
                    partida.getMatch().getTeam1().setTipo(message);
                    if (message.equalsIgnoreCase("random")) {
                        partida.getMatch().getTeam1().setRandom(true);
                    } else {
                        partida.getMatch().getTeam1().setRandom(false);
                    }
                    partida.getMatch().modificarTeams(config);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            InventarioAdmin.crearInventario(jugador, partida.getMatch(), plugin);
                        }
                    }, 3L);
                } else {
                    jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', "&cThat team name doesn't exists."));
                }
            } else if (paso.equals("team2name")) {
                if (config.contains("teams." + message) || message.equalsIgnoreCase("random")) {
                    jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("typeDefined").replace("%number%", "2").replace("%name%", partida.getMatch().getNombre())));
                    partida.getMatch().getTeam2().setTipo(message);
                    if (message.equalsIgnoreCase("random")) {
                        partida.getMatch().getTeam2().setRandom(true);
                    } else {
                        partida.getMatch().getTeam2().setRandom(false);
                    }
                    partida.getMatch().modificarTeams(config);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            InventarioAdmin.crearInventario(jugador, partida.getMatch(), plugin);
                        }
                    }, 3L);
                } else {
                    jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', "&cThat team name doesn't exists."));
                }
            } else if (paso.equals("time")) {
                try {
                    int num = Integer.valueOf(message);
                    if (num > 0) {
                        jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("timeDefined").replace("%name%", partida.getMatch().getNombre())));
                        partida.getMatch().setTiempoMaximo(num);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                InventarioAdmin.crearInventario(jugador, partida.getMatch(), plugin);
                            }
                        }, 3L);
                    } else {
                        jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("validNumberError")));
                    }
                } catch (NumberFormatException e) {
                    jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("validNumberError")));
                }
            } else if (paso.equals("lives")) {
                try {
                    int num = Integer.valueOf(message);
                    if (num > 0) {
                        jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("livesDefined").replace("%name%", partida.getMatch().getNombre())));
                        partida.getMatch().setVidasIniciales(num);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                InventarioAdmin.crearInventario(jugador, partida.getMatch(), plugin);
                            }
                        }, 3L);
                    } else {
                        jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("validNumberError")));
                    }
                } catch (NumberFormatException e) {
                    jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("validNumberError")));
                }
            }
        }
    }
}
