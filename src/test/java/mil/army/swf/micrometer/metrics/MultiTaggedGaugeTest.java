package mil.army.swf.micrometer.metrics;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import mil.army.swf.micrometer.metrics.helper.MetricHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MultiTaggedGaugeTest {
    @Test
    public void multiTaggedGaugeTest() {
        PrometheusMeterRegistry registry = MetricHelper.getRegistry();
        MultiTaggedGauge multiTaggedGauge = new MultiTaggedGauge(registry, "weekly-high-tide", "desc", "city", "day");
        multiTaggedGauge.set(1.75, "Halifax", "Sunday");
        multiTaggedGauge.set(1.3, "Portland", "Sunday");
        multiTaggedGauge.set(2, "Portland", "Monday");
        multiTaggedGauge.set(0.81, "Venice", "Monday");
        multiTaggedGauge.set(1.8, "Halifax", "Monday");
        multiTaggedGauge.set(1.98, "Halifax", "Monday");
        multiTaggedGauge.set(0.43, "Venice", "Tuesday");
        multiTaggedGauge.set(1.86, "Halifax", "Tuesday");
        multiTaggedGauge.set(2.4, "Portland", "Monday");
        multiTaggedGauge.set(0.56, "Venice", "Tuesday");
        int size = registry.getMeters().size();
        registry.getMeters().forEach(m -> {
            registry.remove(m);
        });
        assertEquals(7, size);
    }

    @Test
    public void multiTaggedGaugeIllegalArgsTest() {
        PrometheusMeterRegistry registry = MetricHelper.getRegistry();
        MultiTaggedGauge multiTaggedGauge = new MultiTaggedGauge(registry, "weekly-high-tide", "desc", "city", "day");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            multiTaggedGauge.set(1.75, "Halifax");
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            multiTaggedGauge.set(1.75, "Halifax", "Sunday", "boo");
        });
    }
}