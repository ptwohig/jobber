package com.patricktwohig.jobber.guice;

import com.google.inject.PrivateModule;
import com.patricktwohig.jobber.format.Postprocessor;
import com.patricktwohig.jobber.format.jackson.JacksonPostprocessorBuilderFactory;

public class JacksonPostprocessorModule extends PrivateModule {

    @Override
    protected void configure() {
        install(new ObjectMapperModule());
        expose(Postprocessor.Factory.class);
        bind(Postprocessor.Factory.class).to(JacksonPostprocessorBuilderFactory.class);
    }

}
