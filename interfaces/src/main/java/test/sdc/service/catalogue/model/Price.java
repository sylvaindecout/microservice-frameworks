package test.sdc.service.catalogue.model;

import java.math.BigDecimal;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;

public final class Price {

    private Double valueAsDollars;

    private Price() {
    }

    public static Price fromDollars(final Double inputValue) {
        checkArgument(inputValue >= 0,
                "A price cannot be negative (input value: %s)", inputValue);
        final Integer nbDecimals = getNbDecimals(inputValue);
        checkArgument(nbDecimals <= 2,
                "A price should not have more than two decimals (input value: %s)", inputValue);
        final Price instance = new Price();
        instance.setValueAsDollars(inputValue);
        return instance;
    }

    /**
     * Get number of decimals of input positive value.
     *
     * @param inputValue value
     * @return number of decimals
     */
    private static Integer getNbDecimals(final Double inputValue) {
        return BigDecimal.valueOf(inputValue).stripTrailingZeros().scale();
    }

    public Double getValueAsDollars() {
        return this.valueAsDollars;
    }

    private void setValueAsDollars(final Double value) {
        this.valueAsDollars = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object other) {
        return other instanceof Price
                && Objects.equals(this.valueAsDollars, ((Price) other).valueAsDollars);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.valueAsDollars);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("%1.2f$", this.valueAsDollars);
    }

}