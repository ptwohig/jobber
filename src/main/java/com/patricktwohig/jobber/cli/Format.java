package com.patricktwohig.jobber.cli;

import java.util.Optional;
import java.util.stream.Stream;

public enum Format {

    TEXT,
    DOCX,
    JSON,
    ENV;

    private final String prefix = name().toLowerCase();

    private final String prefixWithDelimiter = prefix + InputLine.DELIMITER;

    public static Optional<Format> fromSchemeName(final String scheme) {
        return Stream.of(values())
                .filter(s -> s.prefix.equals(scheme))
                .findFirst();
    }

    public String prefixWithDelimiter() {
        return prefixWithDelimiter;
    }

}
