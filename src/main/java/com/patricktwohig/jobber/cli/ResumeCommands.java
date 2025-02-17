package com.patricktwohig.jobber.cli;

import com.google.inject.Module;
import picocli.CommandLine;
import picocli.CommandLine.HelpCommand;

import java.util.stream.Stream;

@CommandLine.Command(
        name = "resume",
        aliases = {"cv", "res"},
        description = "Performs resume/cv analysis tasks.",
        subcommands = {HelpCommand.class, AuthorResume.class, AnalyzeResume.class, TuneResume.class, FormatResume.class}
)
public class ResumeCommands implements HasModules {

    @CommandLine.ParentCommand
    private HasModules main;

    @Override
    public Stream<Module> get() {
        return main.get();
    }

}
