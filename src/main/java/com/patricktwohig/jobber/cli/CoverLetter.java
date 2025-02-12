package com.patricktwohig.jobber.cli;

import picocli.CommandLine;
import picocli.CommandLine.HelpCommand;

@CommandLine.Command(
        name = "coverletter",
        aliases = "cl",
        description = "Performs cover letter tasks.",
        helpCommand = true,
        subcommands = {HelpCommand.class, AnalyzeCoverLetter.class, AuthorCoverLetter.class}
)
public class CoverLetter {}
