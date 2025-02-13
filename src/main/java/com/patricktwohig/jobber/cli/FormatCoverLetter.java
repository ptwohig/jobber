package com.patricktwohig.jobber.cli;

import com.google.inject.Guice;
import com.google.inject.Module;
import com.patricktwohig.jobber.format.CoverLetterFormatter;
import com.patricktwohig.jobber.format.Postprocessor;
import com.patricktwohig.jobber.guice.*;
import com.patricktwohig.jobber.input.DocumentInput;
import com.patricktwohig.jobber.model.CoverLetter;
import picocli.CommandLine;

import java.io.BufferedOutputStream;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import static com.patricktwohig.jobber.cli.Format.JSON;

@CommandLine.Command(
        name = "format",
        aliases = "fmt",
        description = "Formats the cover letter from the internal JSON format.",
        subcommands = {CommandLine.HelpCommand.class}
)
public class FormatCoverLetter implements HasModules, Callable<Integer> {

    @CommandLine.Option(
            names = {"-i", "--input"},
            description = "The resume input from structured data.",
            required = true
    )
    private InputLine input;

    @CommandLine.Option(
            names = {"-o", "--output"},
            description = "The resume output file.",
            required = true
    )
    private OutputLine output;

    @CommandLine.Option(
            names = {"-op", "--omit-property"},
            description = "Specifies the properties of the new document to omit entirely."
    )
    private Set<String> omitProperties = Set.of();

    @Override
    public Stream<Module> get() {

        final var outputFormat = output.format() == null ? JSON : output.format();

        final var formatModule = switch (outputFormat) {
            case JSON -> new JsonFormatModule();
            case DOCX -> new DocxFormatModule();
            case TEXT -> new PlainTextFormatModule();
            default -> throw new CliException(ExitCode.UNSUPPORTED_OUTPUT_FORMAT);
        };

        final var documentInputFormat = input.format() == null ? JSON : input.format();

        final var documentInputModule = switch (documentInputFormat) {
            case JSON -> new JsonDocumentInputModule();
            default -> throw new CliException(ExitCode.UNSUPPORTED_INPUT_FORMAT);
        };

        return Stream.of(formatModule, documentInputModule, new JacksonPostprocessorModule());

    }

    @Override
    public Integer call() throws Exception {

        if (input.format() != null && !input.format().equals(JSON)) {
            throw new CliException("Invalid input format:" + input, 1);
        }

        final var modules = loadModules();
        final var guice = Guice.createInjector(modules);
        final var documentInput = guice.getInstance(DocumentInput.class);
        final var resumeFormatter = guice.getInstance(CoverLetterFormatter.class);
        final var postprocessor = guice.getInstance(Postprocessor.Factory.class).get(CoverLetter.class)
                .omit(omitProperties)
                .build();

        try (var is = input.readInputStream(JSON)) {

            final var coverLetter = documentInput.read(CoverLetter.class, is);
            final var postprocessed = postprocessor.apply(coverLetter, coverLetter);

            try (var os = new BufferedOutputStream(output.openOutputFileOrStdout())) {
                resumeFormatter.format(postprocessed, os);
            }

        }

        return 0;

    }

}
