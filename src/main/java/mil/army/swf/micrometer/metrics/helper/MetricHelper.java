package mil.army.swf.micrometer.metrics.helper;

import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

import lombok.extern.slf4j.Slf4j;

import mil.army.swf.micrometer.metrics.*;
import mil.army.swf.micrometer.metrics.exception.InvalidTimeUnitException;
import mil.army.swf.micrometer.metrics.wrapper.MetricWrapper;

import java.time.Duration;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MetricHelper {

    private static PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    public static PrometheusMeterRegistry getRegistry() {
        return registry;
    }

    private static HashMap<String, MetricWrapper> wrappers = new HashMap<>();

    private static void saveWrapper(String name, MetricWrapper wrapper) {
        wrappers.put(name, wrapper);
    }

    private static MetricWrapper getWrapper(String name) {
        return wrappers.get(name);
    }

    public static void createCounter(String name, String description, String tagName) {
        saveWrapper(name, new MetricWrapper(MetricWrapper.MetricType.T_COUNTER, new TaggedCounter(registry, name, description, tagName)));
    }

    public static void incrementCounter(String name, String tagValue, long increment) {
        MetricWrapper metricWrapper = getWrapper(name);
        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.T_COUNTER)) {
            TaggedCounter counter = (TaggedCounter) metricWrapper.getMetric();
            counter.increment(increment, tagValue);
        }
    }

    public static void incrementCounter(String name, String tagValue) {
        MetricWrapper metricWrapper = getWrapper(name);
        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.T_COUNTER)) {
            TaggedCounter counter = (TaggedCounter) metricWrapper.getMetric();
            counter.increment(tagValue);
        }
    }

    public static void createMultiTagCounter(String name, String description, String... tagNames) {
        saveWrapper(name, new MetricWrapper(MetricWrapper.MetricType.MT_COUNTER, new MultiTaggedCounter(registry, name, description, tagNames)));
    }

    public static void incrementMultiTagCounter(String name, long increment, String... tagValues) {
        MetricWrapper metricWrapper = getWrapper(name);
        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.MT_COUNTER)) {
            MultiTaggedCounter counter = (MultiTaggedCounter) metricWrapper.getMetric();
            counter.increment(increment, tagValues);
        }
    }

    public static void incrementMultiTagCounter(String name, String... tagsValues) {
        MetricWrapper metricWrapper = getWrapper(name);
        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.MT_COUNTER)) {
            MultiTaggedCounter counter = (MultiTaggedCounter) metricWrapper.getMetric();
            counter.increment(tagsValues);
        }
    }

    public static void createGauge(String name, String description, String tagName) {
        saveWrapper(name, new MetricWrapper(MetricWrapper.MetricType.T_GAUGE, new TaggedGauge(registry, name, description, tagName)));
    }

    public static void setGauge(String name, String tagValue, double value) {
        MetricWrapper metricWrapper = getWrapper(name);
        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.T_GAUGE)) {
            TaggedGauge gauge = (TaggedGauge) metricWrapper.getMetric();
            gauge.set(tagValue, value);
        }
    }

    public static void createMultiTagGauge(String name, String description, String... tagNames) {
        saveWrapper(name, new MetricWrapper(MetricWrapper.MetricType.MT_GAUGE, new MultiTaggedGauge(registry, name, description, tagNames)));
    }

    public static void setMultiTagGauge(String name, double value, String... tagValues) {
        MetricWrapper metricWrapper = getWrapper(name);
        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.MT_GAUGE)) {
            MultiTaggedGauge gauge = (MultiTaggedGauge) metricWrapper.getMetric();
            gauge.set(value, tagValues);
        }
    }

    public static void createTimer(String name, String description, String tagName) {
        saveWrapper(name, new MetricWrapper(MetricWrapper.MetricType.T_TIMER, new TaggedTimer(registry, name, description, tagName)));
    }

    public static void recordTime(String name, String tagValue, Duration duration) {
        MetricWrapper metricWrapper = getWrapper(name);
        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.T_TIMER)) {
            TaggedTimer timer = (TaggedTimer) metricWrapper.getMetric();
            timer.getTimer(tagValue).record(duration);
        }
    }

    public static void recordTime(String name, TimeUnit timeUnit, long timeValue, String tagValue) throws InvalidTimeUnitException {
        MetricWrapper metricWrapper = getWrapper(name);
        long durationMillis = getTimeUnitDuration(timeUnit, timeValue);
        if (durationMillis == -1) {
            String message = String.format("Time unit %s not supported", timeUnit);
            throw new InvalidTimeUnitException(message);
        }

        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.T_TIMER)) {
            TaggedTimer timer = (TaggedTimer) metricWrapper.getMetric();
            timer.getTimer(tagValue).record(Duration.ofMillis(durationMillis));
        }
    }

    public static void createMultiTagTime(String name, String description, String... tagNames) {
        saveWrapper(name, new MetricWrapper(MetricWrapper.MetricType.MT_TIMER, new MultiTaggedTimer(registry, name, description, tagNames)));
    }

    public static void recordMultiTagTime(String name, Duration duration, String... tagValues) {
        MetricWrapper metricWrapper = getWrapper(name);
        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.MT_TIMER)) {
            MultiTaggedTimer timer = (MultiTaggedTimer) metricWrapper.getMetric();
            timer.getTimer(tagValues).record(duration);
        }
    }

    public static void recordMultiTagTime(String name, TimeUnit timeUnit, long timeValue, String... tags) throws InvalidTimeUnitException {
        MetricWrapper metricWrapper = getWrapper(name);
        long durationMillis = getTimeUnitDuration(timeUnit, timeValue);
        if (durationMillis == -1) {
            String message = String.format("Time unit %s not supported", timeUnit);
            throw new InvalidTimeUnitException(message);
        }

        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.MT_TIMER)) {
            MultiTaggedTimer timer = (MultiTaggedTimer) metricWrapper.getMetric();
            timer.getTimer(tags).record(Duration.ofMillis(durationMillis));
        }
    }

    private static long getTimeUnitDuration(TimeUnit timeUnit, long timeValue) {
        switch (timeUnit) {
            case DAYS:
                return TimeUnit.MILLISECONDS.convert(Duration.ofDays(timeValue));
            case HOURS:
                return TimeUnit.MILLISECONDS.convert(Duration.ofHours(timeValue));
            case MINUTES:
                return TimeUnit.MILLISECONDS.convert(Duration.ofMinutes(timeValue));
            case SECONDS:
                return TimeUnit.MILLISECONDS.convert(Duration.ofSeconds(timeValue));
            case MILLISECONDS:
                return timeValue;
            case NANOSECONDS:
                return TimeUnit.MILLISECONDS.convert(Duration.ofNanos(timeValue));
            default:
                return -1;
        }
    }
}
