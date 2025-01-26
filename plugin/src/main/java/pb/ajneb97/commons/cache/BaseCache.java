package pb.ajneb97.commons.cache;

import java.util.HashMap;
import java.util.Map;

public class BaseCache<K, V> implements Cache<K, V> {

    private final Map<K, V> cacheMap;

    public BaseCache() {
        this.cacheMap = new HashMap<>();
    }

    @Override
    public Map<K, V> get() {
        return this.cacheMap;
    }

}