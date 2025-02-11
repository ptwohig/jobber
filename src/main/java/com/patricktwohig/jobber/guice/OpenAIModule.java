package com.patricktwohig.jobber.guice;

import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import jakarta.inject.Named;

import static com.patricktwohig.jobber.ai.OpenAI.OPENAI_API_KEY;
import static com.patricktwohig.jobber.ai.OpenAI.OPENAI_MODEL;

public class OpenAIModule extends PrivateModule {

    @Override
    protected void configure() {

        expose(EmbeddingModel.class);
        expose(ChatLanguageModel.class);

        bind(EmbeddingModel.class)
                .to(OpenAiEmbeddingModel.class)
                .asEagerSingleton();

        bind(ChatLanguageModel.class)
                .to(OpenAiChatModel.class)
                .asEagerSingleton();
        
    }

    @Provides
    public OpenAiChatModel openAiChatModel(
            @Named(OPENAI_MODEL) final String model,
            @Named(OPENAI_API_KEY) final String apiKey) {
        return new OpenAiChatModel.OpenAiChatModelBuilder()
                .apiKey(apiKey)
                .modelName(model)
                .build();
    }

    @Provides
    public OpenAiEmbeddingModel openAiEmbeddingModel(
            @Named(OPENAI_MODEL) final String model,
            @Named(OPENAI_API_KEY) final String apiKey) {
        return OpenAiEmbeddingModel.builder()
                .apiKey(apiKey)
                .modelName(model)
                .build();
    }

}
