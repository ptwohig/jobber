package com.patricktwohig.jobber.format.poi;

import com.patricktwohig.jobber.format.CoverLetterFormatter;
import com.patricktwohig.jobber.model.CoverLetter;
import com.patricktwohig.jobber.model.Link;
import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

import static java.util.function.Predicate.not;
import static org.apache.poi.xwpf.usermodel.ParagraphAlignment.CENTER;

public class DocxCoverLetterFormatter implements CoverLetterFormatter {

    private static final String DELIMITER = " | ";

    private static final boolean HEADLINE_BOLD = true;

    private static final int HEADLINE_FONT_SIZE = 14;

    private static final int PARAGRAPH_SPACING = 200;

    private static final int HORIZONTAL_RULE_LEFT = 800;

    private static final int HORIZONTAL_RULE_RIGHT = 800;

    @Override
    public void format(final CoverLetter coverLetter, final OutputStream outputStream) throws IOException {
        try (final var document = new CoverLetterDocument(new XWPFDocument(), coverLetter)) {
            document.write(outputStream);
        }
    }

    private record CoverLetterDocument(XWPFDocument document, CoverLetter coverLetter) implements Closeable {

        @Override
        public void close() throws IOException {
            document().close();
        }

        public void write(final OutputStream outputStream) throws IOException {

            if (coverLetter().getSender() != null) {
                writePersonalHeadline();
            }

            if (coverLetter().getEmployer() != null) {
                writeCompanyHeader();
            }

            writeSalutation();
            writeParagraphs();
            writeSignature();
            document().write(outputStream);

        }

        private XWPFParagraph createParagraph() {
            final var paragraph = document().createParagraph();
            paragraph.setSpacingAfter(PARAGRAPH_SPACING);
            return paragraph;
        }

        private void createHorizontalRule() {
            new HorizontalRule.Builder()
                    .withDocument(document())
                    .withBorder(Borders.SINGLE)
                    .withLeftIndent(HORIZONTAL_RULE_LEFT)
                    .withRightIndent(HORIZONTAL_RULE_RIGHT)
                    .buildAndWrite();
        }


        private void writePersonalHeadline() {

            final var sender = coverLetter.getSender();
            final var senderName = sender == null  ? null : sender.getName();
            final var senderEmail = sender == null ? null : sender.getEmail();
            final var senderPhone = sender == null ? null : sender.getPhone();
            final var senderLinks = sender == null ? null : sender.getLinks();

            final var headline = createParagraph();
            headline.setAlignment(CENTER);

            var run = headline.createRun();
            run.setBold(HEADLINE_BOLD);
            run.setFontSize(HEADLINE_FONT_SIZE);
            run.setText(senderName == null ? "<<Your Name>>" : senderName);
            run.addBreak();

            run = headline.createRun();
            run.setText(senderEmail == null ? "<<Your Email>>" : senderEmail);
            run.addBreak();

            run = headline.createRun();
            run.setText(senderPhone == null ? "<<Your Phone>>" : senderPhone);
            run.addBreak();

            new SplitList.Builder()
                    .withDelimiter(() -> {
                        final var delimiter = headline.createRun();
                        delimiter.setText(DELIMITER);
                        return delimiter;
                    })
                    .withRuns((senderLinks == null ? List.<Link>of() : senderLinks).stream().map(link -> () -> {
                        final var record = new DocumentLinkRecord(document(), link);
                        return record.writeFullUrl(headline);
                    })).build().write();

        }

        private void writeCompanyHeader() {

            final var headline = createParagraph();
            headline.setAlignment(CENTER);

            final var employer = coverLetter.getEmployer();
            final var employerName = employer == null ? null : employer.getCompanyName();

            var run = headline.createRun();
            run.setBold(HEADLINE_BOLD);
            run.setFontSize(HEADLINE_FONT_SIZE);
            run.setText(employerName == null ? "<<Employer Name>>" : employerName);
            run.addBreak();

            (employer == null ? List.<String>of() : employer.getAddressLines())
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(not(String::isBlank))
                    .forEach(line -> {
                        final var addressLineRun = headline.createRun();
                        addressLineRun.setText(line);
                        addressLineRun.addBreak();
                    });

        }

        private void writeSalutation() {

            final var recipient = coverLetter.getRecipient();
            final var recipientName = recipient == null ? null : recipient.getName();

            final var salutation = document().createParagraph();
            salutation.setSpacingAfter(PARAGRAPH_SPACING);

            final var run = salutation.createRun();
            run.setText(String.format("Dear %s;", recipientName == null ? "Hiring Manager;" : recipientName));

        }

        private void writeParagraphs() {

            final var bodyParagraphs = coverLetter().getBodyParagraphs();

            if (bodyParagraphs == null) {
                return;
            }

            bodyParagraphs.stream()
                    .filter(Objects::nonNull)
                    .filter(not(String::isBlank))
                    .forEach(bodyParagraph -> {
                        final var paragraph = createParagraph();
                        final var paragraphText = paragraph.createRun();
                        paragraphText.setText(bodyParagraph);
                    });

        }

        private void writeSignature() {

            final var sender = coverLetter.getSender();
            final var senderName = sender == null  ? null : sender.getName();

            final var signature = document().createParagraph();
            signature.setSpacingAfter(PARAGRAPH_SPACING);

            var run = signature.createRun();
            run.setText("Sincerely,");
            run.addBreak();

            run = signature.createRun();
            run.setText(String.format("%s.", senderName == null ? "<<Your Name>>" : senderName));
            run.addBreak();

        }

    }

}
