package com.patricktwohig.jobber.model;

import java.util.List;
import java.util.Objects;

public class Contact {

    private String name;

    private String email;

    private String phone;

    private List<Link> links;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Contact{");
        sb.append("name='").append(name).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", links=").append(links);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(getName(), contact.getName()) && Objects.equals(getEmail(), contact.getEmail()) && Objects.equals(getPhone(), contact.getPhone()) && Objects.equals(getLinks(), contact.getLinks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getEmail(), getPhone(), getLinks());
    }
}
