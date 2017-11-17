package test.sdc.service.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger (REST interface documentation) configuration.
 */
@Configuration
@EnableSwagger2
class SwaggerConfig {

    /**
     * Configure Swagger.
     *
     * @return Swagger entry point
     */
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("Pricing service")
                        .contact(new Contact(
                                "Sylvain Decout",
                                "https://github.com/sylvaindecout",
                                "sdecout@xebia.fr"))
                        .build())
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("test.sdc.service.springboot"))
                .paths(PathSelectors.any())
                .build();
    }

}