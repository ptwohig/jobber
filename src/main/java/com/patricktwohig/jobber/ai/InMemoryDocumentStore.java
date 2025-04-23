package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.format.GenericFormatter;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;

import static dev.langchain4j.model.output.FinishReason.STOP;

public class InMemoryDocumentStore implements DocumentStore {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryDocumentStore.class);

    private ForkJoinPool forkJoinPool;

    private EmbeddingModel embeddingModel;

    private DocumentSplitter documentSplitter;

    private GenericFormatter genericFormatter;

    private EmbeddingStore<TextSegment> embeddingStore;

    private Map<UUID, List<String>> documents = new ConcurrentHashMap<>();

    @Override
    public void remove(final Object object) {
        documents.computeIfPresent((UUID) object, (o, ids) -> {
            getEmbeddingStore().removeAll(ids);
            return null;
        });
    }

    @Override
    public Object upsertDocument(final Object object, final String name) {
        final var uuid = UUID.randomUUID();

        documents.compute(uuid, (o, ids) -> {

            if (ids != null) {
                getEmbeddingStore().removeAll(ids);
            }

            final var text = String.format("%s%n%s",
                    name,
                    getGenericFormatter().format(object)
            );

            final var document = Document.document(text, Metadata.from(NAME, name));

            return getForkJoinPool().submit(() -> getDocumentSplitter().split(document)
                    .stream()
                    .parallel()
                    .map(segment -> {

                        final var result = getEmbeddingModel().embed(segment);

                        if (result.finishReason() != null && !STOP.equals(result.finishReason())) {
                            logger.warn("Failed to embed document: {}", result.finishReason());
                            return EmbeddedSegment.INVALID;
                        }

                        if (result.content() == null) {
                            logger.warn("Failed to embed document: (no content)");
                            return EmbeddedSegment.INVALID;
                        }

                        return new EmbeddedSegment(result.content(), segment);

                    })
                    .filter(EmbeddedSegment::isValid)
                    .map(es -> getEmbeddingStore().add(es.embedding(), es.segment()))
                    .toList())
                    .join();

        });

        return uuid;

    }

    public ForkJoinPool getForkJoinPool() {
        return forkJoinPool;
    }

    @Inject
    public void setForkJoinPool(ForkJoinPool forkJoinPool) {
        this.forkJoinPool = forkJoinPool;
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

    record EmbeddedSegment(Embedding embedding, TextSegment segment) {

        static final EmbeddedSegment INVALID = new EmbeddedSegment(null, null);

        public boolean isValid() {
            return embedding != null && segment != null;
        }

    }

}
