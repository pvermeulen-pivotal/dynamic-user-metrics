package mil.army.swf.micrometer.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.Tag;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

@AllArgsConstructor
@Data
public class MultiTaggedCounter {
    private PrometheusMeterRegistry registry;
    private String name;
    private String description;
    private String[] tagNames;
    private Map<String, Counter> counters;

    public MultiTaggedCounter(PrometheusMeterRegistry registry, String name, String description, String... tagNames) {
        this.registry = registry;
        this.name = name;
        this.description = description;
        this.tagNames = tagNames;
        this.counters = new HashMap<>();
    }

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
