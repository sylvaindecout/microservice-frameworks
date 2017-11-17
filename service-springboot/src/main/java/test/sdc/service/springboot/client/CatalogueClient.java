package test.sdc.service.springboot.client;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import test.sdc.service.catalogue.DefaultCatalogue;
import test.sdc.service.catalogue.model.Article;

/**
 * Interface with catalogue service.
 */
@Component
public class CatalogueClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogueClient.class);

    private static final String HYSTRIX_GROUP_KEY = "pricing-service";
    private static final String HYSTRIX_KEY = "CatalogueClient";
    private final HystrixCommand.Setter setter;
    @Value("${service.catalogue.baseUrl}")
    private String baseUrl;

    /**
     * Constructor.
     */
    public CatalogueClient() {
        LOGGER.info("Initialize {} with base URL {}", HYSTRIX_KEY, this.baseUrl);
        this.setter = HystrixCommand.Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey(HYSTRIX_GROUP_KEY))
                .andCommandKey(HystrixCommandKey.Factory.asKey(HYSTRIX_KEY))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(HYSTRIX_KEY));
    }

    /**
     * Find article from reference.
     *
     * @param reference article reference
     * @return article
     */
    public Article find(final String reference) {
        return new HystrixCommand<Article>(this.setter) {

            /** {@inheritDoc} */
            @Override
            protected Article run() {
                return DefaultCatalogue.find(reference);
            }

        }.execute();
    }

}