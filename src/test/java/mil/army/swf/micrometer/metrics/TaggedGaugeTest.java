package mil.army.swf.micrometer.metrics;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import mil.army.swf.micrometer.metrics.helper.MetricHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaggedGaugeTest {

    @Test
    public void taggedGaugeTest() {
        PrometheusMeterRegistry registry = MetricHelper.getRegistry();
        TaggedGauge taggedGauge = new TaggedGauge(registry, "tide", "desc", "city");
        taggedGauge.set("Halifax", 1.75);
        taggedGauge.set("Portland", 1.3);
        taggedGauge.set("Portland", 2);
        taggedGauge.set("Venice", 0.81);
        taggedGauge.set("Halifax", 1.8);
        taggedGauge.set("Halifax", 1.98);
        taggedGauge.set("Venice", 0.43);
        taggedGauge.set("Halifax", 1.86);
        taggedGauge.set("Portland", 2.4);
        taggedGauge.set("Venice", -0.09);
        int size = registry.getMeters().size();
        registry.getMeters().forEach(m -> {
            registry.remove(m);
        });
        assertEquals(3, size);
    }

}