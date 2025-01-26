package pb.ajneb97.injector.services;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Location;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.commons.service.Service;
import pb.ajneb97.managers.SignManager;
import pb.ajneb97.utils.LocationUtils;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public final class SignsService implements Service {

    @Inject
    private PaintballBattle plugin;
    @Inject
    private SignManager signManager;
    @Inject
    @Named("signs")
    private YamlDocument signsDocument;

    @Override
    public void start() {
        signManager.runTask(plugin);

        Set<String> routes = signsDocument.getRoutesAsStrings(false);
        if (routes.isEmpty()) return;

        routes.forEach(arena -> {
            Set<Location> signLocations = signsDocument.getStringList(arena).stream()
                    .map(LocationUtils::deserializeBlockLocation)
                    .collect(Collectors.toSet());
            signManager.getLocationMap().put(arena, signLocations);
        });
    }

    @Override
    public void finish() {
        signManager.cancelTask();

        signsDocument.clear();

        if (!signManager.getLocationMap().isEmpty()) {
            signManager.getLocationMap().forEach((arena, locationSet) ->
                    signsDocument.set(arena, locationSet.stream()
                            .map(LocationUtils::serializeBlockLocation)
                            .collect(Collectors.toList())));
        }

        try {
            signsDocument.save();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save signs document", e);
        }
    }
}
