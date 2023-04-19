package mil.army.swf.micrometer.metrics;

import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Micrometer tagged timer class
 */
@Data
public class TaggedTimer {
    private PrometheusMeterRegistry registry;
    private String name;
    private String description;
    private String tagName;
    private Map<String, Timer> timers;

    /**
     * Constructor for tagged timer
     *
     * @param registry    - prometheus registry
     * @param name        - name of the meter
     * @param description - description of the meter
     * @param tagName     - tag name associated with the meter
     */
    public TaggedTimer(PrometheusMeterRegistry registry, String name, String description, String tagName) {
        this.registry = registry;
        this.name = name;
        this.description = description;
        this.tagName = tagName;
        timers = new HashMap<>();
    }

    /**
     * Creates a tagged timer if not found otherwise return the timer using the tag values
     *
     * @param tagValue - set of tag values for the meter
     * @return - tagged timer
     */
    public Timer getTimer(String tagValue) {
        Timer timer = timers.get(tagValue);
        if (timer == null) {
            timer = Timer.builder(name).tags(tagName, tagValue).register(registry);
            timers.put(tagValue, timer);
        }
        return timer;
    }

}
