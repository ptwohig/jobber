package com.patricktwohig.jobber.cli;

import com.google.inject.Module;
import com.patricktwohig.jobber.util.URIs;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.net.URI;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

@Command(
        name = "jobber",
        description = "Performs AI-based resume, cover letter, job description analysis.",
        helpCommand = true,
        subcommands = {
                AnalyzeResume.class,
                AnalyzeJobDescription.class,
                AuthorCoverLetter.class,
                AuthorResume.class
        }
)
public class Main {

    @CommandLine.Option(
            names = {"--openai-model"},
            description = "The OpenAI LLM to use.",
            defaultValue = "gpt-4o"
    )
    private URI openAIModel;

    @CommandLine.Option(
            names = {"--openai-api-key"},
            description = "The OpenAI API Key"
    )
    private URI openAIApiKey;

    public Stream<Module> loadModules() {
        return Stream.of();
    }

    public static void main(final String[] args) {

        final var result = new CommandLine(new Main()).execute(args);

        if (result != 0) {
            System.exit(result);
        }

    }

}
