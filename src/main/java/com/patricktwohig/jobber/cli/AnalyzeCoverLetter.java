package com.patricktwohig.jobber.cli;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "analyze-cover-letter",
        description = "Analyzes the candidates cover letter.",
        helpCommand = true
)
public class AnalyzeCoverLetter implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        return 0;
    }

}
