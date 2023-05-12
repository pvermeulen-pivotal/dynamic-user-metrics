package mil.army.swf.micrometer.metrics.helper;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import mil.army.swf.micrometer.metrics.TimerTypes;
import mil.army.swf.micrometer.metrics.exception.InvalidOptionType;
import mil.army.swf.micrometer.metrics.exception.InvalidTimeUnitException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class HelperMultiTaggedTimerTest {

    @BeforeAll
    public static void initialize() {
        MetricHelper.setRegistry(new PrometheusMeterRegistry(PrometheusConfig.DEFAULT));
    }

    @Test
    public void helpMultiTaggedTimerTagsNotEqualTagNames() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
               MetricHelper.createMultiTagTimer("aswf", "ASWF Application Teams", "team color".split(" "), "vector".split(" "));
        });
        assertTrue(thrown.getMessage().startsWith("Timer tags mismatch!"));
    }

    @Test
    public void helpMultiTaggedTimer() {
        MetricHelper.createMultiTagTimer("aswf", "ASWF Application Teams", "team color".split(" "), "vector blue".split(" "));
        MetricHelper.recordMultiTagTime("aswf", Duration.ofSeconds(100));
        assertEquals(1, MetricHelper.getRegistry().getMeters().size());
        Meter meter = MetricHelper.getRegistry().getMeters().get(0);
        if (meter instanceof Timer) {
            Timer timer = (Timer) meter;
            double time = timer.totalTime(TimeUnit.SECONDS);
            MetricHelper.getRegistry().remove(meter);
            assertEquals(100d, time);
        } else {
            assertInstanceOf(Timer.class, meter);
        }
    }

    @Test
    public void helpMultiTaggedTimerTimeUnits() throws InvalidTimeUnitException, IOException {
        MetricHelper.createMultiTagTimer("aswf", "ASWF Application Teams", "team color".split(" "), "vector blue".split(" "));
        MetricHelper.recordMultiTagTime("aswf", TimeUnit.SECONDS, 200l);
        assertEquals(1, MetricHelper.getRegistry().getMeters().size());
        Meter meter = MetricHelper.getRegistry().getMeters().get(0);
        if (meter instanceof Timer) {
            Timer timer = (Timer) meter;
            double time = timer.totalTime(TimeUnit.SECONDS);
            Writer writer = new FileWriter("logs/helpMultiTaggedTimerTimeUnits");
            MetricHelper.getRegistry().scrape(writer);
            writer.close();
            MetricHelper.getRegistry().remove(meter);
            assertEquals(200d, time);
        } else {
            assertInstanceOf(Timer.class, meter);
        }
    }

    @Test
    public void helpMultiTagTimerHistogram() throws IOException {
        MetricHelper.createMultiTagHistogramTimer("aswf", "ASWF Application Teams", "team color".split(" "), "vector blue".split(" "));
        MetricHelper.recordMultiTagTime("aswf", Duration.ofSeconds(30));
        Meter meter = MetricHelper.getRegistry().getMeters().get(0);
        if (meter instanceof Timer) {
            Timer timer = (Timer) meter;
            double time = timer.totalTime(TimeUnit.SECONDS);
            Writer writer = new FileWriter("logs/helpMultiTagTimerHistogram");
            MetricHelper.getRegistry().scrape(writer);
            writer.close();
            MetricHelper.getRegistry().remove(meter);
            assertEquals(30d, time);
        } else {
            assertInstanceOf(Timer.class, meter);
        }
    }

    @Test
    public void helpMultiTagTimerHistogramExpectedValues() throws IOException {
        Duration min = Duration.ofSeconds(10);
        Duration max = Duration.ofSeconds(30);
        MetricHelper.createMultiTagHistogramExpectedValueTimer("aswf", "ASWF Application Teams",
                TimerTypes.TimerType.EXPV, min, max, "team color".split(" "), "vector blue".split(" "));
        MetricHelper.recordMultiTagTime("aswf", Duration.ofSeconds(25));
        Meter meter = MetricHelper.getRegistry().getMeters().get(0);
        if (meter instanceof Timer) {
            Timer timer = (Timer) meter;
            double time = timer.totalTime(TimeUnit.SECONDS);
            Writer writer = new FileWriter("logs/helpTaggedTimerHistogramExpectedValues");
            MetricHelper.getRegistry().scrape(writer);
            writer.close();
            MetricHelper.getRegistry().remove(meter);
            assertEquals(25d, time);
        } else {
            assertInstanceOf(Timer.class, meter);
        }
    }

    @Test
    public void helpMultiTagTimerHistogramExpectedValueSloTimer() throws IOException, InvalidOptionType {
        Duration min = Duration.ofSeconds(10);
        Duration max = Duration.ofSeconds(30);
        Duration slo = Duration.ofSeconds(25);
        MetricHelper.createMultiTagHistogramExpectedValueSloSlaTimer("aswf", "ASWF Application Teams",
                TimerTypes.TimerType.SLO, slo, min, max, "team color".split(" "), "vector blue".split(" ") );
        MetricHelper.recordMultiTagTime("aswf", Duration.ofSeconds(25));
        Meter meter = MetricHelper.getRegistry().getMeters().get(0);
        if (meter instanceof Timer) {
            Timer timer = (Timer) meter;
            double time = timer.totalTime(TimeUnit.SECONDS);
            Writer writer = new FileWriter("logs/helpMultiTagTimerHistogramExpectedValueSloTimer");
            MetricHelper.getRegistry().scrape(writer);
            writer.close();
            MetricHelper.getRegistry().remove(meter);
            assertEquals(25d, time);
        } else {
            assertInstanceOf(Timer.class, meter);
        }
    }

    @Test
    public void helpMultiTagTimerHistogramExpectedValueSlaTimer() throws IOException, InvalidOptionType {
        Duration min = Duration.ofSeconds(10);
        Duration max = Duration.ofSeconds(30);
        Duration sla = Duration.ofSeconds(25);
        MetricHelper.createMultiTagHistogramExpectedValueSloSlaTimer("aswf", "ASWF Application Teams",
                TimerTypes.TimerType.SLA, sla, min, max, "team color".split(" "), "vector blue".split(" ") );
        MetricHelper.recordMultiTagTime("aswf", Duration.ofSeconds(25));
        Meter meter = MetricHelper.getRegistry().getMeters().get(0);
        if (meter instanceof Timer) {
            Timer timer = (Timer) meter;
            double time = timer.totalTime(TimeUnit.SECONDS);
            Writer writer = new FileWriter("logs/helpTaggedTimerHistogramExpectedValueSlaTimer");
            MetricHelper.getRegistry().scrape(writer);
            writer.close();
            MetricHelper.getRegistry().remove(meter);
            assertEquals(25d, time);
        } else {
            assertInstanceOf(Timer.class, meter);
        }
    }
}