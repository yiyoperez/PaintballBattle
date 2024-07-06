package pb.ajneb97.juego;

import org.bukkit.entity.Player;

public class GameEdit {

    private Player player;
    private Partida match;
    private String step;

    public GameEdit(Player player, Partida match) {
        this.player = player;
        this.match = match;
        this.step = "";
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Partida getMatch() {
        return match;
    }

    public void setMatch(Partida match) {
        this.match = match;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getStep() {
        return this.step;
    }
}
