package com.patricktwohig.jobber.cli;

import picocli.CommandLine;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static com.patricktwohig.jobber.cli.Format.PREFIX_DELIMITER;
import static java.nio.file.Files.createDirectories;

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

        if (destination == null || destination.isBlank()) {
            return defaultOutput;
        }

        final var destinationPath = Paths.get(destination).toAbsolutePath();
        createDirectories(destinationPath.getParent());

        return new FileOutputStream(destination);

    }

    @Override
    public String toString() {
        return format == null ? destination : format.getPrefix() + PREFIX_DELIMITER + destination;
    }

    public static class Converter implements CommandLine.ITypeConverter<OutputLine> {

        @Override
        public OutputLine convert(final String value) {
            return OutputLine.valueOf(value);
        }

    }

}
