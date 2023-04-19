package mil.army.swf.micrometer.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.Tag;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.Data;

import java.util.*;

/**
 * Micrometer multi-tagged counter class
 */
@Data
public class MultiTaggedCounter {
    private PrometheusMeterRegistry registry;
    private String name;
    private String description;
    private String[] tagNames;
    private Map<String, Counter> counters;

    /**
     * Constructor for multi-tagged counter
     *
     * @param registry    - prometheus registry
     * @param name        - name of the meter
     * @param description - description of the meter
     * @param tagNames    - set of tag names associated with the meter
     */
    public MultiTaggedCounter(PrometheusMeterRegistry registry, String name, String description, String... tagNames) {
        this.registry = registry;
        this.name = name;
        this.description = description;
        this.tagNames = tagNames;
        this.counters = new HashMap<>();
    }

    /**
     * Increments the multi-tagged counter
     *
     * @param tagValues - set of tag values to increment
     */
    public void increment(String... tagValues) {
        String valuesString = Arrays.toString(tagValues);
        if (tagValues.length != tagNames.length) {
            throw new IllegalArgumentException("Counter tags mismatch! Expected args are " + Arrays.toString(tagNames) + ", provided tags are " + valuesString);
        }
        Counter counter = counters.get(valuesString);
        if (counter == null) {
            List<Tag> tags = new ArrayList<>(tagNames.length);
            for (int i = 0; i < tagNames.length; i++) {
                tags.add(new ImmutableTag(tagNames[i], tagValues[i]));
            }
            counter = Counter.builder(name).description(description).tags(tags).register(registry);
            counters.put(valuesString, counter);
        }
        counter.increment();
    }

    /**
     * Increments a multi-tagged counter using supplied value
     *
     * @param value     - value to increment
     * @param tagValues - set of tag values to increment
     */
    public void increment(long value, String... tagValues) {
        String valuesString = Arrays.toString(tagValues);
        if (tagValues.length != tagNames.length) {
            throw new IllegalArgumentException("Counter tags mismatch! Expected args are " + Arrays.toString(tagNames) + ", provided tags are " + valuesString);
        }
        Counter counter = counters.get(valuesString);
        if (counter == null) {
            List<Tag> tags = new ArrayList<>(tagNames.length);
            for (int i = 0; i < tagNames.length; i++) {
                tags.add(new ImmutableTag(tagNames[i], tagValues[i]));
            }
            counter = Counter.builder(name).description(description).tags(tags).register(registry);
            counters.put(valuesString, counter);
        }
        counter.increment(value);
    }
}
