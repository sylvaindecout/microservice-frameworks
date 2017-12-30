package test.sdc.service.catalogue.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class PriceTest {

    @Test
    void should_reject_null_value() {
        final Double inputValue = null;
        assertThatNullPointerException().isThrownBy(() -> Price.fromDollars(inputValue));
    }

    @Test
    void should_reject_negative_value() {
        final Double inputValue = -1.;
        assertThatIllegalArgumentException().isThrownBy(() -> Price.fromDollars(inputValue))
                .withMessageContaining(String.valueOf(inputValue));
    }

    @Test
    void should_reject_value_with_too_many_decimals() {
        final Double inputValue = 1.123;
        assertThatIllegalArgumentException().isThrownBy(() -> Price.fromDollars(inputValue))
                .withMessageContaining(String.valueOf(inputValue));
    }

}