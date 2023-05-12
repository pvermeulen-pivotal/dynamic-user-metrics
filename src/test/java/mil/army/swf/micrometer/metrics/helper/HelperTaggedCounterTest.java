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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class HelperTaggedCounterTest {

    @BeforeAll
    public static void initialize() {
        MetricHelper.setRegistry(new PrometheusMeterRegistry(PrometheusConfig.DEFAULT));
    }

    @Test
    public void helpTaggedCounter() throws IOException {
        MetricHelper.createTagCounter("aswf", "ASWF Application Teams", "team", "vector");
        MetricHelper.incrementTagCounter("aswf");
        Meter meter = MetricHelper.getRegistry().getMeters().get(0);
        if (meter instanceof Counter) {
            Counter counter = (Counter) meter;
            double count = counter.count();
            Writer writer = new FileWriter("logs/helpTaggedCounter");
            MetricHelper.getRegistry().scrape(writer);
            writer.close();
            MetricHelper.getRegistry().remove(meter);
            assertEquals(1, count);
        } else {
            assertInstanceOf(Counter.class, meter);
        }
    }

    @Test
    public void helpTaggedCounterIncrementAmount() throws IOException {
        MetricHelper.createTagCounter("aswf", "ASWF Application Teams", "team", "vector");
        MetricHelper.incrementTagCounter("aswf", 10);
        Meter meter = MetricHelper.getRegistry().getMeters().get(0);
        if (meter instanceof Counter) {
            Counter counter = (Counter) meter;
            double value = counter.count();
            Writer writer = new FileWriter("logs/helpTaggedCounterIncrementAmount");
            MetricHelper.getRegistry().scrape(writer);
            writer.close();
            MetricHelper.getRegistry().remove(meter);
            assertEquals(10d, value);
        } else {
            assertInstanceOf(Counter.class, meter);
        }
    }
}