package com.patricktwohig.jobber.model;

public enum PositionType {

    FOUNDER("Founder"),

    EMPLOYEE("Employment"),

    CONSULTANT("Consulting");

    private final String displayText;

    PositionType(final String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }

}
