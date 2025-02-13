package com.patricktwohig.jobber.cli;

import picocli.CommandLine;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
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
        return readInputString(Format.VAL);
    }

    public String readInputString(final Format defaultFormat) {
        return switch (format == null ? defaultFormat : format) {
            case VAL -> input;
            case ENV -> System.getenv(input);
            case JSON, TEXT -> readFile();
            default -> throw new CliException(ExitCode.UNSUPPORTED_INPUT_FORMAT);
        };
    }

    private String readFile() {
        try {
            return Files.readString(Path.of(input()));
        } catch (IOException e) {
            throw new CliException(ExitCode.IO_EXCEPTION, e);
        }
    }

    public InputStream readInputStream() throws IOException {
        return readInputStream(Format.VAL);
    }

    public InputStream readInputStream(final Format defaultFormat) throws IOException {
        return switch (format == null ? defaultFormat : format) {
            case VAL -> new ByteArrayInputStream(input.getBytes(charset));
            case ENV -> {
                final String env = System.getenv(input);
                yield new ByteArrayInputStream(env == null ? new byte[0] : env.getBytes(charset));
            }
            case DOCX, JSON, TEXT -> new FileInputStream(input);
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
