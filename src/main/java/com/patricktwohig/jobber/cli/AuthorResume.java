package com.patricktwohig.jobber.cli;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.patricktwohig.jobber.ai.ResumeAuthor;
import com.patricktwohig.jobber.format.ResumeFormatter;
import com.patricktwohig.jobber.guice.*;
import com.patricktwohig.jobber.input.DocumentInput;
import com.patricktwohig.jobber.model.Resume;
import picocli.CommandLine;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import static com.patricktwohig.jobber.cli.ExitCode.IO_EXCEPTION;
import static com.patricktwohig.jobber.cli.ExitCode.UNSUPPORTED_OUTPUT_FORMAT;
import static com.patricktwohig.jobber.cli.Format.JSON;
import static com.patricktwohig.jobber.cli.Format.TEXT;

@CommandLine.Command(
        name = "author",
        aliases = "au",
        description = "Authors the resume with interactive feedback.",
        subcommands = {CommandLine.HelpCommand.class}
)
public class AuthorResume implements Callable<Integer>, HasModules {

    @CommandLine.ParentCommand
    private HasModules parent;

    @CommandLine.Option(
            names = {"-i", "--input"},
            description = "The resume to read in from structured format.",
            required = true
    )
    private InputLine input;

    @CommandLine.Option(
            names = {"-e", "--echo"},
            description = "Echo the full output of the resume after each interactive prompt."
    )
    private boolean echo = false;

    @CommandLine.Option(
            names = {"-o", "--output"},
            description = "The resume output file.",
            required = true
    )
    private Set<OutputLine> output = Set.of();

    private FormatterSet<ResumeFormatter> formatters = new FormatterSet<>(ResumeFormatter.class);

    private Injector injector;

    @Override
    public Stream<Module> get() {

        final var documentInputModule = switch (input.format(JSON)) {
            case JSON -> new JsonDocumentInputModule();
            default -> throw new CliException(ExitCode.UNSUPPORTED_INPUT_FORMAT);
        };

        return Stream.of(documentInputModule, new HtmlUnitPageInputModule(), new JacksonPostprocessorModule());

    }

    @Override
    public Integer call() throws Exception {

        final var modules = concat(parent).loadModules();
        injector = Guice.createInjector(modules);

        output.forEach(o -> {

            if (o.destination().isBlank()) {
                throw new CliException(ExitCode.INVALID_OUTPUT_DESTINATION);
            }

            switch (o.format()) {
                case JSON, DOCX, TEXT: break;
                default: throw new CliException(UNSUPPORTED_OUTPUT_FORMAT);
            }

        });

        final var resumeAuthor = injector.getInstance(ResumeAuthor.class);
        final var documentInput = injector.getInstance(DocumentInput.class);

        var latestRevision = documentInput.read(Resume.class, input.readInputStream());

        final var scanner = new Scanner(System.in);

        System.out.printf("Using input %s and output %s%n", input, output);
        writeResumeToStdout(latestRevision);

        System.out.println("I will help you edit your resume based on the comments you provide.");
        System.out.print(">> ");

        do {

            final var comments = scanner.nextLine();
            System.out.println("Just a moment...");

            final var revisions = resumeAuthor.tuneResumeBasedOnJobSeekersComments(latestRevision, comments);
            latestRevision = revisions.getResume();

            if (echo) {
                System.out.println("--");
                writeResumeToStdout(revisions.getResume());
            }

            writeAll(revisions.getResume());

            System.out.println("--");
            System.out.println(revisions.getRemarks());

            System.out.print(">> ");

        } while (scanner.hasNextLine());

        return 0;

    }

    public void writeResumeToStdout(final Resume resume) {

        final var formatter = formatters.getFormatter(TEXT);

        try {
            formatter.format(resume, System.out);
        } catch (IOException ex) {
            throw new CliException(IO_EXCEPTION, ex);
        }

        System.out.println();

    }

    public void writeAll(final Resume resume) {

        output.forEach(o -> {

            final var formatter = formatters.getFormatter(o.format(JSON));

            try (var os = o.openOutputFileOrStdout()) {
                formatter.format(resume, os);
            } catch (IOException ex) {
                throw new CliException(IO_EXCEPTION, ex);
            }

        });
    }

}

