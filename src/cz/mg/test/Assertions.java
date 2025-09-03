package cz.mg.test;

import cz.mg.annotations.classes.Static;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.test.components.UnsafeRunnable;
import cz.mg.test.components.builders.FluentCodeAssertion;
import cz.mg.test.components.builders.FluentObjectAssertion;

/**
 * Fluent assertions.
 */
public @Static class Assertions {
    public static @Mandatory <T> FluentObjectAssertion<T> assertThat(@Optional T object) {
        return new FluentObjectAssertion<>(object);
    }

    public static @Mandatory FluentCodeAssertion assertThatCode(@Mandatory UnsafeRunnable runnable) {
        return new FluentCodeAssertion(runnable);
    }
}
