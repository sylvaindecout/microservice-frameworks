package test.sdc.service.restexpress;

import dagger.Component;

import javax.inject.Singleton;

/**
 * Dagger component, used to generate objects from the dependency graph defined in the modules.
 */
@Singleton
@Component(modules = PricingModule.class)
interface PricingComponent {

    /**
     * Build instance with all the dependency graph.
     *
     * @return pricing server
     */
    PricingServer getPricingServer();

}