package pb.ajneb97.structures.game;


import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class SavedElements {

    private final GameMode gameMode;
    private final ItemStack[] inventoryContent;
    private final ItemStack[] armorContent;

    private final int level;
    private final int hunger;
    private final float experience;

    private final double health;
    private final double maxHealth;

    private final boolean isFlying;
    private final boolean allowFlight;

    public SavedElements(Player player) {
        this.gameMode = player.getGameMode();
        this.inventoryContent = player.getInventory().getContents().clone();
        this.armorContent = player.getEquipment().getArmorContents().clone();

        this.level = player.getLevel();
        this.hunger = player.getFoodLevel();
        this.experience = player.getExp();

        this.health = player.getHealth();
        this.maxHealth = player.getMaxHealth();

        this.isFlying = player.isFlying();
        this.allowFlight = player.getAllowFlight();
    }

    public void restorePlayerElements(Player player) {
        player.setGameMode(gameMode);
        player.getInventory().setContents(inventoryContent);
        player.getEquipment().setArmorContents(armorContent);

        player.setLevel(level);
        player.setFoodLevel(hunger);
        player.setExp(experience);

        player.setHealth(health);
        player.setMaxHealth(maxHealth);

        player.setFlying(isFlying);
        player.setAllowFlight(allowFlight);
    }
}
