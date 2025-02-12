package com.patricktwohig.jobber.cli;

import com.google.inject.Module;
import com.patricktwohig.jobber.ai.OpenAI;
import com.patricktwohig.jobber.guice.AnalyzersModule;
import com.patricktwohig.jobber.guice.ConfigurationModule;
import com.patricktwohig.jobber.guice.OpenAIModule;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;

import java.util.stream.Stream;

import static com.patricktwohig.jobber.cli.InputLine.Scheme.TEXT;

@Command(
        name = "jobber",
        description = "Performs AI-based resume, cover letter, job description analysis.",
        subcommands = {HelpCommand.class, Resume.class, CoverLetter.class}
)
public class Main implements HasModules {

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
    private InputLine openAIApiKey;

    @Override
    public Stream<Module> get() {
        return Stream.of(
                new OpenAIModule(),
                new AnalyzersModule(),
                new ConfigurationModule()
                        .add(OpenAI.OPENAI_MODEL, openAIModel.readInputString(TEXT))
                        .add(OpenAI.OPENAI_API_KEY, openAIApiKey.readInputString(TEXT))
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
