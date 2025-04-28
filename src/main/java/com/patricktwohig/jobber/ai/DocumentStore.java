package com.patricktwohig.jobber.ai;

import java.util.Map;
import java.util.TreeMap;

public interface DocumentStore {

    String NAME = "name";

    void remove(Object object);

    default Object upsert(Object object, String ... metadata) {

        if (metadata.length % 2 != 0) {
            throw new IllegalArgumentException("Metadata must be in key-value pairs");
        } else if (metadata.length == 0) {
            return upsert(object, Map.of());
        }

        final var map = new TreeMap<String, String>();

        for (int i = 0; i < metadata.length; i += 2) {

            if (metadata[i] == null || metadata[i + 1] == null) {
                throw new IllegalArgumentException("Metadata keys and values must not be null");
            }
            map.put(metadata[i], metadata[i + 1]);

        }

        return upsert(object, map);

    }

    Object upsert(Object object, Map<String, ?> metadata);

}
