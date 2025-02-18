package com.patricktwohig.jobber.cli;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.patricktwohig.jobber.ai.CoverLetterAuthor;
import com.patricktwohig.jobber.format.CoverLetterFormatter;
import com.patricktwohig.jobber.guice.HtmlUnitPageInputModule;
import com.patricktwohig.jobber.guice.JacksonPostprocessorModule;
import com.patricktwohig.jobber.guice.JsonDocumentInputModule;
import com.patricktwohig.jobber.input.DocumentInput;
import com.patricktwohig.jobber.model.CoverLetter;
import picocli.CommandLine;

import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import static com.patricktwohig.jobber.cli.ExitCode.IO_EXCEPTION;
import static com.patricktwohig.jobber.cli.ExitCode.UNSUPPORTED_OUTPUT_FORMAT;
import static com.patricktwohig.jobber.cli.Format.JSON;
import static com.patricktwohig.jobber.cli.Format.TEXT;

@CommandLine.Command(
        name = "author",
        aliases = "au",
        description = "Authors the cover letter with interactive feedback.",
        subcommands = {CommandLine.HelpCommand.class}
)
public class AuthorCoverLetter implements Callable<Integer>, HasModules {

    @CommandLine.ParentCommand
    private HasModules parent;

    @CommandLine.Option(
            names = {"-i", "--input"},
            description = "The cover letter to read in from structured format.",
            required = true
    )
    private InputLine input;

    @CommandLine.Option(
            names = {"-o", "--output"},
            description = "The cover letter output file.",
            required = true
    )
    private Set<OutputLine> output = Set.of();

    @CommandLine.Option(
            names = {"-e", "--echo"},
            description = "Echo the full output of the resume after each interactive prompt."
    )
    private boolean echo = false;

    private FormatterSet<CoverLetterFormatter> formatters = new FormatterSet<>(CoverLetterFormatter.class);

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

        final var documentInput = injector.getInstance(DocumentInput.class);
        final var coverLetterAuthor = injector.getInstance(CoverLetterAuthor.class);

        var latestRevision = documentInput.read(CoverLetter.class, input.readInputStream());

        final var scanner = new Scanner(System.in);
        writeToStdout(latestRevision);

        System.out.printf("Using input %s and output %s%n", input, output);
        System.out.println("I will help you edit your cover lettter based on the comments you provide.");
        System.out.print(">> ");

        do {

            final var comments = scanner.nextLine();
            System.out.println("Just a moment...");

            final var revisions = coverLetterAuthor.tuneCoverLetterBasedOnJobSeekersComments(latestRevision, comments);
            latestRevision = revisions.getCoverLetter();

            if (echo)
                writeToStdout(revisions.getCoverLetter());

            System.out.println("--");
            writeAll(revisions.getCoverLetter());

            System.out.println("--");
            System.out.println(revisions.getRemarks());

            System.out.print(">> ");

        } while (scanner.hasNextLine());

        return 0;

    }

    public void writeToStdout(final CoverLetter coverLetter) throws IOException {
        final var formatter = formatters.getFormatter(TEXT);
        formatter.format(coverLetter, System.out);
        System.out.println();
    }

    public void writeAll(final CoverLetter coverLetter) {

        output.forEach(o -> {

            final var formatter = formatters.getFormatter(o.format(JSON));

            try (var os = o.openOutputFileOrStdout()) {
                formatter.format(coverLetter, os);
            } catch (IOException ex) {
                throw new CliException(IO_EXCEPTION, ex);
            }

        });
    }

}

