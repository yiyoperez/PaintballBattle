package pb.ajneb97.managers;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.structures.Game;
import pb.ajneb97.tasks.SignUpdateTask;
import pb.ajneb97.utils.LocationUtils;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SignManager {

    @Inject
    @Named("signs")
    private YamlDocument signsDocument;
    @Inject
    @Named("messages")
    private YamlDocument messages;
    @Inject
    private GameManager gameManager;

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

        this.task = new SignUpdateTask(messages, this, gameManager)
                .runTaskTimer(plugin, 0, 20L);
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
