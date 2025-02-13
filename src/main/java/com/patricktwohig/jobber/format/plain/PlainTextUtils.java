package com.patricktwohig.jobber.format.plain;

import com.patricktwohig.jobber.model.Contact;
import com.patricktwohig.jobber.model.Employer;

import java.io.PrintWriter;

public class PlainTextUtils {

    private final PrintWriter writer;

    public PlainTextUtils(PrintWriter writer) {
        this.writer = writer;
    }

    public PlainTextUtils writeSeparator() {
        writer.println();
        return this;
    }

    public PlainTextUtils write(final Contact contact) {

        if (contact == null) {
            return this;
        }

        if (contact.getName() != null) {
            writer.println(contact.getName());
        }

        if (contact.getEmail() != null) {
            writer.println(contact.getEmail());
        }

        if (contact.getPhone() != null) {
            writer.println(contact.getPhone());
        }

        if (contact.getLocation() != null) {
            writer.println(contact.getLocation());
        }

        if (contact.getLinks() != null) {
            contact.getLinks().forEach(link -> writer.printf("%s - %s%n", link.getLinkType().getDisplayText(), link.getUrl()));
        }

        return this;
    }

    public PlainTextUtils write(Employer employer) {

        if (employer == null) {
            return this;
        }

        if (employer.getCompanyName() != null) {
            writer.println(employer.getCompanyName());
        }

        if (employer.getWebsite() != null) {
            writer.println(employer.getWebsite());
        }

        if (employer.getAddressLines() != null) {
            employer.getAddressLines().forEach(writer::println);
        }

        return this;
    }

}
