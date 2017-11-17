package test.sdc.service.springboot.config;

import org.springframework.boot.actuate.autoconfigure.ExportMetricWriter;
import org.springframework.boot.actuate.metrics.jmx.JmxMetricWriter;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.MBeanExporter;

/**
 * Metrics configuration.
 */
@Configuration
class MetricsConfig {

    /**
     * Export metrics to JMX.
     *
     * @param exporter JMX metrics writer
     * @return metrics writer
     */
    @Bean
    @ExportMetricWriter
    MetricWriter metricWriter(final MBeanExporter exporter) {
        return new JmxMetricWriter(exporter);
    }

}