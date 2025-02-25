package com.patricktwohig.jobber.cli;

import com.google.inject.Module;
import com.patricktwohig.jobber.ai.Configuration;
import com.patricktwohig.jobber.guice.AiServicesModule;
import com.patricktwohig.jobber.guice.ConfigurationModule;
import com.patricktwohig.jobber.guice.InMemoryChatMemoryModule;
import com.patricktwohig.jobber.guice.OpenAIModule;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;

import java.util.stream.Stream;

import static com.patricktwohig.jobber.cli.Format.LITERAL;

@Command(
        name = "jobber",
        description = "Performs AI-based resume, cover letter, job description analysis.",
        subcommands = {HelpCommand.class, ResumeCommands.class, CoverLetterCommands.class}
)
public class Main implements HasModules {

    @CommandLine.Option(
            names = {"-to", "--api-timeout"},
            description = "The API Call timeout. Expressed in seconds.",
            defaultValue = "180"
    )
    private InputLine apiTimeout;

    @CommandLine.Option(
            names = {"-log", "--log-api-calls"},
            description = "Logs all API Calls for the purposes of debugging.",
            defaultValue = "false"
    )
    private InputLine logApiCalls;

    @CommandLine.Option(
            names = {"--openai-model"},
            description = "The OpenAI LLM to use.",
            defaultValue = "gpt-4o"
    )
    private InputLine openAIModel;

    @CommandLine.Option(
            names = {"--openai-api-key"},
            description = "The OpenAI API Key"
    )
    private InputLine openAIApiKey = EnvironmentVariables.OPENAI_API_KEY.getInputLine();

    @CommandLine.Option(
            names = "--max-message-count",
            description = "The maximum message memory count.",
            defaultValue = "10"
    )
    private InputLine maxTokenCount;

    @Override
    public Stream<Module> get() {
        return Stream.of(
                new OpenAIModule(),
                new AiServicesModule(),
                new InMemoryChatMemoryModule(),
                new ConfigurationModule()
                        .add(Configuration.API_TIMEOUT, apiTimeout.readInputString(LITERAL))
                        .add(Configuration.LOG_API_CALLS, logApiCalls.readInputString(LITERAL))
                        .add(Configuration.MESSAGE_MEMORY_COUNT, maxTokenCount.readInputString(LITERAL))
                        .add(Configuration.OPENAI_MODEL, openAIModel.readInputString(LITERAL))
                        .add(Configuration.OPENAI_API_KEY, openAIApiKey.readInputString(LITERAL))
        );
    }

    public static void main(final String[] args) {

        try {

            final var result = new CommandLine(new Main())
                    .registerConverter(InputLine.class, new InputLine.Converter())
                    .registerConverter(OutputLine.class, new OutputLine.Converter())
                    .execute(args);

            if (result != 0) {
                System.exit(result);
            }

        } catch (CliException ex) {
            System.err.println(ex.getMessage());
            System.exit(ex.getExitCode());
        }

    }

}
