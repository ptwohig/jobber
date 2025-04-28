package com.patricktwohig.jobber.model;

import java.util.LinkedList;
import java.util.Optional;

public class UndoStack<ValueT> {

    private static final Runnable NOOP = () -> {};

    private static final int DEFAULT_MAX_SIZE = 256;

    private final int maxSize;

    private final LinkedList<UndoOperationRecord<ValueT>> stack = new LinkedList<>();

    public UndoStack() {
        this(DEFAULT_MAX_SIZE);
    }

    public UndoStack(int maxSize) {
        this.maxSize = maxSize;
    }

    public ValueT push(final ValueT value) {
        return push(value, NOOP);
    }

    public ValueT push(final ValueT value, final Runnable action) {

        if (stack.size() >= getMaxSize()) {
            final var operation = stack.removeFirst();
            operation.action().run();
        }

        stack.addLast(new UndoOperationRecord<>(value, action));
        return value;

    }

    public Optional<ValueT> pop(final int count) {

        if (count <= 0 && !stack.isEmpty()) {
            return Optional.of(stack.getLast().value());
        }

        Optional<ValueT> result = Optional.empty();

        for (int i = 0; i < count && !stack.isEmpty(); i++) {

            final var record = stack.removeLast();
            record.action.run();

            result = Optional
                    .ofNullable(stack.pollLast())
                    .map(UndoOperationRecord::value);

        }

        return result;

    }

    public int getMaxSize() {
        return maxSize;
    }

    private record UndoOperationRecord<ValueT>(ValueT value, Runnable action) {}

}
