package mil.army.swf.micrometer.metrics;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import mil.army.swf.micrometer.metrics.helper.MetricHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MultiTaggedCounterTest {

    @Test
    public void multiTaggedCounterTest() {
        PrometheusMeterRegistry registry = MetricHelper.getRegistry();
        MultiTaggedCounter multiTaggedCounter = new MultiTaggedCounter(registry, "sheep", "desc", "color", "age-group");
        multiTaggedCounter.increment("black", "young");
        multiTaggedCounter.increment("black", "young");
        multiTaggedCounter.increment("white", "young");
        multiTaggedCounter.increment("black", "old");
        multiTaggedCounter.increment("black", "toddler");
        multiTaggedCounter.increment("black", "old");
        multiTaggedCounter.increment("white", "toddler");
        multiTaggedCounter.increment("brown", "adult");
        int size = registry.getMeters().size();
        registry.getMeters().forEach(m -> {
            registry.remove(m);
        });
        assertEquals(6, size);
    }

    @Test
    public void multiTaggedCounterIllegalArgTest() {
        PrometheusMeterRegistry registry = MetricHelper.getRegistry();
        MultiTaggedCounter multiTaggedCounter = new MultiTaggedCounter(registry, "sheep", "desc", "color", "age-group");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            multiTaggedCounter.increment("black");
        });
    }
}