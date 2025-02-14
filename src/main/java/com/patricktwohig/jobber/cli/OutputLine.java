package com.patricktwohig.jobber.cli;

import picocli.CommandLine;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.Stream;

public record OutputLine(Format format, String destination) implements HasFormat {

    public static OutputLine valueOf(final String input) {

        final var prefixStream = Format.allPrefixes()
                .filter(alias -> input.startsWith(alias.value()))
                .map(alias -> new OutputLine(alias.format(), alias.stripPrefix(input)));

        final var suffixStream = Format.allSuffixes()
                .filter(alias -> input.endsWith(alias.value()))
                .map(alias -> new OutputLine(alias.format(), input));

        return Stream.concat(prefixStream, suffixStream)
                .findFirst()
                .orElseThrow(() -> new CliException(ExitCode.UNSUPPORTED_OUTPUT_FORMAT));

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
