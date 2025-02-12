package com.patricktwohig.jobber.cli;

import picocli.CommandLine;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;
import java.util.stream.Stream;

public record OutputLine(Format format, String destination) {

    public static final String DELIMITER = ":";

    public static OutputLine valueOf(final String input) {
        return Stream.of(Format.values())
                .filter(f -> input.startsWith(f.prefixWithDelimiter()))
                .findFirst()
                .map(f -> new OutputLine(f, input.substring(f.prefixWithDelimiter().length())))
                .orElseGet(() -> new OutputLine(null, input));
    }

    public OutputStream openOutputFileOrStdout() throws IOException {
        return openOutputFileOrDefault(System.out);
    }

    public OutputStream openOutputFileOrDefault(OutputStream defaultOutput) throws IOException {
        return destination == null || destination.isBlank()
                ? defaultOutput
                : new FileOutputStream(destination);
    }

    public enum Format {

        JSON,

        DOCX;

        private final String prefix = name().toLowerCase();

        private final String prefixWithDelimiter = prefix + DELIMITER;

        public static Optional<Format> fromSchemeName(final String scheme) {
            return Stream.of(values())
                    .filter(s -> s.prefix.equals(scheme))
                    .findFirst();
        }

        public String prefixWithDelimiter() {
            return prefixWithDelimiter;
        }

    }

    public static class Converter implements CommandLine.ITypeConverter<OutputLine> {

        @Override
        public OutputLine convert(final String value) throws Exception {
            return OutputLine.valueOf(value);
        }

    }

}
