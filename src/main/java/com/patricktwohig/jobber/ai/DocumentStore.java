package com.patricktwohig.jobber.ai;

public interface DocumentStore {

    String NAME = "name";

    void remove(Object object);

    default void upsertDocument(Object object) {
        final var name = object.getClass().getSimpleName();
        upsertDocument(object, name);
    }

    void upsertDocument(Object object, String name);

}
