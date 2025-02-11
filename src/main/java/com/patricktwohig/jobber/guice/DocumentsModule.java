package com.patricktwohig.jobber.guice;

import com.google.inject.PrivateModule;
import com.patricktwohig.jobber.model.DocumentRecord;

import java.util.ArrayList;
import java.util.List;

public class DocumentsModule extends PrivateModule {

    private List<DocumentRecord> documents = new ArrayList<>();

    @Override
    protected void configure() {

    }

    public DocumentsModule add(final DocumentRecord document) {
        documents.add(document);
        return this;
    }

}
