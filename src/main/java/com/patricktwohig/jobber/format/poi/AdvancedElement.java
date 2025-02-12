package com.patricktwohig.jobber.format.poi;

@FunctionalInterface
public interface AdvancedElement {

    void write();

    interface Builder<T extends AdvancedElement> {

        T build();

        default void buildAndWrite() {
            this.build().write();
        }

    }

}
