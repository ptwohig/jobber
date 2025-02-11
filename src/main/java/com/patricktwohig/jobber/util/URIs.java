package com.patricktwohig.jobber.util;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public class URIs {

    private URIs() {}

    public static final String TEXT_SCHEME = "text";

    public static final String FILE_SCHEME = "file";

    public static final String ENVIRONMENT_SCHEME = "env";

    public static String getValue(final URI uri, final String defaultScheme) {
        return switch (uri.getScheme() == null ? defaultScheme : uri.getScheme()) {
            case TEXT_SCHEME -> readUriComponents(uri);
            case FILE_SCHEME -> loadFile(uri);
            case ENVIRONMENT_SCHEME -> readEnvironment(uri);
            default -> throw new IllegalArgumentException("Unknown URI scheme: " + uri);
        };
    }

    private static String loadFile(final URI uri) {

        final var file = new File(uri.getPath());

        try (var fis = new FileInputStream(file);
             var doc = new XWPFDocument(fis);
             var extractor = new XWPFWordExtractor(doc)) {
            return extractor.getText();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

    }

    private static String readEnvironment(final URI uri) {
        final var environmentVariable = readUriComponents(uri);
        return System.getenv(environmentVariable);
    }

    private static String readUriComponents(final URI uri) {
        return Stream.of(uri.getAuthority(), uri.getHost(), uri.getPath())
                .filter(Objects::nonNull)
                .filter(not(String::isBlank))
                .collect(Collectors.joining("/"));
    }

}
