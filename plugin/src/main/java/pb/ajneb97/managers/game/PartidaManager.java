package pb.ajneb97.managers.game;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.api.Hat;
import pb.ajneb97.api.PaintballAPI;
import pb.ajneb97.juego.GameState;
import pb.ajneb97.juego.PaintballPlayer;
import pb.ajneb97.juego.Partida;
import pb.ajneb97.juego.Team;
import pb.ajneb97.managers.perks.CooldownKillstreaks;
import pb.ajneb97.managers.perks.CooldownManager;
import pb.ajneb97.utils.UtilidadesItems;
import pb.ajneb97.utils.UtilidadesOtros;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class PartidaManager {

    @SuppressWarnings("deprecation")
    public static void jugadorEntra(Partida partida, Player jugador, PaintballBattle plugin) {
        PaintballPlayer paintballPlayer = new PaintballPlayer(jugador);
        YamlDocument messages = plugin.getMessagesDocument();
        partida.agregarJugador(paintballPlayer);
        ArrayList<PaintballPlayer> jugadores = partida.getJugadores();
        for (int i = 0; i < jugadores.size(); i++) {
            jugadores.get(i).getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("playerJoin").replace("%player%", jugador.getName())
                    .replace("%current_players%", partida.getCantidadActualJugadores() + "").replace("%max_players%", partida.getCantidadMaximaJugadores() + "")));
        }

        jugador.getInventory().clear();
        jugador.getEquipment().clear();
        jugador.getEquipment().setArmorContents(null);
        jugador.updateInventory();

        jugador.setGameMode(GameMode.SURVIVAL);
        jugador.setExp(0);
        jugador.setLevel(0);
        jugador.setFoodLevel(20);
        jugador.setMaxHealth(20);
        jugador.setHealth(20);
        jugador.setFlying(false);
        jugador.setAllowFlight(false);
        for (PotionEffect p : jugador.getActivePotionEffects()) {
            jugador.removePotionEffect(p.getType());
        }

        jugador.teleport(partida.getLobby());

        YamlDocument config = plugin.getConfigDocument();
        if (config.getString("leave_item_enabled").equals("true")) {
            ItemStack item = UtilidadesItems.crearItem(config, "leave_item");
            jugador.getInventory().setItem(8, item);
        }
        if (config.getString("hats_item_enabled").equals("true")) {
            ItemStack item = UtilidadesItems.crearItem(config, "hats_item");
            jugador.getInventory().setItem(7, item);
        }
        if (config.getString("choose_team_system").equals("true")) {
            ItemStack item = UtilidadesItems.crearItem(config, "teams." + partida.getTeam1().getTipo());
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', messages.getString("teamChoose").replace("%team%", config.getString("teams." + partida.getTeam1().getTipo() + ".name"))));
            item.setItemMeta(meta);
            jugador.getInventory().setItem(0, item);
            item = UtilidadesItems.crearItem(config, "teams." + partida.getTeam2().getTipo());
            meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', messages.getString("teamChoose").replace("%team%", config.getString("teams." + partida.getTeam2().getTipo() + ".name"))));
            item.setItemMeta(meta);
            jugador.getInventory().setItem(1, item);
        }

        if (partida.getCantidadActualJugadores() >= partida.getCantidadMinimaJugadores()
                && partida.getEstado().equals(GameState.WAITING)) {
            cooldownIniciarPartida(partida, plugin);
        }
    }

    @SuppressWarnings("deprecation")
    public static void jugadorSale(Partida partida, Player jugador, boolean finalizaPartida,
                                   PaintballBattle plugin, boolean cerrandoServer) {
        PaintballPlayer paintballPlayer = partida.getJugador(jugador.getName());
        YamlDocument messages = plugin.getMessagesDocument();

        partida.removerJugador(jugador.getName());

        if (!finalizaPartida) {
            ArrayList<PaintballPlayer> jugadores = partida.getJugadores();
            for (int i = 0; i < jugadores.size(); i++) {
                jugadores.get(i).getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("playerLeave").replace("%player%", jugador.getName())
                        .replace("%current_players%", partida.getCantidadActualJugadores() + "").replace("%max_players%", partida.getCantidadMaximaJugadores() + "")));
            }
        }

        YamlDocument config = plugin.getConfigDocument();
        double x = Double.valueOf(config.getString("MainLobby.x"));
        double y = Double.valueOf(config.getString("MainLobby.y"));
        double z = Double.valueOf(config.getString("MainLobby.z"));
        String world = config.getString("MainLobby.world");
        float yaw = Float.valueOf(config.getString("MainLobby.yaw"));
        float pitch = Float.valueOf(config.getString("MainLobby.pitch"));
        Location mainLobby = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        jugador.teleport(mainLobby);

        paintballPlayer.getSavedElements().restorePlayerElements(jugador);
        for (PotionEffect p : jugador.getActivePotionEffects()) {
            jugador.removePotionEffect(p.getType());
        }
        jugador.updateInventory();

        if (!cerrandoServer) {
            if (partida.getCantidadActualJugadores() < partida.getCantidadMinimaJugadores()
                    && partida.getEstado().equals(GameState.STARTING)) {
                partida.setEstado(GameState.WAITING);
            } else if (partida.getCantidadActualJugadores() <= 1 && (partida.getEstado().equals(GameState.PLAYING))) {
                //fase finalizacion
                PartidaManager.iniciarFaseFinalizacion(partida, plugin);
            } else if ((partida.getTeam1().getCantidadJugadores() == 0 || partida.getTeam2().getCantidadJugadores() == 0) && partida.getEstado().equals(GameState.PLAYING)) {
                //fase finalizacion
                PartidaManager.iniciarFaseFinalizacion(partida, plugin);
            }
        }
    }

    public static void cooldownIniciarPartida(Partida partida, PaintballBattle plugin) {
        partida.setEstado(GameState.STARTING);
        YamlDocument config = plugin.getConfigDocument();
        YamlDocument messages = plugin.getMessagesDocument();
        int time = Integer.valueOf(config.getString("arena_starting_cooldown"));

        CooldownManager cooldown = new CooldownManager(plugin);
        cooldown.cooldownComenzarJuego(partida, time);

        String prefix = ChatColor.translateAlternateColorCodes('&', messages.getString("prefix")) + " ";

        if (config.getString("broadcast_starting_arena.enabled").equals("true")) {
            List<String> worlds = config.getStringList("broadcast_starting_arena.worlds");
            for (String world : worlds) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getWorld().getName().equals(world)) {
                        player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("arenaStartingBroadcast")
                                .replace("%arena%", partida.getNombre())));
                    }
                }
            }
        }
    }

    public static void iniciarPartida(Partida partida, PaintballBattle plugin) {
        partida.setEstado(GameState.PLAYING);
        YamlDocument messages = plugin.getMessagesDocument();
        YamlDocument config = plugin.getConfigDocument();
        //String prefix = ChatColor.translateAlternateColorCodes('&', messages.getString("prefix"))+" ";

        if (config.getString("choose_team_system").equals("true")) {
            setTeams(partida);
        } else {
            setTeamsAleatorios(partida);
        }

        darItems(partida, plugin.getConfigDocument(), plugin.getShopDocument(), messages);
        teletransportarJugadores(partida);
        setVidas(partida, plugin.getShopDocument());

        ArrayList<PaintballPlayer> jugadores = partida.getJugadores();
        String[] separados = config.getString("startGameSound").split(";");
        Sound sound = null;
        float volume = 0;
        float pitch = 0;
        try {
            sound = Sound.valueOf(separados[0]);
            volume = Float.valueOf(separados[1]);
            pitch = Float.valueOf(separados[2]);
        } catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PaintballBattle.prefix + "&7Sound Name: &c" + separados[0] + " &7is not valid."));
            sound = null;
        }
        for (int i = 0; i < jugadores.size(); i++) {
            String nombreTeam = partida.getEquipoJugador(jugadores.get(i).getPlayer().getName()).getTipo();
            jugadores.get(i).getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("gameStarted")));
            jugadores.get(i).getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("teamInformation").replace("%team%", config.getString("teams." + nombreTeam + ".name"))));
            jugadores.get(i).getPlayer().closeInventory();
            if (sound != null) {
                jugadores.get(i).getPlayer().playSound(jugadores.get(i).getPlayer().getLocation(), sound, volume, pitch);
            }
        }

        CooldownManager cooldown = new CooldownManager(plugin);
        cooldown.cooldownJuego(partida);
    }


    public static void setVidas(Partida partida, YamlDocument shop) {
        partida.getTeam1().setVidas(partida.getVidasIniciales());
        partida.getTeam2().setVidas(partida.getVidasIniciales());

        ArrayList<PaintballPlayer> jugadoresTeam1 = partida.getTeam1().getJugadores();
        for (PaintballPlayer j : jugadoresTeam1) {
            //comprobar perk extralives
            int nivelExtraLives = PaintballAPI.getPerkLevel(j.getPlayer(), "extra_lives");
            if (nivelExtraLives != 0) {
                String linea = shop.getStringList("perks_upgrades.extra_lives").get(nivelExtraLives - 1);
                String[] sep = linea.split(";");
                int cantidad = Integer.valueOf(sep[0]);
                partida.getTeam1().aumentarVidas(cantidad);
            }
        }
        ArrayList<PaintballPlayer> jugadoresTeam2 = partida.getTeam2().getJugadores();
        for (PaintballPlayer j : jugadoresTeam2) {
            //comprobar perk extralives
            int nivelExtraLives = PaintballAPI.getPerkLevel(j.getPlayer(), "extra_lives");
            if (nivelExtraLives != 0) {
                String linea = shop.getStringList("perks_upgrades.extra_lives").get(nivelExtraLives - 1);
                String[] sep = linea.split(";");
                int cantidad = Integer.valueOf(sep[0]);
                partida.getTeam2().aumentarVidas(cantidad);
            }
        }
    }

    public static void killstreakInstantanea(String key, Player jugador, Partida partida, PaintballBattle plugin) {
        YamlDocument config = plugin.getConfigDocument();
        if (key.equalsIgnoreCase("3_lives")) {
            Team team = partida.getEquipoJugador(jugador.getName());
            team.aumentarVidas(3);
        } else if (key.equalsIgnoreCase("teleport")) {
            PaintballPlayer j = partida.getJugador(jugador.getName());
            if (j.getDeathLocation() != null) {
                j.getPlayer().teleport(j.getDeathLocation());
            } else {
                Team team = partida.getEquipoJugador(jugador.getName());
                j.getPlayer().teleport(team.getSpawn());
            }
        } else if (key.equalsIgnoreCase("more_snowballs")) {
            PaintballPlayer j = partida.getJugador(jugador.getName());
            int snowballs = Integer.valueOf(config.getString("killstreaks_items." + key + ".snowballs"));
            ItemStack item = null;
            if (Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15")
                    || Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17") || Bukkit.getVersion().contains("1.18")
                    || Bukkit.getVersion().contains("1.19") || Bukkit.getVersion().contains("1.20")) {
                if (j.getSelectedHat().equals("chicken_hat")) {
                    item = new ItemStack(Material.EGG, 1);
                } else {
                    item = new ItemStack(Material.SNOWBALL, 1);
                }

            } else {
                if (j.getSelectedHat().equals("chicken_hat")) {
                    item = new ItemStack(Material.EGG, 1);
                } else {
                    item = new ItemStack(Material.valueOf("SNOW_BALL"), 1);
                }

            }
            for (int i = 0; i < snowballs; i++) {
                jugador.getInventory().addItem(item);
            }
        } else if (key.equalsIgnoreCase("lightning")) {
            PaintballPlayer jugadorAtacante = partida.getJugador(jugador.getName());
            int radio = Integer.valueOf(config.getString("killstreaks_items." + key + ".radius"));
            Collection<Entity> entidades = jugador.getWorld().getNearbyEntities(jugador.getLocation(), radio, radio, radio);
            for (Entity e : entidades) {
                if (e != null && e.getType().equals(EntityType.PLAYER)) {
                    Player player = (Player) e;
                    PaintballPlayer victimPlayer = partida.getJugador(player.getName());
                    if (victimPlayer != null) {
                        PartidaManager.muereJugador(partida, jugadorAtacante, victimPlayer, plugin, true, false);
                    }
                }
            }
        } else if (key.equalsIgnoreCase("nuke")) {
            partida.setEnNuke(true);
            PaintballPlayer jugadorAtacante = partida.getJugador(jugador.getName());
            CooldownKillstreaks c = new CooldownKillstreaks(plugin);
            String[] separados1 = config.getString("killstreaks_items." + key + ".activateSound").split(";");
            String[] separados2 = config.getString("killstreaks_items." + key + ".finalSound").split(";");
            c.cooldownNuke(jugadorAtacante, partida, separados1, separados2);
        }
    }

    @SuppressWarnings("unchecked")
    public static void setTeamsAleatorios(Partida partida) {
        ArrayList<PaintballPlayer> jugadores = partida.getJugadores();
        ArrayList<PaintballPlayer> jugadoresCopia = (ArrayList<PaintballPlayer>) partida.getJugadores().clone();
        //Si son 4 se seleccionan 2, Si son 5 tambien 2, Si son 6, 3, Si son 7, tambien 3
        Random r = new Random();
        int num = jugadores.size() / 2;
        for (int i = 0; i < num; i++) {
            int pos = r.nextInt(jugadoresCopia.size());
            PaintballPlayer jugadorSelect = jugadoresCopia.get(pos);
            jugadoresCopia.remove(pos);

            partida.repartirJugadorTeam2(jugadorSelect);
        }
    }

    private static void setTeams(Partida partida) {
        //Falta comprobar lo siguiente:
        //Si 2 usuarios seleccionan team y uno se va, los 2 usuarios estaran en el mismo team al
        //iniciar la partida y seran solo ellos 2.

        ArrayList<PaintballPlayer> jugadores = partida.getJugadores();
        for (PaintballPlayer j : jugadores) {
            partida.getEquipoJugador(j.getPlayer().getName()).removerJugador(j.getPlayer().getName());
            String preferenciaTeam = j.getPreferenciaTeam();
            if (preferenciaTeam == null) {
                if (partida.puedeSeleccionarEquipo(partida.getTeam1().getTipo())) {
                    j.setPreferenciaTeam(partida.getTeam1().getTipo());
                } else {
                    j.setPreferenciaTeam(partida.getTeam2().getTipo());
                }
            }
            preferenciaTeam = j.getPreferenciaTeam();
            if (preferenciaTeam.equals(partida.getTeam2().getTipo())) {
                partida.getTeam2().agregarJugador(j);
            } else {
                partida.getTeam1().agregarJugador(j);
            }
        }

        //Balanceo final
        Team team1 = partida.getTeam1();
        Team team2 = partida.getTeam2();
        for (PaintballPlayer j : jugadores) {
            Team team = partida.getEquipoJugador(j.getPlayer().getName());
            if (team1.getCantidadJugadores() > team2.getCantidadJugadores() + 1) {
                if (team.getTipo().equals(team1.getTipo())) {
                    //Mover al jugador del equipo1 al equipo2
                    team1.removerJugador(j.getPlayer().getName());
                    team2.agregarJugador(j);
                }
            } else if (team2.getCantidadJugadores() > team1.getCantidadJugadores() + 1) {
                if (team.getTipo().equals(team2.getTipo())) {
                    //Mover al jugador del equipo2 al equipo1
                    team2.removerJugador(j.getPlayer().getName());
                    team1.agregarJugador(j);
                }
            }
        }
    }

    public static void darItems(Partida partida, YamlDocument config, YamlDocument shop, YamlDocument messages) {
        ArrayList<PaintballPlayer> jugadores = partida.getJugadores();
        for (PaintballPlayer j : jugadores) {
            Player p = j.getPlayer();
            p.getInventory().setItem(8, null);

            Team team = partida.getEquipoJugador(p.getName());
            if (config.contains("teams." + team.getTipo())) {
                darEquipamientoJugador(p, Integer.valueOf(config.getString("teams." + team.getTipo() + ".color")));
            } else {
                darEquipamientoJugador(p, 0);
            }
            //comprobar perk initial killcoins
            int nivelInitialKillcoins = PaintballAPI.getPerkLevel(j.getPlayer(), "initial_killcoins");
            if (nivelInitialKillcoins != 0) {
                String linea = shop.getStringList("perks_upgrades.initial_killcoins").get(nivelInitialKillcoins - 1);
                String[] sep = linea.split(";");
                int cantidad = Integer.valueOf(sep[0]);
                j.agregarCoins(cantidad);
            }
            UtilidadesItems.crearItemKillstreaks(j, config);
            ponerHat(partida, j, config, messages);
            setBolasDeNieve(j, config);
        }
    }

    @SuppressWarnings("unchecked")
    public static void ponerHat(Partida partida, PaintballPlayer jugador, YamlDocument config, YamlDocument messages) {
        ArrayList<Hat> hats = PaintballAPI.getHats(jugador.getPlayer());
        for (Hat h : hats) {
            if (h.isSelected()) {
                jugador.setSelectedHat(h.getName());
                ItemStack item = UtilidadesItems.crearItem(config, "hats_items." + h.getName());
                ItemMeta meta = item.getItemMeta();
                meta.setLore(null);
                item.setItemMeta(meta);
                if (config.contains("hats_items." + h.getName() + ".skull_id")) {
                    String id = config.getString("hats_items." + h.getName() + ".skull_id");
                    String textura = config.getString("hats_items." + h.getName() + ".skull_texture");
                    item = UtilidadesItems.getCabeza(item, textura);
                }
                jugador.getPlayer().getEquipment().setHelmet(item);

                if (h.getName().equals("speed_hat")) {
                    jugador.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999999, 0, false, false));
                } else if (h.getName().equals("present_hat")) {
                    Team team = partida.getEquipoJugador(jugador.getPlayer().getName());
                    ArrayList<PaintballPlayer> jugadoresCopy = (ArrayList<PaintballPlayer>) team.getJugadores().clone();
                    jugadoresCopy.remove(jugador);
                    if (!jugadoresCopy.isEmpty()) {
                        Random r = new Random();
                        int pos = r.nextInt(jugadoresCopy.size());
                        String jName = jugadoresCopy.get(pos).getPlayer().getName();
                        PaintballPlayer j = partida.getJugador(jName);
                        j.agregarCoins(3);
                        jugador.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("presentHatGive").replace("%player%", j.getPlayer().getName())));
                        j.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("presentHatReceive").replace("%player%", jugador.getPlayer().getName())));
                    }
                }
                return;
            }
        }
    }

    public static void darEquipamientoJugador(Player jugador, int color) {
        ItemStack item = new ItemStack(Material.LEATHER_HELMET, 1);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(Color.fromRGB(color));
        item.setItemMeta(meta);
        jugador.getInventory().setHelmet(item);

        item = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(Color.fromRGB(color));
        item.setItemMeta(meta);
        jugador.getInventory().setChestplate(item);

        item = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(Color.fromRGB(color));
        item.setItemMeta(meta);
        jugador.getInventory().setLeggings(item);

        item = new ItemStack(Material.LEATHER_BOOTS, 1);
        meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(Color.fromRGB(color));
        item.setItemMeta(meta);
        jugador.getInventory().setBoots(item);
    }

    public static void setBolasDeNieve(PaintballPlayer j, YamlDocument config) {
        for (int i = 0; i <= 7; i++) {
            j.getPlayer().getInventory().setItem(i, null);
        }
        for (int i = 9; i <= 35; i++) {
            j.getPlayer().getInventory().setItem(i, null);
        }
        int amount = Integer.valueOf(config.getString("initial_snowballs"));
        ItemStack item = null;
        if (Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15")
                || Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17") || Bukkit.getVersion().contains("1.18")
                || Bukkit.getVersion().contains("1.19") || Bukkit.getVersion().contains("1.20")) {
            if (j.getSelectedHat().equals("chicken_hat")) {
                item = new ItemStack(Material.EGG, 1);
            } else {
                item = new ItemStack(Material.SNOWBALL, 1);
            }
        } else {
            if (j.getSelectedHat().equals("chicken_hat")) {
                item = new ItemStack(Material.EGG, 1);
            } else {
                item = new ItemStack(Material.valueOf("SNOW_BALL"), 1);
            }
        }

        for (int i = 0; i < amount; i++) {
            j.getPlayer().getInventory().addItem(item);
        }
    }

    public static void lanzarFuegos(ArrayList<PaintballPlayer> jugadores) {
        for (PaintballPlayer j : jugadores) {
            Firework fw = (Firework) j.getPlayer().getWorld().spawnEntity(j.getPlayer().getLocation(), EntityType.FIREWORK);
            FireworkMeta fwm = fw.getFireworkMeta();
            Type type = Type.BALL;
            Color c1 = Color.RED;
            Color c2 = Color.AQUA;
            FireworkEffect efecto = FireworkEffect.builder().withColor(c1).withFade(c2).with(type).build();
            fwm.addEffect(efecto);
            fwm.setPower(2);
            fw.setFireworkMeta(fwm);
        }
    }

    public static void teletransportarJugadores(Partida partida) {
        ArrayList<PaintballPlayer> jugadores = partida.getJugadores();
        for (PaintballPlayer j : jugadores) {
            Player p = j.getPlayer();
            Team team = partida.getEquipoJugador(p.getName());
            p.teleport(team.getSpawn());
        }
    }

    public static void iniciarFaseFinalizacion(Partida partida, PaintballBattle plugin) {
        partida.setEstado(GameState.ENDING);
        Team ganador = partida.getGanador();
        YamlDocument messages = plugin.getMessagesDocument();
        YamlDocument config = plugin.getConfigDocument();

        String nameTeam1 = config.getString("teams." + partida.getTeam1().getTipo() + ".name");
        String nameTeam2 = config.getString("teams." + partida.getTeam2().getTipo() + ".name");

        String status = "";
        if (ganador == null) {
            //empate
            status = messages.getString("gameFinishedTieStatus");
        } else {
            String ganadorTexto = config.getString("teams." + ganador.getTipo() + ".name");
            status = messages.getString("gameFinishedWinnerStatus").replace("%winner_team%", ganadorTexto);
        }

        ArrayList<PaintballPlayer> jugadoresKillsOrd = partida.getJugadoresKills();
        String top1 = "";
        String top2 = "";
        String top3 = "";
        int top1Kills = 0;
        int top2Kills = 0;
        int top3Kills = 0;

        if (jugadoresKillsOrd.size() == 2) {
            top1 = jugadoresKillsOrd.get(0).getPlayer().getName();
            top1Kills = jugadoresKillsOrd.get(0).getKills();
            top2 = jugadoresKillsOrd.get(1).getPlayer().getName();
            top2Kills = jugadoresKillsOrd.get(1).getKills();
            top3 = messages.getString("topKillsNone");
        } else if (jugadoresKillsOrd.size() == 1) {
            top1 = jugadoresKillsOrd.get(0).getPlayer().getName();
            top1Kills = jugadoresKillsOrd.get(0).getKills();
            top3 = messages.getString("topKillsNone");
            top2 = messages.getString("topKillsNone");
        } else {
            top1 = jugadoresKillsOrd.get(0).getPlayer().getName();
            top1Kills = jugadoresKillsOrd.get(0).getKills();
            top2 = jugadoresKillsOrd.get(1).getPlayer().getName();
            top3 = jugadoresKillsOrd.get(2).getPlayer().getName();
            top2Kills = jugadoresKillsOrd.get(1).getKills();
            top3Kills = jugadoresKillsOrd.get(2).getKills();
        }
        ArrayList<PaintballPlayer> jugadores = partida.getJugadores();
        List<String> msg = messages.getStringList("gameFinished");
        for (PaintballPlayer j : jugadores) {
            for (int i = 0; i < msg.size(); i++) {
                j.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', msg.get(i).replace("%status_message%", status).replace("%team1%", nameTeam1)
                        .replace("%team2%", nameTeam2).replace("%kills_team1%", partida.getTeam1().getAsesinatosTotales() + "")
                        .replace("%kills_team2%", partida.getTeam2().getAsesinatosTotales() + "").replace("%player1%", top1).replace("%player2%", top2)
                        .replace("%player3%", top3).replace("%kills_player1%", top1Kills + "").replace("%kills_player2%", top2Kills + "")
                        .replace("%kills_player3%", top3Kills + "").replace("%kills_player%", j.getKills() + "")));
            }
            Team teamJugador = partida.getEquipoJugador(j.getPlayer().getName());
            //TODO
//                plugin.registerPlayer(j.getPlayer().getUniqueId().toString() + ".yml");
//                if (plugin.getJugador(j.getPlayer().getName()) == null) {
//                    plugin.agregarJugadorDatos(new JugadorDatos(j.getPlayer().getName(), j.getPlayer().getUniqueId().toString(), 0, 0, 0, 0, 0, new ArrayList<Perk>(), new ArrayList<Hat>()));
//                }
//                JugadorDatos jugador = plugin.getJugador(j.getPlayer().getName());
//                if (partida.getEquipoJugador(j.getPlayer().getName()).equals(ganador)) {
//                    jugador.aumentarWins();
//                    TitleAPI.sendTitle(j.getPlayer(), 10, 40, 10, messages.getString("winnerTitleMessage"), "");
//                } else if (ganador == null) {
//                    jugador.aumentarTies();
//                    TitleAPI.sendTitle(j.getPlayer(), 10, 40, 10, messages.getString("tieTitleMessage"), "");
//                } else {
//                    jugador.aumentarLoses();
//                    TitleAPI.sendTitle(j.getPlayer(), 10, 40, 10, messages.getString("loserTitleMessage"), "");
//                }
//
//                jugador.aumentarKills(j.getKills());
            j.getPlayer().closeInventory();
            j.getPlayer().getInventory().clear();


            if (config.getString("leave_item_enabled").equals("true")) {
                ItemStack item = UtilidadesItems.crearItem(config, "leave_item");
                j.getPlayer().getInventory().setItem(8, item);
            }
            if (config.getString("play_again_item_enabled").equals("true")) {
                ItemStack item = UtilidadesItems.crearItem(config, "play_again_item");
                j.getPlayer().getInventory().setItem(7, item);
            }

            if (config.getString("rewards_executed_after_teleport").equals("false")) {
                if (ganador != null) {
                    if (ganador.getTipo().equals(teamJugador.getTipo())) {
                        List<String> commands = config.getStringList("winners_command_rewards");
                        ejecutarComandosRewards(commands, j);
                    } else {
                        List<String> commands = config.getStringList("losers_command_rewards");
                        ejecutarComandosRewards(commands, j);
                    }
                } else {
                    List<String> commands = config.getStringList("tie_command_rewards");
                    ejecutarComandosRewards(commands, j);
                }
            }
        }

        int time = Integer.valueOf(config.getString("arena_ending_phase_cooldown"));
        CooldownManager c = new CooldownManager(plugin);
        c.cooldownFaseFinalizacion(partida, time, ganador);
    }

    public static void ejecutarComandosRewards(List<String> commands, PaintballPlayer j) {
        CommandSender console = Bukkit.getServer().getConsoleSender();
        for (int i = 0; i < commands.size(); i++) {
            if (commands.get(i).startsWith("msg %player%")) {
                String mensaje = commands.get(i).replace("msg %player% ", "");
                j.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje));
            } else {
                String comandoAEnviar = commands.get(i).replaceAll("%player%", j.getPlayer().getName());
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

    public static void finalizarPartida(Partida partida, PaintballBattle plugin, boolean cerrandoServer, Team ganadorTeam) {
        YamlDocument config = plugin.getConfigDocument();
        ArrayList<PaintballPlayer> jugadores = partida.getJugadores();
        for (PaintballPlayer j : jugadores) {
            String tipoFin = "";
            if (ganadorTeam != null) {
                Team teamJugador = partida.getEquipoJugador(j.getPlayer().getName());
                if (ganadorTeam.getTipo().equals(teamJugador.getTipo())) {
                    tipoFin = "ganador";
                } else {
                    tipoFin = "perdedor";
                }
            } else {
                tipoFin = "empate";
            }
            jugadorSale(partida, j.getPlayer(), true, plugin, cerrandoServer);
            if (config.getString("rewards_executed_after_teleport").equals("true") && !cerrandoServer) {
                if (tipoFin.equals("ganador")) {
                    List<String> commands = config.getStringList("winners_command_rewards");
                    ejecutarComandosRewards(commands, j);
                } else if (tipoFin.equals("perdedor")) {
                    List<String> commands = config.getStringList("losers_command_rewards");
                    ejecutarComandosRewards(commands, j);
                } else {
                    List<String> commands = config.getStringList("tie_command_rewards");
                    ejecutarComandosRewards(commands, j);
                }
            }
        }
        partida.getTeam1().setVidas(0);
        partida.getTeam2().setVidas(0);
        partida.setEnNuke(false);
        partida.modificarTeams(config);

        partida.setEstado(GameState.WAITING);
    }

    public static void muereJugador(Partida partida, PaintballPlayer jugadorAtacante, final PaintballPlayer victimPlayer, PaintballBattle plugin, boolean lightning, boolean nuke) {
        if (victimPlayer.haSidoAsesinadoRecientemente()) {
            return;
        }
        if (victimPlayer.getSelectedHat().equals("guardian_hat") && victimPlayer.isEfectoHatActivado()) {
            return;
        }
        if (victimPlayer.getSelectedHat().equals("protector_hat")) {
            Random r = new Random();
            int num = r.nextInt(100);
            if (num >= 80) {
                return;
            }
        }

        Team targetTeam = partida.getEquipoJugador(victimPlayer.getPlayer().getName());
        Team teamAtacante = partida.getEquipoJugador(jugadorAtacante.getPlayer().getName());
        if (targetTeam.equals(teamAtacante)) {
            return;
        }

        if (lightning) {
            victimPlayer.getPlayer().getWorld().strikeLightningEffect(victimPlayer.getPlayer().getLocation());
        }
        YamlDocument messages = plugin.getMessagesDocument();
        YamlDocument config = plugin.getConfigDocument();
        victimPlayer.aumentarMuertes();
        victimPlayer.setDeathLocation(victimPlayer.getPlayer().getLocation().clone());
        victimPlayer.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("killedBy").replace("%player%", jugadorAtacante.getPlayer().getName())));
        String[] separados = config.getString("killedBySound").split(";");
        try {
            Sound sound = Sound.valueOf(separados[0]);
            victimPlayer.getPlayer().playSound(victimPlayer.getPlayer().getLocation(), sound, Float.valueOf(separados[1]), Float.valueOf(separados[2]));
        } catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PaintballBattle.prefix + "&7Sound Name: &c" + separados[0] + " &7is not valid."));
        }
        victimPlayer.setAsesinadoRecientemente(true);
        victimPlayer.setLastKilledBy(jugadorAtacante.getPlayer().getName());
        targetTeam.disminuirVidas(1);

        Team team = partida.getEquipoJugador(victimPlayer.getPlayer().getName());
        if (victimPlayer.getSelectedHat().equals("explosive_hat")) {
            Random r = new Random();
            int num = r.nextInt(100);
            if (num >= 80) {
                if (Bukkit.getVersion().contains("1.8")) {
                    victimPlayer.getPlayer().getWorld().playEffect(victimPlayer.getPlayer().getLocation(), Effect.valueOf("EXPLOSION_LARGE"), 2);
                } else {
                    victimPlayer.getPlayer().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, victimPlayer.getPlayer().getLocation(), 2);
                }
                separados = config.getString("explosiveHatSound").split(";");
                try {
                    Sound sound = Sound.valueOf(separados[0]);
                    victimPlayer.getPlayer().getWorld().playSound(victimPlayer.getPlayer().getLocation(), sound, Float.valueOf(separados[1]), Float.valueOf(separados[2]));
                } catch (Exception ex) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PaintballBattle.prefix + "&7Sound Name: &c" + separados[0] + " &7is not valid."));
                }
                Collection<Entity> entidades = victimPlayer.getPlayer().getWorld().getNearbyEntities(victimPlayer.getPlayer().getLocation(), 5, 5, 5);
                for (Entity e : entidades) {
                    if (e != null && e.getType().equals(EntityType.PLAYER)) {
                        Player player = (Player) e;
                        PaintballPlayer secondVictimPlayer = partida.getJugador(player.getName());
                        if (secondVictimPlayer != null) {
                            PartidaManager.muereJugador(partida, victimPlayer, secondVictimPlayer, plugin, false, false);
                        }
                    }
                }
            }
        }
        victimPlayer.getPlayer().teleport(team.getSpawn());
        if (Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15")
                || Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17") || Bukkit.getVersion().contains("1.18")
                || Bukkit.getVersion().contains("1.19") || Bukkit.getVersion().contains("1.20")) {
            if (victimPlayer.getSelectedHat().equals("chicken_hat")) {
                victimPlayer.getPlayer().getInventory().removeItem(new ItemStack(Material.EGG));
            } else {
                victimPlayer.getPlayer().getInventory().removeItem(new ItemStack(Material.SNOWBALL));
            }
        } else {
            if (victimPlayer.getSelectedHat().equals("chicken_hat")) {
                victimPlayer.getPlayer().getInventory().removeItem(new ItemStack(Material.EGG));
            } else {
                victimPlayer.getPlayer().getInventory().removeItem(new ItemStack(Material.valueOf("SNOW_BALL")));
            }
        }
        PartidaManager.setBolasDeNieve(victimPlayer, config);

        jugadorAtacante.aumentarAsesinatos();
        int cantidadCoinsGanados = UtilidadesOtros.coinsGanados(jugadorAtacante.getPlayer(), config);
        int nivelExtraKillCoins = PaintballAPI.getPerkLevel(jugadorAtacante.getPlayer(), "extra_killcoins");
        if (nivelExtraKillCoins != 0) {
            String linea = plugin.getShopDocument().getStringList("perks_upgrades.extra_killcoins").get(nivelExtraKillCoins - 1);
            String[] sep = linea.split(";");
            int cantidad = Integer.valueOf(sep[0]);
            cantidadCoinsGanados = cantidadCoinsGanados + cantidad;
        }
        String lastKilledBy = jugadorAtacante.getLastKilledBy();
        if (lastKilledBy != null && lastKilledBy.equals(victimPlayer.getPlayer().getName())) {
            cantidadCoinsGanados = cantidadCoinsGanados + 1;
        }
        jugadorAtacante.agregarCoins(cantidadCoinsGanados);
        UtilidadesItems.crearItemKillstreaks(jugadorAtacante, config);

        if (nuke) {
            String equipoAtacanteName = config.getString("teams." + teamAtacante.getTipo() + ".name");
            String targetTeamName = config.getString("teams." + targetTeam.getTipo() + ".name");
            for (PaintballPlayer j : partida.getJugadores()) {
                if (!j.getPlayer().getName().equals(jugadorAtacante.getPlayer().getName())) {
                    j.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("nukeKillMessage").replace("%team_player1%", targetTeamName)
                            .replace("%player1%", victimPlayer.getPlayer().getName()).replace("%team_player2%", equipoAtacanteName)
                            .replace("%player2%", jugadorAtacante.getPlayer().getName())));
                }
            }
        }
        jugadorAtacante.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("kill").replace("%player%", victimPlayer.getPlayer().getName())));
        if (!nuke) {
            separados = config.getString("killSound").split(";");
            try {
                Sound sound = Sound.valueOf(separados[0]);
                jugadorAtacante.getPlayer().playSound(jugadorAtacante.getPlayer().getLocation(), sound, Float.valueOf(separados[1]), Float.valueOf(separados[2]));
            } catch (Exception ex) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PaintballBattle.prefix + "&7Sound Name: &c" + separados[0] + " &7is not valid."));
            }
        }


        int snowballs = Integer.valueOf(config.getString("snowballs_per_kill"));
        if (Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15")
                || Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17") || Bukkit.getVersion().contains("1.18")
                || Bukkit.getVersion().contains("1.19") || Bukkit.getVersion().contains("1.20")) {
            if (jugadorAtacante.getSelectedHat().equals("chicken_hat")) {
                jugadorAtacante.getPlayer().getInventory().addItem(new ItemStack(Material.EGG, snowballs));
            } else {
                jugadorAtacante.getPlayer().getInventory().addItem(new ItemStack(Material.SNOWBALL, snowballs));
            }

        } else {
            if (jugadorAtacante.getSelectedHat().equals("chicken_hat")) {
                jugadorAtacante.getPlayer().getInventory().addItem(new ItemStack(Material.EGG, snowballs));
            } else {
                jugadorAtacante.getPlayer().getInventory().addItem(new ItemStack(Material.valueOf("SNOW_BALL"), snowballs));
            }

        }

        if (targetTeam.getVidas() <= 0) {
            //terminar partida
            PartidaManager.iniciarFaseFinalizacion(partida, plugin);
            return;
        }

        int invulnerability = Integer.valueOf(config.getString("respawn_invulnerability"));
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                victimPlayer.setAsesinadoRecientemente(false);
            }
        }, invulnerability * 20L);
    }

    public static Partida getPartidaDisponible(PaintballBattle plugin) {
        ArrayList<Partida> partidas = plugin.getGameManager().getPartidas();
        ArrayList<Partida> disponibles = new ArrayList<Partida>();
        for (int i = 0; i < partidas.size(); i++) {
            if (partidas.get(i).getEstado().equals(GameState.WAITING) ||
                    partidas.get(i).getEstado().equals(GameState.STARTING)) {
                if (!partidas.get(i).estaLlena()) {
                    disponibles.add(partidas.get(i));
                }
            }
        }

        if (disponibles.isEmpty()) {
            return null;
        }

        //Ordenar
        for (int i = 0; i < disponibles.size(); i++) {
            for (int c = i + 1; c < disponibles.size(); c++) {
                if (disponibles.get(i).getCantidadActualJugadores() < disponibles.get(c).getCantidadActualJugadores()) {
                    Partida p = disponibles.get(i);
                    disponibles.set(i, disponibles.get(c));
                    disponibles.set(c, p);
                }
            }
        }
        return disponibles.get(0);
    }
}
