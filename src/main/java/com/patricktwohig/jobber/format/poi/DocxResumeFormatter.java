package com.patricktwohig.jobber.format.poi;

import com.patricktwohig.jobber.format.ResumeFormatter;
import com.patricktwohig.jobber.model.*;
import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.XWPFAbstractNum;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Stream;

import static com.patricktwohig.jobber.model.PositionType.EMPLOYEE;
import static java.util.function.Predicate.not;
import static org.apache.poi.xwpf.usermodel.ParagraphAlignment.*;
import static org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat.BULLET;
import static org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType.PARAGRAPH;

public class DocxResumeFormatter implements ResumeFormatter {

    private static final String DELIMITER = " | ";

    private static final int TOP_MARGIN = 700;

    private static final int BOTTOM_MARGIN = 700;

    private static final int LEFT_MARGIN = 700;

    private static final int RIGHT_MARGIN = 700;

    private static final int DEFAULT_FONT_SIZE = 10;

    private static final int HEADLINE_SIZE = 16;

    private static final boolean HEADLINE_BOLD = true;

    private static final int PARAGRAPH_SPACING = 120;

    private static final String BULLET_TEXT = "•";

    private static final int BULLET_INDENTATION = 600;

    private static final int BULLET_HANGING_SPACE = BULLET_INDENTATION / 2;

    private static final String SKILLS_HEADLINE = "Core Competencies";

    private static final DateTimeFormatter POSITION_DATE_FORMAT = DateTimeFormatter.ofPattern("MMMM yyyy");

    private static final DateTimeFormatter EDUCATION_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy");

    private static final String NAME_BACKGROUND_COLOR = "D474FC";

    private static final String CONTACT_BACKGROUND_COLOR = "D1BAFF";

    private static final String SKILLS_SUMMARY_BACKGROUND_COLOR = "F5C4FF";

    @Override
    public void format(final Resume resume, final OutputStream outputStream) throws IOException {
        try (final var document = new ResumeDocument(new XWPFDocument(), resume, BigInteger.ZERO)) {
            document.write(outputStream);
        }
    }

    private record ResumeDocument(
            XWPFDocument document,
            Resume resume,
            BigInteger numberingId) implements Closeable {

        private ResumeDocument {
            numberingId = generateNumberingId(document, numberingId);
        }

        private BigInteger generateNumberingId(final XWPFDocument document, final BigInteger baseNumberingId) {

            final var numbering = document.createNumbering();

            final var numberingId = numbering.getAbstractNums().stream()
                        .map(n -> n.getAbstractNum().getAbstractNumId())
                        .max(BigInteger::compareTo)
                        .map(n -> n.add(BigInteger.ONE))
                        .orElse(baseNumberingId);

            final var ctAbstractNum = CTAbstractNum.Factory.newInstance();
            ctAbstractNum.setAbstractNumId(numberingId);

            final var level = ctAbstractNum.addNewLvl();
            level.setIlvl(BigInteger.ZERO);
            level.addNewNumFmt().setVal(BULLET);
            level.addNewLvlText().setVal(BULLET_TEXT);

            final var indent = level.addNewPPr().addNewInd();
            indent.setLeft(BigInteger.valueOf(BULLET_INDENTATION)); // 1 inch (total indent)
            indent.setHanging(BigInteger.valueOf(BULLET_HANGING_SPACE)); // 0.5 inch (space after bullet)

            final var abstractNum = new XWPFAbstractNum(ctAbstractNum, numbering);
            numbering.addAbstractNum(abstractNum);

            return numbering.addNum(numberingId);

        }

        @Override
        public void close() throws IOException {
            document().close();
        }

        public void write(final OutputStream outputStream) throws IOException {
            setDocumentStyle();
            setDocumentMargins();
            writeContactInformation();
            writeHeadline();
            createHorizontalRule();
            writeExperience();
            createHorizontalRule();
            writeEducation();
            document().write(outputStream);
        }

        private void setDocumentStyle() {

            final var styles = document().createStyles();
            final var ctStyles = CTStyles.Factory.newInstance();
            styles.setStyles(ctStyles);

            final var defaultStyle = ctStyles.addNewStyle();
            defaultStyle.setStyleId("Normal");
            defaultStyle.setType(PARAGRAPH);

            final var rPr = defaultStyle.isSetRPr()
                    ? defaultStyle.getRPr()
                    : defaultStyle.addNewRPr();

            final var fontSize = rPr.addNewSz();
            fontSize.setVal(BigInteger.valueOf(DEFAULT_FONT_SIZE * 2));

        }

        private void setDocumentMargins() {

            final var sectPr = document.getDocument().getBody().isSetSectPr()
                    ? document.getDocument().getBody().getSectPr()
                    : document.getDocument().getBody().addNewSectPr();

            final var margins  = sectPr.isSetPgMar()
                    ? sectPr.getPgMar()
                    : sectPr.addNewPgMar();

            margins.setTop(BigInteger.valueOf(TOP_MARGIN));
            margins.setBottom(BigInteger.valueOf(BOTTOM_MARGIN));
            margins.setLeft(BigInteger.valueOf(LEFT_MARGIN));
            margins.setRight(BigInteger.valueOf(RIGHT_MARGIN));

        }

        private XWPFParagraph createStandardParagraph() {
            final var paragraph = document().createParagraph();
            paragraph.setSpacingAfter(PARAGRAPH_SPACING);
            return paragraph;
        }

        private XWPFParagraph createStandardParagraph(final String backgroundColor) {
            final var paragraph = createStandardParagraph();
            final var ppr = paragraph.getCTP().addNewPPr();
            final var shd = ppr.isSetShd() ? ppr.getShd() : ppr.addNewShd();
            shd.setFill(backgroundColor);
            return paragraph;
        }

        private void createHorizontalRule() {
            new HorizontalRule.Builder()
                    .withDocument(document())
                    .withBorder(Borders.SINGLE)
                    .withLeftIndent(450)
                    .withRightIndent(450)
                    .buildAndWrite();
        }

        private void writeContactInformation() {

            final var contact = resume().getContact();
            final var contactName = contact == null  ? null : contact.getName();
            final var contactEmail = contact == null ? null : contact.getEmail();
            final var contactPhone = contact == null ? null : contact.getPhone();
            final var contactLocation = contact == null ? null : contact.getLocation();
            final var contactLinks = contact == null ? null : contact.getLinks();

            final var allLinks = (contactLinks == null ? List.<Link>of() : contactLinks)
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(link -> link.getUrl() != null && link.getLinkType() != null)
                    .toList();

            final var allContactInfo = Stream.of(contactLocation, contactEmail, contactPhone)
                    .filter(Objects::nonNull)
                    .filter(not(String::isBlank))
                    .toList();

            final var nameParagraph = createStandardParagraph(NAME_BACKGROUND_COLOR);
            nameParagraph.setAlignment(CENTER);

            var run = nameParagraph.createRun();
            run.setBold(HEADLINE_BOLD);
            run.setFontSize(HEADLINE_SIZE);
            run.setText(contactName == null ? "<<Your Name>>" : contactName);

            final var contactParagraph = createStandardParagraph(CONTACT_BACKGROUND_COLOR);
            contactParagraph.setAlignment(CENTER);

            new SplitList.Builder()
                    .withDelimiter(() -> {
                        final var delimiter = contactParagraph.createRun();
                        delimiter.setText(DELIMITER);
                        return delimiter;
                    })
                    .withRuns(allContactInfo.stream().map(nfo -> () -> {
                        final var contactInfoRun = contactParagraph.createRun();
                        contactInfoRun.setText(nfo);
                        return contactInfoRun;
                    }))
                    .withRuns(allLinks.stream().map(link -> () -> {
                        final var record = new DocumentLinkRecord(document(), link);
                        return record.writeAbbreviated(contactParagraph);
                    })).build().write();

        }

        private void writeHeadline() {

            final var headline = resume().getHeadline();
            final var headlineTitle = headline == null  ? null : headline.getTitle();
            final var headlineSummary = headline == null  ? null : headline.getSummary();
            final var headlineSkills = headline == null  ? List.<String>of() : headline.getSkills();

            final var titleParagraph = createStandardParagraph();
            titleParagraph.setAlignment(CENTER);

            var run = titleParagraph.createRun();
            run.setBold(HEADLINE_BOLD);
            run.setFontSize(HEADLINE_SIZE);
            run.setText(headlineTitle == null ? "<<Your Title>>" : headlineTitle);

            final var summaryParagraph = createStandardParagraph();
            summaryParagraph.setAlignment(BOTH);
            run = summaryParagraph.createRun();
            run.setText(headlineSummary == null ? "<<Your Summary>>" : headlineSummary);

            final var skillsHeadlineParagraph = createStandardParagraph(SKILLS_SUMMARY_BACKGROUND_COLOR);
            skillsHeadlineParagraph.setAlignment(CENTER);
            run = skillsHeadlineParagraph.createRun();
            run.setBold(HEADLINE_BOLD);
            run.setFontSize(HEADLINE_SIZE);
            run.setText(SKILLS_HEADLINE);

            final var skillsParagraph = createStandardParagraph();
            skillsParagraph.setAlignment(CENTER);

            new SplitList.Builder()
                    .withDelimiter(() -> {
                        final var delimiter = skillsParagraph.createRun();
                        delimiter.setText(DELIMITER);
                        return delimiter;
                    })
                    .withRuns(headlineSkills.stream().map(skill -> () -> {
                        final var skillRun = skillsParagraph.createRun();
                        skillRun.setText(skill);
                        return skillRun;
                    })).build().write();

        }

        private void writeExperience() {

            final var positions = resume().getPositions();
            final var positionsByType = (positions == null ? List.<Position>of() : positions)
                    .stream()
                    .filter(Objects::nonNull)
                    .peek(position -> {
                        if (position.getPositionType() == null) {
                            position.setPositionType(EMPLOYEE);
                        }
                    })
                    .reduce(new TreeMap<PositionType, List<Position>>(), (map, position) -> {
                        map.computeIfAbsent(position.getPositionType(), k -> new ArrayList<>()).add(position);
                        return map;
                    }, (m0, m1) -> m0);

            positionsByType.forEach(this::writeExperience);

        }

        private void writeExperience(
                final PositionType type,
                final List<Position> positions) {

            final var headerParagraph = createStandardParagraph();
            headerParagraph.setAlignment(CENTER);

            final var headerRun = headerParagraph.createRun();
            headerRun.setBold(HEADLINE_BOLD);
            headerRun.setFontSize(HEADLINE_SIZE);
            headerRun.setText(String.format("%s Experience", type.getDisplayText()));

            writePositions(positions);

        }

        private void writePositions(final List<Position> positions) {
            positions.forEach(position -> {

                final var headlineItems = new ArrayList<String>();
                headlineItems.add(position.getCompany() == null ? "<<Company>>" : position.getCompany());

                if (position.getTitle() != null && !position.getTitle().isEmpty())
                    headlineItems.add(position.getTitle());

                if (position.getLocation() != null && !position.getLocation().isBlank())
                    headlineItems.add(position.getLocation());

                final var startDate = position.getStartDate() == null
                        ? "<<Start Date>>"
                        : POSITION_DATE_FORMAT.format(position.getStartDate());

                final var endDate = position.getEndDate() == null
                        ? "Present" :
                        POSITION_DATE_FORMAT.format(position.getEndDate());

                headlineItems.add(String.format("%s to %s", startDate, endDate));

                final var headlineParagraph = createStandardParagraph();
                headlineParagraph.setAlignment(LEFT);

                new SplitList.Builder()
                        .withDelimiter(() -> {
                            final var run = headlineParagraph.createRun();
                            run.setBold(HEADLINE_BOLD);
                            run.setText(DELIMITER);
                            return run;
                        })
                        .withRuns(headlineItems.stream().map(item -> () -> {
                            final var run = headlineParagraph.createRun();
                            run.setText(item);
                            run.setBold(HEADLINE_BOLD);
                            return run;
                        })).build().write();

                final var accomplishments = position.getAccomplishmentStatements();

                (accomplishments == null ? List.<String>of() : accomplishments)
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(not(String::isBlank))
                        .forEach(accomplishment -> {

                            final var accomplishmentParagraph = document().createParagraph();
                            accomplishmentParagraph.setNumID(numberingId());
                            accomplishmentParagraph.setNumILvl(BigInteger.ZERO);

                            final var accomplishmentRun = accomplishmentParagraph.createRun();
                            accomplishmentRun.setText(accomplishment);

                        });

            });
        }

        private void writeEducation() {

            final var headerParagraph = createStandardParagraph();
            headerParagraph.setAlignment(CENTER);

            final var headerRun = headerParagraph.createRun();
            headerRun.setBold(HEADLINE_BOLD);
            headerRun.setFontSize(HEADLINE_SIZE);
            headerRun.setText("Education");

            final var educations = resume().getEducations();
            (educations == null ? List.<Education>of() : educations)
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(education -> {

                        final var educationParagraph = createStandardParagraph();
                        educationParagraph.setNumID(numberingId());
                        educationParagraph.setNumILvl(BigInteger.ZERO);

                        final var graduationDate = education.getGraduationDate() == null
                                ? "<<Graduation Date>>"
                                : EDUCATION_DATE_FORMAT.format(education.getGraduationDate());

                        final var educationRun = educationParagraph.createRun();

                        educationRun.setText(String.format(
                                "%s in %s - %s. %s.",
                                education.getDegreeEarned(),
                                education.getMajor(),
                                graduationDate,
                                education.getSchoolName()
                        ));

                    });

        }

    }

}
