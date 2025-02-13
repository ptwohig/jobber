package com.patricktwohig.jobber.format.poi;

import com.patricktwohig.jobber.format.JobDescriptionAnalysisFormatter;
import com.patricktwohig.jobber.model.JobDescriptionAnalysis;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

public class DocxJobDescriptionAnalysisFormatter implements JobDescriptionAnalysisFormatter {

    @Override
    public void format(
            final JobDescriptionAnalysis jobDescriptionAnalysis,
            final OutputStream outputStream) throws IOException {
        try (final var document = new JobDescriptionAnalysisDocument(new XWPFDocument(), jobDescriptionAnalysis)) {
            document.write(outputStream);
        }
    }

    private record JobDescriptionAnalysisDocument(
            XWPFDocument document,
            JobDescriptionAnalysis analysis) implements Closeable {

        @Override
        public void close() throws IOException {
            document().close();
        }

        public void write(final OutputStream outputStream) {
            throw new UnsupportedOperationException("Not implemented");
        }

    }

}
