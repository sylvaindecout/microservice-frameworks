package test.sdc.service.restexpress.client;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.sdc.service.catalogue.model.Article;
import test.sdc.service.discount.DefaultDiscountPolicy;
import test.sdc.service.discount.model.Discount;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.net.URI;
import java.util.Optional;

/**
 * Interface with discount policy service.
 */
public class DiscountPolicyClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscountPolicyClient.class);

    private static final String HYSTRIX_GROUP_KEY = "pricing-service";
    private static final String HYSTRIX_KEY = "DiscountPolicyClient";

    private final WebTarget target;
    private final HystrixCommand.Setter setter;

    /**
     * Constructor.
     */
    public DiscountPolicyClient(final URI baseUrl) {
        LOGGER.trace("Initialize {} with base URL {}", HYSTRIX_KEY, baseUrl);
        this.target = initClient().target(baseUrl);
        this.setter = HystrixCommand.Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey(HYSTRIX_GROUP_KEY))
                .andCommandKey(HystrixCommandKey.Factory.asKey(HYSTRIX_KEY))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(HYSTRIX_KEY));
    }

    /**
     * Initialize object used to bootstrap REST client instances.
     *
     * @return object used to bootstrap REST client instances
     */
    private static Client initClient() {
        return ClientBuilder.newClient(new ClientConfig()
                .register(JacksonFeature.class));
    }

    /**
     * Log fall-back.
     *
     * @param command       Hystrix command object
     * @param message       message pattern
     * @param messageParams message parameters
     */
    private static void logFallback(final HystrixCommand<?> command, final String message, final Object... messageParams) {
        final String messageWithParams = String.format(message, messageParams);
        if (command.isCircuitBreakerOpen()) {
            LOGGER.trace("{} - Cause: circuit is OPEN", messageWithParams);
        } else {
            final Throwable exception = command.getExecutionException();
            LOGGER.warn("{} - Cause: {}", messageWithParams,
                    exception == null ? null : exception.getMessage());
            LOGGER.debug("Stacktrace: ", exception);
        }
    }

    /**
     * Get discount that is applicable to input article.
     *
     * @param article article
     * @return discount
     */
    public Optional<Discount> getApplicableDiscount(final Article article) {
        return Optional.ofNullable(new HystrixCommand<Discount>(this.setter) {

            /** {@inheritDoc} */
            @Override
            protected Discount run() {
                return DiscountPolicyClient.this.target.path("policy")
                        .queryParam("price", article.getPrice().getValueAsDollars())
                        .request()
                        .get(Discount.class);
            }

            /** {@inheritDoc} */
            @Override
            protected Discount getFallback() {
                logFallback(this, "Failed to retrieve appropriate discount policy - using default");
                return DefaultDiscountPolicy.getApplicableDiscount(article);
            }

        }.execute());
    }

}