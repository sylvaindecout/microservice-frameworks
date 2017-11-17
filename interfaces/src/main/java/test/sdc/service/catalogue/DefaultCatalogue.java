package test.sdc.service.catalogue;

import test.sdc.service.catalogue.model.Article;

import java.util.HashMap;
import java.util.Map;

/**
 * Catalogue service mock-up.
 */
public final class DefaultCatalogue {

    private static final Map<String, Article> ARTICLES = new HashMap<>();

    static {
        final Object[][] dataSet = new Object[][]{
                {"1", 12.99},
                {"2", 75.12},
        };
        for (final Object[] info : dataSet) {
            final String ref = (String) info[0];
            final Double price = (Double) info[1];
            final Article article = Article.withReference(ref).withPrice(price).build();
            ARTICLES.put(ref, article);
        }
    }

    /**
     * Find article from reference.
     *
     * @param reference article reference
     * @return article
     */
    public static Article find(final String reference) {
        return ARTICLES.get(reference);
    }

}