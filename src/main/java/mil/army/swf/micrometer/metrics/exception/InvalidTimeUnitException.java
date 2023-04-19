package mil.army.swf.micrometer.metrics.exception;

/**
 * Exception class for an invalid time unit for timer recording
 */
public class InvalidTimeUnitException extends Exception {
    /**
     * Constructor for exception
     *
     * @param message - error message
     */
    public InvalidTimeUnitException(String message) {
        super(message);
    }
}
