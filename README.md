# microservice-frameworks

Test service with several frameworks/stacks.

## Test case

Application starts REST server and maps it to PricingController, so that it exposes methods:
 - Find price for an article reference.

PricingController calls PricingService, which: 
 1. calls CatalogueClient to get article info from remote service,
 2. calls DiscountPolicyClient to get appropriate discount from remote service,
 3. applies discount to article

Configuration includes:
 - Configuration of REST service (binding port, etc.)
 - Base URLs used to contact other services 

## service-restexpress

Custom stack, including:
 - RestExpress for REST capabilities
 - Dagger for injection
 - Metrics for monitoring
 - Hystrix for fault-tolerance
 - Jersey client for calls other REST services
 - Apache commons CLI for command line parsing

All configuration (but for Hystrix) is set in the command line. Default values are set for all properties.

### How to run

 - Start Application
 - Test from HTTP client (http://localhost:12345/pricing/1)
 - Access metrics from JMX console

## service-springboot

Spring Boot, with modules:
 - Actuator for monitoring
 - Swagger2 for REST interface documentation
 - Hystrix for fault-tolerance

All configuration (but for Hystrix) is set in application.properties file.

### How to run

 - Start Application
 - Test from HTTP client (http://localhost:12345/pricing/1)
 - Access metrics from JMX console
 - Access REST documentation from HTTP client (http://localhost:12345/swagger-ui.html)