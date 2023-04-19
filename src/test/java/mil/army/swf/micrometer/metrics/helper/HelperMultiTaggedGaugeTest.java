package mil.army.swf.micrometer.metrics.helper;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Meter;
import mil.army.swf.micrometer.metrics.helper.MetricHelper;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.*;

class HelperMultiTaggedGaugeTest {

    @Test
    public void helpMultiTaggedGaugeTagsNotEqualTagNames() {
        MetricHelper.createMultiTagGauge("aswf", "ASWF Application Teams", "teams", "color");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            MetricHelper.setMultiTagGauge("aswf", 100d, "vector", "green", "group2");
        });
        assertTrue(thrown.getMessage().startsWith("Gauge tags mismatch!"));
    }

    @Test
    public void helpMultiTaggedGauge() throws IOException {
        MetricHelper.createMultiTagGauge("aswf","ASWF Application Teams", "teams", "color");
        MetricHelper.setMultiTagGauge("aswf", 200d, "vector", "blue");
        assertEquals(1, MetricHelper.getRegistry().getMeters().size());
        Meter meter = MetricHelper.getRegistry().getMeters().get(0);
        if (meter instanceof Gauge) {
            Gauge gauge = (Gauge) meter;
            double value = gauge.value();
            Writer writer = new FileWriter("logs/helpMultiTaggedGauge");
            MetricHelper.getRegistry().scrape(writer);
            writer.close();
            MetricHelper.getRegistry().remove(meter);
            assertEquals(200d, value);
        } else {
            assertInstanceOf(Gauge.class, meter);
        }
    }
}