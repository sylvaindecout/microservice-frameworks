package test.sdc.service.discount.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import test.sdc.service.catalogue.model.Price;

/**
 * Discount.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AbsoluteDiscount.class, name = "Absolute"),
        @JsonSubTypes.Type(value = RelativeDiscount.class, name = "Relative")
})
public interface Discount {

    /**
     * Apply discount to initial price.
     *
     * @param initialPrice initial price
     * @return price including discount
     */
    Price apply(final Price initialPrice);

}