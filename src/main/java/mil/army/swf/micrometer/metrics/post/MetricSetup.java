package mil.army.swf.micrometer.metrics.post;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import mil.army.swf.micrometer.metrics.helper.MetricHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Post construct that initializes MetricHelper registry
 */
@Service
@Slf4j
public class MetricSetup {

    @Autowired
    private PrometheusMeterRegistry meterRegistry;

    /**
     * Post constructor to initialize MetricHelper registry
     */
    @PostConstruct
    private void atStartup() {
        log.info("Setting helper meter regis=try");
        MetricHelper.setRegistry(meterRegistry);
    }
}
