package com.patricktwohig.jobber.cli;

import picocli.CommandLine;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.stream.Stream;

public record InputLine(Scheme scheme, String input, Charset charset) {

    public static final String DELIMITER = ":";

    public InputLine {

        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        if (charset == null) {
            charset = Charset.defaultCharset();
        }

    }

    public String readInputString() {
        return readInputString(Scheme.TEXT);
    }

    public String readInputString(final Scheme defaultScheme) {
        return switch (scheme == null ? defaultScheme : scheme) {
            case TEXT -> input;
            case ENV -> System.getenv(input);
            default -> throw new InputLineException("Unsupported scheme: " + scheme);
        };
    }

    public InputStream readInputStream() throws IOException {
        return readInputStream(Scheme.TEXT);
    }

    public InputStream readInputStream(final Scheme defaultScheme) throws IOException {
        return switch (scheme == null ? defaultScheme : scheme) {
            case TEXT -> new ByteArrayInputStream(input.getBytes(charset));
            case ENV -> {
                final String env = System.getenv(input);
                yield new ByteArrayInputStream(env == null ? new byte[0] : env.getBytes(charset));
            }
            case FILE -> new FileInputStream(input);
        };
    }

    public String toString() {
        return scheme == null ? input : scheme.prefixWithDelimiter() + input;
    }

    public static InputLine valueOf(final String input) {
        return Stream.of(Scheme.values())
                .filter(s -> input.startsWith(s.prefixWithDelimiter()))
                .findFirst()
                .map(s -> new InputLine(s, input.substring(s.prefixWithDelimiter().length()), null))
                .orElseGet(() -> new InputLine(null, input, null));
    }

    public enum Scheme {

        TEXT,
        FILE,
        ENV;

        private final String prefix = name().toLowerCase();

        private final String prefixWithDelimiter = prefix + DELIMITER;

        public static Optional<Scheme> fromSchemeName(final String scheme) {
            return Stream.of(values())
                    .filter(s -> s.prefix.equals(scheme))
                    .findFirst();
        }

        public String prefixWithDelimiter() {
            return prefixWithDelimiter;
        }

    }

    public static class Converter implements CommandLine.ITypeConverter<InputLine> {

        @Override
        public InputLine convert(final String value) throws Exception {
            return InputLine.valueOf(value);
        }

    }

}
