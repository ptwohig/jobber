package com.patricktwohig.jobber.ai;

public class Configuration {

    private Configuration() {}

    /**
     * The timeout for the API calls to the LLM, expressed in seconds.
     */
    public static final String API_TIMEOUT = "com.patricktwohig.jobber.api.timeout";

    /**
     * The maximum number of tokens to keep in memory when using chat memory.
     */
    public static final String MESSAGE_MEMORY_COUNT = "com.patricktwohig.jobber.message.memory.count";

    /**
     * The configuration property for the OpenAI API key.
     */
    public static final String LOG_API_CALLS = "com.patricktwohig.jobber.log.api.calls";

    /**
     * Specifies the OpenAI model.
     */
    public static final String OPENAI_MODEL = "com.patricktwohig.jobber.openai.model";

    /**
     * The configuration property for the OpenAI API key.
     */
    public static final String OPENAI_API_KEY = "com.patricktwohig.jobber.openai.api.key";

}
