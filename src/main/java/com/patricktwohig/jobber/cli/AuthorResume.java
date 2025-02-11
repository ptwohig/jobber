package com.patricktwohig.jobber.cli;

import okhttp3.Call;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "author-resume",
        description = "Analyzes the candidates cover letter.",
        helpCommand = true
)
public class AuthorResume implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
                return 0;
        }
}
