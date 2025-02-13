package com.patricktwohig.jobber.format.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patricktwohig.jobber.format.Postprocessor;
import jakarta.inject.Inject;

public class JacksonPostprocessorBuilderFactory implements Postprocessor.Factory {

    private ObjectMapper objectMapper;

    @Override
    public <T> Postprocessor.Builder<T> get(final Class<T> modelT) {
        return new JacksonPostprocessorBuilder<>(modelT, getObjectMapper());
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Inject
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

}
