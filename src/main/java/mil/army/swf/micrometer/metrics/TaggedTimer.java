package mil.army.swf.micrometer.metrics;

import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class TaggedTimer {
    private PrometheusMeterRegistry registry;
    private String name;
    private String description;
    private String tagName;
    private Map<String, Timer> timers;

    public TaggedTimer(PrometheusMeterRegistry registry, String name, String description, String tagName) {
        this.registry = registry;
        this.name = name;
        this.description = description;
        this.tagName = tagName;
        timers = new HashMap<>();
    }

    public Timer getTimer(String tagValue){
        Timer timer = timers.get(tagValue);
        if(timer == null) {
            timer = Timer.builder(name).tags(tagName, tagValue).register(registry);
            timers.put(tagValue, timer);
        }
        return timer;
    }

}
