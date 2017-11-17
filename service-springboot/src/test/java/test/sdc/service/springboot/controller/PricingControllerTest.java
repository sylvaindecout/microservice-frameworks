package test.sdc.service.springboot.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import test.sdc.service.catalogue.model.Price;
import test.sdc.service.springboot.service.PricingService;

import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;

public class PricingControllerTest {

    @Mock
    private PricingService service;

    @InjectMocks
    private PricingController controller;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_set_code_to_404_for_null_price() {
        final String inputRef = "123";
        final Price expected = null;
        final HttpServletResponse inputResponse = mock(HttpServletResponse.class);
        given(this.service.computePrice(inputRef)).willReturn(expected);

        this.controller.computePrice(inputRef, inputResponse);

        then(inputResponse).should(only()).setStatus(404);
    }

    @Test
    public void should_return_price() {
        final String inputRef = "123";
        final Price expected = Price.fromDollars(12.99);
        final HttpServletResponse inputResponse = mock(HttpServletResponse.class);
        given(this.service.computePrice(inputRef)).willReturn(expected);

        final Price actual = this.controller.computePrice(inputRef, inputResponse);

        assertThat(actual).isEqualTo(expected);
    }

}