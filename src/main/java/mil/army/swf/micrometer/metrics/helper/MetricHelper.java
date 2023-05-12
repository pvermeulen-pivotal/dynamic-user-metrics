package mil.army.swf.micrometer.metrics.helper;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheus.PrometheusMeterRegistry;

import lombok.extern.slf4j.Slf4j;
import mil.army.swf.micrometer.metrics.*;
import mil.army.swf.micrometer.metrics.exception.InvalidOptionType;
import mil.army.swf.micrometer.metrics.exception.InvalidTimeUnitException;
import mil.army.swf.micrometer.metrics.wrapper.DoubleWrapper;
import mil.army.swf.micrometer.metrics.wrapper.MetricWrapper;
import org.springframework.stereotype.Service;

import java.time.Duration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Helper class for creating and updating metrics that will be scrapped by Prometheus
 */
@Service
@Slf4j
public class MetricHelper {

    private static PrometheusMeterRegistry registry;

    private static final HashMap<String, MetricWrapper> wrappers = new HashMap<>();

    private MetricHelper() {
    }

    /**
     * Get the meter registry
     *
     * @return meter registry
     */
    public static PrometheusMeterRegistry getRegistry() {
        return registry;
    }

    /**
     * Sets the prometheus registry
     *
     * @param registry1 prometheus registry
     */
    public static void setRegistry(PrometheusMeterRegistry registry1) {
        registry = registry1;
    }

    /**
     * Saves the metric wrapper by name
     *
     * @param name name of the meter
     * @param wrapper metric wrapper
     */
    private static void saveWrapper(String name, MetricWrapper wrapper) {
        wrappers.put(name, wrapper);
    }

    /**
     * Get the wrapper by name
     *
     * @param name wrapper name
     * @return metric wrapper
     */
    private static MetricWrapper getWrapper(String name) {
        return wrappers.get(name);
    }

    /**
     * Creates a tagged micrometer counter
     *
     * @param name meter name
     * @param description meter description
     * @param tagName tag name associated with timer
     * @param tagValue tag value associated with timer
     */
    public static void createTagCounter(String name, String description, String tagName, String tagValue) {
        Counter counter = Counter.builder(name).description(description).tag(tagName, tagValue).register(registry);
        saveWrapper(name, new MetricWrapper(MetricWrapper.MetricType.T_COUNTER, counter, null));
    }

    /**
     * Increments a defined tagged counter with supplied incremental value
     *
     * @param name meter name
     * @param increment increment value
     */
    public static void incrementTagCounter(String name, long increment) {
        MetricWrapper metricWrapper = getWrapper(name);
        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.T_COUNTER)) {
            ((Counter) metricWrapper.getMetric()).increment(increment);
        } else {
            log.error("no tagged counter {} found to increment", name);
        }
    }

    /**
     * Increments a defined tag counter
     *
     * @param name meter name
     */
    public static void incrementTagCounter(String name) {
        MetricWrapper metricWrapper = getWrapper(name);
        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.T_COUNTER)) {
            ((Counter) metricWrapper.getMetric()).increment();
        } else {
            log.error("no tagged counter {} found to increment", name);
        }
    }

    /**
     * Creates a multi-tag micrometer counter
     *
     * @param name meter name
     * @param description meter description
     * @param tagNames string array of tag names associated with timer
     * @param tagValues string array of tag values associated with timer
     */
    public static void createMultiTagCounter(String name, String description, String[] tagNames,
                                             String[] tagValues) {
        if (tagNames.length != tagValues.length) {
            throw new IllegalArgumentException("Counter tags mismatch! Expected args are " + Arrays.toString(tagNames) + ", provided tags are " + Arrays.toString(tagValues));
        }
        Counter counter = Counter.builder(name).description(description)
                .tag(Arrays.toString(tagNames), Arrays.toString(tagValues)).register(registry);
        saveWrapper(name, new MetricWrapper(MetricWrapper.MetricType.MT_COUNTER, counter, null));
    }

    /**
     * Increments a multi-tag counter with an increment value
     *
     * @param name meter name
     * @param increment increment number
     */
    public static void incrementMultiTagCounter(String name, long increment) {
        MetricWrapper metricWrapper = getWrapper(name);
        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.MT_COUNTER)) {
            ((Counter) metricWrapper.getMetric()).increment(increment);
        } else {
            log.error("no multi-tagged counter {} found to increment", name);
        }
    }

    /**
     * Increments multi-tag counter
     *
     * @param name meter name
     */
    public static void incrementMultiTagCounter(String name) {
        MetricWrapper metricWrapper = getWrapper(name);
        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.MT_COUNTER)) {
            ((Counter) metricWrapper.getMetric()).increment();
        } else {
            log.error("no multi-tagged counter {} found to increment", name);
        }
    }

    /**
     * Creates a tagged micrometer gauge
     *
     * @param name meter name
     * @param description meter description
     * @param tagName tag name associated with timer
     * @param tagValue tag value associated with timer
     */
    public static void createTagGauge(String name, String description, String tagName, String tagValue) {
        DoubleWrapper valueHolder = new DoubleWrapper(0d);
        Gauge gauge = Gauge.builder(name, valueHolder, DoubleWrapper::getValue).description(description).tags(tagName, tagValue).register(registry);
        saveWrapper(name, new MetricWrapper(MetricWrapper.MetricType.T_GAUGE, gauge, valueHolder));
    }

    /**
     * Set the value of a defined tagged gauge
     *
     * @param name meter name
     * @param value the value to set the gauge
     */
    public static void setTagGauge(String name, double value) {
        MetricWrapper metricWrapper = getWrapper(name);
        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.T_GAUGE)) {
            metricWrapper.getDoubleWrapper().setValue(value);
        } else {
            log.error("no tagged gauge {} found to set value", name);
        }
    }

    /**
     * Creates a multi-tagged tagged micrometer gauge
     *
     * @param name meter name
     * @param description meter description
     * @param tagNames string array of tag names associated with timer
     * @param tagValues string array of tag values associated with timer
     *
     * @throws IllegalArgumentException tagName and tagValue array lengths are unequal
     */
    public static void createMultiTagGauge(String name, String description, String[] tagNames, String[] tagValues) throws IllegalArgumentException {
        if (tagNames.length != tagValues.length) {
            throw new IllegalArgumentException("Gauge tags mismatch! Expected args are " + Arrays.toString(tagNames) + ", provided tags are " + Arrays.toString(tagValues));
        }
        DoubleWrapper valueHolder = new DoubleWrapper(0d);
        Gauge gauge = Gauge.builder(name, valueHolder, DoubleWrapper::getValue).description(description).tag(Arrays.toString(tagNames), Arrays.toString(tagValues)).register(registry);
        saveWrapper(name, new MetricWrapper(MetricWrapper.MetricType.MT_GAUGE, gauge, valueHolder));
    }

    /**
     * Set the value of a defined multi-tagged gauge
     *
     * @param name meter name
     * @param value the value to set the gauge
     */
    public static void setMultiTagGauge(String name, double value) {
        MetricWrapper metricWrapper = getWrapper(name);
        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.MT_GAUGE)) {
            metricWrapper.getDoubleWrapper().setValue(value);
        } else {
            log.error("no multi-tagged gauge {} found to set value", name);
        }
    }

    /**
     * Creates a tagged micrometer timer
     *
     * @param name meter name
     * @param description meter description
     * @param tagName tag name associated with timer
     * @param tagValue tag value associated with timer
     */
    public static void createTagTimer(String name, String description, String tagName, String tagValue) {
        Timer timer = Timer.builder(name).description(description).tag(tagName, tagValue).register(registry);
        saveWrapper(name, new MetricWrapper(MetricWrapper.MetricType.T_TIMER, timer, null));
    }

    /**
     * Creates a histogram timer
     *
     * @param name meter name
     * @param description meter description
     * @param tagName tag name associated with timer
     * @param tagValue tag value associated with timer
     */
    public static void createTagHistogramTimer(String name, String description,
                                               String tagName, String tagValue) {
        Timer timer = Timer.builder(name).description(description)
                .tag(tagName, tagValue)
                .publishPercentileHistogram().register(registry);
        saveWrapper(name, new MetricWrapper(MetricWrapper.MetricType.T_TIMER, timer, null));
    }

    /**
     * Creates a histogram timer with expected values
     *
     * @param name meter name
     * @param description meter description
     * @param type timer type
     * @param min minimum expected value
     * @param max maximum expected value
     * @param tagName tag name associated with timer
     * @param tagValue tag value associated with timer
     */
    public static void createTagHistogramExpectedValueTimer(String name, String description,
                                                            TimerTypes.TimerType type, Duration min, Duration max,
                                                            String tagName, String tagValue) {
        if (type.equals(TimerTypes.TimerType.EXPV)) {
            Timer timer = Timer.builder(name).description(description)
                    .tag(tagName, tagValue).maximumExpectedValue(min).maximumExpectedValue(max)
                    .publishPercentileHistogram().register(registry);
            saveWrapper(name, new MetricWrapper(MetricWrapper.MetricType.T_TIMER, timer, null));
        }
    }

    /**
     * Creates a histogram timer with expected value and service level objective or service level agreement
     *
     * @param name meter name
     * @param description meter description
     * @param type timer type
     * @param slosla duration for SLA/SLO
     * @param min minimum expected value
     * @param max maximum expected value
     * @param tagName tag name associated with timer
     * @param tagValue tag value associated with timer
     *
     * @throws InvalidOptionType invalid timer type specified
     */
    public static void createTagHistogramExpectedValueSloSlaTimer(String name, String description, TimerTypes.TimerType type,
                                                                  Duration slosla, Duration min, Duration max, String tagName,
                                                                  String tagValue) throws InvalidOptionType {
        Timer timer = null;
        switch (type) {
            case SLO -> {
                timer = Timer.builder(name).description(description)
                        .tag(tagName, tagValue)
                        .publishPercentileHistogram()
                        .sla(slosla)
                        .minimumExpectedValue(min)
                        .maximumExpectedValue(max)
                        .register(registry);
            }
            case SLA -> {
                timer = Timer.builder(name).description(description)
                        .tag(tagName, tagValue)
                        .publishPercentileHistogram()
                        .serviceLevelObjectives(slosla)
                        .minimumExpectedValue(min)
                        .maximumExpectedValue(max)
                        .register(registry);
            }
            default -> {
                throw new InvalidOptionType("Invalid option specified, options are SLA/SLO type specified" + type);
            }
        }
        saveWrapper(name, new MetricWrapper(MetricWrapper.MetricType.T_TIMER, timer, null));
    }

    /**
     * Records the time for a tagged timer
     *
     * @param name meter name
     * @param duration duration of time to record
     */
    public static void recordTagTime(String name, Duration duration) {
        MetricWrapper metricWrapper = getWrapper(name);
        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.T_TIMER)) {
            ((Timer) metricWrapper.getMetric()).record(duration);
        } else {
            log.error("no tagged timer {} found to record time", name);
        }
    }

    /**
     * Records the time for a tagged timer
     *
     * @param name meter name
     * @param timeUnit time unit of the time value
     * @param timeValue time value
     *
     * @throws InvalidTimeUnitException invalid time unit specified
     */
    public static void recordTagTime(String name, TimeUnit timeUnit, long timeValue) throws InvalidTimeUnitException {
        MetricWrapper metricWrapper = getWrapper(name);
        long durationMillis = getTimeUnitDuration(timeUnit, timeValue);
        if (durationMillis == -1) {
            String message = String.format("Time unit %s not supported", timeUnit);
            throw new InvalidTimeUnitException(message);
        }

        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.T_TIMER)) {
            ((Timer) metricWrapper.getMetric()).record(Duration.ofMillis(durationMillis));
        } else {
            log.error("no tagged timer {} found to record time", name);
        }
    }

    /**
     * Create multi-tagged timer
     *
     * @param name meter name
     * @param description meter description
     * @param tagNames string array of tag names associated with timer
     * @param tagValues string array of tag values associated with timer
     */
    public static void createMultiTagTimer(String name, String description, String[] tagNames, String[] tagValues) {
        if (tagNames.length != tagValues.length) {
            throw new IllegalArgumentException("Timer tags mismatch! Expected args are " + Arrays.toString(tagNames) + ", provided tags are " + Arrays.toString(tagValues));
        }
        Timer timer = Timer.builder(name).description(description)
                .tag(Arrays.toString(tagNames), Arrays.toString(tagValues)).register(registry);
        saveWrapper(name, new MetricWrapper(MetricWrapper.MetricType.MT_TIMER, timer, null));
    }

    /**
     * Create a multi-tagged histogram timer
     *
     * @param name meter name
     * @param description meter description
     * @param tagNames string array of tag names associated with timer
     * @param tagValues string array of tag values associated with timer
     */
    public static void createMultiTagHistogramTimer(String name, String description, String[] tagNames, String[] tagValues) {
        if (tagNames.length != tagValues.length) {
            throw new IllegalArgumentException("Timer tags mismatch! Expected args are " + Arrays.toString(tagNames) + ", provided tags are " + Arrays.toString(tagValues));
        }

        Timer timer = Timer.builder(name).description(description)
                .tag(Arrays.toString(tagNames), Arrays.toString(tagValues))
                .publishPercentileHistogram().register(registry);
        saveWrapper(name, new MetricWrapper(MetricWrapper.MetricType.MT_TIMER, timer, null));
    }

    /**
     * Create a multi-tagged expected value timer
     *
     * @param name meter name
     * @param description meter description
     * @param type timer type
     * @param min minimum expected value
     * @param max maximum expected value
     * @param tagNames string array of tag names associated with timer
     * @param tagValues tring array of tag values associated with timer
     */
    public static void createMultiTagHistogramExpectedValueTimer(String name, String description, TimerTypes.TimerType type,
                                                                 Duration min, Duration max, String[] tagNames, String[] tagValues) {
        if (tagNames.length != tagValues.length) {
            throw new IllegalArgumentException("Timer tags mismatch! Expected args are " + Arrays.toString(tagNames) + ", provided tags are " + Arrays.toString(tagValues));
        }

        if (type.equals(TimerTypes.TimerType.EXPV)) {
            Timer timer = Timer.builder(name).description(description)
                    .tag(Arrays.toString(tagNames), Arrays.toString(tagValues))
                    .maximumExpectedValue(min).maximumExpectedValue(max)
                    .publishPercentileHistogram().register(registry);
            saveWrapper(name, new MetricWrapper(MetricWrapper.MetricType.MT_TIMER, timer, null));
        }
    }

    /**
     * Create a multi-tagged slo/sla timer
     *
     * @param name meter name
     * @param description meter description
     * @param type timer type
     * @param slosla duration for SLA/SLO
     * @param min minimum expected value
     * @param max maximum expected value
     * @param tagNames string array of tag names associated with timer
     * @param tagValues string array of tag values associated with timer
     *
     * @throws InvalidOptionType timer tagName and tagValue array are unequal
     */
    public static void createMultiTagHistogramExpectedValueSloSlaTimer(String name, String description,
                                                                       TimerTypes.TimerType type, Duration slosla, Duration min,
                                                                       Duration max, String[] tagNames, String[] tagValues) throws InvalidOptionType {
        if (tagNames.length != tagValues.length) {
            throw new IllegalArgumentException("Timer tags mismatch! Expected args are " + Arrays.toString(tagNames) + ", provided tags are " + Arrays.toString(tagValues));
        }

        Timer timer = null;
        switch (type) {
            case SLO -> {
                timer = Timer.builder(name).description(description)
                        .tag(Arrays.toString(tagNames), Arrays.toString(tagValues))
                        .publishPercentileHistogram()
                        .sla(slosla)
                        .minimumExpectedValue(min)
                        .maximumExpectedValue(max)
                        .register(registry);
            }
            case SLA -> {
                timer = Timer.builder(name).description(description)
                        .tag(Arrays.toString(tagNames), Arrays.toString(tagValues))
                        .publishPercentileHistogram()
                        .serviceLevelObjectives(slosla)
                        .minimumExpectedValue(min)
                        .maximumExpectedValue(max)
                        .register(registry);
            }
            default -> {
                throw new InvalidOptionType("Invalid option specified, options are SLA/SLO type specified" + type);
            }
        }
        saveWrapper(name, new MetricWrapper(MetricWrapper.MetricType.MT_TIMER, timer, null));
    }


    /**
     * Records the time for a multi-tagged timer
     *
     * @param name meter name
     * @param duration duration of time to record
     */
    public static void recordMultiTagTime(String name, Duration duration) {
        MetricWrapper metricWrapper = getWrapper(name);
        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.MT_TIMER)) {
            ((Timer) metricWrapper.getMetric()).record(duration);
        } else {
            log.error("no multi-tagged timer {} found to record time", name);
        }
    }

    /**
     * Records the time for a multi-tagged timer
     *
     * @param name meter name
     * @param timeUnit time unit of the time value
     * @param timeValue time value
     *
     * @throws InvalidTimeUnitException invalid time unit specified
     */
    public static void recordMultiTagTime(String name, TimeUnit timeUnit, long timeValue) throws InvalidTimeUnitException {
        MetricWrapper metricWrapper = getWrapper(name);
        long durationMillis = getTimeUnitDuration(timeUnit, timeValue);
        if (durationMillis == -1) {
            String message = String.format("Time unit %s not supported", timeUnit);
            throw new InvalidTimeUnitException(message);
        }

        if (metricWrapper != null && metricWrapper.getType().equals(MetricWrapper.MetricType.MT_TIMER)) {
            ((Timer) metricWrapper.getMetric()).record(Duration.ofMillis(durationMillis));
        } else {
            log.error("no multi-tagged timer {} found to record time", name);
        }
    }

    /**
     * return the number of milliseconds for the time value as specified by the time unit
     *
     * @param timeUnit Time unit like hours, milliseconds, etc
     * @param timeValue the time value for the time unit
     * @return time value
     */
    private static long getTimeUnitDuration(TimeUnit timeUnit, long timeValue) {
        return switch (timeUnit) {
            case DAYS -> TimeUnit.MILLISECONDS.convert(Duration.ofDays(timeValue));
            case HOURS -> TimeUnit.MILLISECONDS.convert(Duration.ofHours(timeValue));
            case MINUTES -> TimeUnit.MILLISECONDS.convert(Duration.ofMinutes(timeValue));
            case SECONDS -> TimeUnit.MILLISECONDS.convert(Duration.ofSeconds(timeValue));
            case MILLISECONDS -> timeValue;
            case NANOSECONDS -> TimeUnit.MILLISECONDS.convert(Duration.ofNanos(timeValue));
            default -> -1;
        };
    }
}