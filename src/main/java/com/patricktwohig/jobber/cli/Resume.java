package com.patricktwohig.jobber.cli;

import com.google.inject.Module;
import picocli.CommandLine;
import picocli.CommandLine.HelpCommand;

import java.util.stream.Stream;

@CommandLine.Command(
        name = "resume",
        aliases = "cv",
        description = "Performs resume/cv analysis tasks.",
        subcommands = {HelpCommand.class, AnalyzeResume.class, AuthorResume.class}
)
public class Resume implements HasModules {

    @CommandLine.ParentCommand
    private HasModules main;

    @Override
    public Stream<Module> get() {
        return main.get();
    }

}
