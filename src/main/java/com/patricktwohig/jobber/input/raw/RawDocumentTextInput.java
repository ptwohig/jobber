package com.patricktwohig.jobber.input.raw;

import com.patricktwohig.jobber.input.DocumentTextExtractor;
import jakarta.inject.Inject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class RawDocumentTextInput implements DocumentTextExtractor {

    private Charset charset;

    @Override
    public String read(final InputStream inputStream) throws IOException {

        var buffer = new StringBuilder();

        try (var reader = new BufferedReader(new InputStreamReader(inputStream, getCharset()))) {
            for (int character; (character = reader.read()) != -1;) {
                buffer.append((char) character);
            }
        }

        return buffer.toString();

    }

    public Charset getCharset() {
        return charset;
    }

    @Inject
    public void setCharset(Charset charset) {
        this.charset = charset;
    }

}
