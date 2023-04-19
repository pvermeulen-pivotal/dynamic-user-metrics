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

/**
 * Helper class for creating and updating metrics that will be scrapped by Prometheus
 */
@Slf4j
public class MetricHelper {

    private static PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    private static HashMap<String, MetricWrapper> wrappers = new HashMap<>();

    /**
     * Get the meter registry
     *
     * @return - meter registry
     */
    public static PrometheusMeterRegistry getRegistry() {
        return registry;
    }

    /**
     * Saves the metric wrapper by name
     *
     * @param name - name of the meter
     * @param wrapper - metric wrapper
     */
    private static void saveWrapper(String name, MetricWrapper wrapper) {
        wrappers.put(name, wrapper);
    }

    private static MetricWrapper getWrapper(String name) {
        return wrappers.get(name);
    }

    /**
     * Creates a tagged micrometer counter
     *
     * @param name        - meter name
     * @param description - meter description
     * @param tagName     - tag associated with meter
     */
    public static void createCounter(String name, String description, String tagName) {
        saveWrapper(name, new MetricWrapper(MetricWrapper.MetricType.T_COUNTER, new TaggedCounter(registry, name, description, tagName)));
    }

    /**
     * Increments a previously defined tagged counter with supplied increment value
     *
     * @param name      - meter name
     * @param tagValue  - tag to increment
     * @param increment - increment value
     */
    public static void incrementCounter(String name, String tagValue, long increment) {
        MetricWrapper metricWrapper = getWrapper(name);
        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.T_COUNTER)) {
            TaggedCounter counter = (TaggedCounter) metricWrapper.getMetric();
            counter.increment(increment, tagValue);
        }
    }

    /**
     * Increments a previously defined tag counter
     *
     * @param name     - meter name
     * @param tagValue - tag to increment
     */
    public static void incrementCounter(String name, String tagValue) {
        MetricWrapper metricWrapper = getWrapper(name);
        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.T_COUNTER)) {
            TaggedCounter counter = (TaggedCounter) metricWrapper.getMetric();
            counter.increment(tagValue);
        }
    }

    /**
     * Creates a multi-tag micrometer counter
     *
     * @param name        - meter name
     * @param description - meter description
     * @param tagNames    - set of tag names associated with the meter
     */
    public static void createMultiTagCounter(String name, String description, String... tagNames) {
        saveWrapper(name, new MetricWrapper(MetricWrapper.MetricType.MT_COUNTER, new MultiTaggedCounter(registry, name, description, tagNames)));
    }

    /**
     * Increments a previously defined multi-tag counter with an increment value
     * <p>
     * The number of tagValues must be the same as the number of tag names defined when created
     *
     * @param name      - meter name
     * @param increment - increment number
     * @param tagValues - set of tag values to increment
     */
    public static void incrementMultiTagCounter(String name, long increment, String... tagValues) {
        MetricWrapper metricWrapper = getWrapper(name);
        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.MT_COUNTER)) {
            MultiTaggedCounter counter = (MultiTaggedCounter) metricWrapper.getMetric();
            counter.increment(increment, tagValues);
        }
    }

    /**
     * Increments a previously defined multi-tag counter
     *
     * @param name      - meter name
     * @param tagValues - set of tag values to increment
     */
    public static void incrementMultiTagCounter(String name, String... tagValues) {
        MetricWrapper metricWrapper = getWrapper(name);
        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.MT_COUNTER)) {
            MultiTaggedCounter counter = (MultiTaggedCounter) metricWrapper.getMetric();
            counter.increment(tagValues);
        }
    }

    /**
     * Creates a tagged micrometer gauge
     *
     * @param name        - meter name
     * @param description - meter description
     * @param tagName     - tag associated with the meter
     */
    public static void createGauge(String name, String description, String tagName) {
        saveWrapper(name, new MetricWrapper(MetricWrapper.MetricType.T_GAUGE, new TaggedGauge(registry, name, description, tagName)));
    }

    /**
     * Set the value of a previously defined tagged gauge
     *
     * @param name     - meter name
     * @param tagValue - tag value to set
     * @param value    - the value to set the gauge
     */
    public static void setGauge(String name, String tagValue, double value) {
        MetricWrapper metricWrapper = getWrapper(name);
        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.T_GAUGE)) {
            TaggedGauge gauge = (TaggedGauge) metricWrapper.getMetric();
            gauge.set(tagValue, value);
        }
    }

    /**
     * Creates a multi-tagged tagged micrometer gauge
     *
     * @param name        - meter name
     * @param description - meter description
     * @param tagNames    - tags associated with the meter
     */
    public static void createMultiTagGauge(String name, String description, String... tagNames) {
        saveWrapper(name, new MetricWrapper(MetricWrapper.MetricType.MT_GAUGE, new MultiTaggedGauge(registry, name, description, tagNames)));
    }

    /**
     * Set the value of a previously defined multi-tagged gauge
     *
     * @param name      - meter name
     * @param tagValues - a set of tag values to set
     * @param value     - the value to set the gauge
     */
    public static void setMultiTagGauge(String name, double value, String... tagValues) {
        MetricWrapper metricWrapper = getWrapper(name);
        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.MT_GAUGE)) {
            MultiTaggedGauge gauge = (MultiTaggedGauge) metricWrapper.getMetric();
            gauge.set(value, tagValues);
        }
    }

    /**
     * Creates a tagged micrometer timer
     *
     * @param name        - meter name
     * @param description - meter description
     * @param tagName     - tag associated with the meter
     */
    public static void createTimer(String name, String description, String tagName) {
        saveWrapper(name, new MetricWrapper(MetricWrapper.MetricType.T_TIMER, new TaggedTimer(registry, name, description, tagName)));
    }

    /**
     * Records the time for a tagged timer
     *
     * @param name     - meter name
     * @param tagValue - tag value to record time to
     * @param duration - duration of time to record
     */
    public static void recordTime(String name, String tagValue, Duration duration) {
        MetricWrapper metricWrapper = getWrapper(name);
        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.T_TIMER)) {
            TaggedTimer timer = (TaggedTimer) metricWrapper.getMetric();
            timer.getTimer(tagValue).record(duration);
        }
    }

    /**
     * Records the time for a tagged timer
     *
     * @param name      - meter name
     * @param timeUnit  - time unit of the time value
     * @param timeValue - time value
     * @param tagValue  - tag value to record time to
     * @throws InvalidTimeUnitException - invalid time unit specified
     */
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

    /**
     * Creates a multi-tagged micrometer timer
     *
     * @param name        - meter name
     * @param description - meter description
     * @param tagNames    - set of tags associated with the meter
     */
    public static void createMultiTagTime(String name, String description, String... tagNames) {
        saveWrapper(name, new MetricWrapper(MetricWrapper.MetricType.MT_TIMER, new MultiTaggedTimer(registry, name, description, tagNames)));
    }

    /**
     * Records the time for a multi-tagged timer
     *
     * @param name      - meter name
     * @param duration  - duration of time to record
     * @param tagValues - set of tag values to record time to
     */
    public static void recordMultiTagTime(String name, Duration duration, String... tagValues) {
        MetricWrapper metricWrapper = getWrapper(name);
        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.MT_TIMER)) {
            MultiTaggedTimer timer = (MultiTaggedTimer) metricWrapper.getMetric();
            timer.getTimer(tagValues).record(duration);
        }
    }

    /**
     * Records the time for a multi-tagged timer
     *
     * @param name      - meter name
     * @param timeUnit  - time unit of the time value
     * @param timeValue - time value
     * @param tagValues - set of tag values to record time to
     * @throws InvalidTimeUnitException - invalid time unit specified
     */
    public static void recordMultiTagTime(String name, TimeUnit timeUnit, long timeValue, String... tagValues) throws InvalidTimeUnitException {
        MetricWrapper metricWrapper = getWrapper(name);
        long durationMillis = getTimeUnitDuration(timeUnit, timeValue);
        if (durationMillis == -1) {
            String message = String.format("Time unit %s not supported", timeUnit);
            throw new InvalidTimeUnitException(message);
        }

        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.MT_TIMER)) {
            MultiTaggedTimer timer = (MultiTaggedTimer) metricWrapper.getMetric();
            timer.getTimer(tagValues).record(Duration.ofMillis(durationMillis));
        }
    }

    /**
     * return the number of milliseconds for the time value as specified by the time unit
     *
     * @param timeUnit
     * @param timeValue
     * @return
     */
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
