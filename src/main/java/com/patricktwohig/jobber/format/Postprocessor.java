package com.patricktwohig.jobber.format;

public interface Postprocessor<T> {

    /**
     * Applies the postprocessing modifications to the objects allowing for fine-grained control over what gets kept
     * and what gets discarded by the AI revisions.
     *
     * @param original the original (unchanged)
     * @param modified the modified version, updated for each pas
     *
     * @return a new copy of the object, with the requested modifications
     */
    T apply(T original, T modified);

    /**
     * Chains this {@link Postprocessor} to the next one.
     * @param next the next one.
     * @return a new {@link Postprocessor} combining both steps
     */
    default Postprocessor<T> chain(final Postprocessor<T> next) {
        return (original, modified) -> {
            final var intermediate = apply(original, modified);
            return next.apply(original, intermediate);
        };
    }


    interface Factory {

        <T> Builder<T> get(Class<T> modelT);

    }

    interface Builder<T> {

        Postprocessor<T> build();

        Builder<T> keep(Iterable<String> properties);

        Builder<T> omit(Iterable<String> properties);

        default T buildAndApply(final T original, final T modified) {
            return build().apply(original, modified);
        }

    }

}
