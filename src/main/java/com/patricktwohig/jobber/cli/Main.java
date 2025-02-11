package com.patricktwohig.jobber.cli;

import com.google.inject.Module;
import com.patricktwohig.jobber.ai.OpenAI;
import com.patricktwohig.jobber.guice.AnalyzersModule;
import com.patricktwohig.jobber.guice.ConfigurationModule;
import com.patricktwohig.jobber.guice.OpenAIModule;
import com.patricktwohig.jobber.util.URIs;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;

import java.net.URI;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import static com.patricktwohig.jobber.util.URIs.*;

@Command(
        name = "jobber",
        description = "Performs AI-based resume, cover letter, job description analysis.",
        subcommands = {HelpCommand.class, Resume.class, CoverLetter.class}
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
        return Stream.of(
                new OpenAIModule(),
                new AnalyzersModule(),
                new ConfigurationModule()
                        .add(OpenAI.OPENAI_MODEL, getValue(openAIModel, TEXT_SCHEME))
                        .add(OpenAI.OPENAI_API_KEY, getValue(openAIApiKey, TEXT_SCHEME))
        );
    }

    public static void main(final String[] args) {

        final var result = new CommandLine(new Main()).execute(args);

        if (result != 0) {
            System.exit(result);
        }

    }

}
