package test.sdc.service.restexpress;

import com.codahale.metrics.MetricRegistry;
import dagger.Module;
import dagger.Provides;
import org.apache.commons.cli.CommandLine;
import test.sdc.service.restexpress.client.CatalogueClient;
import test.sdc.service.restexpress.client.DiscountPolicyClient;
import test.sdc.service.restexpress.option.StartupOption;

import javax.inject.Named;
import javax.inject.Singleton;
import java.net.URI;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Module in charge of the definition of the dependency graph.
 */
@Module
final class PricingModule {

    private final Integer port;
    private final Integer poolSize;
    private final URI catalogueServiceUrl;
    private final URI discountPolicyServiceUrl;

    /**
     * Constructor.
     *
     * @param commandLine application start-up arguments
     * @throws IllegalArgumentException Missing or null arguments
     */
    PricingModule(final CommandLine commandLine) {
        checkArgument(commandLine != null, "Input command line is null");
        this.port = StartupOption.PORT.readInt(commandLine);
        this.poolSize = StartupOption.POOL_SIZE.readInt(commandLine);
        this.catalogueServiceUrl = StartupOption.CATALOGUE_SERVICE_URL.readUri(commandLine);
        this.discountPolicyServiceUrl = StartupOption.DISCOUNT_POLICY_SERVICE_URL.readUri(commandLine);
    }

    /**
     * Provide port used to bind server.
     *
     * @return port used to bind server
     */
    @Provides
    @Named("port")
    Integer providePort() {
        return this.port;
    }

    /**
     * Provide size of the thread pool.
     *
     * @return size of the thread pool
     */
    @Provides
    @Named("poolSize")
    Integer providePoolSize() {
        return this.poolSize;
    }

    /**
     * Provide interface with catalogue service.
     *
     * @return interface with catalogue service
     */
    @Provides
    @Singleton
    CatalogueClient provideCatalogueClient() {
        return new CatalogueClient(this.catalogueServiceUrl);
    }

    /**
     * Provide interface with discount policy service.
     *
     * @return interface with discount policy service
     */
    @Provides
    @Singleton
    DiscountPolicyClient provideDiscountPolicyClient() {
        return new DiscountPolicyClient(this.discountPolicyServiceUrl);
    }

    @Provides
    @Singleton
    MetricRegistry provideMetricRegistry() {
        return new MetricRegistry();
    }

}