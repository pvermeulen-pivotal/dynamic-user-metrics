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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class HelperTaggedGaugeTest {

    @BeforeAll
    public static void initialize() {
        MetricHelper.setRegistry(new PrometheusMeterRegistry(PrometheusConfig.DEFAULT));
    }

    @Test
    public void helpTaggedGauge() throws IOException {
        MetricHelper.createTagGauge("aswf", "ASWF Application Teams", "team", "vector");
        MetricHelper.setTagGauge("aswf", 100);
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