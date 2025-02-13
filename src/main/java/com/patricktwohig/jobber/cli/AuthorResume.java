package com.patricktwohig.jobber.cli;

import com.google.inject.Guice;
import com.google.inject.Module;
import com.patricktwohig.jobber.ai.ResumeAuthor;
import com.patricktwohig.jobber.format.Postprocessor;
import com.patricktwohig.jobber.format.ResumeFormatter;
import com.patricktwohig.jobber.guice.DocxFormatModule;
import com.patricktwohig.jobber.guice.HtmlUnitPageInputModule;
import com.patricktwohig.jobber.guice.JsonDocumentInputModule;
import com.patricktwohig.jobber.guice.JsonFormatModule;
import com.patricktwohig.jobber.input.DocumentInput;
import com.patricktwohig.jobber.input.PageInput;
import com.patricktwohig.jobber.model.Resume;
import picocli.CommandLine;

import java.io.BufferedOutputStream;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import static com.patricktwohig.jobber.cli.ExitCode.INVALID_PARAMETER;
import static com.patricktwohig.jobber.cli.Format.*;

@CommandLine.Command(
        name = "author",
        description = "Authors the resume, tuning it to the job description provided.",
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
                description = "The resume output file.",
                required = true
        )
        private OutputLine output;
        @CommandLine.Option(
                names = {"-kp", "--keep-properties"},
                description = "Specifies properties of the original document to keep",
                defaultValue = ""
        )
        private Set<String> keepProperties;

        @CommandLine.Option(
                names = {"-op", "--omit-properties"},
                description = "Specifies the properties of the new document to omit entirely.",
                defaultValue = ""
        )
        private Set<String> omitProperties;

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

                return Stream.of(formatModule, documentInputModule, new HtmlUnitPageInputModule());

        }

        @Override
        public Integer call() throws Exception {

                final var modules = concat(parent).loadModules();
                final var injector = Guice.createInjector(modules);
                final var resumeAuthor = injector.getInstance(ResumeAuthor.class);
                final var documentInput = injector.getInstance(DocumentInput.class);
                final var resumeFormatter = injector.getInstance(ResumeFormatter.class);
                final var postprocessor = injector.getInstance(Postprocessor.Factory.class).get(Resume.class)
                        .omit(omitProperties)
                        .keep(keepProperties)
                        .build();

                try (var is = input.readInputStream(JSON)) {

                        final var resume = documentInput.read(Resume.class, is);

                        final String jobDescriptionText;

                        if (jobDescription != null) {
                                jobDescriptionText = jobDescription.readInputString(TEXT);
                        } else if (jobDescriptionUrl != null) {
                                final var pageInput = injector.getInstance(PageInput.class);
                                final var jobDescriptionUrl = this.jobDescriptionUrl.readInputString(VAL);
                                jobDescriptionText = pageInput.loadPage(jobDescriptionUrl);
                        } else {
                                throw new CliException(INVALID_PARAMETER);
                        }

                        final var authoredResumed = resumeAuthor.tuneResumeForPublicJobDescriptionUrl(
                                resume,
                                jobDescriptionText
                        );

                        try (var os = new BufferedOutputStream(output.openOutputFileOrStdout())) {
                                resumeFormatter.format(authoredResumed, os);
                        }

                }

                return 0;

        }

}
