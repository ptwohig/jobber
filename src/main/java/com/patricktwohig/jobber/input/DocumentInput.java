package com.patricktwohig.jobber.input;

import java.io.IOException;
import java.io.InputStream;

public interface DocumentInput {

    <T> T read(Class<T> clazz, InputStream inputStream) throws IOException;

}
