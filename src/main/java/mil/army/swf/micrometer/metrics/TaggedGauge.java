package mil.army.swf.micrometer.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.prometheus.PrometheusMeterRegistry;

import lombok.Data;
import mil.army.swf.micrometer.metrics.wrapper.DoubleWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Micrometer tagged gauge class
 */
@Data
public class TaggedGauge {
    private PrometheusMeterRegistry registry;
    private String name;
    private String description;
    private String tagName;
    private Map<String, DoubleWrapper> gauges;

    /**
     * Constructor for tagged gauge
     *
     * @param registry    - prometheus registry
     * @param name        - name of the meter
     * @param description - description of the meter
     * @param tagName     - tag name associated with the meter
     */
    public TaggedGauge(PrometheusMeterRegistry registry, String name, String description, String tagName) {
        this.registry = registry;
        this.name = name;
        this.description = description;
        this.tagName = tagName;
        gauges = new HashMap<>();
    }

    /**
     * Set a tagged gauge value
     *
     * @param tagValue - tag value to set
     * @param value    - value to set
     */
    public void set(String tagValue, double value) {
        DoubleWrapper number = gauges.get(tagValue);
        if (number == null) {
            DoubleWrapper valueHolder = new DoubleWrapper(value);
            Gauge.builder(name, valueHolder, DoubleWrapper::getValue).tags(tagName, tagValue).register(registry);
            gauges.put(tagValue, valueHolder);
        } else {
            number.setValue(value);
        }
    }
}
