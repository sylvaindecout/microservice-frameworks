package test.sdc.service.springboot.client;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import test.sdc.service.catalogue.model.Article;
import test.sdc.service.discount.DefaultDiscountPolicy;
import test.sdc.service.discount.model.Discount;

import java.net.URI;
import java.util.Optional;

/**
 * Interface with discount policy service.
 */
@Component
public class DiscountPolicyClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscountPolicyClient.class);

    private static final String HYSTRIX_GROUP_KEY = "pricing-service";
    private static final String HYSTRIX_KEY = "DiscountPolicyClient";
    private final RestTemplate restTemplate;
    private final HystrixCommand.Setter setter;
    @Value("${service.discountPolicy.baseUrl}")
    private String baseUrl;

    /**
     * Constructor.
     *
     * @param restTemplate Spring's central class for synchronous client-side HTTP access
     */
    public DiscountPolicyClient(final RestTemplate restTemplate) {
        LOGGER.info("Initialize {} with base URL {}", HYSTRIX_KEY, this.baseUrl);
        this.restTemplate = restTemplate;
        this.setter = HystrixCommand.Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey(HYSTRIX_GROUP_KEY))
                .andCommandKey(HystrixCommandKey.Factory.asKey(HYSTRIX_KEY))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(HYSTRIX_KEY));
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
                final URI uri = UriComponentsBuilder.fromHttpUrl(DiscountPolicyClient.this.baseUrl)
                        .path("policy")
                        .queryParam("price", article.getPrice().getValueAsDollars())
                        .build().toUri();
                LOGGER.trace("Calling URI: {}", uri);
                return DiscountPolicyClient.this.restTemplate.getForObject(uri, Discount.class);
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