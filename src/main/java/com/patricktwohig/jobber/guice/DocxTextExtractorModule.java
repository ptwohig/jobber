package com.patricktwohig.jobber.guice;

import com.google.inject.PrivateModule;
import com.patricktwohig.jobber.input.DocumentTextExtractor;
import com.patricktwohig.jobber.input.poi.DocxDocumentTextExtractor;

public class DocxTextExtractorModule extends PrivateModule {

    @Override
    protected void configure() {
        expose(DocumentTextExtractor.class);
        bind(DocumentTextExtractor.class).to(DocxDocumentTextExtractor.class);
    }

}
