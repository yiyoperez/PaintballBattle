package pb.ajneb97.managers;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.core.utils.message.MessageUtils;
import pb.ajneb97.structures.Game;
import pb.ajneb97.structures.GameEdit;
import pb.ajneb97.utils.LocationUtils;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static pb.ajneb97.utils.enums.EditStep.STARTING_LIVES;
import static pb.ajneb97.utils.enums.EditStep.TIME;
import static pb.ajneb97.utils.enums.EditStep.UNKNOWN;

//TODO NEEDS MENU
public class EditManager {

    @Inject
    private PaintballBattle plugin;
    @Inject
    @Named("messages")
    private YamlDocument messages;

    private final Map<UUID, GameEdit> gameEditMap = new HashMap<>();

    public boolean isArenaAvailable(Game arena) {
        return gameEditMap.values()
                .stream()
                .map(GameEdit::getArena)
                .map(Game::getName)
                .anyMatch(a -> a.equalsIgnoreCase(arena.getName()));
    }

    public void openEditInventory(Player admin) {
        GameEdit edit = getGameEdit(admin);
        Game arena = edit.getArena();

        Gui menu = Gui.gui().title(Component.text("&2Editing Arena: &7" + arena.getName())).rows(5).create();

        // Populate items.
        addLobbyItem(menu, edit);

        addTeam1SpawnItem(menu, edit);
        addTeam2SpawnItem(menu, edit);

        addMinPlayersItems(menu, edit);
        addMaxPlayersItems(menu, edit);

        addTeam1ColorItems(menu, edit);
        addTeam2ColorItems(menu, edit);

        addMaxTimeItem(menu, edit);
        addLivesItem(menu, edit);

        // Close action handle.
        menu.setCloseGuiAction(event -> {
            Player player = (Player) event.getPlayer();
            if (edit.getStep() == UNKNOWN) {
                remove(player);
                player.sendMessage("Ya no estas editando la arena.");
            }
        });

        // Open inventory to player.
        menu.open(admin);
    }

    private void addMenuItem(Gui menu, int slot, Material material, String name, List<String> lore, Consumer<InventoryClickEvent> clickAction) {
        GuiItem item = ItemBuilder.from(material)
                .name(Component.text(name))
                .setLore(lore)
                .asGuiItem();

        item.setAction(event -> {
            ItemStack stack = event.getCurrentItem();
            if (stack != null && stack.isSimilar(item.getItemStack())) {
                clickAction.accept(event);
                event.setCancelled(true);
            }
        });

        menu.setItem(slot, item);
    }

    private void anvilGUI(Player player, String title, String text, BiFunction<Integer, AnvilGUI.StateSnapshot, List<AnvilGUI.ResponseAction>> clickHandler) {
        new AnvilGUI.Builder()
                .plugin(plugin)
                .title(title)
                .text(text)
                .onClick(clickHandler)
                .onClose(stateSnapshot -> {
                    getGameEdit(player).setStep(UNKNOWN);
                    player.closeInventory();
                    Bukkit.getScheduler().runTaskLater(plugin, () -> openEditInventory(stateSnapshot.getPlayer()), 10L);
                })
                .open(player);
    }

    private void addLobbyItem(Gui menu, GameEdit edit) {
        Game arena = edit.getArena();

        addMenuItem(menu, 10, Material.BEACON, "&6&lSet Lobby",
                List.of("&7Click to define the arena Lobby in your",
                        "&7current position.",
                        "",
                        "Current location: " + (arena.hasLobby() ? "Tiene lobby" : "NONE")),
                event -> {
                    Player player = (Player) event.getWhoClicked();
                    arena.setLobby(player.getLocation());
                    player.sendMessage(MessageUtils.translateColor(messages.getString("lobbyDefined").replace("%name%", arena.getName())));
                });
    }

    private void addTeam1SpawnItem(Gui menu, GameEdit edit) {
        Game game = edit.getArena();
        addMenuItem(menu, 11, Material.QUARTZ_BLOCK,
                "&6&lSet Team 1 Spawn",
                List.of("&7Click to define the arena team 1 Spawn",
                        "&7at your current position.",
                        "",
                        "&9Current Position: ",
                        game.getPointOne() != null ? LocationUtils.serialize(game.getPointOne()) : "NONE"),
                event -> {
                    Player player = (Player) event.getWhoClicked();
                    game.setPointOne(player.getLocation());
                    player.sendMessage("Set team 1 spawn location.");
                    //jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("spawnTeamDefined").replace("%number%", "1").replace("%name%", partida.getArena().getNombre())));
                });
    }

    private void addTeam2SpawnItem(Gui menu, GameEdit edit) {
        Game game = edit.getArena();
        addMenuItem(menu, 12, Material.QUARTZ_BLOCK,
                "&6&lSet Team 2 Spawn",
                List.of("&7Click to define the arena team 1 Spawn",
                        "&7at your current position.",
                        "",
                        "&9Current Position: ",
                        game.getPointTwo() != null ? LocationUtils.serialize(game.getPointTwo()) : "NONE"),
                event -> {
                    Player player = (Player) event.getWhoClicked();
                    game.setPointTwo(player.getLocation());
                    player.sendMessage("Set team 2 spawn location.");
                    //jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("spawnTeamDefined").replace("%number%", "2").replace("%name%", partida.getArena().getNombre())));
                });
    }

    // TODO
    private void addMinPlayersItems(Gui menu, GameEdit edit) {
        Game arena = edit.getArena();

        addMenuItem(menu, 13, Material.GHAST_TEAR,
                "&6&lSet Min Players",
                List.of("&7Click to define the arena minimum number",
                        "&7of players.",
                        "",
                        "&9Current value: " + arena.getMinPlayers())
                , event -> {
                    int currentValue = arena.getMinPlayers();

                    if (event.isLeftClick()) {
                        currentValue += 1.0;
                    }
                    if (event.isRightClick()) {
                        if (currentValue > 1) {
                            currentValue -= 1.0;
                        }
                    }

                    arena.setMinPlayers(currentValue);
                    event.getWhoClicked().sendMessage(MessageUtils.translateColor(messages.getString("minPlayersDefined").replace("%name%", arena.getName())));
                    menu.update();
                }
        );
        GuiItem item = ItemBuilder.from(Material.GHAST_TEAR)
                .setName("&6&lSet Min Players")
                .setLore("&7Click to define the arena minimum number",
                        "&7of players.",
                        "",
                        "&9Current value: " + arena.getMinPlayers())
                .asGuiItem();

        item.setAction(event -> {
            event.setCancelled(true);
            int currentValue = arena.getMinPlayers();

            if (event.isLeftClick()) {
                currentValue += 1.0;
            }
            if (event.isRightClick()) {
                if (currentValue > 1) {
                    currentValue -= 1.0;
                }
            }

            arena.setMinPlayers(currentValue);
            event.getWhoClicked().sendMessage(MessageUtils.translateColor(messages.getString("minPlayersDefined").replace("%name%", arena.getName())));
            menu.update();
        });

        menu.setItem(13, item);
    }

    // TODO
    private void addMaxPlayersItems(Gui menu, GameEdit edit) {
        Game arena = edit.getArena();
        GuiItem item = ItemBuilder.from(Material.GHAST_TEAR)
                .setName("&6&lSet Max Players")
                .setLore("&7Click to define the arena maximum number",
                        "&7of players.",
                        "",
                        "&9Current value: " + arena.getMaxPlayers())
                .asGuiItem();

        item.setAction(event -> {
            event.setCancelled(true);
            int currentValue = arena.getMaxPlayers();

            if (event.isLeftClick()) {
                currentValue += 1.0;
            }
            if (event.isRightClick()) {
                if (currentValue > 0) {
                    currentValue -= 1.0;
                }
            }

            arena.setMaxPlayers(currentValue);
            event.getWhoClicked().sendMessage(MessageUtils.translateColor(messages.getString("maxPlayersDefined").replace("%name%", arena.getName())));
            menu.update();
        });

        menu.setItem(14, item);
    }

    // TODO
    private void addTeam1ColorItems(Gui menu, GameEdit edit) {
        Game arena = edit.getArena();
        GuiItem item = ItemBuilder.from(Material.NAME_TAG)
                .setName("&6&lSet Team 1 Color")
                .setLore("&7Click to define the arena team 1 Color.",
                        "",
                        "&9Current value: &7",
                        arena.getFirstTeam() == null ?
                                "NOT SET" :
                                arena.getFirstTeam().getName())
                .asGuiItem(event -> {
                    Player player = (Player) event.getWhoClicked();
                    event.setCancelled(true);

                    player.sendMessage("Needs to open a menu selector will all the teams available.");
                });

        menu.setItem(15, item);

//        String lista = "";
//        for (String key : config.getSection("teams").getRoutesAsStrings(false)) {
//            lista = lista + key + " ";
//        }
//        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aWrite one of these team names: &7random " + lista));

//        if (config.contains("teams." + message) || message.equalsIgnoreCase("random")) {
//            jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("typeDefined").replace("%number%", "1").replace("%name%", partida.getArena().getNombre())));
//            partida.getArena().getTeam1().setTeamName(message);
//            if (message.equalsIgnoreCase("random")) {
//                partida.getArena().getTeam1().setRandom(true);
//            } else {
//                partida.getArena().getTeam1().setRandom(false);
//            }
//            partida.getArena().modificarTeams(config);
//            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
//                @Override
//                public void run() {
//                    InventarioAdmin.crearInventario(jugador, partida.getArena(), plugin);
//                }
//            }, 3L);
//        } else {
//            jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', "&cThat team name doesn't exists."));
//        }
    }

    // TODO
    private void addTeam2ColorItems(Gui menu, GameEdit edit) {
        Game arena = edit.getArena();
        GuiItem item = ItemBuilder.from(Material.NAME_TAG)
                .setName("&6&lSet Team 2 Color")
                .setLore("&7Click to define the arena team 2 Color.",
                        "",
                        "&9Current value: &7",
                        arena.getSecondTeam() == null ?
                                "NOT SET" : arena.getSecondTeam().getName())
                .asGuiItem(event -> {
                    Player player = (Player) event.getWhoClicked();
                    event.setCancelled(true);

                    player.sendMessage("Needs to open a menu selector will all the teams available.");
                });

        menu.setItem(16, item);

//        if (config.contains("teams." + message) || message.equalsIgnoreCase("random")) {
//            jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("typeDefined").replace("%number%", "2").replace("%name%", partida.getArena().getNombre())));
//            partida.getArena().getTeam2().setTeamName(message);
//            if (message.equalsIgnoreCase("random")) {
//                partida.getArena().getTeam2().setRandom(true);
//            } else {
//                partida.getArena().getTeam2().setRandom(false);
//            }
//            partida.getArena().modificarTeams(config);
//            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
//                @Override
//                public void run() {
//                    InventarioAdmin.crearInventario(jugador, partida.getArena(), plugin);
//                }
//            }, 3L);
//        } else {
//            jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', "&cThat team name doesn't exists."));
//        }
    }

    private void addMaxTimeItem(Gui menu, GameEdit edit) {
        Game arena = edit.getArena();
        addMenuItem(menu,
                21,
                Material.CLOCK,
                "&6&lSet Max Time",
                List.of("&7Click to define the arena time in seconds.",
                        "",
                        "&9Current Time: &7" + arena.getMaxTime()),
                (event) -> {
                    Player player = (Player) event.getWhoClicked();
                    edit.setStep(TIME);
                    //jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aWrite a number. This will be the arena time in seconds."));
                    anvilGUI(player,
                            "Time",
                            "Change arena duration",
                            (slot, stateSnapshot) -> {
                                if (slot != AnvilGUI.Slot.OUTPUT) return Collections.emptyList();

                                int value;
                                try {
                                    value = Integer.parseInt(stateSnapshot.getText());
                                } catch (NumberFormatException e) {
                                    return List.of(AnvilGUI.ResponseAction.replaceInputText("Try again, value is not a number."));
                                }

                                return Arrays.asList(
                                        AnvilGUI.ResponseAction.close(),
                                        AnvilGUI.ResponseAction.run(() -> {
                                            arena.setMaxTime(value);
                                            stateSnapshot.getPlayer().sendMessage("Max time is now " + value);
                                        })
                                );
                            });
                }
        );

//        try {
//            int num = Integer.valueOf(message);
//            if (num > 0) {
//                jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("timeDefined").replace("%name%", partida.getArena().getNombre())));
//                partida.getArena().setTiempoMaximo(num);
//                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
//                    @Override
//                    public void run() {
//                        InventarioAdmin.crearInventario(jugador, partida.getArena(), plugin);
//                    }
//                }, 3L);
//            } else {
//                jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("validNumberError")));
//            }
//        } catch (NumberFormatException e) {
//            jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("validNumberError")));
//        }
    }

    private void addLivesItem(Gui menu, GameEdit edit) {
        Game arena = edit.getArena();

        addMenuItem(menu, 23, Material.REDSTONE_BLOCK, "&6&lSet Starting Lives",
                List.of("&7Click to define the starting amount of lives",
                        "&7for both teams.",
                        "",
                        "&9Current Lobby: &7" + arena.getStartingLives()),
                (event) -> {
                    Player player = (Player) event.getWhoClicked();
                    getGameEdit(player).setStep(STARTING_LIVES);
                    //jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aWrite a number. This will be the amount of starting lives for each team."));

                    anvilGUI(player,
                            "Starting Lives",
                            "Change starting lives",
                            (slot, stateSnapshot) -> {
                                if (slot != AnvilGUI.Slot.OUTPUT) {
                                    return Collections.emptyList();
                                }

                                int value;
                                try {
                                    value = Integer.parseInt(stateSnapshot.getText());
                                } catch (NumberFormatException e) {
                                    return List.of(AnvilGUI.ResponseAction.replaceInputText("Try again, value is not a number."));
                                }

                                return Arrays.asList(
                                        AnvilGUI.ResponseAction.close(),
                                        AnvilGUI.ResponseAction.run(() -> {
                                            arena.setStartingLives(value);
                                            stateSnapshot.getPlayer().sendMessage("Starting lives count is now " + value);
                                        })
                                );
                            });

                }
        );


//    / /        try {
//    / /            int num = Integer.valueOf(message);
//    / /            if (num > 0) {
//    / /                jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("livesDefined").replace("%name%", partida.getArena().getNombre())));
//    / /                partida.getArena().setVidasIniciales(num);
//    / /                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
//    / /                    @Override
//    / /                    public void run() {
//    / /                        InventarioAdmin.crearInventario(jugador, partida.getArena(), plugin);
//    / /                    }
//    / /                }, 3L);
//    / /            } else {
//    / /                jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("validNumberError")));
//    / /            }
//    / /        } catch (NumberFormatException e) {
//    / /            jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("validNumberError")));
//    / /        }
    }

    private GameEdit getGameEdit(Player player) {
        return gameEditMap.get(player.getUniqueId());
    }

    public void add(Player player, Game partida) {
        gameEditMap.put(player.getUniqueId(), new GameEdit(player, partida));
    }

    public void remove(Player player) {
        gameEditMap.remove(player.getUniqueId());
    }

    public boolean isEditing(Player player) {
        return gameEditMap.containsKey(player.getUniqueId());
    }

    public boolean isArenaEditAvailable(Game arena) {
        return gameEditMap.values().stream().anyMatch(game -> game.getArena().getName().equalsIgnoreCase(arena.getName()));
    }

    public Map<UUID, GameEdit> getGameEditMap() {
        return gameEditMap;
    }
}
