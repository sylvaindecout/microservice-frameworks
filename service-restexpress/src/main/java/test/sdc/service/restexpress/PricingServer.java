package test.sdc.service.restexpress;

import org.restexpress.RestExpress;
import org.restexpress.pipeline.SimpleConsoleLogMessageObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.sdc.service.restexpress.controller.PricingController;
import test.sdc.service.restexpress.service.PricingService;

import javax.inject.Inject;
import javax.inject.Named;

import static io.netty.handler.codec.http.HttpMethod.GET;

/**
 * Pricing server.
 */
public final class PricingServer
        implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PricingService.class);

    private static final String SERVER_NAME = "Pricing";
    private final Integer port;
    private final String baseUrl;
    private final Integer poolSize;
    private final PricingController controller;
    private RestExpress server;

    /**
     * Constructor.
     *
     * @param port       port used to bind server
     * @param poolSize   size of the thread pool
     * @param controller controller
     */
    @Inject
    public PricingServer(@Named("port") final Integer port, @Named("poolSize") final Integer poolSize, final PricingController controller) {
        this.port = port;
        this.baseUrl = String.format("localhost:%s", port);
        this.poolSize = poolSize;
        this.controller = controller;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        try {
            this.startServer();
        } catch (final Exception ex) {
            LOGGER.error("Application start-up failed", ex);
        }
    }

    /**
     * Start service.
     */
    private void startServer() {
        LOGGER.info("Starting pricing server...");
        this.server = new RestExpress()
                .setName(SERVER_NAME)
                .setBaseUrl(this.baseUrl)
                .setExecutorThreadCount(this.poolSize)
                .addMessageObserver(new SimpleConsoleLogMessageObserver());
        Runtime.getRuntime().addShutdownHook(new Thread() {
            /** {@inheritDoc} */
            @Override
            public void run() {
                LOGGER.info("Stopping pricing server...");
                PricingServer.this.server.shutdown();
            }
        });
        this.defineRoutes();
        this.server.bind(port);
    }

    /**
     * Define available HTTP routes.
     */
    private void defineRoutes() {
        this.server.uri("/pricing/{uuid}", this.controller)
                .method(GET)
                .name("find.route");
    }

}