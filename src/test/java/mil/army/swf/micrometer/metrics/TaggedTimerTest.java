package mil.army.swf.micrometer.metrics;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import mil.army.swf.micrometer.metrics.helper.MetricHelper;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaggedTimerTest {

    @Test
    public void taggedTimerTest(){
        PrometheusMeterRegistry registry = MetricHelper.getRegistry();
        TaggedTimer taggedTimer = new TaggedTimer(registry, "some-timer", "desc", "action");
        taggedTimer.getTimer("walk-the-dog").record(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        taggedTimer.getTimer("make-dinner").record(30, TimeUnit.MINUTES);
        taggedTimer.getTimer("homework").record(2, TimeUnit.HOURS);
        taggedTimer.getTimer("walk-the-dog").record(10, TimeUnit.MINUTES);
        taggedTimer.getTimer("walk-the-dog").record(15, TimeUnit.MINUTES);
        int size = registry.getMeters().size();
        registry.getMeters().forEach(m -> {
            registry.remove(m);
        });
        assertEquals(3, size);
    }

}