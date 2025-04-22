package com.patricktwohig.jobber.cli;

import java.util.HashMap;
import java.util.Map;

public class FormatterSetFactory {

    private Map<Class<?>, FormatterSet<?>> formatterSets = new HashMap<>();

    public <T>
    FormatterSet<T> createFormatterSet(final Class<T> formatterClass) {
        return (FormatterSet<T>) formatterSets.computeIfAbsent(formatterClass, FormatterSet::new);
    }

}
