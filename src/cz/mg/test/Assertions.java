package cz.mg.test;

import cz.mg.annotations.classes.Static;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.test.builders.FluentCodeAssertion;
import cz.mg.test.builders.FluentCollectionAssertion;
import cz.mg.test.builders.FluentObjectAssertion;
import cz.mg.test.functions.UnsafeRunnable;

/**
 * Fluent assertions.
 */
public @Static class Assertions {
    public static @Mandatory <T> FluentObjectAssertion<T> assertThat(@Optional T object) {
        return new FluentObjectAssertion<>(object);
    }

    public static @Mandatory <T>FluentCollectionAssertion<T> assertThatCollection(@Optional Iterable<T> collection) {
        return new FluentCollectionAssertion<>(collection);
    }

    public static @Mandatory FluentCodeAssertion assertThatCode(@Mandatory UnsafeRunnable runnable) {
        return new FluentCodeAssertion(runnable);
    }
}
