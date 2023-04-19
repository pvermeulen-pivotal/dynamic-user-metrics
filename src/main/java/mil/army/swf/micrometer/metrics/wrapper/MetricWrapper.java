package mil.army.swf.micrometer.metrics.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Wrapper class for metrics
 */
@AllArgsConstructor
@Data
public class MetricWrapper {
    /**
     * Enumerator used to define the type of the metric object*
     */
    public enum MetricType {
        /**
         * tagged counter
         */
        T_COUNTER,
        /**
         * tagged gauge
         */
        T_GAUGE,
        /**
         * tagged timer
         */
        T_TIMER,
        /**
         * multi-tagged counter
         */
        MT_COUNTER,
        /**
         * multi-tagged gauge
         */
        MT_GAUGE,
        /**
         * multi-tagged timer
         */
        MT_TIMER
    }

    private MetricType type;
    private Object metric;
}
