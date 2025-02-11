package com.patricktwohig.jobber.cli;

import picocli.CommandLine;
import picocli.CommandLine.HelpCommand;

@CommandLine.Command(
        name = "resume",
        aliases = "cv",
        description = "Performs resume/cv analysis tasks.",
        subcommands = {HelpCommand.class, AnalyzeResume.class, AuthorResume.class}
)
public class Resume {

    @CommandLine.ParentCommand
    private Main main;

    public Main getMain() {
        return main;
    }

}
