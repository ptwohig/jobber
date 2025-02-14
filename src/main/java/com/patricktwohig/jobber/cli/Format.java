package com.patricktwohig.jobber.cli;

import java.util.List;
import java.util.stream.Stream;

import static java.lang.String.format;

public enum Format {

    LITERAL {
        @Override
        public Stream<String> suffixes() {
            return Stream.empty();
        }

        @Override
        public Stream<String> prefixes() {
            return Stream.empty();
        }
    },
    ENV {
        @Override
        public Stream<String> suffixes() {
            return Stream.empty();
        }
    },
    TEXT("text", "txt"),
    DOCX,
    JSON;

    public static final String PREFIX_DELIMITER = ":";

    public static final String SUFFIX_DELIMITER = ".";

    private final List<String> labels;

    Format() {
        this.labels = List.of(name().toLowerCase());
    }

    Format(final String ... labels) {
        this.labels = List.of(labels);
    }

    public String getPrefix() {
        return labels.getFirst();
    }

    public Stream<String> suffixes() {
        return labels.stream().map(label -> format("%s%s", SUFFIX_DELIMITER, label));
    }

    public static Stream<Alias> allSuffixes() {
        return Stream
                .of(values())
                .flatMap(format -> format.suffixes().map(value -> new Alias(format, value)));
    }

    public Stream<String> prefixes() {
        return labels.stream().map(label -> format("%s%s", label, PREFIX_DELIMITER));
    }

    public static Stream<Alias> allPrefixes() {
        return Stream
                .of(values())
                .flatMap(format -> format.prefixes().map(value -> new Alias(format, value)));
    }

    public record Alias(Format format, String value) {

        public String stripPrefix(final String input) {
            return input.startsWith(input)
                    ? input.substring(value.length())
                    : input;
        }

    }

}
