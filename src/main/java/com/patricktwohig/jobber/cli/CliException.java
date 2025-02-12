package com.patricktwohig.jobber.cli;

public class CliException extends RuntimeException {

    private final int exitCode;

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
