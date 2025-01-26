package pb.ajneb97.commons.service;

public interface Service {

    /**
     * Starts service.
     */
    void start();

    /**
     * Stops service
     */
    default void finish() {
        //Nothing by default.
    }
}
