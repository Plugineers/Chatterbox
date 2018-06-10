package com.patrickanker.chatterbox.util;

// From https://stackoverflow.com/questions/23773024/functional-style-of-java-8s-optional-ifpresent-and-if-not-present

import java.util.Optional;
import java.util.function.Consumer;

public class OptionalConsumer<T> {
    private Optional<T> optional;

    private OptionalConsumer(Optional<T> opt) {
        this.optional = opt;
    }

    public static <T> OptionalConsumer<T> of(Optional<T> opt) {
        return new OptionalConsumer<>(opt);
    }

    public OptionalConsumer<T> ifPresent(Consumer<T> c) {
        optional.ifPresent(c);
        return this;
    }

    public OptionalConsumer<T> ifNotPresent(Runnable r) {
        if (!isPresent()) {
            r.run();
        }

        return this;
    }

    public boolean isPresent() {
        return optional.isPresent();
    }

    public T get() {
        return optional.get();
    }
}

