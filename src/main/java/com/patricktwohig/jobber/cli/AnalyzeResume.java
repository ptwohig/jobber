package com.patricktwohig.jobber.cli;

import com.google.inject.Guice;
import com.google.inject.Module;
import com.patricktwohig.jobber.ai.ResumeAnalyst;
import com.patricktwohig.jobber.util.URIs;
import picocli.CommandLine;
import picocli.CommandLine.HelpCommand;

import java.net.URI;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import static com.patricktwohig.jobber.util.URIs.FILE_SCHEME;

@CommandLine.Command(
        name = "analyze",
        aliases = "ar",
        description = "Analyzes the input resume and converts to structured JSON.",
        subcommands = {HelpCommand.class}
)
public class AnalyzeResume implements Callable<Integer> {

    @CommandLine.ParentCommand
    private Resume resumeCommand;

    @CommandLine.Option(
            names = {"-i", "--input"},
            description = "The resume to read in (MS Word DOCX Format).",
            required = true
    )
    private URI inputUri;

    @CommandLine.Option(
            names = {"-o", "--output"},
            description = "The resume output file (in structured JSON).",
            required = true
    )
    private URI outputUri;

    public Stream<Module> loadModules() {
        return Stream.of();
    }

    @Override
    public Integer call() throws Exception {

        final var modules = Stream.concat(
                loadModules(),
                resumeCommand.getMain().loadModules()
        ).toArray(Module[]::new);

        final var guice = Guice.createInjector(modules);
        final var analyst = guice.getInstance(ResumeAnalyst.class);
        final var resumeText = URIs.getValue(inputUri, FILE_SCHEME);
        final var parsedResume = analyst.analyzePlainText(resumeText);

        return 0;
    }

}
