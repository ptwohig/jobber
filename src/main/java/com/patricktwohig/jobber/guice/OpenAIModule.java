package com.patricktwohig.jobber.guice;

import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import dev.langchain4j.model.Tokenizer;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import jakarta.inject.Named;

import java.time.Duration;

import static com.patricktwohig.jobber.config.Configuration.*;

public class OpenAIModule extends PrivateModule {

    @Override
    protected void configure() {

        expose(Tokenizer.class);
        expose(EmbeddingModel.class);
        expose(ChatLanguageModel.class);

        bind(Tokenizer.class)
                .to(OpenAiTokenizer.class)
                .asEagerSingleton();

        bind(ChatLanguageModel.class)
                .to(OpenAiChatModel.class)
                .asEagerSingleton();

        bind(EmbeddingModel.class)
                .to(OpenAiEmbeddingModel.class)
                .asEagerSingleton();

    }

    @Provides
    public OpenAiTokenizer openAiTokenizer(@Named(OPENAI_CHAT_MODEL) final String model) {
        return new OpenAiTokenizer(model);
    }

    @Provides
    public OpenAiChatModel openAiChatModel(
            @Named(API_TIMEOUT) long apiTimeout,
            @Named(LOG_API_CALLS) boolean logApiCalls,
            @Named(OPENAI_CHAT_MODEL) final String model,
            @Named(OPENAI_API_KEY) final String apiKey) {
        final var duration = Duration.ofSeconds(apiTimeout);
        return new OpenAiChatModel.OpenAiChatModelBuilder()
                .apiKey(apiKey)
                .timeout(duration)
                .strictJsonSchema(true)
                .logRequests(logApiCalls)
                .logResponses(logApiCalls)
                .modelName(model)
                .build();
    }

    @Provides
    public OpenAiEmbeddingModel openAiEmbeddingModel(

            @Named(OPENAI_EMBEDDING_MODEL)
            final String model,

            @Named(LOG_API_CALLS)
            final boolean logApiCalls,

            @Named(OPENAI_API_KEY)
            final String apiKey,

            final OpenAiTokenizer openAiTokenizer) {
        return new OpenAiEmbeddingModel.OpenAiEmbeddingModelBuilder()
                .apiKey(apiKey)
                .modelName(model)
                .logRequests(logApiCalls)
                .logResponses(logApiCalls)
                .tokenizer(openAiTokenizer)
                .build();
    }

}
