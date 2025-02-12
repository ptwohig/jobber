package com.patricktwohig.jobber.guice;

import com.google.inject.PrivateModule;
import com.patricktwohig.jobber.format.CoverLetterFormatter;
import com.patricktwohig.jobber.format.ResumeFormatter;
import com.patricktwohig.jobber.format.poi.DocxCoverLetterFormatter;
import com.patricktwohig.jobber.format.poi.DocxResumeFormatter;

public class DocxFormatModule extends PrivateModule {

    @Override
    protected void configure() {
        expose(ResumeFormatter.class);
        expose(CoverLetterFormatter.class);
        bind(ResumeFormatter.class).to(DocxResumeFormatter.class);
        bind(CoverLetterFormatter.class).to(DocxCoverLetterFormatter.class);
    }

}
