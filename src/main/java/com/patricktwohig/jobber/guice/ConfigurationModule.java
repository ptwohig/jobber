package com.patricktwohig.jobber.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;
import com.patricktwohig.jobber.config.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static com.google.inject.name.Names.named;

public class ConfigurationModule extends AbstractModule {

    private final Map<String, ConfigurationRecord<?>> configurationRecordList = new LinkedHashMap<>();

    @Override
    protected void configure() {

        final Matcher<TypeLiteral<?>> matcher = tl -> {
            try {
                final var rawType = tl.getRawType();
                final var method = rawType.getMethod("valueOf", String.class);
                return Modifier.isStatic(method.getModifiers()) && Modifier.isPublic(method.getModifiers());
            } catch (NoSuchMethodException ex) {
                return false;
            }
        };

        binder().convertToTypes(matcher, (fromValue, toType) -> {
            try {
                final var rawType = toType.getRawType();
                final var method = rawType.getMethod("valueOf", String.class);
                return method.invoke(null, fromValue);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                binder().addError(e);
                return null;
            }
        });

        configurationRecordList
                .values()
                .forEach(r -> bind(r.key()).toInstance(r.value()));

    }

    public ConfigurationModule add(final Configuration configuration) {
        configuration.stream().forEach(c -> add(c.key(), c.value()));
        return this;
    }

    public ConfigurationModule add(final String key, final Optional<String> value) {
        return value.map(v -> add(key, v)).orElse(this);
    }

    public <T> ConfigurationModule add(final String key, final String value) {
        final var guiceKey = Key.get(String.class, named(key));
        final var configurationRecord = new ConfigurationRecord<T>(guiceKey,value);
        configurationRecordList.put(key, configurationRecord);
        return this;
    }

    private record ConfigurationRecord<T>(Key<String> key, String value) {}

}
