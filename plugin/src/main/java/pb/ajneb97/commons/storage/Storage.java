package pb.ajneb97.commons.storage;

import java.util.Optional;

public interface Storage<T> {


    Optional<T> find(String unique);

    default void loadAll() {
        //
    }

    default void saveAll() {
        //
    }

    void save(T object);

    void delete(String unique);

    boolean exists(String unique);
}
