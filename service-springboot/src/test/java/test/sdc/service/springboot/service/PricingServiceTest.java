package test.sdc.service.springboot.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import test.sdc.service.catalogue.model.Article;
import test.sdc.service.catalogue.model.Price;
import test.sdc.service.discount.model.AbsoluteDiscount;
import test.sdc.service.discount.model.Discount;
import test.sdc.service.discount.model.RelativeDiscount;
import test.sdc.service.springboot.client.CatalogueClient;
import test.sdc.service.springboot.client.DiscountPolicyClient;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

public class PricingServiceTest {

    @Mock
    private CatalogueClient catalogueClient;
    @Mock
    private DiscountPolicyClient discountPolicyClient;

    @InjectMocks
    private PricingService service;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_call_catalogue_and_discount_policy_services() {
        final String inputRef = "1";
        final Article testArticle = mock(Article.class);
        given(this.catalogueClient.find(inputRef)).willReturn(testArticle);

        final Price actual = this.service.computePrice(inputRef);

        then(this.catalogueClient).should(only()).find(inputRef);
        then(this.discountPolicyClient).should(only()).getApplicableDiscount(testArticle);
    }

    @Test
    public void should_apply_absolute_discount() {
        final String inputRef = "1";
        final Article testArticle = mock(Article.class);
        given(testArticle.getPrice()).willReturn(Price.fromDollars(10.));
        final Optional<Discount> testDiscount = Optional.of(AbsoluteDiscount.of(Price.fromDollars(1.)));
        given(this.catalogueClient.find(inputRef)).willReturn(testArticle);
        given(this.discountPolicyClient.getApplicableDiscount(testArticle)).willReturn(testDiscount);

        final Price actual = this.service.computePrice(inputRef);

        assertThat(actual).isEqualTo(Price.fromDollars(9.));
    }

    @Test
    public void should_apply_relative_discount() {
        final String inputRef = "1";
        final Article testArticle = mock(Article.class);
        given(testArticle.getPrice()).willReturn(Price.fromDollars(10.));
        final Optional<Discount> testDiscount = Optional.of(RelativeDiscount.of(0.1));
        given(this.catalogueClient.find(inputRef)).willReturn(testArticle);
        given(this.discountPolicyClient.getApplicableDiscount(testArticle)).willReturn(testDiscount);

        final Price actual = this.service.computePrice(inputRef);

        assertThat(actual).isEqualTo(Price.fromDollars(9.));
    }

    @Test
    public void should_return_initial_price_if_there_is_no_discount() {
        final String inputRef = "1";
        final Article testArticle = mock(Article.class);
        given(testArticle.getPrice()).willReturn(Price.fromDollars(10.));
        final Optional<Discount> testDiscount = Optional.empty();
        given(this.catalogueClient.find(inputRef)).willReturn(testArticle);
        given(this.discountPolicyClient.getApplicableDiscount(testArticle)).willReturn(testDiscount);

        final Price actual = this.service.computePrice(inputRef);

        assertThat(actual).isEqualTo(Price.fromDollars(10.));
    }

    @Test
    public void should_return_null_if_article_is_unknown_to_catalogue() {
        final String inputRef = "12";
        given(this.catalogueClient.find(inputRef)).willReturn(null);

        final Price actual = this.service.computePrice(inputRef);

        then(this.catalogueClient).should(only()).find(inputRef);
        then(this.discountPolicyClient).should(never()).getApplicableDiscount(any(Article.class));
        assertThat(actual).isNull();
    }

}