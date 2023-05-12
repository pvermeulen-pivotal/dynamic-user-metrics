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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class HelperTaggedTimerTest {

    @BeforeAll
    public static void initialize() {
        MetricHelper.setRegistry(new PrometheusMeterRegistry(PrometheusConfig.DEFAULT));
    }

    @Test
    public void helpTaggedTimer() throws IOException {
        MetricHelper.createTagTimer("aswf", "ASWF Application Teams", "team", "vector");
        MetricHelper.recordTagTime("aswf", Duration.ofSeconds(30l));
        Meter meter = MetricHelper.getRegistry().getMeters().get(0);
        if (meter instanceof Timer) {
            Timer timer = (Timer) meter;
            double time = timer.totalTime(TimeUnit.SECONDS);
            Writer writer = new FileWriter("logs/helpTaggedTimer");
            MetricHelper.getRegistry().scrape(writer);
            writer.close();
            MetricHelper.getRegistry().remove(meter);
            assertEquals(30d, time);
        } else {
            assertInstanceOf(Timer.class, meter);
        }
    }

    @Test
    public void helpTaggedTimerTimeUnit() throws InvalidTimeUnitException, IOException {
        MetricHelper.createTagTimer("aswf", "ASWF Application Teams", "team", "vector");
        MetricHelper.recordTagTime("aswf", TimeUnit.SECONDS, 10l);
        Meter meter = MetricHelper.getRegistry().getMeters().get(0);
        if (meter instanceof Timer) {
            Timer timer = (Timer) meter;
            double total = timer.totalTime(TimeUnit.SECONDS);
            Writer writer = new FileWriter("logs/helpTaggedTimerTimeUnit");
            MetricHelper.getRegistry().scrape(writer);
            writer.close();
            MetricHelper.getRegistry().remove(meter);
            assertEquals(10d, total);
        } else {
            assertInstanceOf(Timer.class, meter);
        }
    }

    @Test
    public void helpTaggedTimerHistogram() throws IOException {
        double[] values = {0.0d, 1.0d};
        MetricHelper.createTagHistogramTimer("aswf", "ASWF Application Teams", "team", "vector");
        MetricHelper.recordTagTime("aswf", Duration.ofSeconds(30l));
        Meter meter = MetricHelper.getRegistry().getMeters().get(0);
        if (meter instanceof Timer) {
            Timer timer = (Timer) meter;
            double time = timer.totalTime(TimeUnit.SECONDS);
            Writer writer = new FileWriter("logs/helpTaggedTimerHistogram");
            MetricHelper.getRegistry().scrape(writer);
            writer.close();
            MetricHelper.getRegistry().remove(meter);
            assertEquals(30d, time);
        } else {
            assertInstanceOf(Timer.class, meter);
        }
    }

    @Test
    public void helpTaggedTimerHistogramExpectedValues() throws IOException {
        double[] values = {0.0d, 1.0d};
        Duration min = Duration.ofSeconds(10);
        Duration max = Duration.ofSeconds(30);
        MetricHelper.createTagHistogramExpectedValueTimer("aswf", "ASWF Application Teams", TimerTypes.TimerType.EXPV, min, max, "team", "vector");
        MetricHelper.recordTagTime("aswf", Duration.ofSeconds(25));
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
    public void helpTaggedTimerHistogramExpectedValueSloTimer() throws IOException, InvalidOptionType {
        double[] values = {0.0d, 1.0d};
        Duration min = Duration.ofSeconds(10);
        Duration max = Duration.ofSeconds(30);
        Duration slo = Duration.ofSeconds(25);
        MetricHelper.createTagHistogramExpectedValueSloSlaTimer("aswf", "ASWF Application Teams",
                TimerTypes.TimerType.SLO, slo, min, max, "team", "vector");
        MetricHelper.recordTagTime("aswf", Duration.ofSeconds(25L));
        Meter meter = MetricHelper.getRegistry().getMeters().get(0);
        if (meter instanceof Timer) {
            Timer timer = (Timer) meter;
            double time = timer.totalTime(TimeUnit.SECONDS);
            Writer writer = new FileWriter("logs/helpTaggedTimerHistogramExpectedValueSloTimer");
            MetricHelper.getRegistry().scrape(writer);
            writer.close();
            MetricHelper.getRegistry().remove(meter);
            assertEquals(25d, time);
        } else {
            assertInstanceOf(Timer.class, meter);
        }
    }

    @Test
    public void helpTaggedTimerHistogramExpectedValueSlaTimer() throws IOException, InvalidOptionType {
        double[] values = {0.0d, 1.0d};
        Duration min = Duration.ofSeconds(10);
        Duration max = Duration.ofSeconds(30);
        Duration sla = Duration.ofSeconds(25);
        MetricHelper.createTagHistogramExpectedValueSloSlaTimer("aswf", "ASWF Application Teams",
                TimerTypes.TimerType.SLA, sla, min, max, "team", "vector");
        MetricHelper.recordTagTime("aswf", Duration.ofSeconds(25));
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