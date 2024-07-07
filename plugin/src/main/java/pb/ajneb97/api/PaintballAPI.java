package pb.ajneb97.api;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.entity.Player;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.database.JugadorDatos;
import pb.ajneb97.juego.GameState;
import pb.ajneb97.juego.Partida;
import pb.ajneb97.managers.game.GameManager;
import pb.ajneb97.managers.players.PlayerDataManager;

import java.util.ArrayList;

public class PaintballAPI {

    private static PaintballBattle plugin;
    private static YamlDocument config;
    private static PlayerDataManager playerDataManager;

    public PaintballAPI(PaintballBattle plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigDocument();
        //TODO
        this.playerDataManager = new PlayerDataManager(plugin);
    }

//	public static JugadorDatos getPaintballDatos(Player player) {
//		if(!MySQL.isEnabled(config)) {
//			JugadorDatos j = plugin.getJugador(player.getName());
//			if(j != null) {
//				return new JugadorPaintballDatos(j.getWins(),j.getLoses(),j.getTies(),j.getKills(),j.getHats(),j.getPerks());
//			}else {
//				return new JugadorPaintballDatos(0,0,0,0,new ArrayList<Hat>(),new ArrayList<Perk>());
//			}
//		}else {
//			return MySQL.getStatsTotales(plugin, player.getName(),"Coins");
//		}
//	}

    public static int getCoins(Player player) {
        JugadorDatos j = playerDataManager.getJugador(player.getName());
        if (j != null) {
            return j.getCoins();
        } else {
            return 0;
        }
    }

    public static void addCoins(Player player, int coins) {
        JugadorDatos j = playerDataManager.getJugador(player.getName());
        if (j != null) {
            j.aumentarCoins(coins);
        }
    }

    public static void removeCoins(Player player, int coins) {
        JugadorDatos j = playerDataManager.getJugador(player.getName());
        if (j != null) {
            j.disminuirCoins(coins);
        }
    }

    public static int getWins(Player player) {
        JugadorDatos j = playerDataManager.getJugador(player.getName());
        if (j != null) {
            return j.getWins();
        } else {
            return 0;
        }
    }

    public static int getLoses(Player player) {
        JugadorDatos j = playerDataManager.getJugador(player.getName());
        if (j != null) {
            return j.getLoses();
        } else {
            return 0;
        }

    }

    public static int getTies(Player player) {
        JugadorDatos j = playerDataManager.getJugador(player.getName());
        if (j != null) {
            return j.getTies();
        } else {
            return 0;
        }
    }

    public static int getKills(Player player) {
        JugadorDatos j = playerDataManager.getJugador(player.getName());
        if (j != null) {
            return j.getKills();
        } else {
            return 0;
        }

    }

    public static int getPerkLevel(Player player, String perk) {
        JugadorDatos j = playerDataManager.getJugador(player.getName());
        if (j != null) {
            return j.getNivelPerk(perk);
        } else {
            return 0;
        }
    }

    public static boolean hasHat(Player player, String hat) {
        JugadorDatos j = playerDataManager.getJugador(player.getName());
        if (j != null) {
            return j.tieneHat(hat);
        } else {
            return false;
        }
    }

    public static boolean hasHatSelected(Player player, String hat) {
        JugadorDatos j = playerDataManager.getJugador(player.getName());
        if (j != null) {
            return j.tieneHatSeleccionado(hat);
        } else {
            return false;
        }
    }

    public static ArrayList<Perk> getPerks(Player player) {
        JugadorDatos j = playerDataManager.getJugador(player.getName());
        if (j != null) {
            return j.getPerks();
        } else {
            return new ArrayList<Perk>();
        }
    }

    public static ArrayList<Hat> getHats(Player player) {
        JugadorDatos j = playerDataManager.getJugador(player.getName());
        if (j != null && j.getHats() != null) {
            return j.getHats();
        } else {
            return new ArrayList<Hat>();
        }
    }

    public static GameManager getGameManager() {
        return plugin.getGameManager();
    }

    public static int getPlayersArena(String arena) {
        Partida partida = getGameManager().getPartida(arena);
        if (partida != null) {
            return partida.getCantidadActualJugadores();
        } else {
            return 0;
        }
    }

    public static String getStatusArena(String arena) {
        Partida partida = getGameManager().getPartida(arena);
        YamlDocument messages = plugin.getMessagesDocument();
        if (partida != null) {
            if (partida.getEstado().equals(GameState.STARTING)) {
                return messages.getString("signStatusStarting");
            } else if (partida.getEstado().equals(GameState.WAITING)) {
                return messages.getString("signStatusWaiting");
            } else if (partida.getEstado().equals(GameState.PLAYING)) {
                return messages.getString("signStatusIngame");
            } else if (partida.getEstado().equals(GameState.ENDING)) {
                return messages.getString("signStatusFinishing");
            } else {
                return messages.getString("signStatusDisabled");
            }
        } else {
            return null;
        }
    }
}
