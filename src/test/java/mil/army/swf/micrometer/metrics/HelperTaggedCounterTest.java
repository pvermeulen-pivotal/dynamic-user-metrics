package mil.army.swf.micrometer.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Meter;
import mil.army.swf.micrometer.metrics.helper.MetricHelper;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.*;

class HelperTaggedCounterTest {

    @Test
    public void helpTaggedCounter() throws IOException {
        MetricHelper.createCounter("aswf", "ASWF Application Teams", "teams");
        MetricHelper.incrementCounter("aswf", "vector");
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
        MetricHelper.createCounter("aswf", "ASWF Application Teams", "teams");
        MetricHelper.incrementCounter("aswf", "vector", 10);
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