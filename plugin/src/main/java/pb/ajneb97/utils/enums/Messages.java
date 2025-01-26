package pb.ajneb97.utils.enums;

public enum Messages {

    HELP_MESSAGE("MAIN.HELP"), NO_PERMISSION("NO_PERMISSION"), RELOADED("RELOADED");

    private final String path;

    Messages(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}