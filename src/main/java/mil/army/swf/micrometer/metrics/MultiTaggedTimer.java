package mil.army.swf.micrometer.metrics;

import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.Data;

import java.util.*;

/**
 * Micrometer multi-tagged timer class
 */
@Data
public class MultiTaggedTimer {
    private PrometheusMeterRegistry registry;
    private String name;
    private String description;
    private String[] tagNames;
    private Map<String, Timer> timers;

    /**
     * Constructor for multi-tagged timer
     *
     * @param registry    - prometheus registry
     * @param name        - name of the meter
     * @param description - description of the meter
     * @param tagNames    - set of tag names associated with the meter
     */
    public MultiTaggedTimer(PrometheusMeterRegistry registry, String name, String description, String... tagNames) {
        this.registry = registry;
        this.name = name;
        this.description = description;
        this.tagNames = tagNames;
        timers = new HashMap<>();
    }

    /**
     * Creates a multi-tagged timer if not found otherwise returns the timer
     *
     * @param tagValues - set of tag values used to retrieve the timer
     * @return - multi-tagged timer
     */
    public Timer getTimer(String... tagValues) {
        String valuesString = Arrays.toString(tagValues);
        if (tagValues.length != tagNames.length) {
            throw new IllegalArgumentException("Timer tags mismatch! Expected args are " + Arrays.toString(tagNames) + ", provided tags are " + valuesString);
        }
        Timer timer = timers.get(valuesString);
        if (timer == null) {
            List<Tag> tags = new ArrayList<>(tagNames.length);
            for (int i = 0; i < tagNames.length; i++) {
                tags.add(new ImmutableTag(tagNames[i], tagValues[i]));
            }
            timer = Timer.builder(name).tags(tags).register(registry);
            timers.put(valuesString, timer);
        }
        return timer;
    }
}
