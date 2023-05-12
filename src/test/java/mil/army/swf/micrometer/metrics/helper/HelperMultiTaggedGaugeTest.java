package mil.army.swf.micrometer.metrics.helper;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Meter;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.*;

class HelperMultiTaggedGaugeTest {

    @BeforeAll
    public static void initialize() {
        MetricHelper.setRegistry(new PrometheusMeterRegistry(PrometheusConfig.DEFAULT));
    }

    @Test
    public void helpMultiTaggedGaugeTagsNotEqualTagNames() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            MetricHelper.createMultiTagGauge("aswf", "ASWF Application Teams", "team color".split(" "), "vector".split(""));
        });
        assertTrue(thrown.getMessage().startsWith("Gauge tags mismatch!"));
    }

    @Test
    public void helpMultiTaggedGauge() throws IOException {
        MetricHelper.createMultiTagGauge("aswf","ASWF Application Teams", "team color".split(" "), "vector blue".split(" "));
        MetricHelper.setMultiTagGauge("aswf", 200d);
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