package test.sdc.service.springboot.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * REST template (Spring's central class for synchronous client-side HTTP access) configuration.
 */
@Configuration
class RestTemplateConfig {

    /**
     * Initialize REST template.
     *
     * @param builder builder
     * @return REST template
     */
    @Bean
    public RestTemplate rest(final RestTemplateBuilder builder) {
        return builder.build();
    }

}