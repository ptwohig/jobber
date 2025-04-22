package com.patricktwohig.jobber.config;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Represents a configuration for the application.
 */
public interface Configuration {

    /**
     * The timeout for the API calls to the LLM, expressed in seconds.
     */
    String API_TIMEOUT = "com.patricktwohig.jobber.api.timeout";

    /**
     * The maximum number of tokens to keep in memory when using chat memory.
     */
    String MESSAGE_MEMORY_COUNT = "com.patricktwohig.jobber.message.memory.count";

    /**
     * The configuration property for the OpenAI API key.
     */
    String LOG_API_CALLS = "com.patricktwohig.jobber.log.api.calls";

    /**
     * Specifies the OpenAI model.
     */
    String OPENAI_CHAT_MODEL = "com.patricktwohig.jobber.openai.chat.model";

    /**
     * Names the embedding model.
     */
    String OPENAI_EMBEDDING_MODEL = "com.patricktwohig.jobber.model.embedding.model";

    /**
     * The configuration property for the OpenAI API key.
     */
    String OPENAI_API_KEY = "com.patricktwohig.jobber.openai.api.key";

    /**
     * Streams all {@link ConfigurationParameter}s.
     * @return a {@link Stream}
     */
    Stream<ConfigurationParameter> stream();

    /**
     * The configuration property for the OpenAI API URL.
     */
    default String getConfigurationParameter(final String key) {
        return findConfigurationParameter(key)
                .orElseThrow(() -> new NoSuchElementException("Missing configuration parameter: " + key));
    }

    /**
     * The configuration property for the OpenAI API URL.
     *
     * @param key the configuration key
     * @return the value
     */
    Optional<String> findConfigurationParameter(String key);

    /**
     * A configuration property.
     * @param key the key
     * @param value the value
     */
    record ConfigurationParameter(String key, String value) {}

}
