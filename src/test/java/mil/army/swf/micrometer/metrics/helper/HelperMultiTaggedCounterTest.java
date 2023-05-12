package mil.army.swf.micrometer.metrics.helper;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Meter;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.*;

class HelperMultiTaggedCounterTest {

    @BeforeAll
    public static void initialize() {
        MetricHelper.setRegistry(new PrometheusMeterRegistry(PrometheusConfig.DEFAULT));
    }

    @Test
    public void helpMultiTaggedCounterTagsNotEqualTagNames() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            MetricHelper.createMultiTagCounter("aswf", "ASWF Application Teams", "team color".split(" "), "vector".split(" "));
        });
        assertTrue(thrown.getMessage().startsWith("Counter tags mismatch!"));
    }

    @Test
    public void helpMultiTaggedCounter() throws IOException {
        MetricHelper.createMultiTagCounter("aswf", "ASWF Application Teams", "team color".split(" "), "vector blue".split(" "));
        MetricHelper.incrementMultiTagCounter("aswf");
        assertEquals(1, MetricHelper.getRegistry().getMeters().size());
        Meter meter = MetricHelper.getRegistry().getMeters().get(0);
        if (meter instanceof Counter) {
            Counter counter = (Counter) meter;
            double count = counter.count();
            Writer writer = new FileWriter("logs/helpMultiTaggedCounter");
            MetricHelper.getRegistry().scrape(writer);
            writer.close();
            MetricHelper.getRegistry().remove(meter);
            assertEquals(1d, count);
        } else {
            assertInstanceOf(Counter.class, meter);
        }
    }

    @Test
    public void helpMultiTaggedCounterIncrementAmount() throws IOException {
        MetricHelper.createMultiTagCounter("aswf", "ASWF Application Teams", "team color".split(" "), "vector blue".split(" "));
        MetricHelper.incrementMultiTagCounter("aswf", 10);
        assertEquals(1, MetricHelper.getRegistry().getMeters().size());
        Meter meter = MetricHelper.getRegistry().getMeters().get(0);
        if (meter instanceof Counter) {
            Counter counter = (Counter) meter;
            double count = counter.count();
            Writer writer = new FileWriter("logs/helpMultiTaggedCounterIncrementAmount");
            MetricHelper.getRegistry().scrape(writer);
            writer.close();
            MetricHelper.getRegistry().remove(meter);
            assertEquals(10d, count);
        } else {
            assertInstanceOf(Counter.class, meter);
        }
    }
}