package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.format.GenericFormatter;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.inject.Inject;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class InMemoryDocumentStore implements DocumentStore {

    private EmbeddingModel embeddingModel;

    private DocumentSplitter documentSplitter;

    private GenericFormatter genericFormatter;

    private EmbeddingStore<TextSegment> embeddingStore;

    private Map<Object, List<String>> documents = new IdentityHashMap<>();

    @Override
    public void remove(final Object object) {
        documents.compute(object, (o, existing) -> {

            if (existing != null) {
                existing.forEach(getEmbeddingStore()::remove);
            }

            return null;

        });
    }

    @Override
    public void upsertDocument(final Object object, final String name) {
        documents.compute(object, (o, existing) -> {

            if (existing != null) {
                existing.forEach(getEmbeddingStore()::remove);
            }

            final var text = String.format("%s%n%s",
                    name,
                    getGenericFormatter().format(object)
            );

            final var document = Document.document(text, Metadata.from(NAME, name));
            final var segments = getDocumentSplitter().split(document);

            return segments.stream()
                    .map(getEmbeddingModel()::embed)
                    .map(Response::content)
                    .map(getEmbeddingStore()::add)
                    .toList();

        });
    }

    public EmbeddingModel getEmbeddingModel() {
        return embeddingModel;
    }

    @Inject
    public void setEmbeddingModel(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    public DocumentSplitter getDocumentSplitter() {
        return documentSplitter;
    }

    @Inject
    public void setDocumentSplitter(DocumentSplitter documentSplitter) {
        this.documentSplitter = documentSplitter;
    }

    public EmbeddingStore<TextSegment> getEmbeddingStore() {
        return embeddingStore;
    }

    @Inject
    public void setEmbeddingStore(EmbeddingStore<TextSegment> embeddingStore) {
        this.embeddingStore = embeddingStore;
    }

    public GenericFormatter getGenericFormatter() {
        return genericFormatter;
    }

    @Inject
    public void setGenericFormatter(GenericFormatter genericFormatter) {
        this.genericFormatter = genericFormatter;
    }

}
