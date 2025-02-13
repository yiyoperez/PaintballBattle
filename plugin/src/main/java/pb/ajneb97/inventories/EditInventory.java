package pb.ajneb97.inventories;

import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.core.utils.message.Placeholder;
import pb.ajneb97.inventories.util.BaseMenu;
import pb.ajneb97.managers.EditManager;
import pb.ajneb97.structures.game.Game;
import pb.ajneb97.structures.game.GameEdit;
import pb.ajneb97.utils.LocationUtils;
import pb.ajneb97.utils.enums.Messages;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static pb.ajneb97.utils.enums.EditStep.STARTING_LIVES;
import static pb.ajneb97.utils.enums.EditStep.TIME;
import static pb.ajneb97.utils.enums.EditStep.UNKNOWN;

public class EditInventory implements BaseMenu {

    private final PaintballBattle plugin;
    private final EditManager editManager;
    private final MessageHandler messageHandler;

    //TODO Set actual messages from Messages enum.

    public EditInventory(PaintballBattle plugin, EditManager editManager, MessageHandler messageHandler) {
        this.plugin = plugin;
        this.editManager = editManager;
        this.messageHandler = messageHandler;
    }

    @Override
    public void open(Player admin) {
        GameEdit edit = editManager.getGameEdit(admin);
        Game arena = edit.getArena();

        Gui menu = Gui.gui().title(messageHandler.getComponent(Messages.EDIT_MENU_TITLE, new Placeholder("%arena%", arena.getName()))).rows(5).create();

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

                editManager.remove(player);
                player.sendMessage("Ya no estas editando la arena.");
            }
        });

        // Open inventory to player.
        menu.open(admin);
    }

    private void addLobbyItem(Gui menu, GameEdit edit) {
        Game arena = edit.getArena();

        addMenuItem(menu, 10, Material.BEACON,
                messageHandler.getComponent(Messages.EDIT_MENU_SET_LOBBY_NAME),
                messageHandler.getComponentMessages(Messages.EDIT_MENU_SET_LOBBY_LORE, new Placeholder("%location%", arena.hasLobby() ? "tiene lobby" : "no tiene lobby")),
                event -> {
                    event.setCancelled(true);
                    Player player = (Player) event.getWhoClicked();
                    arena.setLobby(player.getLocation());
                    messageHandler.sendMessage(player, Messages.LOBBY_DEFINED, new Placeholder("%name%", arena.getName()));
                });
    }

    private void addTeam1SpawnItem(Gui menu, GameEdit edit) {
        Game game = edit.getArena();
        addMenuItem(menu, 11, Material.QUARTZ_BLOCK,
                Component.text("&6&lSet Team 1 Spawn"),
                List.of(Component.text("&7Click to define the arena team 1 Spawn"),
                        Component.text("&7at your current position."),
                        Component.text(""),
                        Component.text("&9Current Position: "),
                        Component.text(game.getPointOne() != null ? LocationUtils.serialize(game.getPointOne()) : "NONE")),
                event -> {
                    event.setCancelled(true);
                    Player player = (Player) event.getWhoClicked();
                    game.setPointOne(player.getLocation());
                    player.sendMessage("Set team 1 spawn location.");
                    //jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("spawnTeamDefined").replace("%number%", "1").replace("%name%", partida.getArena().getNombre())));
                });
    }

    private void addTeam2SpawnItem(Gui menu, GameEdit edit) {
        Game arena = edit.getArena();
        addMenuItem(menu, 12, Material.QUARTZ_BLOCK,
                Component.text("&6&lSet Team 2 Spawn"),
                List.of(Component.text("&7Click to define the arena team 1 Spawn"),
                        Component.text("&7at your current position."),
                        Component.text(""),
                        Component.text("&9Current Position: "),
                        Component.text(arena.getPointTwo() != null ? LocationUtils.serialize(arena.getPointTwo()) : "NONE")),
                event -> {
                    event.setCancelled(true);
                    Player player = (Player) event.getWhoClicked();
                    arena.setPointTwo(player.getLocation());
                    player.sendMessage("Set team 2 spawn location.");
                    //jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("spawnTeamDefined").replace("%number%", "2").replace("%name%", partida.getArena().getNombre())));
                });
    }

    private void addMinPlayersItems(Gui menu, GameEdit edit) {
        Game arena = edit.getArena();
        addMenuItem(menu, 13, Material.GHAST_TEAR,
                Component.text("&6&lSet Min Players"),
                List.of(Component.text("&7Click to define the arena minimum number"),
                        Component.text("&7of players."),
                        Component.text(""),
                        Component.text("&9Current value: " + arena.getMinPlayers()))
                , event -> {
                    event.setCancelled(true);
                    int currentValue = arena.getMinPlayers();

                    if (event.isLeftClick()) {
                        currentValue += 1;
                    }
                    if (event.isRightClick()) {
                        if (currentValue > 1) {
                            currentValue -= 1;
                        }
                    }

                    arena.setMinPlayers(currentValue);
                    messageHandler.sendManualMessage(event.getWhoClicked(), "minPlayersDefined", new Placeholder("%name%", arena.getName()));
                    menu.update();
                }
        );
    }

    private void addMaxPlayersItems(Gui menu, GameEdit edit) {
        Game arena = edit.getArena();

        addMenuItem(menu, 13, Material.GHAST_TEAR,
                Component.text("&6&lSet Min Players"),
                List.of(Component.text("&7Click to define the arena minimum number"),
                        Component.text("&7of players."),
                        Component.text(""),
                        Component.text("&9Current value: " + arena.getMinPlayers()))
                , event -> {
                    event.setCancelled(true);
                    int currentValue = arena.getMinPlayers();

                    if (event.isLeftClick()) {
                        currentValue += 1;
                    }
                    if (event.isRightClick()) {
                        if (currentValue > 1) {
                            currentValue -= 1;
                        }
                    }

                    arena.setMaxPlayers(currentValue);
                    messageHandler.sendManualMessage(event.getWhoClicked(), "maxPlayersDefined", new Placeholder("%name%", arena.getName()));
                    menu.update();
                });
    }

    private void addTeam1ColorItems(Gui menu, GameEdit edit) {
        Game arena = edit.getArena();
        addMenuItem(menu, 15, Material.NAME_TAG,
                Component.text("&6&lSet Team 1 Color"),
                List.of(Component.text("&7Click to define the arena team 1 Color."),
                        Component.text(""),
                        Component.text("&9Current value: &7"),
                        Component.text(arena.getSecondTeam() == null ? "NOT SET" : arena.getSecondTeam().getName()))
                , event -> {
                    Player player = (Player) event.getWhoClicked();
                    event.setCancelled(true);

                    player.sendMessage("Needs to open a menu selector will all the teams available.");
                }
        );
    }

    private void addTeam2ColorItems(Gui menu, GameEdit edit) {
        Game arena = edit.getArena();

        addMenuItem(menu, 16, Material.NAME_TAG,
                Component.text("&6&lSet Team 2 Color"),
                List.of(Component.text("&7Click to define the arena team 2 Color."),
                        Component.text(""),
                        Component.text("&9Current value: &7"),
                        Component.text(arena.getSecondTeam() == null ? "NOT SET" : arena.getSecondTeam().getName()))
                , event -> {
                    Player player = (Player) event.getWhoClicked();
                    event.setCancelled(true);

                    player.sendMessage("Needs to open a menu selector will all the teams available.");
                }
        );
    }

    private void addMaxTimeItem(Gui menu, GameEdit edit) {
        Game arena = edit.getArena();
        addMenuItem(menu,
                21,
                Material.CLOCK,
                Component.text("&6&lSet Max Time"),
                List.of(Component.text("&7Click to define the arena time in seconds."),
                        Component.text(""),
                        Component.text("&9Current Time: &7" + arena.getMaxTime())),
                (event) -> {
                    event.setCancelled(true);
                    edit.setStep(TIME);
                    //jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aWrite a number. This will be the arena time in seconds."));
                    new AnvilGUI.Builder().plugin(plugin)
                            .title("Time")
                            .text("Change arena duration")
                            .onClick(
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
    }

    private void addLivesItem(Gui menu, GameEdit edit) {
        Game arena = edit.getArena();

        addMenuItem(menu, 23, Material.REDSTONE_BLOCK,
                Component.text("&6&lSet Starting Lives"),
                List.of(Component.text("&7Click to define the starting amount of lives"),
                        Component.text("&7for both teams."),
                        Component.text(""),
                        Component.text("&9Current Lobby: &7" + arena.getStartingLives())),
                (event) -> {
                    event.setCancelled(true);

                    Player player = (Player) event.getWhoClicked();
                    editManager.getGameEdit(player).setStep(STARTING_LIVES);
                    //jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aWrite a number. This will be the amount of starting lives for each team."));

                    new AnvilGUI.Builder()
                            .plugin(plugin)
                            .title("Starting Lives")
                            .text("Change starting lives")
                            .onClick(
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
                                    })
                            .onClose(closeEvent -> {

                            });

                }
        );
    }
}
