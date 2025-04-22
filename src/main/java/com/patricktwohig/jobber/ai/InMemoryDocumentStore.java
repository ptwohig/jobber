package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.format.GenericFormatter;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import static dev.langchain4j.model.output.FinishReason.STOP;

public class InMemoryDocumentStore implements DocumentStore {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryDocumentStore.class);

    private EmbeddingModel embeddingModel;

    private DocumentSplitter documentSplitter;

    private GenericFormatter genericFormatter;

    private EmbeddingStore<TextSegment> embeddingStore;

    private Map<Object, List<String>> documents = new IdentityHashMap<>();

    @Override
    public void remove(final Object object) {
        documents.computeIfPresent(object, (o, ids) -> {
            getEmbeddingStore().removeAll(ids);
            return null;
        });
    }

    @Override
    public void upsertDocument(final Object object, final String name) {
        documents.compute(object, (o, ids) -> {

            if (ids == null) {
                ids = new ArrayList<>();
            } else {
                getEmbeddingStore().removeAll(ids);
                ids.clear();
            }

            final var text = String.format("%s%n%s",
                    name,
                    getGenericFormatter().format(object)
            );

            final var document = Document.document(text, Metadata.from(NAME, name));
            final var segments = getDocumentSplitter().split(document);

            for (final var segment : segments) {

                final var result = getEmbeddingModel().embed(segment);

                if (result.finishReason() != null && !STOP.equals(result.finishReason())) {
                    logger.warn("Failed to embed document: {}", result.finishReason());
                    continue;
                }

                if (result.content() == null) {
                    logger.warn("Failed to embed document: (no content)");
                    continue;
                }

                final var id = getEmbeddingStore().add(result.content(), segment);
                ids.add(id);

            }

            return ids;

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
