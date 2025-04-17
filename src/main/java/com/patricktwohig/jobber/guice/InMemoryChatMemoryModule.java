package com.patricktwohig.jobber.guice;

import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.Tokenizer;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import jakarta.inject.Named;

import static com.patricktwohig.jobber.config.Configuration.MESSAGE_MEMORY_COUNT;

public class InMemoryChatMemoryModule extends PrivateModule {

    @Override
    protected void configure() {
        expose(ChatMemory.class);
        bind(ChatMemory.class).to(MessageWindowChatMemory.class).asEagerSingleton();
    }

    @Provides
    public MessageWindowChatMemory chatMemoryStore(
            Tokenizer tokenizer,
            @Named(MESSAGE_MEMORY_COUNT) int tokenMemoryCount) {
        final var store = new InMemoryChatMemoryStore();
        return MessageWindowChatMemory
                .builder()
                .maxMessages(15)
                .chatMemoryStore(store)
                .build();
    }

}
