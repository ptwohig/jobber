package com.patricktwohig.jobber.format.plain;

import com.patricktwohig.jobber.format.CoverLetterFormatter;
import com.patricktwohig.jobber.model.CoverLetter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class PlainTextCoverLetterFormatter implements CoverLetterFormatter {

    private static final String SALUTATION = "Dear";

    private static final String DEFAULT_CONTACT = "Hiring Manager";

    private static final String CLOSING = "Sincerely";

    @Override
    public void format(final CoverLetter coverLetter, final OutputStream outputStream) throws IOException {

        final var writer = new PrintWriter(outputStream);

        final var utils = new PlainTextUtils(writer)
                .write(coverLetter.getSender())
                .writeSeparator()
                .write(coverLetter.getEmployer())
                .writeSeparator()
                .write(coverLetter.getRecipient());

        writer.printf("%s %s;%n",
                SALUTATION,
                coverLetter.getRecipient() == null || coverLetter.getRecipient().getName() == null
                        ? DEFAULT_CONTACT
                        : coverLetter.getRecipient().getName());

        if (coverLetter.getBodyParagraphs() != null) {
            coverLetter.getBodyParagraphs().forEach(paragraph -> {
                writer.println(paragraph);
                utils.writeSeparator();
            });
        }

        writer.printf("%s,%n", CLOSING);

        if (coverLetter.getSender() != null && coverLetter.getSender().getName() != null) {
            writer.println(coverLetter.getSender().getName());
        }

        writer.flush();

    }

}
