package pb.ajneb97.inventories;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.commons.cache.Cache;
import pb.ajneb97.core.logger.Logger;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.core.utils.message.Placeholder;
import pb.ajneb97.inventories.util.BaseMenu;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.structures.game.GameEditSessionBuilder;
import pb.ajneb97.utils.LocationUtils;
import pb.ajneb97.utils.enums.Messages;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EditInventory implements BaseMenu {

    @Inject
    private PaintballBattle plugin;
    @Inject
    private GameManager gameManager;
    @Inject
    private MessageHandler messageHandler;

    //TODO Set actual messages from Messages enum.

    @Inject
    @Named("edit-session-cache")
    private Cache<UUID, GameEditSessionBuilder> editSession;

    @Override
    public void open(Player admin) {
        Optional<GameEditSessionBuilder> sessionBuilderOptional = editSession.find(admin.getUniqueId());
        if (sessionBuilderOptional.isEmpty()) {
            Logger.info("Session edit not found.");
            return;
        }

        GameEditSessionBuilder session = sessionBuilderOptional.get();
        Gui menu = Gui.gui().title(messageHandler.getComponent(Messages.EDIT_MENU_TITLE, new Placeholder("%arena%", session.getName()))).rows(5).create();

        // Populate items.
        addLobbyItem(menu, session);

        addTeam1SpawnItem(menu, session);
        addTeam2SpawnItem(menu, session);

        addMinPlayersItems(menu, session);
        addMaxPlayersItems(menu, session);

        addTeam1ColorItems(menu, session);
        addTeam2ColorItems(menu, session);

        addMaxTimeItem(menu, session);
        addLivesItem(menu, session);

        // Close action handle.
        menu.setCloseGuiAction(event -> {
            Player player = (Player) event.getPlayer();
            player.sendMessage("Ya no estas editando la arena.");
            gameManager.updateGame(session.getName(), session);
            editSession.remove(player.getUniqueId());
        });

        // Open inventory to player.
        menu.open(admin);
    }

    private void addLobbyItem(Gui menu, GameEditSessionBuilder game) {
        GuiItem item = createGuiItem(Material.BEACON,
                messageHandler.getComponent(Messages.EDIT_MENU_SET_LOBBY_NAME),
                messageHandler.getComponentMessages(Messages.EDIT_MENU_SET_LOBBY_LORE,
                        new Placeholder("%location%", game.getLobby() == null ? "tiene lobby" : "no tiene lobby")));

        addMenuItem(menu, item, 10,
                event -> {
                    event.setCancelled(true);
                    Player player = (Player) event.getWhoClicked();
                    game.setLobby(player.getLocation());
                    messageHandler.sendMessage(player, Messages.LOBBY_DEFINED, new Placeholder("%name%", game.getName()));
                    menu.open(player);
                });
    }

    private void addTeam1SpawnItem(Gui menu, GameEditSessionBuilder game) {
        GuiItem item = createGuiItem(Material.QUARTZ_BLOCK,
                messageHandler.getComponent(Messages.EDIT_MENU_SET_TEAM_SPAWN_NAME, new Placeholder("%number%", 1)),
                messageHandler.getComponentMessages(Messages.EDIT_MENU_SET_TEAM_SPAWN_LORE,
                        new Placeholder("%location%", game.getPointOne() != null ? LocationUtils.serialize(game.getPointOne()) : "NONE"))
        );

        addMenuItem(menu, item, 11,
                event -> {
                    event.setCancelled(true);
                    Player player = (Player) event.getWhoClicked();
                    game.setPointOne(player.getLocation());
                    messageHandler.sendMessage(player, Messages.SPAWN_TEAM_DEFINED, new Placeholder("%number%", 1), new Placeholder("%name%", game.getName()));

                    ItemStack itemstack = ItemBuilder.from(item.getItemStack()).lore(messageHandler.getComponentMessages(Messages.EDIT_MENU_SET_TEAM_SPAWN_LORE, new Placeholder("%location%", game.getPointOne() != null ? LocationUtils.serialize(game.getPointOne()) : "NONE"))).build();
                    item.setItemStack(itemstack);
                    menu.update();
                });
    }

    private void addTeam2SpawnItem(Gui menu, GameEditSessionBuilder game) {
        GuiItem item = createGuiItem(Material.QUARTZ_BLOCK,
                messageHandler.getComponent(Messages.EDIT_MENU_SET_TEAM_SPAWN_NAME, new Placeholder("%number%", 2)),
                messageHandler.getComponentMessages(Messages.EDIT_MENU_SET_TEAM_SPAWN_LORE, new Placeholder("%location%", game.getPointTwo() != null ? LocationUtils.serialize(game.getPointTwo()) : "NONE")));

        addMenuItem(menu, item, 12,
                event -> {
                    event.setCancelled(true);
                    Player player = (Player) event.getWhoClicked();
                    game.setPointTwo(player.getLocation());
                    messageHandler.sendMessage(player, Messages.SPAWN_TEAM_DEFINED, new Placeholder("%number%", 2), new Placeholder("%name%", game.getName()));
                });
    }

    private void addMinPlayersItems(Gui menu, GameEditSessionBuilder game) {

        GuiItem item = createGuiItem(Material.GHAST_TEAR,
                messageHandler.getComponent(Messages.EDIT_MENU_SET_MIN_PLAYERS_NAME),
                messageHandler.getComponentMessages(Messages.EDIT_MENU_SET_MIN_PLAYERS_LORE, new Placeholder("%min_players%", game.getMinPlayers())));

        addMenuItem(menu, item, 13
                , event -> {
                    event.setCancelled(true);
                    int currentValue = game.getMinPlayers();

                    if (event.isLeftClick()) {
                        currentValue += 1;
                    }
                    if (event.isRightClick()) {
                        if (currentValue > 1) {
                            currentValue -= 1;
                        }
                    }

                    game.setMinPlayers(currentValue);
                    messageHandler.sendMessage(event.getWhoClicked(), Messages.MIN_PLAYERS_DEFINED, new Placeholder("%min_players%", currentValue), new Placeholder("%name%", game.getName()));

                    ItemStack itemstack = ItemBuilder.from(item.getItemStack())
                            .lore(messageHandler.getComponentMessages(Messages.EDIT_MENU_SET_MIN_PLAYERS_LORE, new Placeholder("%min_players%", game.getMinPlayers()))).build();
                    item.setItemStack(itemstack);
                    menu.update();
                }
        );
    }

    private void addMaxPlayersItems(Gui menu, GameEditSessionBuilder game) {

        GuiItem item = createGuiItem(Material.GHAST_TEAR,
                messageHandler.getComponent(Messages.EDIT_MENU_SET_MAX_PLAYERS_NAME),
                messageHandler.getComponentMessages(Messages.EDIT_MENU_SET_MAX_PLAYERS_LORE, new Placeholder("%max_players%", game.getMaxPlayers())));

        addMenuItem(menu, item, 14,
                event -> {
                    event.setCancelled(true);
                    int currentValue = game.getMaxPlayers();

                    if (event.isLeftClick()) {
                        currentValue += 1;
                    }
                    if (event.isRightClick()) {
                        if (currentValue > 1) {
                            currentValue -= 1;
                        }
                    }

                    game.setMaxPlayers(currentValue);
                    messageHandler.sendMessage(event.getWhoClicked(), Messages.MAX_PLAYERS_DEFINED, new Placeholder("%min_players%", currentValue), new Placeholder("%name%", game.getName()));

                    ItemStack itemstack = ItemBuilder.from(item.getItemStack())
                            .lore(messageHandler.getComponentMessages(Messages.EDIT_MENU_SET_MAX_PLAYERS_LORE, new Placeholder("%max_players%", game.getMaxPlayers()))).build();
                    item.setItemStack(itemstack);
                    menu.update();
                });
    }

    private void addTeam1ColorItems(Gui menu, GameEditSessionBuilder game) {
        GuiItem item = createGuiItem(Material.NAME_TAG,
                Component.text("&6&lSet Team 1 Color"),
                List.of(Component.text("&7Click to define the arena team 1 Color."),
                        Component.text(""),
                        Component.text("&9Current value: &7"),
                        Component.text(game.getSecondTeam() == null ? "NOT SET" : game.getSecondTeam().getName())));

        addMenuItem(menu, item, 15,
                event -> {
                    Player player = (Player) event.getWhoClicked();
                    event.setCancelled(true);

                    player.sendMessage("Needs to open a menu selector will all the teams available.");
                }
        );
    }

    private void addTeam2ColorItems(Gui menu, GameEditSessionBuilder game) {
        GuiItem item = createGuiItem(Material.NAME_TAG,
                Component.text("&6&lSet Team 2 Color"),
                List.of(Component.text("&7Click to define the arena team 2 Color."),
                        Component.text(""),
                        Component.text("&9Current value: &7"),
                        Component.text(game.getSecondTeam() == null ? "NOT SET" : game.getSecondTeam().getName())));

        addMenuItem(menu, item, 16
                , event -> {
                    Player player = (Player) event.getWhoClicked();
                    event.setCancelled(true);

                    player.sendMessage("Needs to open a menu selector will all the teams available.");
                }
        );
    }

    private void addMaxTimeItem(Gui menu, GameEditSessionBuilder game) {
        GuiItem item = createGuiItem(Material.CLOCK,
                messageHandler.getComponent(Messages.EDIT_MENU_SET_MAX_TIME_NAME),
                messageHandler.getComponentMessages(Messages.EDIT_MENU_SET_MAX_TIME_LORE, new Placeholder("%max_time%", game.getMaxTime())));

        addMenuItem(menu, item, 21,
                (event) -> {
                    event.setCancelled(true);
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
                                                    game.setMaxTime(value);
                                                    stateSnapshot.getPlayer().sendMessage("Max time is now " + value);
                                                })
                                        );
                                    })
                            .onClose(closeEvent -> plugin.getServer().getScheduler().runTaskLater(plugin, () -> this.open((Player) event.getWhoClicked()), 5L))
                            .open((Player) event.getWhoClicked());
                }
        );
    }

    private void addLivesItem(Gui menu, GameEditSessionBuilder game) {
        GuiItem item = createGuiItem(Material.REDSTONE_BLOCK,
                messageHandler.getComponent(Messages.EDIT_MENU_SET_STARTING_LIVES_NAME),
                messageHandler.getComponentMessages(Messages.EDIT_MENU_SET_STARTING_LIVES_LORE, new Placeholder("%starting_lives%", game.getStartingLives())));

        addMenuItem(menu, item, 23,
                (event) -> {
                    event.setCancelled(true);

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
                                                    game.setStartingLives(value);
                                                    messageHandler.sendMessage(stateSnapshot.getPlayer(), Messages.LIVES_DEFINED,
                                                            new Placeholder("%value%", value),
                                                            new Placeholder("%name%", game.getName()));
                                                })
                                        );
                                    })
                            .onClose(closeEvent -> plugin.getServer().getScheduler().runTaskLater(plugin, () -> this.open((Player) event.getWhoClicked()), 5L))
                            .open((Player) event.getWhoClicked());

                }
        );
    }
}
