package test.sdc.service.restexpress.option;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import java.net.URI;

/**
 * Start-up options.
 */
public enum StartupOption {

    PORT("port", "Port used to bind server", "12345"),
    POOL_SIZE("poolSize", "Size of the thread pool", "5"),
    CATALOGUE_SERVICE_URL("catalogueServiceUrl", "Base URL of catalogue service", "http://localhost:8080/catalogue"),
    DISCOUNT_POLICY_SERVICE_URL("discountPolicyServiceUrl", "Base URL of discount policy service", "http://localhost:8080/discount"),;

    private final String name;
    private final String description;
    private final String defaultValue;

    /**
     * Constructor.
     *
     * @param name         option name
     * @param description  option description
     * @param defaultValue default value
     */
    StartupOption(final String name, final String description, final String defaultValue) {
        this.name = name;
        this.description = description;
        this.defaultValue = defaultValue;
    }

    /**
     * Constructor.
     *
     * @param name        option name
     * @param description option description
     */
    StartupOption(final String name, final String description) {
        this(name, description, null);
    }

    /**
     * Check if option is required.
     *
     * @return option is required
     */
    private Boolean isRequired() {
        return this.defaultValue == null;
    }

    /**
     * Build option.
     *
     * @return option
     */
    public Option build() {
        return Option.builder(this.name).hasArg().argName(this.name)
                .required(this.isRequired())
                .desc(this.description)
                .build();
    }

    /**
     * Read value from command line, or get default if available.
     *
     * @param commandLine command line
     * @return argument value
     */
    private String readValue(final CommandLine commandLine) {
        return commandLine.hasOption(this.name) || this.isRequired()
                ? commandLine.getOptionValue(this.name)
                : this.defaultValue;
    }

    /**
     * Read argument value as an integer.
     *
     * @param commandLine command line
     * @return argument value as an integer
     * @throws IllegalArgumentException argument value is not a valid integer
     */
    public Integer readInt(final CommandLine commandLine) {
        final String value = this.readValue(commandLine);
        try {
            return Integer.parseInt(value);
        } catch (final NumberFormatException ex) {
            throw new IllegalArgumentException(String.format("Invalid argument for %s: %s; expected: integer", this.name, value));
        }
    }

    /**
     * Read argument value as an URI.
     *
     * @param commandLine command line
     * @return argument value as an URI
     * @throws IllegalArgumentException argument value is not a valid URI
     */
    public URI readUri(final CommandLine commandLine) {
        final String value = this.readValue(commandLine);
        return URI.create(value);
    }

}
