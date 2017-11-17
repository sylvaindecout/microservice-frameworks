package test.sdc.service.restexpress.client;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.sdc.service.catalogue.DefaultCatalogue;
import test.sdc.service.catalogue.model.Article;

import java.net.URI;

/**
 * Interface with catalogue service.
 */
public class CatalogueClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogueClient.class);

    private static final String HYSTRIX_GROUP_KEY = "pricing-service";
    private static final String HYSTRIX_KEY = "CatalogueClient";

    private final HystrixCommand.Setter setter;

    /**
     * Constructor.
     */
    public CatalogueClient(final URI baseUrl) {
        LOGGER.trace("Initialize {} with base URL {}", HYSTRIX_KEY, baseUrl);
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