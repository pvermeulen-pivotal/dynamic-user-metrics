package mil.army.swf.micrometer.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class TaggedCounter {
    private PrometheusMeterRegistry registry;
    private String name;
    private String description;
    private String tagName;
    private Map<String, Counter> counters;

    public TaggedCounter(PrometheusMeterRegistry registry, String name, String description, String tagName) {
        this.registry = registry;
        this.name = name;
        this.description = description;
        this.tagName = tagName;
        counters = new HashMap<>();
    }

    public void increment(String tagValue){
        Counter counter = counters.get(tagValue);
        if(counter == null) {
            counter = Counter.builder(name).tags(tagName, tagValue).register(registry);
            counters.put(tagValue, counter);
        }
        counter.increment();
    }

    public void increment(long value, String tagValue){
        Counter counter = counters.get(tagValue);
        if(counter == null) {
            counter = Counter.builder(name).tags(tagName, tagValue).register(registry);
            counters.put(tagValue, counter);
        }
        counter.increment(value);
    }
}
