package test.sdc.service.restexpress.controller;

import org.restexpress.Request;
import org.restexpress.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.sdc.service.catalogue.model.Price;
import test.sdc.service.restexpress.service.PricingService;

import javax.inject.Inject;

/**
 * Pricing controller.
 */
public class PricingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PricingController.class);

    @Inject
    PricingService service;

    /**
     * Handle GET request.
     *
     * @param request  request
     * @param response response
     * @return object, or null if none
     */
    public Object read(final Request request, final Response response) {
        final Price res;
        final String articleReference = request.getHeader("uuid");
        if (articleReference == null) {
            LOGGER.warn("GET request: null articleReference");
            response.setResponseCode(400);
            res = null;
        } else {
            LOGGER.info("GET request: articleReference={}", articleReference);
            res = this.service.computePrice(articleReference);
            if (res == null) {
                response.setResponseCode(404);
            }
        }
        return res;
    }

}