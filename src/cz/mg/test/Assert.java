package cz.mg.test;

import cz.mg.annotations.classes.Static;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.test.builders.FluentCodeAssertion;
import cz.mg.test.exceptions.AssertException;
import cz.mg.test.functions.UnsafeRunnable;

/**
 * Quick assertions using a single method call.
 */
public @Static class Assert {
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
        Assertions.assertThat(reality).isSameAs(expectation);
    }

    public static void assertNotSame(@Optional Object expectation, @Optional Object reality) {
        Assertions.assertThat(reality).isNotSameAs(expectation);
    }

    public static void assertEquals(@Optional Object expectation, @Optional Object reality) {
        Assertions.assertThat(reality).isEqualTo(expectation);
    }

    public static void assertNotEquals(@Optional Object expectation, @Optional Object reality) {
        Assertions.assertThat(reality).isNotEqualTo(expectation);
    }

    public static void assertException(@Mandatory UnsafeRunnable runnable, @Mandatory Class<? extends Exception> type) {
        new FluentCodeAssertion(runnable).throwsException(type);
    }

    public static void assertNoException(@Mandatory UnsafeRunnable runnable) {
        new FluentCodeAssertion(runnable).doesNotThrowAnyException();
    }
}
