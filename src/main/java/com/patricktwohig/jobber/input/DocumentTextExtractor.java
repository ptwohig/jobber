package com.patricktwohig.jobber.input;

import java.io.IOException;
import java.io.InputStream;

public interface DocumentTextExtractor {

    String read(InputStream inputStream) throws IOException;

}
