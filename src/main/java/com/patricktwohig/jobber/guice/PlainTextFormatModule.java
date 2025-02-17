package com.patricktwohig.jobber.guice;

import com.google.inject.PrivateModule;
import com.patricktwohig.jobber.format.CoverLetterFormatter;
import com.patricktwohig.jobber.format.ResumeFormatter;
import com.patricktwohig.jobber.format.plain.PlainTextCoverLetterFormatter;
import com.patricktwohig.jobber.format.plain.PlainTextResumeFormatter;

public class PlainTextFormatModule extends PrivateModule {

    @Override
    protected void configure() {
        expose(ResumeFormatter.class);
        expose(CoverLetterFormatter.class);
        bind(ResumeFormatter.class).to(PlainTextResumeFormatter.class);
        bind(CoverLetterFormatter.class).to(PlainTextCoverLetterFormatter.class);
    }

}
