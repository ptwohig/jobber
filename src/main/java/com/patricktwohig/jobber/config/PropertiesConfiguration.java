package com.patricktwohig.jobber.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * A {@link Configuration} backed by {@link Properties}.
 */
public class PropertiesConfiguration implements Configuration {

    private static final String DEFAULT_DIRECTORY = ".jobber";

    private static final String DEFAULT_FILE_NAME = "jobber.properties";

    private final Properties properties = new Properties();

    /**
     * Creates a {@link PropertiesConfiguration} with no properties.
     */
    public PropertiesConfiguration() {}

    /**
     * Creates a {@link PropertiesConfiguration} from the given path.
     *
     * @param propertiesFilePath the path to the properties file
     */
    public PropertiesConfiguration(final Path propertiesFilePath) {
        try (InputStream input = Files.newInputStream(propertiesFilePath)) {
            properties.load(input);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load properties file: " + propertiesFilePath, e);
        }
    }

    /**
     * Creates a {@link PropertiesConfiguration} from the given path.
     *
     * @param propertiesFilePath the path to the properties file
     */
    public PropertiesConfiguration(final String propertiesFilePath) {
        this(Paths.get(propertiesFilePath));
    }

    /**
     * Creates a {@link PropertiesConfiguration} from the user home directory.
     *
     * @return the {@link PropertiesConfiguration}
     */
    public static PropertiesConfiguration fromUserHomeDirectory() {

        final var defaultPath = Paths.get(
                System.getProperty("user.home"),
                DEFAULT_DIRECTORY,
                DEFAULT_FILE_NAME
        );

        return Files.isRegularFile(defaultPath)
                ? new PropertiesConfiguration(defaultPath)
                : new PropertiesConfiguration();

    }

    @Override
    public Stream<ConfigurationParameter> stream() {
        return properties.entrySet().stream()
                .map(entry -> new ConfigurationParameter(entry.getKey().toString(), entry.getValue().toString()));
    }

    @Override
    public Optional<String> findConfigurationParameter(String key) {
        return Optional.ofNullable(properties.getProperty(key));
    }

}