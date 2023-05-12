package mil.army.swf.micrometer.metrics;

/**
 * Meter timer types
 */
public class TimerTypes {
    /**
     *
     */
    public static enum TimerType {
        /**
         * No special timer
         */
        NONE,
        /**
         * histogram timer
         */
        HIST,
        /**
         * expected value timer
         */
        EXPV,
        /**
         * service level objective timer
         */
        SLO,
        /**
         * service level agreement timer
         */
        SLA};
}
