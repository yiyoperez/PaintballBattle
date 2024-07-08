package pb.ajneb97.managers.signs;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.utils.LocationUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SignManager {

    private BukkitTask task;
    private final YamlDocument signsDocument;
    private final Map<String, Location> locationMap = new HashMap<>();

    public SignManager(PaintballBattle plugin) {
        try {
            this.signsDocument = YamlDocument.create(new File(plugin.getDataFolder(), "signs.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Location> getLocationMap() {
        return locationMap;
    }

    public void loadSavedSigns() {
        Set<String> routes = signsDocument.getRoutesAsStrings(false);
        if (routes.isEmpty()) return;

        routes.forEach(arena ->
                locationMap.put(arena, LocationUtils.deserialize(signsDocument.getString(arena))));
    }

    // TODO: This only allows 1 sign per arena.
    //  needs refactor to allow multiples signs?
    public void saveLoadedSigns() {
        if (locationMap.isEmpty()) return;

        locationMap.forEach((arena, location) ->
                signsDocument.set(arena, LocationUtils.serialize(location)));

        try {
            signsDocument.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void runTask(PaintballBattle plugin) {
        if (task != null) {
            task.cancel();
        }

        this.task = new SignUpdateRunnable(plugin).runTaskTimer(plugin, 0, 20L);
    }

    public void cancelTask() {
        if (task == null) return;
        task.cancel();
    }

}
