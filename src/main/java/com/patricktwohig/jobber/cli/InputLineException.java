package com.patricktwohig.jobber.cli;

public class InputLineException extends IllegalArgumentException{

    public InputLineException() {}

    public InputLineException(String s) {
        super(s);
    }

    public InputLineException(String message, Throwable cause) {
        super(message, cause);
    }

    public InputLineException(Throwable cause) {
        super(cause);
    }

}
