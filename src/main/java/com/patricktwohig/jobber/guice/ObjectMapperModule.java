package com.patricktwohig.jobber.guice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

public class ObjectMapperModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ObjectMapper.class).toProvider(() -> {
            final var objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            objectMapper.enable(INDENT_OUTPUT);
            return objectMapper;
        }).asEagerSingleton();
    }

}
