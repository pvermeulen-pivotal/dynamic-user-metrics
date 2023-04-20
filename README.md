# Dynamic Tagged User Metrics

#### Vector

## Meters
There are three (3) types of meters implemented by Micrometer:
1. Counter - counts the number of times an activity has been performed.
2. Gauge - measure a point in time for an activity.
3. Timer - records the time an activity took to execute.

### Tagged Meters
Tagged meters are Micrometer counters, gauges and timers that are created using a single tag name which act
like a category for the meter.

### Tagged Counter

Counters monitor monotonically increasing values. Counters may never be reset to a lesser value. 
If you need to track a value that goes up and down, use a Gauge.

##### Create tagged counter
    TaggedCounter(
        name: counter name
        description: counter description
        tagName: category or identifier tag
    )
##### Increment tagged counter by 1
    Increment TaggedCounter(
        name: counter name
        tagValue: value for tag name
    )

##### Increment tagged counter by value
    Increment TaggedCounter(
        name: counter name
        value: increment value
        tagValue: value for tag name
    )
##### Example

    Create Tagged Counter: createTaggedCounter(name="dogs",description="dog counter",tagName="breed")
    
    Increment Tagged Counter (BY 1): increment(name="dogs", tagValue="Labrador")

    Increment Tagged Counter (BY 20): increment(name="dogs", value=20l, tagValue="Labrador)

### Tagged Gauge

A gauge tracks a value that may go up or down. 
The value that is published for gauges is an instantaneous sample of the gauge at publishing time.

##### Create tagged gauge
    TaggedGauge(
        name: gauge name
        description: gauge description
        tagName: category or identifier tag
    )

##### Set tagged gauge value
    Set TaggedGauge(
        name: gauge name
        tagValue: value for tag name
        value: value to set in gauge
    )

#### Example
    Create Tagged Gauge: createTaggedGauge(name="dogs",description="dog gauge",tagName="DogsInKennel")
    
    Set Tagged Gauge: set(name="dogs", tagValue="DogsInKennel", value=60d)

### Tagged Timer

Timer intended to track of a large number of short running events. Example would be something like an HTTP request. 
Though "short running" is a bit subjective the assumption is that it should be under a minute.

##### Create tagged timer
    TaggedTimer(
        name: timer name
        description: timer description
        tagName: category or identifier tag
    )

##### Record tagged timer
    Record TaggedTimer(
        name: timer name
        tagValue: value for tag name
        duration: Duration.of
    )

    Record TaggedTimer(
        name: timer name
        timeUnit: TimeUnits
        timeValue: value for the time unit 
        tagValue: value for tag name
    )

#### Example

    Create Tagged Timer: createTaggedTimer(name="dogs",description="dog timer",tagName="checkin")
    
    Record Tagged Timer: record(name="dogs", tagValue="checkin", duration=Duration.ofSeconds(27))

    Record Tagged Timer: record(name="dogs", timeUnit=TimeUnit.Seconds, timeValue=27l, tagValue="checkin")

### Multi-tagged Meters

Multi-tagged meters are Micrometer counters, gauges and timers that are created using a set of tag names which act
like a set of categories for the meter. 

### Multi-tagged Counter

Counters monitor monotonically increasing values. Counters may never be reset to a lesser value.
If you need to track a value that goes up and down, use a Gauge.

##### Create multi-tagged counter
    MultiTaggedCounter(
        name: counter name
        description: counter description
        tagNames: set of tag names or identifiers
    )

##### Increment multi-tagged counter by 1
    Increment MultiTaggedCounter(
        name: counter name
        tagValues: set of tag values
    )

##### Increment multi-tagged counter by value
    Increment MultiTaggedCounter(
        name: counter name
        value: increment value
        tagValues: set of tag values 
    )
##### Example

    Create MultiTagged Counter: createTaggedCounter(name="cities",description="cities counter",tagNames="state city")
    
    Increment MultiTagged Counter (BY 1): increment(name="cities", tagValues="Texas, Austin")

    Increment MultiTagged Counter (BY 20): increment(name="cities", value=20l, tagValues="Texas, Austin")


### Multi-tagged Gauge

A gauge tracks a value that may go up or down.
The value that is published for gauges is an instantaneous sample of the gauge at publishing time.

##### Create multi-tagged gauge
    MultiTaggedGauge(
        name: gauge name
        description: gauge description
        tagNames: set of tag names or category
    )

##### Set multi-tagged gauge value
    Set MultiTaggedGauge(
        name: gauge name
        value: value to set in gauge
        tagValues: value for tag name
    )

#### Example
    Create MultiTagged Gauge: createMultiTaggedGauge(name="weekly-high-tides",description="weekly high tide gauge",tagNames="city, day"
    
    Set MultiTagged Gauge: set(name="weekly-high-tides", value=1.53d, tagValues="Galveston, Monday)

### Multi-tagged Timer

Timer intended to track of a large number of short running events. Example would be something like an HTTP request.
Though "short running" is a bit subjective the assumption is that it should be under a minute.

##### Create multi-tagged timer
    MultiTaggedTimer(
        name: timer name
        description: timer description
        tagNames: set of tag names or categories
    )

##### Record multi-tagged timer
    Record MultiTaggedTimer(
        name: timer name
        duration: Duration.of
        tagValues: set of tag values for tag names
    )

    Record MultiTaggedTimer(
        name: timer name
        timeUnit: TimeUnits
        timeValue: value for the time unit 
        tagValues: set of tag values for tag names
    )

#### Example

    Create MultiTagged Timer: createTaggedTimer(name="action-timer",description="action timer",tagNames="who, action"

    Record MultiTagged Timer: record(name="action-timer", duration=Duration.ofMinutes(27), tagValues="Eric, make-dinner")

    Record MultiTagged Timer: record(name="action-timer", timeUnit=TimeUnit.Seconds, timeValue=27l, tagValues"Eric, make-dinner")

#### Dependencies 

#### Prometheus ####
   `io.prometheus.simpleclient`

   `io.prometheus.simpleclient_common`

   `io.prometheus.simpleclient_httpserver`
#### Micrometer ####
   `io.micrometer.micrometer-core`

   `io.micrometer.micrometer-registry-prometheus`

## MetricHelper 

The MetricHelper class provides the implementation for both tagged and multi-tagged meters. The implementation allows application teams the capability to enable metrics 
in their Spring Boot applications and have these metrics exposed for scrapping by Prometheus.

Refer to the javadoc for details:

[MetricHelper class]("file:///Users/pvermeulen/Projects/dynamic-user-metrics/apidocs/mil/army/swf/micrometer/metrics/helper/Metric-Helper.html")

## Spring Annotations



## Spring Actuator

