package pb.ajneb97.injector.services;

import dev.dejvokep.boostedyaml.YamlDocument;
import pb.ajneb97.commons.service.Service;
import pb.ajneb97.core.logger.Logger;
import pb.ajneb97.structures.Game;
import pb.ajneb97.managers.GameManager;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public final class GameService implements Service {

    @Inject
    @Named("arena-path")
    private Path arenasPath;
    @Inject
    private GameManager gameManager;

    @Override
    public void start() {
        File[] files = arenasPath.toFile().listFiles();
        if (files == null) return;

        for (File file : files) {
            String id = file.getName().split("\\.")[0];
            Logger.info("Loaded arena " + id);

            YamlDocument document;
            try {
                document = YamlDocument.create(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            gameManager.addGame(new Game(document.getStringRouteMappedValues(false)));
        }
    }

    @Override
    public void finish() {
        if (gameManager.getGames().isEmpty()) return;

        Logger.info("Attempting to save game arenas.");
        gameManager.getGames().forEach(game -> {
            try {
                YamlDocument doc = YamlDocument.create(new File(arenasPath.resolve(game.getName() + ".yml").toUri()));
                game.serialize().forEach((doc::set));
                doc.save();
            } catch (IOException e) {
                throw new RuntimeException("An error occurred trying to save game data " + e.getMessage());
            }
        });

        gameManager.getGames().clear();
    }
}
