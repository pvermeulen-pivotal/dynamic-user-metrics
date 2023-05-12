package mil.army.swf.micrometer.metrics.post;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import jakarta.annotation.PostConstruct;
import mil.army.swf.micrometer.metrics.helper.MetricHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Post construct that initializes MetricHelper registry
 */
@Service
public class MetricSetup {

    @Autowired
    private PrometheusMeterRegistry meterRegistry;

    /**
     * Post constructor to initialize MetricHelper registry
     */
    @PostConstruct
    private void atStartup() {
        MetricHelper.setRegistry(meterRegistry);
    }
}
