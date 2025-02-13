package com.patricktwohig.jobber.cli;

import com.google.inject.Guice;
import com.google.inject.Module;
import com.patricktwohig.jobber.format.Postprocessor;
import com.patricktwohig.jobber.format.ResumeFormatter;
import com.patricktwohig.jobber.guice.DocxFormatModule;
import com.patricktwohig.jobber.guice.JacksonPostprocessorModule;
import com.patricktwohig.jobber.guice.JsonDocumentInputModule;
import com.patricktwohig.jobber.guice.JsonFormatModule;
import com.patricktwohig.jobber.input.DocumentInput;
import com.patricktwohig.jobber.model.Resume;
import picocli.CommandLine;

import java.io.BufferedOutputStream;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import static com.patricktwohig.jobber.cli.Format.JSON;

@CommandLine.Command(
        name = "format",
        aliases = "fmt",
        description = "Formats the resume from the internal JSON format.",
        subcommands = {CommandLine.HelpCommand.class}
)
public class FormatResume implements HasModules, Callable<Integer> {

    @CommandLine.Option(
            names = {"-i", "--input"},
            description = "The resume to read in from structured format.",
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
            names = {"-op", "--omit-properties"},
            description = "Specifies the properties of the new document to omit entirely."
    )
    private Set<String> omitProperties = Set.of();

    @Override
    public Stream<Module> get() {

        final var formatModule = switch (output.format(JSON)) {
            case JSON -> new JsonFormatModule();
            case DOCX -> new DocxFormatModule();
            default -> throw new CliException(ExitCode.UNSUPPORTED_OUTPUT_FORMAT);
        };

        final var documentInputModule = switch (input.format(JSON)) {
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
        final var resumeFormatter = guice.getInstance(ResumeFormatter.class);
        final var postprocessor = guice.getInstance(Postprocessor.Factory.class).get(Resume.class)
                .omit(omitProperties)
                .build();

        try (var is = input.readInputStream(JSON)) {

            final var resume = documentInput.read(Resume.class, is);
            final var postprocessed = postprocessor.apply(resume, resume);

            try (var os = new BufferedOutputStream(output.openOutputFileOrStdout())) {
                resumeFormatter.format(postprocessed, os);
            }

        }

        return 0;

    }

}
