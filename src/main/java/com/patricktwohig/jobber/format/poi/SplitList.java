package com.patricktwohig.jobber.format.poi;

import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.util.function.Supplier;
import java.util.stream.Stream;

public interface SplitList extends AdvancedElement {

    class Builder implements AdvancedElement.Builder<SplitList> {

        private Supplier<XWPFRun> delimterSupplier = () -> {
            throw new IllegalStateException("Delimiter not set.");
        };

        private Stream<Supplier<XWPFRun>> runStream = Stream.empty();

        public Builder withRuns(Stream<Supplier<XWPFRun>> runs) {
            this.runStream = Stream.concat(runStream, runs);
            return this;
        }

        public Builder withDelimiter(Supplier<XWPFRun> delimterSupplier) {
            this.delimterSupplier = delimterSupplier;
            return this;
        }

        public SplitList build() {
            return () -> {

                final var iterator = runStream.iterator();

                while (iterator.hasNext()) {

                    iterator.next().get();

                    if (iterator.hasNext()) {
                        delimterSupplier.get();
                    }

                }

            };
        }

    }

}
