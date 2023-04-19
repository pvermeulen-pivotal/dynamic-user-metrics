package mil.army.swf.micrometer.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.Tag;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.Data;
import mil.army.swf.micrometer.metrics.wrapper.DoubleWrapper;

import java.util.*;

@Data
public class MultiTaggedGauge {
    private PrometheusMeterRegistry registry;
    private String name;
    private String description;
    private String[] tagNames;
    private Map<String, DoubleWrapper> gauges;

    public MultiTaggedGauge(PrometheusMeterRegistry registry, String name, String description, String... tagNames) {
        this.registry = registry;
        this.name = name;
        this.description = description;
        this.tagNames = tagNames;
        gauges = new HashMap<>();
    }

    public void set(double value, String... tagValues) {
        String valuesString = Arrays.toString(tagValues);
        if (tagValues.length != tagNames.length) {
            throw new IllegalArgumentException("Gauge tags mismatch! Expected args are " + Arrays.toString(tagNames) + ", provided tags are " + valuesString);
        }

        DoubleWrapper number = gauges.get(valuesString);
        if (number == null) {
            List<Tag> tags = new ArrayList<>(tagNames.length);
            for (int i = 0; i < tagNames.length; i++) {
                tags.add(new ImmutableTag(tagNames[i], tagValues[i]));
            }
            DoubleWrapper valueHolder = new DoubleWrapper(value);
            Gauge.builder(name, valueHolder, DoubleWrapper::getValue).tags(tags).register(registry);
            gauges.put(valuesString, valueHolder);
        } else {
            number.setValue(value);
        }
    }
}
