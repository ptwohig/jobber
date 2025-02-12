package com.patricktwohig.jobber.cli;

import picocli.CommandLine;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.stream.Stream;

public record InputLine(Format format, String input, Charset charset) {

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
        return readInputString(Format.TEXT);
    }

    public String readInputString(final Format defaultFormat) {
        return switch (format == null ? defaultFormat : format) {
            case TEXT -> input;
            case ENV -> System.getenv(input);
            default -> throw new CliException(ExitCode.UNSUPPORTED_INPUT_FORMAT);
        };
    }

    public InputStream readInputStream() throws IOException {
        return readInputStream(Format.TEXT);
    }

    public InputStream readInputStream(final Format defaultFormat) throws IOException {
        return switch (format == null ? defaultFormat : format) {
            case TEXT -> new ByteArrayInputStream(input.getBytes(charset));
            case ENV -> {
                final String env = System.getenv(input);
                yield new ByteArrayInputStream(env == null ? new byte[0] : env.getBytes(charset));
            }
            case DOCX, JSON -> new FileInputStream(input);
        };
    }

    public String toString() {
        return format == null ? input : format.prefixWithDelimiter() + input;
    }

    public static InputLine valueOf(final String input) {
        return Stream.of(Format.values())
                .filter(s -> input.startsWith(s.prefixWithDelimiter()))
                .findFirst()
                .map(s -> new InputLine(s, input.substring(s.prefixWithDelimiter().length()), null))
                .orElseGet(() -> new InputLine(null, input, null));
    }

    public static class Converter implements CommandLine.ITypeConverter<InputLine> {

        @Override
        public InputLine convert(final String value) throws Exception {
            return InputLine.valueOf(value);
        }

    }

}
