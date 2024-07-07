package pb.ajneb97.managers.players;

import org.bukkit.configuration.file.FileConfiguration;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.PlayerConfig;
import pb.ajneb97.api.Hat;
import pb.ajneb97.api.Perk;
import pb.ajneb97.database.JugadorDatos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayerDataManager {

    private final PaintballBattle plugin;
    private ArrayList<PlayerConfig> configPlayers = new ArrayList<PlayerConfig>();
    private ArrayList<JugadorDatos> jugadoresDatos = new ArrayList<JugadorDatos>();

    public PlayerDataManager(PaintballBattle plugin) {
        this.plugin = plugin;
    }

    public void savePlayers() {
        for (int i = 0; i < configPlayers.size(); i++) {
            configPlayers.get(i).savePlayerConfig();
        }
    }

    public void registerPlayers() {
        String path = plugin.getDataFolder() + File.separator + "players";
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String pathName = listOfFiles[i].getName();
                PlayerConfig config = new PlayerConfig(pathName, plugin);
                config.registerPlayerConfig();
                configPlayers.add(config);
            }
        }
    }

    public ArrayList<PlayerConfig> getConfigPlayers() {
        return this.configPlayers;
    }

    public boolean archivoYaRegistrado(String pathName) {
        for (int i = 0; i < configPlayers.size(); i++) {
            if (configPlayers.get(i).getPath().equals(pathName)) {
                return true;
            }
        }
        return false;
    }

    public PlayerConfig getPlayerConfig(String pathName) {
        for (int i = 0; i < configPlayers.size(); i++) {
            if (configPlayers.get(i).getPath().equals(pathName)) {
                return configPlayers.get(i);
            }
        }
        return null;
    }

    public ArrayList<PlayerConfig> getPlayerConfigs() {
        return this.configPlayers;
    }

    public boolean registerPlayer(String pathName) {
        if (!archivoYaRegistrado(pathName)) {
            PlayerConfig config = new PlayerConfig(pathName, plugin);
            config.registerPlayerConfig();
            configPlayers.add(config);
            return true;
        } else {
            return false;
        }
    }

    public void removerConfigPlayer(String path) {
        for (int i = 0; i < configPlayers.size(); i++) {
            if (configPlayers.get(i).getPath().equals(path)) {
                configPlayers.remove(i);
            }
        }
    }

    //TODO
    public void cargarJugadores() {
        for (PlayerConfig playerConfig : configPlayers) {
            FileConfiguration players = playerConfig.getConfig();
            String jugador = players.getString("name");
            int kills = 0;
            int wins = 0;
            int loses = 0;
            int ties = 0;
            int coins = 0;

            if (players.contains("kills")) {
                kills = Integer.valueOf(players.getString("kills"));
            }
            if (players.contains("wins")) {
                wins = Integer.valueOf(players.getString("wins"));
            }
            if (players.contains("loses")) {
                loses = Integer.valueOf(players.getString("loses"));
            }
            if (players.contains("ties")) {
                ties = Integer.valueOf(players.getString("ties"));
            }
            if (players.contains("coins")) {
                coins = Integer.valueOf(players.getString("coins"));
            }
            ArrayList<Perk> perks = new ArrayList<Perk>();
            if (players.contains("perks")) {
                List<String> listaPerks = players.getStringList("perks");
                for (int i = 0; i < listaPerks.size(); i++) {
                    String[] separados = listaPerks.get(i).split(";");
                    Perk p = new Perk(separados[0], Integer.valueOf(separados[1]));
                    perks.add(p);
                }
            }
            ArrayList<Hat> hats = new ArrayList<Hat>();
            if (players.contains("hats")) {
                List<String> listaHats = players.getStringList("hats");
                for (int i = 0; i < listaHats.size(); i++) {
                    String[] separados = listaHats.get(i).split(";");
                    Hat h = new Hat(separados[0], Boolean.valueOf(separados[1]));
                    hats.add(h);
                }
            }


            this.agregarJugadorDatos(new JugadorDatos(jugador, playerConfig.getPath().replace(".yml", ""), wins, loses, ties, kills, coins, perks, hats));
        }
    }

    public void guardarJugadores() {
        for (JugadorDatos j : jugadoresDatos) {
            String jugador = j.getName();
            PlayerConfig playerConfig = getPlayerConfig(j.getUUID() + ".yml");
            FileConfiguration players = playerConfig.getConfig();
            players.set("name", jugador);
            players.set("kills", j.getKills());
            players.set("wins", j.getWins());
            players.set("ties", j.getTies());
            players.set("loses", j.getLoses());
            players.set("coins", j.getCoins());

            List<String> listaPerks = new ArrayList<String>();
            ArrayList<Perk> perks = j.getPerks();
            for (Perk p : perks) {
                listaPerks.add(p.getName() + ";" + p.getNivel());
            }
            players.set("perks", listaPerks);

            List<String> listaHats = new ArrayList<String>();
            ArrayList<Hat> hats = j.getHats();
            for (Hat h : hats) {
                listaHats.add(h.getName() + ";" + h.isSelected());
            }
            players.set("hats", listaHats);
        }
        savePlayers();
    }

    public void agregarJugadorDatos(JugadorDatos jugador) {
        jugadoresDatos.add(jugador);
    }

    public JugadorDatos getJugador(String jugador) {
        for (JugadorDatos j : jugadoresDatos) {
            if (j != null && j.getName() != null && j.getName().equals(jugador)) {
                return j;
            }
        }
        return null;
    }

    public ArrayList<JugadorDatos> getJugadores() {
        return this.jugadoresDatos;
    }
}
