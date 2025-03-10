package com.patricktwohig.jobber.guice;

import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.patricktwohig.jobber.ai.*;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;

public class AiServicesModule extends PrivateModule {

    @Override
    protected void configure() {
        expose(ResumeAuthor.class);
        expose(ResumeAnalyst.class);
        expose(CoverLetterAuthor.class);
        expose(CoverLetterAnalyst.class);
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
    public CoverLetterAnalyst newCoverLetterAnalyst(final ChatLanguageModel model) {
        return AiServices.create(CoverLetterAnalyst.class, model);
    }

}
