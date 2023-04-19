package mil.army.swf.micrometer.metrics.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MetricWrapper {
    public enum MetricType { COUNTER, GAUGE, TIMER, T_COUNTER, T_GAUGE, T_TIMER, MT_COUNTER, MT_GAUGE, MT_TIMER }

    private MetricType type;
    private Object metric;
}
