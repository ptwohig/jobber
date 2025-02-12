package com.patricktwohig.jobber.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.PrivateModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static com.google.inject.name.Names.named;

public class ConfigurationModule extends AbstractModule {

    private final List<ConfigurationRecord<?>> configurationRecordList = new ArrayList<>();

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

        configurationRecordList.forEach(r -> bind(r.key()).toInstance(r.value()));

    }

    public <T>ConfigurationModule add(final String key, final String value) {
        final var guiceKey = Key.get(String.class, named(key));
        final var configurationRecord = new ConfigurationRecord<T>(guiceKey,value);
        configurationRecordList.add(configurationRecord);
        return this;
    }

    private record ConfigurationRecord<T>(Key<String> key, String value) {}

}
