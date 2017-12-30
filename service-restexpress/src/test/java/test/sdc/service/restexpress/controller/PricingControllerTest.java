package test.sdc.service.restexpress.controller;

import com.codahale.metrics.MetricRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.restexpress.Request;
import org.restexpress.Response;
import test.sdc.service.catalogue.model.Price;
import test.sdc.service.restexpress.service.PricingService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

class PricingControllerTest {

    @Mock
    private PricingService service;
    @Spy
    private MetricRegistry metrics;

    @InjectMocks
    private PricingController controller;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void should_reject_request_with_missing_article_reference() {
        final Response inputResponse = spy(Response.class);
        final Request inputRequest = mock(Request.class);
        given(inputRequest.getHeader("uuid")).willReturn(null);

        this.controller.read(inputRequest, inputResponse);

        then(inputResponse).should().setResponseCode(400);
    }

    @Test
    void should_set_code_to_404_for_null_price() {
        final String inputRef = "123";
        final Price expected = null;
        final Response inputResponse = spy(Response.class);
        final Request inputRequest = mock(Request.class);
        given(inputRequest.getHeader("uuid")).willReturn(inputRef);
        given(this.service.computePrice(inputRef)).willReturn(expected);

        this.controller.read(inputRequest, inputResponse);

        then(inputResponse).should().setResponseCode(404);
    }

    @Test
    void should_return_price() {
        final String inputRef = "123";
        final Price expected = Price.fromDollars(12.99);
        final Response inputResponse = spy(Response.class);
        final Request inputRequest = mock(Request.class);
        given(inputRequest.getHeader("uuid")).willReturn(inputRef);
        given(this.service.computePrice(inputRef)).willReturn(expected);

        final Object actual = this.controller.read(inputRequest, inputResponse);

        assertThat(actual).isEqualTo(expected);
    }

}