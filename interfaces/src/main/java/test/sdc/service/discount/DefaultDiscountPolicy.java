package test.sdc.service.discount;

import com.google.common.collect.Range;
import test.sdc.service.catalogue.model.Article;
import test.sdc.service.catalogue.model.Price;
import test.sdc.service.discount.model.AbsoluteDiscount;
import test.sdc.service.discount.model.Discount;
import test.sdc.service.discount.model.RelativeDiscount;

/**
 * Default discount policy.
 */
public final class DefaultDiscountPolicy {

    private static final Range<Double> MINOR_DISCOUNT_RANGE = Range.openClosed(50., 100.);
    private static final Discount MINOR_DISCOUNT = RelativeDiscount.of(0.1);
    private static final Range<Double> MAJOR_DISCOUNT_RANGE = Range.greaterThan(100.);
    private static final Discount MAJOR_DISCOUNT = AbsoluteDiscount.of(Price.fromDollars(30.));

    /**
     * Private constructor.
     */
    private DefaultDiscountPolicy() {
    }

    /**
     * Get discount that is applicable to input article.
     *
     * @param article article
     * @return discount
     */
    public static Discount getApplicableDiscount(final Article article) {
        final Double price = article.getPrice().getValueAsDollars();
        if (MINOR_DISCOUNT_RANGE.contains(price)) {
            return MINOR_DISCOUNT;
        } else if (MAJOR_DISCOUNT_RANGE.contains(price)) {
            return MAJOR_DISCOUNT;
        } else {
            return null;
        }
    }

}