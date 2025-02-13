package com.patricktwohig.jobber.format;

public class PostprocessingException extends RuntimeException {

    public PostprocessingException() {
    }

    public PostprocessingException(String message) {
        super(message);
    }

    public PostprocessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostprocessingException(Throwable cause) {
        super(cause);
    }

    public PostprocessingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
