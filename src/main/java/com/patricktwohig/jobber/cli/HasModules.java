package com.patricktwohig.jobber.cli;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import java.util.function.Supplier;
import java.util.stream.Stream;

@FunctionalInterface
public interface HasModules extends Supplier<Stream<Module>> {

    /**
     * Concatenates this with the next HasModules.
     * @param other the other
     * @return a new {@link HasModules} concatenating them both.
     */
    default HasModules concat(HasModules other) {
        return () -> Stream.concat(get(), other.get());
    }

    /**
     * Loads all modules.
     * @return all modules
     */
    default Module[] loadModules() {
        return get().toArray(Module[]::new);
    }

    /**
     * Gets a new {@link Injector} for this instance.
     *
     * @return new {@link Injector}
     */
    default Injector newInjector() {
        return Guice.createInjector(loadModules());
    }

}
