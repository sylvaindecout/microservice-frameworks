package test.sdc.service.springboot.controller;

import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.sdc.service.catalogue.model.Price;
import test.sdc.service.springboot.service.PricingService;

import javax.servlet.http.HttpServletResponse;

/**
 * Pricing controller.
 */
@RestController
@RequestMapping("/pricing")
@Api(tags = "Pricing")
public class PricingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PricingController.class);

    @Autowired
    private PricingService service;

    /**
     * Compute price of article with input reference.
     *
     * @param uuid reference of the article in the catalogue
     * @return price of the article including possible discounts, or null if article was not found
     */
    @GetMapping(value = "/{uuid}")
    @ApiOperation(value = "Compute price of article with input reference")
    @ApiResponses({
            @ApiResponse(code = 404, message = "No article with input reference was found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Price computePrice(@ApiParam(value = "Reference of the article in the catalogue", required = true)
                              @PathVariable final String uuid, final HttpServletResponse response) {
        LOGGER.info("GET request: articleReference={}", uuid);
        final Price res = this.service.computePrice(uuid);
        if (res == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return res;
    }

}