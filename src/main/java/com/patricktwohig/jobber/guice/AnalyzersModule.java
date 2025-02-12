package com.patricktwohig.jobber.guice;

import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.patricktwohig.jobber.ai.CoverLetterAuthor;
import com.patricktwohig.jobber.ai.JobDescriptionAnalyst;
import com.patricktwohig.jobber.ai.ResumeAnalyst;
import com.patricktwohig.jobber.ai.ResumeAuthor;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.inject.Named;

import static com.patricktwohig.jobber.ai.OpenAI.OPENAI_API_KEY;
import static com.patricktwohig.jobber.ai.OpenAI.OPENAI_MODEL;

public class AnalyzersModule extends PrivateModule {

    @Override
    protected void configure() {
        expose(ResumeAuthor.class);
        expose(ResumeAnalyst.class);
        expose(CoverLetterAuthor.class);
        expose(JobDescriptionAnalyst.class);
    }

    @Provides
    public OpenAiChatModel openAiChatModel(
            @Named(OPENAI_MODEL) final String model,
            @Named(OPENAI_API_KEY) final String apiKey) {
        return new OpenAiChatModel.OpenAiChatModelBuilder()
                .apiKey(apiKey)
                .modelName(model)
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Provides
    public ResumeAuthor newResumeAuthor(final ChatLanguageModel model) {
        return AiServices.create(ResumeAuthor.class, model);
    }

    @Provides
    public ResumeAnalyst newResumeAnalyst(final ChatLanguageModel model) {
        return AiServices.create(ResumeAnalyst.class, model);
    }

    @Provides
    public CoverLetterAuthor newCoverLetterAuthor(final ChatLanguageModel model) {
        return AiServices.create(CoverLetterAuthor.class, model);
    }

    @Provides
    public JobDescriptionAnalyst newJobDescriptionAnalyst(final ChatLanguageModel model) {
        return AiServices.create(JobDescriptionAnalyst.class, model);
    }

}
