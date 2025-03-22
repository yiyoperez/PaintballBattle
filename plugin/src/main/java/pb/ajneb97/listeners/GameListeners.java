package pb.ajneb97.listeners;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import pb.ajneb97.commons.cache.Cache;
import pb.ajneb97.listeners.customevents.GameLeaveEvent;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.managers.controller.GameController;
import pb.ajneb97.structures.Team;
import pb.ajneb97.structures.game.GameItem;
import pb.ajneb97.utils.enums.GameState;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

import java.util.List;

public class GameListeners implements Listener {

    @Inject
    @Named("config")
    private YamlDocument config;

    @Inject
    private GameManager gameManager;
    @Inject
    private GameController gameController;

    @Inject
    @Named("item-cache")
    private Cache<String, GameItem> itemCache;

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!gameManager.isPlaying(player)) return;

        gameManager.getPlayerGame(player)
                .ifPresent(game -> new GameLeaveEvent(game, player).call());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (gameManager.isPlaying(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (gameManager.isPlaying(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (gameManager.isPlaying(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        if (gameManager.isPlaying(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (gameManager.isPlaying(player)) {
            if (event.getCause() == EntityDamageEvent.DamageCause.KILL) return;

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemSwap(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (gameManager.isPlaying(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCommandUsage(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().toLowerCase();
        Player player = event.getPlayer();

        if (!gameManager.isPlaying(player)) return;
        if (player.hasPermission("paintball.admin") || player.hasPermission("paintball.bypass.commands")) return;

        List<String> allowedCommands = config.getStringList("GAME.COMMAND_WHITELIST");
        for (String s : allowedCommands) {
            if (command.toLowerCase().startsWith(s)) {
                return;
            }
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onCrafting(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) return;
        if (!event.getClickedInventory().getType().equals(InventoryType.CRAFTING)) return;
        if (event.getSlot() != 0 || !event.getSlotType().equals(InventoryType.SlotType.RESULT)) return;

        Player player = (Player) event.getWhoClicked();
        if (gameManager.isPlaying(player.getPlayer())) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onTrampling(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (event.getAction() != Action.PHYSICAL) return;

        Player player = event.getPlayer();
        if (!gameManager.isPlaying(player)) return;

        if (event.getClickedBlock().getType() == Material.FARMLAND) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onVoidFall(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (!gameManager.isPlaying(player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.VOID) return;

        gameManager.getPlayerGame(player).ifPresent(game -> {
            Team team = gameManager.getPlayerTeam(player);
            event.setCancelled(true);
            player.teleport(team.getSpawnLocation());
        });
    }

    @EventHandler
    public void onSnowballClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!gameManager.isPlaying(player)) return;

        if (event.getCurrentItem() == null) return;
        if (event.getInventory().getType() != InventoryType.PLAYER && event.getInventory().getType() != InventoryType.CRAFTING) {
            return;
        }

        ItemStack currentItem = event.getCurrentItem();
        if (currentItem.getType() == Material.AIR || currentItem.getType() == Material.SNOWBALL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSnowballHit(EntityDamageByEntityEvent event) {
        Entity damagetEntity = event.getDamager();
        if (!(damagetEntity instanceof Snowball projectile)) return;

        if (projectile.getShooter() == null) return;

        Entity victimEntity = event.getEntity();
        if (!(victimEntity instanceof Player victim)) return;

        ProjectileSource shooter = projectile.getShooter();
        if (!(shooter instanceof Player killer)) return;

        gameManager.getPlayerGame(killer).ifPresent(game -> {
            if (game.getState() == GameState.PLAYING) {
                event.setCancelled(true);
                gameController.handlePlayerDeath(game, victim, killer);
            }
        });
    }

    @EventHandler
    public void onGameItemClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!gameManager.isPlaying(player)) return;

        if (event.getItem() == null) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;


        gameManager.getPlayerGame(player).ifPresent(game -> {
            ItemStack item = event.getItem();

            itemCache.get().forEach((name, gameItem) -> {
                if (!item.isSimilar(gameItem.getItem())) return;

                event.setCancelled(true);

                switch (name) {
                    case "LEAVE" -> gameController.handlePlayerQuit(player, game);
                    //TODO
                    case "HATS" -> {
                        player.sendMessage("Not implemented yet.");
                    }
                    case "PERKS" -> {
                        player.sendMessage("Not implemented yet.");
                    }
                    case "PLAY_AGAIN" -> gameManager.attemptRandomJoin(player);
                    case "TEAM_SELECTOR" -> {
                        player.sendMessage("Not implemented yet.");
                    }
                }
            });
        });
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) return;
        if (!config.getBoolean("ARENA_SETTINGS.CHAT.ENABLED")) return;

        Player player = event.getPlayer();
        if (!gameManager.isPlaying(player)) return;

//        Game partida = gameManager.getPlayerGame(player.getName());
//        if (partida != null) {
//            String message = event.getParsedMessage();
//            event.setCancelled(true);
//            ArrayList<PaintballPlayer> jugadores = partida.getPlayers();
//            if (partida.hasStarted()) {
//                String teamName = config.getString("teams." + partida.getPlayerTeam(player.getName()).getName() + ".name");
//                for (PaintballPlayer j : jugadores) {
//                    String msg = MessageUtils.translateColor(config.getString("arena_chat_format").replace("%player%", player.getName()).replace("%team%", teamName));
//                    if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
//                        msg = PlaceholderAPI.setPlaceholders(j.getPlayer(), msg.replace("%message%", message));
//                    } else {
//                        msg = msg.replace("%message%", message);
//                    }
//                    j.getPlayer().sendMessage(msg);
//                }
//            } else {
//                for (PaintballPlayer j : jugadores) {
//                    String msg = MessageUtils.translateColor(config.getString("arena_chat_format").replace("%player%", player.getName()).replace("%team%", messages.getString("teamInformationNone")));
//                    if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
//                        msg = PlaceholderAPI.setPlaceholders(j.getPlayer(), msg.replace("%message%", message));
//                    } else {
//                        msg = msg.replace("%message%", message);
//                    }
//                    j.getPlayer().sendMessage(msg);
//                }
//            }
//        } else {
//            for (Player p : Bukkit.getOnlinePlayers()) {
//                if (gameManager.getPlayerGame(p.getName()) != null) {
//                    event.getRecipients().remove(p);
//                }
//            }
//        }
    }

    //    @EventHandler
//    public void preLanzarSnowball(PlayerInteractEvent event) {
//        Player jugador = event.getPlayer();
//        ItemStack item = event.getItem();
//        if (event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
//            if (item != null && (item.getType().name().contains("SNOW") || item.getType().name().contains("EGG"))) {
//                Game partida = gameManager.getPlayerGame(jugador.getName());
//                if (partida != null) {
//                    event.setCancelled(true);
//                    PaintballPlayer player = partida.getJugador(jugador.getName());
//
//                    if (player.getKillstreak("fury") == null) {
//                        if (Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15")
//                                || Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17") || Bukkit.getVersion().contains("1.18")
//                                || Bukkit.getVersion().contains("1.19") || Bukkit.getVersion().contains("1.20")) {
//                            if (player.getSelectedHat().equals("chicken_hat")) {
//                                jugador.getInventory().removeItem(new ItemStack(Material.EGG, 1));
//                            } else {
//                                jugador.getInventory().removeItem(new ItemStack(Material.SNOWBALL, 1));
//                            }
//                        } else {
//                            if (player.getSelectedHat().equals("chicken_hat")) {
//                                jugador.getInventory().removeItem(new ItemStack(Material.EGG, 1));
//                            } else {
//                                jugador.getInventory().removeItem(new ItemStack(Material.valueOf("SNOW_BALL"), 1));
//                            }
//                        }
//                    }
//
//                    String[] separados = config.getString("snowballShootSound").split(";");
//                    try {
//                        Sound sound = Sound.valueOf(separados[0]);
//                        jugador.playSound(jugador.getLocation(), sound, Float.valueOf(separados[1]), Float.valueOf(separados[2]));
//                    } catch (Exception ex) {
//                        Bukkit.getConsoleSender().sendMessage(MessageUtils.translateColor(PaintballBattle.prefix + "&7Sound Name: &c" + separados[0] + " &7is not valid."));
//                    }
//
//                    if (player.getSelectedHat().equals("chicken_hat")) {
//                        jugador.launchProjectile(Egg.class, jugador.getLocation().getDirection());
//                    } else {
//                        jugador.launchProjectile(Snowball.class, jugador.getLocation().getDirection());
//                    }
//
//
//                    Killstreak k = player.getKillstreak("triple_shoot");
//                    if (k != null) {
//                        Vector direccion = jugador.getLocation().getDirection().clone();
//                        double anguloEntre = Math.toRadians(10);
//
//                        double xFinal = direccion.getX() * Math.cos(anguloEntre) - direccion.getZ() * Math.sin(anguloEntre);
//                        double zFinal = direccion.getX() * Math.sin(anguloEntre) + direccion.getZ() * Math.cos(anguloEntre);
//
//
//                        direccion = new Vector(xFinal, direccion.getY(), zFinal);
//
//                        if (player.getSelectedHat().equals("chicken_hat")) {
//                            jugador.launchProjectile(Egg.class, direccion);
//                        } else {
//                            jugador.launchProjectile(Snowball.class, direccion);
//                        }
//
//                        direccion = jugador.getLocation().getDirection().clone();
//
//                        anguloEntre = Math.toRadians(-10);
//
//                        xFinal = direccion.getX() * Math.cos(anguloEntre) - direccion.getZ() * Math.sin(anguloEntre);
//                        zFinal = direccion.getX() * Math.sin(anguloEntre) + direccion.getZ() * Math.cos(anguloEntre);
//
//
//                        direccion = new Vector(xFinal, direccion.getY(), zFinal);
//
//                        if (player.getSelectedHat().equals("chicken_hat")) {
//                            jugador.launchProjectile(Egg.class, direccion);
//                        } else {
//                            jugador.launchProjectile(Snowball.class, direccion);
//                        }
//                    }
//                }
//            }
//        }
//
//    }
//
//    @EventHandler
//    public void lanzarSnowball(ProjectileLaunchEvent event) {
//        Projectile p = event.getEntity();
//        ProjectileSource source = p.getShooter();
//        if ((p.getType().equals(EntityType.SNOWBALL) || p.getType().equals(EntityType.EGG)) && source instanceof Player) {
//            Player jugador = (Player) source;
//            Game partida = gameManager.getPlayerGame(jugador.getName());
//            if (partida != null) {
//                p.setMetadata("PaintballBattle", new FixedMetadataValue(plugin, "proyectil"));
//                p.setVelocity(p.getVelocity().multiply(1.25));
//                PaintballPlayer player = partida.getJugador(jugador.getName());
//                Killstreak k = player.getKillstreak("strong_arm");
//                if (k != null) {
//                    p.setVelocity(p.getVelocity().multiply(2));
//                }
//                String particle = config.getString("snowball_particle");
//                if (!particle.equals("none")) {
//                    if (!player.getSelectedHat().equals("chicken_hat")) {
//                        CooldownSnowballParticle c = new CooldownSnowballParticle(plugin, p, particle);
//                        c.cooldown();
//                    }
//                }
//            }
//        }
//    }

    // KINDA POST GAME LISTENER
    @EventHandler
    public void clickearItemPlayAgain(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!gameManager.isPlaying(player)) return;

        if (event.getItem() == null) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

//        ItemStack item = UtilidadesItems.crearItem(config, "play_again_item");
//        if (event.getItem().isSimilar(item)) {
//            event.setCancelled(true);
//            Game partidaNueva = PartidaManager.getAvailableGame(plugin);
//            if (partidaNueva == null) {
//                player.sendMessage(MessageUtils.translateColor(messages.getString("noArenasAvailable")));
//            } else {
//                PartidaManager.jugadorSale(partida, player, true, plugin, false);
//                PartidaManager.jugadorEntra(partidaNueva, player, plugin);
//            }
//        }
    }
}
