package exceptions_lab;

/**
 * MaximumRecursionDepth is an exception class which is thrown when depth exceeds the allowed depth
 */
public class MaximumRecursionDepth extends RuntimeException {
    /**
     * Detailed exception message
     */
    private final String message;

    /**
     * Depth reached with the exception occurred
     */
    private final int depth;

    /**
     * Class constructor MaximumRecursionDepth
     *
     * @param message detailed exception message
     * @param depth   depth reached when the exception occurred
     */
    public MaximumRecursionDepth(String message, int depth) {
        this.message = message;
        this.depth = depth;
    }

    /**
     * Gets the detailed exception message
     *
     * @return this MaximumRecursionDepth's message
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Gets the depth reached when the exception occurred
     *
     * @return this MaximumRecursionDepth's depth
     */
    public int getDepth() {
        return depth;
    }
}