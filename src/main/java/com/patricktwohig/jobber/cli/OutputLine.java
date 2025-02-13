package com.patricktwohig.jobber.cli;

import picocli.CommandLine;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.Stream;

public record OutputLine(Format format, String destination) implements HasFormat {

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

    public OutputStream openOutputFileOrDefault(final OutputStream defaultOutput) throws IOException {
        return destination == null || destination.isBlank()
                ? defaultOutput
                : new FileOutputStream(destination);
    }

    public static class Converter implements CommandLine.ITypeConverter<OutputLine> {

        @Override
        public OutputLine convert(final String value) {
            return OutputLine.valueOf(value);
        }

    }

}
