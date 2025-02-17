package com.patricktwohig.jobber.guice;

import com.google.inject.PrivateModule;
import com.patricktwohig.jobber.format.CoverLetterFormatter;
import com.patricktwohig.jobber.format.ResumeFormatter;
import com.patricktwohig.jobber.format.jackson.JacksonCoverLetterFormatter;
import com.patricktwohig.jobber.format.jackson.JacksonResumeFormatter;

public class JsonFormatModule extends PrivateModule {

    @Override
    protected void configure() {

        install(new ObjectMapperModule());

        expose(ResumeFormatter.class);
        expose(CoverLetterFormatter.class);

        bind(ResumeFormatter.class).to(JacksonResumeFormatter.class);
        bind(CoverLetterFormatter.class).to(JacksonCoverLetterFormatter.class);

    }

}
