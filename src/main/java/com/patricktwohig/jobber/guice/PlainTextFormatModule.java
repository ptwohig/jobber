package com.patricktwohig.jobber.guice;

import com.google.inject.PrivateModule;
import com.patricktwohig.jobber.format.CoverLetterFormatter;
import com.patricktwohig.jobber.format.GenericFormatter;
import com.patricktwohig.jobber.format.ResumeFormatter;
import com.patricktwohig.jobber.format.plain.PlainTextCoverLetterFormatter;
import com.patricktwohig.jobber.format.plain.PlainTextGenericFormatter;
import com.patricktwohig.jobber.format.plain.PlainTextResumeFormatter;

public class PlainTextFormatModule extends PrivateModule {

    @Override
    protected void configure() {
        expose(ResumeFormatter.class);
        expose(CoverLetterFormatter.class);
        expose(GenericFormatter.class);
        bind(ResumeFormatter.class).to(PlainTextResumeFormatter.class);
        bind(CoverLetterFormatter.class).to(PlainTextCoverLetterFormatter.class);
        bind(GenericFormatter.class).to(PlainTextGenericFormatter.class);
    }

}
