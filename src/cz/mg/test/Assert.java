package cz.mg.test;

import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.test.builders.BinaryObjectAssertion;
import cz.mg.test.builders.CodeAssertion;
import cz.mg.test.builders.UnaryObjectAssertion;
import cz.mg.test.functions.CompareFunction;
import cz.mg.test.functions.PrintFunction;

import java.util.Iterator;

public class Assert {
    public static @Mandatory <T> UnaryObjectAssertion<T> assertThat(T object) {
        return new UnaryObjectAssertion<>(object);
    }

    public static @Mandatory <T> BinaryObjectAssertion<T> assertThat(T expectation, T reality) {
        return new BinaryObjectAssertion<>(expectation, reality);
    }

    public static @Mandatory CodeAssertion assertThatCode(@Mandatory UnsafeRunnable runnable) {
        return new CodeAssertion(runnable);
    }

    @Deprecated
    public static void assertNull(@Optional Object object) {
        assertThat(object).isNull();
    }

    @Deprecated
    public static void assertNotNull(@Optional Object object) {
        assertThat(object).isNotNull();
    }

    @Deprecated
    public static void assertSame(@Optional Object expectation, @Optional Object reality) {
        assertThat(expectation, reality).areSame();
    }

    @Deprecated
    public static void assertNotSame(@Optional Object expectation, @Optional Object reality) {
        assertThat(expectation, reality).areNotSame();
    }

    @Deprecated
    public static void assertEquals(@Optional Object expectation, @Optional Object reality) {
        assertThat(expectation, reality).areEqual();
    }

    @Deprecated
    public static <T> void assertEquals(
        @Optional T expectation,
        @Optional T reality,
        @Mandatory CompareFunction<T> compareFunction,
        @Mandatory PrintFunction<T> printFunction
    ) {
        assertThat(expectation, reality)
            .withCompareFunction(compareFunction)
            .withPrintFunction(printFunction)
            .areEqual();
    }

    @Deprecated
    public static void assertExceptionThrown(@Mandatory UnsafeRunnable runnable) {
        assertThatCode(runnable).throwsException();
    }

    @Deprecated
    public static <E extends Exception> E assertExceptionThrown(
        @Mandatory Class<E> type,
        @Mandatory UnsafeRunnable runnable
    ) {
        return assertThatCode(runnable).throwsException(type);
    }

    @Deprecated
    public static void assertExceptionNotThrown(@Mandatory UnsafeRunnable runnable) {
        assertThatCode(runnable).doesNotThrowAnyException();
    }

    public static <T> void assertEquals(
        @Optional Iterable<T> expectation,
        @Optional Iterable<T> reality,
        @Mandatory CompareFunction<T> compareFunction,
        @Mandatory PrintFunction<T> PrintFunction
    ) {
        if (expectation == reality) {
            return;
        }

        if (expectation == null) {
            throw new AssertException("Expected list to be null, but was not null.");
        }

        if (reality == null) {
            throw new AssertException("Expected list to not be null, but was null.");
        }

        int expectedCount = count(expectation);
        int actualCount = count(reality);

        if (expectedCount != actualCount) {
            throw new AssertException("Expected list count to be " + expectedCount + ", but was " + actualCount + ".");
        }

        int i = -1;
        Iterator<T> expectationIterator = expectation.iterator();
        Iterator<T> realityIterator = reality.iterator();
        while (expectationIterator.hasNext() && realityIterator.hasNext()) {
            i++;
            @Optional T expectedItem = expectationIterator.next();
            @Optional T actualItem = realityIterator.next();

            if (expectedItem == actualItem) {
                continue;
            }

            if (expectedItem == null) {
                throw new AssertException(
                    "Expected item at " + i + " to be null, but was " + PrintFunction.toString(actualItem) + "."
                );
            }

            if (actualItem == null) {
                throw new AssertException(
                    "Expected item at " + i + " to be " + PrintFunction.toString(expectedItem) + ", but was null."
                );
            }

            if (!compareFunction.equals(expectedItem, actualItem)) {
                throw new AssertException(
                    "Expected item at " + i + " to be " + PrintFunction.toString(expectedItem) + ", " +
                        "but was " + PrintFunction.toString(actualItem) + "."
                );
            }
        }
    }

    private static <T> int count(Iterable<T> iterable) {
        int count = 0;
        for (T ignored : iterable) {
            count++;
        }
        return count;
    }
}
