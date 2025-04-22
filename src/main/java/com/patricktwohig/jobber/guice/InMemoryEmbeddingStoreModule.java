package com.patricktwohig.jobber.guice;

import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

public class InMemoryEmbeddingStoreModule extends PrivateModule {

    @Override
    protected void configure() {

        expose(ContentRetriever.class);
        expose(new TypeLiteral<EmbeddingStore<TextSegment>>() {});

        bind(ContentRetriever.class)
                .to(EmbeddingStoreContentRetriever.class)
                .asEagerSingleton();

        bind(new TypeLiteral<EmbeddingStore<TextSegment>>() {})
                .toProvider(InMemoryEmbeddingStore::new)
                .asEagerSingleton();

    }

    @Provides
    public EmbeddingStoreContentRetriever provideContentRetriever(
            final EmbeddingModel embeddingModel,
            final EmbeddingStore<TextSegment> embeddingStore) {
        return new EmbeddingStoreContentRetriever(embeddingStore, embeddingModel);
    }

}
