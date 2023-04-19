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

import static org.junit.jupiter.api.Assertions.*;

class HelperMultiTaggedTimerTest {

    @Test
    public void helpMultiTaggedTimerTagsNotEqualTagNames() {
        MetricHelper.createMultiTagTime("aswf", "ASWF Application Teams", "teams", "color");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            MetricHelper.recordMultiTagTime("aswf", Duration.ofSeconds(10), "vector", "yellow", "group2");
        });
        assertTrue(thrown.getMessage().startsWith("Timer tags mismatch!"));
    }

    @Test
    public void helpMultiTaggedTimer() {
        MetricHelper.createMultiTagTime("aswf", "ASWF Application Teams", "teams", "color");
        MetricHelper.recordMultiTagTime("aswf", Duration.ofSeconds(100), "vector", "magenta");
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
        MetricHelper.createMultiTagTime("aswf", "ASWF Application Teams", "teams", "color");
        MetricHelper.recordMultiTagTime("aswf", TimeUnit.SECONDS, 200l, "vector", "black");
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
}