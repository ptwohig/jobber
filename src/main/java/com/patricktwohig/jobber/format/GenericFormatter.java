package com.patricktwohig.jobber.format;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public interface GenericFormatter {

    /**
     * Formats the given object and writes it to the provided output stream.
     *
     * @param object the object to format
     * @param outputStream the output stream to write to
     * @throws IOException if an I/O error occurs
     */
    void format(Object object, OutputStream outputStream) throws IOException;

    /**
     * Formats the given object and returns it as a string.
     *
     * @param object the object
     * @return the string
     */
    default String format(Object object) {
        try (var outputStream = new ByteArrayOutputStream()) {
            format(object, outputStream);
            return outputStream.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to format object", e);
        }
    }

}
