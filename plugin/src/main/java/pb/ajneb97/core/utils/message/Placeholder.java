package pb.ajneb97.core.utils.message;

public class Placeholder {

    private final String replaced;
    private final String replacement;

    public Placeholder(final String replaced, final String replacement) {
        this.replaced = replaced;
        this.replacement = replacement;
    }

    public Placeholder(final String replaced, final Number amount) {
        this.replaced = replaced;
        this.replacement = String.valueOf(amount);
    }

    public String getReplaced() {
        return replaced;
    }

    public String getReplacement() {
        return replacement;
    }
}
