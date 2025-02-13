package com.patricktwohig.jobber.cli;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "author",
        aliases = {"write"},
        description = "Analyzes the candidates cover letter."
)
public class AuthorCoverLetter implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
                return 0;
        }

}
