package pb.ajneb97.managers;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.structures.game.Game;
import pb.ajneb97.tasks.SignUpdateTask;
import pb.ajneb97.utils.LocationUtils;
import team.unnamed.inject.Inject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SignManager {

    @Inject
    private GameManager gameManager;
    @Inject
    private MessageHandler messageHandler;

    private BukkitTask task;
    private final Map<String, Set<Location>> locationMap = new HashMap<>();

    public void addSignLocation(Game partida, Location location) {
        locationMap.computeIfAbsent(partida.getName(), k -> new HashSet<>()).add(location);
    }

    public boolean isSignLocation(Location location) {
        return getLocationMap()
                .values()
                .stream()
                .findFirst()
                .filter(locationSet -> locationSet.contains(location))
                .isPresent();
    }

    public Map<String, Set<Location>> getLocationMap() {
        return locationMap;
    }

    public void runTask(PaintballBattle plugin) {
        if (task != null) {
            task.cancel();
        }

        this.task = new SignUpdateTask(this, gameManager, messageHandler)
                .runTaskTimer(plugin, 0, 60L);
    }

    public void cancelTask() {
        if (task == null) return;
        task.cancel();
    }

    public Set<Location> getLocationsSet() {
        Set<Location> locationSet = new HashSet<>();
        locationMap.values().forEach(locationSet::addAll);
        return locationSet;
    }

    public void removeLocation(Location location) {
        locationMap.values().forEach(locationSet ->
                locationSet.stream()
                        .filter(loc -> LocationUtils.isSameBlockLocation(loc, location))
                        .findFirst()
                        .ifPresent(locationSet::remove));
    }
}
