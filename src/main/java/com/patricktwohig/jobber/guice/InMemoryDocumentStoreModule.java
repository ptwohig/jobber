package com.patricktwohig.jobber.guice;

import com.google.inject.PrivateModule;
import com.patricktwohig.jobber.ai.DocumentStore;
import com.patricktwohig.jobber.ai.InMemoryDocumentStore;
import com.patricktwohig.jobber.format.GenericFormatter;
import com.patricktwohig.jobber.format.plain.PlainTextGenericFormatter;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentByLineSplitter;

public class InMemoryDocumentStoreModule extends PrivateModule {

    @Override
    protected void configure() {

        expose(DocumentStore.class);

        bind(DocumentStore.class)
                .to(InMemoryDocumentStore.class)
                .asEagerSingleton();

        bind(GenericFormatter.class)
                .to(PlainTextGenericFormatter.class);

        bind(DocumentSplitter.class)
                .toProvider(() -> new DocumentByLineSplitter(100, 20));

    }

}
