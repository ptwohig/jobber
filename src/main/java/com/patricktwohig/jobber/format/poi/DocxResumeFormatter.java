package com.patricktwohig.jobber.format.poi;

import com.patricktwohig.jobber.format.ResumeFormatter;
import com.patricktwohig.jobber.model.*;
import org.apache.poi.xwpf.usermodel.XWPFAbstractNum;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff1;
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
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static com.patricktwohig.jobber.model.PositionType.EMPLOYEE;
import static java.util.function.Predicate.not;
import static org.apache.poi.xwpf.usermodel.ParagraphAlignment.CENTER;
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

    private static final int BULLET_INDENTATION = 720;

    private static final String SKILLS_HEADLINE = "Core Competencies";

    private static final DateTimeFormatter POSITION_DATE_FORMAT = DateTimeFormatter.ofPattern("MMMM yyyy");

    private static final DateTimeFormatter EDUCATION_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy");

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
            level.addNewPPr().addNewInd().setLeft(BigInteger.valueOf(BULLET_INDENTATION));

            final var abstractNum = new XWPFAbstractNum(ctAbstractNum, numbering);
            numbering.addAbstractNum(abstractNum);

            return numberingId;

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
            writeExperience();
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

        private XWPFParagraph createNotSplittingParagraph() {
            final var paragraph = document().createParagraph();
            final var ctp = paragraph.getCTP();
            final var ppr = ctp.isSetPPr() ? ctp.getPPr() : ctp.addNewPPr();
            final var keepLines = (ppr.isSetKeepLines()) ? ppr.getKeepLines() : ppr.addNewKeepLines();
            keepLines.setVal(STOnOff1.ON);
            paragraph.setSpacingAfter(PARAGRAPH_SPACING);
            return paragraph;
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

            final var nameParagraph = createNotSplittingParagraph();
            nameParagraph.setAlignment(CENTER);

            var run = nameParagraph.createRun();
            run.setBold(HEADLINE_BOLD);
            run.setFontSize(HEADLINE_SIZE);
            run.setText(contactName == null ? "<<Your Name>>" : contactName);
            run.addBreak();

            final var contactParagraph = writeSplitList(allContactInfo, (nfo, p) -> {
                final var contactInfoRun = p.createRun();
                contactInfoRun.setText(nfo);
                return contactInfoRun;
            });

            contactParagraph.setAlignment(CENTER);

            final var linkParagraph = writeSplitList(allLinks, (link, p) -> {
                final var record = new DocumentLinkRecord(document(), link);
                return record.writeAbbreviated(p);
            });

            linkParagraph.setAlignment(CENTER);

        }

        private void writeHeadline() {

            final var headline = resume().getHeadline();
            final var headlineTitle = headline == null  ? null : headline.getTitle();
            final var headlineSummary = headline == null  ? null : headline.getSummary();
            final var headlineSkills = headline == null  ? null : headline.getSkills();

            final var titleParagraph = createNotSplittingParagraph();
            titleParagraph.setAlignment(CENTER);

            var run = titleParagraph.createRun();
            run.setBold(HEADLINE_BOLD);
            run.setFontSize(HEADLINE_SIZE);
            run.setText(headlineTitle == null ? "<<Your Title>>" : headlineTitle);
            run.addBreak();

            final var summaryParagraph = createNotSplittingParagraph();
            run = summaryParagraph.createRun();
            run.setText(headlineSummary == null ? "<<Your Summary>>" : headlineSummary);

            if (headlineSkills != null && !headlineSkills.isEmpty()) {

                final var skillsHeadlineParagraph = createNotSplittingParagraph();
                skillsHeadlineParagraph.setAlignment(CENTER);
                run.setBold(HEADLINE_BOLD);
                run.setFontSize(HEADLINE_SIZE);
                run.setText(SKILLS_HEADLINE);

                final var skillsListParagraph = writeSplitList(headlineSkills, (skill, p) -> {
                    final var skillRun = p.createRun();
                    skillRun.setText(skill);
                    return skillRun;
                });

            }

        }

        private <T> XWPFParagraph writeSplitList(
                final Iterable<T> listContents,
                final BiFunction<T, XWPFParagraph, XWPFRun> formatterFunction) {

            final var iterator = listContents.iterator();
            final var paragraph = createNotSplittingParagraph();

            while (iterator.hasNext()) {

                final var item = iterator.next();
                formatterFunction.apply(item, paragraph);

                if (iterator.hasNext()) {
                    final var delimiter = paragraph.createRun();
                    delimiter.setText(DELIMITER);
                }

            }

            paragraph.createRun().addBreak();
            return paragraph;

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

            final var headerParagraph = createNotSplittingParagraph();
            final var headerRun = headerParagraph.createRun();
            headerRun.setBold(HEADLINE_BOLD);
            headerRun.setFontSize(HEADLINE_SIZE);
            headerRun.setText(String.format("%s Experience", type.getDisplayText()));
            headerRun.addBreak();

            writePositions(positions);

        }

        private void writePositions(final List<Position> positions) {
            positions.forEach(position -> {

                final var positionHeader = createNotSplittingParagraph();
                var run = positionHeader.createRun();

                run.setBold(HEADLINE_BOLD);
                run.setText(String.format("%s%s%s%s%s",
                        position.getCompany(),
                        DELIMITER,
                        position.getTitle(),
                        DELIMITER,
                        position.getLocation()));
                run.addBreak();

                final var startDate = position.getStartDate() == null
                        ? "<<Start Date>>"
                        : POSITION_DATE_FORMAT.format(position.getStartDate());

                final var endDate = position.getEndDate() == null
                        ? "Present" :
                        POSITION_DATE_FORMAT.format(position.getEndDate());

                run = positionHeader.createRun();
                run.setText(String.format("%s to %s", startDate, endDate));
                run.addBreak();

                final var accomplishments = position.getAccomplishmentStatements();

                (accomplishments == null ? List.<String>of() : accomplishments)
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(not(String::isBlank))
                        .forEach(accomplishment -> {

                            final var accomplishmentParagraph = createNotSplittingParagraph();
                            accomplishmentParagraph.setNumID(numberingId());
                            accomplishmentParagraph.setNumILvl(BigInteger.ZERO);

                            final var accomplishmentRun = accomplishmentParagraph.createRun();
                            accomplishmentRun.setText(accomplishment);

                        });

            });
        }

        private void writeEducation() {

            final var headerParagraph = createNotSplittingParagraph();
            headerParagraph.setAlignment(CENTER);

            final var headerRun = headerParagraph.createRun();
            headerRun.setBold(HEADLINE_BOLD);
            headerRun.setFontSize(HEADLINE_SIZE);
            headerRun.setText("Education");
            headerRun.addBreak();

            final var educations = resume().getEducations();
            (educations == null ? List.<Education>of() : educations)
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(education -> {

                        final var educationParagraph = createNotSplittingParagraph();
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
