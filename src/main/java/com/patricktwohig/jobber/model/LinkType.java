package com.patricktwohig.jobber.model;

public enum LinkType {

    GITHUB("GitHub"),

    LINKED_IN("LinkedIn"),

    COMPANY_WEBSITE("Company"),

    PERSONAL_WEBSITE("Website");

    private final String displayText;

    LinkType(final String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }

}
