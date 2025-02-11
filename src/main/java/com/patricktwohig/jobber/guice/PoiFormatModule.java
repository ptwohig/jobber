package com.patricktwohig.jobber.guice;

import com.google.inject.PrivateModule;
import com.patricktwohig.jobber.format.CoverLetterFormatter;
import com.patricktwohig.jobber.format.ResumeFormatter;
import com.patricktwohig.jobber.format.poi.PoiCoverLetterFormatter;
import com.patricktwohig.jobber.format.poi.PoiResumeFormatter;
import opennlp.tools.postag.POSContextGenerator;

public class PoiFormatModule extends PrivateModule {

    @Override
    protected void configure() {
        bind(ResumeFormatter.class).to(PoiResumeFormatter.class);
        bind(CoverLetterFormatter.class).to(PoiCoverLetterFormatter.class);
        expose(PoiResumeFormatter.class);
        expose(POSContextGenerator.class);
    }

}
