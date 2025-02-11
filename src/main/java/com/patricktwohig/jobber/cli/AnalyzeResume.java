package com.patricktwohig.jobber.cli;

import picocli.CommandLine;

import java.net.URI;
import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "analyze-resume",
        description = "Analyzes the resume",
        helpCommand = true
)
public class AnalyzeResume implements Callable<Integer> {

    @CommandLine.ParentCommand
    private Main main;

    @CommandLine.Option(
            names = {"--input"},
            description = "The resume to read in (MS Word DOCX Format).",
            required = true
    )
    private URI resume;

    @Override
    public Integer call() throws Exception {
        return 0;
    }

}
