package com.patricktwohig.jobber.cli;

import com.google.inject.Module;
import com.patricktwohig.jobber.config.Configuration;
import com.patricktwohig.jobber.config.PropertiesConfiguration;
import com.patricktwohig.jobber.guice.*;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;

import java.util.stream.Stream;

import static com.patricktwohig.jobber.cli.Format.LITERAL;
import static com.patricktwohig.jobber.config.Configuration.*;

@Command(
        name = "jobber",
        description = "Performs AI-based resume, cover letter, job description analysis.",
        subcommands = {HelpCommand.class, ResumeCommands.class, CoverLetterCommands.class, Assistant.class}
)
public class Main implements HasModules {

    @CommandLine.Option(
            names = {"-to", "--api-timeout"},
            description = "The API Call timeout. Expressed in seconds."
    )
    private InputLine apiTimeout = InputLine.EMPTY;

    @CommandLine.Option(
            names = {"-log", "--log-api-calls"},
            description = "Logs all API Calls for the purposes of debugging."
    )
    private InputLine logApiCalls = InputLine.EMPTY;

    @CommandLine.Option(
            names = {"--openai-model-chat"},
            description = "The OpenAI LLM to use.",
            defaultValue = "gpt-4o"
    )
    private InputLine openAIChatModel = InputLine.EMPTY;

    @CommandLine.Option(
            names = {"--openai-model-embedding"},
            description = "The OpenAI LLM to use.",
            defaultValue = "text-embedding-ada-002"
    )
    private InputLine openAiEmbeddingModel = InputLine.EMPTY;

    @CommandLine.Option(
            names = {"--openai-api-key"},
            description = "The OpenAI API Key"
    )
    private InputLine openAIApiKey = EnvironmentVariables.OPENAI_API_KEY.getInputLine();

    @CommandLine.Option(
            names = "--max-message-count",
            description = "The maximum message memory count."
    )
    private InputLine maxMessageCount = InputLine.EMPTY;

    @CommandLine.Option(
            names = "--embedding-parallel-threads",
            description = "The number of parallel threads to use when embedding."
    )
    private InputLine embeddingParallelThreads = InputLine.EMPTY;

    @CommandLine.Option(
            names = "--embedding-max-segment-size-chars",
            description = "The maximum number of segment size in characters to use when embedding."
    )
    private InputLine embeddingMaxSegmentSizeChars = InputLine.EMPTY;

    @CommandLine.Option(
            names = "--embedding-max-overlap-size-chars",
            description = "The maximum number of overlap size in characters to use when embedding."
    )
    private InputLine embeddingMaxOverlapSizeChars = InputLine.EMPTY;

    @Override
    public Stream<Module> get() {
        return Stream.of(
                new OpenAIModule(),
                new AiServicesModule(),
                new InMemoryChatMemoryModule(),
                new InMemoryDocumentStoreModule(),
                new ConfigurationModule()
                        .add(PropertiesConfiguration.fromUserHomeDirectory())
                        .add(API_TIMEOUT, apiTimeout.tryReadInputString(LITERAL))
                        .add(LOG_API_CALLS, logApiCalls.tryReadInputString(LITERAL))
                        .add(MESSAGE_MEMORY_COUNT, maxMessageCount.tryReadInputString(LITERAL))
                        .add(OPENAI_CHAT_MODEL, openAIChatModel.tryReadInputString(LITERAL))
                        .add(OPENAI_EMBEDDING_MODEL, openAiEmbeddingModel.tryReadInputString(LITERAL))
                        .add(OPENAI_API_KEY, openAIApiKey.tryReadInputString(LITERAL))
                        .add(EMBEDDING_PARALLEL_THREADS, embeddingParallelThreads.tryReadInputString(LITERAL))
                        .add(EMBEDDING_MAX_SEGMENT_SIZE_CHARS, embeddingMaxSegmentSizeChars.tryReadInputString(LITERAL))
                        .add(EMBEDDING_MAX_SEGMENT_OVERLAP_CHARS, embeddingMaxOverlapSizeChars.tryReadInputString(LITERAL))
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
