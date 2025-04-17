package com.patricktwohig.jobber.cli;

import picocli.CommandLine;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

import static com.patricktwohig.jobber.cli.Format.LITERAL;
import static com.patricktwohig.jobber.cli.Format.PREFIX_DELIMITER;

public record InputLine(Format format, String input, Charset charset) implements HasFormat {

    public InputLine {

        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        if (charset == null) {
            charset = Charset.defaultCharset();
        }

    }

    public String readInputString(final Format defaultFormat) {
        return tryReadInputString(defaultFormat).orElseThrow(() -> new CliException(ExitCode.UNSUPPORTED_INPUT_FORMAT));
    }

    public Optional<String> tryReadInputString(final Format defaultFormat) {
        return switch (format == null ? defaultFormat : format) {
            case LITERAL -> Optional.ofNullable(input);
            case ENV -> Optional.ofNullable(System.getenv(input));
            case JSON, TEXT -> tryReadFile();
            default -> Optional.empty();
        };
    }

    private Optional<String> tryReadFile() {
        try {
            final var result = Files.readString(Path.of(input()));
            return Optional.of(result);
        } catch (FileNotFoundException ex) {
            return Optional.empty();
        } catch (IOException e) {
            throw new CliException(ExitCode.IO_EXCEPTION, e);
        }
    }

    public InputStream readInputStream() throws IOException {
        return readInputStream(LITERAL);
    }

    public InputStream readInputStream(final Format defaultFormat) throws IOException {
        return switch (format == null ? defaultFormat : format) {
            case LITERAL -> new ByteArrayInputStream(input.getBytes(charset));
            case ENV -> {
                final String env = System.getenv(input);
                yield new ByteArrayInputStream(env == null ? new byte[0] : env.getBytes(charset));
            }
            case DOCX, JSON, TEXT -> new FileInputStream(input);
        };
    }

    public String toString() {
        return format == null ? input : format.getPrefix() + PREFIX_DELIMITER + input;
    }

    public static InputLine valueOf(final String input) {

        final var prefixStream = Format.allPrefixes()
                .filter(alias -> input.startsWith(alias.value()))
                .map(alias -> new InputLine(alias.format(), alias.stripPrefix(input), null));

        final var suffixStream = Format.allSuffixes()
                .filter(alias -> input.endsWith(alias.value()))
                .map(alias -> new InputLine(alias.format(), input, null));

        return Stream.concat(prefixStream, suffixStream)
                .findFirst()
                .orElseGet(() -> new InputLine(null, input, null));

    }

    public static class Converter implements CommandLine.ITypeConverter<InputLine> {

        @Override
        public InputLine convert(final String value) throws Exception {
            return InputLine.valueOf(value);
        }

    }

}
