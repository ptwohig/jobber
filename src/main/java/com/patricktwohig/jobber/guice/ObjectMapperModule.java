package com.patricktwohig.jobber.guice;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

public class ObjectMapperModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ObjectMapper.class).toProvider(() -> {
            final var objectMapper = new ObjectMapper();

            final var indenter = new DefaultIndenter(" ", "\n");
            final var prettyPrinter = new DefaultPrettyPrinter();
            prettyPrinter.indentArraysWith(indenter);

            objectMapper.findAndRegisterModules();
            objectMapper.enable(INDENT_OUTPUT);
            objectMapper.setDefaultPrettyPrinter(prettyPrinter);

            return objectMapper;
        }).asEagerSingleton();
    }

}
