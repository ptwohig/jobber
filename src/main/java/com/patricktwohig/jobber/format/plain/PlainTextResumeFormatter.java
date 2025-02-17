package com.patricktwohig.jobber.format.plain;

import com.patricktwohig.jobber.format.ResumeFormatter;
import com.patricktwohig.jobber.model.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlainTextResumeFormatter implements ResumeFormatter {

    private static final String DELIMITER = " | ";

    @Override
    public void format(final Resume resume, final OutputStream outputStream) throws IOException {

        if (resume == null) {
            return;
        }

        final var writer = new PrintWriter(outputStream);
        final var record = new ResumeDocument(writer);
        record.write(resume);
        writer.flush();

    }

    private record ResumeDocument(PrintWriter writer) {

        public void write(final Resume resume) throws IOException {
            Optional.ofNullable(resume.getContact()).ifPresent(this::writeContact);
            Optional.ofNullable(resume.getHeadline()).ifPresent(this::writeHeadline);
            Optional.ofNullable(resume.getPositions()).ifPresent(this::writePositions);
            Optional.ofNullable(resume.getEducation()).ifPresent(this::writeEducation);
        }

        private void writeContact(final Contact contact) {
            Optional.ofNullable(contact.getName()).ifPresent(writer::println);
            writeLine(DELIMITER, contact::getEmail, contact::getPhone, contact::getLocation);
            Optional.ofNullable(contact.getLinks()).map(links -> links.stream()
                    .filter(Objects::nonNull)
                    .map(Link::getUrl)
                    .filter(Objects::nonNull)
                    .filter(s -> !s.isBlank())
                    .collect(Collectors.joining(DELIMITER)))
                    .ifPresent(writer::println);
            writer.println();
        }

        private void writeHeadline(final Headline headline) {
            Optional.ofNullable(headline.getTitle()).ifPresent(writer::println);
            Optional.ofNullable(headline.getSummary()).ifPresent(writer::println);
            Optional.ofNullable(headline.getSkills()).map(skills -> skills.stream()
                    .filter(Objects::nonNull)
                    .filter(s -> !s.isBlank())
                    .collect(Collectors.joining(DELIMITER)))
                    .ifPresent(writer::println);
            writer.println();
        }

        private void writePositions(final List<Position> positions) {
            positions.stream()
                    .filter(Objects::nonNull)
                    .forEach(this::writePosition);
            writer.println();
        }

        private void writePosition(final Position position) {

            writeLine(DELIMITER,
                    () -> Optional.of(position.getPositionType()).map(PositionType::getDisplayText).orElse(null),
                    position::getTitle,
                    position::getCompany,
                    position::getLocation,
                    () -> String.format("%s to %s",
                            position.getStartDate() == null ? "Unknown" : position.getStartDate(),
                            position.getEndDate() == null ? "Present" : position.getEndDate())
            );

            Optional.ofNullable(position.getAccomplishmentStatements())
                    .ifPresent(statements -> statements.stream()
                            .filter(Objects::nonNull)
                            .filter(s -> !s.isBlank())
                            .forEach(statement -> writer().printf(" - %s%n", statement))
                    );

            writer.println();

        }

        private void writeEducation(final List<Education> educations) {

            educations.stream()
                    .filter(Objects::nonNull)
                    .forEach(education -> writer.format(
                            "%s in %s. %s. %s",
                            education.getDegreeEarned(),
                            education.getMajor(),
                            education.getGraduationDate(),
                            education.getSchoolName()
                    ));

            writer.println();

        }

        private void writeLine(final String delimiter, final Supplier<?>... suppliers) {

            final var line = Stream.of(suppliers)
                    .map(Supplier::get)
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .collect(Collectors.joining(delimiter));

            if (!line.isBlank())
                writer().println(line);

        }

    }

}
