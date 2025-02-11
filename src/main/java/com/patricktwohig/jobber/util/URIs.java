package com.patricktwohig.jobber.util;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;

public class URIs {

    private URIs() {}

    public static final String TEXT_SCHEME = "text";

    public static final String FILE_SCHEME = "file";

    public static final String ENVIRONMENT_SCHEME = "env";

    public static String getValue(final URI uri, final String defaultScheme) {
        return switch (uri.getScheme() == null ? defaultScheme : uri.getScheme()) {
            case TEXT_SCHEME -> uri.getAuthority();
            case FILE_SCHEME -> loadFile(uri);
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

}
