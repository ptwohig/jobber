package com.patricktwohig.jobber.format.poi;

import com.patricktwohig.jobber.model.Link;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRelation;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STUnderline;

import static java.lang.String.format;

public record DocumentLinkRecord(XWPFDocument document, Link link) {

    public XWPFRun writeAbbreviated(final XWPFParagraph paragraph) {

        final var relationship = document().getPackagePart().addExternalRelationship(
                link.getUrl(),
                XWPFRelation.HYPERLINK.getRelation()
        );

        final var hyperlink = paragraph.getCTP().addNewHyperlink();
        hyperlink.setId(relationship.getId());

        final var ctr = hyperlink.addNewR();
        final var ctrText = ctr.addNewT();
        final var linkText = link.getLinkType().getDisplayText();
        ctrText.setStringValue(linkText);

        final var ctrPr = ctr.addNewRPr();
        final var color = ctrPr.addNewColor();
        color.setVal("0000FF");
        ctrPr.addNewU().setVal(STUnderline.SINGLE);

        final var result = new XWPFRun(ctr, paragraph);
        paragraph.getRuns().add(new XWPFRun(ctr, paragraph));
        return result;

    }

    public XWPFRun writeFullUrl(final XWPFParagraph paragraph) {

        final var relationship = document().getPackagePart().addExternalRelationship(
                link.getUrl(),
                XWPFRelation.HYPERLINK.getRelation()
        );

        final var hyperlink = paragraph.getCTP().addNewHyperlink();
        hyperlink.setId(relationship.getId());

        final var ctr = hyperlink.addNewR();
        final var ctrText = ctr.addNewT();
        final var linkText = format("\uD83D\uDD17 - %s", link.getUrl());
        ctrText.setStringValue(linkText);

        final var ctrPr = ctr.addNewRPr();
        final var color = ctrPr.addNewColor();
        color.setVal("0000FF");
        ctrPr.addNewU().setVal(STUnderline.SINGLE);

        final var result = new XWPFRun(ctr, paragraph);
        paragraph.getRuns().add(new XWPFRun(ctr, paragraph));
        return result;

    }

}
