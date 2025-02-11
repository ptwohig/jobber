package com.patricktwohig.jobber.guice;

import com.google.inject.Key;
import com.google.inject.PrivateModule;

import java.util.ArrayList;
import java.util.List;

import static com.google.inject.name.Names.named;

public class ConfigurationModule extends PrivateModule {

    private final List<ConfigurationRecord> configurationRecordList = new ArrayList<>();

    @Override
    protected void configure() {
        configurationRecordList.forEach(r -> expose(r.key()));
        configurationRecordList.forEach(r -> bind(r.key()).toInstance(r.value()));
    }

    public ConfigurationModule add(final String key, final String value) {
        final var guiceKey = Key.get(String.class, named(key));
        final var configurationRecord = new ConfigurationRecord(guiceKey, value);
        configurationRecordList.add(configurationRecord);
        return this;
    }

    private record ConfigurationRecord(Key<String> key, String value){}

}
