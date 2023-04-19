package mil.army.swf.micrometer.metrics;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import mil.army.swf.micrometer.metrics.helper.MetricHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaggedCounterTest {
    @Test
    public void testTaggedTimer() {
        PrometheusMeterRegistry registry = MetricHelper.getRegistry();
        TaggedCounter taggedCounter = new TaggedCounter(registry, "sheep", "desc", "color");
        taggedCounter.increment("black");
        taggedCounter.increment("white");
        taggedCounter.increment("white");
        taggedCounter.increment("black");
        taggedCounter.increment("brown");
        taggedCounter.increment("black");
        taggedCounter.increment("black");
        taggedCounter.increment("black");
        int size = registry.getMeters().size();
        registry.getMeters().forEach(m -> {
            registry.remove(m);
        });
        assertEquals(3, size);
    }
}