package mil.army.swf.micrometer.metrics;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Timer;
import mil.army.swf.micrometer.metrics.exception.InvalidTimeUnitException;
import mil.army.swf.micrometer.metrics.helper.MetricHelper;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class HelperTaggedTimerTest {

    @Test
    public void helpTaggedTimer() throws IOException {
        MetricHelper.createTimer("aswf", "ASWF Application Teams", "teams");
        MetricHelper.recordTime("aswf", "vector", Duration.ofSeconds(30l));
        Meter meter = MetricHelper.getRegistry().getMeters().get(0);
        if (meter instanceof Timer) {
            Timer timer = (Timer) meter;
            double time =  timer.totalTime(TimeUnit.SECONDS);
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
        MetricHelper.createTimer("aswf", "ASWF Application Teams", "teams");
        MetricHelper.recordTime("aswf", TimeUnit.SECONDS, 10l, "vector");
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
}