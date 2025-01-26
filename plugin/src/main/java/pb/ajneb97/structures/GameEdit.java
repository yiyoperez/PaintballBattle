package pb.ajneb97.structures;

import org.bukkit.entity.Player;
import pb.ajneb97.utils.enums.EditStep;

public class GameEdit {

    private final Player player;
    private final Game arena;
    private EditStep step;

    public GameEdit(Player player, Game arena) {
        this.player = player;
        this.arena = arena;
        this.step = EditStep.UNKNOWN;
    }

    public Player getPlayer() {
        return player;
    }

    public Game getArena() {
        return arena;
    }

    public EditStep getStep() {
        return step;
    }

    public void setStep(EditStep step) {
        this.step = step;
    }
}
