package com.patricktwohig.jobber.model;

import java.util.List;
import java.util.Objects;

public class Employer {

    private String company;

    private String websiteUrl;

    private List<String> addressLines;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public List<String> getAddressLines() {
        return addressLines;
    }

    public void setAddressLines(List<String> addressLines) {
        this.addressLines = addressLines;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Employer{");
        sb.append("company='").append(company).append('\'');
        sb.append(", websiteUrl='").append(websiteUrl).append('\'');
        sb.append(", addressLines=").append(addressLines);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employer employer = (Employer) o;
        return Objects.equals(getCompany(), employer.getCompany()) && Objects.equals(getWebsiteUrl(), employer.getWebsiteUrl()) && Objects.equals(getAddressLines(), employer.getAddressLines());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCompany(), getWebsiteUrl(), getAddressLines());
    }

}
