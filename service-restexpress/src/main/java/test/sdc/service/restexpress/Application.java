package test.sdc.service.restexpress;

import org.apache.commons.cli.*;
import test.sdc.service.restexpress.option.StartupOption;

import java.util.concurrent.Executors;

/**
 * Entry point of the application.
 */
public final class Application {

    private static final String COMMAND_LINE_SYNTAX = "service-restexpress";
    private static final Options STARTUP_OPTIONS = new Options()
            .addOption(StartupOption.PORT.build())
            .addOption(StartupOption.POOL_SIZE.build())
            .addOption(StartupOption.CATALOGUE_SERVICE_URL.build())
            .addOption(StartupOption.DISCOUNT_POLICY_SERVICE_URL.build());

    /**
     * Private constructor.
     */
    private Application() {
    }

    /**
     * Main method.
     *
     * @param args start-up arguments
     */
    public static void main(final String[] args) {
        try {
            final CommandLine commandLine = new DefaultParser().parse(STARTUP_OPTIONS, args);
            final PricingModule module = new PricingModule(commandLine);
            final PricingComponent component = DaggerPricingComponent.builder()
                    .pricingModule(module)
                    .build();
            final PricingServer server = component.getPricingServer();
            Executors.newSingleThreadExecutor().submit(server);
        } catch (final ParseException ex) {
            new HelpFormatter().printHelp(COMMAND_LINE_SYNTAX, STARTUP_OPTIONS, true);
        }
    }

}