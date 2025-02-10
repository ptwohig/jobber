package com.patricktwohig.jobber.format;

import com.patricktwohig.jobber.model.Resume;

import java.io.IOException;
import java.io.OutputStream;

public interface ResumeFormatter {

    void format(Resume resume, OutputStream outputStream) throws IOException;

}
