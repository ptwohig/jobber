package com.patricktwohig.jobber.input.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patricktwohig.jobber.input.DocumentInput;
import jakarta.inject.Inject;

import java.io.IOException;
import java.io.InputStream;

public class JacksonDocumentInput implements DocumentInput {

    private ObjectMapper objectMapper;

    @Override
    public <T> T read(final Class<T> clazz, final InputStream inputStream) throws IOException {
        return getObjectMapper().readValue(inputStream, clazz);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Inject
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

}
