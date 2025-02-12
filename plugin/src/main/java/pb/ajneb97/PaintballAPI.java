package pb.ajneb97;

import dev.dejvokep.boostedyaml.YamlDocument;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.structures.Game;
import pb.ajneb97.utils.enums.Messages;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

public final class PaintballAPI {

    @Inject
    private PaintballBattle plugin;
    @Inject
    @Named("config")
    private YamlDocument config;
    @Inject
    private GameManager gameManager;
    @Inject
    private MessageHandler messageHandler;


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

//    public static int getCoins(Player player) {
//        JugadorDatos j = playerDataManager.getJugador(player.getName());
//        if (j != null) {
//            return j.getCoins();
//        } else {
//            return 0;
//        }
//    }
//
//    public static void addCoins(Player player, int coins) {
//        JugadorDatos j = playerDataManager.getJugador(player.getName());
//        if (j != null) {
//            j.aumentarCoins(coins);
//        }
//    }
//
//    public static void removeCoins(Player player, int coins) {
//        JugadorDatos j = playerDataManager.getJugador(player.getName());
//        if (j != null) {
//            j.disminuirCoins(coins);
//        }
//    }
//
//    public static int getWins(Player player) {
//        JugadorDatos j = playerDataManager.getJugador(player.getName());
//        if (j != null) {
//            return j.getWins();
//        } else {
//            return 0;
//        }
//    }
//
//    public static int getLoses(Player player) {
//        JugadorDatos j = playerDataManager.getJugador(player.getName());
//        if (j != null) {
//            return j.getLoses();
//        } else {
//            return 0;
//        }
//
//    }
//
//    public static int getTies(Player player) {
//        JugadorDatos j = playerDataManager.getJugador(player.getName());
//        if (j != null) {
//            return j.getTies();
//        } else {
//            return 0;
//        }
//    }
//
//    public static int getKills(Player player) {
//        JugadorDatos j = playerDataManager.getJugador(player.getName());
//        if (j != null) {
//            return j.getKills();
//        } else {
//            return 0;
//        }
//
//    }
//
//    public static int getPerkLevel(Player player, String perk) {
//        JugadorDatos j = playerDataManager.getJugador(player.getName());
//        if (j != null) {
//            return j.getNivelPerk(perk);
//        } else {
//            return 0;
//        }
//    }
//
//    public static boolean hasHat(Player player, String hat) {
//        JugadorDatos j = playerDataManager.getJugador(player.getName());
//        if (j != null) {
//            return j.tieneHat(hat);
//        } else {
//            return false;
//        }
//    }
//
//    public static boolean hasHatSelected(Player player, String hat) {
//        JugadorDatos j = playerDataManager.getJugador(player.getName());
//        if (j != null) {
//            return j.tieneHatSeleccionado(hat);
//        } else {
//            return false;
//        }
//    }
//
//    public static ArrayList<Perk> getPerks(Player player) {
//        JugadorDatos j = playerDataManager.getJugador(player.getName());
//        if (j != null) {
//            return j.getPerks();
//        } else {
//            return new ArrayList<Perk>();
//        }
//    }
//
//    public static ArrayList<Hat> getHats(Player player) {
//        JugadorDatos j = playerDataManager.getJugador(player.getName());
//        if (j != null && j.getHats() != null) {
//            return j.getHats();
//        } else {
//            return new ArrayList<Hat>();
//        }
//    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public int getPlayersArena(String arenaName) {
        if (!gameManager.gameExists(arenaName)) {
            return 0;
        }

        Game partida = getGameManager().getGame(arenaName);
        return partida.getCurrentPlayersSize();
    }

    public String getStatusArena(String arenaName) {
        if (!gameManager.gameExists(arenaName)) {
            return "UNKNOWN";
        }

        Game match = getGameManager().getGame(arenaName);
        return switch (match.getState()) {
            case WAITING -> messageHandler.getMessage(Messages.SIGN_STATUS_WAITING);
            case STARTING -> messageHandler.getMessage(Messages.SIGN_STATUS_STARTING);
            case PLAYING -> messageHandler.getMessage(Messages.SIGN_STATUS_INGAME);
            case ENDING -> messageHandler.getMessage(Messages.SIGN_STATUS_FINISHING);
            default -> messageHandler.getMessage(Messages.SIGN_STATUS_DISABLED);
        };
    }
}
