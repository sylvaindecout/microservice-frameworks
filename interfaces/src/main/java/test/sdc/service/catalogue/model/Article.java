package test.sdc.service.catalogue.model;

import static java.util.Objects.requireNonNull;

/**
 * Article.
 */
public class Article {

    private String reference;
    private Price price;

    /**
     * Private constructor.
     */
    private Article() {
        super();
    }

    /**
     * Initialize builder with input article reference.
     *
     * @param inputReference article reference
     * @return new builder instance
     */
    public static Builder withReference(final String inputReference) {
        return new Builder().withReference(inputReference);
    }

    /**
     * Get article reference.
     *
     * @return article reference
     */
    public String getReference() {
        return this.reference;
    }

    /**
     * Set article reference.
     *
     * @param reference article reference
     */
    private void setReference(final String reference) {
        this.reference = reference;
    }

    /**
     * Get price.
     *
     * @return get price
     */
    public Price getPrice() {
        return this.price;
    }

    /**
     * Set price.
     *
     * @param price price
     */
    private void setPrice(final Price price) {
        this.price = price;
    }

    /**
     * Builder.
     */
    public static final class Builder {

        private final Article instance = new Article();

        /**
         * Private constructor.
         */
        private Builder() {
        }

        /**
         * Set article reference.
         *
         * @param inputReference article reference
         * @return current builder instance
         */
        public Builder withReference(final String inputReference) {
            this.instance.setReference(inputReference);
            return this;
        }

        /**
         * Set price.
         *
         * @param inputPrice price
         * @return current builder instance
         */
        public Builder withPrice(final Price inputPrice) {
            this.instance.setPrice(inputPrice);
            return this;
        }

        /**
         * Set price.
         *
         * @param inputDollars price as dollars
         * @return current builder instance
         */
        public Builder withPrice(final Double inputDollars) {
            final Price inputPrice = Price.fromDollars(inputDollars);
            return this.withPrice(inputPrice);
        }

        /**
         * Build article.
         *
         * @return new article instance
         */
        public Article build() {
            requireNonNull(this.instance.price, "Price attribute has not been initialized");
            return this.instance;
        }
    }

}