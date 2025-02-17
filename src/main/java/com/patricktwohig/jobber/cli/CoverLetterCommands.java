package com.patricktwohig.jobber.cli;

import com.google.inject.Module;
import picocli.CommandLine;
import picocli.CommandLine.HelpCommand;

import java.util.stream.Stream;

@CommandLine.Command(
        name = "cover-letter",
        aliases = "cl",
        description = "Performs cover letter tasks.",
        subcommands = {
                HelpCommand.class,
                AuthorCoverLetter.class,
                AnalyzeCoverLetter.class,
                TuneCoverLetter.class,
                FormatCoverLetter.class
        }
)
public class CoverLetterCommands implements HasModules {

    @CommandLine.ParentCommand
    private HasModules parent;

    @Override
    public Stream<Module> get() {
        return parent.get();
    }

}
