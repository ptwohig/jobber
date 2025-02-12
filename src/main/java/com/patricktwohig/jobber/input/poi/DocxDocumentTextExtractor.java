package com.patricktwohig.jobber.input.poi;

import com.patricktwohig.jobber.input.DocumentTextExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;
import java.io.InputStream;

public class DocxDocumentTextExtractor implements DocumentTextExtractor {
    @Override
    public String read(InputStream is) throws IOException {
        try (var doc = new XWPFDocument(is);
             var extractor = new XWPFWordExtractor(doc)) {
            return extractor.getText();
        }
    }
}
