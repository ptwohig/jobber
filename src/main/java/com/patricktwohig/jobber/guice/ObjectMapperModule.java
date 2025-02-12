package com.patricktwohig.jobber.guice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;

public class ObjectMapperModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ObjectMapper.class).toProvider(() -> {
            final var objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            return objectMapper;
        }).asEagerSingleton();
    }

}
