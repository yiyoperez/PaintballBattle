package pb.ajneb97.structures;

import java.util.ArrayList;

public class PaintballPlayerData {

    private int wins;
    private int loses;
    private int ties;
    private int kills;
    private ArrayList<Hat> hats;
    private ArrayList<Perk> perks;

    public PaintballPlayerData(int wins, int loses, int ties, int kills, ArrayList<Hat> hats, ArrayList<Perk> perks) {
        this.wins = wins;
        this.loses = loses;
        this.kills = kills;
        this.ties = ties;
        this.hats = hats;
        this.perks = perks;
    }

    public int getWins() {
        return wins;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLoses() {
        return loses;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    public int getTies() {
        return ties;
    }

    public void setTies(int ties) {
        this.ties = ties;
    }

    public ArrayList<Hat> getHats() {
        return hats;
    }

    public void setHats(ArrayList<Hat> hats) {
        this.hats = hats;
    }

    public ArrayList<Perk> getPerks() {
        return perks;
    }

    public void setPerks(ArrayList<Perk> perks) {
        this.perks = perks;
    }
}