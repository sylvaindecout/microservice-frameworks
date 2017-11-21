package test.sdc.service.restexpress.controller;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.restexpress.Request;
import org.restexpress.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.sdc.service.catalogue.model.Price;
import test.sdc.service.restexpress.service.PricingService;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * Pricing controller.
 */
public class PricingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PricingController.class);

    private final PricingService service;
    private final MetricRegistry metrics;
    private final Map<String, Meter> meters = new HashMap<>();
    private final Lock metersLock = new ReentrantLock();
    private final Timer responseTimer;

    /**
     * Constructor.
     *
     * @param service service
     * @param metrics metric registry
     */
    @Inject
    public PricingController(final PricingService service, final MetricRegistry metrics) {
        this.service = service;
        this.metrics = metrics;
        this.responseTimer = metrics.timer(name(PricingController.class, "read"));
    }

    /**
     * Handle GET request.
     *
     * @param request  request
     * @param response response
     * @return object, or null if none
     */
    public Object read(final Request request, final Response response) {
        final Timer.Context timerContext = this.responseTimer.time();
        try {
            notifyRequestToMetrics(request);
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
            notifyResponseToMetrics(response);
            return res;
        } finally {
            timerContext.stop();
        }
    }

    /**
     * Update metrics with input request.
     *
     * @param request request
     */
    private void notifyRequestToMetrics(final Request request) {
        this.getMeter("requests").mark();
    }

    /**
     * Update metrics with input response.
     *
     * @param response response
     */
    private void notifyResponseToMetrics(final Response response) {
        this.getMeter("responses_ALL").mark();
        this.getMeter(String.format("responses_%s", response.getResponseStatus().code())).mark();
    }

    /**
     * Get meter for input key; initialize it if necessary.
     *
     * @param key key used to identify meter
     * @return meter
     */
    private Meter getMeter(final String key) {
        this.metersLock.lock();
        try {
            if (!this.meters.containsKey(key)) {
                this.meters.put(key, this.metrics.meter(key));
            }
            return this.meters.get(key);
        } finally {
            this.metersLock.unlock();
        }
    }

}