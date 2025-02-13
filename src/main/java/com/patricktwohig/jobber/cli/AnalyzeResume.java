package com.patricktwohig.jobber.cli;

import com.google.inject.Guice;
import com.google.inject.Module;
import com.patricktwohig.jobber.ai.ResumeAnalyst;
import com.patricktwohig.jobber.format.ResumeFormatter;
import com.patricktwohig.jobber.guice.DocxFormatModule;
import com.patricktwohig.jobber.guice.DocxTextExtractorModule;
import com.patricktwohig.jobber.guice.JsonFormatModule;
import com.patricktwohig.jobber.input.DocumentTextExtractor;
import picocli.CommandLine;
import picocli.CommandLine.HelpCommand;

import java.io.BufferedOutputStream;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import static com.patricktwohig.jobber.cli.ExitCode.UNSUPPORTED_INPUT_FORMAT;
import static com.patricktwohig.jobber.cli.Format.DOCX;
import static com.patricktwohig.jobber.cli.Format.JSON;


@CommandLine.Command(
        name = "analyze",
        aliases = "ar",
        description = "Analyzes the input resume and converts to structured JSON.",
        subcommands = {HelpCommand.class}
)
public class AnalyzeResume implements Callable<Integer>, HasModules {

    @CommandLine.ParentCommand
    private HasModules parent;

    @CommandLine.Option(
            names = {"-i", "--input"},
            description = "The resume to read in (MS Word DOCX Format).",
            required = true
    )
    private InputLine input;

    @CommandLine.Option(
            names = {"-o", "--output"},
            description = "The resume output file (in structured JSON).",
            required = true
    )
    private OutputLine output;

    @Override
    public Stream<Module> get() {

        final var formatModule = switch (output.format(JSON)) {
            case JSON -> new JsonFormatModule();
            case DOCX -> new DocxFormatModule();
            default -> throw new CliException(ExitCode.UNSUPPORTED_OUTPUT_FORMAT);
        };

        return Stream.of(formatModule, new DocxTextExtractorModule());

    }

    @Override
    public Integer call() throws Exception {

        if (!input.format(DOCX).equals(DOCX)) {
            throw new CliException(UNSUPPORTED_INPUT_FORMAT);
        }

        final var modules = concat(parent).loadModules();
        final var guice = Guice.createInjector(modules);
        final var analyst = guice.getInstance(ResumeAnalyst.class);
        final var resumeFormatter = guice.getInstance(ResumeFormatter.class);
        final var documentTextExtractor = guice.getInstance(DocumentTextExtractor.class);

        try (var is = input.readInputStream(DOCX)) {
            final var resumeText = documentTextExtractor.read(is);
            final var parsedResume = analyst.analyzePlainText(resumeText);

            try (var os = new BufferedOutputStream(output.openOutputFileOrStdout())) {
                resumeFormatter.format(parsedResume, os);
            }

        }

        return 0;

    }

}
