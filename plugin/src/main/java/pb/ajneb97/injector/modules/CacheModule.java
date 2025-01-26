package pb.ajneb97.injector.modules;

import pb.ajneb97.commons.cache.BaseCache;
import pb.ajneb97.commons.cache.Cache;
import pb.ajneb97.structures.GameItem;
import pb.ajneb97.structures.PaintballPlayer;
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
    }
}
