package com.patricktwohig.jobber.format.poi;

import com.patricktwohig.jobber.model.Link;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STUnderline;

import static java.lang.String.format;

public record DocumentLinkRecord(XWPFDocument document, Link link) {

    public XWPFRun writeAbbreviated(final XWPFParagraph paragraph) {
        final var linkText = link.getLinkType().getDisplayText();
        final var run = paragraph.createHyperlinkRun(link().getUrl());
        run.setText(linkText);
        run.setColor("0000FF");
        run.setUnderline(UnderlinePatterns.SINGLE);
        return run;
    }

    public XWPFRun writeFullUrl(final XWPFParagraph paragraph) {
        final var linkText = format(link.getUrl());
        final var run = paragraph.createHyperlinkRun(link().getUrl());
        run.setText(linkText);
        run.setColor("0000FF");
        run.setUnderline(UnderlinePatterns.SINGLE);
        return run;
    }

}
