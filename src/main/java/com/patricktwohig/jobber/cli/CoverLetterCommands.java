package com.patricktwohig.jobber.cli;

import com.google.inject.Module;
import com.patricktwohig.jobber.format.CoverLetterFormatter;
import picocli.CommandLine;
import picocli.CommandLine.HelpCommand;

import java.util.stream.Stream;

@CommandLine.Command(
        name = "cover-letter",
        aliases = "cl",
        description = "Performs cover letter tasks.",
        subcommands = {
                HelpCommand.class,
                AnalyzeCoverLetter.class,
                AuthorCoverLetter.class,
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
