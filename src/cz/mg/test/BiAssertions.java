package cz.mg.test;

import cz.mg.annotations.classes.Static;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.test.components.builders.BinaryCollectionAssertion;

/**
 * Assertions to compare two objects.
 */
public @Static class BiAssertions {
    public static @Mandatory <T> BinaryCollectionAssertion<T> assertThat(
        @Optional Iterable<T> expectation,
        @Optional Iterable<T> reality
    ) {
        return new BinaryCollectionAssertion<>(expectation, reality);
    }
}
