package com.patricktwohig.jobber.cli;

public interface HasFormat {

    Format format();

    default Format format(Format defaultFormat) {
        return format() == null ? defaultFormat : format();
    }

}
