package com.patricktwohig.jobber.model;

import java.util.Objects;

public class GeneralFeedback {

    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GeneralFeedback that = (GeneralFeedback) o;
        return Objects.equals(response, that.response);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(response);
    }

    @Override
    public String toString() {
        return "GeneralFeedback{" +
                "response='" + response + '\'' +
                '}';
    }

}
