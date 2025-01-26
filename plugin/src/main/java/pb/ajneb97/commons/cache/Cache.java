package pb.ajneb97.commons.cache;

import java.util.Map;
import java.util.Optional;

public interface Cache<K, V> {

    /**
     * @return all cached objects using this Map
     */

    Map<K, V> get();

    /**
     * Obtain an object registered in the cache using its 'key / identifier' in order to obtain it
     *
     * @param id identifier to get the object
     */

    default Optional<V> find(K id) {
        return Optional.ofNullable(get().get(id));
    }

    /**
     * Delete an object registered in the server cache
     *
     * @param id to get the object
     */

    default void remove(K id) {
        get().remove(id);
    }

    /**
     * Add an object to the server cache
     *
     * @param id     identifier of the object to register in the cache
     * @param object to be registered in the server cache
     */

    default void add(K id, V object) {
        get().put(id, object);
    }

    /**
     * @param id to get the object
     *
     * @return if the object exists or is registered in the server cache
     */

    default boolean exists(K id) {
        return get().containsKey(id);
    }
}