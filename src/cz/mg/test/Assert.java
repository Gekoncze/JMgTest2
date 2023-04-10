package cz.mg.test;

import cz.mg.annotations.classes.Static;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.test.builders.BinaryObjectAssertion;
import cz.mg.test.builders.CodeAssertion;
import cz.mg.test.builders.CollectionAssertion;
import cz.mg.test.builders.UnaryObjectAssertion;
import cz.mg.test.functions.CompareFunction;
import cz.mg.test.functions.PrintFunction;

public @Static class Assert {
    public static @Mandatory <T> UnaryObjectAssertion<T> assertThat(@Optional T object) {
        return new UnaryObjectAssertion<>(object);
    }

    public static @Mandatory <T> BinaryObjectAssertion<T> assertThat(@Optional T expectation, @Optional T reality) {
        return new BinaryObjectAssertion<>(expectation, reality);
    }

    public static @Mandatory CodeAssertion assertThatCode(@Mandatory UnsafeRunnable runnable) {
        return new CodeAssertion(runnable);
    }

    public static @Mandatory <T> CollectionAssertion<T> assertThatCollection(
        @Optional Iterable<T> expectation,
        @Optional Iterable<T> reality
    ) {
        return new CollectionAssertion<>(expectation, reality);
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

    @Deprecated
    public static <T> void assertEquals(
        @Optional Iterable<T> expectation,
        @Optional Iterable<T> reality,
        @Mandatory CompareFunction<T> compareFunction,
        @Mandatory PrintFunction<T> printFunction
    ) {
        assertThatCollection(expectation, reality)
            .withCompareFunction(compareFunction)
            .withPrintFunction(printFunction)
            .areEqual();
    }
}
