package test.sdc.service.catalogue.model;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;

public final class Price {

    private Double valueAsDollars;

    private Price() {
    }

    public static Price fromDollars(final Double inputValue) {
        //TODO: check number of decimals, etc.
        checkArgument(inputValue >= 0, "A price cannot be negative");
        final Price instance = new Price();
        instance.setValueAsDollars(inputValue);
        return instance;
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