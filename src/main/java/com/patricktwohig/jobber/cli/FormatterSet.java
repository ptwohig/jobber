package com.patricktwohig.jobber.cli;

import com.google.inject.Guice;
import com.patricktwohig.jobber.format.CoverLetterFormatter;
import com.patricktwohig.jobber.guice.DocxFormatModule;
import com.patricktwohig.jobber.guice.JsonFormatModule;
import com.patricktwohig.jobber.guice.PlainTextFormatModule;

import java.util.EnumMap;
import java.util.Map;

import static com.patricktwohig.jobber.cli.ExitCode.UNSUPPORTED_OUTPUT_FORMAT;
import static com.patricktwohig.jobber.cli.Format.JSON;

public class FormatterSet<FormatterT> {

    private final Class<FormatterT> formatterClass;

    private final Map<Format, FormatterT> formatters = new EnumMap<>(Format.class);

    public FormatterSet(Class<FormatterT> formatterClass) {
        this.formatterClass = formatterClass;
    }

    public FormatterT getFormatter(final Format format) {
        return  formatters.computeIfAbsent(format, f -> {

            final var module = switch (format) {
                case JSON: yield new JsonFormatModule();
                case DOCX: yield new DocxFormatModule();
                case TEXT: yield new PlainTextFormatModule();
                default: throw new CliException(UNSUPPORTED_OUTPUT_FORMAT);
            };

            final var injector = Guice.createInjector(module);
            return injector.getInstance(formatterClass);

        });
    }

}
