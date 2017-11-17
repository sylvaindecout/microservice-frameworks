package test.sdc.service.discount.model;

import test.sdc.service.catalogue.model.Price;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Discount with an absolute amount (as opposed to a percentage).
 */
public final class AbsoluteDiscount
        implements Discount {

    private final Price amount;

    /**
     * Private constructor.
     *
     * @param amount discount amount
     */
    private AbsoluteDiscount(final Price amount) {
        super();
        this.amount = amount;
    }

    /**
     * Initialize new instance from input amount.
     *
     * @param amount discount amount
     * @return discount
     */
    public static AbsoluteDiscount of(final Price amount) {
        return new AbsoluteDiscount(amount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Price apply(final Price initialPrice) {
        checkArgument(initialPrice.getValueAsDollars() >= this.amount.getValueAsDollars(),
                "Discount (%s) must be lower than initial price (%s)", this.amount, initialPrice);
        return Price.fromDollars(initialPrice.getValueAsDollars() - this.amount.getValueAsDollars());
    }

}