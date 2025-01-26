package pb.ajneb97.listeners;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import pb.ajneb97.managers.GameManager;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

public class PreGameListeners implements Listener {

    @Inject
    private GameManager gameManager;

    @Inject
    @Named("config")
    private YamlDocument config;

    //LEAVE ITEM
    @EventHandler
    public void clickearItemSalir(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!gameManager.isPlaying(player)) return;

        if (event.getItem() == null) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        gameManager.getPlayerGame(player)
                .ifPresent(game -> {
                    //TODO
//                        ItemStack item = UtilidadesItems.crearItem(config, "leave_item");
//                        if (event.getItem().isSimilar(item)) {
//                            event.setCancelled(true);
//                            gameHandler.handlePlayerQuit(player, game);
//                        }
                });
    }

    @EventHandler
    public void clickearItemHats(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!gameManager.isPlaying(player)) return;

        if (event.getItem() == null) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

//        ItemStack item = UtilidadesItems.crearItem(config, "hats_item");
//        if (event.getItem().isSimilar(item)) {
//            event.setCancelled(true);
//
//            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
//                @Override
//                public void run() {
//                    jugador.updateInventory();
//                    jugador.getEquipment().setHelmet(null);
//                    InventarioHats.crearInventario(jugador, plugin);
//                }
//            }, 2L);
//        }
    }

    @EventHandler
    public void clickearItemSelectorEquipo(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!gameManager.isPlaying(player)) return;

        if (event.getItem() == null) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;


        // Return if team selection is not available.
        if (!config.getBoolean("ARENA_SETTINGS.TEAM_SELECTION")) return;

//
//        ItemStack team1 = UtilidadesItems.crearItem(config, "teams." + partida.getFirstTeam().getName());
//        ItemMeta meta = team1.getItemMeta();
//        meta.setDisplayName(MessageUtils.translateColor(messages.getString("teamChoose").replace("%team%", config.getString("teams." + partida.getFirstTeam().getName() + ".name"))));
//        team1.setItemMeta(meta);
//        ItemStack team2 = UtilidadesItems.crearItem(config, "teams." + partida.getSecondTeam().getName());
//        meta = team2.getItemMeta();
//        meta.setDisplayName(MessageUtils.translateColor(messages.getString("teamChoose").replace("%team%", config.getString("teams." + partida.getSecondTeam().getName() + ".name"))));
//        team2.setItemMeta(meta);
//        //String prefix = MessageUtils.translateColor( messages.getString("prefix"))+" ";
//        if (event.getItem().isSimilar(team1)) {
//            event.setCancelled(true);
//            jugador.updateInventory();
//            PaintballPlayer j = partida.getJugador(jugador.getName());
//            if (j.getTeamChoice() != null && j.getTeamChoice().equals(partida.getFirstTeam().getName())) {
//                jugador.sendMessage(MessageUtils.translateColor(messages.getString("errorTeamAlreadySelected")));
//                return;
//            }
//            if (partida.canSelectTeam(partida.getFirstTeam().getName())) {
//                j.setTeamChoice(partida.getFirstTeam().getName());
//                jugador.sendMessage(MessageUtils.translateColor(messages.getString("teamSelected").replace("%team%", config.getString("teams." + partida.getFirstTeam().getName() + ".name"))));
//            } else {
//                jugador.sendMessage(MessageUtils.translateColor(messages.getString("errorTeamSelected")));
//            }
//
//        } else if (event.getItem().isSimilar(team2)) {
//            event.setCancelled(true);
//            jugador.updateInventory();
//            PaintballPlayer j = partida.getJugador(jugador.getName());
//            if (j.getTeamChoice() != null && j.getTeamChoice().equals(partida.getSecondTeam().getName())) {
//                jugador.sendMessage(MessageUtils.translateColor(messages.getString("errorTeamAlreadySelected")));
//                return;
//            }
//            if (partida.canSelectTeam(partida.getSecondTeam().getName())) {
//                j.setTeamChoice(partida.getSecondTeam().getName());
//                jugador.sendMessage(MessageUtils.translateColor(messages.getString("teamSelected").replace("%team%", config.getString("teams." + partida.getSecondTeam().getName() + ".name"))));
//            } else {
//                jugador.sendMessage(MessageUtils.translateColor(messages.getString("errorTeamSelected")));
//            }
//
//        }
    }
}
