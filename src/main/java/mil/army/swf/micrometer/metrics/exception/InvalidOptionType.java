package mil.army.swf.micrometer.metrics.exception;

/**
 * Invalid timer type exception
 */
public class InvalidOptionType extends Exception {
    /**
     * Constructor for InvalidOptionType exception
     *
     * @param message - error message
     */
    public InvalidOptionType(String message) {
        super(message);
    }
}
