package com.patricktwohig.jobber.guice;

import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.patricktwohig.jobber.ai.*;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;

public class AiServicesModule extends PrivateModule {

    @Override
    protected void configure() {
        expose(TaskResolver.class);
        expose(ResumeAuthor.class);
        expose(ResumeAnalyst.class);
        expose(CoverLetterAuthor.class);
        expose(CoverLetterAnalyst.class);
        expose(JobDescriptionAnalyst.class);
    }

    @Provides
    public TaskResolver provideTaskResolver(final ChatLanguageModel model,
                                            final ChatMemory chatMemory,
                                            final ContentRetriever contentRetriever) {
        final var implementation = AiServices.builder(TaskResolver.class)
                .chatMemory(chatMemory)
                .chatLanguageModel(model)
                .contentRetriever(contentRetriever)
                .build();
        return new DefaultTaskResolver(implementation);
    }

    @Provides
    public ResumeAuthor newResumeAuthor(final ChatLanguageModel model,
                                        final ChatMemory chatMemory,
                                        final ContentRetriever contentRetriever) {
        return AiServices.builder(ResumeAuthor.class)
                .chatMemory(chatMemory)
                .chatLanguageModel(model)
                .contentRetriever(contentRetriever)
                .build();
    }

    @Provides
    public ResumeAnalyst newResumeAnalyst(final ChatLanguageModel model,
                                          final ChatMemory chatMemory,
                                          final ContentRetriever contentRetriever) {
        return AiServices.builder(ResumeAnalyst.class)
                .chatMemory(chatMemory)
                .chatLanguageModel(model)
                .contentRetriever(contentRetriever)
                .build();
    }

    @Provides
    public CoverLetterAuthor newCoverLetterAuthor(final ChatLanguageModel model,
                                                  final ChatMemory chatMemory,
                                                  final ContentRetriever contentRetriever) {
        return AiServices.builder(CoverLetterAuthor.class)
                .chatMemory(chatMemory)
                .chatLanguageModel(model)
                .contentRetriever(contentRetriever)
                .build();
    }

    @Provides
    public CoverLetterAnalyst newCoverLetterAnalyst(final ChatLanguageModel model,
                                                    final ChatMemory chatMemory,
                                                    final ContentRetriever contentRetriever) {
        return AiServices.builder(CoverLetterAnalyst.class)
                .chatMemory(chatMemory)
                .chatLanguageModel(model)
                .contentRetriever(contentRetriever)
                .build();
    }

    @Provides
    public JobDescriptionAnalyst provideJobDescriptionAnalyst(final ChatLanguageModel model,
                                                              final ChatMemory chatMemory,
                                                              final ContentRetriever contentRetriever) {
        return AiServices.builder(JobDescriptionAnalyst.class)
                .chatMemory(chatMemory)
                .chatLanguageModel(model)
                .contentRetriever(contentRetriever)
                .build();
    }

}
