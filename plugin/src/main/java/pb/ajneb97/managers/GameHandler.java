package pb.ajneb97.managers;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitScheduler;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.commons.cache.Cache;
import pb.ajneb97.core.logger.Logger;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.core.utils.message.MessageUtils;
import pb.ajneb97.structures.Game;
import pb.ajneb97.structures.GameItem;
import pb.ajneb97.structures.PaintballPlayer;
import pb.ajneb97.structures.Team;
import pb.ajneb97.tasks.PlayingGameTask;
import pb.ajneb97.tasks.StartingGameTask;
import pb.ajneb97.utils.LocationUtils;
import pb.ajneb97.utils.UtilidadesItems;
import pb.ajneb97.utils.UtilidadesOtros;
import pb.ajneb97.utils.enums.GameState;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class GameHandler {

    @Inject
    private PaintballBattle plugin;
    @Inject
    @Named("config")
    private YamlDocument config;
    @Inject
    @Named("messages")
    private YamlDocument messages;

    @Inject
    private GameManager gameManager;
    @Inject
    private MessageHandler messageHandler;

    @Inject
    @Named("item-cache")
    private Cache<String, GameItem> itemCache;
    @Inject
    @Named("player-cache")
    private Cache<UUID, PaintballPlayer> playerCache;

    //CREATE OWN CLASS TO MANAGE TASKS.
    private int waitingTaskID;
    private int playingTaskID;

    public void initStartingPhase(Game game) {
        if (config.getBoolean("BROADCAST.ENABLED")) {
            List<String> worlds = config.getStringList("BROADCAST.DENIED_WORLDS");
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (!worlds.contains(player.getWorld().getName())) {
                    player.sendMessage(MessageUtils.translateColor(messages.getString("arenaStartingBroadcast")
                            .replace("%arena%", game.getName())));
                }
            });
        }

        // Game already been set as starting.
        if (game.getState() == GameState.STARTING) return;

        game.setState(GameState.STARTING);

        int time = config.getInt("TIMES.STARTING");
        game.setStartingTime(System.currentTimeMillis() + time * 1000L);

        scheduleWaitingTask();
    }

    public void scheduleWaitingTask() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        if (!scheduler.isCurrentlyRunning(waitingTaskID)) {
            waitingTaskID = scheduler.scheduleSyncRepeatingTask(plugin,
                    new StartingGameTask(gameManager, this, messageHandler), 0L, 20L);
        }
    }

    public void stopWaitingTask() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        if (scheduler.isCurrentlyRunning(waitingTaskID)) {
            scheduler.cancelTask(waitingTaskID);
        }
    }

    public void initEndingPhase(Game game) {
        game.setState(GameState.ENDING);
        Team ganador = game.getWinner();

        String nameTeam1 = game.getFirstTeam().getName();
        String nameTeam2 = game.getSecondTeam().getName();

        String status = "";
        if (ganador == null) {
            //empate
            status = messages.getString("gameFinishedTieStatus");
        } else {
            status = messages.getString("gameFinishedWinnerStatus").replace("%winner_team%", ganador.getName());
        }

        // Initialize default top names and kills.
        String[] tops = {messages.getString("topKillsNone"), messages.getString("topKillsNone"), messages.getString("topKillsNone")};
        int[] topKills = {0, 0, 0};

//        List<PaintballPlayer> jugadoresKillsOrd = game.getPlayerKills();
//        // Fill the top arrays with available players.
//        for (int i = 0; i < Math.min(jugadoresKillsOrd.size(), 3); i++) {
//            tops[i] = jugadoresKillsOrd.get(i).getPlayer().getName();
//            topKills[i] = jugadoresKillsOrd.get(i).getKills();
//        }

        // Assign results to top variables.
        String top1 = tops[0], top2 = tops[1], top3 = tops[2];
        int top1Kills = topKills[0], top2Kills = topKills[1], top3Kills = topKills[2];

        List<String> msg = messages.getStringList("gameFinished");
        for (String s : msg) {
            String finalMessage = s
                    .replace("%status_message%", status)
                    .replace("%team1%", nameTeam1)
                    .replace("%team2%", nameTeam2)
                    .replace("%kills_team1%", game.getFirstTeam().getKills() + "")
                    .replace("%kills_team2%", game.getSecondTeam().getKills() + "")
                    .replace("%player1%", top1).replace("%player2%", top2)
                    .replace("%player3%", top3)
                    .replace("%kills_player1%", top1Kills + "")
                    .replace("%kills_player2%", top2Kills + "")
                    .replace("%kills_player3%", top3Kills + "")
                    .replace("%kills_player%", 0 /*TODO: Replace player kills*/ + "");

            game.notifyPlayers(MessageUtils.translateColor(finalMessage));
        }

        game.getCurrentPlayers().forEach(player -> {
            player.getInventory().clear();

            List<String> gameItemKeys = List.of("LEAVE", "PLAY_AGAIN");
            gameItemKeys.forEach(key ->
                    itemCache.find(key).ifPresent(gameItem -> {
                        if (gameItem.isEnabled()) {
                            player.getInventory().setItem(gameItem.getSlot(), gameItem.getItem());
                        }
                    })
            );
        });
//
//            if (config.getString("rewards_executed_after_teleport").equals("false")) {
//                if (ganador != null) {
//                    if (ganador.getName().equals(teamJugador.getName())) {
//                        List<String> commands = config.getStringList("winners_command_rewards");
//                        runRewardActions(commands, j);
//                    } else {
//                        List<String> commands = config.getStringList("losers_command_rewards");
//                        runRewardActions(commands, j);
//                    }
//                } else {
//                    List<String> commands = config.getStringList("tie_command_rewards");
//                    runRewardActions(commands, j);
//                }
//            }
//        }
//

        long endingTime = config.getLong("TIMES.ENDING");
        Bukkit.getScheduler().runTaskLater(plugin, () -> finishGame(game), 20L * endingTime);
    }

    public void startGame(Game game) {
        game.setState(GameState.PLAYING);

        game.setStartingTime(System.currentTimeMillis() + game.getMaxTime() * 1000L);

        game.setFirstTeam(new Team("red", 100));
        game.setSecondTeam(new Team("blue", 100));

        //TODO set player into its selected team.
        //if (config.getBoolean("ARENA_SETTINGS.TEAM_SELECTION")) {}
        game.sortPlayers();

        //darItems(partida, plugin.getConfigDocument(), plugin.getShopDocument(), messages);
        //teleportTeamSpawn(partida);
        //setVidas(partida, plugin.getShopDocument());

        //TODO: Improve sound thing.
        // startGameSound

        game.notifyPlayers(MessageUtils.translateColor(messages.getString("gameStarted")));
        game.getCurrentPlayers().forEach(player -> {
            player.sendMessage(MessageUtils.translateColor(messages.getString("teamInformation").replace("%team%", game.getPlayerTeam(player).getName())));
        });

        schedulePlayingTask();
    }

    public void schedulePlayingTask() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        if (!scheduler.isCurrentlyRunning(playingTaskID)) {
            Logger.info("Starting task");
            playingTaskID = scheduler.scheduleSyncRepeatingTask(plugin,
                    new PlayingGameTask(gameManager, this), 0L, 20L);
        }
    }

    public void stopPlayingTask() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        if (scheduler.isCurrentlyRunning(playingTaskID)) {
            Logger.info("Stopping task");
            scheduler.cancelTask(playingTaskID);
        }
    }

    public void finishGame(Game game) {
        //runRewardActions();
        game.setState(GameState.WAITING);

        for (Player player : game.getCurrentPlayers()) {
            handlePlayerQuit(player, game);
        }

        game.setFirstTeam(null);
        game.setSecondTeam(null);
        game.getCurrentPlayers().clear();
    }

    public void handlePlayerJoin(Player player, Game game) {
        playerCache.add(player.getUniqueId(), new PaintballPlayer(player));

        game.addPlayer(player);

        game.notifyPlayers(MessageUtils.translateColor(messages.getString("playerJoin").replace("%player%", player.getName())
                .replace("%current_players%", game.getCurrentPlayersSize() + "").replace("%max_players%", game.getMaxPlayers() + "")));

        player.getInventory().clear();
        player.getEquipment().clear();
        player.getEquipment().setArmorContents(null);
        player.updateInventory();

        player.setGameMode(GameMode.SURVIVAL);
        player.setExp(0);
        player.setLevel(0);
        player.setFoodLevel(20);
        player.setExhaustion(10);
        player.setHealth(20);
        player.setMaxHealth(20);
        player.setFlying(false);
        player.setAllowFlight(false);

        for (PotionEffect p : player.getActivePotionEffects()) {
            player.removePotionEffect(p.getType());
        }

        player.teleport(game.getLobby());

        List<String> gameItemKeys = List.of("LEAVE", "PERKS", "HATS", "TEAM_SELECTOR");
        gameItemKeys.forEach(key ->
                itemCache.find(key).ifPresent(gameItem -> {
                    if (gameItem.isEnabled()) {
                        player.getInventory().setItem(gameItem.getSlot(), gameItem.getItem());
                    }
                })
        );

        if (game.getCurrentPlayersSize() >= game.getMinPlayers()
                && game.getState().equals(GameState.WAITING)) {
            initStartingPhase(game);
        }
    }

    public void handlePlayerQuit(Player player, Game game) {
        game.notifyPlayers(MessageUtils.translateColor(messages.getString("playerLeave").replace("%player%", player.getName())
                .replace("%current_players%", game.getCurrentPlayersSize() + "").replace("%max_players%", game.getMaxPlayers() + "")));
        game.removePlayer(player);

        Location mainLobby = LocationUtils.deserialize(config.getString("MainLobby"));
        player.teleport(mainLobby);

        playerCache.find(player.getUniqueId())
                .ifPresent(paintballPlayer ->
                        paintballPlayer.getSavedElements().restorePlayerElements(player));
        player.updateInventory();

        //HANDLE GAME STATES
        GameState state = game.getState();
        if (state == GameState.STARTING) {
            if (game.getCurrentPlayersSize() < game.getMinPlayers()) {
                // Return since game is no longer in starting state.
                game.notifyPlayers(messageHandler.getMessage("gameStartingCancelled"));
                Logger.info("Cancelled start");
                game.setState(GameState.WAITING);
            }
            return;
        }

        if (state == GameState.PLAYING) {
            if (game.getCurrentPlayersSize() <= 1) {
                //Ending phase due to no players.
                initEndingPhase(game);
            }
        }
    }

    public void handlePlayerDeath(Game game, PaintballPlayer dead, PaintballPlayer killer) {
        if (dead.isRecentDeath()) return;

        if (dead.getSelectedHat().equals("guardian_hat") && dead.isEfectoHatActivado()) {
            return;
        }
        if (dead.getSelectedHat().equals("protector_hat")) {
            Random r = new Random();
            int num = r.nextInt(100);
            if (num >= 80) {
                return;
            }
        }

        Team targetTeam = game.getPlayerTeam(dead.getPlayer());
        Team teamAtacante = game.getPlayerTeam(killer.getPlayer());
        if (targetTeam.equals(teamAtacante)) {
            return;
        }

        //TODO: Make as config option.
//        if (lightning) {
//            dead.getPlayer().getWorld().strikeLightningEffect(dead.getPlayer().getLocation());
//        }


        dead.increaseDeaths();
        dead.setDeathLocation(dead.getPlayer().getLocation().clone());
        dead.getPlayer().sendMessage(MessageUtils.translateColor(messages.getString("killedBy").replace("%player%", killer.getPlayer().getName())));
        String[] separados = config.getString("killedBySound").split(";");
        try {
            Sound sound = Sound.valueOf(separados[0]);
            dead.getPlayer().playSound(dead.getPlayer().getLocation(), sound, Float.valueOf(separados[1]), Float.valueOf(separados[2]));
        } catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage(MessageUtils.translateColor("&7Sound Name: &c" + separados[0] + " &7is not valid."));
        }
        dead.setRecentDeath(true);
        dead.setLastKilledBy(killer.getPlayer().getName());
        targetTeam.decreaseLives(1);

        Team team = game.getPlayerTeam(dead.getPlayer());
/*
        if (dead.getSelectedHat().equals("explosive_hat")) {
            Random r = new Random();
            int num = r.nextInt(100);
            if (num >= 80) {
                if (Bukkit.getVersion().contains("1.8")) {
                    dead.getPlayer().getWorld().playEffect(dead.getPlayer().getLocation(), Effect.valueOf("EXPLOSION_LARGE"), 2);
                } else {
                    dead.getPlayer().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, dead.getPlayer().getLocation(), 2);
                }
                separados = config.getString("explosiveHatSound").split(";");
                try {
                    Sound sound = Sound.valueOf(separados[0]);
                    dead.getPlayer().getWorld().playSound(dead.getPlayer().getLocation(), sound, Float.valueOf(separados[1]), Float.valueOf(separados[2]));
                } catch (Exception ex) {
                    Bukkit.getConsoleSender().sendMessage(MessageUtils.translateColor(PaintballBattle.prefix + "&7Sound Name: &c" + separados[0] + " &7is not valid."));
                }
                Collection<Entity> entidades = dead.getPlayer().getWorld().getNearbyEntities(dead.getPlayer().getLocation(), 5, 5, 5);
                for (Entity e : entidades) {
                    if (e != null && e.getType().equals(EntityType.PLAYER)) {
                        Player player = (Player) e;
                        PaintballPlayer secondVictimPlayer = game.getJugador(player.getName());
                        if (secondVictimPlayer != null) {
                            PartidaManager.muereJugador(game, dead, secondVictimPlayer, plugin, false, false);
                        }
                    }
                }
            }
        }
*/
        dead.getPlayer().teleport(team.getSpawnLocation());

        /*if (Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15")
                || Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17") || Bukkit.getVersion().contains("1.18")
                || Bukkit.getVersion().contains("1.19") || Bukkit.getVersion().contains("1.20")) {
            if (dead.getSelectedHat().equals("chicken_hat")) {
                dead.getPlayer().getInventory().removeItem(new ItemStack(Material.EGG));
            } else {
                dead.getPlayer().getInventory().removeItem(new ItemStack(Material.SNOWBALL));
            }
        } else {
            if (dead.getSelectedHat().equals("chicken_hat")) {
                dead.getPlayer().getInventory().removeItem(new ItemStack(Material.EGG));
            } else {
                dead.getPlayer().getInventory().removeItem(new ItemStack(Material.valueOf("SNOW_BALL")));
            }
        }*/
        //PartidaManager.setBolasDeNieve(dead, config);

        killer.increaseKills();
        int cantidadCoinsGanados = UtilidadesOtros.coinsGanados(killer.getPlayer(), config);
//        int nivelExtraKillCoins = PaintballAPI.getPerkLevel(killer.getPlayer(), "extra_killcoins");
//        if (nivelExtraKillCoins != 0) {
//            String linea = plugin.getShopDocument().getStringList("perks_upgrades.extra_killcoins").get(nivelExtraKillCoins - 1);
//            String[] sep = linea.split(";");
//            int cantidad = Integer.valueOf(sep[0]);
//            cantidadCoinsGanados = cantidadCoinsGanados + cantidad;
//        }
        String lastKilledBy = killer.getLastKilledBy();
        if (lastKilledBy != null && lastKilledBy.equals(dead.getPlayer().getName())) {
            cantidadCoinsGanados = cantidadCoinsGanados + 1;
        }
        killer.increaseCoins(cantidadCoinsGanados);
        UtilidadesItems.crearItemKillstreaks(killer, config);

//        if (nuke) {
//            String equipoAtacanteName = config.getString("teams." + teamAtacante.getTeamName() + ".name");
//            String targetTeamName = config.getString("teams." + targetTeam.getTeamName() + ".name");
//            for (PaintballPlayer j : game.getPlayers()) {
//                if (!j.getPlayer().getName().equals(killer.getPlayer().getName())) {
//                    j.getPlayer().sendMessage(MessageUtils.translateColor(messages.getString("nukeKillMessage").replace("%team_player1%", targetTeamName)
//                            .replace("%player1%", dead.getPlayer().getName()).replace("%team_player2%", equipoAtacanteName)
//                            .replace("%player2%", killer.getPlayer().getName())));
//                }
//            }
//        }
        killer.getPlayer().sendMessage(MessageUtils.translateColor(messages.getString("kill").replace("%player%", dead.getPlayer().getName())));
//        if (!nuke) {
//            separados = config.getString("killSound").split(";");
//            try {
//                Sound sound = Sound.valueOf(separados[0]);
//                killer.getPlayer().playSound(killer.getPlayer().getLocation(), sound, Float.valueOf(separados[1]), Float.valueOf(separados[2]));
//            } catch (Exception ex) {
//                Bukkit.getConsoleSender().sendMessage(MessageUtils.translateColor(PaintballBattle.prefix + "&7Sound Name: &c" + separados[0] + " &7is not valid."));
//            }
//        }


        int snowballs = config.getInt("snowballs_per_kill");
        killer.getPlayer().getInventory().addItem(new ItemStack(Material.SNOWBALL, snowballs));
//        if (Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15")
//                || Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17") || Bukkit.getVersion().contains("1.18")
//                || Bukkit.getVersion().contains("1.19") || Bukkit.getVersion().contains("1.20")) {
//            if (killer.getSelectedHat().equals("chicken_hat")) {
//                killer.getPlayer().getInventory().addItem(new ItemStack(Material.EGG, snowballs));
//            } else {
//                killer.getPlayer().getInventory().addItem(new ItemStack(Material.SNOWBALL, snowballs));
//            }
//
//        } else {
//            if (killer.getSelectedHat().equals("chicken_hat")) {
//                killer.getPlayer().getInventory().addItem(new ItemStack(Material.EGG, snowballs));
//            } else {
//                killer.getPlayer().getInventory().addItem(new ItemStack(Material.valueOf("SNOW_BALL"), snowballs));
//            }
//
//        }

        if (targetTeam.getLives() <= 0) {
            //terminar partida
            initEndingPhase(game);
            return;
        }

        int invulnerability = Integer.valueOf(config.getString("respawn_invulnerability"));
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                dead.setRecentDeath(false);
            }
        }, invulnerability * 20L);
    }

    public static void teleportTeamSpawn(Game game) {
        game.getCurrentPlayers().forEach(player -> {
            Team team = game.getPlayerTeam(player);
            player.teleport(team.getSpawnLocation());
        });
    }

    //TODO: Missing give items and extra lives from perks.

    // TODO: Improve
    public void runRewardActions(List<String> commands, PaintballPlayer j) {
        CommandSender console = Bukkit.getServer().getConsoleSender();
        for (String command : commands) {
            if (command.startsWith("msg %player%")) {
                String mensaje = command.replace("msg %player% ", "");
                j.getPlayer().sendMessage(MessageUtils.translateColor(mensaje));
            } else {
                String comandoAEnviar = command.replaceAll("%player%", j.getPlayer().getName());
                if (comandoAEnviar.contains("%random")) {
                    int pos = comandoAEnviar.indexOf("%random");
                    int nextPos = comandoAEnviar.indexOf("%", pos + 1);
                    String variableCompleta = comandoAEnviar.substring(pos, nextPos + 1);
                    String variable = variableCompleta.replace("%random_", "").replace("%", "");
                    String[] sep = variable.split("-");
                    int cantidadMinima = 0;
                    int cantidadMaxima = 0;

                    try {
                        cantidadMinima = (int) UtilidadesOtros.eval(sep[0].replace("kills", j.getKills() + ""));
                        cantidadMaxima = (int) UtilidadesOtros.eval(sep[1].replace("kills", j.getKills() + ""));
                    } catch (Exception e) {

                    }
                    int num = UtilidadesOtros.getNumeroAleatorio(cantidadMinima, cantidadMaxima);
                    comandoAEnviar = comandoAEnviar.replace(variableCompleta, num + "");
                }
                Bukkit.dispatchCommand(console, comandoAEnviar);
            }
        }
    }
}
