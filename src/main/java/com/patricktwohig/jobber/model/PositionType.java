package com.patricktwohig.jobber.model;

public enum PositionType {

    /**
     * A founder position.
     */
    FOUNDER("Founder"),

    /**
     * An employment position.
     */
    EMPLOYEE("Employment"),

    /**
     * A contract position.
     */
    CONSULTANT("Consulting"),

    /**
     * A general position, just listed as "Professional".
     */
    PROFESSIONAL("Professional"),

    /**
     * A position involving an open source project (contributor).
     */
    OPEN_SOURCE("Open Source");

    private final String displayText;

    PositionType(final String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }

}
