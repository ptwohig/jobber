package com.patricktwohig.jobber.format;

import com.patricktwohig.jobber.model.JobDescriptionAnalysis;

import java.io.IOException;
import java.io.OutputStream;

public interface JobDescriptionAnalysisFormatter {

    void format(JobDescriptionAnalysis coverLetter, OutputStream outputStream) throws IOException;

}
