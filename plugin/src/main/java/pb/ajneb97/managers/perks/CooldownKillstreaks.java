package pb.ajneb97.managers.perks;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitScheduler;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.structures.Killstreak;
import pb.ajneb97.structures.PaintballPlayer;
import pb.ajneb97.structures.game.Game;
import pb.ajneb97.utils.enums.GameState;

public class CooldownKillstreaks {

    int taskID;
    int tiempo;
    private PaintballPlayer jugador;
    private Game partida;
    private PaintballBattle plugin;

    public CooldownKillstreaks(PaintballBattle plugin) {
        this.plugin = plugin;
    }

    public void cooldownKillstreak(final PaintballPlayer jugador, final Game partida, final String nombre, int tiempo) {
        this.jugador = jugador;
        this.partida = partida;
        this.tiempo = tiempo;
        if (nombre.equalsIgnoreCase("fury")) {
            CooldownKillstreaks c = new CooldownKillstreaks(plugin);
            c.cooldownParticulasFury(jugador, partida);
        }
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
//        taskID = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
//            public void run() {
//                if (!ejecutarCooldownKillstreak(nombre)) {
//                    YamlDocument messages = plugin.getMessagesDocument();
//                    YamlDocument config = plugin.getConfigDocument();
//                    if (!partida.getState().equals(GameState.ENDING)) {
//                        String name = MessageUtils.translateColor(config.getString("killstreaks_items." + nombre + ".name"));
//                        jugador.getPlayer().sendMessage(MessageUtils.translateColor(messages.getString("killstreakExpired").replace("%killstreak%", name)));
//                        String[] separados = config.getString("expireKillstreakSound").split(";");
//                        try {
//                            Sound sound = Sound.valueOf(separados[0]);
//                            jugador.getPlayer().playSound(jugador.getPlayer().getLocation(), sound, Float.valueOf(separados[1]), Float.valueOf(separados[2]));
//                        } catch (Exception ex) {
//                            Bukkit.getConsoleSender().sendMessage(MessageUtils.translateColor(PaintballBattle.prefix + "&7Sound Name: &c" + separados[0] + " &7is not valid."));
//                        }
//                    }
//
//                    Bukkit.getScheduler().cancelTask(taskID);
//                    return;
//                }
//            }
//        }, 0L, 20L);
    }

    protected boolean ejecutarCooldownKillstreak(String nombre) {
        if (partida != null && partida.getState().equals(GameState.PLAYING)) {
            if (tiempo <= 0) {
                jugador.removerKillstreak(nombre);
                return false;
            } else {
                Killstreak k = jugador.getKillstreak(nombre);
                if (k != null) {
                    tiempo--;
                    k.setTime(tiempo);
                    return true;
                } else {
                    jugador.removerKillstreak(nombre);
                    return false;
                }
            }
        } else {
            jugador.removerKillstreak(nombre);
            return false;
        }
    }

    public void cooldownParticulasFury(final PaintballPlayer jugador, final Game partida) {
        this.jugador = jugador;
        this.partida = partida;
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                if (!ejecutarParticulasFury()) {
                    Bukkit.getScheduler().cancelTask(taskID);
                    return;
                }
            }
        }, 0L, 5L);
    }

    protected boolean ejecutarParticulasFury() {
        if (partida != null && partida.getState().equals(GameState.PLAYING)) {
            if (jugador != null) {
                if (jugador.getKillstreak("fury") != null) {
                    Location l = jugador.getPlayer().getLocation().clone();
                    l.setY(l.getY() + 1.5);
                    if (Bukkit.getVersion().contains("1.8")) {
                        l.getWorld().playEffect(l, Effect.valueOf("VILLAGER_THUNDERCLOUD"), 1);
                    } else {
                        l.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, l, 1);
                    }
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void cooldownNuke(final PaintballPlayer jugador, final Game partida, final String[] separados1, final String[] separados2) {
//        this.jugador = jugador;
//        this.partida = partida;
//        this.tiempo = 5;
//        final YamlDocument messages = plugin.getMessagesDocument();
//        for (PaintballPlayer player : partida.getPlayers()) {
//            player.getPlayer().sendMessage(MessageUtils.translateColor(messages.getString("nukeImpact").replace("%time%", tiempo + "")));
//        }
//        tiempo--;
//        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
//        taskID = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
//            public void run() {
//                if (!ejecutarNuke(separados1, separados2, messages)) {
//                    Bukkit.getScheduler().cancelTask(taskID);
//                    return;
//                }
//            }
//        }, 20L, 20L);
    }

    protected boolean ejecutarNuke(String[] separados1, String[] separados2, YamlDocument messages) {
//        if (partida != null && partida.getState().equals(GameState.PLAYING)) {
//            if (jugador != null) {
//                if (tiempo <= 0) {
//                    try {
//                        Sound sound = Sound.valueOf(separados2[0]);
//                        if (separados2.length >= 4) {
//                            if (separados2[3].equalsIgnoreCase("global")) {
//                                for (PaintballPlayer player : partida.getPlayers()) {
//                                    player.getPlayer().playSound(player.getPlayer().getLocation(), sound, Float.valueOf(separados2[1]), Float.valueOf(separados2[2]));
//                                }
//                            } else {
//                                jugador.getPlayer().playSound(jugador.getPlayer().getLocation(), sound, Float.valueOf(separados2[1]), Float.valueOf(separados2[2]));
//                            }
//                        } else {
//                            jugador.getPlayer().playSound(jugador.getPlayer().getLocation(), sound, Float.valueOf(separados2[1]), Float.valueOf(separados2[2]));
//                        }
//                    } catch (Exception ex) {
//                        Bukkit.getConsoleSender().sendMessage(MessageUtils.translateColor(PaintballBattle.prefix + "&7Sound Name: &c" + separados2[0] + " &7is not valid."));
//                    }
//                    for (PaintballPlayer player : partida.getPlayers()) {
//                        PartidaManager.muereJugador(partida, jugador, player, plugin, false, true);
//                    }
//                    partida.setNuke(false);
//                    return false;
//                } else {
//                    try {
//                        Sound sound = Sound.valueOf(separados1[0]);
//                        if (separados1.length >= 4) {
//                            if (separados1[3].equalsIgnoreCase("global")) {
//                                for (PaintballPlayer player : partida.getPlayers()) {
//                                    player.getPlayer().playSound(player.getPlayer().getLocation(), sound, Float.valueOf(separados1[1]), Float.valueOf(separados1[2]));
//                                }
//                            } else {
//                                jugador.getPlayer().playSound(jugador.getPlayer().getLocation(), sound, Float.valueOf(separados1[1]), Float.valueOf(separados1[2]));
//                            }
//                        } else {
//                            jugador.getPlayer().playSound(jugador.getPlayer().getLocation(), sound, Float.valueOf(separados1[1]), Float.valueOf(separados1[2]));
//                        }
//                    } catch (Exception ex) {
//                        Bukkit.getConsoleSender().sendMessage(MessageUtils.translateColor(PaintballBattle.prefix + "&7Sound Name: &c" + separados1[0] + " &7is not valid."));
//                    }
//                    for (PaintballPlayer player : partida.getPlayers()) {
//                        player.getPlayer().sendMessage(MessageUtils.translateColor(messages.getString("nukeImpact").replace("%time%", tiempo + "")));
//                    }
//                    tiempo--;
//                    return true;
//                }
//            } else {
//                return false;
//            }
//        } else {
//            return false;
//        }
        return false;
    }
}
