package com.patricktwohig.jobber.guice;

import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.patricktwohig.jobber.ai.DocumentStore;
import com.patricktwohig.jobber.ai.InMemoryDocumentStore;
import com.patricktwohig.jobber.config.Configuration;
import com.patricktwohig.jobber.format.GenericFormatter;
import com.patricktwohig.jobber.format.plain.PlainTextGenericFormatter;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentByCharacterSplitter;
import dev.langchain4j.data.document.splitter.DocumentByLineSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.Tokenizer;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import java.util.concurrent.ForkJoinPool;

import static com.patricktwohig.jobber.config.Configuration.EMBEDDING_MAX_SEGMENT_SIZE_CHARS;
import static com.patricktwohig.jobber.config.Configuration.EMBEDDING_PARALLEL_THREADS;

public class InMemoryDocumentStoreModule extends PrivateModule {

    @Override
    protected void configure() {

        expose(DocumentStore.class);
        expose(ContentRetriever.class);

        bind(DocumentStore.class)
                .to(InMemoryDocumentStore.class)
                .asEagerSingleton();

        bind(GenericFormatter.class)
                .to(PlainTextGenericFormatter.class);

        expose(new TypeLiteral<EmbeddingStore<TextSegment>>() {});

        bind(ContentRetriever.class)
                .to(EmbeddingStoreContentRetriever.class)
                .asEagerSingleton();

        bind(new TypeLiteral<EmbeddingStore<TextSegment>>() {})
                .toProvider(InMemoryEmbeddingStore::new)
                .asEagerSingleton();

    }

    @Provides
    public DocumentSplitter provideDocumentByLineSplitter(
            final Tokenizer tokenizer,
            @Named(EMBEDDING_MAX_SEGMENT_SIZE_CHARS)
            final int maxSegmentSizeChars,
            @Named(Configuration.EMBEDDING_MAX_SEGMENT_OVERLAP_CHARS)
            final int maxSegmentOverlapChars) {
        return new DocumentByCharacterSplitter(maxSegmentSizeChars, maxSegmentOverlapChars, tokenizer);
    }

    @Provides
    @Singleton
    public ForkJoinPool provideForkJoinPool(
            @Named(EMBEDDING_PARALLEL_THREADS)
            int parallelThreads) {
        return new ForkJoinPool(parallelThreads);
    }

    @Provides
    public EmbeddingStoreContentRetriever provideContentRetriever(
            final EmbeddingModel embeddingModel,
            final EmbeddingStore<TextSegment> embeddingStore) {
        return EmbeddingStoreContentRetriever
                .builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .build();
    }

}
