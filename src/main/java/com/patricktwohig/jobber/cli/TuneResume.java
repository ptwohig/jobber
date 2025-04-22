package com.patricktwohig.jobber.cli;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.patricktwohig.jobber.ai.ResumeAuthor;
import com.patricktwohig.jobber.format.Postprocessor;
import com.patricktwohig.jobber.format.ResumeFormatter;
import com.patricktwohig.jobber.guice.*;
import com.patricktwohig.jobber.input.DocumentInput;
import com.patricktwohig.jobber.input.PageInput;
import com.patricktwohig.jobber.model.Resume;
import picocli.CommandLine;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import static com.patricktwohig.jobber.cli.ExitCode.INVALID_PARAMETER;
import static com.patricktwohig.jobber.cli.ExitCode.UNSUPPORTED_OUTPUT_FORMAT;
import static com.patricktwohig.jobber.cli.Format.*;

@CommandLine.Command(
        name = "tune",
        description = "Authors the resume, tuning it to the job description provided.",
        subcommands = {CommandLine.HelpCommand.class}
)
public class TuneResume implements Callable<Integer>, HasModules {

        @CommandLine.ParentCommand
        private HasModules parent;

        @CommandLine.Option(
                names = {"-i", "--input"},
                description = "The resume to read in from structured format.",
                required = true
        )
        private InputLine input;

        @CommandLine.Option(
                names = {"-jdt", "--job-description"},
                description = "The text of the job description."
        )
        private InputLine jobDescription;

        @CommandLine.Option(
                names = {"-jdu", "--job-description-url"},
                description = "The URL of the job description to use when authoring the resume."
        )
        private InputLine jobDescriptionUrl;

        @CommandLine.Option(
                names = {"-o", "--output"},
                description = "The resume output file."
        )
        private Set<OutputLine> output = Set.of();

        @CommandLine.Option(
                names = {"-kp", "--keep-property"},
                description = "Specifies properties of the original document to keep"
        )
        private Set<String> keepProperties = Set.of();

        @CommandLine.Option(
                names = {"-op", "--omit-property"},
                description = "Specifies the properties of the new document to omit entirely."
        )
        private Set<String> omitProperties = Set.of();

        private Injector injector;

        private FormatterSet<ResumeFormatter> formatters = new FormatterSet<>(ResumeFormatter.class);

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

                output.forEach(o -> {

                        if (o.destination().isBlank()) {
                                throw new CliException(ExitCode.INVALID_OUTPUT_DESTINATION);
                        }

                        switch (o.format()) {
                                case JSON, DOCX, TEXT: break;
                                default: throw new CliException(UNSUPPORTED_OUTPUT_FORMAT);
                        }

                });

                final var modules = concat(parent).loadModules();
                injector = Guice.createInjector(modules);

                final var resumeAuthor = injector.getInstance(ResumeAuthor.class);
                final var documentInput = injector.getInstance(DocumentInput.class);
                final var postprocessor = injector.getInstance(Postprocessor.Factory.class).get(Resume.class)
                        .omit(omitProperties)
                        .keep(keepProperties)
                        .build();

                try (var is = input.readInputStream(JSON)) {

                        final var resume = documentInput.read(Resume.class, is);
                        final var jobDescriptionText = readJobDescription();

                        final var result = resumeAuthor.tuneResumeForPublicJobDescription(
                                resume,
                                jobDescriptionText
                        );

                        System.out.printf("%nScore: %s%%%n%nSummary:%n%s%n%nRemarks:%n%s%n%n",
                                result.getScore(),
                                result.getSummary(),
                                result.getRemarks()
                        );

                        final var authoredResume = result.getResult();

                        final var postProcessedResume = postprocessor.apply(resume, authoredResume);

                        if (output.isEmpty()) {
                                formatters.getFormatter(TEXT).format(postProcessedResume, System.out);
                        } else {
                                for (var o : output) {
                                        try (final var os = o.openOutputFileOrStdout()) {
                                                final var formatter = formatters.getFormatter(o.format(JSON));
                                                formatter.format(postProcessedResume, os);
                                        }
                                }
                        }

                }

                return 0;

        }

        private String readJobDescription() throws IOException {

                System.out.println("Reading Job Description...");

                try {
                        if (jobDescriptionUrl != null) {
                                final var pageInput = injector.getInstance(PageInput.class);
                                final var jobDescriptionUrl = this.jobDescriptionUrl.readInputString(LITERAL);
                                final var jobDescriptionText = pageInput.loadPage(jobDescriptionUrl);
                                System.out.println("Job Description: " + jobDescriptionText);
                                return jobDescriptionText;
                        } else if (jobDescription != null) {
                                return jobDescription.readInputString(TEXT);
                        } else {
                                throw new CliException(INVALID_PARAMETER);
                        }
                } finally {
                        System.out.println("Reading Job Description complete.");
                }

        }

}
