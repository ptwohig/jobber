package com.patricktwohig.jobber.guice;

import com.google.inject.PrivateModule;
import com.patricktwohig.jobber.input.DocumentInput;
import com.patricktwohig.jobber.input.jackson.JacksonDocumentInput;

public class JsonDocumentInputModule extends PrivateModule {
    @Override
    protected void configure() {
        install(new ObjectMapperModule());
        expose(DocumentInput.class);
        bind(DocumentInput.class).to(JacksonDocumentInput.class);
    }

}
