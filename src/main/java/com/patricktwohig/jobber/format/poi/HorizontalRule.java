package com.patricktwohig.jobber.format.poi;


import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.util.function.Supplier;

import static org.apache.poi.xwpf.usermodel.ParagraphAlignment.CENTER;

public interface HorizontalRule extends AdvancedElement {

    class Builder implements AdvancedElement.Builder<HorizontalRule> {

        private double height = 0.05;

        private long leftIndent = 0;

        private long rightIndent = 0;

        private Borders border = Borders.SINGLE;

        private Supplier<XWPFParagraph> paragraphSupplier = () -> {
            throw new IllegalArgumentException("Paragraph not defined.");
        };

        public Builder withHeight(double height) {
            this.height = height;
            return this;
        }

        public Builder withBorder(Borders border) {
            this.border = border;
            return this;
        }

        public Builder withDocument(final XWPFDocument document) {
            paragraphSupplier = document::createParagraph;
            return this;
        }

        public Builder withLeftIndent(final long leftIndent) {
            this.leftIndent = leftIndent;
            return this;
        }

        public Builder withRightIndent(final long rightIndent) {
            this.rightIndent = rightIndent;
            return this;
        }

        public HorizontalRule build() {
            return () -> {

                final var paragraph = paragraphSupplier.get();
                paragraph.setBorderBottom(border);
                paragraph.setAlignment(CENTER);
                paragraph.setSpacingBefore(0);
                paragraph.setSpacingAfter(0);
                paragraph.setSpacingBetween(height);

                final var ppr = paragraph.getCTP().addNewPPr();
                final var ind = ppr.isSetInd() ? ppr.getInd() : ppr.addNewInd();
                ind.setLeft(leftIndent);
                ind.setRight(rightIndent);

            };
        }

    }

}
