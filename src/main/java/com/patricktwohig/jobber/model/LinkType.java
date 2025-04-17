package com.patricktwohig.jobber.model;

public enum LinkType {

    GITHUB("GitHub", "\uD83D\uDCBB"),

    LINKED_IN("LinkedIn", "\uD83C\uDF10"),

    COMPANY_WEBSITE("Company", "\uD83C\uDFE2"),

    PERSONAL_WEBSITE("Website", "\uD83C\uDF10"),

    PROJECT_WEBSITE("Project", "\uD83D\uDEE0\uFE0F");

    private final String displayIcon;

    private final String displayText;

    LinkType(final String displayText, final String displayIcon) {
        this.displayText = displayText;
        this.displayIcon = displayIcon;
    }

    public String getDisplayText() {
        return displayText;
    }

    public String getDisplayIcon() {
        return displayIcon;
    }

}
