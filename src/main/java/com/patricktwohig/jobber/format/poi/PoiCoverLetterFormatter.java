package com.patricktwohig.jobber.format.poi;

import com.patricktwohig.jobber.format.CoverLetterFormatter;
import com.patricktwohig.jobber.model.CoverLetter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

import static org.apache.poi.xwpf.usermodel.ParagraphAlignment.CENTER;

public class PoiCoverLetterFormatter implements CoverLetterFormatter {

    @Override
    public void format(final CoverLetter coverLetter, final OutputStream outputStream) throws IOException {
        try (final var document = new CoverLetterDocument(new XWPFDocument())) {
            document.writeHeadline();
        }
    }

    private record CoverLetterDocument(XWPFDocument document) implements Closeable {
        @Override
        public void close() throws IOException {
            document().close();
        }
    }

}
