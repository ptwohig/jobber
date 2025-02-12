package com.patricktwohig.jobber.format.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patricktwohig.jobber.format.CoverLetterFormatter;
import com.patricktwohig.jobber.model.CoverLetter;
import jakarta.inject.Inject;

import java.io.IOException;
import java.io.OutputStream;

public class JacksonCoverLetterFormatter implements CoverLetterFormatter {

    private ObjectMapper objectMapper;

    @Override
    public void format(final CoverLetter coverLetter, final OutputStream outputStream) throws IOException {
        getObjectMapper().writeValue(outputStream, coverLetter);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Inject
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

}
