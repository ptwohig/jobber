package com.patricktwohig.jobber.cli;

import picocli.CommandLine;

public class CliException extends RuntimeException {

    private final int exitCode;

    public CliException(final ExitCode exitCode) {
        this("Error: " + exitCode, exitCode.ordinal());
    }

    public CliException(final ExitCode exitCode, final Throwable cause) {
        this("Error: " + exitCode, exitCode.ordinal(), cause);
    }

    public CliException(final String message, final int exitCode) {
        super(message);
        this.exitCode = exitCode;
    }

    public CliException(final String message, final int exitCode, final Throwable cause) {
        super(message, cause);
        this.exitCode = exitCode;
    }

    public int getExitCode() {
        return exitCode;
    }

}
