package pb.ajneb97.structures;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import pb.ajneb97.structures.game.SavedElements;

import java.util.ArrayList;


public class PaintballPlayer {

    private Player player;
    private int kills;
    private int deaths;
    private SavedElements savedElements;
    private boolean recentDeath;
    private String teamChoice;
    private int coins;
    private ArrayList<Killstreak> killstreaks;
    private Location deathLocation;

    private String selectedHat;
    private boolean efectoHatActivado;
    private boolean efectoHatEnCooldown;
    private int tiempoEfectoHat;
    private String lastKilledBy;

    public PaintballPlayer(Player player) {
        this.player = player;
        this.savedElements = new SavedElements(player);
        this.recentDeath = false;
        this.deaths = 0;
        this.kills = 0;
        this.coins = 0;
        this.killstreaks = new ArrayList<>();
        this.efectoHatActivado = false;
        this.efectoHatEnCooldown = false;
        this.tiempoEfectoHat = 0;
        this.selectedHat = "";
    }

    public String getLastKilledBy() {
        return lastKilledBy;
    }

    public void setLastKilledBy(String lastKilledBy) {
        this.lastKilledBy = lastKilledBy;
    }

    public boolean isEfectoHatActivado() {
        return efectoHatActivado;
    }

    public void setEfectoHatActivado(boolean efectoHatActivado) {
        this.efectoHatActivado = efectoHatActivado;
    }

    public boolean isEfectoHatEnCooldown() {
        return efectoHatEnCooldown;
    }

    public void setEfectoHatEnCooldown(boolean efectoHatEnCooldown) {
        this.efectoHatEnCooldown = efectoHatEnCooldown;
    }

    public int getTiempoEfectoHat() {
        return tiempoEfectoHat;
    }

    public void setTiempoEfectoHat(int tiempoEfectoHat) {
        this.tiempoEfectoHat = tiempoEfectoHat;
    }

    public void setSelectedHat(String hat) {
        this.selectedHat = hat;
    }

    public String getSelectedHat() {
        return this.selectedHat;
    }

    public void setDeathLocation(Location l) {
        this.deathLocation = l;
    }

    public Location getDeathLocation() {
        return this.deathLocation;
    }

    public void agregarKillstreak(Killstreak k) {
        this.killstreaks.add(k);
    }

    public Killstreak getKillstreak(String tipo) {
        for (Killstreak k : this.killstreaks) {
            if (k.getType().equals(tipo)) {
                return k;
            }
        }
        return null;
    }

    public void removerKillstreak(String tipo) {
        killstreaks.removeIf(killstreak -> killstreak.getType().equals(tipo));
    }

    public Killstreak getUltimaKillstreak() {
        if (killstreaks.isEmpty()) return null;

        return killstreaks.get(killstreaks.size() - 1);
    }

    public int getCoins() {
        return this.coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void increaseCoins(int amount) {
        this.coins = coins + amount;
    }

    public void decreaseCoins(int amount) {
        this.coins = coins - amount;
    }

    public void setTeamChoice(String team) {
        this.teamChoice = team;
    }

    public String getTeamChoice() {
        return this.teamChoice;
    }

    public SavedElements getSavedElements() {
        return this.savedElements;
    }

    public void increaseKills() {
        this.kills++;
    }

    public void increaseDeaths() {
        this.deaths++;
    }

    public int getKills() {
        return this.kills;
    }

    public int getDeaths() {
        return this.deaths;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setRecentDeath(boolean recentDeath) {
        this.recentDeath = recentDeath;
    }

    public boolean isRecentDeath() {
        return this.recentDeath;
    }

}
