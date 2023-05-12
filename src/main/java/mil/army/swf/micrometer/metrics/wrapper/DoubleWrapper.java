package mil.army.swf.micrometer.metrics.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Wrapper value class for a gauge value
 */
@AllArgsConstructor
@Data
public class DoubleWrapper {
    private double value;
}
