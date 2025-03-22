package pb.ajneb97.injector.modules;

import pb.ajneb97.commons.cache.BaseCache;
import pb.ajneb97.commons.cache.Cache;
import pb.ajneb97.structures.PaintballPlayer;
import pb.ajneb97.structures.game.GameEditSessionBuilder;
import pb.ajneb97.structures.game.GameItem;
import pb.ajneb97.structures.scoreboard.ScoreboardHelper;
import team.unnamed.inject.AbstractModule;
import team.unnamed.inject.key.TypeReference;

import java.util.UUID;

public final class CacheModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeReference<Cache<UUID, PaintballPlayer>>() {
        }).named("player-cache").toInstance(new BaseCache<>());

        bind(new TypeReference<Cache<String, GameItem>>() {
        }).named("item-cache").toInstance(new BaseCache<>());

        bind(new TypeReference<Cache<UUID, ScoreboardHelper>>() {
        }).named("board-cache").toInstance(new BaseCache<>());

        bind(new TypeReference<Cache<UUID, GameEditSessionBuilder>>() {
        }).named("edit-session-cache").toInstance(new BaseCache<>());
    }
}
