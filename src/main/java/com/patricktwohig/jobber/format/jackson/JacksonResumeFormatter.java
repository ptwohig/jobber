package com.patricktwohig.jobber.format.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patricktwohig.jobber.format.ResumeFormatter;
import com.patricktwohig.jobber.model.Resume;
import jakarta.inject.Inject;

import java.io.IOException;
import java.io.OutputStream;

public class JacksonResumeFormatter implements ResumeFormatter {

    private ObjectMapper objectMapper;

    @Override
    public void format(final Resume resume, final OutputStream outputStream) throws IOException {
        getObjectMapper().writeValue(outputStream, resume);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Inject
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

}
