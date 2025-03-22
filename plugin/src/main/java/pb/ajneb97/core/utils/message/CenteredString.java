package pb.ajneb97.core.utils.message;

public class CenteredString {

    private static final int TARGET_WIDTH = 154; // Target width for centering
    private static final int SPACE_LENGTH = DefaultFontInfo.SPACE.getLength() + 1; // Length of a space character

    public static String formatMessage(String message) {
        if (message == null || message.isEmpty()) return "";

        // Calculate the pixel width of the message
        int messagePxSize = calculateMessageWidth(message);

        // Calculate the number of spaces needed for alignment
        int spacesToAdd = calculateSpacesToAdd(messagePxSize);

        // Build the formatted message with leading spaces
        return " ".repeat(Math.max(0, spacesToAdd)) + message;
    }

    private static int calculateMessageWidth(String message) {
        int messagePxSize = 0;
        boolean isBold = false;

        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);

            if (c == '§' && i + 1 < message.length()) {
                // Handle format codes (e.g., bold)
                char formatCode = message.charAt(i + 1);
                isBold = formatCode == 'l' || formatCode == 'L';
                i++; // Skip the format code character
                continue; // Skip to the next character
            }

            // Calculate character width
            DefaultFontInfo fontInfo = DefaultFontInfo.getDefaultFontInfo(c);
            messagePxSize += isBold ? fontInfo.getBoldLength() : fontInfo.getLength();
            messagePxSize++; // Add 1 pixel for spacing
        }

        return messagePxSize;
    }

    private static int calculateSpacesToAdd(int messagePxSize) {
        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = TARGET_WIDTH - halvedMessageSize;

        // Calculate the number of spaces needed
        return Math.max(0, toCompensate / SPACE_LENGTH);
    }
}
