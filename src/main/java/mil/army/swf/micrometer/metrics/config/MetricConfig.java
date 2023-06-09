package mil.army.swf.micrometer.metrics.config;

import io.micrometer.core.instrument.Clock;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to create prometheus and collector registry beans
 */
@Configuration
@Slf4j
public class MetricConfig {

    /**
     * Create prometheus meter registry if bean doesn't exist
     *
     * @param collectorRegistry - spring registry
     * @return                  - PrometheusMeterRegistry
     */
    @Bean
    @ConditionalOnMissingBean(PrometheusMeterRegistry.class)
    public io.micrometer.prometheus.PrometheusMeterRegistry prometheusMeterRegistry(io.prometheus.client.CollectorRegistry collectorRegistry) {
        log.info("Creating prometheus meter registry");
        return new PrometheusMeterRegistry(PrometheusConfig.DEFAULT,collectorRegistry, Clock.SYSTEM );
    }

    /**
     * Create collector registry if bean doesn't exist
     *
     * @return - default collector registry
     */
    @Bean
    @ConditionalOnMissingBean(CollectorRegistry.class)
    public io.prometheus.client.CollectorRegistry collectorRegistry(){
        log.info("Creating collector registry");
        return CollectorRegistry.defaultRegistry;
    }
}
