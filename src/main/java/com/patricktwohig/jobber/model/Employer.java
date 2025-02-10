package com.patricktwohig.jobber.model;

import java.util.List;
import java.util.Objects;

public class Employer {

    private String companyName;

    private Link website;

    private List<String> addressLines;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Link getWebsite() {
        return website;
    }

    public void setWebsite(Link website) {
        this.website = website;
    }

    public List<String> getAddressLines() {
        return addressLines;
    }

    public void setAddressLines(List<String> addressLines) {
        this.addressLines = addressLines;
    }

    @Override
    public String toString() {
        return "Employer{" +
                "companyName='" + companyName + '\'' +
                ", website=" + website +
                ", addressLines=" + addressLines +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employer employer = (Employer) o;
        return Objects.equals(companyName, employer.companyName) && Objects.equals(website, employer.website) && Objects.equals(addressLines, employer.addressLines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyName, website, addressLines);
    }

}
