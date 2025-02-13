package com.patricktwohig.jobber.format.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.patricktwohig.jobber.format.PostprocessingException;
import com.patricktwohig.jobber.format.Postprocessor;

import java.util.HashSet;
import java.util.Set;

public class JacksonPostprocessorBuilder<T> implements Postprocessor.Builder<T> {

    private Postprocessor<T> postprocessor;

    private final Class<T> clazz;

    private final ObjectMapper objectMapper;

    private final Set<String> properties = new HashSet<>();

    public JacksonPostprocessorBuilder(final Class<T> clazz, final ObjectMapper objectMapper) {
        this.clazz = clazz;
        this.objectMapper = objectMapper;
        this.postprocessor = (original, modified) -> {
            try {
                final var string = objectMapper.writeValueAsString(modified);
                return objectMapper.readValue(string, clazz);
            } catch (JsonProcessingException e) {
                throw new PostprocessingException(e);
            }
        };
    }

    private void check(final String property) {
        if (!this.properties.add(property)) {
            throw new IllegalArgumentException("Duplicate property: " + property);
        }
    }

    @Override
    public Postprocessor<T> build() {
        return postprocessor;
    }

    @Override
    public Postprocessor.Builder<T> keep(final Iterable<String> properties) {

        for (var property : properties) {

            check(property);

            postprocessor = postprocessor.chain((original, modified) -> {

                final var sourceNode = objectMapper.valueToTree(original);
                final var targetNode = (ObjectNode) objectMapper.valueToTree(modified);

                if (sourceNode.has(property)) {
                    targetNode.set(property, sourceNode.get(property));
                }

                try {
                    return objectMapper.treeToValue(targetNode, clazz);
                } catch (JsonProcessingException ex) {
                    throw new PostprocessingException(ex);
                }

            });

        }

        return this;

    }

    @Override
    public Postprocessor.Builder<T> omit(final Iterable<String> properties) {

        for (String property : properties) {

            check(property);

            postprocessor = postprocessor.chain((original, modified) -> {

                final var targetNode = (ObjectNode) objectMapper.valueToTree(modified);
                targetNode.set(property, null);

                try {
                    return objectMapper.treeToValue(targetNode, clazz);
                } catch (JsonProcessingException ex) {
                    throw new PostprocessingException(ex);
                }

            });

        }

        return this;

    }

}
