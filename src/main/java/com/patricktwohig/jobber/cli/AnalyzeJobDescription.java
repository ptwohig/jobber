package com.patricktwohig.jobber.cli;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "analyze-jd",
        description = "Analyzes the job description and provides a detailed report.",
        helpCommand = true
)
public class AnalyzeJobDescription implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        return 0;
    }

}
