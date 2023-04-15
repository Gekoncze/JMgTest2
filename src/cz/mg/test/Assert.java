package cz.mg.test;

import cz.mg.annotations.classes.Static;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.test.builders.BinaryCollectionAssertion;
import cz.mg.test.builders.BinaryObjectAssertion;
import cz.mg.test.builders.CodeAssertion;

public @Static class Assert {
    public static @Mandatory <T> BinaryObjectAssertion<T> assertThat(@Optional T expectation, @Optional T reality) {
        return new BinaryObjectAssertion<>(expectation, reality);
    }

    public static @Mandatory CodeAssertion assertThatCode(@Mandatory UnsafeRunnable runnable) {
        return new CodeAssertion(runnable);
    }

    public static @Mandatory <T> BinaryCollectionAssertion<T> assertThatCollections(
        @Optional Iterable<T> expectation,
        @Optional Iterable<T> reality
    ) {
        return new BinaryCollectionAssertion<>(expectation, reality);
    }

    public static void assertNull(@Optional Object object) {
        if (object != null) {
            throw new AssertException("Expected null, but got " + object + ".");
        }
    }

    public static void assertNotNull(@Optional Object object) {
        if (object == null) {
            throw new AssertException("Unexpected null value.");
        }
    }

    public static void assertSame(@Optional Object expectation, @Optional Object reality) {
        assertThat(expectation, reality).areSame();
    }

    public static void assertNotSame(@Optional Object expectation, @Optional Object reality) {
        assertThat(expectation, reality).areNotSame();
    }

    public static void assertEquals(@Optional Object expectation, @Optional Object reality) {
        assertThat(expectation, reality).areEqual();
    }

    public static void assertNotEquals(@Optional Object expectation, @Optional Object reality) {
        assertThat(expectation, reality).areNotEqual();
    }
}
