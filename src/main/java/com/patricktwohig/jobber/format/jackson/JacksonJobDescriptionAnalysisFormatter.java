package com.patricktwohig.jobber.format.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patricktwohig.jobber.format.JobDescriptionAnalysisFormatter;
import com.patricktwohig.jobber.model.JobDescriptionAnalysis;
import jakarta.inject.Inject;

import java.io.IOException;
import java.io.OutputStream;

public class JacksonJobDescriptionAnalysisFormatter implements JobDescriptionAnalysisFormatter {

    private ObjectMapper objectMapper;

    @Override
    public void format(JobDescriptionAnalysis coverLetter, OutputStream outputStream) throws IOException {
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
