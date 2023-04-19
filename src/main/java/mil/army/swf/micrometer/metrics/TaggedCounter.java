package mil.army.swf.micrometer.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Micrometer tagged counter class
 */
@Data
public class TaggedCounter {
    private PrometheusMeterRegistry registry;
    private String name;
    private String description;
    private String tagName;
    private Map<String, Counter> counters;

    /**
     * Constructor for multi-tagged counter
     *
     * @param registry    - prometheus registry
     * @param name        - name of the meter
     * @param description - description of the meter
     * @param tagName     - tag name associated with the meter
     */
    public TaggedCounter(PrometheusMeterRegistry registry, String name, String description, String tagName) {
        this.registry = registry;
        this.name = name;
        this.description = description;
        this.tagName = tagName;
        counters = new HashMap<>();
    }

    /**
     * Increments a tagged counter
     *
     * @param tagValue - set of tag values to increment
     */
    public void increment(String tagValue) {
        Counter counter = counters.get(tagValue);
        if (counter == null) {
            counter = Counter.builder(name).tags(tagName, tagValue).register(registry);
            counters.put(tagValue, counter);
        }
        counter.increment();
    }

    /**
     * Increments a tagged counter with the supplied value
     *
     * @param value    - the value to increment
     * @param tagValue - the tag value to increment
     */
    public void increment(long value, String tagValue) {
        Counter counter = counters.get(tagValue);
        if (counter == null) {
            counter = Counter.builder(name).tags(tagName, tagValue).register(registry);
            counters.put(tagValue, counter);
        }
        counter.increment(value);
    }
}
