package com.patricktwohig.jobber.format.plain;

import com.patricktwohig.jobber.format.GenericFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlainTextGenericFormatter implements GenericFormatter {

    private static final Logger logger = LoggerFactory.getLogger(PlainTextGenericFormatter.class);

    private static final String INDENT = "  "; // Two spaces for indentation

    @Override
    public void format(Object object, OutputStream outputStream) throws IOException {

        if (object == null) {
            return;
        }

        final var writer = new PrintWriter(outputStream);
        formatObject(object, writer, 0);
        writer.flush();

    }

    private void formatObject(Object object, PrintWriter writer, int depth) {
        if (object == null) {
            return;
        }

        try {
            for (PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(object.getClass()).getPropertyDescriptors()) {
                Method readMethod = propertyDescriptor.getReadMethod();

                if (readMethod != null
                        && readMethod.canAccess(object)
                        && Modifier.isPublic(readMethod.getModifiers())
                        && !"class".equals(propertyDescriptor.getName())) {

                    Object value = readMethod.invoke(object);
                    String header = toTitleCase(propertyDescriptor.getName());
                    writeIndented(writer, header + ":", depth);

                    if (value instanceof Collection<?> collection) {
                        for (Object item : collection) {
                            if (isBean(item)) {
                                formatObject(item, writer, depth + 1);
                            } else {
                                writeIndented(writer, "- " + item, depth + 1);
                            }
                        }
                    } else if (isBean(value)) {
                        formatObject(value, writer, depth + 1);
                    } else {
                        writeIndented(writer, value, depth + 1);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Could not format object: {}", object, e);
        }

    }

    private boolean isBean(Object object) {
        return object != null && !(
                object instanceof String ||
                object instanceof Number ||
                object instanceof Boolean ||
                object instanceof Character ||
                object instanceof Enum<?>
        );
    }

    private void writeIndented(PrintWriter writer, Object text, int depth) {
        writer.println(INDENT.repeat(depth) + text);
    }

    private String toTitleCase(final String camelCase) {

        var result = camelCase.strip();

        if (result.isEmpty()) {
            return result;
        }

        result = toUpperCamelCase(result);

        return Stream
                .of(result.split("(?=[A-Z])"))
                .collect(Collectors.joining(" "));

    }

    private static String toUpperCamelCase(final String lowerCamelCase) {
        return Character.toUpperCase(lowerCamelCase.charAt(0)) + lowerCamelCase.substring(1);
    }

}
