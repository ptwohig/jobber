package com.patricktwohig.jobber.format;

import com.patricktwohig.jobber.model.CoverLetter;

import java.io.IOException;
import java.io.OutputStream;

public interface CoverLetterFormatter {

    void format(CoverLetter coverLetter, OutputStream outputStream) throws IOException;

}
