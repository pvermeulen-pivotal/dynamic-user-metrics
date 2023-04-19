package mil.army.swf.micrometer.metrics;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import mil.army.swf.micrometer.metrics.helper.MetricHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MultiTaggedTimerTest {

    @Test
    public void multiTaggedTimerTest() {
        PrometheusMeterRegistry registry = MetricHelper.getRegistry();
        MultiTaggedTimer multiTaggedTimer = new MultiTaggedTimer(registry, "some-timer", "desc", "who", "action");
        multiTaggedTimer.getTimer("Eric", "walk-the-dog").record(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        multiTaggedTimer.getTimer("Eric", "make-dinner").record(30, TimeUnit.MINUTES);
        multiTaggedTimer.getTimer("Benz", "make-dinner").record(30, TimeUnit.MINUTES);
        multiTaggedTimer.getTimer("Benz", "homework").record(2, TimeUnit.HOURS);
        multiTaggedTimer.getTimer("Eric", "walk-the-dog").record(10, TimeUnit.MINUTES);
        multiTaggedTimer.getTimer("Benz", "walk-the-dog").record(15, TimeUnit.MINUTES);
        int size = registry.getMeters().size();
        registry.getMeters().forEach(m -> {
            registry.remove(m);
        });
        assertEquals(5, size);
    }


    @Test
    public void multiTaggedTimerIllegalArgsTest() {
        PrometheusMeterRegistry registry = MetricHelper.getRegistry();
        MultiTaggedTimer multiTaggedTimer = new MultiTaggedTimer(registry, "some-timer", "desc", "who", "action");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            multiTaggedTimer.getTimer("walk-the-dog").record(800, TimeUnit.MINUTES);
        });
    }
}