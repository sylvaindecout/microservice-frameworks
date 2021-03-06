package test.sdc.service.restexpress.service;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.sdc.service.catalogue.model.Article;
import test.sdc.service.catalogue.model.Price;
import test.sdc.service.discount.model.Discount;
import test.sdc.service.restexpress.client.CatalogueClient;
import test.sdc.service.restexpress.client.DiscountPolicyClient;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Pricing service.
 */
public class PricingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PricingService.class);

    private final CatalogueClient catalogueClient;
    private final DiscountPolicyClient discountPolicyClient;
    private final Meter discountMeter;

    /**
     * Constructor.
     *
     * @param catalogueClient      interface with catalogue service
     * @param discountPolicyClient interface with discount policy service
     * @param metrics metric registry
     */
    @Inject
    public PricingService(final CatalogueClient catalogueClient, final DiscountPolicyClient discountPolicyClient,
                          final MetricRegistry metrics) {
        this.catalogueClient = catalogueClient;
        this.discountPolicyClient = discountPolicyClient;
        this.discountMeter = metrics.meter("discount");
    }

    /**
     * Compute price of article with input reference.
     *
     * @param articleReference reference of the article in the catalogue
     * @return price of the article including possible discounts, or null if article was not found
     */
    public Price computePrice(final String articleReference) {
        final Price res;
        final Article article = this.catalogueClient.find(articleReference);
        if (article == null) {
            res = null;
        } else {
            final Optional<Discount> discount = this.discountPolicyClient.getApplicableDiscount(article);
            discount.ifPresent(value -> this.discountMeter.mark());
            final Price initialPrice = article.getPrice();
            res = discount.isPresent()
                    ? discount.get().apply(initialPrice)
                    : initialPrice;
        }
        return res;
    }

}