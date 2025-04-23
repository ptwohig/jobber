package com.patricktwohig.jobber.cli;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.patricktwohig.jobber.ai.*;
import com.patricktwohig.jobber.format.CoverLetterFormatter;
import com.patricktwohig.jobber.format.GenericFormatter;
import com.patricktwohig.jobber.format.ResumeFormatter;
import com.patricktwohig.jobber.guice.HtmlUnitPageInputModule;
import com.patricktwohig.jobber.guice.JacksonPostprocessorModule;
import com.patricktwohig.jobber.guice.JsonDocumentInputModule;
import com.patricktwohig.jobber.input.DocumentInput;
import com.patricktwohig.jobber.input.PageInput;
import com.patricktwohig.jobber.model.*;
import picocli.CommandLine;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import static com.patricktwohig.jobber.cli.ExitCode.*;
import static com.patricktwohig.jobber.cli.Format.*;
import static com.patricktwohig.jobber.model.Task.NO_TASK_REQUESTED;

@CommandLine.Command(
        name = "assistant",
        description = "Provides interactive assistance with a specific job description.",
        subcommands = {CommandLine.HelpCommand.class}
)
public class Assistant implements HasModules, Callable<Integer> {

    @CommandLine.ParentCommand
    private HasModules parent;

    @CommandLine.Option(
            names = {"-ir", "--input-resume"},
            description = "The resume to read in from structured format.",
            required = true
    )
    private InputLine inputResume;

    @CommandLine.Option(
            names = {"-ic", "--input-cover-letter"},
            description = "The cover letter to read in from structured format.",
            required = true
    )
    private InputLine inputCoverLetter;

    @CommandLine.Option(
            names = {"-jdt", "--job-description"},
            description = "The text of the job description. Specify "
    )
    private InputLine jobDescription;

    @CommandLine.Option(
            names = {"-jdu", "--job-description-url"},
            description = "The URL of the job description to use when authoring the resume."
    )
    private InputLine jobDescriptionUrl;

    @CommandLine.Option(
            names = {"-of", "--output-formats"},
            description = "The resume output file."
    )
    private Set<Format> outputFormats = Set.of(Format.DOCX, Format.JSON);

    @CommandLine.Option(
            names = {"-od", "--output-directory"},
            description = "The output directory.",
            defaultValue = "."
    )
    private Path outputDirectory;

    @Override
    public Stream<Module> get() {

        final var documentInputModule = switch (inputResume.format(JSON)) {
            case JSON -> new JsonDocumentInputModule();
            default -> throw new CliException(ExitCode.UNSUPPORTED_INPUT_FORMAT);
        };

        if (!inputResume.format(JSON).equals(inputCoverLetter.format(JSON))) {
            throw new CliException(ExitCode.UNSUPPORTED_INPUT_FORMAT);
        }

        return Stream.of(documentInputModule, new HtmlUnitPageInputModule(), new JacksonPostprocessorModule());

    }

    private Injector injector;

    private Resume resume;

    private CoverLetter coverLetter;

    private String jobDescriptionText;

    private final UndoStack<Resume> resumeUndoStack = new UndoStack<>();

    private final UndoStack<CoverLetter> coverLetterUndoStack = new UndoStack<>();

    private final FormatterSetFactory formatterSetFactory = new FormatterSetFactory();

    @Override
    public Integer call() throws Exception {

        injector = concat(parent).newInjector();

        final var documentInput = injector.getInstance(DocumentInput.class);
        resume = readInput(inputResume, Resume.class, documentInput);
        coverLetter = readInput(inputCoverLetter, CoverLetter.class, documentInput);
        jobDescriptionText = readJobDescription();

        return runInteractive();

    }

    private <T> T readInput(final InputLine input,
                            final Class<T> type,
                            final DocumentInput documentInput) {
        try (var is = input.readInputStream(JSON)) {
            return documentInput.read(type, is);
        } catch (IOException ex) {
            throw new CliException(IO_EXCEPTION, ex);
        }
    }

    private String readJobDescription() throws IOException {
        if (jobDescriptionUrl != null) {
            final var pageInput = injector.getInstance(PageInput.class);
            final var jobDescriptionUrl = this.jobDescriptionUrl.readInputString(LITERAL);
            final var jobDescriptionText = pageInput.loadPage(jobDescriptionUrl);
            return jobDescriptionText;
        } else if (jobDescription != null) {
            return jobDescription.readInputString(TEXT);
        } else {
            throw new CliException(INVALID_PARAMETER);
        }
    }

    private Integer runInteractive() throws Exception {

        final var resultFormatter = formatterSetFactory
                .createFormatterSet(GenericFormatter.class)
                .getFormatter(TEXT);

        final var scanner = new Scanner(System.in);

        final var taskResolver = injector.getInstance(TaskResolver.class);
        final var resumeAuthor = injector.getInstance(ResumeAuthor.class);
        final var generalAssistant = injector.getInstance(GeneralAssistant.class);
        final var coverLetterAuthor = injector.getInstance(CoverLetterAuthor.class);
        final var resumeAnalyst = injector.getInstance(ResumeAnalyst.class);
        final var coverLetterAnalyst = injector.getInstance(CoverLetterAnalyst.class);
        final var jobsDescriptionAnalyst = injector.getInstance(JobDescriptionAnalyst.class);

        System.out.printf("Just give me a second to go over what you provided...%n");

        final var documentStore = injector.getInstance(DocumentStore.class);
        documentStore.upsertDocument(resume, "Resume for candidate.");
        documentStore.upsertDocument(coverLetter, "Cover letter for candidate.");

        System.out.printf("Now let me read the job description...%n");

        final var generalRemarks = jobsDescriptionAnalyst.analyzeJobDescription(jobDescriptionText);
        final var jobDescriptionSummary = jobsDescriptionAnalyst.summarizeJobDescription(jobDescriptionText);

        System.out.printf("%nJob Description Summary:%n");
        resultFormatter.format(jobDescriptionSummary, System.out);

        System.out.printf("%nMy General Remarks:%n");
        resultFormatter.format(generalRemarks, System.out);

        Task task;

        do {

            System.out.print("> ");
            final var prompt = scanner.nextLine();

            if (prompt.isBlank()) {
                task = NO_TASK_REQUESTED;
                continue;
            }

            System.out.println("Just a moment...");
            final var result = taskResolver.resolve(prompt);
            System.out.println(result.getRemarks());

            task = result.getTask();
            task = (task == null) ? NO_TASK_REQUESTED : task;

            switch (task) {
                case NO_TASK_REQUESTED -> {
                    final var generalFeedback = generalAssistant.provideGeneralFeedback(prompt);
                    resultFormatter.format(generalFeedback, System.out);
                }
                case REVERT_RESUME -> {

                    final var undoRequest = generalAssistant.undoTheRequestedActions(
                            prompt,
                            resumeUndoStack.getMaxSize()
                    );

                    resume = resumeUndoStack.pop(undoRequest.getCount())
                            .orElseGet(() -> {
                                final var documentInput = injector.getInstance(DocumentInput.class);
                                return readInput(inputResume, Resume.class, documentInput);
                            });

                    resultFormatter.format(resume, System.out);

                }
                case REVERT_COVER_LETTER -> {

                    final var undoRequest = generalAssistant.undoTheRequestedActions(
                            prompt,
                            coverLetterUndoStack.getMaxSize()
                    );

                    coverLetter = coverLetterUndoStack.pop(undoRequest.getCount())
                            .orElseGet(() -> {
                                final var documentInput = injector.getInstance(DocumentInput.class);
                                return readInput(inputCoverLetter, CoverLetter.class, documentInput);
                            });

                    resultFormatter.format(coverLetter, System.out);

                }
                case UPDATE_RESUME_WITH_COMMENTS -> {

                    final var resumeResponse = resumeAuthor.tuneResumeBasedOnJobSeekersComments(
                            resume,
                            jobDescriptionSummary,
                            prompt
                    );

                    resume = resumeResponse.getResume();
                    resultFormatter.format(resumeResponse, System.out);

                    writeResume(resumeResponse);

                    final var documentId = documentStore.upsertDocument(
                            resumeResponse,
                            resumeResponse.getFileName()
                    );

                    resumeUndoStack.push(resume, () -> documentStore.remove(documentId));

                }
                case UPDATE_COVER_LETTER_WITH_COMMENTS -> {

                    final var coverLetterResponse = coverLetterAuthor.tuneCoverLetterBasedOnJobSeekersComments(
                            coverLetter,
                            jobDescriptionSummary,
                            prompt
                    );

                    coverLetter = coverLetterResponse.getCoverLetter();
                    resultFormatter.format(coverLetterResponse, System.out);

                    writeCoverLetter(coverLetterResponse);

                    final var documentId = documentStore.upsertDocument(
                            coverLetterResponse,
                            coverLetterResponse.getFileName());

                    coverLetterUndoStack.push(coverLetter, () -> documentStore.remove(documentId));

                }
                case PROVIDE_RESUME_ANALYSIS -> {
                    final var remarks = resumeAnalyst.analyzeResume(resume);
                    resultFormatter.format(remarks, System.out);
                }
                case PROVIDE_COVER_LETTER_ANALYSIS -> {
                    final var remarks = coverLetterAnalyst.analyzeCoverLetter(coverLetter);
                    resultFormatter.format(remarks, System.out);
                }
            }

        } while (!Task.QUIT.equals(task));

        return 0;

    }

    private void writeResume(final InteractiveResumeResponse resumeResponse) {
        write(
            resumeResponse.getFileName(),
            ResumeFormatter.class,
            (formatter, os) -> formatter.format(resumeResponse.getResume(), os)
        );
    }

    private void writeCoverLetter(final InteractiveCoverLetterResponse coverLetterResponse) {
        write(
                coverLetterResponse.getFileName(),
                CoverLetterFormatter.class,
                (formatter, os) -> formatter.format(coverLetterResponse.getCoverLetter(), os)
        );
    }

    private <FormatterT> void write(
            final String fileName,
            final Class<FormatterT> formatterTClass,
            final FormatterConsumer<FormatterT> formatterTConsumer) {
        outputFormats.forEach(format -> {

            final var formatter = formatterSetFactory
                    .createFormatterSet(formatterTClass)
                    .getFormatter(format);

            final var suffix = format
                    .suffixes()
                    .findFirst()
                    .orElseThrow(() -> new CliException(UNSUPPORTED_OUTPUT_FORMAT));

            var fullFileName = outputDirectory.resolve(fileName + suffix).toAbsolutePath();

            for (int i = 0; Files.exists(fullFileName); i++) {
                fullFileName = outputDirectory.resolve(fileName + "_" + i + suffix).toAbsolutePath();
            }

            try (final var os = Files.newOutputStream(fullFileName);
                 final var bos = new BufferedOutputStream(os)){
                formatterTConsumer.accept(formatter, os);
                System.out.printf("Wrote file %s%n", fullFileName);
            } catch (Exception e) {
                System.err.println("Failed to format: " + e.getMessage());
            }

        });
    }

    @FunctionalInterface
    private interface FormatterConsumer<FormatterT> {
        void accept(FormatterT formatter, OutputStream os) throws IOException;
    }

}
