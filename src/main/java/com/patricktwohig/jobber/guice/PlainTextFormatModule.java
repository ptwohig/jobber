package com.patricktwohig.jobber.guice;

import com.google.inject.PrivateModule;
import com.patricktwohig.jobber.format.CoverLetterFormatter;
import com.patricktwohig.jobber.format.plain.PlainTextCoverLetterFormatter;

public class PlainTextFormatModule extends PrivateModule {

    @Override
    protected void configure() {
        expose(CoverLetterFormatter.class);
        bind(CoverLetterFormatter.class).to(PlainTextCoverLetterFormatter.class);
    }

}
