package mil.army.swf.micrometer.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Meter;
import mil.army.swf.micrometer.metrics.helper.MetricHelper;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class HelperTaggedGaugeTest {
    @Test
    public void helpTaggedGauge() throws IOException {
        MetricHelper.createGauge("aswf", "ASWF Application Teams", "teams");
        MetricHelper.setGauge("aswf", "vector", 100);
        assertEquals(1, MetricHelper.getRegistry().getMeters().size());
        Meter meter = MetricHelper.getRegistry().getMeters().get(0);
        if (meter instanceof Gauge) {
            Gauge gauge = (Gauge) meter;
            double value = gauge.value();
            Writer writer = new FileWriter("logs/helpTaggedGauge");
            MetricHelper.getRegistry().scrape(writer);
            writer.close();
            MetricHelper.getRegistry().remove(meter);
            assertEquals(100d, value);
        } else {
            assertInstanceOf(Gauge.class, meter);
        }
    }
}